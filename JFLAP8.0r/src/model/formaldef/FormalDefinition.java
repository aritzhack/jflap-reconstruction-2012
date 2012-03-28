package model.formaldef;

import java.lang.reflect.InvocationTargetException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JOptionPane;


import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.AlphabetActionType;
import model.formaldef.components.alphabets.AlphabetException;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.functionset.FunctionSet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.formaldef.rules.JFLAPRulebook;

import errors.BooleanWrapper;


public abstract class FormalDefinition<T extends Alphabet, S extends FunctionSet> implements Describable, 
																								UsesSymbols{


	private T myLanguageAlphabet;


	private S myFunctionSet;


	public FormalDefinition(T langAlph, 
			S functions,
			FormalDefinitionComponent ... components) {
		myLanguageAlphabet = langAlph;
		myFunctionSet = functions;
		setAuxilliaryComponents(components);
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

	public FormalDefinitionComponent[] getComponents(){
		return new FormalDefinitionComponent[]{this.getLanguageAlphabet(),
				this.getFunctionSet()};
	}

	private T getLanguageAlphabet() {
		return myLanguageAlphabet;
	}

	private S getFunctionSet() {
		return myFunctionSet;
	}

	public BooleanWrapper purgeAndRemoveSymbol(Class<? extends Alphabet> cls, Symbol symbol){
		return BooleanWrapper.combineWrappers(
				purgeSymbol(cls, symbol), 
				removeSymbol(cls, symbol));
	}

	public Set<Symbol> getUnusedSymbols() {
		Set<Symbol> symbols = this.getAllSymbolsInAlphabets();
		symbols.removeAll(this.getSymbolsUsed());
		return symbols;
	}

	public Collection<Symbol> getSymbolsUsed() {
		TreeSet<Symbol> used = new TreeSet<Symbol>();
		
		for (FormalDefinitionComponent f: this.getComponents()){
			if (f instanceof UsesSymbols)
				used.addAll(((UsesSymbols) f).getUniqueSymbolsUsed());
		}

		return used;
	}

	public FormalDefinition alphabetAloneCopy(){
		try {
			FormalDefinition fd = (FormalDefinition) getClass().newInstance();
			for (Alphabet alph: getAlphabets()){

				for (Symbol s: alph){
					fd.addSymbol(alph.getClass(), s.clone());
				}
			}
			return fd;
		} catch (Exception e) {
			throw new AlphabetException("Formal Definition clone failed.");
		}
	}

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

	public abstract BooleanWrapper purgeSymbol(Class<? extends Alphabet> cls, Symbol s);

	public abstract void setAuxilliaryComponents(FormalDefinitionComponent[] components);


}
