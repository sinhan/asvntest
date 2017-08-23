/*
 * Copyright 2005 The Apache Software Foundation.
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
package org.apache.jmeter.protocol.java.sampler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @author pete
 *
 * This is a basic implementation that runs a single test method of
 * a JUnit test case. The current implementation will use the string
 * constructor first. If the test class does not declare a string
 * constructor, the sampler will try empty constructor.
 */
public class JUnitSampler extends AbstractSampler {

    /**
     * Property key representing the classname of the JavaSamplerClient to
     * user.
     */
    public static final String CLASSNAME = "junitSampler.classname";
    public static final String CONSTRUCTORSTRING = "junitsampler.constructorstring";
    public static final String METHOD = "junitsampler.method";
    public static final String ERROR = "junitsampler.error";
    public static final String ERRORCODE = "junitsampler.error.code";
    public static final String FAILURE = "junitsampler.failure";
    public static final String FAILURECODE = "junitsampler.failure.code";
    public static final String SUCCESS = "junitsampler.success";
    public static final String SUCCESSCODE = "junitsampler.success.code";
    public static final String FILTER = "junitsampler.pkg.filter";
    public static final String DOSETUP = "junitsampler.exec.setup";
    
    public static final String SETUP = "setUp";
    public static final String TEARDOWN = "tearDown";
    /// the Method objects for setUp and tearDown methods
    protected Method SETUP_METHOD = null;
    protected Method TDOWN_METHOD = null;
    protected boolean checkStartUpTearDown = false;
    
    protected TestCase TEST_INSTANCE = null;
    
    /**
     * Logging
     */
    private static transient Logger log = LoggingManager.getLoggerForClass();

    public JUnitSampler(){
    }
    
    /**
     * Method tries to get the setUp and tearDown method for the class
     * @param tc
     */
    public void initMethodObjects(TestCase tc){
        if (!this.checkStartUpTearDown && !getDoNotSetUpTearDown()) {
			if (SETUP_METHOD == null) {
				SETUP_METHOD = getMethod(tc, SETUP);
			}
			if (TDOWN_METHOD == null) {
				TDOWN_METHOD = getMethod(tc, TEARDOWN);
			}
			this.checkStartUpTearDown = true;
		}
    }
    
    /**
	 * Sets the Classname attribute of the JavaConfig object
	 * 
	 * @param classname
	 *            the new Classname value
	 */
    public void setClassname(String classname)
    {
        setProperty(CLASSNAME, classname);
    }

    /**
     * Gets the Classname attribute of the JavaConfig object
     *
     * @return  the Classname value
     */
    public String getClassname()
    {
        return getPropertyAsString(CLASSNAME);
    }
    
    /**
     * Set the string label used to create an instance of the
     * test with the string constructor.
     * @param constr
     */
    public void setConstructorString(String constr)
    {
        setProperty(CONSTRUCTORSTRING,constr);
    }
    
    /**
     * get the string passed to the string constructor
     * @return
     */
    public String getConstructorString()
    {
        return getPropertyAsString(CONSTRUCTORSTRING);
    }
    
    /**
     * Return the name of the method to test
     * @return
     */
    public String getMethod(){
        return getPropertyAsString(METHOD);
    }

    /**
     * Method should add the JUnit testXXX method to the list at
     * the end, since the sequence matters.
     * @param methodName
     */
    public void setMethod(String methodName){
        setProperty(METHOD,methodName);
    }
    
    /**
     * get the success message
     * @return
     */
    public String getSuccess(){
        return getPropertyAsString(SUCCESS);
    }
    
    /**
     * set the success message
     * @param success
     */
    public void setSuccess(String success){
        setProperty(SUCCESS,success);
    }
    
    /**
     * get the success code defined by the user
     * @return
     */
    public String getSuccessCode(){
        return getPropertyAsString(SUCCESSCODE);
    }

