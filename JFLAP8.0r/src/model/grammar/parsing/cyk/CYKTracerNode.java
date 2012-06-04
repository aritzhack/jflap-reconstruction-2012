package model.grammar.parsing.cyk;

import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Variable;
import model.grammar.Production;

public class CYKTracerNode {
	private Variable LHS;
	private SymbolString RHS;
	private int k, row, col;
	
	public CYKTracerNode(Production p, int k){
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
	
	public Variable getAVariable(){
		return (Variable) RHS.getFirst();
	}
	
	public Variable getBVariable(){
		return (Variable) RHS.getLast();
	}
	
	public int getK(){
		return k;
	}
}
