/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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
 * @version $Revision: 516032 $ on $Date: 2007-03-08 05:14:40 -0800 (Thu, 08 Mar 2007) $
 */
public interface TestListener {
	/**
	 * Called just before the start of the test from the main engine thread.
	 * 
	 * Note that not all the test
	 * variables will have been set up at this point.
	 * 
	 * @see org.apache.jmeter.engine.StandardJMeterEngine#run()
	 * 
	 */
	public void testStarted();

	/**
	 * Called just before the start of the test from the main engine thread.
	 * 
	 * Note that not all the test
	 * variables will have been set up at this point.
	 * 
	 * @see org.apache.jmeter.engine.StandardJMeterEngine#run()
	 * 
	 */
	public void testStarted(String host);

	/**
	 * Called once for all threads after the end of a test.
	 * 
	 * @see org.apache.jmeter.engine.StandardJMeterEngine#stopTest()
	 * 
	 */
	public void testEnded();

	/**
	 * Called once for all threads after the end of a test.
	 * 
	 * @see org.apache.jmeter.engine.StandardJMeterEngine#stopTest()
	 * 
	 */

	public void testEnded(String host);

	/**
	 * Each time through a Thread Group's test script, an iteration event is
	 * fired for each thread.
	 * 
	 * @param event
	 */
	public void testIterationStart(LoopIterationEvent event);
}