// $Header$
/*
 * Copyright 2005 The Apache Software Foundation.
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

import java.awt.FlowLayout;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.apache.jmeter.control.IncludeController;
import org.apache.jmeter.gui.util.FilePanel;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.layout.VerticalLayout;

/**
 * 
 * 
 * @version $Revision: 325810 $ on $Date: 2005-10-01 18:12:31 -0700 (Sat, 01 Oct 2005) $
 */
public class IncludeControllerGui extends AbstractControllerGui
                                                                /*
																 * implements
																 * UnsharedComponent
																 */
{

	private JLabel warningLabel;

    private FilePanel includePanel = 
        new FilePanel(JMeterUtils.getResString("include_path"), ".jmx");

    public static final String CONTROLLER = "Module To Run";


	/**
	 * Initializes the gui panel for the ModuleController instance.
	 */
	public IncludeControllerGui() {
		init();
	}

	public String getLabelResource() {
		return "include_controller";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#configure(TestElement)
	 */
	public void configure(TestElement el) {
		super.configure(el);
		IncludeController controller = (IncludeController) el;
        this.includePanel.setFilename(controller.getIncludePath());
	}

	private String renderPath(Collection path) {
		Iterator iter = path.iterator();
		StringBuffer buf = new StringBuffer();
		boolean first = true;
		while (iter.hasNext()) {
			if (first) {
				first = false;
				iter.next();
				continue;
			}
			buf.append(iter.next());
			if (iter.hasNext())
				buf.append(" > ");
		}
		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
	 */
	public TestElement createTestElement() {
		IncludeController mc = new IncludeController();
		configureTestElement(mc);
		return mc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
	 */
	public void modifyTestElement(TestElement element) {
		configureTestElement(element);
        IncludeController controller = (IncludeController)element;
        controller.setIncludePath(this.includePanel.getFilename());
	}

	public JPopupMenu createPopupMenu() {
		JPopupMenu menu = new JPopupMenu();
		JMenu addMenu = MenuFactory.makeMenus(new String[] { MenuFactory.CONFIG_ELEMENTS, MenuFactory.ASSERTIONS,
				MenuFactory.TIMERS, MenuFactory.LISTENERS, }, JMeterUtils.getResString("Add"), "Add");
		menu.add(addMenu);
		MenuFactory.addEditMenu(menu, true);
		MenuFactory.addFileMenu(menu);
		return menu;
	}

	private void init() {
		setLayout(new VerticalLayout(5, VerticalLayout.LEFT, VerticalLayout.TOP));
		setBorder(makeBorder());
		add(makeTitlePanel());

        add(includePanel);
	}

	private String spaces(int level) {
		int multi = 4;
		StringBuffer spaces = new StringBuffer(level * multi);
		for (int i = 0; i < level * multi; i++) {
			spaces.append(" ");
		}
		return spaces.toString();
	}
    
}