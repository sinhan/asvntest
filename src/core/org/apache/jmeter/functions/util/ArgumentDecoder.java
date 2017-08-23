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

package org.apache.jmeter.functions.util;

import org.apache.oro.text.perl.Perl5Util;

/**
 * @version $Revision: 324292 $
 */
public final class ArgumentDecoder
{
    private static Perl5Util util = new Perl5Util();
    private static String expression = "s#[\\\\](.)#$1#g";

    public static String decode(String s)
    {
        return util.substitute(expression, s);
    }

    /**
     * Prevent instantiation of utility class.
     */
    private ArgumentDecoder()
    {
    }
}