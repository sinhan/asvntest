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
 * This Exception is for use by functions etc to signal a Stop Thread condition
 * where there is no access to the normal stop method
 * 
 * @version $Revision: 325042 $ $Date: 2004-11-07 11:51:11 -0800 (Sun, 07 Nov 2004) $
 */
public class JMeterStopThreadException extends RuntimeException
{
	public JMeterStopThreadException() {
		super();
	}
	public JMeterStopThreadException(String s) {
		super(s);
	}
}
