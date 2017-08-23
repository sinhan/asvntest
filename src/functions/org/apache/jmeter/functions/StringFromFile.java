/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001,2003 The Apache Software Foundation.  All rights
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

package org.apache.jmeter.functions;

import java.io.BufferedReader;
import java.io.FileReader;
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



/**
 * StringFromFile (Function)
 * 
 * @author default
 *
 * @version $Id: StringFromFile.java 323330 2003-06-11 14:48:49Z mstover1 $
 * 
 * Function to read a String from a text file
 * 
 * Parameters:
 * 		- file name
 * 		- variable name (optional - defaults to StringFromFile_)
 * 
 * Returns:
 * 		- the next line from the file - or **ERR** if an error occurs
 *		- value is also saved in the variable for later re-use.
 * 
 * Ensure that different variable names are used for each call to the function
 * 
 * 
 * Notes:
 * - JMeter instantiates a copy of each function for every reference in a Sampler
 *   or elsewhere; each instance will open its own copy of the the file
 * - the file name is resolved at file (re-)open time
 * - the output variable name is resolved every time the function is invoked
 * 
 */
public class StringFromFile extends AbstractFunction implements Serializable
{
	private static Logger log = LoggingManager.getLoggerForClass();

	private static final List desc = new LinkedList();
	private static final String KEY = "_StringFromFile"; // Function name (only 1 _)

	static
	{
		desc.add(JMeterUtils.getResString("string_from_file_file_name"));
		desc.add(JMeterUtils.getResString("function_name_param"));
	}
	
	private String myValue = "<please supply a file>"; // Default value
	private String myName  = "StringFromFile_"; // Name to store value in
	private Object[] values;
	private BufferedReader myBread; // Buffered reader
	private boolean reopenFile=true; // Set from parameter list one day ...
	private String fileName; // needed for error messages
	
	public StringFromFile()
	{
	}
	
	public Object clone()
	{
		StringFromFile newReader = new StringFromFile();
		if (log.isDebugEnabled()){ // Skip expensive paramter creation ..
			log.debug(this+"::StringFromFile.clone()",new Throwable("debug"));
		}
			
		return newReader;
	}

	private void openFile(){
		fileName = ((CompoundVariable)values[0]).execute();
	    try {
			FileReader fis = new FileReader(fileName);
			myBread = new BufferedReader(fis);
			log.info("Opened "+fileName);
	    } catch (Exception e) {
			log.error("Error in openFile "+fileName,e);
	    }
	}

    /* (non-Javadoc)
	 * @see org.apache.jmeter.functions.Function#execute(SampleResult, Sampler)
	 */
	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
		throws InvalidVariableException {
		
		JMeterVariables vars = getVariables();

		if (values.length >= 2) {
			myName = ((CompoundVariable)values[1]).execute();
		}
		
		myValue="**ERR**";
		if (null != myBread) {// Did we open the file?
		  try {
		    String line = myBread.readLine();
		    if (line == null && reopenFile) { // EOF, re-open file
		    	log.info("Reached EOF on "+fileName);
				myBread.close();
				openFile();
				line = myBread.readLine();
		    }
		    myValue = line;
		  } catch (Exception e) {
		    log.error("Error reading file "+fileName,e);
		  }
		}
		
		vars.put(myName,myValue);

		log.debug(this+"::StringFromFile.execute() value " + myValue);

		return myValue;

	}

    /* (non-Javadoc)
     * Parameters:
     * - file name
     * - variable name (optional)
     * 
	 * @see org.apache.jmeter.functions.Function#setParameters(Collection)
	 */
	public void setParameters(Collection parameters)
		throws InvalidVariableException {
			
		log.debug(this+"::StringFromFile.setParameters()");

		values = parameters.toArray();
		
		if (( values.length > 2 ) || (values.length < 1)) {
			throw new InvalidVariableException("Wrong number of parameters");
		}
		
		openFile();
		
		log.info("Variable name: "+ myName);
			
	}

    /* (non-Javadoc)
	 * @see org.apache.jmeter.functions.Function#getReferenceKey()
	 */
	public String getReferenceKey() {
		return KEY;
	}

    /* (non-Javadoc)
	 * @see org.apache.jmeter.functions.Function#getArgumentDesc()
	 */
	public List getArgumentDesc() {
		return desc;
	}

}
