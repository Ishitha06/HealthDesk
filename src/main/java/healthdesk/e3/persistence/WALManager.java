
package healthdesk.e3.persistence;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages Write-Ahead Log (WAL) file operations for HealthDesk.
 *
 * Responsible for appending state-change records to persistent storage
 * and reading previously stored records for recovery.
 */
public class WALManager {

    private final LSNManager lsnManager;
    private final Path walPath;

    // Serializes LSN assignment and file writes to preserve WAL ordering.
    private final ReentrantLock writeLock = new ReentrantLock();

    /**
     * Creates a WAL manager using the default WAL file location.
     */
    public WALManager() {
        this(new LSNManager(), Paths.get("data", "wal.log"));
    }

    /**
     * Creates a WAL manager with injected LSN management and file path.
     *
     * @param lsnManager manager responsible for generating LSNs
     * @param walPath path of the WAL file
     * @throws IllegalArgumentException if either argument is null
     */
    public WALManager(LSNManager lsnManager, Path walPath) {
        if (lsnManager == null || walPath == null) {
            throw new IllegalArgumentException(
                    "LSNManager and WAL path cannot be null");
        }

        this.lsnManager = lsnManager;
        this.walPath = walPath;
    }

    /**
     * Appends a state-change record to the WAL.
     *
     * Validates the operation, assigns an LSN, encodes the record,
     * writes it to the WAL file and forces the write to storage
     * before returning.
     *
     * @param operation PUT or DELETE operation
     * @param key key affected by the operation
     * @param value value for PUT; null for DELETE
     * @return LSN assigned to the persisted record
     * @throws IOException if the WAL cannot be written
     * @throws IllegalArgumentException if the operation or data is invalid
     */
    public long append(WALRecord.Operation operation,
                       String key, String value) throws IOException {

        // Validate the request before allocating a sequence number.
        if (operation == null) {
            throw new IllegalArgumentException("Operation cannot be null");
        }

        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Key cannot be empty");
        }

        if (operation == WALRecord.Operation.PUT && value == null) {
            throw new IllegalArgumentException("PUT requires a value");
        }

        writeLock.lock();

        try {
            // Assign the sequence number while holding the write lock.
            long lsn = lsnManager.nextLSN();

            // Build a validated WAL record for the state change.
            WALRecord record =
                    new WALRecord(lsn, operation, key, value);

            // Base64 encoding protects the record structure from delimiters
            // contained within the original key or value.
            String encodedKey = encode(record.getKey());

            String encodedValue = record.getValue() == null
                    ? "-"
                    : encode(record.getValue());

            // Each WAL entry is stored as a pipe-delimited text line.
            String line = record.getLsn() + "|"
                    + record.getOperation() + "|"
                    + encodedKey + "|"
                    + encodedValue + "\n";

            // Ensure the parent directory exists before opening the file.
            Path parent = walPath.toAbsolutePath().getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            // Append the record to the WAL file.
            try (FileChannel channel = FileChannel.open(
                    walPath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.APPEND)) {

                ByteBuffer buffer = ByteBuffer.wrap(
                        line.getBytes(StandardCharsets.UTF_8));

                // Continue writing until the complete record is written.
                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }

                // Force file data and metadata to storage before returning.
                channel.force(true);
            }

            // The caller receives the LSN after the WAL write completes.
            return lsn;

        } finally {
            // Always release the lock, including when file I/O fails.
            writeLock.unlock();
        }
    }

    /**
     * Encodes a string into Base64 using UTF-8.
     *
     * @param value string to encode
     * @return Base64 encoded string
     */
    private String encode(String value) {
        return Base64.getEncoder().encodeToString(
                value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decodes a Base64 string into UTF-8 text.
     *
     * @param value encoded string
     * @return decoded text
     * @throws IOException if the input contains invalid Base64 data
     */
    private String decode(String value) throws IOException {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(value);

            return new String(decodedBytes, StandardCharsets.UTF_8);

        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid Base64 data in WAL", e);
        }
    }

    /**
     * Reads and decodes all validly formatted records from the WAL file.
     *
     * If the WAL file does not exist, an empty record list is returned.
     *
     * @return decoded WAL records in file order
     * @throws IOException if the file cannot be read or a record is malformed
     */
    public List<WALRecord> readAllRecords() throws IOException {

        List<WALRecord> records = new ArrayList<>();

        // A missing WAL means there are currently no records to replay.
        if (!Files.exists(walPath)) {
            return records;
        }

        // Read the WAL as UTF-8 text, one record per line.
        List<String> lines = Files.readAllLines(
                walPath,
                StandardCharsets.UTF_8);

        for (String line : lines) {

            // Ignore empty lines.
            if (line.isBlank()) {
                continue;
            }

            // Split the record into its four expected fields.
            String[] parts = line.split("\\|", -1);

            if (parts.length != 4) {
                throw new IOException("Invalid WAL record format");
            }

            // Parse the sequence number.
            long lsn;

            try {
                lsn = Long.parseLong(parts[0]);
            } catch (NumberFormatException e) {
                throw new IOException("Invalid LSN in WAL", e);
            }

            // Parse the operation type.
            WALRecord.Operation operation;

            try {
                operation = WALRecord.Operation.valueOf(parts[1]);
            } catch (IllegalArgumentException e) {
                throw new IOException("Invalid WAL operation", e);
            }

            // Decode the key and optional value.
            String key = decode(parts[2]);

            String value = parts[3].equals("-")
                    ? null
                    : decode(parts[3]);

            // Reconstruct the record object from the stored fields.
            records.add(new WALRecord(lsn, operation, key, value));
        }

        return records;
    }
}
