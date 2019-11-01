package projects.defaultProject;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import sinalgo.tools.statistics.DataSeries;

public class TimeSeriesMean extends ApplicationFrame {

  private final String TITLE = "Operations";
  private final float MINMAX = 50;
  private final int COUNT = 2 * 60;
  private final Random random = new Random();

  private DataSeries dataSeries;
  private final DynamicTimeSeriesCollection dataset;

  public TimeSeriesMean demo;

  public TimeSeriesMean(final String title, DataSeries dataSeries) {
    super("operation");

    this.dataSeries = dataSeries;

    this.dataset =
        new DynamicTimeSeriesCollection(1, COUNT, new Second());
    dataset.setTimeBase(new Second(0, 0, 0, 1, 1, 2019));
    dataset.addSeries(data(), 0, title);
    JFreeChart chart = createChart(dataset);

    this.add(new ChartPanel(chart), BorderLayout.CENTER);
  }

  private float[] data() {
    float[] a = new float[0];
    a[0] = 0;
    return a;
  }

  public void plotSample() {

    float[] newData = new float[1];
    newData[0] = (float) this.dataSeries.getMean();

    this.dataset.advanceTime();
    this.dataset.appendData(newData);
  }

  private JFreeChart createChart(final XYDataset dataset) {
    final JFreeChart result = ChartFactory.createTimeSeriesChart(
        TITLE, "rounds", "mean", dataset, true, true, false);
    final XYPlot plot = result.getXYPlot();
    ValueAxis domain = plot.getDomainAxis();
    domain.setAutoRange(true);
    ValueAxis range = plot.getRangeAxis();
    range.setRange(0, MINMAX);
    return result;
  }

}