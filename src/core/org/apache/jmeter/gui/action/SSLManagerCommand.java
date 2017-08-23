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

package org.apache.jmeter.gui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.util.SSLManager;

/**
 * SSL Manager Command. The SSL Manager provides a mechanism to change your
 * client authentication if required by the server. If you have JSSE 1.0.2
 * installed, you can select your client identity from a list of installed keys.
 * You can also change your keystore. JSSE 1.0.2 allows you to export a PKCS#12
 * key from Netscape 4.04 or higher and use it in a read only format. You must
 * supply a password that is greater than six characters due to limitations in
 * the keytool program--and possibly the rest of the system.
 * <p>
 * By selecting a *.p12 file as your keystore (your PKCS#12) format file, you
 * can have a whopping one key keystore. The advantage is that you can test a
 * connection using the assigned Certificate from a Certificate Authority.
 * </p>
 * 
 * @author <a href="bloritsch@apache.org">Berin Loritsch</a>
 * @version CVS $Revision: 325542 $ $Date: 2005-07-12 13:51:09 -0700 (Tue, 12 Jul 2005) $
 */
public class SSLManagerCommand implements Command {
	private static Set commandSet;
	static {
		HashSet commands = new HashSet();
		commands.add("sslManager");
		SSLManagerCommand.commandSet = Collections.unmodifiableSet(commands);
	}

	private JFileChooser keyStoreChooser;

	/**
	 * Handle the "sslmanager" action by displaying the "SSL CLient Manager"
	 * dialog box. The Dialog Box is NOT modal, because those should be avoided
	 * if at all possible.
	 */
	public void doAction(ActionEvent e) {
		if (e.getActionCommand().equals("sslManager")) {
			this.sslManager();
		}
	}

	/**
	 * Provide the list of Action names that are available in this command.
	 */
	public Set getActionNames() {
		return SSLManagerCommand.commandSet;
	}

	/**
	 * Called by sslManager button. Raises sslManager dialog. Currently the
	 * sslManager box has the product image and the copyright notice. The dialog
	 * box is centered over the MainFrame.
	 */
	private void sslManager() {
		SSLManager.reset();

		keyStoreChooser = new JFileChooser(JMeterUtils.getJMeterProperties().getProperty("user.dir"));
		keyStoreChooser.addChoosableFileFilter(new AcceptPKCS12FileFilter());
		keyStoreChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int retVal = keyStoreChooser.showOpenDialog(GuiPackage.getInstance().getMainFrame());

		if (JFileChooser.APPROVE_OPTION == retVal) {
			File selectedFile = keyStoreChooser.getSelectedFile();
			try {
				JMeterUtils.getJMeterProperties()
						.setProperty("javax.net.ssl.keyStore", selectedFile.getCanonicalPath());
			} catch (Exception e) {
			}
		}

		keyStoreChooser = null;
		SSLManager.getInstance();
	}

	/**
	 * Internal class to add a PKCS12 file format filter for JFileChooser.
	 */
	static private class AcceptPKCS12FileFilter extends FileFilter {
		/**
		 * Get the description that shows up in JFileChooser filter menu.
		 * 
		 * @return description
		 */
		public String getDescription() {
			return JMeterUtils.getResString("pkcs12_desc");
		}

		/**
		 * Tests to see if the file ends with "*.p12" or "*.P12".
		 * 
		 * @param testfile
		 *            file to test
		 * @return true if file is accepted, false otherwise
		 */
		public boolean accept(File testFile) {
			return testFile.isDirectory() || testFile.getName().endsWith(".p12") || testFile.getName().endsWith(".P12");
		}
	}
}
