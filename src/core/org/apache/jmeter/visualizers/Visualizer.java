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

package org.apache.jmeter.visualizers;
import org.apache.jmeter.samplers.SampleResult;

/**
 * Implement this method to be a Visualizer for JMeter.  This interface defines
 * a single method, "add()", that provides the means by which
 * {@link org.apache.jmeter.samplers.SampleResult SampleResults} are passed to
 * the implementing visualizer for display/logging.  The easiest way to create
 * the visualizer is to extend the
 * {@link org.apache.jmeter.visualizers.gui.AbstractVisualizer}
 * class.
 *
 * @author  <a href="mailto:mstoer1@apache.org">Michael Stover</a>
 * @version $Revision: 324283 $ $Date: 2004-02-12 18:40:55 -0800 (Thu, 12 Feb 2004) $
 */
public interface Visualizer
{
    /**
     * This method is called by sampling thread to inform
     * the visualizer about the arrival of a new sample.
     */
    public void add(SampleResult sample);
}
