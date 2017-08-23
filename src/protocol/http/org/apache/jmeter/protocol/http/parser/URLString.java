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
 * 
*/

package org.apache.jmeter.protocol.http.parser;

import java.net.URL;

/**
 * Helper class to allow URLs to be stored in Collections without
 * incurring the cost of the hostname lookup performed by the
 * URL methods equals() and hashCode()
 * URL is a final class, so cannot be extended ...
 * 
 * @version $Revision: 325238 $ $Date: 2005-03-22 14:15:32 -0800 (Tue, 22 Mar 2005) $
 */
public class URLString
	implements Comparable // To allow use in Sorted Collections
{

     private URL url;
     private String urlAsString;
     private int hashCode;
     
    private URLString()// not instantiable
    {
    }

	public URLString(URL u)
	{
		url=u;
		urlAsString=u.toExternalForm();
		/*
		 * TODO improve string version to better match browser behaviour?
		 * e.g. do browsers regard http://host/ and http://Host:80/ as the
		 * same? If so, it would be better to reflect this in the string
		*/
		
		hashCode=urlAsString.hashCode();
	}

    /*
     * Parsers can return the URL as a string if it does not parse properly
     */
	public URLString(String s)
	{
		url=null;
		urlAsString=s;
		hashCode=urlAsString.hashCode();
	}

    public String toString()
    {
    	return urlAsString;
    }

    public URL getURL()
    {
    	return url;
    }
    
    public int compareTo(Object o)
    {
    	return urlAsString.compareTo(o.toString());
    }
    
    public boolean equals(Object o)
    {
    	return (o instanceof URLString && urlAsString.equals(o.toString()));
    }

	public int hashCode()
	{
		return hashCode;
	}
}
