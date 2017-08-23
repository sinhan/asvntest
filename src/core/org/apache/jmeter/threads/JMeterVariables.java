package org.apache.jmeter.threads;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @version $Revision: 323379 $
 */
public class JMeterVariables
{
    private Map variables = new HashMap();
    private int iteration = 0;

    public JMeterVariables()
    {
    }

    public String getThreadName()
    {
        return Thread.currentThread().getName();
    }

    public int getIteration()
    {
        return iteration;
    }

    public void incIteration()
    {
        iteration++;
    }

    public void initialize()
    {
        variables.clear();
    }

    public void put(String key, String value)
    {
        variables.put(key, value);
    }
    
    public void putObject(String key, Object value)
    {
        variables.put(key, value);
    }

    public void putAll(Map vars)
    {
        Iterator iter = vars.keySet().iterator();
        while (iter.hasNext())
        {
            String item = (String) iter.next();
            put(item, (String) vars.get(item));

        }
    }
    
    public void putAll(JMeterVariables vars)
    {
        putAll(vars.variables);
    }

    /**
     * Returns null values if variable doesn't exist.  Users of this must check
     * for null.
     */
    public String get(String key)
    {
        return (String) variables.get(key);
    }
    
    public Object getObject(String key)
    {
        return variables.get(key);
    }
}