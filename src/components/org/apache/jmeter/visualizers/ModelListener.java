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


/**
 * The Interface to be implemented by any class that wants to be notified by
 * model which makes use of this callback-Interface.
 *
 * @author     <a href="mailto:wolfram.rittmeyer@web.de">Wolfram Rittmeyer</a>
 * @version    $Revision: 324280 $ $Date: 2004-02-12 17:48:46 -0800 (Thu, 12 Feb 2004) $
 */
public interface ModelListener
{

    /**
     * Informs the Visualizer that the model has changed.
     */
    public void updateVisualizer();

    /**
     * Informs the Visualizer that a message should be displayed.
     */
    public void displayMessage(String messageString, boolean isError);
}
