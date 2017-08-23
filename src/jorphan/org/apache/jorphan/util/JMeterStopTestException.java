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
 * @version $Revision: 325199 $ $Date: 2005-03-12 07:30:41 -0800 (Sat, 12 Mar 2005) $
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
