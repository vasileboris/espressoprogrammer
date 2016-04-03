package com.espressoprogrammer.references;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Scope(value= WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ReferencesService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MemoryUsageDAO memoryUsageDAO;

    @Autowired
    private ReferencesCountDAO referencesCountDAO;

    @Autowired
    private ChartService chartService;

    private AtomicInteger softReferredCount;
    private AtomicInteger weakReferredCount;
    private AtomicInteger phantomReferredCount;
    private List<Referred> hardReferences;
    private List<SoftReference<Referred>> softReferences;
    private List<WeakReference<Referred>> weakReferences;
    private List<PhantomReference<Referred>> phantomReferences;
    private List<String> heap;
    private ReferenceQueue<Referred> referredQueue;
    private long startToKeepInMemoryHardReferences;

    private int referencesCountMax;
    private int insertMoreReferencesMax;
    private int millisToKeepInMemoryHardReferences;

    public byte[] execute(int referencesCountMax, int insertMoreReferencesMax, int millisToKeepInMemoryHardReferences) {
        logger.info("Creating references");

        this.referencesCountMax = referencesCountMax;
        this.insertMoreReferencesMax = insertMoreReferencesMax;
        this.millisToKeepInMemoryHardReferences = millisToKeepInMemoryHardReferences;

        clearHeap();
        prepareForNewExecution();

        insertChartData();
        createReferences();
        consumeHeap();

        clearHeap();
        return chartService.generateChartImage();
    }

    private void clearHeap() {
        heap = new LinkedList<>();
    }

    private void prepareForNewExecution() {
        softReferredCount = new AtomicInteger(0);
        weakReferredCount = new AtomicInteger(0);
        phantomReferredCount = new AtomicInteger(0);
        hardReferences = new LinkedList<>();
        softReferences = new LinkedList<>();
        weakReferences = new LinkedList<>();
        phantomReferences = new LinkedList<>();
        referredQueue = new ReferenceQueue<>();
        memoryUsageDAO.deleteAll();
        referencesCountDAO.deleteAll();
    }

    private void insertChartData() {
        memoryUsageDAO.insert(new MemoryUsage());
        referencesCountDAO.insert(new ReferencesCount(hardReferences.size(),
                softReferredCount.intValue(),
                weakReferredCount.intValue(),
                phantomReferredCount.intValue()));
    }

    private void createReferences() {
        createReferences(
                () -> {
                    Referred referred = new Referred("weak", weakReferredCount);
                    weakReferences.add(new WeakReference<>(referred));
                    insertChartData();
                    return referred;
                },
                () -> {
                    Referred referred = new Referred("soft", softReferredCount);
                    softReferences.add(new SoftReference<>(referred));
                    insertChartData();
                    return referred;
                },
                () -> {
                    Referred referred = new Referred("phantom", phantomReferredCount);
                    phantomReferences.add(new PhantomReference<>(referred, referredQueue));
                    insertChartData();
                    return referred;
                });
        startToKeepInMemoryHardReferences = System.currentTimeMillis();
    }

    @SafeVarargs
    private final void createReferences(ReferenceCreator<Referred>... creators) {
        Map<Integer, Integer> referencesCounters = new HashMap<>();
        for(int i=0; i<creators.length; i++) {
            referencesCounters.put(i, 0);
        }

        Random random = new Random(System.currentTimeMillis());
        while(referencesCounters.values().stream().reduce(0, Integer::sum) < referencesCountMax * creators.length) {
            int idx = random.nextInt(creators.length);
            if(referencesCounters.get(idx) < referencesCountMax) {
                hardReferences.add(creators[idx].create());
                referencesCounters.put(idx, referencesCounters.get(idx)+1);
            }

            sleepMilliseconds(250);
        }

        sleepMilliseconds(1000);
    }

    private void consumeHeap() {
        logger.info("Start consuming heap");
        try {
            int insertMoreReferences = 0;
            while(stillHaveReferences()) {
                if(isRoomForMoreReferences(insertMoreReferences) && wereWeakAndPhantomReferencesGarbageCollected()) {
                    logger.info("Creating more references step {}", insertMoreReferences);
                    createReferences();
                    insertMoreReferences++;
                } else {
                    addObjectsInHeap();
                    insertChartData();
                }

                if(isElapsedTimeToKeepHardRefencencesInMemory()) {
                    logger.info("Releasing hard references after {} seconds", TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startToKeepInMemoryHardReferences));
                    hardReferences.clear();
                    sleepMilliseconds(1000);
                }
            }
        }
        catch (OutOfMemoryError e) {
            logger.error("Out of memory error raised", e);
        }

        logger.info("Stop consuming heap");
    }

    private boolean stillHaveReferences() {
        return softReferredCount.intValue() > 0 || weakReferredCount.intValue() > 0;
    }

    private boolean wereWeakAndPhantomReferencesGarbageCollected() {
        return weakReferredCount.intValue() == 0 && phantomReferredCount.intValue() ==0;
    }

    private void addObjectsInHeap() {
        for(int i=0; i< 10; i++) {
            heap.add(UUID.randomUUID().toString());
        }
    }

    private boolean isElapsedTimeToKeepHardRefencencesInMemory() {
        return System.currentTimeMillis() - startToKeepInMemoryHardReferences > millisToKeepInMemoryHardReferences && !hardReferences.isEmpty();
    }

    private boolean isRoomForMoreReferences(int insertMoreReferences) {
        return insertMoreReferences < insertMoreReferencesMax;
    }

    private void sleepMilliseconds(int milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {}
    }

}
