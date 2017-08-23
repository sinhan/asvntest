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

package org.apache.jmeter.timers.gui;

import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.timers.ConstantTimer;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.layout.VerticalLayout;

/**
 * The GUI for ConstantTimer. 
 *
 * @author Michael Stover
 * @author <a href="mailto:seade@backstagetech.com.au">Scott Eade</a>
 * @version $Id: ConstantTimerGui.java 323094 2003-02-28 14:21:52Z mstover1 $
 */
public class ConstantTimerGui extends AbstractTimerGui 
{
	/**
	 * The default value for the delay.
	 */
	private final String DEFAULT_DELAY = "300";

	private final String DELAY_FIELD = "Delay Field";

	private JTextField delayField;

	/**
	 * No-arg constructor.
	 */
	public ConstantTimerGui()
	{
		init();
	}

	/**
	 * Handle an error.
	 *
	 * @param e the Exception that was thrown.
	 * @param thrower the JComponent that threw the Exception.
	 */
	public static void error(Exception e, JComponent thrower)
	{
		JOptionPane.showMessageDialog(thrower, e, "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Get the title to display for this component.
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#getStaticLabel()
	 */
	public String getStaticLabel()
	{
		return JMeterUtils.getResString("constant_timer_title");
	}

	/**
	 * Create the test element underlying this GUI component.
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
	 */
	public TestElement createTestElement()
	{
		ConstantTimer timer = new ConstantTimer();
		modifyTestElement(timer);
		return timer;
	}

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement timer)
    {
        this.configureTestElement(timer);
        ((ConstantTimer)timer).setDelay(delayField.getText());
    }

	/**
	 * Configure this GUI component from the underlying TestElement.
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#configure(TestElement)
	 */
	public void configure(TestElement el)
	{
		super.configure(el);
		delayField.setText(((ConstantTimer)el).getDelay());
	}

	/**
	 * Initialize this component.
	 */
	private void init()
	{
		this.setLayout(new VerticalLayout(5, VerticalLayout.LEFT, VerticalLayout.TOP));

		// MAIN PANEL
		JPanel mainPanel = new JPanel();
		Border margin = new EmptyBorder(10, 10, 5, 10);
		mainPanel.setBorder(margin);
		mainPanel.setLayout(new VerticalLayout(5, VerticalLayout.LEFT));

		// TITLE
		JLabel panelTitleLabel = new JLabel(JMeterUtils.getResString("constant_timer_title"));
		Font curFont = panelTitleLabel.getFont();
		int curFontSize = curFont.getSize();
		curFontSize += 4;
		panelTitleLabel.setFont(new Font(curFont.getFontName(), curFont.getStyle(), curFontSize));
		mainPanel.add(panelTitleLabel);

		// NAME
		mainPanel.add(getNamePanel());

		// DELAY
		JPanel delayPanel = new JPanel();
		JLabel delayLabel = new JLabel(JMeterUtils.getResString("constant_timer_delay"));
		delayPanel.add(delayLabel);
		delayField = new JTextField(20);
		delayField.setText(DEFAULT_DELAY);
		delayPanel.add(delayField);
		mainPanel.add(delayPanel);
		delayField.setName(DELAY_FIELD);

		this.add(mainPanel);
	}
	
}
