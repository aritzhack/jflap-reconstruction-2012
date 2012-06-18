package file.xml.formaldef.regex;

import model.automata.InputAlphabet;
import model.automata.transducers.OutputAlphabet;
import model.formaldef.components.symbols.SymbolString;
import model.regex.ExpressionComponent;
import model.regex.OperatorAlphabet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import file.xml.StructureTransducer;
import file.xml.XMLTransducer;
import file.xml.formaldef.components.symbols.SymbolStringTransducer;

public class ExpressionStringTransducer extends StructureTransducer<ExpressionComponent> {

	private SymbolStringTransducer mySymbolStringTransducer;

	public ExpressionStringTransducer(InputAlphabet input){
		mySymbolStringTransducer = new SymbolStringTransducer(EXPRESSION_TAG, input, new OperatorAlphabet());
	}

	@Override
	public String getTag() {
		return EXPRESSION_TAG;
	}

	@Override
	public ExpressionComponent fromStructureRoot(Element root) {
		Element e = (Element) root.getElementsByTagName(EXPRESSION_TAG).item(0);
		SymbolString expression = mySymbolStringTransducer.fromStructureRoot(e);
		return new ExpressionComponent(expression);
	}

	@Override
	public Element appendComponentsToRoot(Document doc, ExpressionComponent structure,
			Element root) {
		Element e = mySymbolStringTransducer.toXMLTree(doc, structure.getExpression());
		root.appendChild(e);
		return root;
	}
	

}
