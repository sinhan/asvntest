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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.reporters.ResultSaver;
import org.apache.jmeter.processor.gui.AbstractPostProcessorGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

/**
 * Create a ResultSaver test element, which saves the sample information
 * in set of files
 * 
 * @version $Revision: 324389 $ Last updated: $Date: 2004-03-04 17:39:07 -0800 (Thu, 04 Mar 2004) $
 */
public class ResultSaverGui extends AbstractPostProcessorGui
{
   
    private JTextField filename;

	public ResultSaverGui()
    {
        super();
        init();
    }

    /**
     * @see org.apache.jmeter.gui.JMeterGUIComponent#getStaticLabel()
     */
    public String getLabelResource()
    {
        return "resultsaver_title";
    }
    
	/**
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#configure(TestElement)
	 */
    public void configure(TestElement el)
    {
        super.configure(el);
        filename.setText(el.getPropertyAsString(ResultSaver.FILENAME));
    }

    /**
     * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
     */
    public TestElement createTestElement()
    {
        ResultSaver ResultSaver = new ResultSaver();
        modifyTestElement(ResultSaver);
        return ResultSaver;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement te)
    {
        super.configureTestElement(te);
		te.setProperty(ResultSaver.FILENAME, filename.getText());
    }
    
    private void init()
    {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
		Box box = Box.createVerticalBox();
		box.add(makeTitlePanel());
		box.add(createFilenamePanel());
		add(box,BorderLayout.NORTH);
        
//        add(makeTitlePanel(),BorderLayout.NORTH);
    }
	private JPanel createFilenamePanel()//TODO ought to be a FileChooser ...
	{
		JLabel label = new JLabel(JMeterUtils.getResString("resultsaver_prefix"));
		
		filename = new JTextField(10);
		filename.setName(ResultSaver.FILENAME);
		label.setLabelFor(filename);

		JPanel filenamePanel = new JPanel(new BorderLayout(5, 0));
		filenamePanel.add(label, BorderLayout.WEST);
		filenamePanel.add(filename, BorderLayout.CENTER);
		return filenamePanel;
	}

}
