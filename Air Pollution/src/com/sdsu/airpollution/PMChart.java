package com.sdsu.airpollution;
/**
 * @author Deepak
 */
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JDialog;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class PMChart extends JDialog {
	private static final long serialVersionUID = -3863689586451667166L;
	private static HashMap<String, Double> chartInput = new HashMap<String, Double>();

	public PMChart(String applicationTitle, String chartTitle,String pmlevelurl) {

		setTitle(applicationTitle);
		createCityList(pmlevelurl);
		JFreeChart barChart = ChartFactory.createBarChart(chartTitle, "City",
				"PM Level", createDataset(), PlotOrientation.HORIZONTAL, true,
				true, true);

		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setAutoscrolls(true);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 500));
		setContentPane(chartPanel);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});

	}

	@SuppressWarnings("rawtypes")
	private CategoryDataset createDataset() {
		final String pm10 = "Particulate Matter";
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Iterator it = chartInput.entrySet().iterator();
		for (int i = 1; i <= 35; i++) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			dataset.addValue((Number) pair.getValue(), pm10, pair.getKey()
					.toString());
			it.remove(); // avoids a ConcurrentModificationException
		}
		return dataset;
	}

	public static int createCityList(String url) {
		int len;
		try {
			File file = new File(url);
			FileReader fred = new FileReader(file);
			BufferedReader in = new BufferedReader(fred);
			String s; // = in.readLine();
			String city;
			Double pmvalue;
			while ((s = in.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(s, ",");
				st.nextToken();
				st.nextToken();
				city = st.nextToken();
				pmvalue = Double.parseDouble(st.nextToken());
				chartInput.put(city, pmvalue);
			}
			in.close();
		} catch (IOException e) {
			System.out.println("Error :" + e);
		}

		len = chartInput.size();
		return len;
	}
}
