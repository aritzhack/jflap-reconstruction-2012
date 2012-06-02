package model.formaldef;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.Copyable;

import model.formaldef.components.ChangeTypes;
import model.formaldef.components.ComponentChangeEvent;
import model.formaldef.components.ComponentChangeListener;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.FunctionSet;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.rules.applied.DisallowedCharacterRule;
import model.util.JFLAPConstants;
import errors.BooleanWrapper;


public abstract class FormalDefinition extends ChangingObject implements Describable, 
																			UsesSymbols, 
																			ChangeListener, 
																			ChangeTypes,
																			JFLAPConstants,
																			Copyable{

	private LinkedList<FormalDefinitionComponent> myComponents;


	public FormalDefinition(FormalDefinitionComponent ... comps) {
		myComponents = new LinkedList<FormalDefinitionComponent>();
		for (FormalDefinitionComponent comp : comps){
			myComponents.add(comp);
			comp.addListener(this);
		}
		for (Alphabet a: this.getAlphabets())
			a.addRules(new DisallowedCharacterRule(this));
	}

	public String toNtupleString(){
		String out = this.getDescriptionName() + " = (";

		for (FormalDefinitionComponent comp : this.getComponents()){
			out += comp.getCharacterAbbr() + ", ";
		}

		out = out.substring(0,out.length()-2)+")";

		return out;
	}

	@Override
	public String toString() {
		String out = this.toNtupleString() + "\n";

		for (FormalDefinitionComponent comp : this.getComponents()){
			out += "\t" + comp.toString() + "\n";
		}

		return out;
	}

	public <T extends FormalDefinitionComponent> T getComponentOfClass(Class<T> clz) {
		for (FormalDefinitionComponent comp: this.getComponents()){
			if (clz.isAssignableFrom(comp.getClass()))
				return clz.cast(comp);
		}
		return null;
	}

	public void trimAlphabets(){
		Set<Symbol> used = this.getUniqueSymbolsUsed();
		for (Alphabet a: this.getAlphabets()){
			a.retainAll(used);
		}
	}

//	@Override
//	public FormalDefinition copy() {
//		ArrayList<FormalDefinitionComponent> cloned = new ArrayList<FormalDefinitionComponent>();
//		for (FormalDefinitionComponent comp : this.getComponents())
//			cloned.add(comp.copy());
//		cloned.trimToSize();
//		try {
//			return (FormalDefinition) this.getClass().getConstructors()[0].newInstance(cloned.toArray());
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		} 
//	}

	public BooleanWrapper[] isComplete() {
		ArrayList<BooleanWrapper> incomplete = new ArrayList<BooleanWrapper>();
		for (FormalDefinitionComponent comp: this.getComponents()){
			BooleanWrapper amComplete = comp.isComplete();
			if (amComplete.isError())
				incomplete.add(amComplete);
		}
		return incomplete.toArray(new BooleanWrapper[0]);
	}


	/**
	 * Retrieves all of the characters disallowed in this formal definition.
	 * Those are characters which are used in other parts of the definition
	 * which cannot be confused for other symbols.
	 * 
	 * @return
	 */
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

	/**
	 * Retrieves all of the {@link FormalDefinitionComponent}s in order
	 * as they should be in the n-tuple of this {@link FormalDefinition}.
	 * THIS METHOD MUST BE OVERRIDDEN UPON CHANGING THE COMPONENTS IN THE
	 * FORMAL DEFINTION, I.E. SUBCLASSING.
	 * 
	 * @return all of the {@link FormalDefinitionComponent} in this 
	 * 											{@link FormalDefinition}.
	 */
	public FormalDefinitionComponent[] getComponents(){
		return myComponents.toArray(new FormalDefinitionComponent[0]);
	}

	public Set<Symbol> getUnusedSymbols() {
		Set<Symbol> symbols = this.getAllSymbolsInAlphabets();
		symbols.removeAll(this.getUniqueSymbolsUsed());
		return symbols;
	}

	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		TreeSet<Symbol> used = new TreeSet<Symbol>();

		for (FormalDefinitionComponent f: this.getComponents()){
			if (f instanceof UsesSymbols)
				used.addAll(((UsesSymbols) f).getUniqueSymbolsUsed());
		}

		return used;
	}

	public Set<Symbol> getAllSymbolsInAlphabets() {
		Set<Symbol> symbols = new HashSet<Symbol>();
		for (Alphabet alph: getAlphabets()){
			symbols.addAll(alph);
		}
		return symbols;
	}

	@Override
	public boolean purgeOfSymbol(Symbol s){
		boolean result = false;
		for (FormalDefinitionComponent f: this.getComponents()){
			if (f instanceof UsesSymbols)
				result = ((UsesSymbols) f).purgeOfSymbol(s) || result;
		}
		this.distributeChanged();
		return result;
	}

	public abstract FormalDefinition alphabetAloneCopy();

	@Override
	public void stateChanged(ChangeEvent event) {
		this.componentChanged((ComponentChangeEvent) event);
	}

	public void componentChanged(ComponentChangeEvent event){
		for (Alphabet a: this.getAlphabets()){
			if (event.comesFrom(a)){
				switch (event.getType()){
				case ITEM_REMOVED: 
					this.purgeOfSymbol((Symbol) event.getArg(0));

				}
			}
		}
	}

}