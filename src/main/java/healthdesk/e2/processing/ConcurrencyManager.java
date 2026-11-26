package healthdesk.e2.processing;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Manages synchronized access to shared HealthDesk state.
 */
public class ConcurrencyManager {

    private final ReentrantReadWriteLock lock =
            new ReentrantReadWriteLock(true);

    /**
     * Acquires the read lock.
     */
    public void acquireReadLock() {
        lock.readLock().lock();
    }

    /**
     * Releases the read lock.
     */
    public void releaseReadLock() {
        lock.readLock().unlock();
    }

    /**
     * Acquires the write lock.
     */
    public void acquireWriteLock() {
        lock.writeLock().lock();
    }

    /**
     * Releases the write lock.
     */
    public void releaseWriteLock() {
        lock.writeLock().unlock();
    }
}