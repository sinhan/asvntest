/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2003 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
 
package org.apache.jmeter.threads;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.control.Controller;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.samplers.RemoteSampleListener;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.IntegerProperty;
import org.apache.jmeter.testelement.property.LongProperty;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

/**
 * ThreadGroup
 *
 * @author Michael Stover
 * @version $Id: ThreadGroup.java 323379 2003-06-28 19:35:40Z jeremy_a $
 */
public class ThreadGroup
    extends AbstractTestElement
    implements SampleListener, Serializable, Controller
{
    private static Logger log =
        Hierarchy.getDefaultHierarchy().getLoggerFor("jmeter.elements");

    public final static String NUM_THREADS = "ThreadGroup.num_threads";
    public final static String RAMP_TIME = "ThreadGroup.ramp_time";
    public final static String MAIN_CONTROLLER = "ThreadGroup.main_controller";

    public final static String SCHEDULER = "ThreadGroup.scheduler";
    public final static String START_TIME= "ThreadGroup.start_time";
    public final static String END_TIME= "ThreadGroup.end_time";


    private final int DEFAULT_NUM_THREADS = 1;
    private final int DEFAULT_RAMP_UP = 0;
    private SampleQueue queue = null;
    private int threadsStarted = 0;
    private LinkedList listeners = new LinkedList();
    private LinkedList remoteListeners = new LinkedList();

    /**
     * No-arg constructor.
     */
    public ThreadGroup()
    {
    }

    /**
     * Set the nuber of threads.
     *
     * @param numThreads the number of threads.
     */
    public void setNumThreads(int numThreads)
    {
        setProperty(new IntegerProperty(NUM_THREADS, numThreads));
    }

    public boolean isDone()
    {
        return getSamplerController().isDone();
    }

    public Sampler next()
    {
        return getSamplerController().next();
    }



    /**
     * Set the Scheduler value.
     *
     * @param Scheduler the Scheduler value.
     */
    public void setScheduler(boolean Scheduler)
    {
        setProperty(new BooleanProperty(SCHEDULER,Scheduler));
    }

    /**
     * Get the Scheduler value.
     *
     * @return the Scheduler value.
     */
    public boolean getScheduler()
    {
        return getPropertyAsBoolean(SCHEDULER);
    }

    /**
     * Set the StartTime value.
     *
     * @param StartTime the StartTime value.
     */
    public void setStartTime(long stime)
    {
        setProperty(new LongProperty(START_TIME,stime));
    }

    /**
     * Get the start time value.
     *
     * @return the start time value.
     */
    public long getStartTime()
    {
        return getPropertyAsLong(START_TIME);
    }

    /**
     * Set the EndTime value.
     *
     * @param EndTime the EndTime value.
     */
    public void setEndTime(long etime)
    {
        setProperty(new LongProperty(END_TIME, etime));
    }

    /**
     * Get the end time value.
     *
     * @return the end time  value.
     */
    public long getEndTime()
    {
        return getPropertyAsLong(END_TIME);
    }

    /**
     * Set the ramp-up value.
     *
     * @param rampUp the ramp-up value.
     */
    public void setRampUp(int rampUp)
    {
        setProperty(new IntegerProperty(RAMP_TIME,rampUp));
    }

    /**
     * Get the ramp-up value.
     *
     * @return the ramp-up value.
     */
    public int getRampUp()
    {
        return getPropertyAsInt(ThreadGroup.RAMP_TIME);
    }

    /**
     * Get the sampler controller.
     *
     * @return the sampler controller.
     */
    public Controller getSamplerController()
    {
        return (Controller) getProperty(MAIN_CONTROLLER).getObjectValue();
    }

    /**
     * Set the sampler controller.
     *
     * @param c the sampler controller.
     */
    public void setSamplerController(LoopController c)
    {
        c.setContinueForever(false);
        setProperty(new TestElementProperty(MAIN_CONTROLLER, c));
    }

    /**
     * Get the number of threads.
     *
     * @return the number of threads.
     */
    public int getNumThreads()
    {
        return this.getPropertyAsInt(ThreadGroup.NUM_THREADS);
    }

    /**
     * Get the default number of threads.
     *
     * @return the default number of threads.
     */
    public int getDefaultNumThreads()
    {
        return this.DEFAULT_NUM_THREADS;
    }

    /**
     * Get the default ramp-up value.
     *
     * @return the default ramp-up value (in seconds).
     */
    public int getDefaultRampUp()
    {
        return this.DEFAULT_RAMP_UP;
    }

    /**
     * Add a test element.
     *
     * @param child the test element to add.
     */
    public void addTestElement(TestElement child)
    {
        getSamplerController().addTestElement(child);
    }

    /**
     * A sample has occurred.
     *
     *@param e the sample event.
     */
    public void sampleOccurred(SampleEvent e)
    {
        if (queue == null)
        {
            queue = new SampleQueue();
            Thread thread = new Thread(queue);
            //thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        }
        queue.sampleOccurred(e);
    }

    /**
     * A sample has started.
     *
     *@param e the sample event.
     */
    public void sampleStarted(SampleEvent e)
    {
    }

    /**
     * A sample has stopped.
     *
     * @param e the sample event
     */
    public void sampleStopped(SampleEvent e)
    {
    }

    /**
     * Separate thread to deliver all SampleEvents. This ensures that sample
     * listeners will get sample events one at a time and can thus ignore thread
     * issues.
     *
     * @author Mike Stover
     * @version $Id: ThreadGroup.java 323379 2003-06-28 19:35:40Z jeremy_a $
     */
    private class SampleQueue implements Runnable, Serializable
    {
        List occurredQ = Collections.synchronizedList(new LinkedList());

        /**
         * No-arg constructor.
         */
        public SampleQueue()
        {
        }

        /**
         * A sample occurred.
         * 
         * @param e the sample event.
         */
        public synchronized void sampleOccurred(SampleEvent e)
        {
            occurredQ.add(e);
            this.notify();
        }

        /**
         * Run the thread.
         * 
         * @see java.lang.Runnable#run()
         */
        public void run()
        {
            SampleEvent event = null;
            while (true)
            {
                try
                {
                    event = (SampleEvent) occurredQ.remove(0);
                }
                catch (Exception ex)
                {
                    waitForSamples();
                    continue;
                }
                try
                {
                    if (event != null)
                    {
                        Iterator iter = listeners.iterator();
                        while (iter.hasNext())
                        {
                            ((SampleListener) iter.next()).sampleOccurred(
                                event);
                        }
                        iter = remoteListeners.iterator();
                        while (iter.hasNext())
                        {
                            try
                            {
                                (
                                    (RemoteSampleListener) iter
                                        .next())
                                        .sampleOccurred(
                                    event);
                            }
                            catch (Exception ex)
                            {
                                log.error("", ex);
                            }
                        }
                    }
                    else
                    {
                        waitForSamples();
                    }
                }
                catch (Throwable ex)
                {
                    log.error("", ex);
                }

            }
        }

        private synchronized void waitForSamples()
        {
            try
            {
                this.wait();
            }
            catch (Exception ex)
            {
                log.error("", ex);
            }
        }
    }
    
    /* (non-Javadoc)
     * @see Controller#addIterationListener(LoopIterationListener)
     */
    public void addIterationListener(LoopIterationListener lis)
    {
        getSamplerController().addIterationListener(lis);
    }

    /* (non-Javadoc)
     * @see Controller#initialize()
     */
    public void initialize()
    {
        getSamplerController().initialize();
    }

}
