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

import org.apache.jmeter.control.OnceOnlyController;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.layout.VerticalLayout;

/**
 * @version   $Revision: 324389 $ on $Date: 2004-03-04 17:39:07 -0800 (Thu, 04 Mar 2004) $
 */
public class OnceOnlyControllerGui extends AbstractControllerGui
{
    public OnceOnlyControllerGui()
    {
        init();
    }

    public TestElement createTestElement()
    {
        OnceOnlyController oc = new OnceOnlyController();
        modifyTestElement(oc);
        return oc;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement oc)
    {
        configureTestElement(oc);
    }

    public String getLabelResource()
    {
        return "once_only_controller_title";
    }

    private void init()
    {
        setLayout(
            new VerticalLayout(5, VerticalLayout.LEFT, VerticalLayout.TOP));
        setBorder(makeBorder());
        add(makeTitlePanel());
    }
}
