// $Header$
/*
 * Copyright 2003-2004 The Apache Software Foundation.
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

package org.apache.jmeter.reporters.gui;

import java.awt.BorderLayout;

import javax.swing.Box;

import org.apache.jmeter.reporters.ResultAction;
import org.apache.jmeter.gui.OnErrorPanel;
import org.apache.jmeter.processor.gui.AbstractPostProcessorGui;
import org.apache.jmeter.testelement.OnErrorTestElement;
import org.apache.jmeter.testelement.TestElement;

/**
 * Create a Result Action Test Element
 * 
 * @version $Revision: 324389 $ Last updated: $Date: 2004-03-04 17:39:07 -0800 (Thu, 04 Mar 2004) $
 */
public class ResultActionGui extends AbstractPostProcessorGui
{
	
	private OnErrorPanel errorPanel;
   
	public ResultActionGui()
    {
        super();
        init();
    }

    /**
     * @see org.apache.jmeter.gui.JMeterGUIComponent#getStaticLabel()
     */
    public String getLabelResource()
    {
        return "resultaction_title";
    }
    
	/**
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#configure(TestElement)
	 */
    public void configure(TestElement el)
    {
        super.configure(el);
        errorPanel.configure(((OnErrorTestElement)el).getErrorAction());
    }

    /**
     * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
     */
    public TestElement createTestElement()
    {
        ResultAction resultAction = new ResultAction();
        modifyTestElement(resultAction);
        return resultAction;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement te)
    {
        super.configureTestElement(te);
        ((OnErrorTestElement) te).setErrorAction(errorPanel.getOnErrorSetting());
    }
    
    private void init()
    {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
		Box box = Box.createVerticalBox();
		box.add(makeTitlePanel());
		errorPanel = new OnErrorPanel(); 
		box.add(errorPanel);
		add(box,BorderLayout.NORTH);
    }
}