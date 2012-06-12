package model.languages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;

/**
 * Operators that may be performed on a language (set of strings) but not
 * necessarily on sets consisting of generic objects
 * 
 * Note: works only with sets of SymbolStrings. Strings, Symbols, etc. should be
 * converted to SymbolStrings first.
 * 
 * @author Peggy Li
 * 
 */


public class LanguageSetOperators {


	public static Collection<SymbolString> kleeneStar (Collection<SymbolString> strings, int count) {
		Collection<SymbolString> results = new ArrayList<SymbolString>(strings);
		ArrayList<SymbolString> prev = new ArrayList<SymbolString>();
		prev.addAll(strings);

		loop: while (true) {
			for (SymbolString s : strings) {

				ArrayList<SymbolString> temp = new ArrayList<SymbolString>();
				for (SymbolString p : prev) {
					temp.add(new SymbolString(s).concat(p));
				}

				if (results.size() >= count)
					break loop;

				results.addAll(temp);
				prev.clear();
				prev.addAll(temp);
			}
		}

		results.add(new SymbolString());
		return results;
	}

	/**
	 * 
	 * 
	 * @param symbols
	 *            the characters in an alphabet
	 * @param count
	 *            number of strings to generate
	 * @return <code>count</code> number of strings formed from Kleene Star
	 * 
	 */

	public static Collection<SymbolString> kleeneStar(int count, Collection<Symbol> symbols) {
		Collection<SymbolString> symbolsToStrings = new ArrayList<SymbolString>();
		for (Symbol sym : symbols) {
			symbolsToStrings.add(new SymbolString(sym));
		}

		return kleeneStar(symbolsToStrings, count);
	}


	public static Collection<SymbolString> homomorphism(Set<SymbolString> strings, Symbol original, Symbol replaceWith) {
		Collection<SymbolString> results = new ArrayList<SymbolString>();
		for (SymbolString s : strings) {
			SymbolString replaced = s.replaceAll(original, replaceWith);
			results.add(replaced);
		}
		return results;
	}


	public static Collection<SymbolString> concatenate(Set<SymbolString> lang1, Set<SymbolString> lang2) {
		Collection<SymbolString> results = new ArrayList<SymbolString>();
		for (SymbolString a : lang1) {
			for (SymbolString b : lang2) {
				SymbolString copy = new SymbolString(a);
				results.add(copy.concat(b));
			}
		}
		return results;
	}


	public static void main(String[] args) {
		Set<SymbolString> one = new TreeSet<SymbolString>();
		one.add(new SymbolString(new Symbol("a")));
		one.add(new SymbolString(new Symbol("b")));

		Set<SymbolString> two = new TreeSet<SymbolString>();
		two.add(new SymbolString(new Symbol("cc")));
		two.add(new SymbolString(new Symbol("dd")));

		//System.out.println(concatenate(one, two));
		System.out.println(homomorphism(one, new Symbol("a"), new Symbol("x")));
	}

}
