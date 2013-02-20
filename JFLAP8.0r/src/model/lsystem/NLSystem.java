package model.lsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import debug.JFLAPDebug;

import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class NLSystem {
	private CommandAlphabet myCommandAlph;
	private SymbolString myAxiom;
	private Grammar myGrammar;
	private Map<String, String> myParameters;
	private Map<SymbolString, List<SymbolString>> myReplacements;
	
	public NLSystem(){
		this(new SymbolString(), new Grammar(), new HashMap<String, String>());
	}
	
	public NLSystem(SymbolString axiom, Grammar g, Map<String, String> parameters){
		myAxiom = axiom;
		myCommandAlph = new CommandAlphabet();
		myGrammar = g;
		myGrammar.getLanguageAlphabet().addAll(myCommandAlph);
		addAxiomToAlphabet();
		myParameters = parameters;
		initReplacements();
	}

	private void initReplacements() {
		myReplacements = new TreeMap<SymbolString, List<SymbolString>>();
		ProductionSet prods = myGrammar.getProductionSet();
		
		for(Production p : prods){
			SymbolString lhs = new SymbolString(p.getLHS());
			if(!myReplacements.containsKey(lhs))
				myReplacements.put(lhs, new ArrayList<SymbolString>());
			SymbolString rhs = new SymbolString(p.getRHS());
			myReplacements.get(lhs).add(rhs);
		}
	}

	private void addAxiomToAlphabet() {
		for(Symbol s : myAxiom){
			if(s instanceof Variable)
				myGrammar.getVariables().add(s);
			else
				myGrammar.getTerminals().add(s);
		}
	}
	
	public SymbolString getAxiom(){
		return myAxiom;
	}
	
	public Map<String, String> getParameters(){
		return myParameters;
	}
	
	public SymbolString[] getReplacements(SymbolString s){
		List<SymbolString> sList = myReplacements.get(s);
		SymbolString[] emptyArray = new SymbolString[0];
		return sList == null ? emptyArray : sList.toArray(emptyArray);
	}
	
	public Set<SymbolString> getSymbolStringsWithReplacements(){
		return myReplacements.keySet();
	}
	
	public boolean isNondeterministic(){
		for(List<SymbolString> p : myReplacements.values()){
			if(p.size() > 1) return true;
		}
		return false;
	}
}
