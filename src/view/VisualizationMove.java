package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ShapeUtilities;


public class VisualizationMove extends ApplicationFrame {
	
	private static String path = "src/fileTxt/moveCheminDroit.txt";

    public VisualizationMove(final String title) {
        super(title);
        init();
        JPanel jpanel = createPanel();
        jpanel.setPreferredSize(new Dimension(900, 500));
        add(jpanel);
    }
    
    public void init(){
    	JMenuBar menuBar = new JMenuBar();
    	JMenu menu1 = new JMenu("File");
    	JMenuItem menuItems1 = new JMenuItem("Open");
    	JMenuItem menuItems2 = new JMenuItem("Exit");

    	menu1.add(menuItems1);
    	menu1.add(menuItems2);
    	
    	menuBar.add(menu1);
    	setJMenuBar(menuBar);
    	
		menuItems2.addActionListener(new ExitAction());
		menuItems1.addActionListener(new OpenAction());

    }
    
    private class ExitAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
    
    private class OpenAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
	}
    
    public static JPanel createPanel() {
        JFreeChart jfreechart = ChartFactory.createScatterPlot(
            "Deplacement Du Robot", "Abscisse X", "Ordonnee Y", populateData(),
            PlotOrientation.VERTICAL, true, true, false);
        Shape shape = ShapeUtilities.createDiagonalCross(3, (float) 0.3);
        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        NumberAxis axisX = (NumberAxis) xyPlot.getDomainAxis();
//        axisX.setRange(0, 40);
//        axisX.setRange(0, 2000);
        axisX.setRange(0, ((XYSeriesCollection) populateData()).getSeries(0).getMaxX() + 40);
        NumberAxis axisY = (NumberAxis) xyPlot.getRangeAxis();
//        axisY.setRange(-15, 15);
//        axisY.setRange(-1000, 1000);
        axisY.setRange(((XYSeriesCollection) populateData()).getSeries(0).getMinY() - 100, ((XYSeriesCollection) populateData()).getSeries(0).getMaxY() + 100);
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesShape(0, shape);
        renderer.setSeriesPaint(0, Color.red);
        return new ChartPanel(jfreechart);
    }
    
    private static XYDataset populateData() {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        XYSeries series = new XYSeries("Trajectoire");
        String line, fields[];
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			while ((line = reader.readLine()) != null) {
				fields = line.split(",");
				series.add(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]));
				System.out.println("X=" + fields[0] + ", Y=" + fields[1]);
			}
			reader.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
        xySeriesCollection.addSeries(series);
        System.out.println(xySeriesCollection.getSeries(0));
        return xySeriesCollection;
    }

     public static void main(final String[] args) {
    	 VisualizationMove vm = new VisualizationMove("Graphique");
         vm.pack();
         RefineryUtilities.centerFrameOnScreen(vm);
         vm.setVisible(true);
    }
}
