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

/**
 * Allow threads to perform startup and closedown if necessary
 * 
 * @version $Revision: 349162 $ on $Date: 2005-11-26 17:01:05 -0800 (Sat, 26 Nov 2005) $
 */
public interface ThreadListener {
	/**
	 * Called just before the start of the thread 
     * 
     * @see org.apache.jmeter.threads.JMeterThread#threadStarted()
	 * 
	 */
	public void threadStarted();

	/**
	 * Called once for each thread at the end of a test
	 * 
	 * @see org.apache.jmeter.threads.JMeterThread#threadFinished()
	 * 
	 */
	public void threadFinished();

}