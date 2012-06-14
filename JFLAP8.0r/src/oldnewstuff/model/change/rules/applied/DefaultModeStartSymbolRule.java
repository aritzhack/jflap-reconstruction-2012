package oldnewstuff.model.change.rules.applied;

import oldnewstuff.model.change.ChangeEvent;
import oldnewstuff.model.change.events.SetToEvent;
import oldnewstuff.model.change.rules.Rule;
import preferences.JFLAPPreferences;
import errors.BooleanWrapper;
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
