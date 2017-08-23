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

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.apache.jorphan.collections.HashTree;

/**
 * @version $Revision: 325542 $ Last updated $Date: 2005-07-12 13:51:09 -0700 (Tue, 12 Jul 2005) $
 */
public interface RemoteJMeterEngine extends Remote {
	void configure(HashTree testTree) throws RemoteException;

	void runTest() throws RemoteException, JMeterEngineException;

	void stopTest() throws RemoteException;

	void reset() throws RemoteException;

	void setHost(String host) throws RemoteException;

	void exit() throws RemoteException;
}
