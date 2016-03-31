package com.espressoprogrammer.references;

import java.sql.Time;

public class ReferencesCount {

    private Time created;
    private int softReferencesCount;
    private int weakReferencesCount;
    private int phantomReferencesCount;

    public ReferencesCount(int softReferencesCount, int weakReferencesCount, int phantomReferencesCount) {
        this.created = new Time(System.currentTimeMillis());
        this.softReferencesCount = softReferencesCount;
        this.weakReferencesCount = weakReferencesCount;
        this.phantomReferencesCount = phantomReferencesCount;
    }

    public ReferencesCount(Time created, int softReferencesCount, int weakReferencesCount, int phantomReferencesCount) {
        this.created = created;
        this.softReferencesCount = softReferencesCount;
        this.weakReferencesCount = weakReferencesCount;
        this.phantomReferencesCount = phantomReferencesCount;
    }

    public Time getCreated() {
        return created;
    }

    public int getSoftReferencesCount() {
        return softReferencesCount;
    }

    public int getWeakReferencesCount() {
        return weakReferencesCount;
    }

    public int getPhantomReferencesCount() {
        return phantomReferencesCount;
    }

    @Override
    public String toString() {
        return "ReferencesCount{" +
                "created=" + created +
                ", softReferencesCount=" + softReferencesCount +
                ", weakReferencesCount=" + weakReferencesCount +
                ", phantomReferencesCount=" + phantomReferencesCount +
                '}';
    }
}
