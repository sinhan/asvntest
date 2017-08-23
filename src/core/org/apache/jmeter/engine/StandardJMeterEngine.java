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
package org.apache.jmeter.engine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.JMeterThread;
import org.apache.jmeter.threads.JMeterThreadMonitor;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.threads.TestCompiler;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.ListedHashTree;
import org.apache.jmeter.util.SearchByClass;

/************************************************************
 *  !ToDo (Class description)
 *
 *@author     $Author: mstover1 $
 *@created    $Date: 2002-08-11 12:24:54 -0700 (Sun, 11 Aug 2002) $
 *@version    $Revision: 322831 $
 ***********************************************************/
public class StandardJMeterEngine implements JMeterEngine,JMeterThreadMonitor
{
	Set allThreads;
	boolean running = false;
	ListedHashTree test;
	SearchByClass testListeners;
	String host = null;

	/************************************************************
	 *  !ToDo (Constructor description)
	 ***********************************************************/
	public StandardJMeterEngine()
	{
		allThreads = new HashSet();
	}

	public StandardJMeterEngine(String host)
	{
		this();
		this.host = host;
	}

	public void configure(ListedHashTree testTree)
	{
		test = testTree;
	}
	
	public void setHost(String host)
	{
		this.host = host;
	}

	protected ListedHashTree getTestTree()
	{
		return test;
	}
	
	protected void compileTree()
	{
		PreCompiler compiler = new PreCompiler();
		getTestTree().traverse(compiler);
	}

	/************************************************************
	 *  !ToDo (Method description)
	 ***********************************************************/
	public void runTest() throws JMeterEngineException
	{
		try
		{
			System.out.println("Running the test!");
			running = true;
			compileTree();
			SearchByClass searcher = new SearchByClass(ThreadGroup.class);
			testListeners = new SearchByClass(TestListener.class);
			setMode();
			getTestTree().traverse(testListeners);
			getTestTree().traverse(searcher);
			TestCompiler.initialize();
			//for each thread group, generate threads
			// hand each thread the sampler controller
			// and the listeners, and the timer
			JMeterThread[] threads;
			Iterator iter = searcher.getSearchResults().iterator();
			if(iter.hasNext())
			{
				notifyTestListenersOfStart();
			}
			JMeterVariables.initialize();
			while(iter.hasNext())
			{
				ThreadGroup group = (ThreadGroup)iter.next();
				threads = new JMeterThread[group.getNumThreads()];
				for(int i = 0;running && i < threads.length; i++)
				{
					threads[i] = new JMeterThread(cloneTree(searcher.getSubTree(group)),this);
					threads[i].setInitialDelay((int)(((float)(group.getRampUp() * 1000) /
							(float)group.getNumThreads()) * (float)i));
					threads[i].setThreadName(group.getName()+"-"+(i+1));
					Thread newThread = new Thread(threads[i]);
					newThread.setName(group.getName()+"-"+(i+1));
					allThreads.add(threads[i]);
					newThread.start();
				}
			}
		}
		catch(Exception err)
		{
			stopTest();
			StringWriter string = new StringWriter();
			PrintWriter writer = new PrintWriter(string);
			err.printStackTrace(writer);
			throw new JMeterEngineException(string.toString());
		}
	}
	
	protected void setMode()
	{
		SearchByClass testPlan = new SearchByClass(TestPlan.class);
		getTestTree().traverse(testPlan);
		Object[] plan = testPlan.getSearchResults().toArray();
		ResultCollector.enableFunctionalMode(((TestPlan)plan[0]).isFunctionalMode());
	}

	protected void notifyTestListenersOfStart()
	{
		Iterator iter = testListeners.getSearchResults().iterator();
		while(iter.hasNext())
		{
			if(host == null)
			{
				((TestListener)iter.next()).testStarted();
			}
			else
			{
				((TestListener)iter.next()).testStarted(host);
			}
		}
	}


	protected void notifyTestListenersOfEnd()
	{
		Iterator iter = testListeners.getSearchResults().iterator();
		while(iter.hasNext())
		{
			if(host == null)
			{
				((TestListener)iter.next()).testEnded();
			}
			else
			{
				((TestListener)iter.next()).testEnded(host);
			}
		}
	}

	private ListedHashTree cloneTree(ListedHashTree tree)
	{
		TreeCloner cloner = new TreeCloner();
		tree.traverse(cloner);
		return cloner.getClonedTree();
	}

	/************************************************************
	 *  !ToDo (Method description)
	 ***********************************************************/
	public void reset()
	{
		if(running)
		{
			stopTest();
			running = false;
		}
	}

	public void threadFinished(JMeterThread thread)
	{
		allThreads.remove(thread);
		if(allThreads.size() == 0)
		{
			notifyTestListenersOfEnd();
		}
	}

	/************************************************************
	 *  !ToDo (Method description)
	 ***********************************************************/
	public void stopTest()
	{
		running = false;
		Iterator iter = new HashSet(allThreads).iterator();
		while(iter.hasNext())
		{
			JMeterThread item = (JMeterThread)iter.next();
			item.stop();
		}
	}

}
