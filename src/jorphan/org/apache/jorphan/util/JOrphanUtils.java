// $Header$
/*
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

package org.apache.jorphan.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Vector;

import org.apache.log.Logger;
import org.apache.jorphan.logging.LoggingManager;

import junit.framework.TestCase;

/**
 * This class contains frequently-used static utility methods.
 * 
 * @author <a href="mailto://jsalvata@atg.com">Jordi Salvat i Alabart</a>
 *         Created 27th December 2002
 * @version $Revision: 325810 $ Last updated: $Date: 2005-10-01 18:12:31 -0700 (Sat, 01 Oct 2005) $
 */
public final class JOrphanUtils {
	private static Logger log = LoggingManager.getLoggerForClass();

	/**
	 * Private constructor to prevent instantiation.
	 */
	private JOrphanUtils() {
	}

	/**
	 * This is _almost_ equivalent to the String.split method in JDK 1.4. It is
	 * here to enable us to support earlier JDKs.
	 * 
	 * Note that unlike JDK1.4 spilt(), it ignores leading split Characters.
	 * 
	 * <P>
	 * This piece of code used to be part of JMeterUtils, but was moved here
	 * because some JOrphan classes use it too.
	 * 
	 * @param splittee
	 *            String to be split
	 * @param splitChar
	 *            Character to split the string on
	 * @return Array of all the tokens.
	 */
	public static String[] split(String splittee, String splitChar,boolean truncate) {
		if (splittee == null || splitChar == null) {
			return new String[0];
		}
		int spot;
        if(truncate) {
    		while ((spot = splittee.indexOf(splitChar + splitChar)) != -1) {
    			splittee = splittee.substring(0, spot + splitChar.length())
    					+ splittee.substring(spot + 2 * splitChar.length(), splittee.length());
    		}
            if(splittee.startsWith(splitChar)) splittee = splittee.substring(splitChar.length());
        }
		Vector returns = new Vector();
		int start = 0;
		int length = splittee.length();
		spot = 0;
		while (start < length && (spot = splittee.indexOf(splitChar, start)) > -1) {
			if (spot > 0) {
				returns.addElement(splittee.substring(start, spot));
			}
            else
            {
                returns.addElement("");
            }
			start = spot + splitChar.length();
		}
		if (start < length) {
			returns.add(splittee.substring(start));
		}
		String[] values = new String[returns.size()];
		returns.copyInto(values);
		return values;
	}
    
    public static String[] split(String splittee,String splitChar)
    {
        return split(splittee,splitChar,true);
    }

	private static final String SPACES = "                                 ";

	private static final int SPACES_LEN = SPACES.length();

	/**
	 * Right aligns some text in a StringBuffer N.B. modifies the input buffer
	 * 
	 * @param in
	 *            StringBuffer containing some text
	 * @param len
	 *            output length desired
	 * @return input StringBuffer, with leading spaces
	 */
	public static StringBuffer rightAlign(StringBuffer in, int len) {
		int pfx = len - in.length();
		if (pfx <= 0)
			return in;
		if (pfx > SPACES_LEN)
			pfx = SPACES_LEN;
		in.insert(0, SPACES.substring(0, pfx));
		return in;
	}

	/**
	 * Left aligns some text in a StringBuffer N.B. modifies the input buffer
	 * 
	 * @param in
	 *            StringBuffer containing some text
	 * @param len
	 *            output length desired
	 * @return input StringBuffer, with trailing spaces
	 */
	public static StringBuffer leftAlign(StringBuffer in, int len) {
		int sfx = len - in.length();
		if (sfx <= 0)
			return in;
		if (sfx > SPACES_LEN)
			sfx = SPACES_LEN;
		in.append(SPACES.substring(0, sfx));
		return in;
	}

	/**
	 * Convert a boolean to its string representation Equivalent to
	 * Boolean.valueOf(boolean).toString() but valid also for JDK 1.3, which
	 * does not have valueOf(boolean)
	 * 
	 * @param value
	 *            boolean to convert
	 * @return "true" or "false"
	 */
	public static String booleanToString(boolean value) {
		return value ? "true" : "false";
	}

