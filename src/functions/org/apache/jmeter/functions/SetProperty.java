// $Header$
/*
 * Copyright 2005 The Apache Software Foundation.
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

package org.apache.jmeter.functions;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.util.JMeterUtils;

/**
 * Function to set a JMeter property
 * 
 * Parameters: - property name - value
 * 
 * Usage:
 * 
 * Set the property value in the appropriate GUI by using the string:
 * ${__setProperty(propname,propvalue)}
 * 
 * Returns: nothing
 * 
 * @version $Revision$ Updated: $Date$
 */
public class SetProperty extends AbstractFunction implements Serializable {

	private static final List desc = new LinkedList();

	private static final String KEY = "__setProperty";

	// Number of parameters expected - used to reject invalid calls
	private static final int MIN_PARAMETER_COUNT = 2;

	private static final int MAX_PARAMETER_COUNT = 2;
	static {
		desc.add(JMeterUtils.getResString("property_name_param"));
		desc.add(JMeterUtils.getResString("property_value_param"));
	}

	private Object[] values;

	public SetProperty() {
	}

	public Object clone() {
		return new SetProperty();
	}

	public synchronized String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		String propertyName = ((CompoundVariable) values[0]).execute();

		String propertyValue = ((CompoundVariable) values[1]).execute();

		JMeterUtils.setProperty(propertyName, propertyValue);

		return propertyValue;

	}

	public void setParameters(Collection parameters) throws InvalidVariableException {

		values = parameters.toArray();

		if ((values.length < MIN_PARAMETER_COUNT) || (values.length > MAX_PARAMETER_COUNT)) {
			throw new InvalidVariableException("Parameter Count not between " + MIN_PARAMETER_COUNT + " & "
					+ MAX_PARAMETER_COUNT);
		}

	}

	public String getReferenceKey() {
		return KEY;
	}

	public List getArgumentDesc() {
		return desc;
	}

}