
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

public class WALManager {

    private final LSNManager lsnManager;
    private final Path walPath;

    // One lock coordinates LSN assignment and WAL writing.
    private final ReentrantLock writeLock = new ReentrantLock();

    public WALManager() {
        this(new LSNManager(), Paths.get("data", "wal.log"));
    }

    public WALManager(LSNManager lsnManager, Path walPath) {
        if (lsnManager == null || walPath == null) {
            throw new IllegalArgumentException(
                    "LSNManager and WAL path cannot be null");
        }

        this.lsnManager = lsnManager;
        this.walPath = walPath;
    }

    public long append(WALRecord.Operation operation,
                       String key, String value) throws IOException {

        // Validate before assigning an LSN.
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
            // 1. Assign the next LSN.
            long lsn = lsnManager.nextLSN();

            // 2. Create the WAL record.
            WALRecord record =
                    new WALRecord(lsn, operation, key, value);

            // 3. Encode the record into one line.
            String encodedKey = encode(record.getKey());

            String encodedValue = record.getValue() == null
                    ? "-"
                    : encode(record.getValue());

            String line = record.getLsn() + "|"
                    + record.getOperation() + "|"
                    + encodedKey + "|"
                    + encodedValue + "\n";

            // 4. Create the WAL directory if necessary.
            Path parent = walPath.toAbsolutePath().getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            // 5. Append the record and force it to storage.
            try (FileChannel channel = FileChannel.open(
                    walPath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.APPEND)) {

                ByteBuffer buffer = ByteBuffer.wrap(
                        line.getBytes(StandardCharsets.UTF_8));

                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }

                channel.force(true);
            }

            // 6. Return only after the write succeeds.
            return lsn;

        } finally {
            writeLock.unlock();
        }
    }

    private String encode(String value) {
        return Base64.getEncoder().encodeToString(
                value.getBytes(StandardCharsets.UTF_8));
    }
}
