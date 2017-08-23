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

package org.apache.jorphan.util;

/**
 * This Exception is for use by functions etc to signal a Stop Test condition
 * where there is no access to the normal stop method
 * 
 * @version $Revision: 324796 $ $Date: 2004-07-10 12:31:08 -0700 (Sat, 10 Jul 2004) $
 */
public class JMeterStopTestException extends RuntimeException
{
	public JMeterStopTestException() {
		super();
	}
	public JMeterStopTestException(String s) {
		super(s);
	}
}
