// $Header$
/*
 * Copyright 2003-2004 The Apache Software Foundation.
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

/*
 * Created on Jun 13, 2003
 */
package org.apache.jmeter.util;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @author ano ano
 * @version $Revision: 324292 $
 */
public final class NameUpdater
{
    private static Properties nameMap;
    private static Logger log = LoggingManager.getLoggerForClass();
    
    static {
        nameMap = new Properties();
        try
        {
            nameMap.load(
                new FileInputStream(
                    JMeterUtils.getJMeterHome()
                        + JMeterUtils.getPropDefault(
                            "upgrade_properties",
                            "/bin/upgrade.properties")));
        }
        catch (Exception e)
        {
            log.error("Bad upgrade file",e);
        }
    }
    
    public static String getCurrentName(String className)
    {
    	if (nameMap.containsKey(className))
    	{
			String newName= nameMap.getProperty(className);
    		log.info("Upgrading class "+className+" to "+newName);
    		return newName; 
    	}
        return className;
    }

	public static String getCurrentName(String propertyName, String className)
	{
		String key= className+"/"+propertyName;
		if (nameMap.containsKey(key))
		{
			String newName= nameMap.getProperty(key);
			log.info("Upgrading property "+propertyName+" to "+newName);
			return newName;
		}
		return propertyName;
	}

	public static String getCurrentName(String value, String propertyName, String className)
	{
		String key= className+"."+propertyName+"/"+value;
		if (nameMap.containsKey(key))
		{
			String newValue= nameMap.getProperty(key);
			log.info("Upgrading value "+value+" to "+newValue);
			return newValue;
		}
		return value;
	}

    /**
     * Private constructor to prevent instantiation.
     */
    private NameUpdater()
    {
    }
}
