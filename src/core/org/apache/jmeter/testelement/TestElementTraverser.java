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

package org.apache.jmeter.testelement;

import org.apache.jmeter.testelement.property.JMeterProperty;

/**
 * For traversing Test Elements, which contain property that can be other test
 * elements, strings, collections, maps, objects
 *
 * @version $Revision: 324292 $ on $Date: 2004-02-13 19:34:31 -0800 (Fri, 13 Feb 2004) $
 */
public interface TestElementTraverser
{

    /**
     * Notification that a new test element is about to be traversed.  
     * @param el
     */
    public void startTestElement(TestElement el);
    
    /**
     * Notification that the test element is now done.
     * @param el
     */
    public void endTestElement(TestElement el);
    
    /**
     * Notification that a property is starting.  This could be a test element
     * property or a Map property - depends on the context.
     * @param key
     */
    public void startProperty(JMeterProperty key);
    
    /**
     * Notification that a property is ending.  Again, this could be a test
     * element or a Map property, dependig on the context.
     * @param key
     */
    public void endProperty(JMeterProperty key);
    
}
