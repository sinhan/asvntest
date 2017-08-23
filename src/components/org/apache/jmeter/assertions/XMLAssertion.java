/*
 * Copyright 2003-2006 The Apache Software Foundation.
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

package org.apache.jmeter.assertions;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Checks if the result is a well-formed XML content using jdom
 * 
 * @author <a href="mailto:gottfried@szing.at">Gottfried Szing</a>
 * @version $Revision: 390535 $, $Date: 2006-03-31 15:04:52 -0800 (Fri, 31 Mar 2006) $
 */
public class XMLAssertion extends AbstractTestElement implements Serializable, Assertion {
	private static final Logger log = LoggingManager.getLoggerForClass();

	// one builder for all requests in a thread
    private static ThreadLocal myBuilder = new ThreadLocal() {
        protected Object initialValue() {
            return new SAXBuilder();
        }
    };

	/**
	 * Returns the result of the Assertion. Here it checks wether the Sample
	 * took to long to be considered successful. If so an AssertionResult
	 * containing a FailureMessage will be returned. Otherwise the returned
	 * AssertionResult will reflect the success of the Sample.
	 */
	public AssertionResult getResult(SampleResult response) {
		// no error as default
		AssertionResult result = new AssertionResult();
		byte[] responseData = response.getResponseData();
		if (responseData.length == 0) {
			return result.setResultForNull();
		}
		result.setFailure(false);

		// the result data
		String resultData = new String(getResultBody(responseData));

        SAXBuilder builder = (SAXBuilder) myBuilder.get();

		try {
			builder.build(new StringReader(resultData));
		} catch (JDOMException e) {
			log.debug("Cannot parse result content", e); // may well happen
			result.setFailure(true);
			result.setFailureMessage(e.getMessage());
		} catch (IOException e) {
            log.error("Cannot read result content", e); // should never happen
            result.setError(true);
            result.setFailureMessage(e.getMessage());
        }

		return result;
	}

	/**
	 * Return the body of the http return.
	 */
	private byte[] getResultBody(byte[] resultData) {
		for (int i = 0; i < (resultData.length - 1); i++) {
			if (resultData[i] == '\n' && resultData[i + 1] == '\n') {
				return getByteArraySlice(resultData, (i + 2), resultData.length - 1);
			}
		}
		return resultData;
	}

	/**
	 * Return a slice of a byte array
	 */
	private byte[] getByteArraySlice(byte[] array, int begin, int end) {
		byte[] slice = new byte[(end - begin + 1)];
		int count = 0;
		for (int i = begin; i <= end; i++) {
			slice[count] = array[i];
			count++;
		}

		return slice;
	}
}
