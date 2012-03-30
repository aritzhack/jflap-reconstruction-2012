package model.formaldef.rules;

import errors.BooleanWrapper;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.formaldef.components.alphabets.specific.VariableAlphabet;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.grammar.Grammar;


public class VariableGroupingRule extends GroupingRule<VariableAlphabet> {





	private TerminalGroupingRule myInternalGroupingRule;

	public VariableGroupingRule(GroupingPair gp) {
		super(gp);
		myInternalGroupingRule = new TerminalGroupingRule(gp);
	}


	private BooleanWrapper checkExternalGrouping(String string) {
		
		boolean correct = string.length() > 2 && 
							string.indexOf(this.getOpenGroup()) == 0 && 
							string.indexOf(this.getCloseGroup()) == string.length()-1;
		
			return new BooleanWrapper(correct, "The Variable " + string + 
												" does not contain the necessary\n"+ 
												"grouping characters " + this.getGroupingPair().toString() + " flanking the symbol.");
	}



	private BooleanWrapper checkValid(Symbol newSymbol) {
		String toCheck = newSymbol.toString();
		
		//Check that grouping outside is correct
		BooleanWrapper bw = checkExternalGrouping(newSymbol.toString());
		
		if (bw.isTrue()){
			//Check that there are no grouping characters inside the symbol
			bw = checkInternalGrouping(toCheck);
		}
		
		return bw;
	}


	private BooleanWrapper checkInternalGrouping(String toCheck) {
		String inside = toCheck.substring(1, toCheck.length()-1);
		boolean contains = myInternalGroupingRule.canAdd(null, new Symbol(inside)).isTrue();
		return new BooleanWrapper(contains, "You may not create a Variable " +
												"with internal grouping characters. ");
	}


	@Override
	public BooleanWrapper canModify(VariableAlphabet a,
			Symbol oldSymbol, Symbol newSymbol) {
		return checkValid(newSymbol);
	}

	@Override
	public BooleanWrapper canAdd(VariableAlphabet a, Symbol newSymbol) {
		return checkValid(newSymbol);
	}
	

	@Override
	public String getDescription() {
		return "Variables are fun!! This rule dictates what is possible in the case of grammars using grouping.";
	}

	@Override
	public String getDescriptionName() {
		return "Variables Rule";
	}


}
