package model.formaldef;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.functionset.FunctionSet;
import model.formaldef.rules.applied.DisallowedCharacterRule;
import errors.BooleanWrapper;


public abstract class FormalDefinition<T extends Alphabet, S extends FunctionSet> implements Describable, 
																								UsesSymbols{


	private T myLanguageAlphabet;


	private S myFunctionSet;


	public FormalDefinition(T langAlph, S functions) {
		myLanguageAlphabet = langAlph;
		myFunctionSet = functions;
		myLanguageAlphabet.addRules(new DisallowedCharacterRule(this));
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
			out += "\t" + comp.getDescriptionName() + ": " + comp.toString() + "\n";
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

	@Override
	public FormalDefinition clone() {
		ArrayList<FormalDefinitionComponent> cloned = new ArrayList<FormalDefinitionComponent>();
		for (FormalDefinitionComponent comp : this.getComponents())
			cloned.add(comp.clone());

		try {
			return (FormalDefinition) this.getClass().getConstructors()[0].newInstance(cloned.toArray(new FormalDefinition[0]));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
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
		return new FormalDefinitionComponent[]{this.getLanguageAlphabet(),
				this.getFunctionSet()};
	}

	public T getLanguageAlphabet() {
		return myLanguageAlphabet;
	}

	public S getFunctionSet() {
		return myFunctionSet;
	}

	public boolean purgeAndRemoveSymbol(Symbol symbol){
		
		boolean purged = purgeOfSymbol(symbol);
		boolean removed = removeSymbolFromAlphabets(symbol);
		
		return purged || removed;
	}

	private boolean removeSymbolFromAlphabets(Symbol symbol) {
		boolean removed = false; 
		for (Alphabet a: this.getAlphabets()){
			removed = a.remove(symbol) || removed;
		}
		
		return removed;
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

	public abstract FormalDefinition<T,S> alphabetAloneCopy();

//	public BooleanWrapper importAlphabetsFrom(FormalDefinition imp) {
//		if (!this.getClass().isAssignableFrom(imp.getClass())){
//			return;
//		}
//		for (Alphabet a: myAlphabets){
//			Alphabet other = imp.getAlphabetByClass(a.getClass());
//
//			//null check 
//			if (other == null) continue;
//
//			//adjust grouping
//			if (other instanceof IGrouping){
//				if (!((IGrouping) a).getGrouping().equals( 
//						((IGrouping) other).getGrouping())){
//					int i = JOptionPane.showConfirmDialog(null, 
//							"The imported alphabet uses different grouping than\n" +
//									"selected for the current " + a.getName() +".\n" +
//									"the grouping will be adjusted accordingly.",
//									"Import issue", 
//									JOptionPane.OK_CANCEL_OPTION);
//					if (i == 1) return;
//				}
//
//				//TODO: should there be an option shown here? For now, I will say NAY
//				((IGrouping) a).setGrouping(((IGrouping) other).getGrouping());
//			}
//			//add all symbols
//			for (Object s: other){
//				a.add(((Symbol)s).clone());
//			}
//		}
//
//	}

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
		
		return result;
	}

}
