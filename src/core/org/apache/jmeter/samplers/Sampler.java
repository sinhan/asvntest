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

package org.apache.jmeter.samplers;

import org.apache.jmeter.testelement.TestElement;

/**
 * Classes which are able to generate information about an entry should
 * implement this interface.
 *
 * @author     unattributed
 * @version    $Revision: 324283 $ Last updated: $Date: 2004-02-12 18:40:55 -0800 (Thu, 12 Feb 2004) $
 */
public interface Sampler extends java.io.Serializable, TestElement
{
    public final static String HEADER = "sampler.HEADER";

    /**
     * Obtains statistics about the given Entry, and packages the information
     * into a SampleResult.
     */
    public SampleResult sample(Entry e);
}
