/*******************************************************************************
 * Copyright (c) 2005, 2009 Andrea Bittau and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Andrea Bittau - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.xml.xpath2.processor;

/**
 * This exception is thrown if there is a problem with the XPath parser.
 */
public class XPathParserException extends StaticError {

	/**
	 * The type of exception.
	 */
	public static final String INVALID_XPATH_EXPRESSION = "XP0003";

	/**
	 * Constructor for XPathParserException.
	 * @param reason is the reason why the exception has been thrown. 
	 */
	public XPathParserException(String reason) {
		super(INVALID_XPATH_EXPRESSION,reason);
	}
}