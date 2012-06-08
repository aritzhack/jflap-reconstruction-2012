package file.xml.formaldef;

import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import file.xml.XMLHelper;

import model.automata.InputAlphabet;
import model.formaldef.components.symbols.SymbolString;
import model.regex.RegularExpression;

public class RegExTransducer extends FormalDefinitionTransducer<RegularExpression> {

	private static final String EXPRESSION_TAG = "exp";

	@Override
	public RegularExpression fromStructureRoot(Element root) {
		RegularExpression regex = super.fromStructureRoot(root);
		NodeList list = root.getElementsByTagName(EXPRESSION_TAG);
		String exp = XMLHelper.containedText(list.item(0));
		regex.setTo(exp);
		return regex;

	}
	
	@Override
	public RegularExpression buildStructure(Object[] subComp) {
		return buildStructure((InputAlphabet) subComp[0]);
	}

	private RegularExpression buildStructure(InputAlphabet inputAlphabet) {
		RegularExpression regex = new RegularExpression(inputAlphabet);
		return regex;
	}
	
	

	@Override
	public Element appendComponentsToRoot(RegularExpression structure,
			Element root) {
		root.appendChild(createExpressionElement(structure.getExpression()));
		return super.appendComponentsToRoot(structure, root);
	}

	private Node createExpressionElement(SymbolString exp) {
		return XMLHelper.createElement(EXPRESSION_TAG, exp.toNondelimitedString(), null);
	}

	@Override
	public String getTag() {
		return "regex";
	}


}
