package org.apache.jmeter.config;
import org.apache.jmeter.testelement.*;
import org.apache.jmeter.testelement.TestElement;
import java.io.Serializable;

/****************************************
 * Title: JMeter Description: Copyright: Copyright (c) 2000 Company: Apache
 *
 *@author    Michael Stover
 *@created   $Date: 2002-08-11 12:24:54 -0700 (Sun, 11 Aug 2002) $
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
