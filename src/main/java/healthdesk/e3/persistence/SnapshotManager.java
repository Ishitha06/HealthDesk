
package healthdesk.e3.persistence;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handles creation and loading of persistent snapshots.
 *
 * Snapshots store a copy of the key-value state at a specific LSN,
 * allowing recovery to start from a saved state instead of replaying
 * the entire WAL history.
 */
public class SnapshotManager {

    private final Path snapshotDirectory;

    /**
     * Creates a snapshot manager using the default snapshot directory.
     */
    public SnapshotManager() {
        this(Paths.get("data", "snapshots"));
    }

    /**
     * Creates a snapshot manager for a specified directory.
     *
     * @param snapshotDirectory directory in which snapshots are stored
     * @throws IllegalArgumentException if the directory is null
     */
    public SnapshotManager(Path snapshotDirectory) {
        if (snapshotDirectory == null) {
            throw new IllegalArgumentException(
                    "Snapshot directory cannot be null");
        }

        this.snapshotDirectory = snapshotDirectory;
    }

    /**
     * Persists the supplied state as a snapshot associated with an LSN.
     *
     * Writes to a temporary file, forces the contents to storage,
     * and then replaces the target snapshot file.
     *
     * @param snapshotLSN LSN representing the captured state
     * @param state key-value state to persist
     * @return path of the completed snapshot file
     * @throws IOException if snapshot file operations fail
     * @throws IllegalArgumentException if the LSN or state is invalid
     */
    public Path saveSnapshot(
            long snapshotLSN,
            Map<String, String> state) throws IOException {

        if (snapshotLSN < 0 || state == null) {
            throw new IllegalArgumentException(
                    "Invalid snapshot LSN or state");
        }

        // Create the snapshot directory if it does not exist.
        Files.createDirectories(snapshotDirectory);

        // Generate the final and temporary file paths.
        Path snapshotPath = snapshotDirectory.resolve(
                "snapshot-" + snapshotLSN + ".snap");

        Path tempPath = snapshotDirectory.resolve(
                "snapshot-" + snapshotLSN + ".tmp");

        try {
            // Write the snapshot to a temporary file first.
            try (FileChannel channel = FileChannel.open(
                    tempPath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE)) {

                // Header identifies the file format and snapshot LSN.
                writeLine(channel, "SNAPSHOT|1|" + snapshotLSN);

                // Persist each state entry as an encoded PUT record.
                for (Map.Entry<String, String> entry
                        : state.entrySet()) {

                    if (entry.getKey() == null
                            || entry.getValue() == null) {
                        throw new IllegalArgumentException(
                                "Snapshot keys and values cannot be null");
                    }

                    String key = encode(entry.getKey());
                    String value = encode(entry.getValue());

                    writeLine(channel, "PUT|" + key + "|" + value);
                }

                // Force snapshot data to storage before publishing the file.
                channel.force(true);
            }

            // Replace the final snapshot with the completed temporary file.
            try {
                Files.move(
                        tempPath,
                        snapshotPath,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                // Use a non-atomic replacement if the filesystem
                // does not support atomic moves.
                Files.move(
                        tempPath,
                        snapshotPath,
                        StandardCopyOption.REPLACE_EXISTING);
            }

            return snapshotPath;

        } finally {
            // Remove any leftover temporary file after success or failure.
            Files.deleteIfExists(tempPath);
        }
    }

    /**
     * Loads a snapshot file and reconstructs its key-value state.
     *
     * Validates the header, snapshot LSN, record structure,
     * Base64 encoding and duplicate keys.
     *
     * @param snapshotPath path of the snapshot to load
     * @return parsed snapshot data
     * @throws IOException if the snapshot is missing, malformed or unreadable
     */
    public SnapshotData loadSnapshot(Path snapshotPath)
            throws IOException {

        Map<String, String> state = new LinkedHashMap<>();

        // Read all snapshot lines using UTF-8 encoding.
        var lines = Files.readAllLines(
                snapshotPath, StandardCharsets.UTF_8);

        if (lines.isEmpty()) {
            throw new IOException("Snapshot file is empty");
        }

        // Validate the snapshot header and file-format version.
        String[] header = lines.get(0).split("\\|", -1);

        if (header.length != 3
                || !header[0].equals("SNAPSHOT")
                || !header[1].equals("1")) {
            throw new IOException("Invalid snapshot header");
        }

        // Extract and validate the snapshot LSN.
        long snapshotLSN;

        try {
            snapshotLSN = Long.parseLong(header[2]);

            if (snapshotLSN < 0) {
                throw new IOException("Snapshot LSN cannot be negative");
            }

        } catch (NumberFormatException e) {
            throw new IOException("Invalid snapshot LSN", e);
        }

        // Parse each entry after the header.
        for (int i = 1; i < lines.size(); i++) {

            String[] parts = lines.get(i).split("\\|", -1);

            if (parts.length != 3 || !parts[0].equals("PUT")) {
                throw new IOException(
                        "Invalid snapshot record at line " + (i + 1));
            }

            try {
                String key = decode(parts[1]);
                String value = decode(parts[2]);

                // Reject blank or duplicate keys to avoid ambiguous state.
                if (key.isBlank() || state.containsKey(key)) {
                    throw new IOException(
                            "Invalid or duplicate snapshot key");
                }

                state.put(key, value);

            } catch (IllegalArgumentException e) {
                throw new IOException(
                        "Invalid encoded data at line " + (i + 1), e);
            }
        }

        // Return the parsed state together with its recovery LSN.
        return new SnapshotData(snapshotLSN, state);
    }

    /**
     * Writes a UTF-8 line to the supplied file channel.
     *
     * @param channel destination file channel
     * @param line text to write
     * @throws IOException if the write fails
     */
    private void writeLine(FileChannel channel, String line)
            throws IOException {

        ByteBuffer buffer = ByteBuffer.wrap(
                (line + "\n").getBytes(StandardCharsets.UTF_8));

        // Write the complete line even if the channel writes partially.
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }

    /**
     * Encodes a string as Base64 using UTF-8.
     *
     * @param value original text
     * @return encoded text
     */
    private String encode(String value) {
        return Base64.getEncoder().encodeToString(
                value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decodes Base64 data into a UTF-8 string.
     *
     * @param value encoded text
     * @return decoded string
     */
    private String decode(String value) {
        return new String(
                Base64.getDecoder().decode(value),
                StandardCharsets.UTF_8);
    }
}
