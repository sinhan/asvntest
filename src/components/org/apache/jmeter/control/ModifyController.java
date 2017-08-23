/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
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
package org.apache.jmeter.control;
import java.io.*;
import java.util.*;
import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.config.Modifier;
import org.apache.jmeter.config.ResponseBasedModifier;
import org.apache.jmeter.gui.JMeterComponentModel;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.samplers.*;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.testelement.TestElement;

/****************************************
 * !ToDo (Class description)
 *
 *@author    $Author: mstover1 $
 *@created   $Date: 2002-10-15 11:53:31 -0700 (Tue, 15 Oct 2002) $
 *@version   $Revision: 322904 $
 ***************************************/

public class ModifyController extends GenericController implements SampleListener,
		Serializable
{
	SampleResult currentResult;
	private String currentResponse;

	/****************************************
	 * Constructor for the GeneratorManager object
	 ***************************************/
	public ModifyController()
	{
	}

	/****************************************
	 * Methods to satisfy SampleListener interface.
	 *
	 *@param event  !ToDo (Parameter description)
	 ***************************************/
	public void sampleStarted(SampleEvent event) { }

	/****************************************
	 * !ToDo (Method description)
	 *
	 *@param event  !ToDo (Parameter description)
	 ***************************************/
	public void sampleStopped(SampleEvent event) { }

	/****************************************
	 * !ToDo (Method description)
	 *
	 *@param event  !ToDo (Parameter description)
	 ***************************************/
	public void sampleOccurred(SampleEvent event)
	{
		this.currentResult = event.getResult();
	}

	/****************************************
	 * Adds a feature to the ConfigElements attribute of the GenericController
	 * object
	 *
	 *@param entry  The feature to be added to the ConfigElements attribute
	 ***************************************/
	protected void addConfigElements(Sampler entry)
	{
		if(entry != null)
		{
			Iterator iter = this.getConfigElements().iterator();
			while(iter.hasNext())
			{
				Object item = iter.next();
				if(item instanceof Modifier)
				{
					((Modifier)item).modifyEntry(entry);
				}
				else if(item instanceof ResponseBasedModifier)
				{
					((ResponseBasedModifier)item).modifyEntry(entry, currentResult);
				}
				else
				{
					entry.addTestElement((TestElement)item);
				}
			}
		}
	}
}
