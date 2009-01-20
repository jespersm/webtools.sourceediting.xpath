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

import java.util.*;

import org.eclipse.wst.xml.xpath2.processor.ast.*;
import org.eclipse.wst.xml.xpath2.processor.types.*;

/**
 * Interface to the methods of range of result sequence
 */
public abstract class ResultSequence {
	
	/**
 	 * add item
 	 * @param item is an item of any type.
 	 */
	public abstract void add(AnyType item);

	/**
 	 * concatinate from rs
 	 * @param rs is a Result Sequence.
 	 */
	public abstract void concat(ResultSequence rs);

	/**
 	 * List Iterator.
 	 */
	public abstract ListIterator iterator();

	/**
 	 * get item in index i
 	 * @param i is the position.
 	 */
	public abstract AnyType get(int i);

	/**
 	 * get the size
 	 * @return the size.
 	 */
	public abstract int size();

	/**
 	 * clear 
 	 */
	public abstract void clear();

	/**
 	 * create a new result sequence
 	 * @return a new result sequence.
 	 */
	public abstract ResultSequence create_new();

	/**
 	 * retrieve the first item
 	 * @return the first item.
 	 */
	public AnyType first() {
		return get(0);
	}

	/**
 	 * check is the sequence is empty
 	 * @return boolean.
 	 */
	public boolean empty() {
		if(size() == 0)
			return true;
		return false;	
	}
	
	/**
 	 * retrieve items in sequence
 	 * @return result string
 	 */
	public String string() {
		String result = "";
		int num = 1;

		for(Iterator i = iterator(); i.hasNext();) {
			AnyType elem = (AnyType) i.next();

			result += num + ") ";
			
			result += elem.string_type() + ": ";

			String value = elem.string_value();

			if(elem instanceof NodeType) {
				QName tmp = ((NodeType)elem).node_name();

				if(tmp != null)
					value = tmp.expanded_name();
			}
			result += value + "\n";

			num++;
		}
		if(num == 1)
			result = "Empty results\n";
		return result;
	}

	/**
 	 * release the result sequence
 	 */
	public void release() {
		ResultSequenceFactory.release(this);
	}
}