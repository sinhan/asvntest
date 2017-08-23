// $Header$
/*
 * Copyright 2001-2004 The Apache Software Foundation.
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

package org.apache.jmeter.protocol.http.modifier.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.jmeter.processor.gui.AbstractPreProcessorGui;
import org.apache.jmeter.protocol.http.modifier.UserParameterModifier;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

/**
 * A swing panel to allow UI with the UserParameterModifier class.
 * 
 * @version $Revision: 325542 $ on $Date: 2005-07-12 13:51:09 -0700 (Tue, 12 Jul 2005) $
 */
public class UserParameterModifierGui extends AbstractPreProcessorGui {
	// -------------------------------------------
	// Constants and Data Members
	// -------------------------------------------
	private JTextField fileNameField;

	// -------------------------------------------
	// Constructors
	// -------------------------------------------

	public UserParameterModifierGui() {
		super();
		init();
	}

	public TestElement createTestElement() {
		UserParameterModifier mod = new UserParameterModifier();
		modifyTestElement(mod);
		return mod;
	}

	/**
	 * Modifies a given TestElement to mirror the data in the gui components.
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
	 */
	public void modifyTestElement(TestElement mod) {
		this.configureTestElement(mod);
		((UserParameterModifier) mod).setXmlUri(fileNameField.getText());
	}

	public void updateGui() {
	}

	public String getLabelResource() {
		return "http_user_parameter_modifier";
	}

	public void configure(TestElement el) {
		super.configure(el);
		fileNameField.setText(((UserParameterModifier) el).getXmlUri());
	}

	/*-------------------------------------------------------------------------
	 * Methods Private
	 *------------------------------------------------------------------------*/
	private void init() {
		setLayout(new BorderLayout(0, 5));
		setBorder(makeBorder());
		add(makeTitlePanel(), BorderLayout.NORTH);

		JPanel mainPanel = new JPanel(new BorderLayout(0, 5));
		mainPanel.add(getFileLocator(), BorderLayout.NORTH);

		// We want the help text to look like a label, but wrap like a text area
		JTextArea helpText = new JTextArea(JMeterUtils.getResString("user_param_mod_help_note"));
		helpText.setLineWrap(true);
		helpText.setWrapStyleWord(true);
		helpText.setBackground(getBackground());
		helpText.setEditable(false);
		JLabel dummyLabel = new JLabel();
		helpText.setFont(dummyLabel.getFont());
		helpText.setForeground(dummyLabel.getForeground());
		JScrollPane scroller = new JScrollPane(helpText);
		scroller.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.add(scroller, BorderLayout.CENTER);

		add(mainPanel, BorderLayout.CENTER);
	}

	private JPanel getFileLocator() {
		fileNameField = new JTextField("users.xml", 15);
		JLabel label = new JLabel(JMeterUtils.getResString("filename"));
		label.setLabelFor(fileNameField);

		JPanel fileLocator = new JPanel(new BorderLayout());
		fileLocator.add(label, BorderLayout.WEST);
		fileLocator.add(fileNameField, BorderLayout.CENTER);
		return fileLocator;
	}

	public Dimension getPreferredSize() {
		return getMinimumSize();
	}
}