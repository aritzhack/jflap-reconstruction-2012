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

public abstract class StructureTransducer<T> implements Transducer<T> {
	/* (non-Javadoc)
	 * @see file.xml.Transducer#fromStructureRoot(org.w3c.dom.Element, org.w3c.dom.Document)
	 */
	@Override
	public abstract T fromStructureRoot(Element root);

	/* (non-Javadoc)
	 * @see file.xml.Transducer#toXMLTree(org.w3c.dom.Document, T)
	 */
	@Override
	public Element toXMLTree(Document doc, T structure){
		Element root = creatRoot(doc, structure);
		return appendComponentsToRoot(doc, structure, root);
	}

	public abstract Element appendComponentsToRoot(Document doc, T structure, Element root);

	public Element creatRoot(Document doc, T structure) {
		
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put(STRUCTURE_TYPE_NAME, getTag());
		// Create and add the <structure> element.
		Element structureElement = XMLHelper.createElement(doc, STRUCTURE_TAG, null,
				attributes);
		return structureElement;
	}

	/* (non-Javadoc)
	 * @see file.xml.Transducer#getTag()
	 */
	@Override
	public abstract String getTag();

	/** The tag name for the root of a structure. */
	public static final String STRUCTURE_TAG = "structure";

	/** The tag name for the type of structure this is. */
	public static final String STRUCTURE_TYPE_NAME = "type";
}
