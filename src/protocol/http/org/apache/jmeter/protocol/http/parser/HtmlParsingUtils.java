/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.jmeter.protocol.http.parser;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.config.Argument;
import org.apache.jmeter.junit.JMeterTestCase;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.apache.oro.text.PatternCacheLRU;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;
import org.xml.sax.SAXException;

/**
 * @author    Michael Stover
 * Created   June 14, 2001
 * @version   $Revision: 323909 $ Last updated: $Date: 2003-12-17 05:03:47 -0800 (Wed, 17 Dec 2003) $
 */
public final class HtmlParsingUtils implements Serializable
{
    transient private static Logger log = LoggingManager.getLoggerForClass();

    protected static String utfEncodingName;
    /* NOTUSED 
    private int compilerOptions =
        Perl5Compiler.CASE_INSENSITIVE_MASK
            | Perl5Compiler.MULTILINE_MASK
            | Perl5Compiler.READ_ONLY_MASK;
	*/
	
    private static PatternCacheLRU patternCache =
        new PatternCacheLRU(1000, new Perl5Compiler());

    private static ThreadLocal localMatcher = new ThreadLocal()
    {
        protected Object initialValue()
        {
            return new Perl5Matcher();
        }
    };

    /**
     * Private constructor to prevent instantiation.
     */
    private HtmlParsingUtils()
    {
    }

    public static synchronized boolean isAnchorMatched(
        HTTPSampler newLink,
        HTTPSampler config)
        throws MalformedPatternException
    {
        boolean ok = true;
        Perl5Matcher matcher = (Perl5Matcher) localMatcher.get();
        PropertyIterator iter = config.getArguments().iterator();

        String query = null;
        try
        {
            query = URLDecoder.decode(newLink.getQueryString(),"UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            // UTF-8 unsupported? You must be joking!
            log.error("UTF-8 encoding not supported!");
            throw new Error(e);
        }

        if (query == null && config.getArguments().getArgumentCount() > 0)
        {
            return false;
        }
        
        while (iter.hasNext())
        {
            Argument item = (Argument) iter.next().getObjectValue();
            if (query.indexOf(item.getName() + "=") == -1)
            {
                if (!(ok =
                    ok
                        && matcher.contains(
                            query,
                            patternCache.getPattern(
                                item.getName(),
                                Perl5Compiler.READ_ONLY_MASK))))
                {
                    return false;
                }
            }
        }

        if (config.getDomain() != null
            && config.getDomain().length() > 0
            && !newLink.getDomain().equals(config.getDomain()))
        {
            if (!(ok =
                ok
                    && matcher.matches(
                        newLink.getDomain(),
                        patternCache.getPattern(
                            config.getDomain(),
                            Perl5Compiler.READ_ONLY_MASK))))
            {
                return false;
            }
        }

        if (!newLink.getPath().equals(config.getPath())
            && !matcher.matches(
                newLink.getPath(),
                patternCache.getPattern(
                    "[/]*" + config.getPath(),
                    Perl5Compiler.READ_ONLY_MASK)))
        {
            return false;
        }

        if (!(ok =
            ok
                && matcher.matches(
                    newLink.getProtocol(),
                    patternCache.getPattern(
                        config.getProtocol(),
                        Perl5Compiler.READ_ONLY_MASK))))
        {
            return false;
        }

        return ok;
    }

    public static synchronized boolean isArgumentMatched(
        Argument arg,
        Argument patternArg)
        throws MalformedPatternException
    {
        Perl5Matcher matcher = (Perl5Matcher) localMatcher.get();
        return (
            arg.getName().equals(patternArg.getName())
                || matcher.matches(
                    arg.getName(),
                    patternCache.getPattern(
                        patternArg.getName(),
                        Perl5Compiler.READ_ONLY_MASK)))
            && (arg.getValue().equals(patternArg.getValue())
                || matcher.matches(
                    (String) arg.getValue(),
                    patternCache.getPattern(
                        (String) patternArg.getValue(),
                        Perl5Compiler.READ_ONLY_MASK)));
    }

    /**
     * Returns <code>tidy</code> as HTML parser.
     *
     * @return   a <code>tidy</code> HTML parser
     */
    public static Tidy getParser()
    {
        log.debug("Start : getParser1");
        Tidy tidy = new Tidy();
        tidy.setCharEncoding(org.w3c.tidy.Configuration.UTF8);
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);

        if (log.isDebugEnabled())
        {
            log.debug("getParser1 : tidy parser created - " + tidy);
        }

        log.debug("End : getParser1");

        return tidy;
    }

    /**
     * Returns a node representing a whole xml given an xml document.
     *
     * @param text              an xml document
     * @return                  a node representing a whole xml
     */
    public static Node getDOM(String text) throws SAXException
    {
        log.debug("Start : getDOM1");

        try
        {
            Node node =
                getParser().parseDOM(
                    new ByteArrayInputStream(
                        text.getBytes("UTF-8")),
                    null);

            if (log.isDebugEnabled())
            {
                log.debug("node : " + node);
            }

            log.debug("End : getDOM1");

            return node;
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("getDOM1 : Unsupported encoding exception - " + e);
            log.debug("End : getDOM1");
            throw new RuntimeException("UTF-8 encoding failed");
        }
    }

    public static Document createEmptyDoc()
    {
        return Tidy.createEmptyDocument();
    }

