/*
 * Copyright 2002-2004,2006 The Apache Software Foundation.
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

import java.net.HttpURLConnection;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.jmeter.util.keystore.JmeterKeyStore;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import com.sun.net.ssl.HostnameVerifier;
import com.sun.net.ssl.HttpsURLConnection;
import com.sun.net.ssl.KeyManager;
import com.sun.net.ssl.KeyManagerFactory;
import com.sun.net.ssl.SSLContext;
import com.sun.net.ssl.TrustManager;
import com.sun.net.ssl.X509KeyManager;
import com.sun.net.ssl.X509TrustManager;

/**
 * The SSLManager handles the KeyStore information for JMeter. Basically, it
 * handles all the logic for loading and initializing all the JSSE parameters
 * and selecting the alias to authenticate against if it is available.
 * SSLManager will try to automatically select the client certificate for you,
 * but if it can't make a decision, it will pop open a dialog asking you for
 * more information.
 * 
 * @author <a href="bloritsch@apache.org">Berin Loritsch</a> Created March 21,
 *         2002
 */
public class JsseSSLManager extends SSLManager {
	private static final Logger log = LoggingManager.getLoggerForClass();

	/**
	 * Cache the SecureRandom instance because it takes a long time to create
	 */
	private SecureRandom rand;

	/**
	 * Cache the Context so we can retrieve it from other places
	 */
	private SSLContext context = null;

	private Provider pro = null;

	/**
	 * Private Constructor to remove the possibility of directly instantiating
	 * this object. Create the SSLContext, and wrap all the X509KeyManagers with
	 * our X509KeyManager so that we can choose our alias.
	 * 
	 * @param provider
	 *            Description of Parameter
	 */
	public JsseSSLManager(Provider provider) {
		log.debug("ssl Provider =  " + provider);
		setProvider(provider);
		if (null == this.rand) {
			this.rand = new SecureRandom();
		}

		if ("all".equalsIgnoreCase( // $NON-NLS-1$
				JMeterUtils.getPropDefault("javax.net.debug", "none"))) { // $NON-NLS-1$ // $NON-NLS-2$
			System.setProperty("javax.net.debug", "all"); // $NON-NLS-1$ // $NON-NLS-2$
		}
		this.getContext();
		log.info("JsseSSLManager installed");
	}

	/**
	 * Sets the Context attribute of the JsseSSLManager object
	 * 
	 * @param conn
	 *            The new Context value
	 */
	public void setContext(HttpURLConnection conn) {
		if (conn instanceof com.sun.net.ssl.HttpsURLConnection) {
			com.sun.net.ssl.HttpsURLConnection secureConn = (com.sun.net.ssl.HttpsURLConnection) conn;
			secureConn.setSSLSocketFactory(this.getContext().getSocketFactory());
		} else if (conn instanceof sun.net.www.protocol.https.HttpsURLConnectionImpl) {
			sun.net.www.protocol.https.HttpsURLConnectionImpl secureConn = (sun.net.www.protocol.https.HttpsURLConnectionImpl) conn;
			secureConn.setSSLSocketFactory(this.getContext().getSocketFactory());
		}
	}

	/**
	 * Sets the Provider attribute of the JsseSSLManager object
	 * 
	 * @param p
	 *            The new Provider value
	 */
	protected final void setProvider(Provider p) {
		super.setProvider(p);
		if (null == this.pro) {
			this.pro = p;
		}
	}

	/**
	 * Returns the SSLContext we are using. It is useful for obtaining the
	 * SSLSocketFactory so that your created sockets are authenticated.
	 * 
	 * @return The Context value
	 */
	private SSLContext getContext() {
		if (null == this.context) {
			try {
				if (pro != null) {
					this.context = SSLContext.getInstance("TLS", pro); // $NON-NLS-1$
				} else {
					this.context = SSLContext.getInstance("TLS"); // $NON-NLS-1$
				}
				log.debug("SSL context = " + context);
			} catch (Exception ee) {
				log.error("Exception occurred", ee);
			}
			try {
				KeyManagerFactory managerFactory = KeyManagerFactory.getInstance("SunX509"); // $NON-NLS-1$
				JmeterKeyStore keys = this.getKeyStore();
				managerFactory.init(null, this.defaultpw.toCharArray());
				KeyManager[] managers = managerFactory.getKeyManagers();
				log.info(keys.getClass().toString());
				for (int i = 0; i < managers.length; i++) {
					if (managers[i] instanceof X509KeyManager) {
						X509KeyManager manager = (X509KeyManager) managers[i];
						managers[i] = new WrappedX509KeyManager(manager, keys);
					}
				}
				TrustManager[] trusts = new TrustManager[] { new AlwaysTrustManager(this.getTrustStore()) };
				context.init(managers, trusts, this.rand);
				HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
				HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
					public boolean verify(String urlHostname, String certHostname) {
						return true;
					}
				});
				log.debug("SSL stuff all set");
			} catch (Exception e) {
				log.error("Exception occurred", e);
			}

