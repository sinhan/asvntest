package org.apache.jmeter.protocol.java.control.gui;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane; 
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.jmeter.protocol.java.sampler.BSFSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

/**
 * @author    sebb AT apache DOT org 
 * @version   $Revision: 323696 $ $Date: 2003-11-16 16:52:14 -0800 (Sun, 16 Nov 2003) $
 */
public class BSFSamplerGui extends AbstractSamplerGui
{
	private JTextArea scriptField;
	private JTextField langField;// Language TODO should this be a drop-down list?
	private JTextField filename;// script file name (if present)
	private JTextField parameters;// parameters to pass to script file (or script)

    public BSFSamplerGui()
    {
        init();
    }

    public void configure(TestElement element)
    {
		super.configure(element);
    	scriptField.setText(element.getProperty(BSFSampler.SCRIPT).toString());
		langField.setText(element.getProperty(BSFSampler.LANGUAGE).toString());
    }

    public TestElement createTestElement()
    {
        BSFSampler sampler = new BSFSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement te)
    {
        te.clear();
        this.configureTestElement(te);
		te.setProperty(BSFSampler.FILENAME,filename.getText());
		te.setProperty(BSFSampler.LANGUAGE,langField.getText());
		te.setProperty(BSFSampler.PARAMETERS,parameters.getText());
		te.setProperty(BSFSampler.SCRIPT, scriptField.getText());
    }

    public String getStaticLabel()
    {
        return JMeterUtils.getResString("bsf_sampler_title") + " (ALPHA CODE)";
    }

    private void init()
    { 
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

		Box box = Box.createVerticalBox();
		box.add(makeTitlePanel());
		box.add(createLanguagePanel());
		box.add(createFilenamePanel());
		box.add(createParameterPanel());
		add(box,BorderLayout.NORTH);

		JPanel panel = createScriptPanel();
		add(panel, BorderLayout.CENTER);
		// Don't let the input field shrink too much
		add(
			Box.createVerticalStrut(panel.getPreferredSize().height),
			BorderLayout.WEST);
    }

	private JPanel createParameterPanel()
	{
		JLabel label = new JLabel(JMeterUtils.getResString("bsf_script_parameters"));

		parameters = new JTextField(10);
		parameters.setName(BSFSampler.PARAMETERS);
		label.setLabelFor(parameters);

		JPanel parameterPanel = new JPanel(new BorderLayout(5,0));
		parameterPanel.add(label,BorderLayout.WEST);
		parameterPanel.add(parameters, BorderLayout.CENTER);
		return parameterPanel;
	}


	private JPanel createFilenamePanel()//TODO ought to be a FileChooser ...
	{
		JLabel label = new JLabel(JMeterUtils.getResString("bsf_script_file"));
		
		filename = new JTextField(10);
		filename.setName(BSFSampler.FILENAME);
		label.setLabelFor(filename);

		JPanel filenamePanel = new JPanel(new BorderLayout(5, 0));
		filenamePanel.add(label, BorderLayout.WEST);
		filenamePanel.add(filename, BorderLayout.CENTER);
		return filenamePanel;
	}

	private JPanel createLanguagePanel()
	{
		JLabel label = new JLabel(JMeterUtils.getResString("bsf_script_language"));

		langField = new JTextField(10);
		langField.setName(BSFSampler.LANGUAGE);
		label.setLabelFor(langField);

		JPanel langPanel = new JPanel(new BorderLayout(5,0));
		langPanel.add(label,BorderLayout.WEST);
		langPanel.add(langField, BorderLayout.CENTER);
		return langPanel;
	}



	private JPanel createScriptPanel()
	{
		scriptField = new JTextArea();
		scriptField.setRows(4);
		scriptField.setLineWrap(true);
		scriptField.setWrapStyleWord(true);

		JLabel label = new JLabel(JMeterUtils.getResString("bsf_script"));
		label.setLabelFor(scriptField);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(label, BorderLayout.NORTH);
		panel.add(new JScrollPane(scriptField), BorderLayout.CENTER);
		return panel;
	}
}
