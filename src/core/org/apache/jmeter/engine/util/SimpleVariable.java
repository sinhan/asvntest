package org.apache.jmeter.engine.util;

import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

/**
 * @version $Revision: 323474 $
 */
public class SimpleVariable
{

    private String name;

    public SimpleVariable(String name)
    {
        this.name = name;
    }

    public SimpleVariable()
    {
        this.name = "";
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @see org.apache.jmeter.functions.Function#execute(SampleResult, Sampler)
     */
    public String toString()
    {
        String ret = null;
        JMeterVariables vars = getVariables();

        if (vars != null)
        {
            ret = vars.get(name);
        }

        if (ret == null)
        {
            return "${" + name + "}";
        }

        return ret;
    }

    private JMeterVariables getVariables()
    {
        JMeterContext context = JMeterContextService.getContext();
        return context.getVariables();
    }

}