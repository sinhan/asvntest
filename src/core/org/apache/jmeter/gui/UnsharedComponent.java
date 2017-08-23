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

package org.apache.jmeter.gui;

/**
 * Marker interface indicating that an instance of a component cannot be shared.
 * The GUI instance will be shared among all test elements of a given type if
 * the GUI component class does not implement this interface.
 * 
 * @author unknown
 * @version $Revision: 324292 $
 */
public interface UnsharedComponent
{
}
