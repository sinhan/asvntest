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
package org.apache.jmeter.config;

import java.io.Serializable;

/************************************************************
 *  Title: JMeter Description: Copyright: Copyright (c) 2000 Company: Apache
 *
 *@author     Michael Stover
 *@created    March 13, 2001
 *@version    1.0
 ***********************************************************/

public class LoginConfig extends ConfigTestElement implements Serializable
{

	/************************************************************
	 *  Constructor for the LoginConfig object
	 ***********************************************************/
	public LoginConfig()
	{
	}

	/************************************************************
	 *  Sets the Username attribute of the LoginConfig object
	 *
	 *@param  username  The new Username value
	 ***********************************************************/
	public void setUsername(String username)
	{
		setProperty(ConfigTestElement.USERNAME, username);
	}

	/************************************************************
	 *  Sets the Password attribute of the LoginConfig object
	 *
	 *@param  password  The new Password value
	 ***********************************************************/
	public void setPassword(String password)
	{
		setProperty(ConfigTestElement.PASSWORD, password);
	}


	/************************************************************
	 *  Gets the Username attribute of the LoginConfig object
	 *
	 *@return    The Username value
	 ***********************************************************/
	public String getUsername()
	{
		return (String)this.getProperty(ConfigTestElement.USERNAME);
	}

	/************************************************************
	 *  Gets the Password attribute of the LoginConfig object
	 *
	 *@return    The Password value
	 ***********************************************************/
	public String getPassword()
	{
		return (String)this.getProperty(ConfigTestElement.PASSWORD);
	}

}