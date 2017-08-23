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

package org.apache.jmeter.gui.action;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.util.JMeterUtils;

/**
 * @author  Brendan Burns
 * @author  <a href="mailto:klancast@swbell.net">Keith Lancaster</a>
 * @version $Revision: 324282 $ updated on $Date: 2004-02-12 18:21:39 -0800 (Thu, 12 Feb 2004) $
 */
public class ExitCommand implements Command
{

    public static final String EXIT = "exit";
    private static Set commands = new HashSet();
    static {
        commands.add(EXIT);
    }

    /**
     *  Constructor for the ExitCommand object
     */
    public ExitCommand()
    {}

    /**
     *  Gets the ActionNames attribute of the ExitCommand object
     *
     *@return    The ActionNames value
     */
    public Set getActionNames()
    {
        return commands;
    }

    /**
     *  Description of the Method
     *
     *@param  e  Description of Parameter
     */
    public void doAction(ActionEvent e)
    {
        ActionRouter.getInstance().doActionNow(
            new ActionEvent(e.getSource(), e.getID(), CheckDirty.CHECK_DIRTY));
        if (GuiPackage.getInstance().isDirty())
        {
            int chosenOption =
                JOptionPane.showConfirmDialog(
                    GuiPackage.getInstance().getMainFrame(),
                    JMeterUtils.getResString("cancel_exit_to_save"),
                    JMeterUtils.getResString("Save?"),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (chosenOption == JOptionPane.NO_OPTION)
            {
                System.exit(0);
            }
            else if (chosenOption == JOptionPane.YES_OPTION)
            {
                ActionRouter.getInstance().doActionNow(
                    new ActionEvent(e.getSource(), e.getID(), Save.SAVE_ALL_AS));
                if (!GuiPackage.getInstance().isDirty())
                {
                    System.exit(0);
                }
            }
        }
        else
        {
            System.exit(0);
        }
    }
}
