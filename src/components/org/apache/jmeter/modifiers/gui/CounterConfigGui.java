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

package org.apache.jmeter.modifiers.gui;

import javax.swing.JCheckBox;

import org.apache.jmeter.modifiers.CounterConfig;
import org.apache.jmeter.processor.gui.AbstractPreProcessorGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledTextField;
import org.apache.jorphan.gui.layout.VerticalLayout;

/**
 * @version $Revision: 325222 $ on $Date: 2005-03-18 07:27:20 -0800 (Fri, 18 Mar 2005) $
 */
public class CounterConfigGui extends AbstractPreProcessorGui
{
    private JLabeledTextField startField, incrField, endField, varNameField;
    private JCheckBox perUserField;

    public CounterConfigGui()
    {
        super();
        init();
    }

    public String getLabelResource()
    {
        return "counter_config_title";
    }
    /**
     * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
     */
    public TestElement createTestElement()
    {
        CounterConfig config = new CounterConfig();
        modifyTestElement(config);
        return config;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement c)
    {
        if (c instanceof CounterConfig)
        {
            CounterConfig config = (CounterConfig) c;
            config.setStart(startField.getText());
            //Bug 22820 if (endField.getText().length() > 0)
            {
                config.setEnd(endField.getText());
            }
            config.setIncrement(incrField.getText());
            config.setVarName(varNameField.getText());
            config.setIsPerUser(perUserField.isSelected());
        }
        super.configureTestElement(c);
    }

    public void configure(TestElement element)
    {
        super.configure(element);
        CounterConfig config = (CounterConfig) element;
        startField.setText(config.getPropertyAsString(CounterConfig.START));
        endField.setText(config.getPropertyAsString(CounterConfig.END));
        incrField.setText(config.getPropertyAsString(CounterConfig.INCREMENT));
        varNameField.setText(config.getVarName());
        perUserField.setSelected(config.isPerUser());
    }

    private void init()
    {
        setBorder (makeBorder());
        setLayout(new VerticalLayout(5, VerticalLayout.LEFT));
        
        startField =
            new JLabeledTextField(JMeterUtils.getResString("start")+"          ");//TODO proper alignment
        incrField =
            new JLabeledTextField(JMeterUtils.getResString("increment"));
        endField = new JLabeledTextField(JMeterUtils.getResString("max"));
        varNameField =
            new JLabeledTextField(JMeterUtils.getResString("var_name"));
        perUserField =
            new JCheckBox(JMeterUtils.getResString("counter_per_user"));

        add(makeTitlePanel());
        add(startField);
        add(incrField);
        add(endField);
        add(varNameField);
        add(perUserField);
    }
}
