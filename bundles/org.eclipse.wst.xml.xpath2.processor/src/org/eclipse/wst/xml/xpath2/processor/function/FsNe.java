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
 * Class for Not equal function.
 */
public class FsNe extends Function {
	/**
	 * Constructor for FsNe.
	 */
	public FsNe() {
		super(new QName("ne"), 2);
	}
	/**
         * Evaluate arguments.
         * @param args argument expressions.
         * @throws DynamicError Dynamic error.
         * @return Result of evaluation.
         */
	public ResultSequence evaluate(Collection args) throws DynamicError {
		assert args.size() == arity();

		return fs_ne_value(args);
	}

	/**
         * Operation on the values of the arguments.
         * @param args input arguments.
         * @throws DynamicError Dynamic error.
         * @return Result of the operation.
         */
	public static ResultSequence fs_ne_value(Collection args) throws DynamicError {
		return FnNot.fn_not(FsEq.fs_eq_value(args));
	}
	/**
         * General operation on the arguments.
         * @param args input arguments.
         * @throws DynamicError Dynamic error.
         * @return Result of the operation.
         */
        public static ResultSequence fs_ne_general(Collection args) throws DynamicError {
                return FsEq.do_cmp_general_op(args, FsNe.class, "fs_ne_value");
        }

}