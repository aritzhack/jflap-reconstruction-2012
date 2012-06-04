package model.grammar.parsing.cyk;

import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Variable;
import model.grammar.Production;

public class CYKParseNode {
	private Variable LHS;
	private SymbolString RHS;
	private int k;
	
	public CYKParseNode(Production p, int k){
		LHS = (Variable) p.getLHS().getFirst();
		RHS = p.getRHS();
		this.k = k;
	}
	
	public Variable getLHS(){
		return LHS;
	}
	
	public SymbolString getRHS(){
		return RHS;
	}
	
	public Variable getFirstRHSVariable(){
		return (Variable) RHS.getFirst();
	}
	
	public Variable getSecondRHSVariable(){
		return (Variable) RHS.getLast();
	}
	
	public int getK(){
		return k;
	}
}
