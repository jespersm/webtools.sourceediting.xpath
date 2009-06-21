/*******************************************************************************
 * Copyright (c) 2005, 2009 Andrea Bittau, University College London, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Andrea Bittau - initial API and implementation from the PsychoPath XPath 2.0
 *     David Carver (STAR) - bug 262765 - added exception handling to toss correct error numbers. 
 *******************************************************************************/

package org.eclipse.wst.xml.xpath2.processor.internal.function;

import org.eclipse.wst.xml.xpath2.processor.DynamicError;
import org.eclipse.wst.xml.xpath2.processor.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.ResultSequenceFactory;
import org.eclipse.wst.xml.xpath2.processor.internal.*;
import org.eclipse.wst.xml.xpath2.processor.internal.types.*;

import java.util.*;
import java.util.regex.*;

/**
 * The function returns the xs:string that is obtained by replacing each
 * non-overlapping substring of $input that matches the given $pattern with an
 * occurrence of the $replacement string.
 */
public class FnReplace extends Function {
	private static Collection _expected_args = null;

	/**
	 * Constructor for RnReplace.
	 */
	public FnReplace() {
		super(new QName("replace"), -3);
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
		return replace(args);
	}

	/**
	 * Replace operation.
	 * 
	 * @param args
	 *            Result from the expressions evaluation.
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of fn:replace operation.
	 */
	public static ResultSequence replace(Collection args) throws DynamicError {
		Collection cargs = Function.convert_arguments(args, expected_args());

		ResultSequence rs = ResultSequenceFactory.create_new();

		// get args
		Iterator argiter = cargs.iterator();
		ResultSequence arg1 = (ResultSequence) argiter.next();
		String str1 = "";
		if (!arg1.empty())
			str1 = ((XSString) arg1.first()).value();

		ResultSequence arg2 = (ResultSequence) argiter.next();
		ResultSequence arg3 = (ResultSequence) argiter.next();
		ResultSequence arg4 = null;
		if (argiter.hasNext()) {
			arg4 = (ResultSequence) argiter.next();
			String flags = arg4.first().string_value();
			
			if (flags.length() == 0) {
				arg4 = null;
			} else if (isFlagValid(flags) == false) {
				throw new DynamicError("FORX0001", "Invalid regular expression. flags");
			}
		}
		String pattern = ((XSString) arg2.first()).value();
		String replacement = ((XSString) arg3.first()).value();
		
		try {
			rs.add(new XSString(str1.replaceAll(pattern, replacement)));
			return rs; 
		} catch (PatternSyntaxException err) {
			throw DynamicError.regex_error(null);
		} catch (IllegalArgumentException ex) {
			throw new DynamicError("FORX0004", "invalid regex.");
		} catch (IndexOutOfBoundsException ex) {
			String className = ex.getClass().getName();
			if (className.endsWith("StringIndexOutOfBoundsException")) {
				throw new DynamicError("FORX0004", "result out of bounds");
			}
			throw new DynamicError("FORX0003", "invalid regex.");
		} catch (Exception ex) {
			throw new DynamicError("FORX0004", "invalid regex.");
		}
	}
	
	private static boolean isFlagValid(String flag) {
		char flags[] = {'s', 'm', 'i', 'x'};
		
		for (int i = 0; i < flags.length; i++) {
			if (flag.indexOf(flags[i]) != -1) {
				return true;
			}
		}
		
		return false;
	}
	
	

	/**
	 * Obtain a list of expected arguments.
	 * 
	 * @return Result of operation.
	 */
	public static Collection expected_args() {
		if (_expected_args == null) {
			_expected_args = new ArrayList();
			SeqType arg = new SeqType(new XSString(), SeqType.OCC_QMARK);
			_expected_args.add(arg);
			_expected_args.add(new SeqType(new XSString(), SeqType.OCC_NONE));
			_expected_args.add(new SeqType(new XSString(), SeqType.OCC_NONE));
			_expected_args.add(new SeqType(new XSString(), SeqType.OCC_NONE));
		}

		return _expected_args;
	}
}
