// $Header$
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

package org.apache.jmeter.control;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * 
 * 
 * @author Peter Lin
 * @version $Revision: 325662 $
 */
public class IncludeController extends GenericController implements ReplaceableController {
	private static final Logger log = LoggingManager.getLoggerForClass();

    public static final String INCLUDE_PATH = "IncludeController.includepath";

    private HashTree SUBTREE = null;
    private TestElement SUB = null;

	/**
	 * No-arg constructor
	 * 
	 * @see java.lang.Object#Object()
	 */
	public IncludeController() {
		super();
	}

	public Object clone() {
        this.loadIncludedElements();
		IncludeController clone = (IncludeController) super.clone();
        clone.setIncludePath(this.getIncludePath());
        if (this.SUBTREE != null) {
            if (this.SUBTREE.keySet().size() == 1) {
                Iterator itr = this.SUBTREE.keySet().iterator();
                while (itr.hasNext()) {
                    this.SUB = (TestElement)itr.next();
                }
            }
            clone.SUBTREE = (HashTree)this.SUBTREE.clone();
            clone.SUB = (TestElement)this.SUB.clone();
        }
		return clone;
	}

    /**
     * In the event an user wants to include an external JMX test plan
     * the GUI would call this.
     * @param jmxfile
     */
    public void setIncludePath(String jmxfile) {
        this.setProperty(INCLUDE_PATH,jmxfile);
    }
    
    /**
     * return the JMX file path.
     * @return
     */
    public String getIncludePath() {
        return this.getPropertyAsString(INCLUDE_PATH);
    }
    
    /**
     * The way ReplaceableController works is clone is called first,
     * followed by replace(HashTree) and finally getReplacement().
     */
    public HashTree getReplacementSubTree() {
        return SUBTREE;
    }

    /**
     * load the included elements using SaveService
     * @param node
     */
    protected HashTree loadIncludedElements() {
        // only try to load the JMX test plan if there is one
        if (getIncludePath() != null && getIncludePath().length() > 0) {
            try {
                log.info("loadIncludedElements -- try to load included module");
                InputStream reader = new FileInputStream(getIncludePath());
                this.SUBTREE = SaveService.loadTree(reader);
                return this.SUBTREE;
            } catch (NoClassDefFoundError ex) // Allow for missing optional jars
            {
                String msg = ex.getMessage();
                if (msg == null) {
                    msg = "Missing jar file - see log for details";
                    log.warn("Missing jar file", ex);
                }
                JMeterUtils.reportErrorToUser(msg);
            } catch (Exception ex) {
                String msg = ex.getMessage();
                if (msg == null) {
                    msg = "Unexpected error - see log for details";
                    log.warn("Unexpected error", ex);
                }
            }
        }
        return this.SUBTREE;
    }
    
}
