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

import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.testelement.TestElement;

/**
 * A generic controller component.
 *
 * @version   $Revision: 324389 $ on $Date: 2004-03-04 17:39:07 -0800 (Thu, 04 Mar 2004) $
 */
public class LogicControllerGui extends AbstractControllerGui
{
    /**
     * Create a new LogicControllerGui instance.
     */
    public LogicControllerGui()
    {
        init();
    }

    /* Implements JMeterGUIComponent.createTestElement() */
    public TestElement createTestElement()
    {
        GenericController lc = new GenericController();
        configureTestElement(lc);
        return lc;
    }

    /* Implements JMeterGUIComponent.modifyTestElement(TestElement) */
    public void modifyTestElement(TestElement el)
    {
        configureTestElement(el);
    }


    public String getLabelResource()
    {
        return "logic_controller_title";
    }

    /**
     * Initialize the GUI components and layout for this component.
     */
    private void init()
    {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
    }
}
