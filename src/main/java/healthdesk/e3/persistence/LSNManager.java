package healthdesk.e3.persistence;

import java.util.concurrent.atomic.AtomicLong;

public class LSNManager {

    private final AtomicLong currentLSN;

    public LSNManager() {
        this(0);
    }

    public LSNManager(long initialLSN) {
        if (initialLSN < 0) {
            throw new IllegalArgumentException("LSN cannot be negative");
        }

        this.currentLSN = new AtomicLong(initialLSN);
    }

    public long nextLSN() {
        return currentLSN.incrementAndGet();
    }

    public long getCurrentLSN() {
        return currentLSN.get();
    }
}