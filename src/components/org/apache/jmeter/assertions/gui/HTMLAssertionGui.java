// $Header$
/*
 * Copyright 2003-2004 The Apache Software Foundation.
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

package org.apache.jmeter.assertions.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.jmeter.assertions.HTMLAssertion;
import org.apache.jmeter.gui.util.FilePanel;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * GUI for HTMLAssertion
 */
public class HTMLAssertionGui extends AbstractAssertionGui implements FocusListener, ActionListener, ChangeListener {

  //instance attributes
  private JTextField errorThresholdField = null;
  private JTextField warningThresholdField = null;
  private JCheckBox errorsOnly = null;
  private JComboBox docTypeBox = null;
  private JRadioButton htmlRadioButton = null;
  private JRadioButton xhtmlRadioButton = null;
  private JRadioButton xmlRadioButton = null;
  private FilePanel filePanel = null;

  //class attributes
  transient private static Logger log = LoggingManager.getLoggerForClass();

  /**
   * The constructor.
   */
  public HTMLAssertionGui() {
    init();
  }

  /**
   * Returns the label to be shown within the JTree-Component.
   */
  public String getLabelResource() {
    return "html_assertion_title";
  }

  /**
   * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
   */
  public TestElement createTestElement() {
    HTMLAssertion el = new HTMLAssertion();
    modifyTestElement(el);
    return el;
  }

  /**
   * Modifies a given TestElement to mirror the data in the gui components.
   *
   * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
   */
  public void modifyTestElement(TestElement inElement) {

    log.debug("HTMLAssertionGui.modifyTestElement() called");

    configureTestElement(inElement);

    String errorThresholdString = errorThresholdField.getText();
    long errorThreshold = 0;

    try {
      errorThreshold = Long.parseLong(errorThresholdString);
    } catch (NumberFormatException e) {
      errorThreshold = 0;
    }
    ((HTMLAssertion) inElement).setErrorThreshold(errorThreshold);

    String warningThresholdString = warningThresholdField.getText();
    long warningThreshold = 0;
    try {
      warningThreshold = Long.parseLong(warningThresholdString);
    } catch (NumberFormatException e) {
      warningThreshold = 0;
    }
    ((HTMLAssertion) inElement).setWarningThreshold(warningThreshold);

    String docTypeString = docTypeBox.getSelectedItem().toString();
    ((HTMLAssertion) inElement).setDoctype(docTypeString);

    boolean trackErrorsOnly = errorsOnly.isSelected();
    ((HTMLAssertion) inElement).setErrorsOnly(trackErrorsOnly);

    if (htmlRadioButton.isSelected()) {
      ((HTMLAssertion) inElement).setHTML();
    } else if (xhtmlRadioButton.isSelected()) {
      ((HTMLAssertion) inElement).setXHTML();
    } else {
      ((HTMLAssertion) inElement).setXML();
    }
    ((HTMLAssertion) inElement).setFilename(filePanel.getFilename());
  }

  /**
   * Configures the associated test element.
   * @param inElement
   */
  public void configure(TestElement inElement) {
    super.configure(inElement);
    HTMLAssertion lAssertion = (HTMLAssertion) inElement;
    errorThresholdField.setText(String.valueOf(lAssertion.getErrorThreshold()));
    warningThresholdField.setText(String.valueOf(lAssertion.getWarningThreshold()));
    errorsOnly.setSelected(lAssertion.isErrorsOnly());
    docTypeBox.setSelectedItem(lAssertion.getDoctype());
    if (lAssertion.isHTML()) {
      htmlRadioButton.setSelected(true);
    } else if (lAssertion.isXHTML()) {
      xhtmlRadioButton.setSelected(true);
    } else {
      xmlRadioButton.setSelected(true);
    }
    if (lAssertion.isErrorsOnly()) {
      warningThresholdField.setEnabled(false);
      warningThresholdField.setEditable(false);
    }
    filePanel.setFilename(lAssertion.getFilename());
  }

