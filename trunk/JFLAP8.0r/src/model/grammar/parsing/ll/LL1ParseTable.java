package model.grammar.parsing.ll;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import universe.preferences.JFLAPPreferences;

import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.parsing.FirstFollowTable;
import model.util.UtilFunctions;

public class LL1ParseTable {

	private Set<SymbolString> myTable[][];
	private FirstFollowTable myFirstFollow;
	private Grammar myGrammar;
	private Variable[] myRows;
	private Terminal[] myColumns;

	public LL1ParseTable(FirstFollowTable table, boolean complete) {
		myFirstFollow = table;
		myGrammar = myFirstFollow.getAssociatedGrammar();
		myRows = myGrammar.getVariables().toArray(new Variable[0]);
		
		myColumns = myGrammar.getTerminals().toArray(new Terminal[0]);
		myColumns = UtilFunctions.combine(myColumns,
				JFLAPPreferences.getEndOfStringMarker());
		myTable = new Set[myRows.length][myColumns.length];
		for (int i = 0; i< myRows.length;i++){
			for (int j = 0; j < myColumns.length; j++){
				myTable[i][j] = new TreeSet<SymbolString>();
			}
		}
		if (complete)
			completeTable();
	}
	
	

	public LL1ParseTable(Grammar g){
		this (g, true);
	}



	public LL1ParseTable(Grammar g, boolean complete) {
		this(new FirstFollowTable(g), complete);
	}



	private void completeTable() {
		for (Production p: myGrammar.getProductionSet()){
			addEntryForProduction(p);
		}
	}


	public boolean addEntryForProduction(Production p) {
		Set<Terminal> terms = myFirstFollow.retrieveFirstSet(p.getRHS());
		Terminal empty = JFLAPPreferences.getSubForEmptyString();
		Variable A = (Variable) p.getLHS().getFirst();
		if (terms.contains(empty)){
			terms = myFirstFollow.getFollow(A);
		}
		
		boolean changed = false;
		
		for (Terminal t: terms){
			changed = setValue(p.getRHS(), A, t) || changed;
		}
		
		return changed;
		
	}

	public boolean setValue(SymbolString rhs, Variable a, Terminal t) {
		int r = getRowForVar(a);
		int c = getColForTerm(t);
		return setValue(rhs, r, c);
	}
	


	private boolean setValue(SymbolString rhs, int r, int c) {
		return myTable[r][c].add(rhs);
	}



	private int getColForTerm(Terminal t) {
		for(int i = 0; i < myColumns.length; i++){
			if (myColumns[i].equals(t))
				return i;
		}
		return -1;
	}



	private int getRowForVar(Variable v) {
		for(int i = 0; i < myRows.length; i++){
			if (myRows[i].equals(v))
				return i;
		}
		return -1;
	}

}
