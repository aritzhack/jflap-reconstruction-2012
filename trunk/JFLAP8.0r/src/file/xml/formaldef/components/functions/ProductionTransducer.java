package file.xml.formaldef.components.functions;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.ValidationEvent;

import model.formaldef.components.functionset.function.LanguageFunction;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Production;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class ProductionTransducer extends FunctionTransducer<Production> {

	public static final String PRODUCTION_TAG = "production";

	public ProductionTransducer(VariableAlphabet vars, TerminalAlphabet terms) {
		super(vars, terms);
	}
	
	@Override
	public String getTag() {
		return PRODUCTION_TAG;
	}

	@Override
	public Production createFunction(Map<String, Object> valueMap) {
		SymbolString lhs = (SymbolString) valueMap.get(LHS_TAG);
		SymbolString rhs = (SymbolString) valueMap.get(RHS_TAG);
		return new Production(lhs, rhs);
	}

	@Override
	public Map<String, Object> createTagToValueMap(Production p) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(LHS_TAG, p.getLHS());
		map.put(RHS_TAG, p.getRHS());
		return map;
	}


}
