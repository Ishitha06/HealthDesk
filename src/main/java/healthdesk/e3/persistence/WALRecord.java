
package healthdesk.e3.persistence;

public class WALRecord {

    public enum Operation {
        PUT,
        DELETE
    }

    private final long lsn;
    private final Operation operation;
    private final String key;
    private final String value;

    public WALRecord(long lsn, Operation operation,
                     String key, String value) {

        if (lsn <= 0) {
            throw new IllegalArgumentException("LSN must be positive");
        }

        if (operation == null) {
            throw new IllegalArgumentException("Operation cannot be null");
        }

        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Key cannot be empty");
        }

        if (operation == Operation.PUT && value == null) {
            throw new IllegalArgumentException("PUT requires a value");
        }

        this.lsn = lsn;
        this.operation = operation;
        this.key = key;
        this.value = value;
    }

    public long getLsn() {
        return lsn;
    }

    public Operation getOperation() {
        return operation;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
