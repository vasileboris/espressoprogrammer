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
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.awt.image.BufferedImage;

@Service
public class ChartService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MemoryUsageDAO memoryUsageDAO;

    @Autowired
    private ReferencesCountDAO referencesCountDAO;

    public byte[] generateChartImage() {
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
        BufferedImage chartImage = chart.createBufferedImage(1360, 768, new ChartRenderingInfo(new StandardEntityCollection()));
        PngEncoder encoder = new PngEncoder(chartImage);
        logger.info("End generating chart image");

        return encoder.pngEncode();
    }

}
