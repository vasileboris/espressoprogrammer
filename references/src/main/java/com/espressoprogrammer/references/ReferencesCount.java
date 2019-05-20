package com.espressoprogrammer.references;

import java.sql.Time;

public class ReferencesCount {

    private Time created;
    private int hardReferencesCount;
    private int softReferentsCount;
    private int weakReferentsCount;
    private int phantomReferentsCount;

    public ReferencesCount(int hardReferencesCount, int softReferentsCount, int weakReferentsCount, int phantomReferentsCount) {
        this.created = new Time(System.currentTimeMillis());
        this.hardReferencesCount = hardReferencesCount;
        this.softReferentsCount = softReferentsCount;
        this.weakReferentsCount = weakReferentsCount;
        this.phantomReferentsCount = phantomReferentsCount;
    }

    public ReferencesCount(Time created, int hardReferencesCount, int softReferentsCount, int weakReferentsCount, int phantomReferentsCount) {
        this.created = created;
        this.hardReferencesCount = hardReferencesCount;
        this.softReferentsCount = softReferentsCount;
        this.weakReferentsCount = weakReferentsCount;
        this.phantomReferentsCount = phantomReferentsCount;
    }

    public Time getCreated() {
        return created;
    }

    public int getHardReferencesCount() {
        return hardReferencesCount;
    }

    public int getSoftReferentsCount() {
        return softReferentsCount;
    }

    public int getWeakReferentsCount() {
        return weakReferentsCount;
    }

    public int getPhantomReferentsCount() {
        return phantomReferentsCount;
    }

    @Override
    public String toString() {
        return "ReferencesCount{" +
                "created=" + created +
                ", hardReferencesCount=" + hardReferencesCount +
                ", softReferentsCount=" + softReferentsCount +
                ", weakReferentsCount=" + weakReferentsCount +
                ", phantomReferentsCount=" + phantomReferentsCount +
                '}';
    }
}
