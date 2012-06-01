package model.grammar.parsing.cyk;

/**
 * CYK parser redesign, Summer 2012
 * @author Peggy Li, Ian McMahon
 */

import java.util.*;

import errors.BooleanWrapper;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.parsing.Parser;

public class CYKParser extends Parser {
	
	private ProductionSet myProductions;
	private static Variable myStartVariable;
	
	private ArrayList<Production> myDerivationRules;
	
	private CYKParseTable myParseTable;

	public CYKParser(Grammar g) {
		super(g);
		
		myProductions = g.getProductionSet();
		myStartVariable = g.getStartVariable();
		
		myDerivationRules = new ArrayList<Production>();
	}
	
	

	@Override
	public String getDescriptionName() {
		return "CYK Parser";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	// should be parsing, not initialization
	public BooleanWrapper init(SymbolString input) {
		myParseTable = new CYKParseTable(input);
		
		int length = input.size();
		
		// terminals
		for (int i = 0; i < length; i++) {
			int count = 0;
			SymbolString current = input.subList(i, i+1);
			for (Production p : myProductions) {
				if (p.getRHS().equals(current)) {
					myParseTable.addVariable(i, i, (Variable) p.getLHS().getFirst());
					count++;
				}
			}
			if (count == 0)		
				return new BooleanWrapper(false, "At least one terminal in the string cannot be dervied.");
			
		}
		
		for (int increment = 1; increment < length; increment++) {
			for (int row = 0; row < length - 1; row++) {
				
				int substringLength = row + increment;
				if (length <= substringLength) {
					break;
				}
				
				findProductions(row, substringLength);
				
			}
		}
		
 		return new BooleanWrapper(myParseTable.getVariableSet(0, length-1).contains(myStartVariable));
	}
	
	// finds all productions whose RHS matches the substring
	private void findProductions (int start, int end) {
		
		for (int k = start; k < end; k++) {
			for (Variable A : myParseTable.getVariableSet(start, k)) {
				for (Variable B : myParseTable.getVariableSet(k+1, end)) {
					SymbolString concat = new SymbolString(A, B);
					for (Production p : myProductions) {
						if (p.getRHS().equals(concat)) {
							myParseTable.addVariable(start, end, 
									(Variable) p.getLHS().getFirst());
						}
					}
				}
			}
			
		}
	}

}
