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

package org.apache.jorphan.test;

/**
 * Implement this interface to work with the AllTests class. This interface
 * allows AllTests to pass a configuration file to your application before
 * running the junit unit tests.
 * 
 * @see AllTests
 * @author Michael Stover (mstover at apache.org)
 * @version $Revision: 325542 $
 */
public interface UnitTestManager {
	/**
	 * Your implementation will be handed the filename that was provided to
	 * AllTests as a configuration file. It can hold whatever properties you
	 * need to configure your system prior to the unit tests running.
	 * 
	 * @param filename
	 */
	public void initializeProperties(String filename);
}
