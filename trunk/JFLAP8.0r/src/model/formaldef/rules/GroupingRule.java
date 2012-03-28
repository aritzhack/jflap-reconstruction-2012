package model.formaldef.rules;

import java.util.ArrayList;

import errors.BooleanWrapper;

import model.formaldef.FormalDefinition;
import model.formaldef.alphabets.Alphabet;
import model.formaldef.symbols.Symbol;




public class GroupingRule extends SymbolRule<FormalDefinition, Alphabet> {

	private ArrayList<String> getAllGrouping(FormalDefinition def){
		ArrayList<String> chars = new ArrayList<String>();
		
		for(Alphabet a: def.getAlphabets()){
			if (a.usingGrouping())
				chars.add(a.getGrouping().getCloseGroup());
				chars.add(a.getGrouping().getOpenGroup());
		}
		return chars;
	}
	
	
	private BooleanWrapper checkValidGroupingSyntax(Alphabet a, String string) {
		if (string.length() < 2 || 
				string.indexOf(a.getGrouping().getOpenGroup()) != 0 || 
						string.indexOf(a.getGrouping().getCloseGroup()) != string.length()-1)
			return new BooleanWrapper(false, "The symbol " + string + 
					" does not contain the necessary\n"+ 
					"grouping characters in the correct locations.");
		return new BooleanWrapper(true);
		
		
	}

	@Override
	public BooleanWrapper canModify(FormalDefinition parent, Alphabet a,
			Symbol oldSymbol, Symbol newSymbol) {
		return checkValid(parent, a, newSymbol);
	}


	public BooleanWrapper checkValid(FormalDefinition parent, Alphabet a,
			Symbol newSymbol) {
		String toCheck = newSymbol.toString();
		BooleanWrapper bw;

		//check to see if the new Symbol uses valid grouping if necessary
		if (a.usingGrouping()){
			bw = checkValidGroupingSyntax(a, newSymbol.toString());
			if (bw.isTrue()) toCheck = toCheck.substring(1, toCheck.length()-1);
			else return bw;
		}

		//check to make sure the symbol does not contain grouping characters from other alphabets
		for (String s: this.getAllGrouping(parent)){
			for(Character c: s.toCharArray())
				if (toCheck.indexOf(c) > 0){
					return new BooleanWrapper(false,"You cannot add a symbol containing the\n" +
							" grouping character: " + c);
				}
		}
		
		return new BooleanWrapper(true);
	}

	
	@Override
	public BooleanWrapper canRemove(FormalDefinition parent, Alphabet a,
			Symbol oldSymbol) {
		return new BooleanWrapper(true);
	}

	@Override
	public BooleanWrapper canAdd(FormalDefinition parent, Alphabet a,
			Symbol newSymbol) {
		return checkValid(parent, a, newSymbol);
	}

	@Override
	public String getDescription() {
		return "This rule dictates what is possible in the case of grammars using grouping.";
	}

	@Override
	public String getName() {
		return "Grouping Pair Rule";
	}

}
