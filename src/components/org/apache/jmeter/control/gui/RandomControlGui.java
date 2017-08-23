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

import javax.swing.JCheckBox;

import org.apache.jmeter.control.InterleaveControl;
import org.apache.jmeter.control.RandomController;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.layout.VerticalLayout;

/**
 * @version   $Revision: 324389 $ on $Date: 2004-03-04 17:39:07 -0800 (Thu, 04 Mar 2004) $
 */
public class RandomControlGui extends AbstractControllerGui
{
    private JCheckBox style;

    public RandomControlGui()
    {
        init();
    }

    public TestElement createTestElement()
    {
        RandomController ic = new RandomController();
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
        if (style.isSelected())
        {
            ((RandomController) ic).setStyle(
                InterleaveControl.IGNORE_SUB_CONTROLLERS);
        }
        else
        {
            ((RandomController) ic).setStyle(
                InterleaveControl.USE_SUB_CONTROLLERS);
        }
    }

    public void configure(TestElement el)
    {
        super.configure(el);
        if (((RandomController) el).getStyle()
            == InterleaveControl.IGNORE_SUB_CONTROLLERS)
        {
            style.setSelected(true);
        }
        else
        {
            style.setSelected(false);
        }
    }

    public String getLabelResource()
    {
        return "random_control_title";
    }

    private void init()
    {
        setLayout(
            new VerticalLayout(5, VerticalLayout.LEFT, VerticalLayout.TOP));
        setBorder(makeBorder());
        add(makeTitlePanel());

        style =
            new JCheckBox(JMeterUtils.getResString("ignore_subcontrollers"));
        add(style);
    }
}
