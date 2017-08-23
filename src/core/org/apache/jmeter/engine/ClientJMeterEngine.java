/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.util.Properties;

import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.SearchByClass;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Class to run remote tests from the client JMeter and collect remote samples
 */
public class ClientJMeterEngine implements JMeterEngine, Runnable {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final Object LOCK = new Object();

    private RemoteJMeterEngine remote;

    private HashTree test;

    private final String host;

    private static RemoteJMeterEngine getEngine(String h) throws MalformedURLException, RemoteException,
            NotBoundException {
       final String name = "//" + h + "/" + RemoteJMeterEngineImpl.JMETER_ENGINE_RMI_NAME; // $NON-NLS-1$ $NON-NLS-2$
       Remote remobj = Naming.lookup(name);
       if (remobj instanceof RemoteJMeterEngine){
           final RemoteJMeterEngine rje = (RemoteJMeterEngine) remobj;
           if (remobj instanceof RemoteObject){
               RemoteObject robj = (RemoteObject) remobj;
               System.out.println("Using remote object: "+robj.getRef().remoteToString());
           }
           return rje;
       }
       throw new RemoteException("Could not find "+name);
    }

    public ClientJMeterEngine(String host) throws MalformedURLException, NotBoundException, RemoteException {
        this.remote = getEngine(host);
        this.host = host;
    }

    /** {@inheritDoc} */
    public void configure(HashTree testTree) {
        TreeCloner cloner = new TreeCloner(false);
        testTree.traverse(cloner);
        test = cloner.getClonedTree();
    }

    /** {@inheritDoc} */
    public void runTest() {
        log.info("about to run remote test on "+host);
        new Thread(this).start();
        log.info("done initiating run command");
    }

    /** {@inheritDoc} */
    public void stopTest() {
        log.info("about to stop remote test on "+host);
        try {
            remote.stopTest();
        } catch (Exception ex) {
            log.error("", ex); // $NON-NLS-1$
        }
    }

    /** {@inheritDoc} */
    public void reset() {
        try {
            try {
                remote.reset();
            } catch (java.rmi.ConnectException e) {
                remote = getEngine(host);
                remote.reset();
            }
        } catch (Exception ex) {
            log.error("", ex); // $NON-NLS-1$
        }
    }

    /** {@inheritDoc} */
    public void run() {
        log.info("running clientengine run method");
        SearchByClass<TestListener> testListeners = new SearchByClass<TestListener>(TestListener.class);
        ConvertListeners sampleListeners = new ConvertListeners();
        HashTree testTree = test;
        PreCompiler compiler = new PreCompiler(true); // limit the changes to client only test elements
        synchronized(testTree) {
            testTree.traverse(compiler);
            testTree.traverse(new TurnElementsOn());
            testTree.traverse(testListeners);
            testTree.traverse(sampleListeners);
        }

        try {
            JMeterContextService.startTest();
            /*
             * Add fix for Deadlocks, see:
             * 
             * See https://issues.apache.org/bugzilla/show_bug.cgi?id=48350
            */
            synchronized(LOCK)
            {
                remote.configure(testTree, host);
            }
            log.info("sent test to " + host);
            if (savep != null){
                log.info("Sending properties "+savep);
                try {
                    remote.setProperties(savep);
                } catch (RemoteException e) {
                    log.warn("Could not set properties: " + e.toString());
                }
            }
            remote.runTest();
            log.info("sent run command to "+ host);
        } catch (Exception ex) {
            log.error("", ex); // $NON-NLS-1$
        }
    }

    /** {@inheritDoc} */
    public void exit() {
        log.info("about to exit remote server on "+host);
        try {
            remote.exit();
        } catch (RemoteException e) {
            log.warn("Could not perform remote exit: " + e.toString());
        }
    }

    private Properties savep;
    /** {@inheritDoc} */
    public void setProperties(Properties p) {
        savep = p;
        // Sent later
    }
}
