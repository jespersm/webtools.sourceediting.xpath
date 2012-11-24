/*******************************************************************************
 * Copyright (c) 2005, 2010 Andrea Bittau, University College London, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Andrea Bittau - initial API and implementation from the PsychoPath XPath 2.0 
 *     Jesper Moller - bug 280555 - Add pluggable collation support
 *     David Carver (STAR) - bug 262765 - fixed collation and comparison issues.
 *     Mukul Gandhi - bug 280798 - PsychoPath support for JDK 1.4
 *******************************************************************************/

package org.eclipse.wst.xml.xpath2.processor.internal.function;

import org.apache.xerces.dom.PSVIElementNSImpl;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.eclipse.wst.xml.xpath2.processor.DynamicContext;
import org.eclipse.wst.xml.xpath2.processor.DynamicError;
import org.eclipse.wst.xml.xpath2.processor.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.ResultSequenceFactory;
import org.eclipse.wst.xml.xpath2.processor.internal.SeqType;
import org.eclipse.wst.xml.xpath2.processor.internal.types.AnyAtomicType;
import org.eclipse.wst.xml.xpath2.processor.internal.types.AnyType;
import org.eclipse.wst.xml.xpath2.processor.internal.types.AttrType;
import org.eclipse.wst.xml.xpath2.processor.internal.types.ElementType;
import org.eclipse.wst.xml.xpath2.processor.internal.types.NodeType;
import org.eclipse.wst.xml.xpath2.processor.internal.types.NumericType;
import org.eclipse.wst.xml.xpath2.processor.internal.types.QName;
import org.eclipse.wst.xml.xpath2.processor.internal.types.XSBoolean;
import org.eclipse.wst.xml.xpath2.processor.internal.types.XSDuration;
import org.eclipse.wst.xml.xpath2.processor.internal.types.XSInteger;
import org.eclipse.wst.xml.xpath2.processor.internal.types.XSString;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Returns a sequence of positive integers giving the positions within the
 * sequence $seqParam of items that are equal to $srchParam.
 */
public class FnIndexOf extends AbstractCollationEqualFunction {
	
	private static Collection _expected_args = null;
	
