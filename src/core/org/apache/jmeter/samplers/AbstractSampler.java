package org.apache.jmeter.samplers;
import java.util.ArrayList;
import java.util.List;

import org.apache.jmeter.assertions.Assertion;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.PerSampleClonable;
import org.apache.jmeter.testelement.TestElement;

/****************************************
 * Title: JMeter Description: Copyright: Copyright (c) 2000 Company: Apache
 *
 *@author    Michael Stover
 *@created   $Date: 2003-02-04 21:12:10 -0800 (Tue, 04 Feb 2003) $
 *@version   1.0
 ***************************************/

public abstract class AbstractSampler extends AbstractTestElement implements Sampler,
		PerSampleClonable
{
	private final static String ASSERTIONS = "AbstractSampler.assertions";

	public AbstractSampler()
	{
		setProperty(ASSERTIONS,new ArrayList());
	}

	public void addTestElement(TestElement element)
	{
		if(element instanceof Assertion)
		{
			addAssertion((Assertion)element);
		}
		else
		{
			if(element.getClass().getName().equals(ConfigTestElement.class.getName()))
			{
				mergeIn(element);
			}
			else
			{
				addCustomTestElement(element);
			}
		}
	}

	abstract protected void addCustomTestElement(TestElement element);

	protected void addAssertion(Assertion assertion)
	{
		getAssertions().add(assertion);
	}

	protected List getAssertions()
	{
		return (List)getProperty(ASSERTIONS);
	}
}
