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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import file.DataException;


/**
 * This is an class for objects that serve as a go between from DOM to a
 * JFLAP object representing a structure (such as an automaton or grammar), and
 * back again.
 * 
 * @author Thomas Finley
 */

public abstract class StructureTransducer<T> implements XMLTransducer<T> {
	/** The tag name for the type of structure this is. */
	public static final String STRUCTURE_TYPE_NAME = "type";


	/** The tag name for the root of a structure. */
	public static final String STRUCTURE_TAG = "structure";


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
		attributes.put(STRUCTURE_TYPE_ATTR, getTypeTag());
		// Create and add the <structure> element.
		Element structureElement = XMLHelper.createElement(doc, getTag(), null,
				attributes);
		return structureElement;
	}
	
	@Override
	public String getTag() {
		return STRUCTURE_TAG;
	}
	
	public abstract String getTypeTag();

	public static String retrieveTypeTag(Element struct) {
		// Check for the type tag.
		String tag = struct.getAttribute(STRUCTURE_TYPE_ATTR);

		return tag;
	}
	
	/**
	 * Given a DOM document, this will return an appropriate instance of a
	 * transducer for the type of document. Note that the type of the structure
	 * should be specified with in the "type" tags.
	 * 
	 * @param document
	 *            the document to get the transducer for
	 * @return the correct transducer for this document
	 * @throws IllegalArgumentException
	 *             if the document does not map to a transducer, or if it does
	 *             not contain a "type" tag at all
	 */
	public static XMLTransducer getStructureTransducer(Element root) {
		
		return TransducerFactory.getTransducerForTag(retrieveTypeTag(root));
		
	}
}
