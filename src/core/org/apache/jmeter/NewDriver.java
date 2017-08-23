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
package org.apache.jmeter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;





/************************************************************
 *  Title: JMeter Description: Copyright: Copyright (c) 2000 Company: Apache
 *
 *@author     Michael Stover
 *@created    March 13, 2001
 *@version    1.0
 ***********************************************************/

public class NewDriver
{
	private static URLClassLoader loader;	
	private static String jmDir;
	

	
	static {
		List jars = new LinkedList();
		String cp = System.getProperty("java.class.path");
		
		//Find JMeter home dir
		StringTokenizer tok = new StringTokenizer(cp, File.pathSeparator);
		if (tok.countTokens() == 1) {
			File jar = new File(tok.nextToken());
			try {
				jmDir = jar.getCanonicalFile().getParentFile().getParent();
			} catch (IOException e) {
 			}
 		}
 		else
 		{
 			File userDir = new File(System.getProperty("user.dir"));
			jmDir = userDir.getAbsoluteFile().getParent();
 		}
 		
		StringBuffer classpath = new StringBuffer();
		File[] libDirs = new File[]{new File(jmDir+File.separator+"lib"),
				new File(jmDir+File.separator+"lib"+File.separator+"ext")};
		for(int a = 0;a < libDirs.length;a++)
		{
			File[] libJars = libDirs[a].listFiles(new FilenameFilter() {
			public boolean accept(File dir,String name) {
				if(name.endsWith(".jar"))
					return true;
				return false;
			}});
			for(int i = 0;i < libJars.length;i++)
			{
				try
				{
					jars.add(new URL("file","",libJars[i].getPath()));
					classpath.append(System.getProperty("path.separator"));
					classpath.append(libJars[i].getPath());
				}
				catch(MalformedURLException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		System.setProperty("java.class.path",System.getProperty("java.class.path")+classpath.toString());
		loader = new URLClassLoader((URL[])jars.toArray(new URL[0]));
		try {
			loader.loadClass("org.apache.jmeter.gui.GuiPackage");
			loader.loadClass("org.apache.jmeter.gui.JMeterFileFilter");
			loader.loadClass("org.apache.jmeter.gui.MainFrame");
			loader.loadClass("org.apache.jmeter.gui.action.ActionRouter");
			loader.loadClass("org.apache.jmeter.gui.action.CheckDirty");
			loader.loadClass("org.apache.jmeter.gui.tree.JMeterTreeListener");
			loader.loadClass("org.apache.jmeter.gui.tree.JMeterTreeModel");
			loader.loadClass("org.apache.jmeter.gui.util.ComponentUtil");
			loader.loadClass("org.apache.jmeter.util.JMeterUtils");
			loader.loadClass("javax.xml.parsers.SAXParserFactory");
			loader.loadClass("org.apache.xerces.jaxp.SAXParserFactoryImpl");
		
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static String getJMeterDir()
	{
		return jmDir;
	}
	
	private NewDriver(String[] args)
	{
		
	}

	/************************************************************
	 *  The main program for the NewDriver class
	 *
	 *@param  args  The command line arguments
	 ***********************************************************/
	public static void main(String[] args)
	{	
		Thread.currentThread().setContextClassLoader(loader);
		if (System.getProperty("log4j.configuration") == null)
		{
			File conf = new File(jmDir, "bin" + File.separator + "log4j.conf");
			System.setProperty("log4j.configuration", "file:" + conf);
		}
		
			try {
				Class JMeter = loader.loadClass("org.apache.jmeter.JMeter");
				Object instance = JMeter.newInstance();
				Method startup = JMeter.getMethod("start",new Class[]{(new String[0]).getClass()});
				startup.invoke(instance,new Object[]{args});
				
			} catch(Exception e) {
				e.printStackTrace();
			} 		
	}
}
