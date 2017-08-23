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

package org.apache.jmeter.control;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.collections.HashTree;

/**
 * This interface represents a controller that gets replaced during the
 * compilation phase of test execution in an arbitrary way.
 * 
 * @see org.apache.jmeter.gui.action.AbstractAction
 * 
 * @author Thad Smith
 * @version $Revision: 325542 $
 */
public interface ReplaceableController {
	/**
	 * Returns the TestElement that should replace the current
	 * ReplaceableCoroller.
	 * 
	 * @return TestElement
	 * @see org.apache.jmeter.testelement.TestElement
	 */
	public TestElement getReplacement();

	/**
	 * Used to replace the test execution tree (usually by adding the
	 * subelements of the TestElement that is replacing the
	 * ReplaceableController.
	 * 
	 * @param tree -
	 *            The current HashTree to be executed.
	 * @see org.apache.jorphan.collections.HashTree
	 * @see org.apache.jmeter.gui.action.AbstractAction#convertSubTree
	 */
	public void replace(HashTree tree);
}
