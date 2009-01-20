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


// mite wanna do two separate classes at the end
/**
 * Support for Quantified expressions.
 */
public class QuantifiedExpr extends Expr {
	/**
	 * Set internal value for SOME.
	 */
	public static final int SOME = 0;
	/**
	 * Set internal value for ALL.
	 */
	public static final int ALL = 1;

	private Collection _var_expr_pairs;
	private Expr _return;
	private int _type;
	/**
	 * Constructor for QuantifiedExpr.
	 * @param type Type (0 for SOME, 1 for ALL).
	 * @param varexp Expressions.
	 * @param ret Returned expression.
	 */
	public QuantifiedExpr(int type, Collection varexp, Expr ret) {
		_type = type;
		_var_expr_pairs = varexp;
		_return = ret;
	}
	/**
	 * Support for Visitor interface.
	 * @return Result of Visitor operation.
	 */
	public Object accept(XPathVisitor v) {
		return v.visit(this);
	}
	/**
	 * Support for Integer interface.
	 * @return Result of Int operation.
	 */
	public int type() { return _type; }
	/**
	 * Support for Iterator inteface.
	 * @return Result of Iterator operation.
	 */
	public Iterator iterator() { return _var_expr_pairs.iterator(); }
	/**
	 * Support for Expression interface.
	 * @return Result of Expr operation.
	 */
	public Expr expr() { return _return; }
	/**
	 * Set next expression.
	 * @param e Expression.
	 */
        public void set_expr(Expr e) { _return = e; }

        // used for normalization... basically just keep a "simple for"... no
        // pairs... collection will always have 1 element
	/**
	 * Normalization of expression pairs.
	 */
        public void truncate_pairs() {
                boolean first = true;

                for(Iterator i = _var_expr_pairs.iterator(); i.hasNext();) {
                        i.next();
                        if(!first)
                                i.remove();

                        first = false;
                }
        }
	/**
	 * Support for Collection interface.
	 * @return Expression pairs.
	 */
	public Collection ve_pairs() { return _var_expr_pairs; }
}