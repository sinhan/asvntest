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

package org.apache.jmeter.testelement;

import org.apache.jmeter.engine.event.LoopIterationEvent;

/**
 * @version $Revision: 324373 $ on $Date: 2004-02-25 16:50:44 -0800 (Wed, 25 Feb 2004) $
 */
public interface TestListener
{
	/**
	 * Called just before the start of the test
	 * Note that not all the test variables will have been set up
	 * at this point.
	 * 
	 * @see org.apache.jmeter.engine.StandardJMeterEngine#run()
	 *
	 */
    public void testStarted();

	/**
	 * Called once for all threads after the end of a test
	 * 
	 * @see org.apache.jmeter.engine.StandardJMeterEngine#stopTest()
	 *
	 */
    public void testEnded();

    public void testStarted(String host);

    public void testEnded(String host);
    
    /**
     * Each time through a Thread Group's test script, an iteration event is
     * fired.
     * @param event
     */
    public void testIterationStart(LoopIterationEvent event);
}