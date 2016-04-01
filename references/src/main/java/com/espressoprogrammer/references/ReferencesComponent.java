package com.espressoprogrammer.references;

import com.keypoint.PngEncoder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ReferencesComponent {
    private static final int REFERENCES_COUNT_MAX = 10;
    private static final int INSERT_MORE_REFERENCES_MAX = 2;
    private static final int MILLIS_TO_KEEP_IN_MEMORY_HARD_REFERENCES = 5000;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MemoryUsageDAO memoryUsageDAO;

    @Autowired
    private ReferencesCountDAO referencesCountDAO;

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

    public byte[] execute() {
        logger.info("Creating references");

        clearHeap();
        prepareForNewExecution();

        insertChartData();
        createReferences();
        consumeHeap();

        clearHeap();
        return generateChartImage();
    }

    private void prepareForNewExecution() {
        softReferredCount = new AtomicInteger(0);
        weakReferredCount = new AtomicInteger(0);
        phantomReferredCount = new AtomicInteger(0);
        memoryUsageDAO.deleteAll();
        referencesCountDAO.deleteAll();
        hardReferences = new LinkedList<>();
        softReferences = new LinkedList<>();
        weakReferences = new LinkedList<>();
        phantomReferences = new LinkedList<>();
        referredQueue = new ReferenceQueue<>();

    }

    private void clearHeap() {
        heap = new LinkedList<>();
    }

    private void insertChartData() {
        memoryUsageDAO.insert(new MemoryUsage());
        referencesCountDAO.insert(new ReferencesCount(hardReferences.size(),
                softReferredCount.intValue(),
                weakReferredCount.intValue(),
                phantomReferredCount.intValue()));
    }

    private void consumeHeap() {
        logger.info("Start consuming heap");
        try {
            int insertMoreReferences = 0;
            while(stillHaveReferences()) {
                if(insertMoreReferences < INSERT_MORE_REFERENCES_MAX && wereWeakAndPhantomReferencesGarbageCollected()) {
                    logger.info("Creating more references step {}", insertMoreReferences);
                    createReferences();
                    insertMoreReferences++;
                } else {
                    heap.add(UUID.randomUUID().toString());
                    insertChartData();
                }
                if(System.currentTimeMillis() - startToKeepInMemoryHardReferences > MILLIS_TO_KEEP_IN_MEMORY_HARD_REFERENCES && !hardReferences.isEmpty()) {
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

    private byte[] generateChartImage() {
        logger.info("Start generating chart");

        TimeSeries totalMemorySeries = new TimeSeries("Total memory");
        TimeSeries usedMemorySeries = new TimeSeries("Used Memory");
        memoryUsageDAO.retrieveAll().forEach(mu -> {
            totalMemorySeries.addOrUpdate(new Millisecond(mu.getCreated()), mu.getTotalMemory());
            usedMemorySeries.addOrUpdate(new Millisecond(mu.getCreated()), mu.getTotalMemory() - mu.getFreeMemory());
        });
        TimeSeriesCollection memoryUsageDataset = new TimeSeriesCollection();
        memoryUsageDataset.addSeries(totalMemorySeries);
        memoryUsageDataset.addSeries(usedMemorySeries);

        TimeSeries hardReferencesCountSeries = new TimeSeries("Hard references count");
        TimeSeries softReferredCountSeries = new TimeSeries("Soft referred count");
        TimeSeries weakReferredCountSeries = new TimeSeries("Weak referred count");
        TimeSeries phantomReferredCountSeries = new TimeSeries("Phantom referred count");
        referencesCountDAO.retrieveAll().forEach(rc -> {
            hardReferencesCountSeries.addOrUpdate(new Millisecond(rc.getCreated()), rc.getHardReferencesCount());
            softReferredCountSeries.addOrUpdate(new Millisecond(rc.getCreated()), rc.getSoftReferredCount());
            weakReferredCountSeries.addOrUpdate(new Millisecond(rc.getCreated()), rc.getWeakReferredCount());
            phantomReferredCountSeries.addOrUpdate(new Millisecond(rc.getCreated()), rc.getPhantomReferredCount());
        });
        TimeSeriesCollection referencesCountDataset = new TimeSeriesCollection();
        referencesCountDataset.addSeries(hardReferencesCountSeries);
        referencesCountDataset.addSeries(softReferredCountSeries);
        referencesCountDataset.addSeries(weakReferredCountSeries);
        referencesCountDataset.addSeries(phantomReferredCountSeries);

        JFreeChart chart = ChartFactory.createTimeSeriesChart("References",
                "Time",
                "Memory (MB)",
                memoryUsageDataset);

        XYPlot plot = chart.getXYPlot();
        plot.getRenderer(0).setSeriesPaint(0, Color.RED);
        plot.getRenderer(0).setSeriesPaint(1, Color.YELLOW);

        NumberAxis referencesCountAxis = new NumberAxis("References count");
        referencesCountAxis.setLabelFont(plot.getRangeAxis(0).getLabelFont());
        referencesCountAxis.setTickUnit(new NumberTickUnit(1));
        plot.setRangeAxis(1, referencesCountAxis);
        plot.setDataset(1, referencesCountDataset);
        plot.mapDatasetToRangeAxis(1, 1);

        plot.setRenderer(1, new StandardXYItemRenderer());
        plot.getRenderer(1).setSeriesPaint(0, Color.BLACK);
        plot.getRenderer(1).setSeriesPaint(1, Color.BLUE);
        plot.getRenderer(1).setSeriesPaint(2, Color.CYAN);
        plot.getRenderer(1).setSeriesPaint(3, Color.MAGENTA);

        logger.info("End generating chart");
        logger.info("Start generating chart image");
        BufferedImage chartImage = chart.createBufferedImage(1024, 768, new ChartRenderingInfo(new StandardEntityCollection()));
        PngEncoder encoder = new PngEncoder(chartImage);
        logger.info("End generating chart image");

        return encoder.pngEncode();
    }

    private void createReferences(ReferenceCreator<Referred>... creators) {
        Map<Integer, Integer> referencesCounters = new HashMap<>();
        for(int i=0; i<creators.length; i++) {
            referencesCounters.put(i, 0);
        }

        Random random = new Random(System.currentTimeMillis());
        while(referencesCounters.values().stream().reduce(0, Integer::sum) < REFERENCES_COUNT_MAX * creators.length) {
            int idx = random.nextInt(creators.length);
            if(referencesCounters.get(idx) < REFERENCES_COUNT_MAX) {
                hardReferences.add(creators[idx].create());
                referencesCounters.put(idx, referencesCounters.get(idx)+1);
            }

            sleepMilliseconds(250);
        }

        sleepMilliseconds(1000);
    }

    private void sleepMilliseconds(int milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {}
    }

    private boolean stillHaveReferences() {
        return softReferredCount.intValue() > 0 || weakReferredCount.intValue() > 0;
    }

    private boolean wereWeakAndPhantomReferencesGarbageCollected() {
        return weakReferredCount.intValue() == 0 && phantomReferredCount.intValue() ==0;
    }

}
