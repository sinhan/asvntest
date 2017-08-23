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

import java.io.Serializable;

/**
 * @author    Michael Stover
 * Created   March 13, 2001
 * @version   $Revision: 324292 $ Last updated: $Date: 2004-02-13 19:34:31 -0800 (Fri, 13 Feb 2004) $
 */
public class WorkBench extends AbstractTestElement implements Serializable
{

    /**
     * Constructor for the WorkBench object.
     */
    public WorkBench(String name, boolean isRootNode)
    {
        setName(name);
    }

    public WorkBench()
    {
    }
}
