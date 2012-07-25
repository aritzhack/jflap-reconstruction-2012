package model.languages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import model.algorithms.testinput.parse.Derivation;
import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class BruteLanguageGenerator extends LanguageGenerator {

	private Queue<Derivation> myDerivationQueue;
	private Set<SymbolString> mySententialsSeen;
	private int maxLHSsize;

	public BruteLanguageGenerator(Grammar g) {
		super(g);
	}

	@Override
	protected void initialize(Grammar g) {
		super.initialize(g);
		myDerivationQueue = new LinkedList<Derivation>();
		mySententialsSeen = new HashSet<SymbolString>();
		maxLHSsize = g.getProductionSet().getMaxLHSLength();
	}

	@Override
	protected void clear() {
		super.clear();
		myDerivationQueue.clear();
		mySententialsSeen.clear();
	}

	@Override
	public void generateStrings() {
		for (Production p : getGrammar().getStartProductions()) {
			Derivation d = new Derivation(p);
			myDerivationQueue.add(d);

			SymbolString sentential = d.createResult();
			if (sentential.getSymbolsOfClass(Variable.class).size() == 0
					&& getStringsInLanguage().size() < getNumberToGenerate()) {
				mySententialsSeen.add(sentential);
				addStringToLanguage(sentential);
			}
		}

		while (getStringsInLanguage().size() < getNumberToGenerate()
				&& !myDerivationQueue.isEmpty()) {
			makeNextReplacement();
		}
	}

	/**
	 * Brute force method that generates strings as it finds them, based on the
	 * BruteParser algorithm.
	 */
	private void makeNextReplacement() {
		ArrayList<Derivation> temp = new ArrayList<Derivation>();

		while (!myDerivationQueue.isEmpty()) {
			Derivation d = myDerivationQueue.poll();
			SymbolString result = d.createResult();

			for (int i = 0; i < result.size(); i++) {
				for (int j = i; j < Math.min(maxLHSsize + i, result.size()); j++) {
					SymbolString LHS = result.subList(i, j + 1);

					Production[] productionsWithLHS = getGrammar()
							.getProductionSet().getProductionsWithLHS(LHS);
					for (Production p : productionsWithLHS) {

						if (getStringsInLanguage().size() >= getNumberToGenerate())
							return;

						Derivation tempDerivation = d.copy();
						tempDerivation.addStep(p, result.indexOf(LHS, i));
						SymbolString sentential = tempDerivation.createResult();

						if (!mySententialsSeen.contains(sentential)) {
							temp.add(tempDerivation);
							mySententialsSeen.add(sentential);

							if (sentential.getSymbolsOfClass(Variable.class)
									.size() == 0) {
								addStringToLanguage(sentential);
							}
						}
					}
				}
			}
		}
		myDerivationQueue.addAll(temp);
	}

	@Override
	public void generateStringsOfLength(int length) {
		// TODO: ???
		return;
	}

}
