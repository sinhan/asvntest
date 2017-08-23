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
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.util.JMeterUtils;

/**
 * This command clears the existing test plan, allowing the creation of a New
 * test plan.
 * 
 * @author <a href="mramshaw@alumni.concordia.ca">Martin Ramshaw</a> Created
 *         June 6, 2002
 * @version $Revision: 382208 $ Last updated: $Date: 2006-03-01 17:06:37 -0800 (Wed, 01 Mar 2006) $
 */
public class Close implements Command {

	private static Set commands = new HashSet();
	static {
		commands.add(ActionNames.CLOSE);
	}

	/**
	 * Constructor for the Close object.
	 */
	public Close() {
	}

	/**
	 * Gets the ActionNames attribute of the Close object.
	 * 
	 * @return the ActionNames value
	 */
	public Set getActionNames() {
		return commands;
	}

	/**
	 * This method performs the actual command processing.
	 * 
	 * @param e
	 *            the generic UI action event
	 */
	public void doAction(ActionEvent e) {
		ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.CHECK_DIRTY));
		GuiPackage guiPackage = GuiPackage.getInstance();
		if (guiPackage.isDirty()) {
			if (JOptionPane.showConfirmDialog(GuiPackage.getInstance().getMainFrame(), JMeterUtils
					.getResString("cancel_new_to_save"), JMeterUtils.getResString("Save?"), JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
				ActionRouter.getInstance().doActionNow(new ActionEvent(e.getSource(), e.getID(), ActionNames.SAVE));
			}
		}
		guiPackage.getTreeModel().clearTestPlan();
		guiPackage.getTreeListener().getJTree().setSelectionRow(1);

		// Clear the name of the test plan file
		GuiPackage.getInstance().setTestPlanFile(null);

		ActionRouter.getInstance().actionPerformed(new ActionEvent(e.getSource(), e.getID(), ActionNames.ADD_ALL));
	}
}
