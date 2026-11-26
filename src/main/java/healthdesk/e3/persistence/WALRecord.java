
package healthdesk.e3.persistence;

/**
 * Represents a single state-change entry in the Write-Ahead Log.
 *
 * Each record contains an LSN, operation type, key and optional value.
 * Records are used to reconstruct the in-memory state during recovery.
 */
public class WALRecord {

    /**
     * Defines the supported operations recorded in the WAL.
     */
    public enum Operation {
        PUT,
        DELETE
    }

    private final long lsn;
    private final Operation operation;
    private final String key;
    private final String value;

    /**
     * Creates a WAL record after validating its required fields.
     *
     * @param lsn sequence number assigned to the operation
     * @param operation type of state modification
     * @param key key identifying the state entry
     * @param value value associated with a PUT operation
     * @throws IllegalArgumentException if the record contains invalid data
     */
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

    /**
     * Returns the sequence number of this WAL record.
     *
     * @return record LSN
     */
    public long getLsn() {
        return lsn;
    }

    /**
     * Returns the operation represented by this record.
     *
     * @return PUT or DELETE operation
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     * Returns the key affected by this operation.
     *
     * @return state key
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the value associated with this record.
     * DELETE records may have a null value.
     *
     * @return stored value, or null for a DELETE record
     */
    public String getValue() {
        return value;
    }
}
