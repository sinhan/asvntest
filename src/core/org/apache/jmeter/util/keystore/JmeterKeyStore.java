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
package org.apache.jmeter.util.keystore;

import java.lang.reflect.Constructor;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import org.apache.jmeter.util.JMeterUtils;

/**
 * Use this Keystore for JMeter specific KeyStores.
 *
 * @author <a href="bloritsch@apache.org">Berin Loritsch</a>
 * @version CVS $Revision: 322831 $ $Date: 2002-08-11 12:24:54 -0700 (Sun, 11 Aug 2002) $
 */
public abstract class JmeterKeyStore {

    /**
     * Process the input stream
     */
    public abstract void load(InputStream is, String password)
    throws Exception;

    /**
     * Get the ordered certificate chain.
     */
    public abstract X509Certificate[] getCertificateChain();

    public abstract String getAlias();

    /**
     * Return the private Key
     */
    public abstract PrivateKey getPrivateKey();

    public static final JmeterKeyStore getInstance(String type)
    throws Exception {
        if ("PKCS12".equalsIgnoreCase(type)) {
            try {
                Class PKCS12 = Class.forName
                    ("org.apache.jmeter.util.keystore.PKCS12KeyStore");
                Constructor con = PKCS12.getConstructor(
                    new Class[] {String.class});
                return (JmeterKeyStore) con.newInstance(new Object[] {type});
            } catch (Exception e) {}
        }

        Class keyStore = Class.forName
            ("org.apache.jmeter.util.keystore.DefaultKeyStore");
        Constructor con = keyStore.getConstructor(
            new Class[] {String.class});
        return (JmeterKeyStore) con.newInstance(new Object[] {type});
    }
}