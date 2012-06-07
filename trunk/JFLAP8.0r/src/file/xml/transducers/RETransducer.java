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

import jflap.file.ParseException;
import jflap.file.xml.TransducerHelper;
import jflap.model.formaldef.symbols.SymbolString;
import jflap.model.regular.*;


import org.w3c.dom.*;


/**
 * This transducer is the codec for {@link jflap.model.regular.RegularExpression} objects.
 * 
 * @author Thomas Finley
 */

public class RETransducer extends FormalDefinitionTransducer<RegularExpression> {
	
	
	/** The tag name for the regular expression itself. */
	public static final String EXPRESSION_NAME = "expression";

	/** The comment for the list of productions. */
	private static final String COMMENT_EXPRESSION = "The regular expression.";
	
	
	public String getType() {
		return "re";
	}

	@Override
	public RegularExpression fromSpecificContentRoot(Element root,
			RegularExpression def) {
		String exp = TransducerHelper.containedText(root);
		SymbolString symExp = SymbolString.createFromString(exp, def);
		def.setExpression(symExp);
		return def;
	}

	@Override
	public String getStructureSpecificTag() {
		return EXPRESSION_NAME;
	}

	@Override
	protected Document appendStructure(RegularExpression structure,
			Element content_root, Document doc) {
		Node struct = content_root.getParentNode();
		struct.insertBefore(TransducerHelper.createComment(doc, COMMENT_EXPRESSION), content_root);
		content_root.appendChild(doc.createTextNode(structure.getExpression().toString()));
		return doc;
	}

	
}
