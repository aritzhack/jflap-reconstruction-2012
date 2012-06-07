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





package file.xml.transducers;

import java.util.Map;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



/**
 * This transducer is the codec for {@link jflap.model.grammar.Grammar} objects.
 * 
 * @author Thomas Finley
 */

public class GrammarTransducer extends FormalDefinitionTransducer<UnboundGrammar> {
	/**
	 * Returns the type this transducer recognizes, "grammar".
	 * 
	 * @return the string "grammar"
	 */
	public String getType() {
		return "grammar";
	}

	@Override
	public UnboundGrammar fromSpecificContentRoot(Element root, UnboundGrammar g) {
		NodeList list = root.getElementsByTagName(PRODUCTION_NAME);
		for (int i = 0; i < list.getLength(); i++) {
			Production p = createProduction(list.item(i), g);
			g.addProduction(p);
		}
		return g;
	}


	@Override
	public String getStructureSpecificTag() {
		return getType();
	}

	@Override
	protected Document appendStructure(UnboundGrammar grammar, Element grammarRoot, Document doc) {

		// Add the productions as subelements of the structure element.
		Production[] productions = grammar.getProductions();
		if (productions.length > 0)
			grammarRoot.appendChild(TransducerHelper.createComment(doc, COMMENT_PRODUCTIONS));
		for (int i = 0; i < productions.length; i++)
			grammarRoot.appendChild(createProductionElement(doc, productions[i]));
		// Return the completed document.
		return doc;
	}


	/**
	 * Returns a production for a given node.
	 * 
	 * @param node
	 *            the node the encapsulates a production
	 */
	private Production createProduction(Node node, UnboundGrammar g) {
		Map<String, String> e2t = TransducerHelper.elementsToText(node);
		SymbolString left = SymbolString.createFromString((String) e2t.get(PRODUCTION_LEFT_NAME), g),
				right = SymbolString.createFromString((String) e2t.get(PRODUCTION_RIGHT_NAME), g);
		return new Production(left, right);
	}

	private Element createProductionElement(Document document,
			Production production) {
		Element pe = TransducerHelper.createElement(document, PRODUCTION_NAME, null, null);
		pe.appendChild(TransducerHelper.createElement(document, PRODUCTION_LEFT_NAME, null,
				production.getLHS().toString()));
		pe.appendChild(TransducerHelper.createElement(document, PRODUCTION_RIGHT_NAME, null,
				production.getRHS().toString()));
		return pe;
	}


	/** The tag name for productions. */
	public static final String PRODUCTION_NAME = "production";

	/** The tag name for the left of the production. */
	public static final String PRODUCTION_LEFT_NAME = "left";

	/** The tag name for the right of the production. */
	public static final String PRODUCTION_RIGHT_NAME = "right";

	/** The comment for the list of productions. */
	private static final String COMMENT_PRODUCTIONS = "The list of productions.";

}
