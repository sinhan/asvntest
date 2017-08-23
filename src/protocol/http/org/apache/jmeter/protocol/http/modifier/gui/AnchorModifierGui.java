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

package org.apache.jmeter.protocol.http.modifier.gui;

import java.awt.BorderLayout;

import org.apache.jmeter.processor.gui.AbstractPreProcessorGui;
import org.apache.jmeter.protocol.http.modifier.AnchorModifier;
import org.apache.jmeter.testelement.TestElement;

/**
 * @version $Revision: 325542 $ on $Date: 2005-07-12 13:51:09 -0700 (Tue, 12 Jul 2005) $
 */
public class AnchorModifierGui extends AbstractPreProcessorGui {
	public AnchorModifierGui() {
		init();
	}

	public String getLabelResource() {
		return "anchor_modifier_title";
	}

	public TestElement createTestElement() {
		AnchorModifier modifier = new AnchorModifier();
		modifyTestElement(modifier);
		return modifier;
	}

	/**
	 * Modifies a given TestElement to mirror the data in the gui components.
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
	 */
	public void modifyTestElement(TestElement modifier) {
		configureTestElement(modifier);
	}

	private void init() {
		setLayout(new BorderLayout());
		setBorder(makeBorder());

		add(makeTitlePanel(), BorderLayout.NORTH);
	}
}
