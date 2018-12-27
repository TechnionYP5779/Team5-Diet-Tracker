package GraphsMaker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class simpleGraph  extends JFrame{

        JFreeChart chart;
        ArrayList<Calendar> dates;
        ArrayList<Integer> weights;
        String imageName="";
        
        public simpleGraph make() {
        	XYDataset dataset = createDataset();
            chart = createChart(dataset);
            return this;
        }
        
        public simpleGraph setDates(ArrayList<Calendar> d) {
        	dates=d;
        	return this;
        }
        
        public simpleGraph setWeights(ArrayList<Integer> w) {
        	weights=w;
        	return this;
        }

        private void initUI() {

            XYDataset dataset = createDataset();
            chart = createChart(dataset);
            ChartPanel chartPanel = new ChartPanel(chart, false);
            //chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            chartPanel.setBackground(Color.white);
            add(chartPanel);

            pack();
            setTitle("Line chart");
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        private XYDataset createDataset() {

            TimeSeries series = new TimeSeries("Weights");
             
            for(int i=0;i<dates.size();i++) {
            	Calendar currentDate=dates.get(i);
            	Integer currentWeight=weights.get(i);
            	series.add(new Day(currentDate.get(Calendar.DAY_OF_MONTH), +currentDate.get(Calendar.MONTH), currentDate.get(Calendar.YEAR)), currentWeight);
            }
            
            TimeSeriesCollection dataset = new TimeSeriesCollection();
            dataset.addSeries(series);
            return dataset;
        }

        private JFreeChart createChart(XYDataset dataset) {

            JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    "Weight per day",
                    "Date",
                    "Weight",
                    dataset,
                    true,
                    true,
                    false
            );

            XYPlot plot = chart.getXYPlot();

            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            renderer.setSeriesPaint(0, Color.blue);
            renderer.setSeriesStroke(0, new BasicStroke(2.0f));

            plot.setRenderer(renderer);
            plot.setBackgroundPaint(Color.white);

            plot.setRangeGridlinesVisible(true);
            plot.setRangeGridlinePaint(Color.BLACK);

            plot.setDomainGridlinesVisible(true);
            plot.setDomainGridlinePaint(Color.BLACK);

            //chart.getLegend().setFrame(BlockBorder.NONE);

            chart.setTitle(new TextTitle("Weight per day",
                            new Font("Serif", java.awt.Font.BOLD, 18)
                    )
            );

            DateAxis axis = (DateAxis) plot.getDomainAxis();
            axis.setDateFormatOverride(new SimpleDateFormat("dd.MM.yyyy"));

            return chart;
        }

        public void save(int width, int height,String imageName){
        	this.imageName=imageName;
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = img.createGraphics();
            chart.draw(g2, new Rectangle2D.Double(0, 0, width, height));
            g2.dispose();
            File outputfile = new File(imageName+".jpg");
            try {
                ImageIO.write(img, "jpg", outputfile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        
        public void delete() {
        	 File file = new File(imageName+".jpg");
        	 file.delete();
        }
        
    }
