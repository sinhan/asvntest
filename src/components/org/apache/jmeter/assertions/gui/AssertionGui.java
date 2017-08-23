/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
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
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import org.apache.jmeter.assertions.Assertion;
import org.apache.jmeter.assertions.ResponseAssertion;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.TextAreaCellRenderer;
import org.apache.jmeter.gui.util.TextAreaTableCellEditor;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;


/****************************************
 * Title: Jakarta-JMeter Description: Copyright: Copyright (c) 2001 Company:
 * Apache
 *
 *@author    Michael Stover
 *@created   $Date: 2002-08-11 12:24:54 -0700 (Sun, 11 Aug 2002) $
 *@version   1.0
 ***************************************/

public class AssertionGui extends AbstractAssertionGui implements FocusListener
{
	static final String COL_NAME = JMeterUtils.getResString("assertion_patterns_to_test");
	
	private JRadioButton responseStringButton, labelButton, containsBox, matchesBox;
	private JCheckBox notBox;
	private JTable stringTable;
	private JButton addPattern,deletePattern;
	PowerTableModel tableModel;

	/****************************************
	 * !ToDo (Constructor description)
	 ***************************************/
	public AssertionGui()
	{
		init();
	}

	public String getStaticLabel()
	{
		return JMeterUtils.getResString("assertion_title");
	}

	public TestElement createTestElement()
	{
		ResponseAssertion el = new ResponseAssertion();
		String[] testStrings = tableModel.getData().getColumn(COL_NAME);
		for(int i = 0;i < testStrings.length;i++)
		{
			el.addTestString(testStrings[i]);
		}
		configureTestElement(el);
		if(labelButton.isSelected())
		{
			el.setTestField(ResponseAssertion.SAMPLE_LABEL);
		}
		else
		{
			el.setTestField(ResponseAssertion.RESPONSE_DATA);
		}
		if(containsBox.isSelected())
		{
			el.setToContainsType();
		}
		else
		{
			el.setToMatchType();
		}
		if(notBox.isSelected())
		{
			el.setToNotType();
		}
		else
		{
			el.unsetNotType();
		}
		return el;
	}

	/****************************************
	 * !ToDo (Method description)
	 ***************************************/
	public void configure(TestElement el)
	{
		super.configure(el);
		ResponseAssertion model = (ResponseAssertion)el;
		if(model.isContainsType())
		{
			containsBox.setSelected(true);
			matchesBox.setSelected(false);
		}

		else
		{
			containsBox.setSelected(false);
			matchesBox.setSelected(true);
		}

		if(model.isNotType())
		{
			notBox.setSelected(true);
		}
		else
		{
			notBox.setSelected(false);
		}

		if(ResponseAssertion.RESPONSE_DATA.equals(model.getTestField()))
		{
			responseStringButton.setSelected(true);
			labelButton.setSelected(false);
		}
		else
		{
			responseStringButton.setSelected(false);
			labelButton.setSelected(true);
		}
		Iterator tests = model.getTestStrings().iterator();
		while(tests.hasNext())
		{
			tableModel.addRow(new Object[]{tests.next()});
		}
		tableModel.fireTableDataChanged();
	}

	private void init()
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = gbc.WEST;
		gbc.fill = gbc.BOTH;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;

		// MAIN PANEL
		JPanel mainPanel = new JPanel();
		add(mainPanel,gbc.clone());
		//gbc.fill = gbc.NONE;
		gbc.weighty = 0;
		mainPanel.setLayout(new GridBagLayout());
		Border margin = new EmptyBorder(10, 10, 5, 10);
		mainPanel.setBorder(margin);

		// TITLE
		JLabel panelTitleLabel = new JLabel(JMeterUtils.getResString("assertion_title"));
		Font curFont = panelTitleLabel.getFont();
		int curFontSize = curFont.getSize();
		curFontSize += 4;
		panelTitleLabel.setFont(new Font(curFont.getFontName(), curFont.getStyle(), curFontSize));
		mainPanel.add(panelTitleLabel,gbc.clone());
		gbc.gridy++;

