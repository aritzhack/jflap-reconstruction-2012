package model.formaldef;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import errors.BooleanWrapper;

import jflap.model.formaldef.alphabets.Alphabet;
import jflap.model.formaldef.symbols.Symbol;

public abstract class FormalDefinition implements Describable{

	private LinkedList<FormalDefinitionComponent> myComponents;
	
	public FormalDefinition(FormalDefinitionComponent ... components) {
		myComponents = new LinkedList<FormalDefinitionComponent>(Arrays.asList(components));
	}
	
	public String toNtupleString(){
		String out = this.getName() + " = ( ";
		
		for (FormalDefinitionComponent comp : this.getComponents()){
			out += comp.getCharacterAbbr() + ", ";
		}
		out += ")";
		out.replaceAll(", )", " )");
		
		return out;
	}
	
	@Override
	public String toString() {
		String out = this.toNtupleString();
		
		for (FormalDefinitionComponent comp : this.getComponents()){
			out += "\t" + comp.toString() + "\n";
		}
		
		return out;
	}

	public FormalDefinitionComponent[] getComponents() {
		return myComponents.toArray(new FormalDefinitionComponent[0]);
	}
	
	public <T extends FormalDefinitionComponent> T replaceComponent(T newComp){
		T oldComp = (T) this.getComponentOfClass(newComp.getClass());
		
		if (oldComp != null){
			int index = myComponents.indexOf(oldComp);
			myComponents.remove(oldComp);
			myComponents.add(index, newComp);
		}
		
		return oldComp;
				
	}

	public <T extends FormalDefinitionComponent> T getComponentOfClass(Class<T> clz) {
		for (FormalDefinitionComponent comp: this.getComponents()){
			if (clz.isAssignableFrom(comp.getClass()))
				return clz.cast(comp);
		}
		return null;
	}
	
	public BooleanWrapper isComplete() {
		BooleanWrapper amComplete = new BooleanWrapper(true);
		for (Alphabet alph: getAlphabets()){
			amComplete = alph.isComplete();
			if (amComplete.isFalse())
				return amComplete;
		}
		return amComplete;
	}
	
}
