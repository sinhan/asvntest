// $Header$
/*
 * Copyright 2002-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
*/

package org.apache.jmeter.visualizers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;

import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;
import org.apache.jorphan.gui.layout.VerticalLayout;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * This class implements a statistical analyser that calculates both the average
 * and the standard deviation of the sampling process. The samples are displayed
 * in a JTable, and the statistics are displayed at the bottom of the table.
 *
 * created   March 10, 2002
 *@version   $Revision: 325238 $ Updated on $Date: 2005-03-22 14:15:32 -0800 (Tue, 22 Mar 2005) $
 */
public class TableVisualizer
    extends AbstractVisualizer
    implements GraphListener, Clearable
{
    private static transient Logger log =LoggingManager.getLoggerForClass();

    private TableDataModel model = null;
    private JTable table = null;
    private JTextField dataField = null;
    private JTextField averageField = null;
    private JTextField deviationField = null;
    private JTextField noSamplesField = null;
    private JScrollPane tableScrollPanel = null;

    /**
     * Constructor for the TableVisualizer object.
     */
    public TableVisualizer()
    {
        super();
        model = new TableDataModel();
        model.addGraphListener(this);
        init();
    }

    public String getLabelResource()
    {
        return "view_results_in_table";
    }

    public void updateGui()
    {
        // Not completely sure if this is the correct way of updating the table
        table.tableChanged(new TableModelEvent(model));
        tableScrollPanel.revalidate();
        tableScrollPanel.repaint();
        updateTextFields();
    }

    protected synchronized void updateTextFields()
    {
            noSamplesField.setText(Long.toString(model.getSampleCount()));
            dataField.setText(Long.toString(model.getCurrentData()));
            averageField.setText(Long.toString(model.getCurrentAverage()));
            deviationField.setText(Long.toString(model.getCurrentDeviation()));
    }

    public void add(SampleResult res)
    {
        model.addSample(res);
    }

    public void updateGui(Sample s)
    {
        // We have received one more sample
        // Not completely sure if this is the correct way of updating the table
        table.tableChanged(new TableModelEvent(model));
        tableScrollPanel.revalidate();
        tableScrollPanel.repaint();
        updateTextFields();
    }

    public synchronized void clear()
    {
        log.debug("Clear called",new Exception("Debug"));
        // this.graph.clear();
        model.clear();
        dataField.setText("0000");
        averageField.setText("0000");
        deviationField.setText("0000");
        repaint();
    }

    public String toString()
    {
        return "Show the samples in a table";
    }

    private void init()
    {
        this.setLayout(new BorderLayout());

        // MAIN PANEL
        JPanel mainPanel = new JPanel();
        Border margin = new EmptyBorder(10, 10, 5, 10);

        mainPanel.setBorder(margin);
        mainPanel.setLayout(new VerticalLayout(5, VerticalLayout.LEFT));

        // NAME
        mainPanel.add(makeTitlePanel());

        // Set up the table itself
        table = new JTable(model);
        // table.getTableHeader().setReorderingAllowed(false);
        tableScrollPanel = new JScrollPane(table);
        tableScrollPanel.setViewportBorder(
            BorderFactory.createEmptyBorder(2, 2, 2, 2));

        // Set up footer of table which displays numerics of the graphs
        JPanel dataPanel = new JPanel();
        JLabel dataLabel =
            new JLabel(JMeterUtils.getResString("graph_results_latest_sample"));
        dataLabel.setForeground(Color.black);
        dataField = new JTextField(5);
        dataField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        dataField.setEditable(false);
        dataField.setForeground(Color.black);
        dataField.setBackground(getBackground());
        dataPanel.add(dataLabel);
        dataPanel.add(dataField);

        JPanel averagePanel = new JPanel();
        JLabel averageLabel =
            new JLabel(JMeterUtils.getResString("graph_results_average"));
        averageLabel.setForeground(Color.blue);
        averageField = new JTextField(5);
        averageField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        averageField.setEditable(false);
        averageField.setForeground(Color.blue);
        averageField.setBackground(getBackground());
        averagePanel.add(averageLabel);
        averagePanel.add(averageField);

        JPanel deviationPanel = new JPanel();
        JLabel deviationLabel =
            new JLabel(JMeterUtils.getResString("graph_results_deviation"));
        deviationLabel.setForeground(Color.red);
        deviationField = new JTextField(5);
        deviationField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        deviationField.setEditable(false);
        deviationField.setForeground(Color.red);
        deviationField.setBackground(getBackground());
        deviationPanel.add(deviationLabel);
        deviationPanel.add(deviationField);
 
        JPanel noSamplesPanel = new JPanel();
        JLabel noSamplesLabel =
            new JLabel(JMeterUtils.getResString("graph_results_no_samples"));

        noSamplesField = new JTextField(10);
        noSamplesField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        noSamplesField.setEditable(false);
        noSamplesField.setForeground(Color.black);
        noSamplesField.setBackground(getBackground());
        noSamplesPanel.add(noSamplesLabel);
        noSamplesPanel.add(noSamplesField);


        JPanel tableInfoPanel = new JPanel();
        tableInfoPanel.setLayout(new FlowLayout());
        tableInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        tableInfoPanel.add(noSamplesPanel);
        tableInfoPanel.add(dataPanel);
        tableInfoPanel.add(averagePanel);
        tableInfoPanel.add(deviationPanel);

        // Set up the table with footer
        JPanel tablePanel = new JPanel();

        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(tableScrollPanel, BorderLayout.CENTER);
        tablePanel.add(tableInfoPanel, BorderLayout.SOUTH);

        // Add the main panel and the graph
        this.add(mainPanel, BorderLayout.NORTH);
        this.add(tablePanel, BorderLayout.CENTER);
    }
}
