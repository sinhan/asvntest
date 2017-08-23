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

package org.apache.jmeter.junit.protocol.http.config;

import junit.framework.TestSuite;

/**
 * @author Michael Stover
 * @version $Revision: 324264 $
 */
public final class HttpConfigSuite
{
    /**
     * Private constructor to prevent instantiation.
     */
    private HttpConfigSuite()
    {
    }
    
    public static TestSuite suite()
    {
       TestSuite test = new TestSuite(UrlConfigTest.class);
       return test;
    }
}