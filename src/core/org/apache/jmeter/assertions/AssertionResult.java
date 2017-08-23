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

package org.apache.jmeter.assertions;

import java.io.Serializable;

/**
 * @author Michael Stover
 * @version $Revision: 324282 $
 */
public class AssertionResult implements Serializable
{
    /** True if the assertion failed. */
    private boolean failure;
    
    /** True if there was an error checking the assertion. */
    private boolean error;
    
    /** A message describing the failure. */
    private String failureMessage;

    /**
     * Create a new Assertion Result.  The result will indicate no failure or
     * error.
     */
    public AssertionResult()
    {
    }

    /**
     * Check if the assertion failed.  If it failed, the failure message may
     * give more details about the failure.
     * 
     * @return true if the assertion failed, false if the sample met the
     *         assertion criteria
     */
    public boolean isFailure()
    {
        return failure;
    }

    /**
     * Check if an error occurred while checking the assertion.  If an error
     * occurred, the failure message may give more details about the error.
     * 
     * @return true if an error occurred while checking the assertion, false
     *         otherwise.
     */
    public boolean isError()
    {
        return error;
    }

    /**
     * Get the message associated with any failure or error.  This method may
     * return null if no message was set.
     * 
     * @return a failure or error message, or null if no message has been set
     */
    public String getFailureMessage()
    {
        return failureMessage;
    }

    /**
     * Set the flag indicating whether or not an error occurred.
     * 
     * @param e true if an error occurred, false otherwise
     */
    public void setError(boolean e)
    {
        error = e;
    }

    /**
     * Set the flag indicating whether or not a failure occurred.
     * 
     * @param f true if a failure occurred, false otherwise
     */
    public void setFailure(boolean f)
    {
        failure = f;
    }

    /**
     * Set the failure message giving more details about a failure or error.
     * 
     * @param message the message to set
     */
    public void setFailureMessage(String message)
    {
        failureMessage = message;
    }
}
