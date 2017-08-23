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

package org.apache.jmeter.util.keystore;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

/**
 * Use this Keystore to wrap the normal KeyStore implementation.
 * 
 * @author <a href="bloritsch@apache.org">Berin Loritsch</a>
 * @version CVS $Revision: 325542 $ $Date: 2005-07-12 13:51:09 -0700 (Tue, 12 Jul 2005) $
 */
public class DefaultKeyStore extends JmeterKeyStore {
	private X509Certificate[] certChain;

	private PrivateKey key;

	private String alias;

	private final KeyStore store;

	public DefaultKeyStore(String type) throws Exception {
		this.store = KeyStore.getInstance(type);
	}

	public void load(InputStream is, String pword) throws Exception {
		store.load(is, pword.toCharArray());
		PrivateKey key = null;
		X509Certificate[] certChain = null;

		Enumeration aliases = store.aliases();
		while (aliases.hasMoreElements()) {
			this.alias = (String) aliases.nextElement();
			if (store.isKeyEntry(alias)) {
				key = (PrivateKey) store.getKey(alias, pword.toCharArray());
				Certificate[] chain = store.getCertificateChain(alias);
				certChain = new X509Certificate[chain.length];

				for (int i = 0; i < chain.length; i++) {
					certChain[i] = (X509Certificate) chain[i];
				}

				break;
			}
		}

		if (null == key) {
			throw new Exception("No key found");
		}
		if (null == certChain) {
			throw new Exception("No certificate chain found");
		}

		this.key = key;
		this.certChain = certChain;
	}

	public final X509Certificate[] getCertificateChain() {
		return this.certChain;
	}

	public final PrivateKey getPrivateKey() {
		return this.key;
	}

	public final String getAlias() {
		return this.alias;
	}
}
