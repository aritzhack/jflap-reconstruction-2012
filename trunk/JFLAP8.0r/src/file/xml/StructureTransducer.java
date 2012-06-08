/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





package file.xml;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * This is an class for objects that serve as a go between from DOM to a
 * JFLAP object representing a structure (such as an automaton or grammar), and
 * back again.
 * 
 * @author Thomas Finley
 */

public abstract class StructureTransducer<T> {
	/**
	 * Given a document, this will return the corresponding JFLAP structure
	 * encoded in the DOM document.
	 * 
	 * @param root
	 *            the DOM document to decode
	 * @return a serializable object, as all JFLAP structures are encoded in
	 *         serializable objects
	 * @throws FileParseException
	 *             in the event of an error that may lead to undesirable
	 *             functionality
	 */
	public abstract T fromStructureRoot(Element root);

	/**
	 * Given a JFLAP structure, this will return the corresponding DOM encoding
	 * of the structure.
	 * 
	 * @param structure
	 *            the JFLAP structure to encode
	 * @return a DOM document instance
	 */
	public Element toXMLTree(T structure){
		Element root = creatRoot(structure);
		return appendComponentsToRoot(structure, root);
	}

	public abstract Element appendComponentsToRoot(T structure, Element root);

	public Element creatRoot(T structure) {
		
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put(STRUCTURE_TYPE_NAME, getTag());
		// Create and add the <structure> element.
		Element structureElement = XMLHelper.createElement(STRUCTURE_NAME, null,
				attributes);
		return structureElement;
	}

	/**
	 * Returns the string encoding of the type this transducer decodes and
	 * encodes.
	 * 
	 * @return the type this transducer recognizes
	 */
	public abstract String getTag();

	/** The tag name for the root of a structure. */
	public static final String STRUCTURE_NAME = "structure";

	/** The tag name for the type of structure this is. */
	public static final String STRUCTURE_TYPE_NAME = "type";
}
