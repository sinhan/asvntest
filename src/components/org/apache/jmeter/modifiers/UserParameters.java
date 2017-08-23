package org.apache.jmeter.modifiers;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @version $Revision: 323653 $
 */
public class UserParameters
    extends AbstractTestElement
    implements Serializable, PreProcessor, LoopIterationListener
{
	private static final Logger log = LoggingManager.getLoggerForClass();

    public static final String NAMES = "UserParameters.names";
    public static final String THREAD_VALUES = "UserParameters.thread_values";
    public static final String PER_ITERATION = "UserParameters.per_iteration";
    private Integer lock = new Integer(0);

    public CollectionProperty getNames()
    {
        return (CollectionProperty) getProperty(NAMES);
    }

    public CollectionProperty getThreadLists()
    {
        return (CollectionProperty) getProperty(THREAD_VALUES);
    }

    /**
     * The list of names of the variables to hold values.  This list must come
     * in the same order as the sub lists that are given to
     * {@link #setThreadLists(Collection)}.
     */
    public void setNames(Collection list)
    {
        setProperty(new CollectionProperty(NAMES, list));
    }

    /**
     * The list of names of the variables to hold values.  This list must come
     * in the same order as the sub lists that are given to
     * {@link #setThreadLists(CollectionProperty)}.
     */
    public void setNames(CollectionProperty list)
    {
        setProperty(list);
    }

    /**
     * The thread list is a list of lists.  Each list within the parent list is
     * a collection of values for a simulated user.  As many different sets of 
     * values can be supplied in this fashion to cause JMeter to set different 
     * values to variables for different test threads.
     */
    public void setThreadLists(Collection threadLists)
    {
        setProperty(new CollectionProperty(THREAD_VALUES, threadLists));
    }

    /**
     * The thread list is a list of lists.  Each list within the parent list is
     * a collection of values for a simulated user.  As many different sets of 
     * values can be supplied in this fashion to cause JMeter to set different 
     * values to variables for different test threads.
     */
    public void setThreadLists(CollectionProperty threadLists)
    {
        setProperty(threadLists);
    }

    private CollectionProperty getValues()
    {
        CollectionProperty threadValues =
            (CollectionProperty) getProperty(THREAD_VALUES);
        if (threadValues.size() > 0)
        {
            return (CollectionProperty) threadValues.get(
                JMeterContextService.getContext().getThreadNum()
                    % threadValues.size());
        }
        else
        {
            return new CollectionProperty("noname", new LinkedList());
        }
    }

    public boolean isPerIteration()
    {
        return getPropertyAsBoolean(PER_ITERATION);
    }

    public void setPerIteration(boolean perIter)
    {
        setProperty(new BooleanProperty(PER_ITERATION, perIter));
    }

    public void process()
    {
    	if (log.isDebugEnabled())
    	{
			log.debug(Thread.currentThread().getName() + " process " + isPerIteration());//$NON-NLS-1$
    	}
        if (!isPerIteration())
        {
            setValues();
        }
    }

    private void setValues()
    {
        synchronized (lock)
        {
			if (log.isDebugEnabled())
			{
		        log.debug(Thread.currentThread().getName() + " Running up named: " + getName());//$NON-NLS-1$
			}
            PropertyIterator namesIter = getNames().iterator();
            PropertyIterator valueIter = getValues().iterator();
            JMeterVariables jmvars =
                JMeterContextService.getContext().getVariables();
            while (namesIter.hasNext() && valueIter.hasNext())
            {
                String name = namesIter.next().getStringValue();
                String value = valueIter.next().getStringValue();
				if (log.isDebugEnabled())
				{
                    log.debug(Thread.currentThread().getName()+" saving variable: "+name+"="+value);//$NON-NLS-1$
				}
                jmvars.put(name, value);
            }
        }
    }

    /**
     * @see LoopIterationListener#iterationStart(LoopIterationEvent)
     */
    public void iterationStart(LoopIterationEvent event)
    {
		if (log.isDebugEnabled())
        {
			log.debug(Thread.currentThread().getName() + " iteration start " + isPerIteration());//$NON-NLS-1$
        }
        if (isPerIteration())
        {
            setValues();
        }
    }

    /* This method doesn't appear to be used anymore.
     * jeremy_a@bigfoot.com  03 May 2003
     * 
     * @see ThreadListener#setJMeterVariables(JMeterVariables)
    public void setJMeterVariables(JMeterVariables jmVars)
    {}
     */

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    public Object clone()
    {
        UserParameters up = (UserParameters) super.clone();
        up.lock = lock;
        return up;
    }

    /* (non-Javadoc)
     * @see AbstractTestElement#mergeIn(TestElement)
     */
    protected void mergeIn(TestElement element)
    {
        // super.mergeIn(element);
    }
}
