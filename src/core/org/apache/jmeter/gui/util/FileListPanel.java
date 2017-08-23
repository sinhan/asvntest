// $Header$
/*
 * Copyright 2002-2004 The Apache Software Foundation.
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

package org.apache.jmeter.gui.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.JMeterFileFilter;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.jorphan.reflect.Functor;

/**
 * @author Peter Lin 
 * @version $Revision: 325810 $ Last updated: $Date: 2005-10-01 18:12:31 -0700 (Sat, 01 Oct 2005) $
 */
public class FileListPanel extends JPanel implements ActionListener {
	protected JTable files = null;

    protected transient ObjectTableModel tableModel = null;
    
	JButton browse = new JButton(JMeterUtils.getResString("browse"));

    JButton clear = new JButton(JMeterUtils.getResString("clear"));

    private JButton delete = new JButton(JMeterUtils.getResString("delete"));

    List listeners = new LinkedList();

	String title;

	String filetype;

	/**
	 * Constructor for the FilePanel object.
	 */
	public FileListPanel() {
		title = "";
		init();
	}

	public FileListPanel(String title) {
		this.title = title;
		init();
	}

	public FileListPanel(String title, String filetype) {
		this(title);
		this.filetype = filetype;
        init();
	}

	/**
	 * Constructor for the FilePanel object.
	 */
	public FileListPanel(ChangeListener l, String title) {
		this.title = title;
		init();
		listeners.add(l);
	}

	public void addChangeListener(ChangeListener l) {
		listeners.add(l);
	}

	private void init() {
        this.setLayout(new BorderLayout(0, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        JLabel jtitle = new JLabel(title);

        HorizontalPanel buttons = new HorizontalPanel();
        buttons.add(jtitle);
        buttons.add(browse);
        buttons.add(delete);
        buttons.add(clear);
        add(buttons,BorderLayout.NORTH);

        this.initializeTableModel();
        files = new JTable(tableModel);
        files.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        files.revalidate();

        JScrollPane scrollpane = new JScrollPane(files);
        scrollpane.setPreferredSize(new Dimension(400,140));
        add(scrollpane,BorderLayout.CENTER);

		browse.setActionCommand("browse");
		browse.addActionListener(this);
        clear.addActionListener(this);
        delete.addActionListener(this);
        this.setPreferredSize(new Dimension(400,230));
	}

	/**
	 * If the gui needs to enable/disable the FilePanel, call the method.
	 * 
	 * @param enable
	 */
	public void enableFile(boolean enable) {
		browse.setEnabled(enable);
        files.setEnabled(false);
	}

    /**
     * Add a single file to the table
     * @param f
     */
    public void addFilename(String f) {
        tableModel.addRow(f);
    }
    
    /**
     * clear the files from the table
     */
    public void clearFiles() {
        tableModel.clearData();
    }

    public void setFiles(String[] files) {
        this.clearFiles();
        for (int idx=0; idx < files.length; idx++) {
            addFilename(files[idx]);
        }
    }
    
    public String[] getFiles() {
        String[] files = new String[tableModel.getRowCount()];
        for (int idx=0; idx < files.length; idx++) {
            files[idx] = (String)tableModel.getValueAt(idx,0);
        }
        return files;
    }
    
    protected void deleteFile() {
        // If a table cell is being edited, we must cancel the editing before
        // deleting the row

        int rowSelected = files.getSelectedRow();
        if (rowSelected >= 0) {
            tableModel.removeRow(rowSelected);
			tableModel.fireTableDataChanged();

        }
    }
    
	private void fireFileChanged() {
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			((ChangeListener) iter.next()).stateChanged(new ChangeEvent(this));
		}
	}

    protected void initializeTableModel() {
        tableModel = new ObjectTableModel(new String[] { "Library" }, new Functor[0],
                new Functor[0],
                new Class[] { String.class });
    }
    
	public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clear) {
            this.clearFiles();
        } else if (e.getActionCommand().equals("browse")) {
			JFileChooser chooser = new JFileChooser();
            String start = JMeterUtils.getPropDefault("user.dir", "");
            chooser.setCurrentDirectory(new File(start));
            chooser.setFileFilter(new JMeterFileFilter(new String[] { filetype }));
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setMultiSelectionEnabled(true);
            chooser.showOpenDialog(GuiPackage.getInstance().getMainFrame());
            File[] cfiles = chooser.getSelectedFiles();
			if (chooser != null && cfiles != null) {
                for (int idx=0; idx < cfiles.length; idx++) {
                    this.addFilename(cfiles[idx].getPath());
                }
				fireFileChanged();
			}
        } else if (e.getSource() == delete) {
            this.deleteFile();
		} else {
			fireFileChanged();
		}
	}
}
