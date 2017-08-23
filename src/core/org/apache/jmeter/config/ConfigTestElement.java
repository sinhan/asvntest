package org.apache.jmeter.config;
import java.io.Serializable;

import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestElement;

/****************************************
 * Title: JMeter Description: Copyright: Copyright (c) 2000 Company: Apache
 *
 *@author    Michael Stover
 *@created   $Date: 2003-02-04 21:12:10 -0800 (Tue, 04 Feb 2003) $
 *@version   1.0
 ***************************************/

public class ConfigTestElement extends AbstractTestElement implements Serializable
{

	public final static String USERNAME = "ConfigTestElement.username";
	public final static String PASSWORD = "ConfigTestElement.password";

	/****************************************
	 * !ToDo (Constructor description)
	 ***************************************/
	public ConfigTestElement() { }

	/****************************************
	 * !ToDo
	 *
	 *@param parm1  !ToDo
	 ***************************************/
	public void addTestElement(TestElement parm1)
	{
		if(parm1 instanceof ConfigTestElement)
		{
			mergeIn(parm1);
		}
	}
}
