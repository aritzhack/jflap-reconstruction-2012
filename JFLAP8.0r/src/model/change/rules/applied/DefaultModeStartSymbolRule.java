package model.change.rules.applied;

import preferences.JFLAPPreferences;
import errors.BooleanWrapper;
import model.change.ChangeEvent;
import model.change.events.SetToEvent;
import model.change.rules.Rule;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.Variable;
import model.grammar.StartVariable;


public class DefaultModeStartSymbolRule extends Rule {

	public DefaultModeStartSymbolRule() {
		super(SET_TO);
	}

	@Override
	public String getDescriptionName() {
		return "Default Mode Start Variable Rule";
	}

	@Override
	public String getDescription() {
		return "The Start Variable can only be set to the default start variable, " +
					JFLAPPreferences.getDefaultStartVariable() + ".";
	}

	@Override
	public BooleanWrapper checkRule(ChangeEvent event) {
		SetToEvent<StartVariable, Symbol> e = (SetToEvent<StartVariable, Symbol>) event;
		boolean ok = e.getTo().equals(JFLAPPreferences.getDefaultStartVariable());
		return new BooleanWrapper(ok, getDescription());
	}

}
