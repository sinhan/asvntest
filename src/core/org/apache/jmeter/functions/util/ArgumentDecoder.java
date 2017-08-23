package org.apache.jmeter.functions.util;

import org.apache.oro.text.perl.Perl5Util;

/**
 * @version $Revision: 323369 $
 */
public class ArgumentDecoder
{

    private static Perl5Util util = new Perl5Util();
    private static String expression = "s#[\\\\](.)#$1#g";

    public static String decode(String s)
    {
        return util.substitute(expression, s);
    }

}
