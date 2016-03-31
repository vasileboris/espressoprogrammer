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
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ReferencesComponent {
    private static final int REFERENCES_COUNT_MAX = 10;
    private static final int INSERT_MORE_REFERENCES_MAX = 2;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MemoryUsageDAO memoryUsageDAO;

    @Autowired
    private ReferencesCountDAO referencesCountDAO;

    private AtomicInteger softReferencesCount;
    private AtomicInteger weakReferencesCount;
    private AtomicInteger phantomReferencesCount;
    private List<SoftReference<Referred>> softReferences;
    private List<WeakReference<Referred>> weakReferences;
    private List<PhantomReference<Referred>> phantomReferences;
    private List<String> heap;
    private ReferenceQueue<Referred> referredQueue;

    public byte[] execute() {
        logger.info("Creating references");

        clearHeap();
        prepareForNewExecution();

        insertChartData();
        createSoftReferences();
        createWeakReferences();
        createPhantomReferences();
        consumeHeap();

        clearHeap();
        return generateChartImage();
    }

    private void prepareForNewExecution() {
        softReferencesCount = new AtomicInteger(0);
        weakReferencesCount = new AtomicInteger(0);
        phantomReferencesCount = new AtomicInteger(0);
        memoryUsageDAO.deleteAll();
        referencesCountDAO.deleteAll();
        softReferences = new LinkedList<>();
        weakReferences = new LinkedList<>();
        phantomReferences = new LinkedList<>();
        referredQueue = new ReferenceQueue<>();

    }

    private void clearHeap() {
        heap = new LinkedList<>();
    }

    private void createWeakReferences() {
        createReferences(() -> {
            weakReferences.add(new WeakReference<>(new Referred("weak", weakReferencesCount)));
            insertChartData();
        });
    }

    private void createSoftReferences() {
        createReferences(() -> {
            softReferences.add(new SoftReference<>(new Referred("soft", softReferencesCount)));
            insertChartData();
        });
    }

    private void createPhantomReferences() {
        createReferences(() -> {
            phantomReferences.add(new PhantomReference<>(new Referred("phantom", phantomReferencesCount), referredQueue));
            insertChartData();
        });
    }

    private void insertChartData() {
        memoryUsageDAO.insert(new MemoryUsage());
        referencesCountDAO.insert(new ReferencesCount(softReferencesCount.intValue(), weakReferencesCount.intValue(), phantomReferencesCount.intValue()));
    }

    private void consumeHeap() {
        logger.info("Start consuming heap");
        try {
            int insertMoreReferences = INSERT_MORE_REFERENCES_MAX;
            while(stillHaveReferences()) {
                if(insertMoreReferences > 0 && wereWeakAndPhantomReferencesGarbageCollected()) {
                    createSoftReferences();
                    createWeakReferences();
                    createPhantomReferences();
                    insertMoreReferences--;
                } else {
                    heap.add(UUID.randomUUID().toString());
                    insertChartData();
                }
            }
        }
        catch (OutOfMemoryError e) {
            logger.error("Out of memory error raised", e);
        }

        logger.info("Stop consuming heap");
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

        TimeSeries softReferencesCountSeries = new TimeSeries("Soft references count");
        TimeSeries weakReferencesCountSeries = new TimeSeries("Weak references count");
        TimeSeries phantomReferencesCountSeries = new TimeSeries("Phantom references count");
        referencesCountDAO.retrieveAll().forEach(rc -> {
            softReferencesCountSeries.addOrUpdate(new Millisecond(rc.getCreated()), rc.getSoftReferencesCount());
            weakReferencesCountSeries.addOrUpdate(new Millisecond(rc.getCreated()), rc.getWeakReferencesCount());
            phantomReferencesCountSeries.addOrUpdate(new Millisecond(rc.getCreated()), rc.getPhantomReferencesCount());
        });
        TimeSeriesCollection referencesCountDataset = new TimeSeriesCollection();
        referencesCountDataset.addSeries(softReferencesCountSeries);
        referencesCountDataset.addSeries(weakReferencesCountSeries);
        referencesCountDataset.addSeries(phantomReferencesCountSeries);

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
        plot.getRenderer(1).setSeriesPaint(0, Color.BLUE);
        plot.getRenderer(1).setSeriesPaint(1, Color.CYAN);
        plot.getRenderer(1).setSeriesPaint(2, Color.MAGENTA);

        logger.info("End generating chart");
        logger.info("Start generating chart image");
        BufferedImage chartImage = chart.createBufferedImage(1024, 768, new ChartRenderingInfo(new StandardEntityCollection()));
        PngEncoder encoder = new PngEncoder(chartImage);
        logger.info("End generating chart image");

        return encoder.pngEncode();
    }

    private <T> void createReferences(ReferenceCreator creator) {
        int referencesCount = 0;
        while(referencesCount < REFERENCES_COUNT_MAX) {
            creator.create();

            referencesCount++;

            sleepMilliseconds(100);
        }

        sleepMilliseconds(250);
    }

    private void sleepMilliseconds(int milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {}
    }

    private boolean stillHaveReferences() {
        return softReferencesCount.intValue() > 0 || weakReferencesCount.intValue() > 0;
    }

    private boolean wereWeakAndPhantomReferencesGarbageCollected() {
        return weakReferencesCount.intValue() == 0 && phantomReferencesCount.intValue() ==0;
    }

}
