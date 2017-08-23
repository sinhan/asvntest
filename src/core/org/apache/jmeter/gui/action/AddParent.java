package org.apache.jmeter.gui.action;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.*;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.JMeterGUIComponent;
import org.apache.jmeter.gui.tree.JMeterTreeNode;

/****************************************
 * Title: JMeter Description: Copyright: Copyright (c) 2000 Company: Apache
 *
 *@author    Michael Stover
 *@created   $Date: 2002-08-11 12:24:54 -0700 (Sun, 11 Aug 2002) $
 *@version   1.0
 ***************************************/

public class AddParent implements Command
{
	private static Set commands = new HashSet();
	static
	{
		commands.add("Add Parent");
	}

	/****************************************
	 * !ToDo (Constructor description)
	 ***************************************/
	public AddParent() { }

	/****************************************
	 * !ToDo (Method description)
	 *
	 *@param e  !ToDo (Parameter description)
	 ***************************************/
	public void doAction(ActionEvent e)
	{
		String name = ((Component)e.getSource()).getName();
		try
		{
			JMeterGUIComponent controller = (JMeterGUIComponent)Class.forName(name).newInstance();
			addParentToTree(controller);
		}
		catch(Exception err)
		{
			err.printStackTrace();
		}

	}

	/****************************************
	 * !ToDoo (Method description)
	 *
	 *@return   !ToDo (Return description)
	 ***************************************/
	public Set getActionNames()
	{
		return commands;
	}

	/****************************************
	 * !ToDo
	 *
	 *@param newParent  !ToDo
	 ***************************************/
	protected void addParentToTree(JMeterGUIComponent newParent)
	{
		GuiPackage guiPackage = GuiPackage.getInstance();
		JMeterTreeNode newNode = new JMeterTreeNode(newParent);
		JMeterTreeNode currentNode = guiPackage.getTreeListener().getCurrentNode();
		JMeterTreeNode parentNode = (JMeterTreeNode)currentNode.getParent();
		int index = parentNode.getIndex(currentNode);
		guiPackage.getTreeModel().removeNodeFromParent(currentNode);
		guiPackage.getTreeModel().insertNodeInto(newNode,
				(JMeterTreeNode)parentNode, index);
		guiPackage.getTreeModel().insertNodeInto(currentNode, newNode,
				newNode.getChildCount());
	}
}
