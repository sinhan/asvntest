//$Header$
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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.JFileChooser;

import org.apache.jmeter.exceptions.IllegalUserActionException;
import org.apache.jmeter.gui.ReportGuiPackage;
import org.apache.jmeter.report.gui.action.ReportActionRouter;
import org.apache.jmeter.gui.action.Command;
import org.apache.jmeter.gui.util.ReportFileDialoger;
import org.apache.jmeter.report.gui.tree.ReportTreeNode;
import org.apache.jmeter.save.OldSaveService;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @author Peter Lin
 * @version $Revision: 325818 $ updated on $Date: 2005-10-10 11:15:25 -0700 (Mon, 10 Oct 2005) $
 */
public class ReportSave implements Command {
	transient private static Logger log = LoggingManager.getLoggerForClass();

	public final static String SAVE_ALL_AS = "save_all_as";

	public final static String SAVE_AS = "save_as";

	public final static String SAVE = "save";

	// NOTUSED private String chosenFile;

	private static Set commands = new HashSet();
	static {
		commands.add(SAVE_AS);
		commands.add(SAVE_ALL_AS);
		commands.add(SAVE);
	}

	/**
	 * Constructor for the Save object.
	 */
	public ReportSave() {
	}

	/**
	 * Gets the ActionNames attribute of the Save object.
	 * 
	 * @return the ActionNames value
	 */
	public Set getActionNames() {
		return commands;
	}

	public void doAction(ActionEvent e) throws IllegalUserActionException {
		HashTree subTree = null;
		if (!commands.contains(e.getActionCommand())) {
			throw new IllegalUserActionException("Invalid user command:" + e.getActionCommand());
		}
		if (e.getActionCommand().equals(SAVE_AS)) {
			subTree = ReportGuiPackage.getInstance().getCurrentSubTree();
		} else {
			subTree = ReportGuiPackage.getInstance().getTreeModel().getReportPlan();
		}

		String updateFile = ReportGuiPackage.getInstance().getReportPlanFile();
		if (!SAVE.equals(e.getActionCommand()) || updateFile == null) {
			JFileChooser chooser = ReportFileDialoger.promptToSaveFile(ReportGuiPackage.getInstance().getTreeListener()
					.getCurrentNode().getName()
					+ ".jmr");
			if (chooser == null) {
				return;
			}
			updateFile = chooser.getSelectedFile().getAbsolutePath();
			if (!e.getActionCommand().equals(SAVE_AS)) {
				ReportGuiPackage.getInstance().setReportPlanFile(updateFile);
			}
		}
		// TODO: doesn't putting this here mark the tree as
		// saved even though a failure may occur later?

		ReportActionRouter.getInstance().doActionNow(new ActionEvent(subTree, e.getID(), ReportCheckDirty.SUB_TREE_SAVED));
		try {
			convertSubTree(subTree);
		} catch (Exception err) {
		}
		Writer writer = null;
		FileOutputStream ostream = null;
		try {
			if (SaveService.isSaveTestPlanFormat20()) {
				ostream = new FileOutputStream(updateFile);
				OldSaveService.saveSubTree(subTree, ostream);
                log.info("saveSubTree");
			} else {
				writer = new FileWriter(updateFile);
				SaveService.saveTree(subTree, writer);
                log.info("saveTree");
			}
		} catch (Throwable ex) {
			ReportGuiPackage.getInstance().setReportPlanFile(null);
			log.error("", ex);
			throw new IllegalUserActionException("Couldn't save test plan to file: " + updateFile);
		} finally {
			closeWriter(writer);
			closeStream(ostream);
		}
	}

	private void convertSubTree(HashTree tree) {
		Iterator iter = new LinkedList(tree.list()).iterator();
		while (iter.hasNext()) {
			ReportTreeNode item = (ReportTreeNode) iter.next();
			convertSubTree(tree.getTree(item));
			TestElement testElement = item.getTestElement();
			tree.replace(item, testElement);
		}
	}

	private void closeWriter(Writer writer) {
		if (writer != null) {
			try {
				writer.close();
			} catch (Exception ex) {
				log.error("", ex);
			}
		}
	}

	private void closeStream(FileOutputStream fos) {
		if (fos != null) {
			try {
				fos.close();
			} catch (Exception ex) {
				log.error("", ex);
			}
		}
	}

}
