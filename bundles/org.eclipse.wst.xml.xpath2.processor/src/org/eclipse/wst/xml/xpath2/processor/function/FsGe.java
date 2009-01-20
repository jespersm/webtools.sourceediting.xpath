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
 * Class for the Greater than or equal to function.
 */
public class FsGe extends Function {
	/**
	 * Constructor for FsGe.
	 */
	public FsGe() {
		super(new QName("ge"), 2);
	}
	/**
         * Evaluate arguments.
         * @param args argument expressions.
         * @throws DynamicError Dynamic error.
         * @return Result of evaluation.
         */
	public ResultSequence evaluate(Collection args) throws DynamicError {
		assert args.size() == arity();

		return fs_ge_value(args);
	}

	/**
	 * Greater than or equal to operation on the values of the arguments.
	 * @param args input arguments.
	 * @throws DynamicError Dynamic error.
	 * @return Result of the operation.
	 */
	public static ResultSequence fs_ge_value(Collection args) throws DynamicError {
		ResultSequence greater = FsGt.fs_gt_value(args);

		if( ((XSBoolean)greater.first()).value())
			return greater;
	
		ResultSequence equal = FsEq.fs_eq_value(args);

		if( ((XSBoolean)equal.first()).value())
			return equal;

		return ResultSequenceFactory.create_new(new XSBoolean(false));
	}
	/**
	 * General greater than or equal to operation.
	 * @param args input arguments.
	 * @throws DynamicError Dynamic error.
	 * @return Result of the operation.
	 */
        public static ResultSequence fs_ge_general(Collection args) throws DynamicError {
                return FsEq.do_cmp_general_op(args, FsGe.class, "fs_ge_value");
        }
}