// $Header$
/*
 * Copyright 2004 The Apache Software Foundation.
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

package org.apache.jmeter.timers;

import java.beans.PropertyDescriptor;
import java.util.ResourceBundle;

import org.apache.jmeter.testbeans.BeanInfoSupport;

/**
 * BeanInfo for the ConstantThroughputTimer.
 * 
 * @version $Revision: 325542 $ updated on $Date: 2005-07-12 13:51:09 -0700 (Tue, 12 Jul 2005) $
 */
public class ConstantThroughputTimerBeanInfo extends BeanInfoSupport {
	private static final String[] tags = new String[3];

	public ConstantThroughputTimerBeanInfo() {
		super(ConstantThroughputTimer.class);

		ResourceBundle rb = (ResourceBundle) getBeanDescriptor().getValue(RESOURCE_BUNDLE);
		tags[0] = rb.getString("calcMode.1");// These must agree with the
												// Timer
		tags[1] = rb.getString("calcMode.2");
		tags[2] = rb.getString("calcMode.3");
		createPropertyGroup("delay", new String[] { "throughput", "calcMode" });

		PropertyDescriptor p = property("throughput");
		p.setValue(NOT_UNDEFINED, Boolean.TRUE);
		p.setValue(DEFAULT, new Double(0.0));

		p = property("calcMode");
		p.setValue(NOT_UNDEFINED, Boolean.TRUE);
		p.setValue(DEFAULT, tags[0]);
		p.setValue(NOT_OTHER, Boolean.TRUE);
		p.setValue(NOT_EXPRESSION, Boolean.TRUE);
		p.setValue(TAGS, tags);
	}

	// TODO need to find better way to do this
	public static int getCalcModeAsInt(String mode) {
		for (int i = 0; i < tags.length; i++) {
			if (tags[i].equals(mode))
				return i;
		}
		return -1;
	}
}