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

package org.htmlparser.tests.visitorsTests;

import org.htmlparser.Parser;
import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.visitors.UrlModifyingVisitor;

public class UrlModifyingVisitorTest extends ParserTestCase
{
    private static final String HTML_WITH_LINK =
        "<HTML><BODY>"
            + "<A HREF=\"mylink.html\"><IMG SRC=\"mypic.jpg\">"
            + "</A><IMG SRC=\"mysecondimage.gif\">"
            + "</BODY></HTML>";

    private static final String MODIFIED_HTML =
        "<HTML><BODY>"
            + "<A HREF=\"localhost://mylink.html\">"
            + "<IMG SRC=\"localhost://mypic.jpg\"></A>"
            + "<IMG SRC=\"localhost://mysecondimage.gif\">"
            + "</BODY></HTML>";

    public UrlModifyingVisitorTest(String name)
    {
        super(name);
    }

    public void testUrlModificationWithVisitor() throws Exception
    {
        Parser parser = Parser.createParser(HTML_WITH_LINK);
        UrlModifyingVisitor visitor =
            new UrlModifyingVisitor(parser, "localhost://");
        parser.visitAllNodesWith(visitor);
        assertStringEquals(
            "Expected HTML",
            MODIFIED_HTML,
            visitor.getModifiedResult());
    }
}