package src.ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.statistics.HistogramDataset;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@SuppressWarnings("unused")

public class HistogramPanel extends JPanel {
    private BufferedImage image;
    private String title;
    private ChartPanel chartPanel;

    public HistogramPanel(BufferedImage image, String title) {
        this.title = title;
        setLayout(new BorderLayout());
        setImage(image);
    }


    public void setImage(BufferedImage newImage) {
        this.image = newImage;
        removeAll();

        if (image != null) {
            JFreeChart chart = createChart(image, title);
            chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(600, 200));
            add(chartPanel, BorderLayout.CENTER);
        } else {
            add(new JLabel("Imagem não disponível para " + title), BorderLayout.CENTER);
        }

        revalidate();
        repaint();
    }

    private JFreeChart createChart(BufferedImage image, String title) {
        HistogramDataset dataset = new HistogramDataset();
        double[] reds = getColorChannel(image, 'r');
        double[] greens = getColorChannel(image, 'g');
        double[] blues = getColorChannel(image, 'b');

        dataset.addSeries("Red", reds, 256);
        dataset.addSeries("Green", greens, 256);
        dataset.addSeries("Blue", blues, 256);

        JFreeChart chart = ChartFactory.createHistogram(
            title,
            "Intensidade",
            "Frequência",
            dataset,
            org.jfree.chart.plot.PlotOrientation.VERTICAL,
            false, false, false
        );

        XYPlot plot = chart.getXYPlot();
        
        plot.setOutlineVisible(false);
        plot.getDomainAxis().setLowerMargin(0);
        plot.getDomainAxis().setUpperMargin(0);
        plot.getRangeAxis().setAutoRange(true);

        plot.getRenderer().setSeriesPaint(0, new Color(255, 0, 0, 150));   // Vermelho com opacidade
        plot.getRenderer().setSeriesPaint(1, new Color(0, 255, 0, 150));   // Verde com opacidade
        plot.getRenderer().setSeriesPaint(2, new Color(0, 0, 255, 150));   // Azul com opacidade


        return chart;
    }

    private double[] getColorChannel(BufferedImage image, char channel) {
        int w = image.getWidth();
        int h = image.getHeight();
        double[] values = new double[w * h];
        int idx = 0;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color c = new Color(image.getRGB(x, y));
                switch (channel) {
                    case 'r': values[idx++] = c.getRed(); break;
                    case 'g': values[idx++] = c.getGreen(); break;
                    case 'b': values[idx++] = c.getBlue(); break;
                }
            }
        }

        return values;
    }

        

        private double[] getGrayScalePixels(BufferedImage image) {
            int width = image.getWidth();
            int height = image.getHeight();
            double[] pixels = new double[width * height];
            int i = 0;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    int gray = (rgb >> 16) & 0xFF; // Assumindo escala de cinza
                    pixels[i++] = gray;
                }
            }

            return pixels;
        }
}
