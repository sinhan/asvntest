// $Header$
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

package org.apache.jmeter.protocol.java.control.gui;

import java.awt.BorderLayout;

import org.apache.jmeter.protocol.java.config.JavaConfig;
import org.apache.jmeter.protocol.java.config.gui.JavaConfigGui;
import org.apache.jmeter.protocol.java.sampler.JavaSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

/**
 * The <code>JavaTestSamplerGui</code> class provides the user interface for
 * the {@link JavaSampler}.
 * 
 * @version $Revision: 325542 $ on $Date: 2005-07-12 13:51:09 -0700 (Tue, 12 Jul 2005) $
 */
public class JavaTestSamplerGui extends AbstractSamplerGui {
	/** Panel containing the configuration options. */
	private JavaConfigGui javaPanel = null;

	/**
	 * Constructor for JavaTestSamplerGui
	 */
	public JavaTestSamplerGui() {
		super();
		init();
	}

	public String getLabelResource() {
		return "java_request";
	}

	/**
	 * Initialize the GUI components and layout.
	 */
	private void init() {
		setLayout(new BorderLayout(0, 5));
		setBorder(makeBorder());

		add(makeTitlePanel(), BorderLayout.NORTH);

		javaPanel = new JavaConfigGui(false);

		add(javaPanel, BorderLayout.CENTER);
	}

	/* Implements JMeterGuiComponent.createTestElement() */
	public TestElement createTestElement() {
		JavaSampler sampler = new JavaSampler();
		modifyTestElement(sampler);
		return sampler;
	}

	/* Implements JMeterGuiComponent.modifyTestElement(TestElement) */
	public void modifyTestElement(TestElement sampler) {
		sampler.clear();
		JavaConfig config = (JavaConfig) javaPanel.createTestElement();
		configureTestElement(sampler);
		sampler.addTestElement(config);
	}

	/* Overrides AbstractJMeterGuiComponent.configure(TestElement) */
	public void configure(TestElement el) {
		super.configure(el);
		javaPanel.configure(el);
	}
}
