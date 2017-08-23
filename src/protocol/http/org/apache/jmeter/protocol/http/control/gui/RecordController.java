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

package org.apache.jmeter.protocol.http.control.gui;

import org.apache.jmeter.control.gui.LogicControllerGui;
import org.apache.jmeter.protocol.http.control.RecordingController;
import org.apache.jmeter.testelement.TestElement;

/**
 * @version $Revision: 324395 $
 */
public class RecordController extends LogicControllerGui
{
    public String getLabelResource()
    {
        return "record_controller_title";
    }

    public TestElement createTestElement()
    {
        RecordingController con = new RecordingController();
        this.configureTestElement(con);
        return con;
    }
}