  /**
   * Inits the GUI.
   */
  private void init() {

    setLayout(new BorderLayout(0, 10));
    setBorder(makeBorder());

    add(makeTitlePanel(), BorderLayout.NORTH);

    JPanel mainPanel = new JPanel(new BorderLayout());

    // USER_INPUT
    VerticalPanel assertionPanel = new VerticalPanel();
    assertionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Tidy Settings"));

    //doctype
    HorizontalPanel docTypePanel = new HorizontalPanel();
    docTypeBox = new JComboBox(new Object[] { "omit", "auto", "strict", "loose" });
    docTypeBox.addFocusListener(this);
    //docTypePanel.add(new JLabel(JMeterUtils.getResString("duration_assertion_label")));
    docTypePanel.add(new JLabel("Doctype:"));
    docTypePanel.add(docTypeBox);
    assertionPanel.add(docTypePanel);

    //format (HMTL, XHTML, XML)
    VerticalPanel formatPanel = new VerticalPanel();
    formatPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Format"));
    htmlRadioButton = new JRadioButton("HTML", true);
    xhtmlRadioButton = new JRadioButton("XHTML", false);
    xmlRadioButton = new JRadioButton("XML", false);
    ButtonGroup buttonGroup = new ButtonGroup();
    buttonGroup.add(htmlRadioButton);
    buttonGroup.add(xhtmlRadioButton);
    buttonGroup.add(xmlRadioButton);
    formatPanel.add(htmlRadioButton);
    formatPanel.add(xhtmlRadioButton);
    formatPanel.add(xmlRadioButton);
    assertionPanel.add(formatPanel);

    //errors only
    errorsOnly = new JCheckBox("Errors only", false);
    errorsOnly.addFocusListener(this);
    errorsOnly.addActionListener(this);
    assertionPanel.add(errorsOnly);

    //thresholds
    HorizontalPanel thresholdPanel = new HorizontalPanel();
    thresholdPanel.add(new JLabel("Error threshold:"));
    errorThresholdField = new JTextField("0", 5);
    errorThresholdField.addFocusListener(this);
    thresholdPanel.add(errorThresholdField);
    thresholdPanel.add(new JLabel("Warning threshold:"));
    warningThresholdField = new JTextField("0", 5);
    warningThresholdField.addFocusListener(this);
    thresholdPanel.add(warningThresholdField);
    assertionPanel.add(thresholdPanel);

    //file panel
    filePanel = new FilePanel(JMeterUtils.getResString("file_visualizer_output_file"), ".txt");
    filePanel.addChangeListener(this);
    assertionPanel.add(filePanel);

    mainPanel.add(assertionPanel, BorderLayout.NORTH);
    add(mainPanel, BorderLayout.CENTER);
  }

  /**
   * This method is called if one of the threshold field looses the focus
   * @param inEvent
   */
  public void focusLost(FocusEvent inEvent) {
    log.debug("HTMLAssertionGui.focusLost() called");

    String errorThresholdString = errorThresholdField.getText();
    if (errorThresholdString != null) {
      boolean isInvalid = false;
      try {
        long errorThreshold = Long.parseLong(errorThresholdString);
        if (errorThreshold < 0) {
          isInvalid = true;
        }
      } catch (NumberFormatException ex) {
        isInvalid = true;
      }
      if (isInvalid) {
        log.warn("HTMLAssertionGui: Error threshold Not a valid number!");
        JOptionPane.showMessageDialog(null, "Threshold for errors is invalid", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }

    String warningThresholdString = warningThresholdField.getText();
    if (warningThresholdString != null) {
      boolean isInvalid = false;
      try {
        long warningThreshold = Long.parseLong(warningThresholdString);
        if (warningThreshold < 0) {
          isInvalid = true;
        }
      } catch (NumberFormatException ex) {
        isInvalid = true;
      }
      if (isInvalid) {
        log.warn("HTMLAssertionGui: Error threshold Not a valid number!");
        JOptionPane.showMessageDialog(null, "Threshold for warnings is invalid", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /**
   * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
   */
  public void focusGained(FocusEvent e) {

  }

  /**
   * This method is called from erros-only checkbox
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e) {
    if (errorsOnly.isSelected()) {
      warningThresholdField.setEnabled(false);
      warningThresholdField.setEditable(false);
    } else {
      warningThresholdField.setEnabled(true);
      warningThresholdField.setEditable(true);
    }
  }

  /** This method is called on change of output file input
   * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
   */
  public void stateChanged(ChangeEvent e) {
    log.debug("HTMLAssertionGui.stateChanged() called");
  }

}
