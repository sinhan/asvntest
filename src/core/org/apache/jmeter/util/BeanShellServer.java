/*
 * Copyright 2003-2005 The Apache Software Foundation.
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

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Implements a BeanShell server to allow access to JMeter variables and
 * methods.
 * 
 * To enable, define the JMeter property: beanshell.server.port (see
 * JMeter.java) beanshell.server.file (optional, startup file)
 * 
 * @version $Revision: 391788 $ Last updated: $Date: 2006-04-05 13:27:14 -0700 (Wed, 05 Apr 2006) $
 */
public class BeanShellServer implements Runnable {

	private static final Logger log = LoggingManager.getLoggerForClass();

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

	// For use by the server script
	private static void setprop(String s, String v) {
		JMeterUtils.getJMeterProperties().setProperty(s, v);
	}

	public void run() {

		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		try {
			Class Interpreter = loader.loadClass("bsh.Interpreter");//$NON-NLS-1$
			Object instance = Interpreter.newInstance();
			Class string = String.class;
			Class object = Object.class;

			Method eval = Interpreter.getMethod("eval", new Class[] { string });//$NON-NLS-1$
			Method setObj = Interpreter.getMethod("set", new Class[] { string, object });//$NON-NLS-1$
			Method setInt = Interpreter.getMethod("set", new Class[] { string, int.class });//$NON-NLS-1$
			Method source = Interpreter.getMethod("source", new Class[] { string });//$NON-NLS-1$

			setObj.invoke(instance, new Object[] { "t", this });//$NON-NLS-1$
			setInt.invoke(instance, new Object[] { "portnum", new Integer(serverport) });//$NON-NLS-1$

			if (serverfile.length() > 0) {
				try {
					source.invoke(instance, new Object[] { serverfile });
				} catch (InvocationTargetException e1) {
					log.warn("Could not source " + serverfile);
					// JDK1.4: Throwable t= e1.getCause();
					// JDK1.4: if (t != null) log.warn(t.toString());
				}
			}
			eval.invoke(instance, new Object[] { "setAccessibility(true);" });//$NON-NLS-1$
			eval.invoke(instance, new Object[] { "server(portnum);" });//$NON-NLS-1$

		} catch (ClassNotFoundException e) {
			log.error("Beanshell Interpreter not found");
		} catch (Exception e) {
			log.error("Problem starting BeanShell server ", e);
		}
	}
}
