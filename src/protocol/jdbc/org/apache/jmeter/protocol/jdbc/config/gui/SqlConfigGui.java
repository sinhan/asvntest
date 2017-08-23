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
package org.apache.jmeter.protocol.jdbc.config.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.protocol.jdbc.sampler.JDBCSampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

/**
 * @author    Michael Stover
 * @version   $Revision: 323402 $
 */
public class SqlConfigGui extends AbstractConfigGui
{
    private JTextArea sqlField;
    private boolean displayName;

    public SqlConfigGui()
    {
        this(true);
    }

    public SqlConfigGui(boolean displayName)
    {
        this.displayName = displayName;
        init();
    }

    public String getStaticLabel()
    {
        return JMeterUtils.getResString("database_sql_query_title");
    }

    public void configure(TestElement element)
    {
        sqlField.setText(element.getProperty(JDBCSampler.QUERY).toString());
        super.configure(element);
    }

    public TestElement createTestElement()
    {
        ConfigTestElement element = new ConfigTestElement();
        modifyTestElement(element);
        return element;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement element)
    {
        configureTestElement(element);

        String text = sqlField.getText();
        // Remove any line feeds from the text
        text = text.replace('\n', ' ');
        element.setProperty(JDBCSampler.QUERY, text);
    }

    private void init()
    {
        setLayout(new BorderLayout(0, 5));

        if (displayName)
        {
            setBorder(makeBorder());
            add(makeTitlePanel(), BorderLayout.NORTH);
        }

        JPanel panel = createSqlPanel();
        add(panel, BorderLayout.CENTER);
        // Don't let the SQL field shrink too much
        add(
            Box.createVerticalStrut(panel.getPreferredSize().height),
            BorderLayout.WEST);
    }

    private JPanel createSqlPanel()
    {
        sqlField = new JTextArea();
        sqlField.setRows(4);
        sqlField.setLineWrap(true);
        sqlField.setWrapStyleWord(true);

        JLabel label =
            new JLabel(JMeterUtils.getResString("database_sql_query_string"));
        label.setLabelFor(sqlField);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(sqlField), BorderLayout.CENTER);
        return panel;
    }

    public Dimension getPreferredSize()
    {
        return getMinimumSize();
    }
}