            if (log.isDebugEnabled()){
    			String[] dCiphers = this.context.getSocketFactory().getDefaultCipherSuites();
    			String[] sCiphers = this.context.getSocketFactory().getSupportedCipherSuites();
    			int len = (dCiphers.length > sCiphers.length) ? dCiphers.length : sCiphers.length;
    			for (int i = 0; i < len; i++) {
    				if (i < dCiphers.length) {
    					log.debug("Default Cipher: " + dCiphers[i]);
    				}
    				if (i < sCiphers.length) {
    					log.debug("Supported Cipher: " + sCiphers[i]);
    				}
    			}
            }
		}
		return this.context;
	}

	/**
	 * @author MStover Created March 21, 2002
	 */
	protected static class AlwaysTrustManager implements X509TrustManager {
		/**
		 * Description of the Field
		 */
		protected X509Certificate[] certs;

		/**
		 * Constructor for the AlwaysTrustManager object
		 * 
		 * @param store
		 *            Description of Parameter
		 */
		public AlwaysTrustManager(KeyStore store) {
			try {
				java.util.Enumeration enumer = store.aliases();
				java.util.ArrayList list = new java.util.ArrayList(store.size());
				while (enumer.hasMoreElements()) {
					String alias = (String) enumer.nextElement();
					log.info("AlwaysTrustManager alias: " + alias);
					if (store.isCertificateEntry(alias)) {
						list.add(store.getCertificate(alias));
						log.info(" INSTALLED");
					} else {
						log.info(" SKIPPED");
					}
				}
				this.certs = (X509Certificate[]) list.toArray(new X509Certificate[] {});
			} catch (Exception e) {
				this.certs = null;
			}
		}

		/**
		 * Gets the AcceptedIssuers attribute of the AlwaysTrustManager object
		 * 
		 * @return The AcceptedIssuers value
		 */
		public X509Certificate[] getAcceptedIssuers() {
			log.info("Get accepted Issuers");
			return certs;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see X509TrustManager#checkClientTrusted(X509Certificate[], String)
		 */
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see X509TrustManager#checkServerTrusted(X509Certificate[], String)
		 */
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		public boolean isClientTrusted(X509Certificate[] arg0) {
			return true;
		}

		public boolean isServerTrusted(X509Certificate[] arg0) {
			return true;
		}

	}

	/**
	 * This is the X509KeyManager we have defined for the sole purpose of
	 * selecting the proper key and certificate based on the keystore available.
	 * 
	 * @author MStover Created March 21, 2002
	 */
	private static class WrappedX509KeyManager implements X509KeyManager {
		/**
		 * The parent X509KeyManager
		 */
		private final X509KeyManager manager;

		/**
		 * The KeyStore this KeyManager uses
		 */
		private final JmeterKeyStore store;

		/**
		 * Instantiate a new WrappedX509KeyManager.
		 * 
		 * @param parent
		 *            The parent X509KeyManager
		 * @param ks
		 *            The KeyStore we derive our client certs and keys from
		 */
		public WrappedX509KeyManager(X509KeyManager parent, JmeterKeyStore ks) {
			this.manager = parent;
			this.store = ks;
		}

		/**
		 * Compiles the list of all client aliases with a private key.
		 * Currently, keyType and issuers are both ignored.
		 * 
		 * @param keyType
		 *            the type of private key the server expects (RSA, DSA,
		 *            etc.)
		 * @param issuers
		 *            the CA certificates we are narrowing our selection on.
		 * @return the ClientAliases value
		 */
		public String[] getClientAliases(String keyType, Principal[] issuers) {
			log.info("WrappedX509Manager: getClientAliases: ");
			log.info(this.store.getAlias());
			return new String[] { this.store.getAlias() };
		}

		/**
		 * Get the list of server aliases for the SSLServerSockets. This is not
		 * used in JMeter.
		 * 
		 * @param keyType
		 *            the type of private key the server expects (RSA, DSA,
		 *            etc.)
		 * @param issuers
		 *            the CA certificates we are narrowing our selection on.
		 * @return the ServerAliases value
		 */
		public String[] getServerAliases(String keyType, Principal[] issuers) {
			log.info("WrappedX509Manager: getServerAliases: ");
			return this.manager.getServerAliases(keyType, issuers);
		}

		/**
		 * Get the Certificate chain for a particular alias
		 * 
		 * @param alias
		 *            The client alias
		 * @return The CertificateChain value
		 */
		public X509Certificate[] getCertificateChain(String alias) {
			log.info("WrappedX509Manager: getCertificateChain(" + alias + ")");
			return this.store.getCertificateChain();
		}

		/**
		 * Get the Private Key for a particular alias
		 * 
		 * @param alias
		 *            The client alias
		 * @return The PrivateKey value
		 */
		public PrivateKey getPrivateKey(String alias) {
			log.info("WrappedX509Manager: getPrivateKey: " + this.store.getPrivateKey());
			return this.store.getPrivateKey();
		}

		/**
		 * Select the Alias we will authenticate as if Client authentication is
		 * required by the server we are connecting to. We get the list of
		 * aliases, and if there is only one alias we automatically select it.
		 * If there are more than one alias that has a private key, we prompt
		 * the user to choose which alias using a combo box. Otherwise, we
		 * simply provide a text box, which may or may not work. The alias does
		 * have to match one in the keystore.
		 * 
		 * @see javax.net.ssl.X509KeyManager#chooseClientAlias(java.lang.String,
		 *      java.security.Principal, java.net.Socket)
		 */
		public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
			log.info("Alias: " + this.store.getAlias());
			return this.store.getAlias();
		}

		/**
		 * Choose the server alias for the SSLServerSockets. This are not used
		 * in JMeter.
		 * 
		 * @see javax.net.ssl.X509KeyManager#chooseServerAlias(java.lang.String,
		 *      java.security.Principal, java.net.Socket)
		 */
		public String chooseServerAlias(String arg0, Principal[] arg1, Socket arg2) {
			return this.manager.chooseServerAlias(arg0, arg1);
		}

		public String chooseClientAlias(String arg0, Principal[] arg1) {
			return store.getAlias();
		}

		public String chooseServerAlias(String arg0, Principal[] arg1) {
			return manager.chooseServerAlias(arg0, arg1);
		}
	}
}
