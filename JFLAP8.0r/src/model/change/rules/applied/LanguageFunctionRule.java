package model.change.rules.applied;

import errors.BooleanWrapper;
import model.change.ChangeEvent;
import model.change.events.SetToEvent;
import model.change.rules.Rule;
import model.formaldef.components.functionset.function.LanguageFunction;

public abstract class LanguageFunctionRule<T extends LanguageFunction<T>> extends Rule {

	public LanguageFunctionRule() {
		super(SET_TO);
	}

	@Override
	public BooleanWrapper checkRule(ChangeEvent event) {
		return checkRule(((SetToEvent<T, T>) event).getTo());
	}

	
	public abstract BooleanWrapper checkRule(T func);
}
