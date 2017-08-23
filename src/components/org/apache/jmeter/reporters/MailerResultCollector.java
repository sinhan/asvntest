// $Header$
/*
 * Copyright 2001-2004 The Apache Software Foundation.
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

package org.apache.jmeter.reporters;

import java.io.Serializable;

import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.testelement.property.TestElementProperty;
//import org.apache.jorphan.logging.LoggingManager;
//import org.apache.log.Logger;

/**
 * @author Michael Stover
 * @version $Revision: 324279 $
 */
public class MailerResultCollector
    extends ResultCollector
    implements Serializable
{
    //transient private static Logger log = LoggingManager.getLoggerForClass();
    public static final String MAILER_MODEL =
        "MailerResultCollector.mailer_model";


    public MailerResultCollector()
    {
        super();
        setProperty(new TestElementProperty(MAILER_MODEL, new MailerModel()));
    }
    
    public void clear()
    {
        super.clear();
        setProperty(new TestElementProperty(MAILER_MODEL,new MailerModel()));
    }
    

    /* (non-Javadoc)
     * @see SampleListener#sampleOccurred(SampleEvent)
     */
    public void sampleOccurred(SampleEvent e)
    {
        // TODO Auto-generated method stub
        super.sampleOccurred(e);
        getMailerModel().add(e.getResult());
    }
    
    public MailerModel getMailerModel()
    {
        return (MailerModel)getProperty(MAILER_MODEL).getObjectValue();
    }
}
