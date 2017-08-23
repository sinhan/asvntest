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

package org.apache.jmeter.report.gui.action;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.jmeter.gui.ReportGuiPackage;
import org.apache.jmeter.report.gui.action.ReportActionRouter;
import org.apache.jmeter.gui.action.CheckDirty;
import org.apache.jmeter.gui.action.Command;
import org.apache.jmeter.gui.action.Save;
import org.apache.jmeter.util.JMeterUtils;

/**
 * This command clears the existing test plan, allowing the creation of a New
 * test plan.
 * 
 * @author Peter Lin
 * @version $Revision: 325728 $ Last updated: $Date: 2005-09-04 21:19:09 -0700 (Sun, 04 Sep 2005) $
 */
public class ReportClose implements Command {

	private static Set commands = new HashSet();
	static {
		commands.add("close");
	}

	/**
	 * Constructor for the Close object.
	 */
	public ReportClose() {
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
		ReportActionRouter.getInstance().doActionNow(
				new ActionEvent(e.getSource(), e.getID(),
						ReportCheckDirty.CHECK_DIRTY));
		ReportGuiPackage guiPackage = ReportGuiPackage.getInstance();
		if (guiPackage.isDirty()) {
			if (JOptionPane.showConfirmDialog(ReportGuiPackage.getInstance()
					.getMainFrame(), JMeterUtils
					.getResString("cancel_new_to_save"), JMeterUtils
					.getResString("Save?"), JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
				ReportActionRouter.getInstance().doActionNow(
						new ActionEvent(e.getSource(), e.getID(), Save.SAVE));
			}
		}
		guiPackage.getTreeModel().clearTestPlan();
		guiPackage.getTreeListener().getJTree().setSelectionRow(1);

		// Clear the name of the test plan file
		ReportGuiPackage.getInstance().setReportPlanFile(null);

		ReportActionRouter.getInstance().actionPerformed(
				new ActionEvent(e.getSource(), e.getID(), CheckDirty.ADD_ALL));
	}
}