	/**
	 * Constructor for FnIndexOf.
	 */
	public FnIndexOf() {
		super(new QName("index-of"), 2, 3);
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
	public ResultSequence evaluate(Collection args) throws DynamicError {
		return index_of(args, dynamic_context());
	}

	/**
	 * Obtain a comparable type.
	 * 
	 * @param at
	 *            expression of any type.
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of operation.
	 */
	private static CmpEq get_comparable(AnyType at) throws DynamicError {
		if (at instanceof NodeType) {
			XSString nodeString = new XSString(at.string_value());
			return nodeString;
		}
		
		if (!(at instanceof AnyAtomicType))
			DynamicError.throw_type_error();
		
		if (!(at instanceof CmpEq))
			throw DynamicError.not_cmp(null);

		return (CmpEq) at;
	}

	/**
	 * Index-Of operation.
	 * 
	 * @param args
	 *            Result from the expressions evaluation.
	 * @param dynamicContext 
	 * @throws DynamicError
	 *             Dynamic error.
	 * @return Result of fn:index-of operation.
	 */
	public static ResultSequence index_of(Collection args, DynamicContext dynamicContext) throws DynamicError {
		Function.convert_arguments(args, expected_args());

		ResultSequence rs = ResultSequenceFactory.create_new();

		// get args
		Iterator citer = args.iterator();
		ResultSequence arg1 = (ResultSequence) citer.next();
		ResultSequence arg2 = (ResultSequence) citer.next();
		
		if (arg1.empty()) {
			return rs;
		}

		// sanity chex
		if (arg2.size() != 1)
			DynamicError.throw_type_error();
		
		String collationUri = dynamicContext.default_collation_name();
		if (citer.hasNext()) {
			ResultSequence arg3 = (ResultSequence) citer.next();
			if (!arg3.empty()) {
				XSString collation = (XSString) arg3.first();
				collationUri = collation.string_value();
			}
		}

		AnyAtomicType at = (AnyAtomicType)arg2.first();

		get_comparable(at);

		int index = 1;
		for (Iterator iter = arg1.iterator(); iter.hasNext();) {
			AnyType cmptype = (AnyType) iter.next();
			get_comparable(cmptype);
			
			boolean isSeqItemComparable = false;
			if (cmptype instanceof AnyAtomicType) {
				AnyAtomicType cmpAnyAtomicType = (AnyAtomicType)cmptype;
				if (cmpAnyAtomicType instanceof CmpEq) {
					isSeqItemComparable = true;
				}
			}
			if (!(at instanceof CmpEq || isSeqItemComparable)) {
				// values that cannot be compared, are considered distinct
				rs.add(new XSInteger(BigInteger.valueOf(index)));
				index++;
			}
			else if (cmptype instanceof NodeType && isNodeXSTypeCompatible(cmptype)) {
				// if the item was an element or attribute node, consider its atomized value
				ResultSequence rsAtomized = ResultSequenceFactory.create_new();
				rsAtomized.add(cmptype);
				rsAtomized = FnData.atomize(rsAtomized);
				if (rsAtomized.size() == 1) {
				    populateFnIndexOfResultSequence(dynamicContext, rs, collationUri, at, index, rsAtomized.first());
				    index++;
				}
				else if (rsAtomized.size() > 1) {
					// this may happen if an item represents a simpleType list
					for (Iterator iter_1 = rsAtomized.iterator(); iter_1.hasNext();) {
						AnyType cmptype_1 = (AnyType) iter_1.next();
						populateFnIndexOfResultSequence(dynamicContext, rs, collationUri, at, index, cmptype_1);
						index++;
					}
				}
			}
			else {
			   populateFnIndexOfResultSequence(dynamicContext, rs, collationUri, at, index, cmptype);
			   index++;
			}
		}

		return rs;
		
	} // index_of
	
	/*
	 * Check if the node's schema type is right for comparison.
	 */
	private static boolean isNodeXSTypeCompatible(AnyType cmptype) {
		boolean isXstypeCompatible = false;
		
		NodeType nodeType = (NodeType)cmptype;
		
		if (nodeType instanceof ElementType) {
			PSVIElementNSImpl elemPsvImpl = (PSVIElementNSImpl)((ElementType)nodeType).value();
			XSTypeDefinition typeDef = elemPsvImpl.getTypeDefinition();
			if (typeDef instanceof XSComplexTypeDefinition) {
				XSComplexTypeDefinition complexTypeDefinition = (XSComplexTypeDefinition) typeDef;
				if (complexTypeDefinition.getContentType() == XSComplexTypeDefinition.CONTENTTYPE_SIMPLE) {
					isXstypeCompatible = true;	
				}
			}
			else {
				// simple type definition
				isXstypeCompatible = true;
			}
		}
		else if (nodeType instanceof AttrType) {
			isXstypeCompatible = true;	
		}
		
		return isXstypeCompatible;
		
	} // isNodeXSTypeCompatible

	/*
	 * Add an index value to the result sequence, if the searched item was found in input sequence. This doesn't
	 * consider node types (they are handled separately). 
	 */
	private static void populateFnIndexOfResultSequence(DynamicContext dynamicContext, ResultSequence rs,
			                                            String collationUri, AnyAtomicType at, int index, AnyType cmptype)
			                         throws DynamicError {
		
		if (isBoolean(cmptype, at)) {
			XSBoolean boolat = (XSBoolean) cmptype;
			if (boolat.eq(at, dynamicContext)) {
			   rs.add(new XSInteger(BigInteger.valueOf(index)));
			}
		} else if (isNumeric(cmptype, at)) {
			NumericType numericat = (NumericType) at;
			if (numericat.eq(cmptype, dynamicContext)) {
				rs.add(new XSInteger(BigInteger.valueOf(index)));
			}
		} else if (isDuration(cmptype, at)) {
			XSDuration durat = (XSDuration) at;
			if (durat.eq(cmptype, dynamicContext)) {
				rs.add(new XSInteger(BigInteger.valueOf(index)));
			}
		} 
		else if (at instanceof QName && cmptype instanceof QName) {
			QName qname = (QName)at;
			if (qname.eq(cmptype, dynamicContext)) {
				rs.add(new XSInteger(BigInteger.valueOf(index)));
			}
		}
		else if (needsStringComparison(cmptype, at)) {
			XSString xstr1 = new XSString(cmptype.string_value());
			XSString itemStr = new XSString(at.string_value());
			if (FnCompare.compare_string(collationUri, xstr1, itemStr, dynamicContext).equals(BigInteger.ZERO)) {
				rs.add(new XSInteger(BigInteger.valueOf(index)));
			}
		}
		
	} // populateFnIndexOfResultSequence

	/**
	 * Obtain a list of expected arguments.
	 * 
	 * @return Result of operation.
	 */
	public synchronized static Collection expected_args() {
		if (_expected_args == null) {
			_expected_args = new ArrayList();
			SeqType arg = new SeqType(AnyType.class, SeqType.OCC_STAR);
			_expected_args.add(arg);
			_expected_args.add(new SeqType(AnyAtomicType.class, SeqType.OCC_NONE));
			_expected_args.add(new SeqType(new XSString(), SeqType.OCC_NONE));
			_expected_args.add(arg);
		}

		return _expected_args;
	}
	
}
