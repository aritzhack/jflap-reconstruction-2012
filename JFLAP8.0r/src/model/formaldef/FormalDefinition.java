package model.formaldef;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observer;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import preferences.JFLAPPreferences;

import util.Copyable;


import model.JFLAPConstants;
import model.change.ChangeApplyingObject;
import model.change.ChangeEvent;
import model.change.ChangeListener;
import model.change.ChangeTypes;
import model.change.ChangeDistributingObject;
import model.change.events.TrimAlphabetsEvent;
import model.change.interactions.Interaction;
import model.change.rules.applied.DefaultModeInUseRule;
import model.change.rules.applied.DisallowedCharacterRule;
import model.change.rules.applied.SelfIdenticalRule;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.SetComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.FunctionSet;
import model.formaldef.components.symbols.Symbol;
import model.undo.IUndoRedo;
import errors.BooleanWrapper;


public abstract class FormalDefinition extends ChangeApplyingObject implements Describable, 
JFLAPConstants,
ChangeTypes,
UsesSymbols,
Copyable{

	private LinkedList<FormalDefinitionComponent> myComponents;
//	private boolean amPurging;
//	private PurgeEvent myPurgeEvent;


	public FormalDefinition(FormalDefinitionComponent ... comps) {
		myComponents = new LinkedList<FormalDefinitionComponent>();
		for (FormalDefinitionComponent comp : comps){
			myComponents.add(comp);
		}

		updateRulesAndInteractions();
	}

	public void updateRulesAndInteractions() {
		this.clearInteractionsAndRules();
		if (JFLAPPreferences.isUserDefinedMode())
			this.setUpUserDefinedRulesAndInteractions();
		else
			this.setUpDefaultRulesAndInteractions();
		//add disallowedCharacterRule to all
		for (Alphabet a: this.getAlphabets()){
			a.addRules(new DisallowedCharacterRule(ITEM_ADD, this),
						new DisallowedCharacterRule(ITEM_MODIFY, this));
		}
	}

	public void setUpDefaultRulesAndInteractions(){
		for (Alphabet a: this.getAlphabets()){
			a.addRules(new DefaultModeInUseRule(ITEM_ADD, a, this),
					new DefaultModeInUseRule(ITEM_REMOVE, a, this),
					new DefaultModeInUseRule(ITEM_MODIFY, a, this));
		}
	}

	public abstract void setUpUserDefinedRulesAndInteractions();

	private void clearInteractionsAndRules() {
		for (FormalDefinitionComponent comp: this.getComponents()){
			comp.clearInteractions();
			comp.clearRules();
		}
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

	public boolean trimAlphabets(){
		TreeMap<Alphabet, Set<Symbol>> trimMap = new TreeMap<Alphabet, Set<Symbol>>();
		for (Alphabet alph: this.getAlphabets()){
			Set<Symbol> unused = new TreeSet<Symbol>(alph);
			unused.removeAll(getUniqueSymbolsUsed(alph));
			trimMap.put(alph, unused);
		}
		return this.applyChange(new TrimAlphabetsEvent(this, trimMap));
	}
	
	/**
	 * Retrieve the symbols used by this formal definition that apply
	 * to the alphabet alph, i.e. that are actually in the alphabet.
	 * This is used primarily for trimming.
	 * 
	 * @param alph
	 * @return
	 */
	public abstract Set<? extends Symbol> getUniqueSymbolsUsed(Alphabet alph);
	
	@Override
	public Set<Symbol> getUniqueSymbolsUsed() {
		Set<Symbol> used = new TreeSet<Symbol>();
		for (Alphabet a: this.getAlphabets()){
			used.addAll(getUniqueSymbolsUsed(a));
		}
		return used;
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

	public Collection<Alphabet> getAlphabets() {
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

//	public Set<Symbol> getUnusedSymbols() {
//		Set<Symbol> symbols = this.getAllSymbolsInAlphabets();
//		symbols.removeAll(this.getUniqueSymbolsUsed());
//		return symbols;
//	}

//	@Override
//	public Set<Symbol> getUniqueSymbolsUsed() {
//		TreeSet<Symbol> used = new TreeSet<Symbol>();
//
//		for (FormalDefinitionComponent f: this.getComponents()){
//			if (f instanceof UsesSymbols)
//				used.addAll(((UsesSymbols) f).getUniqueSymbolsUsed());
//		}
//
//		return used;
//	}

	public Set<Symbol> getAllSymbolsInAlphabets() {
		Set<Symbol> symbols = new HashSet<Symbol>();
		for (Alphabet alph: getAlphabets()){
			symbols.addAll(alph);
		}
		return symbols;
	}

//	@Override
//	public void applyModification(Symbol from, Symbol to) {
//		for (FormalDefinitionComponent f: this.getComponents()){
//			if (f instanceof UsesSymbols)
//				((UsesSymbols) f).applyModification(from, to);
//		}
//	}
	
//	@Override
//	public boolean purgeOfSymbol(Alphabet a, Symbol s){
//		boolean result = false;
////		myPurgeEvent = new PurgeEvent(this, new RemoveEvent<Symbol>(a, s));
////		amPurging = true;
//		for (FormalDefinitionComponent f: this.getComponents()){
//			if (f instanceof UsesSymbols)
//				result = ((UsesSymbols) f).purgeOfSymbol(a, s) || result;
//		}
////		amPurging = false;
////		this.distributeChange(myPurgeEvent);
////		myPurgeEvent = null;
//		return result;
//	}

	public abstract FormalDefinition alphabetAloneCopy();

//	@Override
//	public void stateChanged(ChangeEvent event) {
//		this.componentChanged(event);
//	}

//	public void componentChanged(ChangeEvent event){
//		if (amPurging && event instanceof IUndoRedo){
//			myPurgeEvent.addSubEvents((IUndoRedo) event);
//			return;
//		}
//		
//		if (event instanceof ModifySymbolEvent){
//			ModifySymbolEvent e = (ModifySymbolEvent) event;
//			this.applyModification(e.getFrom(), e.getTo());
//
//		}
//			
//		if (event instanceof RemoveEvent){
//			for (Alphabet alph: getAlphabets()){
//				if (event.comesFrom(alph)){
//					RemoveEvent<Symbol> e = (RemoveEvent<Symbol>) event;
//					this.purgeOfSymbol(alph, e.getToRemove());
//				}
//			}
//		}
//	}

}