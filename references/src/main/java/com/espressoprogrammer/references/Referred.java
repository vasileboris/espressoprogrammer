package com.espressoprogrammer.references;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class Referred {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String referenceType;
    private AtomicInteger referencesCount;

    public Referred(String referenceType, AtomicInteger referencesCount) {
        logger.info("Create new " + referenceType + " instance");
        this.referenceType = referenceType;
        this.referencesCount = referencesCount;
        this.referencesCount.incrementAndGet();
    }

    protected void finalize() {
        logger.info("Release " + referenceType + " instance");
        referencesCount.decrementAndGet();
    }

}
