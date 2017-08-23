/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002,2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.jmeter.protocol.java.sampler;

import java.io.IOException;

//import bsh.EvalError;
import bsh.Interpreter;
   
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

/**
 * A sampler which understands BeanShell
 *
 * @author sebb AT apache DOT org
 * @version    $Revision: 323915 $ Updated on: $Date: 2003-12-19 06:38:47 -0800 (Fri, 19 Dec 2003) $
 */
public class BeanShellSampler extends AbstractSampler 
{
	protected static Logger log = LoggingManager.getLoggerForClass();

    public static final String FILENAME   = "BeanShellSampler.filename"; //$NON-NLS-1$
	public static final String SCRIPT     = "BeanShellSampler.query"; //$NON-NLS-1$
	public static final String PARAMETERS = "BeanShellSampler.parameters"; //$NON-NLS-1$

    private Interpreter bshInterpreter;
	
	public BeanShellSampler()
	{
		try{
			bshInterpreter = new Interpreter();
		} catch (NoClassDefFoundError e){
			bshInterpreter=null;
		}
	}

    /**
     * Returns a formatted string label describing this sampler
     *
     * @return a formatted string label describing this sampler
     */

    public String getLabel()
    {
        return getName();
    }

	public String getScript()
	{
		return this.getPropertyAsString(SCRIPT);
	}
    
	public String getFilename()
	{
		return getPropertyAsString(FILENAME);
	}

	public String getParameters()
	{
		return getPropertyAsString(PARAMETERS);
	}

    public SampleResult sample(Entry e)// Entry tends to be ignored ...
    {
    	//log.info(getLabel()+" "+getFilename());
        SampleResult res = new SampleResult();
        boolean isSuccessful = false;
        res.setSampleLabel(getLabel());
        res.sampleStart();
        try
        {
        	String request=getScript();
        	String fileName=getFilename();
        	if (fileName.length() == 0) {
				res.setSamplerData(request);	
        	} else {
				res.setSamplerData(fileName);
        	}

			//TODO - set some more variables?
			bshInterpreter.set("Label",getLabel());
			bshInterpreter.set("FileName",getFilename());
			bshInterpreter.set("SampleResult",res);
			bshInterpreter.set("Parameters",getParameters());// as a single line
			bshInterpreter.set("bsh.args",JOrphanUtils.split(getParameters()," "));

            // Set default values
			bshInterpreter.set("ResponseCode","200"); //$NON-NLS-1$
			bshInterpreter.set("ResponseMessage","OK");//$NON-NLS-1$
			bshInterpreter.set("IsSuccess",true);//$NON-NLS-1$
			
			Object bshOut;
			
			if (fileName.length() == 0){
				bshOut = bshInterpreter.eval(request);
			} else {
				bshOut = bshInterpreter.source(fileName);
			}
			
			String out;
			if (bshOut == null) {// Script did not return anything...
				out="";
			} else { 
				out = bshOut.toString();
			}
	        res.setResponseData(out.getBytes());
	        res.setDataType(SampleResult.TEXT);
	        res.setResponseCode(bshInterpreter.get("ResponseCode").toString());//$NON-NLS-1$
	        res.setResponseMessage(bshInterpreter.get("ResponseMessage").toString());//$NON-NLS-1$
	        isSuccessful = Boolean.valueOf(bshInterpreter.get("IsSuccess") //$NON-NLS-1$
	            .toString()).booleanValue();
        }
/*
 * To avoid class loading problems when bsh,jar is missing,
 * we don't try to catch this error separately
 * 		catch (bsh.EvalError ex)
		{
			log.debug("",ex);
			res.setResponseCode("500");//$NON-NLS-1$
			res.setResponseMessage(ex.toString());
		} 
 */
        // but we do trap this error to make tests work better
        catch(NoClassDefFoundError ex){
			log.error("BeanShell Jar missing? "+ex.toString());
			res.setResponseCode("501");//$NON-NLS-1$
			res.setResponseMessage(ex.toString());
			res.setStopThread(true); // No point continuing
        }
		catch (IOException ex)
		{
			log.warn(ex.toString());
			res.setResponseCode("500");//$NON-NLS-1$
			res.setResponseMessage(ex.toString());
		}
		catch (Exception ex) // Mainly for bsh.EvalError
		{
			log.warn(ex.toString());
			res.setResponseCode("500");//$NON-NLS-1$
			res.setResponseMessage(ex.toString());
		}

        res.sampleEnd();

        // Set if we were successful or not
        res.setSuccessful(isSuccessful);

        return res;
    }
}