	/**
	 * Convert a boolean to its string representation Equivalent to
	 * Boolean.valueOf(boolean).toString().toUpperCase() but valid also for JDK
	 * 1.3, which does not have valueOf(boolean)
	 * 
	 * @param value
	 *            boolean to convert
	 * @return "TRUE" or "FALSE"
	 */
	public static String booleanToSTRING(boolean value) {
		return value ? "TRUE" : "FALSE";
	}

	/**
	 * Version of Boolean.valueOf(boolean) for JDK 1.3
	 * 
	 * @param value
	 *            boolean to convert
	 * @return Boolean.TRUE or Boolean.FALSE
	 */
	public static Boolean valueOf(boolean value) {
		return value ? Boolean.TRUE : Boolean.FALSE;
	}

	private static Method decodeMethod = null;

	private static Method encodeMethod = null;

	static {
		Class URLEncoder = URLEncoder.class;
		Class URLDecoder = URLDecoder.class;
		Class[] argTypes = { String.class, String.class };
		try {
			decodeMethod = URLDecoder.getMethod("decode", argTypes);
			encodeMethod = URLEncoder.getMethod("encode", argTypes);
			// System.out.println("Using JDK1.4 xxcode() calls");
		} catch (Exception e) {
			// e.printStackTrace();
		}
		// System.out.println("java.version="+System.getProperty("java.version"));
	}

	/**
	 * Version of URLEncoder().encode(string,encoding) for JDK1.3 Also supports
	 * JDK1.4 (but will be a bit slower)
	 * 
	 * @param string
	 *            to be encoded
	 * @param encoding
	 *            (ignored for JDK1.3)
	 * @return the encoded string
	 */
	public static String encode(String string, String encoding) throws UnsupportedEncodingException {
		if (encodeMethod != null) {
			// JDK1.4: return URLEncoder.encode(string, encoding);
			Object args[] = { string, encoding };
			try {
				return (String) encodeMethod.invoke(null, args);
			} catch (Exception e) {
				log.warn("Error trying to encode", e);
				return string;
			}
		} else {
			return URLEncoder.encode(string);// JDK1.3
		}

	}

	/**
	 * Version of URLDecoder().decode(string,encoding) for JDK1.3 Also supports
	 * JDK1.4 (but will be a bit slower)
	 * 
	 * @param string
	 *            to be decoded
	 * @param encoding
	 *            (ignored for JDK1.3)
	 * @return the encoded string
	 */
	public static String decode(String string, String encoding) throws UnsupportedEncodingException {
		if (decodeMethod != null) {
			// JDK1.4: return URLDecoder.decode(string, encoding);
			Object args[] = { string, encoding };
			try {
				return (String) decodeMethod.invoke(null, args);
			} catch (Exception e) {
				log.warn("Error trying to decode", e);
				return string;
			}
		} else {
			return URLDecoder.decode(string); // JDK1.3
		}
	}

	/**
	 * Simple-minded String.replace() for JDK1.3 Should probably be recoded...
	 * 
	 * @param source
	 *            input string
	 * @param search
	 *            string to look for (no regular expressions)
	 * @param replace
	 *            string to replace the search string
	 * @return the output string
	 */
	public static String replaceFirst(String source, String search, String replace) {
		int start = source.indexOf(search);
		int len = search.length();
		if (start == -1)
			return source;
		if (start == 0)
			return replace + source.substring(len);
		return source.substring(0, start) + replace + source.substring(start + len);
	}

	/**
	 * Returns a slice of a byte array.
	 * 
	 * TODO - add bounds checking?
	 * 
	 * @param array -
	 *            input array
	 * @param begin -
	 *            start of slice
	 * @param end -
	 *            end of slice
	 * @return slice from the input array
	 */
	public static byte[] getByteArraySlice(byte[] array, int begin, int end) {
		byte[] slice = new byte[(end - begin + 1)];
		int count = 0;
		for (int i = begin; i <= end; i++) {
			slice[count] = array[i];
			count++;
		}

		return slice;
	}
}
