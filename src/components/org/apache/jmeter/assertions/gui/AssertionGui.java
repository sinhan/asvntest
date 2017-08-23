/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001,2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.jmeter.assertions.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.jmeter.assertions.ResponseAssertion;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.TextAreaCellRenderer;
import org.apache.jmeter.gui.util.TextAreaTableCellEditor;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.util.JMeterUtils;

/**
 * GUI interface for a {@link ResponseAssertion}.
 *
 * @author    Michael Stover
 * @version   $Revision: 323322 $
 */
public class AssertionGui extends AbstractAssertionGui
{
    /** The name of the table column in the list of patterns. */
    private static final String COL_NAME =
        JMeterUtils.getResString("assertion_patterns_to_test");

    /** Radio button indicating that the text response should be tested. */
    private JRadioButton responseStringButton;

    /** Radio button indicating that the URL should be tested. */
    private JRadioButton labelButton;

    /**
     * Radio button indicating to test if the field contains one of the
     * patterns.
     */
    private JRadioButton containsBox;

    /**
     * Radio button indicating to test if the field matches one of the
     * patterns.
     */
    private JRadioButton matchesBox;

    /**
     * Checkbox indicating to test that the field does NOT contain/match
     * the patterns.
     */
    private JCheckBox notBox;

    /** A table of patterns to test against. */
    private JTable stringTable;

    /** Button to add a new pattern. */
    private JButton addPattern;

    /** Button to delete a pattern. */
    private JButton deletePattern;

    /** Table model for the pattern table. */
    private PowerTableModel tableModel;

    /**
     * Create a new AssertionGui panel.
     */
    public AssertionGui()
    {
        init();
    }

    /* Implements JMeterGUIComponent.getStaticLabel() */
    public String getStaticLabel()
    {
        return JMeterUtils.getResString("assertion_title");
    }

    /* Implements JMeterGUIComponent.createTestElement() */
    public TestElement createTestElement()
    {
        ResponseAssertion el = new ResponseAssertion();
        modifyTestElement(el);
        return el;
    }

    /* Implements JMeterGUIComponent.modifyTestElement(TestElement) */
    public void modifyTestElement(TestElement el)
    {
        configureTestElement(el);
        if (el instanceof ResponseAssertion)
        {
            ResponseAssertion ra = (ResponseAssertion) el;

            ra.clearTestStrings();
            String[] testStrings = tableModel.getData().getColumn(COL_NAME);
            for (int i = 0; i < testStrings.length; i++)
            {
                ra.addTestString(testStrings[i]);
            }

            if (labelButton.isSelected())
            {
                ra.setTestField(ResponseAssertion.SAMPLE_LABEL);
            }
            else
            {
                ra.setTestField(ResponseAssertion.RESPONSE_DATA);
            }

            if (containsBox.isSelected())
            {
                ra.setToContainsType();
            }
            else
            {
                ra.setToMatchType();
            }

            if (notBox.isSelected())
            {
                ra.setToNotType();
            }
            else
            {
                ra.unsetNotType();
            }
        }
    }

    /**
     * A newly created component can be initialized with the contents of
     * a Test Element object by calling this method.  The component is
     * responsible for querying the Test Element object for the
     * relevant information to display in its GUI.
     *
     * @param el the TestElement to configure
     */
    public void configure(TestElement el)
    {
        super.configure(el);
        ResponseAssertion model = (ResponseAssertion) el;

        if (model.isContainsType())
        {
            containsBox.setSelected(true);
            matchesBox.setSelected(false);
        }
        else
        {
            containsBox.setSelected(false);
            matchesBox.setSelected(true);
        }

        if (model.isNotType())
        {
            notBox.setSelected(true);
        }
        else
        {
            notBox.setSelected(false);
        }

        if (ResponseAssertion.RESPONSE_DATA.equals(model.getTestField()))
        {
            responseStringButton.setSelected(true);
            labelButton.setSelected(false);
        }
        else
        {
            responseStringButton.setSelected(false);
            labelButton.setSelected(true);
        }

        tableModel.clearData();
        PropertyIterator tests = model.getTestStrings().iterator();
        while (tests.hasNext())
        {
            tableModel.addRow(new Object[] { tests.next().getStringValue()});
        }

        if(model.getTestStrings().size() == 0)
        {
            deletePattern.setEnabled(false);
        }
        else
        {
            deletePattern.setEnabled(true);
        }

        tableModel.fireTableDataChanged();
    }

