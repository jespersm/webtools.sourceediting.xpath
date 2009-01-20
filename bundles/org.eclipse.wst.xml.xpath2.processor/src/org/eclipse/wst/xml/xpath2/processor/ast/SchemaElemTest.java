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

package org.eclipse.wst.xml.xpath2.processor.ast;

import org.eclipse.wst.xml.xpath2.processor.types.*;
/**
 * Support for Schema Element Test.
 */
public class SchemaElemTest extends KindTest {
	private QName _arg;
	/**
	 * Constructor for SchemaElemTest.
	 * @param arg QName argument.
	 */
	public SchemaElemTest(QName arg) {
		_arg = arg;
	}
	/**
	 * Support for Visitor interface.
	 * @return Result of Visitor operation.
	 */
	public Object accept(XPathVisitor v) {
		return v.visit(this);
	}
	/**
	 * Support for QName interface.
 	 * @return Result of QName operation.
	 */
	public QName name() { return _arg; }

}