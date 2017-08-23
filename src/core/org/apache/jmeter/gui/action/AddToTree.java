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

package org.apache.jmeter.gui.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.tree.TreePath;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @author unattributed
 * @version $Revision: 325542 $ Last updated: $Date: 2005-07-12 13:51:09 -0700 (Tue, 12 Jul 2005) $
 */
public class AddToTree implements Command {
	transient private static Logger log = LoggingManager.getLoggerForClass();

	private Map allJMeterComponentCommands;

	public AddToTree() {
		allJMeterComponentCommands = new HashMap();
		allJMeterComponentCommands.put("Add", "Add");
	}

	/**
	 * Gets the Set of actions this Command class responds to.
	 * 
	 * @return the ActionNames value
	 */
	public Set getActionNames() {
		return allJMeterComponentCommands.keySet();
	}

	/**
	 * Adds the specified class to the current node of the tree.
	 */
	public void doAction(ActionEvent e) {
		try {
			TestElement node = GuiPackage.getInstance().createTestElement(((JComponent) e.getSource()).getName());
			addObjectToTree(node);
		} catch (Exception err) {
			log.error("", err);
		}
	}

	protected void addObjectToTree(TestElement el) {
		GuiPackage guiPackage = GuiPackage.getInstance();
		JMeterTreeNode node = new JMeterTreeNode(el, guiPackage.getTreeModel());
		guiPackage.getTreeModel().insertNodeInto(node, guiPackage.getTreeListener().getCurrentNode(),
				guiPackage.getTreeListener().getCurrentNode().getChildCount());
		guiPackage.getMainFrame().getTree().setSelectionPath(new TreePath(node.getPath()));
	}
}
