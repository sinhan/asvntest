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

package org.apache.jmeter.functions;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrappedException;

public class JavaScript extends AbstractFunction implements Serializable
{

    private static final List desc = new LinkedList();
    private static final String KEY = "__javaScript";
	private static Logger log = LoggingManager.getLoggerForClass();

    static {
        desc.add("JavaScript expression to evaluate");
        desc.add(JMeterUtils.getResString("function_name_param"));
    }

    private Object[] values;

    public JavaScript()
    {
    }

    public Object clone()
    {
        JavaScript newJavaScript = new JavaScript();
        return newJavaScript;
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.functions.Function#execute(SampleResult, Sampler)
     */
    public synchronized String execute(
        SampleResult previousResult,
        Sampler currentSampler)
        throws InvalidVariableException
    {

        JMeterVariables vars = getVariables();

        String script = ((CompoundVariable) values[0]).execute();
        String varName =
            ((CompoundVariable) values[1]).execute();
        String resultStr = "";

        Context cx = Context.enter();
        try
        {

            Scriptable scope = cx.initStandardObjects(null);
            Object result = cx.evaluateString(scope, script, "<cmd>", 1, null);

            resultStr = Context.toString(result);
            vars.put(varName, resultStr);

        }
        catch (WrappedException e)
		{
        	log.error("Error processing Javascript",e);
        	throw new InvalidVariableException();        	
		}
        catch (EcmaError e)
		{
        	log.error("Error processing Javascript",e);
        	throw new InvalidVariableException();
		}
        catch (JavaScriptException e)
        {
        	log.error("Error processing Javascript",e);
            throw new InvalidVariableException();
        }
        finally
        {
            Context.exit();
        }

        return resultStr;

    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.functions.Function#setParameters(Collection)
     */
    public void setParameters(Collection parameters)
        throws InvalidVariableException
    {

        values = parameters.toArray();

        if (values.length != 2)
        {
            throw new InvalidVariableException(
            		"Expecting 2 parameters, but found " + values.length);//$NON-NLS-1$
        }

    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.functions.Function#getReferenceKey()
     */
    public String getReferenceKey()
    {
        return KEY;
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.functions.Function#getArgumentDesc()
     */
    public List getArgumentDesc()
    {
        return desc;
    }

}
