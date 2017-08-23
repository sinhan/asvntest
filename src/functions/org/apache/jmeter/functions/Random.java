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

/**
 * Provides a Random function which returns a random integer between
 * a min (first argument) and a max (seceond argument).
 *
 * @author <a href="mailto:sjkwadzo@praize.com">Jonathan Kwadzo</a>
 * @version $Id: Random.java 323418 2003-08-16 15:03:50Z jeremy_a $
 */
public class Random extends AbstractFunction implements Serializable
{

    private static final List desc = new LinkedList();
    private static final String KEY = "__Random";

    static {
        desc.add(JMeterUtils.getResString("minimum_param"));
        desc.add(JMeterUtils.getResString("maximum_param"));
        desc.add(JMeterUtils.getResString("function_name_param"));
    }

    private CompoundVariable varName, minimum, maximum;

    /**
     * No-arg constructor.
     */
    public Random()
    {
    }

    /**
     * Clone this Add object.
     *
     * @return A new Add object.
     */
    public Object clone()
    {
        Random newRandom = new Random();
        return newRandom;
    }

    /**
     * Execute the function.
     *
     * @see Function#execute(SampleResult, Sampler)
     */
    public synchronized String execute(
        SampleResult previousResult,
        Sampler currentSampler)
        throws InvalidVariableException
    {

        JMeterVariables vars = getVariables();

        int min = Integer.parseInt(minimum.execute());
        int max = Integer.parseInt(maximum.execute());

        int rand = (int) Math.round(min + Math.random() * (max - min));

        String randString = Integer.toString(rand);
        vars.put(varName.execute(), randString);

        return randString;

    }

    /**
     * Set the parameters for the function.
     *
     * @see Function#setParameters(Collection)
     */
    public void setParameters(Collection parameters)
        throws InvalidVariableException
    {
        Object[] values = parameters.toArray();

        if (values.length < 3)
        {
            throw new InvalidVariableException();
        }
        else
        {
            varName = (CompoundVariable) values[2];
            minimum = (CompoundVariable) values[0];
            maximum = (CompoundVariable) values[1];
        }

    }

    /**
     * Get the invocation key for this function.
     *
     * @see Function#getReferenceKey()
     */
    public String getReferenceKey()
    {
        return KEY;
    }

    /**
     * Get the description of this function.
     *
     * @see Function#getArgumentDesc()
     */
    public List getArgumentDesc()
    {
        return desc;
    }

}
