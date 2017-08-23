/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
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
 * 
 * @author  Michael Stover
 * @author  <a href="mailto:jsalvata@apache.org">Jordi Salvat i Alabart</a>
 * @version $Id: PreCompiler.java 323904 2003-12-16 14:37:36Z jsalvata $
 */
package org.apache.jmeter.engine;

import java.util.Map;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.engine.util.ValueReplacer;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.HashTreeTraverser;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class PreCompiler implements HashTreeTraverser
{
    transient private static Logger log = LoggingManager.getLoggerForClass();
    private ValueReplacer replacer;

    public PreCompiler()
    {
        replacer = new ValueReplacer();
    }

    /* (non-Javadoc)
     * @see HashTreeTraverser#addNode(Object, HashTree)
     */
    public void addNode(Object node, HashTree subTree)
    {
        if (node instanceof TestPlan)
        {
            Map args= ((TestPlan)node).getUserDefinedVariables();
            replacer.setUserDefinedVariables(args);
            JMeterVariables vars= new JMeterVariables();
            vars.putAll(args);
            JMeterContextService.getContext().setVariables(vars);
        }
        else if (node instanceof TestElement)
        {
            try
            {
                replacer.replaceValues((TestElement) node);
                ((TestElement)node).setRunningVersion(true);
            }
            catch (InvalidVariableException e)
            {
                log.error("invalid variables", e);
            }
        }

        if (node instanceof Arguments)
        {
            Map args= ((Arguments)node).getArgumentsAsMap();
            replacer.addVariables(args);
            JMeterContextService.getContext().getVariables().putAll(args);
        }
    }

    /* (non-Javadoc)
     * @see HashTreeTraverser#subtractNode()
     */
    public void subtractNode()
    {
    }

    /* (non-Javadoc)
     * @see HashTreeTraverser#processPath()
     */
    public void processPath()
    {
    }
}
