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

package org.apache.jorphan.gui;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * @version $Revision: 324262 $
 */
public class JLabeledPasswordField extends JLabeledTextField
{

    public JLabeledPasswordField()
    {
        super();
    }

    /**
     * Constructs a new component with the label displaying the
     * passed text.
     *
     * @param pLabel The text to in the label.
     */
    public JLabeledPasswordField(String pLabel)
    {
        super(pLabel);
    }

    protected JTextField createTextField(int size)
    {
        return new JPasswordField(size);
    }
}