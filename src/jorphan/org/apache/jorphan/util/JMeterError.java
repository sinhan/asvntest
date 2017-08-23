// $Header$
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

package org.apache.jorphan.util;

/**
 * The rationale for this class is to support chained Errors in JDK 1.3
 * 
 * @version $Revision: 324515 $ $Date: 2004-03-30 10:08:09 -0800 (Tue, 30 Mar 2004) $
 */
public class JMeterError extends Error
{
	private Throwable savedCause; //Support JDK1.4 getCause() on JDK1.3

    /**
     * 
     */
    public JMeterError()
    {
        super();
    }

    /**
     * @param s
     */
    public JMeterError(String s)
    {
        super(s);
    }

	/**
	 * @param cause
	 */
	public JMeterError(Throwable cause)
	{
		//JDK1.4: super(cause);
		savedCause = cause;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public JMeterError(String message, Throwable cause)
	{
		//JDK1.4: super(message, cause);
		super(message);
		savedCause = cause;
	}
    
	/**
	 * Local version of getCause() for JDK1.3 support
	 * 
	 */
	public Throwable getCause()
	{
		return savedCause;
	}

}
