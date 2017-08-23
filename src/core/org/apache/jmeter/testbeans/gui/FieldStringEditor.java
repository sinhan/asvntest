/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
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
 * 
 * @author <a href="mailto:jsalvata@apache.org">Jordi Salvat i Alabart</a>
 * @version $Id: FieldStringEditor.java 324050 2004-01-14 12:53:28Z jsalvata $
 */
package org.apache.jmeter.testbeans.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyEditorSupport;

import javax.swing.JTextField;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * This class implements a property editor for non-null String properties
 * that supports custom editing (i.e.: provides a GUI component) based
 * on a text field.
 * <p>
 * The provided GUI is a simple text field.
 */
class FieldStringEditor extends PropertyEditorSupport
	implements ActionListener, FocusListener
{
	protected static Logger log= LoggingManager.getLoggerForClass();

	/**
	 * This will hold the text editing component, either a plain JTextField
	 * (in cases where the combo box would not have other options
	 * than 'Edit'), or the text editing component in the combo box.  
	 */
	private JTextField textField;

    /**
     * True iif we're currently processing an event triggered by the user
     * selecting or entering a new value. Used to prevent touching at
     * GUI states outside of the event handling method -- otherwise it's
     * a mess.
     */
    private boolean processingItemEvent= false;

    protected FieldStringEditor()
    {
    	super();

		textField= new JTextField();
		textField.addActionListener(this);
		textField.addFocusListener(this);
    }

	public String getAsText()
	{
		return textField.getText();
	}
	
	public void setAsText(String value)
	{
		textField.setText(value);
	}
	
	public Object getValue()
	{
		return getAsText();
	}
	
	public void setValue(Object value)
	{
		if (value instanceof String) setAsText((String)value);
		else throw new IllegalArgumentException();
	}
	
    /* (non-Javadoc)
     * @see java.beans.PropertyEditor#getCustomEditor()
     */
    public Component getCustomEditor()
    {
		return textField;
    }

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		firePropertyChange();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	public void focusGained(FocusEvent e)
	{
	}

	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	public void focusLost(FocusEvent e)
	{
		firePropertyChange();
	}
	
	public static class Test extends junit.framework.TestCase
	{
		public Test(String name)
		{
			super(name);
		}

		private void testSetGet(ComboStringEditor e, Object value) throws Exception
		{
			e.setValue(value);
			assertEquals(value, e.getValue());
		}
		private void testSetGetAsText(ComboStringEditor e, String text) throws Exception
		{
			e.setAsText(text);
			assertEquals(text, e.getAsText());
		}
		public void testSetGet() throws Exception
		{
			ComboStringEditor e= new ComboStringEditor();
				
			testSetGet(e, "any string");
			testSetGet(e, "");
			testSetGet(e, "${var}");
		}
		public void testSetGetAsText() throws Exception
		{
			ComboStringEditor e= new ComboStringEditor();
				
			testSetGetAsText(e, "any string");
			testSetGetAsText(e, "");
			testSetGetAsText(e, "${var}");
		}
	}
}