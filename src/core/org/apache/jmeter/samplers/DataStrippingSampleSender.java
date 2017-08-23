package org.apache.jmeter.samplers;

import java.io.Serializable;
import java.rmi.RemoteException;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * The standard remote sample reporting should be more friendly to the main purpose of
 * remote testing - which is scalability.  To increase scalability, this class strips out the 
 * response data before sending.
 * 
 * @author Michael Stover (mstover1@apache.org).
 *
 */
public class DataStrippingSampleSender implements SampleSender,Serializable {
	private static final long serialVersionUID = 1;
	transient private static Logger log = LoggingManager.getLoggerForClass();
	
	RemoteSampleListener listener;
	SampleSender decoratedSender;

	public DataStrippingSampleSender(RemoteSampleListener listener) {
		this.listener = listener;
	}
	
	public DataStrippingSampleSender(SampleSender decorate)
	{
		decoratedSender = decorate;
	}

	public void testEnded() {
		if(decoratedSender != null) decoratedSender.testEnded();
	}

	public void testEnded(String host) {
		if(decoratedSender != null) decoratedSender.testEnded(host);
	}

	public void SampleOccurred(SampleEvent e) {
		//Strip the response data before wiring, but only for a successful request.
		if(e.getResult().isSuccessful()) e.getResult().setResponseData(new byte[0]);
		if(decoratedSender == null)
		{
			try {
				listener.sampleOccurred(e);
			} catch (RemoteException e1) {
				log.error("Error sending sample result over network ",e1);
			}
		}
		else
		{
			decoratedSender.SampleOccurred(e);
		}
	}

}
