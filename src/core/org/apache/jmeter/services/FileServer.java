/*
 * Copyright 2004-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *  
 */

/*
 * Created on Oct 19, 2004
 */
package org.apache.jmeter.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.apache.jmeter.gui.JMeterFileFilter;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @author mstover
 * 
 * The point of this class is to provide thread-safe access to files, and to
 * provide some simplifying assumptions about where to find files and how to
 * name them. For instance, putting supporting files in the same directory as
 * the saved test plan file allows users to refer to the file with just it's
 * name - this FileServer class will find the file without a problem.
 * Eventually, I want all in-test file access to be done through here, with the
 * goal of packaging up entire test plans as a directory structure that can be
 * sent via rmi to remote servers (currently, one must make sure the remote
 * server has all support files in a relative-same location) and to package up
 * test plans to execute on unknown boxes that only have Java installed.
 */
public class FileServer {
	private static final Logger log = LoggingManager.getLoggerForClass();

    private static final String DEFAULT_BASE = JMeterUtils.getProperty("user.dir");
    
	private File base;

    //TODO - make "files" and "random" static as the class is a singleton?
    
	private final Map files = new HashMap();
    
	private static final FileServer server = new FileServer();

	private final Random random = new Random();

	private FileServer() {
		base = new File(DEFAULT_BASE);
	}

	public static FileServer getFileServer() {
		return server;
	}

    public void resetBase() throws IOException{
        setBasedir(DEFAULT_BASE);
    }
    
	public synchronized void setBasedir(String basedir) throws IOException {
		if (filesOpen()) {
			throw new IOException("Files are still open, cannot change base directory");
		}
		files.clear();
		if (basedir != null) {
			base = new File(basedir);
			if (!base.isDirectory()) {
				base = base.getParentFile();
			}
		}
	}

	public synchronized String getBaseDir() {
		return base.getAbsolutePath();
	}

    /**
     * Creates an association between a filename and a File inputOutputObject,
     * and stores it for later use - unless it is already stored.
     * 
     * @param filename - relative (to base) or absolute file name
     */
	public synchronized void reserveFile(String filename) {
		if (!files.containsKey(filename)) {
            File f = new File(filename); 
            FileEntry file = 
                new FileEntry(f.isAbsolute() ? f : new File(base, filename),null);
            log.info("Stored: "+filename);
			files.put(filename, file);
		}
	}

   /**
	 * Get the next line of the named file, recycle by default.
	 *
     * @param filename
	 * @return String containing the next line in the file
	 * @throws IOException
	 */
    public String readLine(String filename) throws IOException {
      return readLine(filename, true);
    }

   /**
	 * Get the next line of the named file.
	 * 
     * @param filename
     * @param recycle - should file be restarted at EOF?
	 * @return String containing the next line in the file (null if EOF reached and not recycle)
	 * @throws IOException
	 */
	public synchronized String readLine(String filename, boolean recycle) throws IOException {
		FileEntry fileEntry = (FileEntry) files.get(filename);
		if (fileEntry != null) {
			if (fileEntry.inputOutputObject == null) {
				BufferedReader r = new BufferedReader(new FileReader(fileEntry.file));
				fileEntry.inputOutputObject = r;
            } else if (!(fileEntry.inputOutputObject instanceof Reader)) {
                throw new IOException("File " + filename + " already in use");
            }
			BufferedReader reader = (BufferedReader) fileEntry.inputOutputObject;
			String line = reader.readLine();
			if (line == null && recycle) {
				reader.close();
				reader = new BufferedReader(new FileReader(fileEntry.file));
				fileEntry.inputOutputObject = reader;
				line = reader.readLine();
			}
            if (log.isDebugEnabled()) log.debug("Read:"+line);
			return line;
		}
		throw new IOException("File never reserved: "+filename);
	}

	public synchronized void write(String filename, String value) throws IOException {
		FileEntry fileEntry = (FileEntry) files.get(filename);
		if (fileEntry != null) {
			if (fileEntry.inputOutputObject == null) {
				fileEntry.inputOutputObject = new BufferedWriter(new FileWriter(fileEntry.file));
			} else if (!(fileEntry.inputOutputObject instanceof Writer)) {
				throw new IOException("File " + filename + " already in use");
			}
			BufferedWriter writer = (BufferedWriter) fileEntry.inputOutputObject;
            if (log.isDebugEnabled()) log.debug("Write:"+value);
			writer.write(value);
		} else {
            throw new IOException("File never reserved: "+filename);      
        }
	}

	public void closeFiles() throws IOException {
		Iterator iter = files.entrySet().iterator();
		while (iter.hasNext()) {
            Map.Entry me = (Map.Entry) iter.next();
			closeFile((String)me.getKey(),(FileEntry)me.getValue() );
		}
		files.clear();
	}

	/**
	 * @param name
	 * @throws IOException
	 */
	public synchronized void closeFile(String name) throws IOException {
		FileEntry fileEntry = (FileEntry) files.get(name);
		closeFile(name, fileEntry);
	}

    private void closeFile(String name, FileEntry fileEntry) throws IOException {
        if (fileEntry != null && fileEntry.inputOutputObject != null) {
            log.info("Close: "+name);
			((Reader) fileEntry.inputOutputObject).close();
			fileEntry.inputOutputObject = null;
		}
    }

	protected boolean filesOpen() {
		Iterator iter = files.values().iterator();
		while (iter.hasNext()) {
			FileEntry fileEntry = (FileEntry) iter.next();
            if (fileEntry.inputOutputObject != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method will get a random file in a base directory
     * TODO hey, not sure this method belongs here.  FileServer is for threadsafe
     * File access relative to current test's base directory.  
	 * 
	 * @param basedir
	 * @return
	 */
	public File getRandomFile(String basedir, String[] extensions) {
		File input = null;
		if (basedir != null) {
			File src = new File(basedir);
			if (src.isDirectory() && src.list() != null) {
				File[] lfiles = src.listFiles(new JMeterFileFilter(extensions));
				int count = lfiles.length;
				input = lfiles[random.nextInt(count)];
			}
		}
		return input;
	}
    
    private static class FileEntry{
        private File file;
        private Object inputOutputObject; // Reader/Writer
        FileEntry(File f, Object o){
            file=f;
            inputOutputObject=o;
        }
    }
}
