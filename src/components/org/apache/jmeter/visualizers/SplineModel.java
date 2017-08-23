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
package org.apache.jmeter.visualizers;


import java.util.*;


import org.apache.jmeter.samplers.*;

import org.apache.jmeter.gui.*;
import org.apache.jmeter.util.JMeterUtils;

/************************************************************
 *  Title: JMeter Description: Copyright: Copyright (c) 2000 Company: Apache
 *
 *@author     Michael Stover
 *@created    $Date: 2002-08-11 12:24:54 -0700 (Sun, 11 Aug 2002) $
 *@version    1.0
 ***********************************************************/


public class SplineModel implements Clearable
{

	public final int DEFAULT_NUMBER_OF_NODES = 10;

	public final int DEFAULT_REFRESH_PERIOD = 1;

	protected final boolean SHOW_INCOMING_SAMPLES = true;

	protected int numberOfNodes = DEFAULT_NUMBER_OF_NODES;

	protected int refreshPeriod = DEFAULT_REFRESH_PERIOD;


	protected Spline3 dataCurve = null;
	// current Spline curve


	protected long sum = 0;
	// sum of the samples


	protected long average = 0;
	// average of the samples


	protected long n = 0;

	ArrayList samples;
	// number of collected samples


	private GraphListener listener;


	private long minimum = Integer.MAX_VALUE, maximum = Integer.MIN_VALUE, incoming;

	private String name;


	/************************************************************
	 *  !ToDo (Constructor description)
	 ***********************************************************/
	public SplineModel()
	{

		samples = new ArrayList();

	}


	/************************************************************
	 *  !ToDo (Method description)
	 *
	 *@param  vis  !ToDo (Parameter description)
	 ***********************************************************/
	public void setListener(GraphListener vis)
	{

		listener = vis;

	}

	/************************************************************
	 *  !ToDo (Method description)
	 *
	 *@param  newName  !ToDo (Parameter description)
	 ***********************************************************/
	public void setName(String newName)
	{
		name = newName;
	}

	/************************************************************
	 *  !ToDoo (Method description)
	 *
	 *@return    !ToDo (Return description)
	 ***********************************************************/
	public boolean isEditable()
	{

		return true;
	}


	/************************************************************
	 *  !ToDoo (Method description)
	 *
	 *@return    !ToDo (Return description)
	 ***********************************************************/
	public Spline3 getDataCurve()
	{

		return dataCurve;
	}


	/************************************************************
	 *  !ToDoo (Method description)
	 *
	 *@return    !ToDo (Return description)
	 ***********************************************************/
	public Class getGuiClass()
	{

		return org.apache.jmeter.visualizers.SplineVisualizer.class;
	}



	/************************************************************
	 *  !ToDoo (Method description)
	 *
	 *@return    !ToDo (Return description)
	 ***********************************************************/
	public Collection getAddList()
	{

		return null;
	}


	/************************************************************
	 *  !ToDoo (Method description)
	 *
	 *@return    !ToDo (Return description)
	 ***********************************************************/
	public String getClassLabel()
	{

		return JMeterUtils.getResString("spline_visualizer_title");
	}


	/************************************************************
	 *  !ToDoo (Method description)
	 *
	 *@return    !ToDo (Return description)
	 ***********************************************************/
	public long getMinimum()
	{

		return minimum;
	}


	/************************************************************
	 *  !ToDoo (Method description)
	 *
	 *@return    !ToDo (Return description)
	 ***********************************************************/
	public long getMaximum()
	{

		return maximum;
	}


	/************************************************************
	 *  !ToDoo (Method description)
	 *
	 *@return    !ToDo (Return description)
	 ***********************************************************/
	public long getAverage()
	{

		return average;
	}


	/************************************************************
	 *  !ToDoo (Method description)
	 *
	 *@return    !ToDo (Return description)
	 ***********************************************************/
	public long getCurrent()
	{

		return incoming;
	}


	/************************************************************
	 *  !ToDoo (Method description)
	 *
	 *@return    !ToDo (Return description)
	 ***********************************************************/
	public long[] getSamples()
	{
		int n = samples.size();
		long[] longSample = new long[n];
		for (int i = 0; i < n; i++)
		{
			longSample[i] = ((Long)samples.get(i)).longValue();
		}
		return longSample;
	}

	/************************************************************
	 *  !ToDoo (Method description)
	 *
	 *@param  i  !ToDo (Parameter description)
	 *@return    !ToDo (Return description)
	 ***********************************************************/
	public long getSample(int i)
	{
		Long sample = (Long)this.samples.get(i);
		return sample.longValue();
	}

	/************************************************************
	 *  !ToDoo (Method description)
	 *
	 *@return    !ToDo (Return description)
	 ***********************************************************/
	public int getNumberOfCollectedSamples()
	{
		return this.samples.size();
	}

	/************************************************************
	 *  !ToDoo (Method description)
	 *
	 *@return    !ToDo (Return description)
	 ***********************************************************/
	public String getName()
	{
		return name;
	}


	/************************************************************
	 *  !ToDo (Method description)
	 ***********************************************************/
	public void uncompile()
	{
		clear();
	}


	/************************************************************
	 *  !ToDo (Method description)
	 ***********************************************************/
	public synchronized void clear()
	{

		//this.graph.clear();

		samples.clear();

		this.n = 0;

		this.sum = 0;

		this.average = 0;

		minimum = Integer.MAX_VALUE;

		maximum = Integer.MIN_VALUE;

		this.dataCurve = null;

		if(listener != null)
		{
			listener.updateGui();
		}

	}


	/************************************************************
	 *  !ToDo (Method description)
	 *
	 *@param  sampleResult  !ToDo (Parameter description)
	 ***********************************************************/
	public synchronized void add(SampleResult sampleResult)
	{
		long sample = sampleResult.getTime();
		this.n++;
		this.sum += sample;
		this.average = this.sum / this.n;
		if (SHOW_INCOMING_SAMPLES)
		{
			incoming = sample;
		}
		if (sample > maximum)
		{
			maximum = sample;
		}
		if (sample < minimum)
		{
			minimum = sample;
		}
		samples.add(new Long(sample));
		int n = getNumberOfCollectedSamples();
		if ((n % (numberOfNodes * refreshPeriod)) == 0)
		{
			float[] floatNode = new float[numberOfNodes];
			long[] longSample = getSamples();
			// load each node
			int loadFactor = n / numberOfNodes;
			for (int i = 0; i < numberOfNodes; i++)
			{
				for (int j = 0; j < loadFactor; j++)
				{
					floatNode[i] += getSample((i * loadFactor) + j);
				}
				floatNode[i] = floatNode[i] / loadFactor;
			}
			// compute the new Spline curve
			dataCurve = new Spline3(floatNode);
			if (listener != null)
			{
				listener.updateGui();
			}
		}
		else
		{
			// do nothing, wait for the next pile to complete
		}
	}
}
