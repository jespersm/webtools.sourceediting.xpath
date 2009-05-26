/*******************************************************************************
 * Copyright (c) 2005, 2009 Andrea Bittau, University College London, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Andrea Bittau - initial API and implementation from the PsychoPath XPath 2.0
 *     Mukul Gandhi - bug 274805 - improvements to xs:integer data type 
 *******************************************************************************/

package org.eclipse.wst.xml.xpath2.processor.internal.function;

import org.eclipse.wst.xml.xpath2.processor.DynamicError;
import org.eclipse.wst.xml.xpath2.processor.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.ResultSequenceFactory;
import org.eclipse.wst.xml.xpath2.processor.internal.*;
import org.eclipse.wst.xml.xpath2.processor.internal.types.*;

import java.math.BigInteger;
import java.util.*;

/**
 * Support for To operation.
 */
public class OpTo extends Function {
	private static Collection _expected_args = null;

	/**
	 * Constructor for OpTo.
	 */
	public OpTo() {
		super(new QName("to"), 2);
	}

	/**
	 * Evaluate arguments.
	 * 
	 * @param args
	 *            argument expressions.
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of evaluation.
	 */
	@Override
	public ResultSequence evaluate(Collection args) throws DynamicError {
		assert args.size() == 2;

		// Iterator i = args.iterator();

		// return op_to( (ResultSequence) i.next(), (ResultSequence) i.next());
		return op_to(args);
	}

	/**
	 * Op-To operation.
	 * 
	 * @param args
	 *            Result from the expressions evaluation.
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of operation.
	 */
	public static ResultSequence op_to(Collection args) throws DynamicError {
		ResultSequence rs = ResultSequenceFactory.create_new();

		// convert arguments
		Collection cargs = Function.convert_arguments(args, expected_args());

		// get arguments
		Iterator iter = cargs.iterator();
		ResultSequence r = (ResultSequence) iter.next();
		int one = ((XSInteger) r.first()).int_value().intValue();
		r = (ResultSequence) iter.next();
		int two = ((XSInteger) r.first()).int_value().intValue();

		if (one > two)
			return rs;

		// inclusive first and last
		rs.add(new XSInteger(BigInteger.valueOf(one)));

		if (one == two) {
			return rs;
		}
		/*
		 * for(one++; one <= two; one++) { rs.add(new XSInteger(one)); }
		 * 
		 * return rs;
		 */
		return new RangeResultSequence(one, two);
	}

	/**
	 * Obtain a list of expected arguments.
	 * 
	 * @return Result of operation.
	 */
	public static Collection expected_args() {
		if (_expected_args == null) {
			_expected_args = new ArrayList();

			SeqType st = new SeqType(new XSInteger());

			_expected_args.add(st);
			_expected_args.add(st);
		}
		return _expected_args;
	}
}