    /**
     * set the succes code. the success code should
     * be unique.
     * @param code
     */
    public void setSuccessCode(String code){
        setProperty(SUCCESSCODE,code);
    }
    
    /**
     * get the failure message
     * @return
     */
    public String getFailure(){
        return getPropertyAsString(FAILURE);
    }

    /**
     * set the failure message
     * @param fail
     */
    public void setFailure(String fail){
        setProperty(FAILURE,fail);
    }
    
    /**
     * The failure code is used by other components
     * @return
     */
    public String getFailureCode(){
        return getPropertyAsString(FAILURECODE);
    }
    
    /**
     * Provide some unique code to denote a type of failure
     * @param code
     */
    public void setFailureCode(String code){
        setProperty(FAILURECODE,code);
    }

    /**
     * return the descriptive error for the test
     * @return
     */
    public String getError(){
        return getPropertyAsString(ERROR);
    }
    
    /**
     * provide a descriptive error for the test method. For
     * a description of the difference between failure and
     * error, please refer to the following url
     * http://junit.sourceforge.net/doc/faq/faq.htm#tests_9
     * @param error
     */
    public void setError(String error){
        setProperty(ERROR,error);
    }
    
    /**
     * return the error code for the test method. it should
     * be an unique error code.
     * @return
     */
    public String getErrorCode(){
        return getPropertyAsString(ERRORCODE);
    }
    
    /**
     * provide an unique error code for when the test
     * does not pass the assert test.
     * @param code
     */
    public void setErrorCode(String code){
        setProperty(ERRORCODE,code);
    }
    
    /**
     * return the comma separated string for the filter
     * @return
     */
    public String getFilterString(){
        return getPropertyAsString(FILTER);
    }
    
    /**
     * set the filter string in comman separated format
     * @param text
     */
    public void setFilterString(String text){
        setProperty(FILTER,text);
    }
    
    public boolean getDoNotSetUpTearDown(){
        return getPropertyAsBoolean(DOSETUP);
    }
    
    public void setDoNotSetUpTearDown(boolean setup){
        setProperty(DOSETUP,String.valueOf(setup));
    }
    
