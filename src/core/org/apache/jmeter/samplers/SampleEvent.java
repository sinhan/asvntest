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

import java.io.Serializable;

/**
 * Packages information regarding the target of a sample event,
 * such as the result from that event and the thread group it
 * ran in.
 * 
 * @version $Revision: 324283 $
 */
public class SampleEvent implements Serializable
{
    SampleResult result;
    String threadGroup;

    public SampleEvent()
    {
    }

    public SampleEvent(SampleResult result, String threadGroup)
    {
        this.result = result;
        this.threadGroup = threadGroup;
    }

    public SampleResult getResult()
    {
        return result;
    }

    public String getThreadGroup()
    {
        return threadGroup;
    }
}
