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

package org.apache.jmeter.protocol.http.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.gui.util.FileDialoger;
import org.apache.jmeter.protocol.http.control.AuthManager;
import org.apache.jmeter.protocol.http.control.Authorization;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * Handles input for determining if authentication services are required for a
 * Sampler. It also understands how to get AuthManagers for the files that the
 * user selects.
 * 
 * @version $Revision: 325542 $ Last updated: $Date: 2005-07-12 13:51:09 -0700 (Tue, 12 Jul 2005) $
 */
public class AuthPanel extends AbstractConfigGui implements ActionListener {
	transient private static Logger log = LoggingManager.getLoggerForClass();

	private static final String ADD_COMMAND = "Add";

	private static final String DELETE_COMMAND = "Delete";

	private static final String LOAD_COMMAND = "Load";

	private static final String SAVE_COMMAND = "Save";

	private InnerTableModel tableModel;

	/**
	 * A table to show the authentication information.
	 */
	private JTable authTable;

	private JButton addButton;

	private JButton deleteButton;

	private JButton loadButton;

	private JButton saveButton;

	/**
	 * Default Constructor.
	 */
	public AuthPanel() {
		tableModel = new InnerTableModel();
		init();
	}

	public TestElement createTestElement() {
		AuthManager authMan = tableModel.manager;
		configureTestElement(authMan);
		return (TestElement) authMan.clone();
	}

	/**
	 * Modifies a given TestElement to mirror the data in the gui components.
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
	 */
	public void modifyTestElement(TestElement el) {
		if (authTable.isEditing()) {
			authTable.getCellEditor().stopCellEditing();
		}
		el.clear();
		el.addTestElement((TestElement) tableModel.manager.clone());
		configureTestElement(el);
	}

	public void configure(TestElement el) {
		super.configure(el);
		tableModel.manager.clear();
		tableModel.manager.addTestElement((AuthManager) el.clone());
	}

	public String getLabelResource() {
		return "auth_manager_title";
	}

