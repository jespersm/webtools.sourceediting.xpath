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

import java.util.*;
/**
 * Class for Or operation.
 */
public class OrExpr extends BinExpr {
	/**
	 * Constructor for OrExpr.
	 * @param l left expression.
 	 * @param r right expression.
	 */
	public OrExpr(Expr l, Expr r) {
		super(l,r);
	}
	/**
	 * Support for Visitor interface.
	 * @return Resulf of Visitor operation.
 	 */
	public Object accept(XPathVisitor v) {
		return v.visit(this);
	}
}