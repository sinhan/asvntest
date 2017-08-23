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

import org.apache.jmeter.assertions.XMLAssertion;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.layout.VerticalLayout;


/**
 * @version $Revision: 324389 $, $Date: 2004-03-04 17:39:07 -0800 (Thu, 04 Mar 2004) $
 */
public class XMLAssertionGui extends AbstractAssertionGui
{
    /**
     * The constructor.
     */
    public XMLAssertionGui()
    {
        init();
    }

    /**
     * Returns the label to be shown within the JTree-Component.
     */
    public String getLabelResource()
    {
        return "xml_assertion_title";
    }

    public TestElement createTestElement()
    {
        XMLAssertion el = new XMLAssertion();
        modifyTestElement(el);
        return el;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement el)
    {
        configureTestElement(el);
    }

    /**
     * Configures the associated test element.
     * @param el
     */
    public void configure(TestElement el)
    {
        super.configure(el);
    }


    /**
     * Inits the GUI.
     */
    private void init()
    {
        setLayout(
            new VerticalLayout(5, VerticalLayout.LEFT, VerticalLayout.TOP));
        setBorder(makeBorder());

        add(makeTitlePanel());
    }
}
