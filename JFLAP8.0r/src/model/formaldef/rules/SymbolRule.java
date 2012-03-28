package model.formaldef.rules;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;




public abstract class SymbolRule<T extends FormalDefinition, S extends Alphabet> implements IRule<T, S> {
	
	
	public boolean shouldBeApplied(T parent, S a){
		boolean b=  this.getApplicableAlphType().isAssignableFrom(a.getClass()) &&
				this.getApplicableDefType().isAssignableFrom(parent.getClass());
		return b;
	}
	
	public abstract String getName();

	public Class<Alphabet> getApplicableAlphType(){
		return (Class<Alphabet>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}
	
	public Class<FormalDefinition> getApplicableDefType(){
		return (Class<FormalDefinition>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public String toString(){
		return this.getName() + ": " + this.getDescription();
	}
}
