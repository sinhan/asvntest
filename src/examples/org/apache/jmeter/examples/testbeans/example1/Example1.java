//$Header$
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

package org.apache.jmeter.examples.testbeans.example1;

import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testbeans.TestBean;

/**
 * This TestBean is just an example about how to write testbeans. The intent is to demonstrate
 * usage of the TestBean features to podential TestBean developers. Note that only the class's
 * introspector view matters: the methods do nothing -- nothing useful, in any case.
 */
public class Example1 extends TestBean implements Sampler {
	public SampleResult sample(Entry e) {
		return new SampleResult();
	}
	
	// A String property:
	public void setMyStringProperty(String s)
	{};
	public String getMyStringProperty()
	{return "";}
	
	// A String[] property:
	public void setMyStrings(String[] s)
	{};
	public String[] getMyStrings()
	{return null;}
}
