package com.espressoprogrammer.references;

import java.sql.Time;

public class ReferencesCount {

    private Time created;
    private int hardReferencesCount;
    private int softReferredCount;
    private int weakReferredCount;
    private int phantomReferredCount;

    public ReferencesCount(int hardReferencesCount, int softReferredCount, int weakReferredCount, int phantomReferredCount) {
        this.created = new Time(System.currentTimeMillis());
        this.hardReferencesCount = hardReferencesCount;
        this.softReferredCount = softReferredCount;
        this.weakReferredCount = weakReferredCount;
        this.phantomReferredCount = phantomReferredCount;
    }

    public ReferencesCount(Time created, int hardReferencesCount, int softReferredCount, int weakReferredCount, int phantomReferredCount) {
        this.created = created;
        this.hardReferencesCount = hardReferencesCount;
        this.softReferredCount = softReferredCount;
        this.weakReferredCount = weakReferredCount;
        this.phantomReferredCount = phantomReferredCount;
    }

    public Time getCreated() {
        return created;
    }

    public int getHardReferencesCount() {
        return hardReferencesCount;
    }

    public int getSoftReferredCount() {
        return softReferredCount;
    }

    public int getWeakReferredCount() {
        return weakReferredCount;
    }

    public int getPhantomReferredCount() {
        return phantomReferredCount;
    }

    @Override
    public String toString() {
        return "ReferencesCount{" +
                "created=" + created +
                ", hardReferencesCount=" + hardReferencesCount +
                ", softReferredCount=" + softReferredCount +
                ", weakReferredCount=" + weakReferredCount +
                ", phantomReferredCount=" + phantomReferredCount +
                '}';
    }
}
