// $Header$
/*
 * Copyright 2003-2004 The Apache Software Foundation.
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

package org.apache.jmeter.testelement.property;

/**
 * @author Administrator
 * @version $Revision: 324283 $ on $Date: 2004-02-12 18:40:55 -0800 (Thu, 12 Feb 2004) $
 */
public interface PropertyIterator
{
    public boolean hasNext();
    
    public JMeterProperty next();
    
    public void remove();
}
