package org.apache.jmeter.functions.util;

import org.apache.oro.text.perl.Perl5Util;

/**
 * @version $Revision: 323431 $
 */
public final class ArgumentEncoder
{
    private static Perl5Util util = new Perl5Util();
    private static String expression = "s#([${}(),\\\\])#\\$1#g";

    public static String encode(String s)
    {
        return util.substitute(expression, s);
    }
    
    /**
     * Prevent instantiation of utility class.
     */
    private ArgumentEncoder()
    {
    }
}