	/**
	 * Shows the main authentication panel for this object.
	 */
	public void init() {
		setLayout(new BorderLayout());
		setBorder(makeBorder());

		add(makeTitlePanel(), BorderLayout.NORTH);
		add(createAuthTablePanel(), BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		if (action.equals(DELETE_COMMAND)) {
			if (tableModel.getRowCount() > 0) {
				// If a table cell is being edited, we must cancel the editing
				// before deleting the row.
				if (authTable.isEditing()) {
					TableCellEditor cellEditor = authTable.getCellEditor(authTable.getEditingRow(), authTable
							.getEditingColumn());
					cellEditor.cancelCellEditing();
				}

				int rowSelected = authTable.getSelectedRow();

				if (rowSelected != -1) {
					tableModel.removeRow(rowSelected);
					tableModel.fireTableDataChanged();

					// Disable the DELETE and SAVE buttons if no rows remaining
					// after delete.
					if (tableModel.getRowCount() == 0) {
						deleteButton.setEnabled(false);
						saveButton.setEnabled(false);
					}

					// Table still contains one or more rows, so highlight
					// (select) the appropriate one.
					else {
						int rowToSelect = rowSelected;

						if (rowSelected >= tableModel.getRowCount()) {
							rowToSelect = rowSelected - 1;
						}

						authTable.setRowSelectionInterval(rowToSelect, rowToSelect);
					}
				}
			}
		} else if (action.equals(ADD_COMMAND)) {
			// If a table cell is being edited, we should accept the current
			// value and stop the editing before adding a new row.
			if (authTable.isEditing()) {
				TableCellEditor cellEditor = authTable.getCellEditor(authTable.getEditingRow(), authTable
						.getEditingColumn());
				cellEditor.stopCellEditing();
			}

			tableModel.addNewRow();
			tableModel.fireTableDataChanged();

			// Enable the DELETE and SAVE buttons if they are currently
			// disabled.
			if (!deleteButton.isEnabled()) {
				deleteButton.setEnabled(true);
			}
			if (!saveButton.isEnabled()) {
				saveButton.setEnabled(true);
			}

			// Highlight (select) the appropriate row.
			int rowToSelect = tableModel.getRowCount() - 1;
			authTable.setRowSelectionInterval(rowToSelect, rowToSelect);
		} else if (action.equals(LOAD_COMMAND)) {
			try {
				File tmp = FileDialoger.promptToOpenFile().getSelectedFile();
				if (tmp != null) {
					tableModel.manager.addFile(tmp.getAbsolutePath());
					tableModel.fireTableDataChanged();

					if (tableModel.getRowCount() > 0) {
						deleteButton.setEnabled(true);
						saveButton.setEnabled(true);
					}
				}
			} catch (IOException ex) {
				log.error("", ex);
			} catch (NullPointerException err) {
			}
		} else if (action.equals(SAVE_COMMAND)) {
			try {
				File tmp = FileDialoger.promptToSaveFile(null).getSelectedFile();
				if (tmp != null) {
					tableModel.manager.save(tmp.getAbsolutePath());
				}
			} catch (IOException ex) {
				log.error("", ex);
			} catch (NullPointerException err) {
			}
		}
	}

	public JPanel createAuthTablePanel() {
		// create the JTable that holds auth per row
		authTable = new JTable(tableModel);
		authTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		authTable.setPreferredScrollableViewportSize(new Dimension(100, 70));

		TableColumn passwordColumn = authTable.getColumnModel().getColumn(2);
		passwordColumn.setCellEditor(new DefaultCellEditor(new JPasswordField()));
		passwordColumn.setCellRenderer(new PasswordCellRenderer());

		JPanel panel = new JPanel(new BorderLayout(0, 5));
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), JMeterUtils
				.getResString("auths_stored")));
		panel.add(new JScrollPane(authTable));
		panel.add(createButtonPanel(), BorderLayout.SOUTH);
		return panel;
	}

	private JButton createButton(String resName, char mnemonic, String command, boolean enabled) {
		JButton button = new JButton(JMeterUtils.getResString(resName));
		button.setMnemonic(mnemonic);
		button.setActionCommand(command);
		button.setEnabled(enabled);
		button.addActionListener(this);
		return button;
	}

	private JPanel createButtonPanel() {
		boolean tableEmpty = (tableModel.getRowCount() == 0);

		addButton = createButton("add", 'A', ADD_COMMAND, true);
		deleteButton = createButton("delete", 'D', DELETE_COMMAND, !tableEmpty);
		loadButton = createButton("load", 'L', LOAD_COMMAND, true);
		saveButton = createButton("save", 'S', SAVE_COMMAND, !tableEmpty);

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(loadButton);
		buttonPanel.add(saveButton);
		return buttonPanel;
	}

	/**
	 * @version $Revision: 325542 $
	 */
	private class InnerTableModel extends AbstractTableModel {
		AuthManager manager;

		public InnerTableModel(AuthManager man) {
			manager = man;
		}

		public InnerTableModel() {
			manager = new AuthManager();
		}

		public void removeRow(int row) {
			manager.remove(row);
		}

		public void addNewRow() {
			manager.addAuth();
		}

		public boolean isCellEditable(int row, int column) {
			// all table cells are editable
			return true;
		}

		public Class getColumnClass(int column) {
			return getValueAt(0, column).getClass();
		}

		/**
		 * Required by table model interface.
		 */
		public int getRowCount() {
			return manager.getAuthObjects().size();
		}

		/**
		 * Required by table model interface.
		 */
		public int getColumnCount() {
			return manager.getColumnCount();
		}

		/**
		 * Required by table model interface.
		 */
		public String getColumnName(int column) {
			return manager.getColumnName(column);
		}

		/**
		 * Required by table model interface.
		 */
		public Object getValueAt(int row, int column) {
			Authorization auth = manager.getAuthObjectAt(row);

			if (column == 0) {
				return auth.getURL();
			} else if (column == 1) {
				return auth.getUser();
			} else if (column == 2) {
				return auth.getPass();
			}
			return null;
		}

		public void setValueAt(Object value, int row, int column) {
			Authorization auth = manager.getAuthObjectAt(row);
			log.debug("Setting auth value: " + value);
			if (column == 0) {
				auth.setURL((String) value);
			} else if (column == 1) {
				auth.setUser((String) value);
			} else if (column == 2) {
				auth.setPass((String) value);
			}
		}
	}

	/**
	 * @version $Revision: 325542 $
	 */
	private class PasswordCellRenderer extends JPasswordField implements TableCellRenderer {
		private Border myBorder;

		public PasswordCellRenderer() {
			super();
			myBorder = new EmptyBorder(1, 2, 1, 2);
			setOpaque(true);
			setBorder(myBorder);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			setText((String) value);

			setBackground(isSelected && !hasFocus ? table.getSelectionBackground() : table.getBackground());
			setForeground(isSelected && !hasFocus ? table.getSelectionForeground() : table.getForeground());

			setFont(table.getFont());

			return this;
		}
	}
}
