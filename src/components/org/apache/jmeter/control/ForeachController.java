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

package org.apache.jmeter.control;

import java.io.Serializable;

import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.testelement.property.StringProperty;

/**
 * @author    Dolf Smits
 * @author    Michael Stover
 * @author    Thad Smith
 * @version   $Revision: 324364 $
 */
public class ForeachController extends GenericController implements Serializable
{

    private final static String INPUTVAL = "ForeachController.inputVal";
    private final static String RETURNVAL ="ForeachController.returnVal";
    private int loopCount = 0;

    public ForeachController()
    {
    }

    public void initialize()
    {
        log.debug("Initilizing ForEach");
    }
    
    
    public void setInputVal(String inputValue)
    {
        setProperty(new StringProperty(INPUTVAL, inputValue));
    }

    public String getInputValString()
    {
        return getPropertyAsString(INPUTVAL);
    }

    public void setReturnVal(String inputValue)
    {
        setProperty(new StringProperty(RETURNVAL, inputValue));
    }

    public String getReturnValString()
    {
        return getPropertyAsString(RETURNVAL);
    }

   /* (non-Javadoc)
     * @see org.apache.jmeter.control.Controller#isDone()
     */
    public boolean isDone()
    {
        JMeterContext context = getThreadContext();
    	String inputVariable=getInputValString()+"_"+(loopCount+1);
    	if (context.getVariables().get(inputVariable) != null) 
    	{
    	   context.getVariables().put(getReturnValString(), context.getVariables().get(inputVariable));
                   log.debug("ForEach resultstring isDone="+context.getVariables().get(getReturnValString()));
    	   return false;
    	} 
        return super.isDone();
    }

    private boolean endOfArguments()
    {
        JMeterContext context = getThreadContext();
    	String inputVariable=getInputValString()+"_"+(loopCount+1);
    	if (context.getVariables().get(inputVariable) != null) 
    	{
           log.debug("ForEach resultstring eofArgs= false");
    	   return false;
    	} else {
           log.debug("ForEach resultstring eofArgs= true");
    	   return true;
    	}
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.control.GenericController#nextIsNull()
     */
    protected Sampler nextIsNull() throws NextIsNullException
    {
        reInitialize();
        if (endOfArguments())
        {
//           setDone(true);
           resetLoopCount();
           return null;
        }
        else
        {
            return next();
        }
    }

    protected void incrementLoopCount()
    {
        loopCount++;
    }

    protected void resetLoopCount()
    {
        loopCount = 0;
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.control.GenericController#getIterCount()
     */
    protected int getIterCount()
    {
        return loopCount + 1;
    }

	/* (non-Javadoc)
	 * @see org.apache.jmeter.control.GenericController#reInitialize()
	 */
	protected void reInitialize()
	{
		setFirst(true);
		resetCurrent();
		incrementLoopCount();
	}
}