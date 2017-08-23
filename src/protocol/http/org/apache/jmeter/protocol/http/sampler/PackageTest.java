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

/*
 * Created on Jul 16, 2003
 */
package org.apache.jmeter.protocol.http.sampler;

import junit.framework.TestCase;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.protocol.http.config.gui.HttpDefaultsGui;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.util.HTTPArgument;

/**
 * @author ano ano
 * @version $Revision: 324266 $ last updated $Date: 2004-02-11 16:33:56 -0800 (Wed, 11 Feb 2004) $
 */
public class PackageTest extends TestCase
{
    public PackageTest(String arg0)
    {
        super(arg0);
    }

    public void testConfiguring() throws Exception
    {
        HTTPSampler sampler =
            (HTTPSampler) new HttpTestSampleGui().createTestElement();
        sampler.addArgument("arg1", "val1");
        ConfigTestElement config =
            (ConfigTestElement) new HttpDefaultsGui().createTestElement();
        (
            (Arguments) config
                .getProperty(HTTPSampler.ARGUMENTS)
                .getObjectValue())
                .addArgument(
            new HTTPArgument("config1", "configValue"));
        config.setRunningVersion(true);
        sampler.setRunningVersion(true);
        sampler.setRunningVersion(true);
        sampler.addTestElement(config);
        assertEquals(
            "config1=configValue",
            sampler.getArguments().getArgument(1).toString());
        sampler.recoverRunningVersion();
        config.recoverRunningVersion();
        assertEquals(1, sampler.getArguments().getArgumentCount());
        sampler.addTestElement(config);
        assertEquals(
            "config1=configValue",
            sampler.getArguments().getArgument(1).toString());
    }
}
