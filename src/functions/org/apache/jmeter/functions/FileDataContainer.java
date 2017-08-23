/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
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
package org.apache.jmeter.functions;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Creation date: (20/03/2003 09:37:44)
 * @author  Cyrus Montakab
 * @version $Revision: 323720 $
 */
public class FileDataContainer
{
    transient private static Logger log = LoggingManager.getLoggerForClass();

    private ArrayList fileData;

    /** Keeping track on which row was last read. */
    private int rowPosition = 0;

    /**
     * FileDataContainer constructor comment.
     */
    public FileDataContainer()
    {
        super();
        this.fileData = new ArrayList();
        rowPosition = 0;
        log.debug(Thread.currentThread().getName() +" Constructed()");
    }

    /**
     * FileDataContainer constructor comment.
     */
    public FileDataContainer(ArrayList newFileData)
    {
        super();
        fileData = newFileData;
		rowPosition = 0;
		log.debug(Thread.currentThread().getName() +" Constructed(fileData)");
    }

    /**
     * Creation date: (20/03/2003 09:39:50)
     * @param newLine - line to be stored
     */
    public void addLine(String newLine)
    {
        fileData.add(processNextCSVLine(newLine));
    }

    /**
     * Creation date: (20/03/2003 09:39:50)
     * @return java.util.ArrayList
     */
    public ArrayList getNextLine()
    {
        ArrayList result = null;
        if (fileData != null && fileData.size() > 0)
        {
        	if (log.isDebugEnabled()) {
				log.debug(Thread.currentThread().getName() +" getNextline, Row = " + rowPosition);
        	}
            result = (ArrayList) fileData.get(rowPosition);
        }
        return result;
    }
    
    /**
     * Creation date: (20/03/2003 09:39:50)
     * @return int
     */
    public int getRowPosition()
    {
		if (log.isDebugEnabled()) {
    	    log.debug(Thread.currentThread().getName() +" getRowPosition = " + rowPosition);
		}
        return rowPosition;
    }

    /**
     * Moves the file pointer to the next row, wrapping
     * round to the beginning if necessary.
     * 
     *  Creation date: (20/03/2003 09:39:50)
     *
     */
    public void incrementRowPosition()
    {
        if (this.fileData != null && fileData.size() > 0)
        {
            rowPosition++;
            if (rowPosition >= fileData.size())
            {
                rowPosition = 0;
            }
        }
        if (log.isDebugEnabled()){
			log.debug (Thread.currentThread().getName() +
				 " >>>> incrementRowPosition - returning : "+ rowPosition);
        }
    }
    
    /**
     * @return        an ArrayList of Strings containing one element for each
     *                value in the line
     */
    protected ArrayList processNextCSVLine(String theLine)
    {
        ArrayList result = new ArrayList();
        StringTokenizer tokener = new StringTokenizer(theLine,",");
        while(tokener.hasMoreTokens())
        {
            String token = tokener.nextToken();
            result.add(token);
        }
        return result;
    }
}