package model.formaldef.components.functionset.function;

import errors.BooleanWrapper;
import util.Copyable;
import model.change.ChangeApplyingObject;
import model.change.ChangeDistributingObject;
import model.change.ChangeEvent;
import model.change.rules.applied.LanguageFunctionRule;
import model.formaldef.Describable;
import model.formaldef.FormalDefinitionException;
import model.formaldef.UsesSymbols;
import model.formaldef.components.SetSubComponent;
import model.grammar.GrammarException;

public abstract class LanguageFunction<T extends LanguageFunction<T>>
			extends SetSubComponent<T> implements UsesSymbols{
	
	private LanguageFunctionRule<T> myRule;
	
	@Override
	public boolean applyChange(ChangeEvent e) {
		if (myRule != null && myRule.appliesTo(e)){
			BooleanWrapper bw = myRule.checkRule(e);
			if (bw.isError())
				throw new FormalDefinitionException(bw.getMessage());
		}
			
		return super.applyChange(e);
	}
	
	
	protected LanguageFunctionRule<T> getRule(){
		return myRule;
	}
	public void setFunctionRule(LanguageFunctionRule<T> rule) {
		myRule = rule;
	}

}
