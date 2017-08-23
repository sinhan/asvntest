// $Header$
/*
 * Copyright 2001-2004 The Apache Software Foundation.
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

package org.apache.jmeter.engine;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;


/**
 * @version $Revision: 324782 $ Updated on: $Date: 2004-07-08 17:24:54 -0700 (Thu, 08 Jul 2004) $
 */
public class RemoteJMeterEngineImpl
    extends java.rmi.server.UnicastRemoteObject
    implements RemoteJMeterEngine
{
    transient private static Logger log = LoggingManager.getLoggerForClass();
    JMeterEngine backingEngine;

    public static final int DEFAULT_RMI_PORT = 1099;
    
    public RemoteJMeterEngineImpl() throws RemoteException
    {
    	init(DEFAULT_RMI_PORT);
    }
    public RemoteJMeterEngineImpl(int port) throws RemoteException
    {
       init(port == 0 ? DEFAULT_RMI_PORT : port);
    }

    private void init(int port) throws RemoteException
    {
    	log.info("Starting backing engine on "+port);
		log.debug("This = "+ this);
        try
        {
        	Registry reg = LocateRegistry.getRegistry(port);
            backingEngine =
                new StandardJMeterEngine(
                    InetAddress.getLocalHost().getHostName());
            reg.rebind("JMeterEngine", this);
            log.info("Bound to registry on port " + port);
        }
        catch(Exception ex){
			log.error(
				"rmiregistry needs to be running to start JMeter in server " +
				"mode\n\t" + ex.toString());
			// Throw an Exception to ensure caller knows ...
			throw new RemoteException("Cannot start. See server log file.");
        }
    }

    public void setHost(String host)
    {
        log.info("received host: "+host);
        backingEngine.setHost(host);
    }

    /**
     * Adds a feature to the ThreadGroup attribute of the RemoteJMeterEngineImpl
     * object.
     *
     * @param  testTree the feature to be added to the ThreadGroup attribute
     */
    public void configure(HashTree testTree) throws RemoteException
    {
        log.info("received test tree");
        backingEngine.configure(testTree);
    }

    public void runTest() throws RemoteException, JMeterEngineException
    {
        log.info("running test");
		log.debug("This = "+ this);
        backingEngine.runTest();
    }

    public void reset() throws RemoteException
    {
    	log.info("Reset");
        backingEngine.reset();
    }

    public void stopTest() throws RemoteException
    {
    	log.info("Stopping test");
        backingEngine.stopTest();//TODO: askThreadsToStop() instead?
    }

	public void exit() throws RemoteException
	{
		log.info("Exitting");
		backingEngine.exit();
	}
	
    /**
     * The main program for the RemoteJMeterEngineImpl class.
     *
     * @param  args  the command line arguments
     */
    public static void main(String[] args)
    {
    	log.info("Starting main");
        try
        {
            new RemoteJMeterEngineImpl();
            while (true)
            {
                Thread.sleep(Long.MAX_VALUE);
            }
        }
        catch (Exception ex)
        {
            log.error("", ex);
        }

    }
}
