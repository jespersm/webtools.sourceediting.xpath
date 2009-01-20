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

package org.eclipse.wst.xml.xpath2.processor.function;

import org.eclipse.wst.xml.xpath2.processor.*;
import org.eclipse.wst.xml.xpath2.processor.types.*;

import java.util.*;
/**
 * Returns an xs:integer indicating the position of the context item within the sequence
 * of items currently being processed. If the context item is undefined, an error is
 * raised [err:FONC0001].
 */
public class FnPosition extends Function {
	/**
	 * Constructor for FnPosition.
	 */	
	public FnPosition() {
		super(new QName("position"), 0);
	}
	/**
         * Evaluate arguments.
         * @param args argument expressions.
         * @throws DynamicError Dynamic error.
         * @return Result of evaluation.
         */
	public ResultSequence evaluate(Collection args) throws DynamicError {
		return position(args, dynamic_context());
	}
	/**
         * Position operation.
         * @param args Result from the expressions evaluation.
	 * @param dc Result of dynamic context operation.
         * @throws DynamicError Dynamic error.
         * @return Result of fn:position operation.
         */
	public static ResultSequence position(Collection args, DynamicContext dc) throws DynamicError {
		assert args.size() == 0;


		return ResultSequenceFactory.create_new(new XSInteger(dc.context_position()));
	}
}