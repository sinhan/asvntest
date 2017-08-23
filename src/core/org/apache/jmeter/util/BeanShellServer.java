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

package org.apache.jmeter.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Implements a BeanShell server to allow access to JMeter variables and
 * methods.
 * 
 * To enable, define the JMeter property: beanshell.server.port (see
 * JMeter.java) beanshell.server.file (optional, startup file)
 * 
 * @version $Revision: 325542 $ Last updated: $Date: 2005-07-12 13:51:09 -0700 (Tue, 12 Jul 2005) $
 */
public class BeanShellServer implements Runnable {

	transient private static Logger log = LoggingManager.getLoggerForClass();

	private final int serverport;

	private final String serverfile;

	/**
	 * 
	 */
	public BeanShellServer(int port, String file) {
		super();
		serverfile = file;// can be the empty string
		serverport = port;
	}

	private BeanShellServer() {// do not use!
		super();
		serverport = 0;
		serverfile = "";
	}

	// For use by the server script
	private static String getprop(String s) {
		return JMeterUtils.getPropDefault(s, s);
	}

	private static void setprop(String s, String v) {
		JMeterUtils.getJMeterProperties().setProperty(s, v);
	}

	public void run() {

		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		try {
			Class Interpreter = loader.loadClass("bsh.Interpreter");
			Object instance = Interpreter.newInstance();
			Class string = String.class;
			Class object = Object.class;

			Method eval = Interpreter.getMethod("eval", new Class[] { string });
			Method setObj = Interpreter.getMethod("set", new Class[] { string, object });
			Method setInt = Interpreter.getMethod("set", new Class[] { string, int.class });
			Method source = Interpreter.getMethod("source", new Class[] { string });

			setObj.invoke(instance, new Object[] { "t", this });
			setInt.invoke(instance, new Object[] { "portnum", new Integer(serverport) });

			if (serverfile.length() > 0) {
				try {
					source.invoke(instance, new Object[] { serverfile });
				} catch (InvocationTargetException e1) {
					log.warn("Could not source " + serverfile);
					// JDK1.4: Throwable t= e1.getCause();
					// JDK1.4: if (t != null) log.warn(t.toString());
				}
			}
			eval.invoke(instance, new Object[] { "setAccessibility(true);" });
			eval.invoke(instance, new Object[] { "server(portnum);" });

		} catch (ClassNotFoundException e) {
			log.error("Beanshell Interpreter not found");
		} catch (Exception e) {
			log.error("Problem starting BeanShell server ", e);
		}
	}

	public static class Test extends TestCase {
		// private static Logger log = LoggingManager.getLoggerForClass();

		public Test() {
			super();
		}

		public void testServer() throws Exception {
			BeanShellServer bshs = new BeanShellServer(9876, "");
			assertNotNull(bshs);
			// Not sure we can test anything else here
		}

		public void testProps() throws Exception {
			if (JMeterUtils.getJMeterProperties() != null) {// Can't test
															// standalone
				assertNotNull("Property user.dir should not be null", getprop("user.dir"));
				setprop("beanshelltest", "xyz");
				assertEquals("xyz", getprop("beanshelltest"));
			}
		}
	}
}
