package org.apache.jmeter.protocol.java.sampler;

import org.apache.bsf.*;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * A sampler which understands BSF
 *
 * @author sebb AT apache DOT org
 * @version    $Revision: 323915 $ Updated on: $Date: 2003-12-19 06:38:47 -0800 (Fri, 19 Dec 2003) $
 */
public class BSFSampler extends AbstractSampler
{

    protected static Logger log = LoggingManager.getLoggerForClass();

    public static final String FILENAME   = "BSFSampler.filename"; //$NON-NLS-1$
	public static final String SCRIPT     = "BSFSampler.query"; //$NON-NLS-1$
	public static final String LANGUAGE   = "BSFSampler.language"; //$NON-NLS-1$
	public static final String PARAMETERS = "BSFSampler.parameters"; //$NON-NLS-1$

	private BSFManager mgr;
	private BSFEngine bsfEngine;
	
	public BSFSampler()
	{
		try {
			// register beanshell with the BSF framework
			BSFManager.registerScriptingEngine("beanshell",
			 "bsh.util.BeanShellBSFEngine", new String [] { "bsh"} );
		} catch (NoClassDefFoundError e){
		}
	     
	   //TODO: register other scripting languages ...

	}
    
	public String getFilename()
	{
		return getPropertyAsString(FILENAME);
	}
	public void setFilename(String newFilename)
	{
		this.setProperty(FILENAME, newFilename);
	}
	public String getScript()
	{
		return this.getPropertyAsString(SCRIPT);
	}
	public void setScript(String newScript)
	{
		this.setProperty(SCRIPT, newScript);
	}
	public String getParameters()
	{
		return this.getPropertyAsString(PARAMETERS);
	}
	public void setParameters(String newScript)
	{
		this.setProperty(PARAMETERS, newScript);
	}
	public String getScriptLanguage()
	{
		return this.getPropertyAsString(LANGUAGE);
	}
	public void setScriptLanguage(String lang)
	{
		this.setProperty(LANGUAGE,lang);
	}

    /**
     * Returns a formatted string label describing this sampler
     *
     * @return a formatted string label describing this sampler
     */

    public String getLabel()
    {
        return getName();
    }


    public SampleResult sample(Entry e)// Entry tends to be ignored ...
    {
    	log.info(getLabel()+" "+getFilename());
        SampleResult res = new SampleResult();
        boolean isSuccessful = false;
        res.setSampleLabel(getLabel());
        res.sampleStart();
        try
        {
        	String request=getScript();
			res.setSamplerData(request);

			mgr.registerBean("Label",getLabel());
			mgr.registerBean("Name",getFilename());

			bsfEngine = mgr.loadScriptingEngine(getScriptLanguage());

			Object bsfOut = bsfEngine.eval("Sampler",0,0,request);

	        res.setResponseData(bsfOut.toString().getBytes());
	        res.setDataType(SampleResult.TEXT);
	        res.setResponseCode("200");//TODO set from script
	        res.setResponseMessage("OK");//TODO set from script
	        isSuccessful = true;//TODO set from script
        }
		catch (NoClassDefFoundError ex)
		{
			log.warn("",ex);
			res.setResponseCode("500");
			res.setResponseMessage(ex.toString());
		}
        catch (Exception ex)
        {
        	log.warn("",ex);
			res.setResponseCode("500");
            res.setResponseMessage(ex.toString());
        }

		res.sampleEnd();

        // Set if we were successful or not
        res.setSuccessful(isSuccessful);

        return res;
    }
}
