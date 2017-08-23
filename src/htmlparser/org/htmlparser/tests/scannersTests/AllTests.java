// $Header$
/*
 * ====================================================================
 * Copyright 2002-2004 The Apache Software Foundation.
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

// The developers of JMeter and Apache are greatful to the developers
// of HTMLParser for giving Apache Software Foundation a non-exclusive
// license. The performance benefits of HTMLParser are clear and the
// users of JMeter will benefit from the hard work the HTMLParser
// team. For detailed information about HTMLParser, the project is
// hosted on sourceforge at http://htmlparser.sourceforge.net/.
//
// HTMLParser was originally created by Somik Raha in 2000. Since then
// a healthy community of users has formed and helped refine the
// design so that it is able to tackle the difficult task of parsing
// dirty HTML. Derrick Oswald is the current lead developer and was kind
// enough to assist JMeter.

package org.htmlparser.tests.scannersTests;

import junit.framework.TestSuite;

public class AllTests extends junit.framework.TestCase
{

    public AllTests(String name)
    {
        super(name);
    }

    public static TestSuite suite()
    {
        TestSuite suite = new TestSuite("Scanner Tests");
        suite.addTestSuite(TagScannerTest.class);
        suite.addTestSuite(AppletScannerTest.class);
        suite.addTestSuite(ScriptScannerTest.class);
        suite.addTestSuite(ImageScannerTest.class);
        suite.addTestSuite(LinkScannerTest.class);
        suite.addTestSuite(StyleScannerTest.class);
        suite.addTestSuite(MetaTagScannerTest.class);
        suite.addTestSuite(TitleScannerTest.class);
        suite.addTestSuite(FormScannerTest.class);
        suite.addTestSuite(FrameScannerTest.class);
        suite.addTestSuite(FrameSetScannerTest.class);
        suite.addTestSuite(InputTagScannerTest.class);
        suite.addTestSuite(OptionTagScannerTest.class);
        suite.addTestSuite(SelectTagScannerTest.class);
        suite.addTestSuite(TextareaTagScannerTest.class);
        suite.addTestSuite(BaseHREFScannerTest.class);
        suite.addTestSuite(JspScannerTest.class);
        suite.addTestSuite(TableScannerTest.class);
        suite.addTestSuite(SpanScannerTest.class);
        suite.addTestSuite(DivScannerTest.class);
        suite.addTestSuite(LabelScannerTest.class);
        suite.addTestSuite(BodyScannerTest.class);
        suite.addTestSuite(CompositeTagScannerTest.class);
        suite.addTestSuite(HeadScannerTest.class);
        return suite;
    }

    /**
     * Mainline for all suites of tests.
     * @param args Command line arguments. The following options
     * are understood:
     * <pre>
     * -text  -- use junit.textui.TestRunner
     * -awt   -- use junit.awtui.TestRunner
     * -swing -- use junit.swingui.TestRunner (default)
     * </pre>
     * All other options are passed on to the junit framework.
     */
    public static void main(String[] args)
    {
        String runner;
        int i;
        String arguments[];
        Class cls;

        runner = null;
        for (i = 0;(i < args.length) && (null == runner); i++)
        {
            if (args[i].equalsIgnoreCase("-text"))
                runner = "junit.textui.TestRunner";
            else if (args[i].equalsIgnoreCase("-awt"))
                runner = "junit.awtui.TestRunner";
            else if (args[i].equalsIgnoreCase("-swing"))
                runner = "junit.swingui.TestRunner";
        }
        if (null != runner)
        {
            // remove it from the arguments
            arguments = new String[args.length - 1];
            System.arraycopy(args, 0, arguments, 0, i - 1);
            System.arraycopy(args, i, arguments, i - 1, args.length - i);
            args = arguments;
        }
        else
            runner = "junit.swingui.TestRunner";

        // append the test class
        arguments = new String[args.length + 1];
        System.arraycopy(args, 0, arguments, 0, args.length);
        arguments[args.length] = "org.htmlparser.tests.scannersTests.AllTests";

        // invoke main() of the test runner
        try
        {
            cls = Class.forName(runner);
            java.lang.reflect.Method method =
                cls.getDeclaredMethod("main", new Class[] { String[].class });
            method.invoke(null, new Object[] { arguments });
        }
        catch (Throwable t)
        {
            System.err.println("cannot run unit test (" + t.getMessage() + ")");
        }
    }
}
