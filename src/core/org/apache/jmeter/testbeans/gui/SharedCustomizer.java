// $Header$
/*
 * Copyright 2004 The Apache Software Foundation.
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
 */
package org.apache.jmeter.testbeans.gui;

import java.beans.Customizer;

/**
 * Tagging interface to mark a customizer class as shareable among elements
 * of the same type.
 * <p>
 * The interface is equivalent to Customizer -- the only difference is that setElement
 * can be called multiple times to change the element it works on.
 * 
 * @author <a href="mailto:jsalvata@apache.org">Jordi Salvat i Alabart</a>
 * @version $Revision: 324239 $ updated on $Date: 2004-02-10 13:24:01 -0800 (Tue, 10 Feb 2004) $
 */
public interface SharedCustomizer extends Customizer
{
}
