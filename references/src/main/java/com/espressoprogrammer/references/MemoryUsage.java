package com.espressoprogrammer.references;

import java.sql.Time;

public class MemoryUsage {
    private static final long MEGABYTE = 1024 * 1024;

    private Time created;
    private long totalMemory;
    private long freeMemory;

    public MemoryUsage() {
        this.created = new Time(System.currentTimeMillis());
        this.totalMemory = Runtime.getRuntime().totalMemory() / MEGABYTE;
        this.freeMemory = Runtime.getRuntime().freeMemory() / MEGABYTE;
    }

    public MemoryUsage(Time created, long totalMemory, long freeMemory) {
        this.created = created;
        this.totalMemory = totalMemory;
        this.freeMemory = freeMemory;
    }

    public Time getCreated() {
        return created;
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    public long getFreeMemory() {
        return freeMemory;
    }

    @Override
    public String toString() {
        return "MemoryUsage{" +
                "created=" + created +
                ", totalMemory=" + totalMemory +
                ", freeMemory=" + freeMemory +
                '}';
    }

}
