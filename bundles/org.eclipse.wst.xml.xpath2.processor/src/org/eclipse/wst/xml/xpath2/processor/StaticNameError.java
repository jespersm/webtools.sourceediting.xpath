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
 * Error caused by static name.
 */
public class StaticNameError extends StaticError {
	public static final String NAME_NOT_FOUND = "XP0008";

	/**
	 * Constructor for static name error
	 * @param code is the code.
	 * @param reason is the reason for the error.
 	 */
	public StaticNameError(String code, String reason) {
		super(code,reason);
	}

	/**
	 * Constructor for static name error
	 * @param reason is the reason for the error.
 	 */
	public StaticNameError(String reason) {
		this(NAME_NOT_FOUND,reason);
	}
	
}