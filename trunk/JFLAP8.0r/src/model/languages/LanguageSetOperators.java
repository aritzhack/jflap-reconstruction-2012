package model.languages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

/**
 * Operators that may be performed on sets of strings in languages
 * or symbols that comprise the language's alphabet
 * 
 * Separate from {@link SetOperators} because not all operators
 * necessarily can be applied to all objects in a set
 * 
 * @author Peggy Li
 *
 */

public class LanguageSetOperators {


	/*
	 * Currently works only with SymbolStrings
	 */

	@SuppressWarnings("unchecked")
	public synchronized static <T extends SymbolString> Set<T> kleeneStar(Set<T> set, int count) {

		Collection<T> concats = new ArrayList<T>(set);
		ArrayList<T> prev = new ArrayList<T>();
		loop: while (true) {
			for (T a : set) {
				prev.addAll(set);
				ArrayList<T> temp = new ArrayList<T>();
				for (T b : prev) {
					temp.add((T) new SymbolString(a).concat(b));
				}
				if (concats.size() >= count)
					break loop;

				concats.addAll(temp);
				prev.clear();
				prev.addAll(temp);
			}
		}
		Set<T> result = new TreeSet<T>();
		result.add((T) new SymbolString());
		result.addAll(concats);
		return result;

	}

	@SuppressWarnings("unchecked")
	public static <T extends SymbolString> Set<T> homomorphism (Set<T> set, Symbol original, Symbol replaceWith) {
		Set<T> result = new HashSet<T>();
		for (SymbolString s : set) {
			T temp = (T) new SymbolString(s);
			temp.replaceAll(original, replaceWith);
			result.add(temp);
		}
		return result;
	}
	
	
	public static void main (String[] args) {
		Set<SymbolString> set = new TreeSet<SymbolString>();
		set.add(new SymbolString(new Symbol("a")));
		set.add(new SymbolString(new Symbol("b")));
		System.out.println(kleeneStar(set, 5));
	}


}
