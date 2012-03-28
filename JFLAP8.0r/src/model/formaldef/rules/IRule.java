package model.formaldef.rules;

import errors.BooleanWrapper;
import model.formaldef.FormalDefinition;
import model.formaldef.alphabets.Alphabet;
import model.formaldef.symbols.Symbol;






public interface IRule<T extends FormalDefinition, S extends Alphabet> {

	public BooleanWrapper canModify(T parent, S a, Symbol oldSymbol,
			Symbol newSymbol);

	public BooleanWrapper canRemove(T parent, S a, Symbol oldSymbol);

	public BooleanWrapper canAdd(T parent, S a, Symbol newSymbol);
	
	public boolean shouldBeApplied(T parent, S a);
	
	public String getName();
	
	public String getDescription();
	
	public String toString();
	
}