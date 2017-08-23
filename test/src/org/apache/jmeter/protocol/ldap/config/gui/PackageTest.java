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

package org.apache.jmeter.protocol.ldap.config.gui;

import junit.framework.TestCase;

public class PackageTest extends TestCase {
		/**
		 * Create a new test.
		 * 
		 * @param name
		 *            the name of the test
		 */
		public PackageTest(String name) {
			super(name);
		}

		/**
		 * Test that adding an argument to the table results in an appropriate
		 * TestElement being created.
		 * 
		 * @throws Exception
		 *             if an exception occurred during the test
		 */
		public void testLDAPArgumentCreation() throws Exception {
			LDAPArgumentsPanel gui = new LDAPArgumentsPanel();
			gui.tableModel.addRow(new LDAPArgument());
			gui.tableModel.setValueAt("howdy", 0, 0);
			gui.tableModel.addRow(new LDAPArgument());
			gui.tableModel.setValueAt("doody", 0, 1);

			assertEquals("=", ((LDAPArgument) ((LDAPArguments) gui.createTestElement()).getArguments().get(0)
					.getObjectValue()).getMetaData());
		}
}
