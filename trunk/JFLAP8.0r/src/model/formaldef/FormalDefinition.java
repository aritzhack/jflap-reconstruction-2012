package model.formaldef;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import model.formaldef.alphabets.Alphabet;
import model.formaldef.alphabets.AlphabetActionType;
import model.formaldef.alphabets.AlphabetException;
import model.formaldef.rules.JFLAPRulebook;
import model.formaldef.symbols.Symbol;

import errors.BooleanWrapper;


public abstract class FormalDefinition implements Describable{

	
	public FormalDefinition(FormalDefinitionComponent ... components) {
		setComponents(components);
	}
	
	public abstract void setComponents(FormalDefinitionComponent[] components);

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

	public abstract FormalDefinitionComponent[] getComponents();
	
	public <T extends FormalDefinitionComponent> T getComponentOfClass(Class<T> clz) {
		for (FormalDefinitionComponent comp: this.getComponents()){
			if (clz.isAssignableFrom(comp.getClass()))
				return clz.cast(comp);
		}
		return null;
	}
	
	public BooleanWrapper[] isComplete() {
		ArrayList<BooleanWrapper> incomplete = new ArrayList<BooleanWrapper>();
		for (FormalDefinitionComponent comp: this.getComponents()){
			BooleanWrapper amComplete = comp.isComplete();
			if (amComplete.isFalse())
				incomplete.add(amComplete);
		}
		return incomplete.toArray(new BooleanWrapper[0]);
	}
	
	private BooleanWrapper doGenericAction(AlphabetActionType action,
			Class<? extends Alphabet> type, Symbol ... symbols) {
		Alphabet a = this.getComponentOfClass(type);
		if (a == null){
			throw new AlphabetException("No alphabet of the type " + type + " exists in this " + this.getClass());
		}
		BooleanWrapper bw = JFLAPRulebook.checkRules(action, this, a, symbols);
		if (bw.isTrue()){
			switch (action){
			case ADD: a.add(symbols[0]); break;
			case MODIFY: a.modify(symbols[0], symbols[1]); break;
			case REMOVE: a.remove(symbols[0]); break;
			default: throw new AlphabetException("No action specified");
			}
		}
		return bw;
	}

	public BooleanWrapper addSymbol(Class<? extends Alphabet> type, Symbol newSymbol){
		return doGenericAction(AlphabetActionType.ADD, type, newSymbol);
	}
	
	public BooleanWrapper modifySymbol(Class<? extends Alphabet> type, Symbol oldSymbol, Symbol newSymbol){
		return doGenericAction(AlphabetActionType.MODIFY, type, oldSymbol, newSymbol);
	}

	public BooleanWrapper removeSymbol(Class<? extends Alphabet> class1, Symbol newSymbol){
		return doGenericAction(AlphabetActionType.REMOVE, class1, newSymbol);
	}

	public ArrayList<Character> getDisallowedCharacters() {
		return new ArrayList<Character>(Arrays.asList(new Character[]{' '}));
	}

	public AbstractCollection<Alphabet> getAlphabets() {
		AbstractCollection<Alphabet> alphs = new ArrayList<Alphabet>();
		
		for (FormalDefinitionComponent comp : this.getComponents()){
			if (comp instanceof Alphabet){
				alphs.add((Alphabet) comp);
			}
		}
		
		
		return alphs;
	}

	
}
