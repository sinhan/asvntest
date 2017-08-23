package org.apache.jmeter.functions;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

/**
 * @author mstover
 * @version $Revision: 323907 $
 */
public abstract class AbstractFunction implements Function
{
    
    /**
     * @see Function#execute(SampleResult, Sampler)
     */
    abstract public String execute(
        SampleResult previousResult,
        Sampler currentSampler)
        throws InvalidVariableException;

    public String execute() throws InvalidVariableException
    {
        JMeterContext context = JMeterContextService.getContext();
        SampleResult previousResult = context.getPreviousResult();
        Sampler currentSampler = context.getCurrentSampler();
        return execute(previousResult, currentSampler);
    }


    /**
     * @see Function#setParameters(Collection)
     */
    abstract public void setParameters(Collection parameters)
        throws InvalidVariableException;

    /**
     * @see Function#getReferenceKey()
     */
    abstract public String getReferenceKey();

    /**
     * Provides a convenient way to parse the given argument string into a
     * collection of individual arguments.  Takes care of splitting the string
     * based on commas, generates blank strings for values between adjacent
     * commas, and decodes the string using URLDecoder.
     * 
     * @deprecated
     */
    protected Collection parseArguments(String params)
    {
        StringTokenizer tk = new StringTokenizer(params, ",", true);
        List arguments = new LinkedList();
        String previous = "";
        while (tk.hasMoreTokens())
        {
            String arg = tk.nextToken();

            if (arg.equals(",") && previous.equals(","))
            {
                arguments.add("");
            }
            else if (!arg.equals(","))
            {
                try
                {
                    arguments.add(URLDecoder.decode(arg, "UTF-8"));
                }
                catch (UnsupportedEncodingException e)
                {
                    // UTF-8 unsupported? You must be joking!
                    throw new Error(e);
                }
            }
            previous = arg;
        }
        return arguments;
    }

    /**
     * Provides a convenient way to parse the given argument string into a
     * collection of individual arguments.  Takes care of splitting the string
     * based on commas, generates blank strings for values between adjacent
     * commas, and decodes the string using URLDecoder.
     */
/*
    protected Collection parseArguments2(String params)
    {
        StringTokenizer tk = new StringTokenizer(params, ",", true);
        List arguments = new LinkedList();
        String previous = "";
        while (tk.hasMoreTokens())
        {
            String arg = tk.nextToken();

            if (arg.equals(",")
                && (previous.equals(",") || previous.length() == 0))
            {
                arguments.add(new CompoundVariable());
            }
            else if (!arg.equals(","))
            {
                try
                {
                    CompoundVariable compoundArg = new CompoundVariable();
                    compoundArg.setParameters(URLDecoder.decode(arg));
                    arguments.add(compoundArg);
                }
                catch (InvalidVariableException e)
                {
                }
            }
            previous = arg;
        }

        if (previous.equals(","))
        {
            arguments.add(new CompoundVariable());
        }

        return arguments;
    }
*/

    protected JMeterVariables getVariables()
    {
        return JMeterContextService.getContext().getVariables();
    }
}
