package view.action.newactions;

import java.awt.Component;

import universe.preferences.JFLAPPreferences;

import model.grammar.Grammar;

public class NewGrammarAction extends NewAction<Grammar> {

	public NewGrammarAction() {
		super("Grammar");
	}

	@Override
	public Grammar createNewModel() {
		Grammar g = new Grammar();
		if (JFLAPPreferences.isCustomMode())
			g.setVariableGrouping(JFLAPPreferences.getVariableGrouping());
		return g;
	}

}
