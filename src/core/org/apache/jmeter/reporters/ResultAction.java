package org.apache.jmeter.reporters;

import java.io.Serializable;

import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.OnErrorTestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * ResultAction - take action based on the status of the last Result
 *  
 * @author sebb AT apache DOT org
 * @version $Revision: 323849 $ Last updated: $Date: 2003-12-09 15:17:39 -0800 (Tue, 09 Dec 2003) $
 */
public class ResultAction
    extends OnErrorTestElement
    implements Serializable,
    SampleListener,
    Clearable
{
	private static final Logger log = LoggingManager.getLoggerForClass();
    
    /*
     * Constructor is initially called once for each occurrence in the test plan
     * For GUI, several more instances are created
     * Then clear is called at start of test
     * Called several times during test startup
     * The name will not necessarily have been set at this point.
     */
	public ResultAction(){
		super();
		//log.debug(Thread.currentThread().getName());
		//System.out.println(">> "+me+"        "+this.getName()+" "+Thread.currentThread().getName());		
	}

    /*
     * This is called once for each occurrence in the test plan, before the start of the test.
     * The super.clear() method clears the name (and all other properties),
     * so it is called last.
     */
	public void clear()
	{
		//System.out.println("-- "+me+this.getName()+" "+Thread.currentThread().getName());
		super.clear();
	}
	
	/**
	 * Examine the sample(s) and take appropriate action 
	 * 
	 * @see org.apache.jmeter.samplers.SampleListener#sampleOccurred(org.apache.jmeter.samplers.SampleEvent)
	 */
	public void sampleOccurred(SampleEvent e) {
		SampleResult s = e.getResult();
		log.debug(s.getSampleLabel()+" OK? "+s.isSuccessful());
		if (!s.isSuccessful()) {
			if (isStopTest()) {
				s.setStopTest(true);
			}
			if (isStopThread()) {
				s.setStopThread(true);
			}
		}
    }

    /* (non-Javadoc)
	 * @see org.apache.jmeter.samplers.SampleListener#sampleStarted(org.apache.jmeter.samplers.SampleEvent)
	 */
	public void sampleStarted(SampleEvent e) 
	{
		// not used
	}

	/* (non-Javadoc)
	 * @see org.apache.jmeter.samplers.SampleListener#sampleStopped(org.apache.jmeter.samplers.SampleEvent)
	 */
	public void sampleStopped(SampleEvent e)
	{
		// not used
	}
}
