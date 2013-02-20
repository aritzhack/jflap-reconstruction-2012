package model.lsystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import model.symbols.Symbol;
import model.symbols.SymbolString;

public class NExpander{
	private NLSystem lsystem;
	private Random stochiastic;
	private List<SymbolString> cachedExpansions;
	private Context[] contexts;
	
	private static final Random RANDOM = new Random();
	
	public NExpander (NLSystem lsystem){
		this(lsystem, RANDOM.nextLong());
	}
	
	public NExpander (NLSystem lsystem, long seed){
		stochiastic = new Random(seed);
		this.lsystem = lsystem;
		cachedExpansions = new ArrayList<SymbolString>();
		cachedExpansions.add(lsystem.getAxiom());
		initializeContexts();
	}
	
	private void initializeContexts(){
		Set<SymbolString> replacementSet = lsystem.getSymbolStringsWithReplacements();
		List<Context> contextList = new ArrayList<Context>();
		boolean hasContexts = false;
		
		for(SymbolString s : replacementSet){
			SymbolString[] replacements = lsystem.getReplacements(s);
			int context = 0;
			
			switch(s.size()){
			case 0: //In case of empty string
				continue;
			case 1:
				break;
			default: //Multiple symbols
				try{
					context = Integer.parseInt(s.get(0).getString());
					s.get(context+1); //Finley said to check
				} catch (NumberFormatException e){
					//Not a number in the first symbol
					continue;
				} catch (IndexOutOfBoundsException e){
					//The number is out of bounds.
					continue;
				}
				hasContexts = true;
				s = s.subList(1);
			}
			contextList.add(new Context (s, context, replacements));
		}
		if (hasContexts)
			contexts = contextList.toArray(new Context[0]);
	}
	
	/**
	 * Returns the expansion at a given level of recursion. An input of 0 will
	 * return the axiom (i.e., no replacement or recursion has occurred).
	 * 
	 * @param level
	 *            the level of recursion to sink to
	 * @return the list of string symbols
	 * @throws IllegalArgumentException
	 *             if the level is less than 0
	 */
	public SymbolString expansionForLevel(int level) {
		if (level < 0)
			throw new IllegalArgumentException("Recursion level " + level
					+ " impossible!");
		if (level < cachedExpansions.size())
			return (SymbolString) cachedExpansions.get(level);
		SymbolString lastOne = cachedExpansions.get(cachedExpansions.size() - 1);
		for (int i = cachedExpansions.size(); i <= level; i++)
			cachedExpansions.add(lastOne = expand(lastOne));
		return lastOne;
	}
	
	/**
	 * Does the expansion of a given SymbolString.
	 * 
	 * @param symbols
	 *            the list of symbols to expand
	 * @return the expansion of the passed in symbols
	 */
	private SymbolString expand(SymbolString symbols) {
		if (contexts == null)
			return expandNoContext(symbols);
		return expandContext(symbols);
	}
	
	/**
	 * Does the expansion of a given string list thing given that we have no
	 * "contexts" to worry about.
	 * 
	 * @param symbols
	 *            the list of symbols to expand
	 * @return the expansion of the passed in symbols
	 */
	private SymbolString expandNoContext(SymbolString symbols) {
		SymbolString newExpansion = new SymbolString();
		for (int i = 0; i < symbols.size(); i++) {
			Symbol s = symbols.get(i);
			SymbolString[] replacements = lsystem.getReplacements(new SymbolString(s));
			SymbolString newReplacement;
			switch (replacements.length) {
			case 0:
				// This cannot be replaced, so we skip to the next symbol.
				newExpansion.add(s);
				continue;
			case 1:
				// There is only one replacement possibility.
				newReplacement = replacements[0];
				break;
			default:
				// If there's more than one possibility, we choose one
				// nearly at random.
				newReplacement = replacements[stochiastic
						.nextInt(replacements.length)];
				break;
			}
			newExpansion.addAll(newReplacement); // Add replacements!
		}
		return newExpansion;
	}
	
	/**
	 * Does the expansion of a given string list thing given that we have
	 * contexts. This can be computationally more expensive, though not horribly
	 * so.
	 * 
	 * @param symbols
	 *            the list of symbols to expand
	 * @return the expansion of the passed in symbols
	 */
	private SymbolString expandContext(SymbolString symbols) {
		SymbolString newExpansion = new SymbolString();
		for (int i = 0; i < symbols.size(); i++) {
			Symbol s = symbols.get(i);
			ArrayList<SymbolString> replacementsList = new ArrayList<SymbolString>();
			for (int j = 0; j < contexts.length; j++) {
				SymbolString[] l = contexts[j].matches(symbols, i);
				for (int k = 0; k < l.length; k++)
					replacementsList.add(l[k]);
			}
			SymbolString[] replacements = replacementsList
					.toArray(new SymbolString[0]);
			SymbolString newReplacement;
			switch (replacements.length) {
			case 0:
				// This cannot be replaced, so we skip to the next symbol.
				newExpansion.add(s);
				continue;
			case 1:
				// There is only one replacement possibility.
				newReplacement = replacements[0];
				break;
			default:
				// If there's more than one possibility, we choose one
				// nearly at random.
				newReplacement = replacements[stochiastic
						.nextInt(replacements.length)];
				break;
			}
			newExpansion.addAll(newReplacement); // Add replacements!
		}
		return newExpansion;
	}
	
	
	/**
	 * This is a class that is used to perform limited matchings of a list.
	 */
	private class Context {
		private SymbolString symbols;
		private int center;
		private SymbolString[] results;
		
		/**
		 * Instantiates a given context list.
		 * 
		 * @param tokens
		 *            the list of tokens
		 * @param center
		 *            the index of the "center" token
		 * @param results
		 *            the results of matching
		 */
		public Context(SymbolString tokens, int center, SymbolString[] results) {
			this.symbols = tokens;
			this.center = center;
			this.results = results;
		}

		/**
		 * Given an input list, checks to see if it partially matches with the
		 * center of the input list and the center of this list.
		 * 
		 * @param list
		 *            the list of tokens we shoudl check this against
		 * @return the resulting replacement lists for the center token if there
		 *         was a match, or an empty array otherwise
		 */
		public SymbolString[] matches(SymbolString list, int centerList) {
			centerList -= center;
			try {
				List sub = list.subList(centerList, centerList + symbols.size());
				if (sub.equals(symbols))
					return results;
			} catch (IndexOutOfBoundsException e) {
			}
			return new SymbolString[0];
		}

		/**
		 * Returns a string description of this context.
		 * 
		 * @return a string description of this context
		 */
		public String toString() {
			StringBuffer sb = new StringBuffer(super.toString());
			sb.append(" : symbols(");
			sb.append(symbols);
			sb.append(") at ");
			sb.append(center);
			sb.append(" with ");
			sb.append(Arrays.asList(results));
			return sb.toString();
		}

		
	}
}