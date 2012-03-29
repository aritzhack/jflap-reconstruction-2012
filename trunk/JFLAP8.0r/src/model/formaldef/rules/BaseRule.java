package model.formaldef.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import errors.BooleanWrapper;

import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.SymbolHelper;




public class BaseRule extends SymbolRule<FormalDefinition<?,?>, Alphabet> {

	//TODO: This seems silly...is there a better way to check for "can change" than basically 
		//changing without CHANGING the symbol?
	
	@Override
	public BooleanWrapper canModify(FormalDefinition<?,?> parent, Alphabet a,
			Symbol oldSymbol, Symbol newSymbol) {
		BooleanWrapper canModify = parent.removeSymbolFromAlphabet(a.getClass(), oldSymbol);
		if (canModify.isTrue()) {
			canModify = this.canAdd(parent, a, newSymbol);
			parent.addSymbol(a.getClass(), oldSymbol);
		}
		
		return canModify;
	}

	@Override
	public BooleanWrapper canRemove(FormalDefinition<?,?> parent, Alphabet a,
			Symbol oldSymbol) {
		return BooleanWrapper.combineWrappers(
				new BooleanWrapper(!a.isEmpty(),
						"The " + this.toString() +" is empty, you may not remove symbols from it"),
				new BooleanWrapper(a.contains(oldSymbol), 
						"This " + a.getName() + " does not contain " + "the symbol " + oldSymbol.getString()));

	}

	@Override
	public BooleanWrapper canAdd(FormalDefinition<?,?> parent, Alphabet a,
			Symbol newSymbol) {
		if (newSymbol.length() <= 0)
			return new BooleanWrapper(false, "You may not add a symbol of no length.");
		for (Character c: parent.getDisallowedCharacters()){
			if (newSymbol.containsCharacters(c))
				return new BooleanWrapper(false, "The character " + c + " is disallowed for this " + a.getName() +
						". For more information on allowability rules, " +
						            "go to the Rules option in the Help Menu.");
		}
		for(Symbol s: a){
			if (areTooSimilar(newSymbol, s))
				return new BooleanWrapper(false, "The " + newSymbol.getString() + " is not allowed because " +
						"it is too similar to the - " + s.getString() + " -  in the " + a.getName() + ". "+ 
						"For more information on allowability rules, " +
						            "go to the Rules option in the Help Menu.");
		}
		return new BooleanWrapper(true, "Symbol " + newSymbol.getString() + " can be added to the " + a.getName() +" sucessfully");
	}

	protected boolean areTooSimilar(Symbol s1, Symbol s2) {
		return SymbolHelper.containsSubstring(s2, s1) || 
				SymbolHelper.containsSubstring(s1, s2);
	}

	
	
	
	@Override
	public boolean shouldBeApplied(FormalDefinition parent, Alphabet a) {
		return super.shouldBeApplied(parent, a);
	}

	@Override
	public String getName() {
		return "Basic Rule";
	}

	@Override
	public String getDescription() {
		return "The basic rules that all alphabets are held to individually. These include...";
	}

}
