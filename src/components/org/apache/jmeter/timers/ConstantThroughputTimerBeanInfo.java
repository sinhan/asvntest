// $Header$
/*
 * Copyright 2004 The Apache Software Foundation.
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

package org.apache.jmeter.timers;

import java.beans.PropertyDescriptor;

import org.apache.jmeter.testbeans.BeanInfoSupport;

/**
 * BeanInfo for the ConstantThroughputTimer.
 *
 * @version $Revision: 325061 $ updated on $Date: 2004-12-13 16:37:13 -0800 (Mon, 13 Dec 2004) $
 */
public class ConstantThroughputTimerBeanInfo extends BeanInfoSupport
{
    public ConstantThroughputTimerBeanInfo()
    {
        super(ConstantThroughputTimer.class);
        
        createPropertyGroup("delay", new String[] { "throughput", "legacyMode" });

        PropertyDescriptor p= property("throughput");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, new Double(0.0));
        
        p = property("legacyMode");
        p.setValue(NOT_UNDEFINED,Boolean.TRUE);
        p.setValue(DEFAULT, Boolean.FALSE);
        p.setValue(NOT_OTHER,Boolean.TRUE);
        p.setValue(NOT_EXPRESSION,Boolean.TRUE);
    }
}