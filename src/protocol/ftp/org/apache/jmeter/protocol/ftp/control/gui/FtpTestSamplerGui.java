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

package org.apache.jmeter.protocol.ftp.control.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;

import org.apache.jmeter.config.gui.LoginConfigGui;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.protocol.ftp.config.gui.FtpConfigGui;
import org.apache.jmeter.protocol.ftp.sampler.FTPSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

/**
 * 
 * @version   $Revision: 324389 $ last updated $Date: 2004-03-04 17:39:07 -0800 (Thu, 04 Mar 2004) $
 */
public class FtpTestSamplerGui extends AbstractSamplerGui
{
    private LoginConfigGui loginPanel;
    private FtpConfigGui ftpDefaultPanel;

    public FtpTestSamplerGui()
    {
        init();
    }

    public void configure(TestElement element)
    {
        super.configure(element);
        loginPanel.configure(element);
        ftpDefaultPanel.configure(element);
    }

    public TestElement createTestElement()
    {
        FTPSampler sampler = new FTPSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement sampler)
    {
        sampler.clear();
        ((FTPSampler) sampler).addTestElement(
            ftpDefaultPanel.createTestElement());
        ((FTPSampler) sampler).addTestElement(loginPanel.createTestElement());
        this.configureTestElement(sampler);
    }

    public String getLabelResource()
    {
        return "ftp_testing_title";
    }

    private void init()
    {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(makeTitlePanel(), BorderLayout.NORTH);

        VerticalPanel mainPanel = new VerticalPanel();

        ftpDefaultPanel = new FtpConfigGui(false);
        mainPanel.add(ftpDefaultPanel);

        loginPanel = new LoginConfigGui(false);
        loginPanel.setBorder(
            BorderFactory.createTitledBorder(
                JMeterUtils.getResString("login_config")));
        mainPanel.add(loginPanel);

        add(mainPanel, BorderLayout.CENTER);
    }
}
