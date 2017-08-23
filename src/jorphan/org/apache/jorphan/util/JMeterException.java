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

package org.apache.jorphan.util;

/**
 * The rationale for this class is to support chained Errors in JDK 1.3
 * 
 * @version $Revision: 493784 $ $Date: 2007-01-07 09:57:55 -0800 (Sun, 07 Jan 2007) $
 */
public class JMeterException extends Exception {
	private Throwable savedCause; // Support JDK1.4 getCause() on JDK1.3

	/**
	 * 
	 */
	public JMeterException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param s
	 */
	public JMeterException(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public JMeterException(Throwable cause) {
		// JDK1.4: super(cause);
		savedCause = cause;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public JMeterException(String message, Throwable cause) {
		// JDK1.4: super(message, cause);
		super(message);
		savedCause = cause;
	}

	/**
	 * Local version of getCause() for JDK1.3 support
	 * 
	 */
	public Throwable getCause() {
		return savedCause;
	}

}
