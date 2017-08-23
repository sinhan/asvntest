// $Header$
/*
 * Copyright 2003-2004 The Apache Software Foundation.
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

package org.apache.jmeter.protocol.ldap.control.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;

import org.apache.jmeter.config.gui.LoginConfigGui;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.protocol.ldap.config.gui.LdapConfigGui;
import org.apache.jmeter.protocol.ldap.sampler.LDAPSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

/**
 * Created Apr 29 2003 11:52 AM
 * 
 * @version $Revision: 325542 $ Last updated: $Date: 2005-07-12 13:51:09 -0700 (Tue, 12 Jul 2005) $
 */
public class LdapTestSamplerGui extends AbstractSamplerGui {
	private LoginConfigGui loginPanel;

	private LdapConfigGui ldapDefaultPanel;

	public LdapTestSamplerGui() {
		init();
	}

	/**
	 * A newly created component can be initialized with the contents of a Test
	 * Element object by calling this method. The component is responsible for
	 * querying the Test Element object for the relevant information to display
	 * in its GUI.
	 * 
	 * @param element
	 *            the TestElement to configure
	 */
	public void configure(TestElement element) {
		super.configure(element);
		loginPanel.configure(element);
		ldapDefaultPanel.configure(element);
	}

	public TestElement createTestElement() {
		LDAPSampler sampler = new LDAPSampler();
		modifyTestElement(sampler);
		return sampler;
	}

	/**
	 * Modifies a given TestElement to mirror the data in the gui components.
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
	 */
	public void modifyTestElement(TestElement sampler) {
		sampler.clear();
		((LDAPSampler) sampler).addTestElement(ldapDefaultPanel.createTestElement());
		((LDAPSampler) sampler).addTestElement(loginPanel.createTestElement());
		this.configureTestElement(sampler);
	}

	public String getLabelResource() {
		return "ldap_testing_title";
	}

	private void init() {
		setLayout(new BorderLayout(0, 5));
		setBorder(makeBorder());
		// MAIN PANEL
		VerticalPanel mainPanel = new VerticalPanel();
		loginPanel = new LoginConfigGui(false);
		ldapDefaultPanel = new LdapConfigGui(false);
		loginPanel.setBorder(BorderFactory.createTitledBorder(JMeterUtils.getResString("login_config")));
		add(makeTitlePanel(), BorderLayout.NORTH);
		mainPanel.add(loginPanel);
		mainPanel.add(ldapDefaultPanel);
		add(mainPanel, BorderLayout.CENTER);
	}
}