    /**
     * Initialize the GUI components and layout.
     */
    private void init()
    {
        setLayout(new BorderLayout());
        Box box = Box.createVerticalBox();
        setBorder(makeBorder());

        box.add(makeTitlePanel());
        box.add(createFieldPanel());
        box.add(createTypePanel());
        add(box,BorderLayout.NORTH);
        add(createStringPanel(),BorderLayout.CENTER);
    }

    /**
     * Create a panel allowing the user to choose which response field should
     * be tested.
     *
     * @return a new panel for selecting the response field
     */
    private JPanel createFieldPanel()
    {
        JPanel panel = new JPanel();
        panel.setBorder(
            BorderFactory.createTitledBorder(
                JMeterUtils.getResString("assertion_resp_field")));

        responseStringButton =
            new JRadioButton(JMeterUtils.getResString("assertion_text_resp"));
        labelButton =
            new JRadioButton(JMeterUtils.getResString("assertion_url_samp"));

        ButtonGroup group = new ButtonGroup();
        group.add(responseStringButton);
        group.add(labelButton);
        panel.add(responseStringButton);
        panel.add(labelButton);

        responseStringButton.setSelected(true);

        return panel;
    }

    /**
     * Create a panel allowing the user to choose what type of test should be
     * performed.
     *
     * @return a new panel for selecting the type of assertion test
     */
    private JPanel createTypePanel()
    {
        JPanel panel = new JPanel();
        panel.setBorder(
            BorderFactory.createTitledBorder(
                JMeterUtils.getResString("assertion_pattern_match_rules")));

        ButtonGroup group = new ButtonGroup();

        containsBox =
            new JRadioButton(JMeterUtils.getResString("assertion_contains"));
        group.add(containsBox);
        containsBox.setSelected(true);
        panel.add(containsBox);

        matchesBox =
            new JRadioButton(JMeterUtils.getResString("assertion_matches"));
        group.add(matchesBox);
        panel.add(matchesBox);

        notBox = new JCheckBox(JMeterUtils.getResString("assertion_not"));
        panel.add(notBox);

        return panel;
    }

    /**
     * Create a panel allowing the user to supply a list of string patterns to
     * test against.
     *
     * @return a new panel for adding string patterns
     */
    private JPanel createStringPanel()
    {
        tableModel =
            new PowerTableModel(
                new String[] { COL_NAME },
                new Class[] { String.class });
        stringTable = new JTable(tableModel);

        TextAreaCellRenderer renderer = new TextAreaCellRenderer();
        stringTable.setRowHeight(renderer.getPreferredHeight());
        stringTable.setDefaultRenderer(String.class, renderer);
        stringTable.setDefaultEditor(
            String.class,
            new TextAreaTableCellEditor());
        stringTable.setPreferredScrollableViewportSize(new Dimension(100, 70));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(
            BorderFactory.createTitledBorder(
                JMeterUtils.getResString("assertion_patterns_to_test")));

        panel.add(new JScrollPane(stringTable), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Create a panel with buttons to add and delete string patterns.
     *
     * @return the new panel with add and delete buttons
     */
    private JPanel createButtonPanel()
    {
        addPattern = new JButton(JMeterUtils.getResString("add"));
        addPattern.addActionListener(new AddPatternListener());

        deletePattern = new JButton(JMeterUtils.getResString("delete"));
        deletePattern.addActionListener(new ClearPatternsListener());
        deletePattern.setEnabled(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addPattern);
        buttonPanel.add(deletePattern);
        return buttonPanel;
    }

    /**
     * An ActionListener for deleting a pattern.
     *
     * @author    $Author: jeremy_a $
     * @version   $Revision: 323322 $
     */
    private class ClearPatternsListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            int index = stringTable.getSelectedRow();
            if (index > -1)
            {
                stringTable
                    .getCellEditor(index, stringTable.getSelectedColumn())
                    .cancelCellEditing();
                tableModel.removeRow(index);
                tableModel.fireTableDataChanged();
            }
            if (stringTable.getModel().getRowCount() == 0)
            {
                deletePattern.setEnabled(false);
            }
        }
    }

    /**
     * An ActionListener for adding a pattern.
     *
     * @author    $Author: jeremy_a $
     * @version   $Revision: 323322 $
     */
    private class AddPatternListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            tableModel.addNewRow();
            deletePattern.setEnabled(true);
            tableModel.fireTableDataChanged();
        }
    }
}
