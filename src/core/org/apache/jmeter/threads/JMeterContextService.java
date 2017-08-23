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

package org.apache.jmeter.threads;

import java.io.Serializable;

/**
 * @author Thad Smith
 * @version $Revision: 325061 $
 */
public final class JMeterContextService implements Serializable
{
    static private ThreadLocal threadContext = new ThreadLocal(){
       public Object initialValue()
       {
          return new JMeterContext();
       }
    };
    
    private static long testStart = 0;
    private static int numberOfThreads = 0;

    /**
     * Private constructor to prevent instantiation.
     */
    private JMeterContextService()
    {
    }

    static public JMeterContext getContext()
    {
        return (JMeterContext) threadContext.get();
    }
    
    static public void startTest()
    {
       if(testStart == 0)
       {
          numberOfThreads = 0;
	       testStart = System.currentTimeMillis();
	       threadContext = new ThreadLocal(){
	          public Object initialValue()
	          {
	             return new JMeterContext();
	          }
	       };
       }
    }
    
    static synchronized void incrNumberOfThreads()
    {
       numberOfThreads++;
    }
    
    static synchronized void decrNumberOfThreads()
    {
       numberOfThreads--;
    }
    
    static public synchronized int getNumberOfThreads()
    {
       return numberOfThreads;
    }
    
    static public void endTest()
    {
       testStart = 0;
       numberOfThreads = 0;
    }
    
    static public long getTestStartTime()
    {
       return testStart;
    }

}
