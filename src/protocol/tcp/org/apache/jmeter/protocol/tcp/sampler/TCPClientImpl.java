/*
 * Basic TCP Sampler Client class
 * 
 * Can be used to test the TCP Sampler against an HTTP server
 * 
 * The protocol handler class name is defined by the property tcp.handler
 * 
 */
package org.apache.jmeter.protocol.tcp.sampler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 *
 * @author sebb AT apache DOT org
 * @version $Revision: 323687 $ $Date: 2003-11-15 18:09:06 -0800 (Sat, 15 Nov 2003) $  
 *
 */
public class TCPClientImpl implements TCPClient
{
	private static Logger log = LoggingManager.getLoggerForClass();

    public TCPClientImpl()
    {
        super();
        log.info("Created "+this);
    }


	/* (non-Javadoc)
	 * @see org.apache.jmeter.protocol.tcp.sampler.TCPClient#setupTest()
	 */
	public void setupTest()
	{
		log.info("setuptest");
        
	}

    /* (non-Javadoc)
     * @see org.apache.jmeter.protocol.tcp.sampler.TCPClient#teardownTest()
     */
    public void teardownTest()
    {
		log.info("teardowntest");
        
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.protocol.tcp.sampler.TCPClient#write(java.io.OutputStream)
     */
    public String write(OutputStream os)
    {
		String s = "GET /  HTTP/1.1\nHost: www.dummy.invalid\n\n"; //or get from file etc
		try
        {
            os.write(s.getBytes());
			os.flush();
        }
        catch (IOException e)
        {
            log.debug("Write error",e);
        }
        log.debug("Wrote: "+s);
        return s;
    }


    /* (non-Javadoc)
     * @see org.apache.jmeter.protocol.tcp.sampler.TCPClient#read(java.io.InputStream)
     */
    public String read(InputStream is)
    {
		byte [] buffer = new byte[4096];
		ByteArrayOutputStream w = new ByteArrayOutputStream();
		int x = 0;
		try {
			while ((x = is.read(buffer)) > -1)
			{
				w.write(buffer, 0, x);
			}
		} catch (SocketTimeoutException e) {
			// drop out to handle buffer
		} catch (IOException e) {
			log.debug("Read error",e);
			return "";
		}
		
		// do we need to close byte array (or flush it?)
		log.debug("Read:\n"+w.toString());
		return w.toString();
    }
}