		// NAME
		mainPanel.add(getNamePanel(),gbc.clone());
		gbc.gridx++;
		//gbc.fill = gbc.HORIZONTAL;
		mainPanel.add(Box.createHorizontalGlue(),gbc.clone());
		gbc.gridx--;
		//gbc.fill = gbc.NONE;
		gbc.gridy++;
		mainPanel.add(createFieldPanel(),gbc.clone());
		gbc.gridy++;
		mainPanel.add(createTypePanel(),gbc.clone());
		gbc.gridy++;
		//gbc.fill = gbc.BOTH;
		gbc.weighty = 1;
		gbc.gridwidth = 2;
		mainPanel.add(createStringPanel(),gbc.clone());
	}

	private JPanel createFieldPanel()
	{
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				JMeterUtils.getResString("assertion_resp_field")));
		responseStringButton = new JRadioButton(JMeterUtils.getResString("assertion_text_resp"));
		labelButton = new JRadioButton(JMeterUtils.getResString("assertion_url_samp"));
		ButtonGroup group = new ButtonGroup();
		group.add(responseStringButton);
		group.add(labelButton);
		panel.add(responseStringButton);
		panel.add(labelButton);
		responseStringButton.setSelected(true);
		return panel;
	}

	private JPanel createTypePanel()
	{
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				JMeterUtils.getResString("assertion_pattern_match_rules")));
		containsBox = new JRadioButton(JMeterUtils.getResString("assertion_contains"));
		matchesBox = new JRadioButton(JMeterUtils.getResString("assertion_matches"));
		notBox = new JCheckBox(JMeterUtils.getResString("assertion_not"));
		ButtonGroup group = new ButtonGroup();
		group.add(matchesBox);
		group.add(containsBox);
		panel.add(containsBox);
		panel.add(matchesBox);
		panel.add(notBox);
		containsBox.setSelected(true);
		return panel;
	}
	
	/****************************************
	 * Description of the Method
	 *
	 *@param e  Description of Parameter
	 ***************************************/
	public void focusLost(FocusEvent e)
	{
		try
		{
			stringTable.getCellEditor().stopCellEditing();
		}
		catch(NullPointerException err){}
	}

	/****************************************
	 * Description of the Method
	 *
	 *@param e  Description of Parameter
	 ***************************************/
	public void focusGained(FocusEvent e) { }

	private JPanel createStringPanel()
	{
		JPanel panel = new JPanel();
		stringTable = new JTable(0,1);
		stringTable.addFocusListener(this);
		tableModel = new PowerTableModel(new String[]{COL_NAME},new Class[]{String.class});
		stringTable.setModel(tableModel);
		TextAreaCellRenderer renderer = new TextAreaCellRenderer();
		stringTable.setDefaultEditor(String.class,new TextAreaTableCellEditor());
		stringTable.setRowHeight(renderer.getPreferredHeight());
		stringTable.setDefaultRenderer(String.class,renderer);
		panel.setLayout(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				JMeterUtils.getResString("assertion_patterns_to_test")));
		panel.add(new JScrollPane(stringTable),BorderLayout.CENTER);
		addPattern = new JButton(JMeterUtils.getResString("add"));
		deletePattern = new JButton(JMeterUtils.getResString("delete"));
		addPattern.addActionListener(new AddPatternListener());
		deletePattern.addActionListener(new ClearPatternsListener());
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(addPattern);
		buttonPanel.add(deletePattern);
		panel.add(buttonPanel,BorderLayout.SOUTH);
		deletePattern.setEnabled(false);
		
		return panel;
	}

	/****************************************
	 * !ToDo (Class description)
	 *
	 *@author    $Author: mstover1 $
	 *@created   $Date: 2002-08-11 12:24:54 -0700 (Sun, 11 Aug 2002) $
	 *@version   $Revision: 322831 $
	 ***************************************/
	private class ClearPatternsListener implements ActionListener
	{
		/****************************************
		 * !ToDo (Method description)
		 *
		 *@param e  !ToDo (Parameter description)
		 ***************************************/
		public void actionPerformed(ActionEvent e)
		{
			int index = stringTable.getSelectedRow();
			stringTable.getCellEditor(index,stringTable.getSelectedColumn()).cancelCellEditing();
			if(index > -1)
			{
				tableModel.removeRow(index);
				tableModel.fireTableDataChanged();
			}
			if(stringTable.getModel().getRowCount() == 0)
			{
				deletePattern.setEnabled(false);
			}
		}
	}

	/****************************************
	 * !ToDo (Class description)
	 *
	 *@author    $Author: mstover1 $
	 *@created   $Date: 2002-08-11 12:24:54 -0700 (Sun, 11 Aug 2002) $
	 *@version   $Revision: 322831 $
	 ***************************************/
	private class AddPatternListener implements ActionListener
	{
		/****************************************
		 * !ToDo (Method description)
		 *
		 *@param e  !ToDo (Parameter description)
		 ***************************************/
		public void actionPerformed(ActionEvent e)
		{
			tableModel.addNewRow();
			deletePattern.setEnabled(true);
			tableModel.fireTableDataChanged();
		}
	}

	/****************************************
	 * !ToDo (Class description)
	 *
	 *@author    $Author: mstover1 $
	 *@created   $Date: 2002-08-11 12:24:54 -0700 (Sun, 11 Aug 2002) $
	 *@version   $Revision: 322831 $
	 ***************************************/
	private class PatternRenderer extends DefaultListCellRenderer
	{
		/****************************************
		 * !ToDoo (Method description)
		 *
		 *@param list          !ToDo (Parameter description)
		 *@param value         !ToDo (Parameter description)
		 *@param index         !ToDo (Parameter description)
		 *@param isSelected    !ToDo (Parameter description)
		 *@param cellHasFocus  !ToDo (Parameter description)
		 *@return              !ToDo (Return description)
		 ***************************************/
		public Component getListCellRendererComponent(JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus)
		{
			String displayText = value.toString();
			if(displayText.length() > 10)
			{
				displayText = displayText.substring(0, 10);
			}

			JLabel label = new JLabel(displayText);

			if(isSelected)
			{
				label.setBackground(Color.blue);
				label.setForeground(Color.white);
				label.setOpaque(true);
			}
			else
			{
				label.setForeground(Color.black);
				label.setBackground(Color.white);
			}

			if(cellHasFocus)
			{
				label.setBorder(BorderFactory.createEtchedBorder());
			}

			label.setText(displayText);

			return label;
		}
	}

	
	
	
}
