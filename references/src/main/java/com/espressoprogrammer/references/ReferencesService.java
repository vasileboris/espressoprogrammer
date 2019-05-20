package com.espressoprogrammer.references;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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

    private AtomicInteger softReferentCount;
    private AtomicInteger weakReferentCount;
    private AtomicInteger phantomReferentCount;
    private List<Referent> hardReferences;
    private List<SoftReference<Referent>> softReferences;
    private List<WeakReference<Referent>> weakReferences;
    private List<PhantomReference<Referent>> phantomReferences;
    private List<String> heap;
    private ReferenceQueue<Referent> referentQueue;
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

        logHeapCount();
        clearHeap();
        return chartService.generateChartImage();
    }

    private void clearHeap() {
        heap = new LinkedList<>();
    }

    private void prepareForNewExecution() {
        softReferentCount = new AtomicInteger(0);
        weakReferentCount = new AtomicInteger(0);
        phantomReferentCount = new AtomicInteger(0);
        hardReferences = new LinkedList<>();
        softReferences = new LinkedList<>();
        weakReferences = new LinkedList<>();
        phantomReferences = new LinkedList<>();
        referentQueue = new ReferenceQueue<>();
        memoryUsageDAO.deleteAll();
        referencesCountDAO.deleteAll();
    }

    private void insertChartData() {
        memoryUsageDAO.insert(new MemoryUsage());
        referencesCountDAO.insert(new ReferencesCount(hardReferences.size(),
                softReferentCount.intValue(),
                weakReferentCount.intValue(),
                phantomReferentCount.intValue()));
    }

    private void createReferences() {
        createReferences(
                () -> {
                    Referent referent = new Referent("weak", weakReferentCount);
                    weakReferences.add(new WeakReference<>(referent));
                    insertChartData();
                    return referent;
                },
                () -> {
                    Referent referent = new Referent("soft", softReferentCount);
                    softReferences.add(new SoftReference<>(referent));
                    insertChartData();
                    return referent;
                },
                () -> {
                    Referent referent = new Referent("phantom", phantomReferentCount);
                    phantomReferences.add(new PhantomReference<>(referent, referentQueue));
                    insertChartData();
                    return referent;
                });
        startToKeepInMemoryHardReferences = System.currentTimeMillis();
    }

    @SafeVarargs
    private final void createReferences(ReferenceCreator<Referent>... creators) {
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
                    logger.info("Releasing hard references after {} milliseconds", System.currentTimeMillis() - startToKeepInMemoryHardReferences);
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
        return softReferentCount.intValue() > 0 || weakReferentCount.intValue() > 0;
    }

    private boolean wereWeakAndPhantomReferencesGarbageCollected() {
        return weakReferentCount.intValue() == 0 && phantomReferentCount.intValue() ==0;
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

    private void logHeapCount() {
        logger.info("heap count: {} ", CollectionUtils.isEmpty(heap) ? 0 : heap.size());
        logger.info("softReferences count: {} ", CollectionUtils.isEmpty(softReferences) ? 0 : softReferences.size());
        logger.info("weakReferences count: {} ", CollectionUtils.isEmpty(weakReferences) ? 0 : weakReferences.size());
        logger.info("phantomReferences count: {} ", CollectionUtils.isEmpty(phantomReferences) ? 0 : phantomReferences.size());
    }

}
