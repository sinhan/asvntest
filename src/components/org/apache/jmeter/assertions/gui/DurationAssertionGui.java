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

package org.apache.jmeter.assertions.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.assertions.DurationAssertion;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
//import org.apache.jorphan.logging.LoggingManager;
//import org.apache.log.Logger;

/**
 * @version $Revision: 345098 $ Last updated: $Date: 2005-11-16 12:18:28 -0800 (Wed, 16 Nov 2005) $
 */
public class DurationAssertionGui extends AbstractAssertionGui {
	//private static final Logger log = LoggingManager.getLoggerForClass();

	private JTextField duration;

	public DurationAssertionGui() {
		init();
	}

	public String getLabelResource() {
		return "duration_assertion_title"; // $NON-NLS-1$
	}

	public String getDurationAttributesTitle() {
		return JMeterUtils.getResString("duration_assertion_duration_test"); // $NON-NLS-1$
	}

	public TestElement createTestElement() {
		DurationAssertion el = new DurationAssertion();
		modifyTestElement(el);
		return el;
	}

	/**
	 * Modifies a given TestElement to mirror the data in the gui components.
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
	 */
	public void modifyTestElement(TestElement el) {
		configureTestElement(el);
		el.setProperty(DurationAssertion.DURATION_KEY,duration.getText());
	}

	public void configure(TestElement el) {
		super.configure(el);
		duration.setText(el.getPropertyAsString(DurationAssertion.DURATION_KEY));
	}

	private void init() {
		setLayout(new BorderLayout(0, 10));
		setBorder(makeBorder());

		add(makeTitlePanel(), BorderLayout.NORTH);

		JPanel mainPanel = new JPanel(new BorderLayout());

		// USER_INPUT
		VerticalPanel durationPanel = new VerticalPanel();
		durationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				getDurationAttributesTitle()));

		JPanel labelPanel = new JPanel(new BorderLayout(5, 0));
		JLabel durationLabel = 
			new JLabel(JMeterUtils.getResString("duration_assertion_label")); // $NON-NLS-1$ 
		labelPanel.add(durationLabel, BorderLayout.WEST);

		duration = new JTextField();
		labelPanel.add(duration, BorderLayout.CENTER);
		durationLabel.setLabelFor(duration);
		durationPanel.add(labelPanel);
		
		mainPanel.add(durationPanel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
	}
}