    /**
     * Create a new URL based on an HREF string plus a contextual URL object.
     * Given that an HREF string might be of three possible forms, some
     * processing is required.
     */
    public static HTTPSampler createUrlFromAnchor(
        String parsedUrlString,
        URL context)
        throws MalformedURLException
    {
        if (log.isDebugEnabled())
        {
            log.debug("Creating URL from Anchor: "+parsedUrlString
                +", base: "+context);
        }
        URL url= new URL(context, parsedUrlString);
        HTTPSampler sampler = new HTTPSampler();
        sampler.setDomain(url.getHost());
        sampler.setProtocol(url.getProtocol());
        sampler.setPort(url.getPort());
        sampler.setPath(url.getPath());
        sampler.parseArguments(url.getQuery());

        return sampler;
    }

    public static List createURLFromForm(
            Node doc, 
            URL context)
    {
        String selectName = null;
        LinkedList urlConfigs = new LinkedList();
        recurseForm(doc, urlConfigs, context, selectName, false);
        /*
         * NamedNodeMap atts = formNode.getAttributes();
         * if(atts.getNamedItem("action") == null)
         * {
         * throw new MalformedURLException();
         * }
         * String action = atts.getNamedItem("action").getNodeValue();
         * UrlConfig url = createUrlFromAnchor(action, context);
         * recurseForm(doc, url, selectName,true,formStart);
         */
        return urlConfigs;
    }

    private static boolean recurseForm(
        Node tempNode,
        LinkedList urlConfigs,
        URL context,
        String selectName,
        boolean inForm)
    {
        NamedNodeMap nodeAtts = tempNode.getAttributes();
        String tag = tempNode.getNodeName();
        try
        {
            if (inForm)
            {
                HTTPSampler url = (HTTPSampler) urlConfigs.getLast();
                if (tag.equalsIgnoreCase("form"))
                {
                    try
                    {
                        urlConfigs.add(createFormUrlConfig(tempNode, context));
                    }
                    catch (MalformedURLException e)
                    {
                        inForm = false;
                    }
                }
                else if (tag.equalsIgnoreCase("input"))
                {
                    url.addArgument(
                        getAttributeValue(nodeAtts, "name"),
                        getAttributeValue(nodeAtts, "value"));
                }
                else if (tag.equalsIgnoreCase("textarea"))
                {
                    try
                    {
                        url.addArgument(
                            getAttributeValue(nodeAtts, "name"),
                            tempNode.getFirstChild().getNodeValue());
                    }
                    catch (NullPointerException e)
                    {
                        url.addArgument(
                            getAttributeValue(nodeAtts, "name"),
                            "");
                    }
                }
                else if (tag.equalsIgnoreCase("select"))
                {
                    selectName = getAttributeValue(nodeAtts, "name");
                }
                else if (tag.equalsIgnoreCase("option"))
                {
                    String value = getAttributeValue(nodeAtts, "value");
                    if (value == null)
                    {
                        try
                        {
                            value = tempNode.getFirstChild().getNodeValue();
                        }
                        catch (NullPointerException e)
                        {
                            value = "";
                        }
                    }
                    url.addArgument(selectName, value);
                }
            }
            else if (tag.equalsIgnoreCase("form"))
            {
                try
                {
                    urlConfigs.add(createFormUrlConfig(tempNode, context));
                    inForm = true;
                }
                catch (MalformedURLException e)
                {
                    inForm = false;
                }
                try
                {
                    Thread.sleep(5000);
                }
                catch (Exception e)
                {
                }
            }
        }
        catch (Exception ex)
        {
            log.warn("Some bad HTML " + printNode(tempNode), ex);
        }
        NodeList childNodes = tempNode.getChildNodes();
        for (int x = 0; x < childNodes.getLength(); x++)
        {
            inForm =
                recurseForm(
                    childNodes.item(x),
                    urlConfigs,
                    context,
                    selectName,
                    inForm);
        }
        return inForm;
    }

    private static String getAttributeValue(NamedNodeMap att, String attName)
    {
        try
        {
            return att.getNamedItem(attName).getNodeValue();
        }
        catch (Exception ex)
        {
            return "";
        }
    }

    private static String printNode(Node node)
    {
        StringBuffer buf = new StringBuffer();
        buf.append("<");
        buf.append(node.getNodeName());
        NamedNodeMap atts = node.getAttributes();
        for (int x = 0; x < atts.getLength(); x++)
        {
            buf.append(" ");
            buf.append(atts.item(x).getNodeName());
            buf.append("=\"");
            buf.append(atts.item(x).getNodeValue());
            buf.append("\"");
        }

        buf.append(">");

        return buf.toString();
    }
    private static HTTPSampler createFormUrlConfig(
        Node tempNode,
        URL context)
        throws MalformedURLException
    {
        NamedNodeMap atts = tempNode.getAttributes();
        if (atts.getNamedItem("action") == null)
        {
            throw new MalformedURLException();
        }
        String action = atts.getNamedItem("action").getNodeValue();
        HTTPSampler url = createUrlFromAnchor(action, context);
        return url;
    }
    
///////////////////// Start of Test Code /////////////////

// TODO: need more tests

	public static class Test extends JMeterTestCase
	{

		public Test(String name)
		{
			super(name);
		}

		protected void setUp()
		{
		}
		
		public void testGetParser() throws Exception
		{
			getParser();
		}
		public void testGetDom() throws Exception
		{
			getDOM("<HTML></HTML>");
			getDOM("");
		}
		public void testIsArgumentMatched() throws Exception
		{
			Argument arg = new Argument();
			Argument argp = new Argument();
			assertTrue(isArgumentMatched(arg,argp));

			arg = new Argument("test","abcd");
			argp = new Argument("test","a.*d");
			assertTrue(isArgumentMatched(arg,argp));

			arg = new Argument("test","abcd");
			argp = new Argument("test","a.*e");
			assertFalse(isArgumentMatched(arg,argp));
		}
	}
}
