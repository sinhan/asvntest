/*
 * Created on Sep 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.apache.jmeter.visualizers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.jmeter.gui.action.ActionRouter;
import org.apache.jmeter.gui.action.SaveGraphics;
import org.apache.jmeter.util.JMeterUtils;
import org.jCharts.axisChart.AxisChart;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.DataSeries;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.BarChartProperties;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.types.ChartType;

/**
 * @author pete
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AxisGraph extends JPanel {

    protected double[][] data = null;
    protected String title, xAxisTitle, yAxisTitle, yAxisLabel;
    protected String[] xAxisLabels;
    protected int width, height;
    
	/**
	 * 
	 */
	public AxisGraph() {
		super();
	}

	/**
	 * @param layout
	 */
	public AxisGraph(LayoutManager layout) {
		super(layout);
	}

	/**
	 * @param layout
	 * @param isDoubleBuffered
	 */
	public AxisGraph(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}
    
    public void setData(double[][] data) {
        this.data = data;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setXAxisTitle(String title) {
        this.xAxisTitle = title;
    }
    
    public void setYAxisTitle(String title) {
        this.yAxisTitle = title;
    }
    
    public void setXAxisLabels(String[] labels) {
        this.xAxisLabels = labels;
    }
    
    public void setYAxisLabels(String label) {
        this.yAxisLabel = label;
    }
    
    public void setWidth(int w) {
        this.width = w;
    }
    
    public void setHeight(int h) {
        this.height = h;
    }
    
    public void paintComponent(Graphics g) {
        if (data != null && this.title != null && this.xAxisLabels != null &&
                this.xAxisTitle != null && this.yAxisLabel != null &&
                this.yAxisTitle != null) {
            drawSample(this.title,this.xAxisLabels,this.xAxisTitle,
                    this.yAxisTitle,this.data,this.width,this.height,g);
        }
    }
    
    private void drawSample(String title, String[] xAxisLabels, String xAxisTitle,
            String yAxisTitle, double[][] data, int width, int height, Graphics g) {
        try {
            if (width == 0) {
                width = 450;
            }
            if (height == 0) {
                height = 250;
            }
            this.setPreferredSize(new Dimension(width,height));
            DataSeries dataSeries = new DataSeries( xAxisLabels, xAxisTitle, yAxisTitle, title );
            
            String[] legendLabels= { yAxisLabel };
            Paint[] paints= new Paint[]{ Color.blue.darker() };
            BarChartProperties barChartProperties= new BarChartProperties();
            AxisChartDataSet axisChartDataSet =
                new AxisChartDataSet(
                        data, legendLabels, paints, ChartType.BAR, barChartProperties );
            dataSeries.addIAxisPlotDataSet( axisChartDataSet );

            ChartProperties chartProperties= new ChartProperties();
            AxisProperties axisProperties= new AxisProperties();
            axisProperties.setXAxisLabelsAreVertical(true);
            LegendProperties legendProperties= new LegendProperties();
            AxisChart axisChart = new AxisChart( 
                    dataSeries, chartProperties, axisProperties, 
                    legendProperties, width, height );
            axisChart.setGraphics2D((Graphics2D) g);
            axisChart.render();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
