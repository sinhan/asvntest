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

package org.apache.jmeter.control.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.control.SwitchController;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

/**
 * @version   $Revision: 325222 $ on $Date: 2005-03-18 07:27:20 -0800 (Fri, 18 Mar 2005) $
 */
public class SwitchControllerGui extends AbstractControllerGui
{
   private static final String SWITCH_LABEL = "switch_controller_label";

	private JTextField switchValue;

    public SwitchControllerGui()
    {
        init();
    }

    public TestElement createTestElement()
    {
        SwitchController ic = new SwitchController();
        modifyTestElement(ic);
        return ic;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement ic)
    {
        configureTestElement(ic);
		((SwitchController) ic).setSelection(switchValue.getText());
    }

    public void configure(TestElement el)
    {
        super.configure(el);
        switchValue.setText(((SwitchController) el).getSelection());
    }

    public String getLabelResource()
    {
        return "switch_controller_title";
    }

    private void init()
    {
		setLayout(new BorderLayout(0, 5));
		setBorder(makeBorder());
		add(makeTitlePanel(), BorderLayout.NORTH);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(createSwitchPanel(), BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
    }

	private JPanel createSwitchPanel()  {
		JPanel switchPanel = new JPanel(new BorderLayout(5, 0));
		JLabel selectionLabel =
		new JLabel(JMeterUtils.getResString( SWITCH_LABEL ));
		switchValue = new JTextField("");
		selectionLabel.setLabelFor(switchValue);
        switchPanel.add(selectionLabel, BorderLayout.WEST);
        switchPanel.add(switchValue, BorderLayout.CENTER);
		return switchPanel;
	}

}
