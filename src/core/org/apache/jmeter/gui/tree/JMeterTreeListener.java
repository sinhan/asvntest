/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001,2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.jmeter.gui.tree;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.control.gui.WorkBenchGui;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.JMeterGUIComponent;
import org.apache.jmeter.gui.MainFrame;
import org.apache.jmeter.gui.action.DragNDrop;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @author    Michael Stover
 * Created   March 11, 2001
 * @version   $Revision: 323598 $ Last updated: $Date: 2003-10-19 15:26:08 -0700 (Sun, 19 Oct 2003) $
 */
public class JMeterTreeListener
    implements TreeSelectionListener, MouseListener, KeyListener,
               MouseMotionListener
{
    transient private static Logger log = LoggingManager.getLoggerForClass();
    //Container endWindow;
    //JPopupMenu pop;
    private TreePath currentPath;
    private ActionListener actionHandler;

    private JMeterTreeModel model;
    private JTree tree;
    private boolean dragging = false;
    private JMeterTreeNode[] draggedNodes;
    private JLabel dragIcon = new JLabel(JMeterUtils.getImage("leafnode.gif"));

    /**
     * Constructor for the JMeterTreeListener object.
     */
    public JMeterTreeListener(JMeterTreeModel model)
    {
        this.model = model;
        dragIcon.validate();
        dragIcon.setVisible(true);
    }

    public JMeterTreeListener()
    {
        dragIcon.validate();
        dragIcon.setVisible(true);
    }

    public void setModel(JMeterTreeModel m)
    {
        model = m;
    }

    /**
     * Sets the ActionHandler attribute of the JMeterTreeListener object.
     *
     * @param ah  the new ActionHandler value
     */
    public void setActionHandler(ActionListener ah)
    {
        actionHandler = ah;
    }

    /**
     * Sets the JTree attribute of the JMeterTreeListener object.
     *
     * @param tree  the new JTree value
     */
    public void setJTree(JTree tree)
    {
        this.tree = tree;
    }

    /**
     * Sets the EndWindow attribute of the JMeterTreeListener object.
     *
     * @param window  the new EndWindow value
     */
    public void setEndWindow(Container window)
    {
        //endWindow = window;
    }

    /**
     * Gets the JTree attribute of the JMeterTreeListener object.
     *
     * @return tree the current JTree value.
     */
    public JTree getJTree()
    {
        return tree;
    }

    /**
     * Gets the CurrentNode attribute of the JMeterTreeListener object.
     *
     * @return   the CurrentNode value
     */
    public JMeterTreeNode getCurrentNode()
    {
        if (currentPath != null)
        {
            if (currentPath.getLastPathComponent() != null)
            {
                return (JMeterTreeNode) currentPath.getLastPathComponent();
            }
            else
            {
                return (JMeterTreeNode) currentPath
                    .getParentPath()
                    .getLastPathComponent();
            }
        }
        else
        {
            return (JMeterTreeNode) model.getRoot();
        }
    }

    public JMeterTreeNode[] getSelectedNodes()
    {
        TreePath[] paths = tree.getSelectionPaths();
        if (paths == null)
        {
            return new JMeterTreeNode[] { getCurrentNode()};
        }
        JMeterTreeNode[] nodes = new JMeterTreeNode[paths.length];
        for (int i = 0; i < paths.length; i++)
        {
            nodes[i] = (JMeterTreeNode) paths[i].getLastPathComponent();
        }

        return nodes;
    }

    public TreePath removedSelectedNode()
    {
        currentPath = currentPath.getParentPath();
        return currentPath;
    }

    public void valueChanged(TreeSelectionEvent e)
    {
        currentPath = e.getNewLeadSelectionPath();
        actionHandler.actionPerformed(new ActionEvent(this, 3333, "edit"));
    }

    public void mouseClicked(MouseEvent ev)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
        if (dragging && isValidDragAction(draggedNodes, getCurrentNode()))
        {
            dragging = false;
            JPopupMenu dragNdrop = new JPopupMenu();
            JMenuItem item =
                new JMenuItem(JMeterUtils.getResString("Insert Before"));
            item.addActionListener(actionHandler);
            item.setActionCommand(DragNDrop.INSERT_BEFORE);
            dragNdrop.add(item);
            item = new JMenuItem(JMeterUtils.getResString("Insert After"));
            item.addActionListener(actionHandler);
            item.setActionCommand(DragNDrop.INSERT_AFTER);
            dragNdrop.add(item);
            item = new JMenuItem(JMeterUtils.getResString("Add as Child"));
            item.addActionListener(actionHandler);
            item.setActionCommand(DragNDrop.ADD);
            dragNdrop.add(item);
            dragNdrop.addSeparator();
            item = new JMenuItem(JMeterUtils.getResString("Cancel"));
            dragNdrop.add(item);
            displayPopUp(e, dragNdrop);
        }
        else
        {
            GuiPackage.getInstance().getMainFrame().repaint();
        }
        dragging = false;
    }

    public JMeterTreeNode[] getDraggedNodes()
    {
        return draggedNodes;
    }

    /**
     * Tests if the node is being dragged into one of it's own sub-nodes, or
     * into itself.
     */
    private boolean isValidDragAction(
        JMeterTreeNode[] source,
        JMeterTreeNode dest)
    {
        boolean isValid = true;
        TreeNode[] path = dest.getPath();
        for (int i = 0; i < path.length; i++)
        {
            if (contains(source,path[i]))
            {
                isValid = false;
            }
        }
        return isValid;
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    private void changeSelectionIfDragging(MouseEvent e)
    {
        if (dragging)
        {
            GuiPackage.getInstance().getMainFrame().drawDraggedComponent(
                dragIcon,
                e.getX(),
                e.getY());
            if (tree.getPathForLocation(e.getX(), e.getY()) != null)
            {
                currentPath = tree.getPathForLocation(e.getX(), e.getY());
                if (!contains(draggedNodes,getCurrentNode()))
                {
                    tree.setSelectionPath(currentPath);
                }
            }
        }
    }
    
    private boolean contains(Object[] container,Object item)
    {
        for (int i = 0; i < container.length; i++)
        {
            if(container[i] == item)
            {
                return true;
            }
        }
        return false;
    }

    public void mousePressed(MouseEvent e)
    {
        // Get the Main Frame.
        MainFrame mainFrame = GuiPackage.getInstance().getMainFrame();

        // Close any Main Menu that is open
        mainFrame.closeMenu();
        int selRow = tree.getRowForLocation(e.getX(), e.getY());
        if (tree.getPathForLocation(e.getX(), e.getY()) != null)
        {
            currentPath = tree.getPathForLocation(e.getX(), e.getY());
        }
        if (selRow != -1)
        {
            //updateMainMenu(((JMeterGUIComponent)
            //    getCurrentNode().getUserObject()).createPopupMenu());
            if (isRightClick(e))
            {
                if (tree.getSelectionCount() < 2)
                {
                    tree.setSelectionPath(currentPath);
                }
                if (getCurrentNode() instanceof JMeterGUIComponent)
                {
                    log.debug("About to display pop-up");
                    displayPopUp(e);
                }
            }
        }
    }

    public void mouseDragged(MouseEvent e)
    {
        if (!dragging)
        {
            dragging = true;
            draggedNodes = getSelectedNodes();
            if (draggedNodes[0].getUserObject() instanceof TestPlanGui
                || draggedNodes[0].getUserObject() instanceof WorkBenchGui)
            {
                dragging = false;
            }

        }
        changeSelectionIfDragging(e);
    }

    public void mouseMoved(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent ev)
    {
    }

    public void keyPressed(KeyEvent e)
    {
    }

    public void keyReleased(KeyEvent e)
    {
    }

    public void keyTyped(KeyEvent e)
    {
    }

    private boolean isRightClick(MouseEvent e)
    {
        return (MouseEvent.BUTTON2_MASK & e.getModifiers()) > 0
            || (MouseEvent.BUTTON3_MASK == e.getModifiers());
    }

/* NOTUSED
    private void updateMainMenu(JPopupMenu menu)
    {
        try
        {
            MainFrame mainFrame = GuiPackage.getInstance().getMainFrame();
            mainFrame.setEditMenu(menu);
        }
        catch (NullPointerException e)
        {
            log.error("Null pointer: JMeterTreeListener.updateMenuItem()", e);
            log.error("", e);
        }
    }
*/

    private void displayPopUp(MouseEvent e)
    {
        JPopupMenu pop =
            ((JMeterGUIComponent) getCurrentNode()).createPopupMenu();
        GuiPackage.getInstance().displayPopUp(e, pop);
    }

    private void displayPopUp(MouseEvent e, JPopupMenu popup)
    {
        log.warn("Shouldn't be here");
        if (popup != null)
        {
            popup.pack();
            popup.show(tree, e.getX(), e.getY());
            popup.setVisible(true);
            popup.requestFocus();
        }
    }
}
