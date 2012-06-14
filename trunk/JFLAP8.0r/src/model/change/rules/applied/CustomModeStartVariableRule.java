package model.change.rules.applied;

import preferences.JFLAPPreferences;
import errors.BooleanWrapper;
import model.change.ChangeEvent;
import model.change.events.SetToEvent;
import model.change.rules.Rule;
import model.formaldef.components.symbols.Symbol;
import model.grammar.StartVariable;
import model.grammar.VariableAlphabet;

public class CustomModeStartVariableRule extends Rule {

	private VariableAlphabet myVars;

	public CustomModeStartVariableRule(VariableAlphabet alph) {
		super(SET_TO);
		myVars = alph;
	}

	@Override
	public String getDescriptionName() {
		return "Custom Mode Start Variable Rule";
	}

	@Override
	public String getDescription() {
		return "The Start Variable can only be set to a variable in the variable" +
				" alphabet.";
	}

	@Override
	public BooleanWrapper checkRule(ChangeEvent event) {
		SetToEvent<StartVariable, Symbol> e = (SetToEvent<StartVariable, Symbol>) event;
		boolean ok = myVars.contains(e.getTo());
		return new BooleanWrapper(ok, getDescription());
	}


}
