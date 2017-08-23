/*
 * Copyright 2002-2004 The Apache Software Foundation.
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

package org.apache.jmeter.timers;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * This class implements a constant throughput timer. A Constant Throughtput
 * Timer paces the samplers under it's influence so that the total number of
 * samples per unit of time approaches a given constant as much as possible.
 * 
 */
public class ConstantThroughputTimer extends AbstractTestElement implements Timer, TestListener, TestBean {
	private static final long serialVersionUID = 2;

	private static final Logger log = LoggingManager.getLoggerForClass();

	private static final double MILLISEC_PER_MIN = 60000.0;

	/**
	 * Target time for the start of the next request. The delay provided by the
	 * timer will be calculated so that the next request happens at this time.
	 */
	private long previousTime = 0;

	private String calcMode; // String representing the mode
								// (Locale-specific)

	private int modeInt; // mode as an integer

	/**
	 * Desired throughput, in samples per minute.
	 */
	private double throughput;

	/**
	 * Constructor for a non-configured ConstantThroughputTimer.
	 */
	public ConstantThroughputTimer() {
	}

	/**
	 * Sets the desired throughput.
	 * 
	 * @param throughput
	 *            Desired sampling rate, in samples per minute.
	 */
	public void setThroughput(double throughput) {
		this.throughput = throughput;
	}

	/**
	 * Gets the configured desired throughput.
	 * 
	 * @return the rate at which samples should occur, in samples per minute.
	 */
	public double getThroughput() {
		return throughput;
	}

	public String getCalcMode() {
		return calcMode;
	}

	public void setCalcMode(String mode) {
		// TODO find better way to get modeInt
		this.modeInt = ConstantThroughputTimerBeanInfo.getCalcModeAsInt(calcMode);
		this.calcMode = mode;
	}

	/**
	 * Retrieve the delay to use during test execution.
	 * 
	 * @see org.apache.jmeter.timers.Timer#delay()
	 */
	public synchronized long delay() {
		long currentTime = System.currentTimeMillis();
		long currentTarget = previousTime == 0 ? currentTime : previousTime;
		previousTime = currentTarget + calculateDelay();
		if (currentTime > currentTarget) {
			// We're behind schedule -- try to catch up:
			return 0;
		}
		return currentTarget - currentTime;
	}

	/**
	 * @param currentTime
	 * @return new Target time
	 */
	protected long calculateCurrentTarget(long currentTime) {
		return currentTime + calculateDelay();
	}

	// Calculate the delay based on the mode
	private long calculateDelay() {
		long offset = 0;
		long rate = (long) (MILLISEC_PER_MIN / getThroughput());
		switch (modeInt) {
		case 1: // Total number of threads
			offset = (
			// previousTime == 0 ? //TODO - why is this needed?
					// (JMeterContextService.getContext().getThreadNum() + 1)
					// :
					JMeterContextService.getNumberOfThreads()) * rate;
			break;
		case 2: // Active threads in this group
			offset = JMeterContextService.getContext().getThread().getThreadGroup().getNumberOfThreads() * rate;
			break;
		default:
			offset = rate; // i.e. rate * 1
			break;
		}
		return offset;
	}

	/**
	 * Provide a description of this timer class.
	 * 
	 * TODO: Is this ever used? I can't remember where. Remove if it isn't --
	 * TODO: or obtain text from bean's displayName or shortDescription.
	 * 
	 * @return the description of this timer class.
	 */
	public String toString() {
		return JMeterUtils.getResString("constant_throughput_timer_memo");
	}

	/**
	 * Get the timer ready to compute delays for a new test.
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testStarted()
	 */
	public synchronized void testStarted()// synch to protect targetTime
	{
		log.debug("Test started - reset throughput calculation.");
		previousTime = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testEnded()
	 */
	public void testEnded() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testStarted(java.lang.String)
	 */
	public void testStarted(String host) {
		testStarted();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testEnded(java.lang.String)
	 */
	public void testEnded(String host) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.testelement.TestListener#testIterationStart(org.apache.jmeter.engine.event.LoopIterationEvent)
	 */
	public void testIterationStart(LoopIterationEvent event) {
	}
}