    /* (non-Javadoc)
	 * @see org.apache.jmeter.samplers.Sampler#sample(org.apache.jmeter.samplers.Entry)
	 */
	public SampleResult sample(Entry entry) {
		SampleResult sresult = new SampleResult();
        String rlabel = null;
        if (getConstructorString().length() > 0) {
            rlabel = getConstructorString();
        } else {
            rlabel = JUnitSampler.class.getName();
        }
        sresult.setSampleLabel(rlabel);
        sresult.setSamplerData(getClassname() + "." + getMethod());
        // check to see if the test class is null. if it is, we create
        // a new instance. this should only happen at the start of a
        // test run
        if (this.TEST_INSTANCE == null) {
            this.TEST_INSTANCE = (TestCase)getClassInstance(getClassname(),rlabel);
        }
        if (this.TEST_INSTANCE != null){
            initMethodObjects(this.TEST_INSTANCE);
            // create a new TestResult
            TestResult tr = new TestResult();
            try {
                if (!getDoNotSetUpTearDown() && SETUP_METHOD != null){
                    SETUP_METHOD.invoke(TEST_INSTANCE,new Class[0]);
                }
                Method m = getMethod(TEST_INSTANCE,getMethod());
                sresult.sampleStart();
                m.invoke(TEST_INSTANCE,null);
                sresult.sampleEnd();
                // log.info("invoked " + getMethod());
                if (!getDoNotSetUpTearDown() && TDOWN_METHOD != null){
                    TDOWN_METHOD.invoke(TEST_INSTANCE,new Class[0]);
                }
            } catch (InvocationTargetException e) {
                // log.warn(e.getMessage());
                sresult.setResponseCode(getErrorCode());
                sresult.setResponseMessage(getError());
                sresult.setResponseData(e.getMessage().getBytes());
                sresult.setSuccessful(false);
            } catch (IllegalAccessException e) {
                // log.warn(e.getMessage());
                sresult.setResponseCode(getErrorCode());
                sresult.setResponseMessage(getError());
                sresult.setResponseData(e.getMessage().getBytes());
                sresult.setSuccessful(false);
            } catch (IllegalArgumentException e) {
                sresult.setResponseCode(getErrorCode());
                sresult.setResponseMessage(getError());
                sresult.setResponseData(e.getMessage().getBytes());
                sresult.setSuccessful(false);
            } catch (Exception e) {
                sresult.setResponseCode(getErrorCode());
                sresult.setResponseMessage(getError());
                sresult.setResponseData(e.getMessage().getBytes());
                sresult.setSuccessful(false);
            } catch (Throwable e) {
                sresult.setResponseCode(getErrorCode());
                sresult.setResponseMessage(getError());
                sresult.setResponseData(e.getMessage().getBytes());
                sresult.setSuccessful(false);
            }
            if ( !tr.wasSuccessful() ){
                sresult.setSuccessful(false);
                StringBuffer buf = new StringBuffer();
                buf.append(getFailure());
                Enumeration en = tr.errors();
                while (en.hasMoreElements()){
                    buf.append((String)en.nextElement());
                }
                sresult.setResponseMessage(buf.toString());
                sresult.setResponseCode(getFailureCode());
            } else {
                // this means there's no failures
                sresult.setSuccessful(true);
                sresult.setResponseMessage(getSuccess());
                sresult.setResponseCode(getSuccessCode());
            }
        } else {
            // we should log a warning, but allow the test to keep running
            sresult.setSuccessful(false);
            // this should be externalized to the properties
            sresult.setResponseMessage("failed to create an instance of the class");
        }
		return sresult;
	}

    /**
     * If the method is not able to create a new instance of the
     * class, it returns null and logs all the exceptions at
     * warning level.
     * @return
     */
    public static Object getClassInstance(String className, String label){
        Object testclass = null;
        if (className != null){
            Constructor con = null;
            Class theclazz = null;
            Object[] params = null;
            try
            {
                theclazz = Class.forName(
                            className.trim(),
                            true,
                            Thread.currentThread().getContextClassLoader()
                        );
            } catch (ClassNotFoundException e) {
                log.warn("ClassNotFoundException:: " + e.getMessage());
            }
            if (theclazz != null) {
                // first we see if the class declares a string
                // constructor. if it is doesn't we look for
                // empty constructor.
                try {
                    con = theclazz.getDeclaredConstructor(
                            new Class[] {String.class});
                    params = new Object[]{label};
                } catch (NoSuchMethodException e) {
                    log.info("String constructor:: " + e.getMessage());
                }
                if (con == null ){
                    try {
                        con = theclazz.getDeclaredConstructor(new Class[0]);
                        if (con != null){
                            params = new Object[]{};
                        }
                    } catch (NoSuchMethodException e) {
                        log.info("Empty constructor:: " + e.getMessage());
                    }
                }
                try {
                    if (con != null){
                        testclass = (TestCase)con.newInstance(params);
                    }
                } catch (InvocationTargetException e) {
                    log.warn(e.getMessage());
                } catch (InstantiationException e) {
                    log.info(e.getMessage());
                } catch (IllegalAccessException e) {
                    log.info(e.getMessage());
                }
            }
        }
        return testclass;
    }
    
    /**
     * 
     * @param clazz
     * @param method
     * @return
     */
    public Method getMethod(Object clazz, String method){
        if (clazz != null && method != null){
            log.info("class " + clazz.getClass().getName() +
                    " method name is " + method);
            try {
                return clazz.getClass().getMethod(method,new Class[0]);
            } catch (NoSuchMethodException e) {
                log.warn(e.getMessage());
            }
        }
        return null;
    }
}
