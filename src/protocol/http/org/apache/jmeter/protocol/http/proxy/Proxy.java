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

package org.apache.jmeter.protocol.http.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Thread to handle one client request. Gets the request from the client and
 * passes it on to the server, then sends the response back to the client.
 * Information about the request and response is stored so it can be used in a
 * JMeter test plan.
 * 
 * @author mike
 * @version $Revision: 325542 $ Last updated: $Date: 2005-07-12 13:51:09 -0700 (Tue, 12 Jul 2005) $ Created
 *          June 8, 2001
 */
public class Proxy extends Thread {
	/** Logging. */
	private static transient Logger log = LoggingManager.getLoggerForClass();

	/** Socket to client. */
	private Socket clientSocket = null;

	/** Target to receive the generated sampler. */
	private ProxyControl target;

	/** Whether or not to capture the HTTP headers. */
	private boolean captureHttpHeaders;

	/**
	 * Default constructor.
	 */
	public Proxy() {
	}

	/**
	 * Create and configure a new Proxy object.
	 * 
	 * @param clientSocket
	 *            the socket connection to the client
	 * @param target
	 *            the ProxyControl which will receive the generated sampler
	 */
	Proxy(Socket clientSocket, ProxyControl target) {
		configure(clientSocket, target);
	}

	/**
	 * Configure the Proxy.
	 * 
	 * @param clientSocket
	 *            the socket connection to the client
	 * @param target
	 *            the ProxyControl which will receive the generated sampler
	 */
	void configure(Socket clientSocket, ProxyControl target) {
		this.target = target;
		this.clientSocket = clientSocket;
		this.captureHttpHeaders = target.getCaptureHttpHeaders();
	}

	/**
	 * Main processing method for the Proxy object
	 */
	public void run() {
		HttpRequestHdr request = new HttpRequestHdr();
		SampleResult result = null;
		HeaderManager headers = null;

		HTTPSamplerBase sampler = new HTTPSampler();
		try {
			request.parse(new BufferedInputStream(clientSocket.getInputStream()));

			sampler = request.getSampler();

			/*
			 * Create a Header Manager to ensure that the browsers headers are
			 * captured and sent to the server
			 */
			headers = request.getHeaderManager();
			sampler.setHeaderManager(headers);
			result = sampler.sample();
			writeToClient(result, new BufferedOutputStream(clientSocket.getOutputStream()));
			/*
			 * We don't want to store any cookies in the generated test plan
			 */
			headers.removeHeaderNamed("cookie");// Always remove cookies
		} catch (UnknownHostException uhe) {
			log.warn("Server Not Found.", uhe);
			writeErrorToClient(HttpReplyHdr.formServerNotFound());
		} catch (Exception e) {
			log.error("", e);
			writeErrorToClient(HttpReplyHdr.formTimeout());
		} finally {
			target.deliverSampler(sampler, new TestElement[] { captureHttpHeaders ? headers : null }, result);
			try {
				clientSocket.close();
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	/**
	 * Write output to the output stream, then flush and close the stream.
	 * 
	 * @param inBytes
	 *            the bytes to write
	 * @param out
	 *            the output stream to write to
	 * @throws IOException
	 *             if an IOException occurs while writing
	 */
	private void writeToClient(SampleResult res, OutputStream out) throws IOException {
		try {
			String responseHeaders = massageResponseHeaders(res, res.getResponseHeaders());
			out.write((responseHeaders + "\n").getBytes());
			out.write(res.getResponseData());
			out.flush();
			log.debug("Done writing to client");
		} catch (IOException e) {
			log.error("", e);
			throw e;
		} finally {
			try {
				out.close();
			} catch (Exception ex) {
				log.warn("Error while closing socket", ex);
			}
		}
	}

	/**
	 * In the event the content was gzipped and unpacked, the content-encoding
	 * header must be removed and the content-length header should be corrected.
	 * 
	 * @param res
	 * @param headers
	 * @return
	 */
	private String massageResponseHeaders(SampleResult res, String headers) {
		int encodingHeaderLoc = headers.indexOf(": gzip");
		String newHeaders = headers;
		if (encodingHeaderLoc > -1) {
			int end = headers.indexOf("\n", encodingHeaderLoc);
			int begin = headers.lastIndexOf("\n", encodingHeaderLoc);
			newHeaders = newHeaders.substring(0, begin) + newHeaders.substring(end);
			int lengthIndex = newHeaders.indexOf("ength: ");
			end = newHeaders.indexOf("\n", lengthIndex);
			newHeaders = newHeaders.substring(0, lengthIndex + 7) + res.getResponseData().length
					+ newHeaders.substring(end);
		}
		return newHeaders;
	}

	/**
	 * Write an error message to the client. The message should be the full HTTP
	 * response.
	 * 
	 * @param message
	 *            the message to write
	 */
	private void writeErrorToClient(String message) {
		try {
			OutputStream sockOut = clientSocket.getOutputStream();
			DataOutputStream out = new DataOutputStream(sockOut);
			out.writeBytes(message);
			out.flush();
		} catch (Exception e) {
			log.warn("Exception while writing error", e);
		}
	}
}