package model.grammar.parsing.brute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.parsing.Derivation;
import model.grammar.parsing.Parser;
import model.grammar.parsing.ParserException;
import model.grammar.typetest.GrammarType;

/**
 * Brute force parser
 * 
 * Note: Brute force parser was re-implemented in Summer 2012
 * to conform with the new parser hierarchy and new classes.
 * 
 * @author Peggy Li
 * @author Julian Genkins
 * @author Ian McMahon
 * 
 */

public abstract class BruteParser extends Parser {

	private static final int INCREMENT_CAPACITY_BY = 5000;
	private int NODES_TO_GENERATE = 5000;

	private Queue<Derivation> myDerivationsQueue;
	private int myNodesGenerated;
	private Derivation myAnswerDerivation;

	public BruteParser(Grammar g) {
		super(g);
	}

	@Override
	public String getDescriptionName() {
		return "Brute Force Parser";
	}

	@Override
	public String getDescription() {
		return "Brute force parsing implementation";
	}

	@Override
	public boolean resetParserStateOnly() {
		myNodesGenerated = 0;
		initializeQueue();
		return true;
	}

	@Override
	public boolean isAccept() {
		for (Derivation d : myDerivationsQueue) {
			if (d.createResult().equals(getInput())) {
				myAnswerDerivation = d;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isDone() {
		return capacityReached() || isAccept() || myDerivationsQueue.isEmpty();
	}

	@Override
	public GrammarType getRequiredGrammarType() throws ParserException {
		return GrammarType.UNRESTRICTED;
	}
	
	@Override
	public Derivation getDerivation () {
		return myAnswerDerivation;
	}

	private boolean capacityReached() {
		return myNodesGenerated >= NODES_TO_GENERATE;
	}

	@Override
	public boolean stepParser() {

		makeNextReplacement();
		return true;
	}

	

	private void initializeQueue() {
		myDerivationsQueue = new LinkedList<Derivation>();

		SymbolString start = new SymbolString(getGrammar().getStartVariable());
		Derivation d = new Derivation(new Production(new SymbolString(), start));
		myDerivationsQueue.add(d);
		myNodesGenerated++;
	}


	private boolean makeNextReplacement() {

		// hold new Derivations, else queue not empty until end => cannot step
		ArrayList<Derivation> temp = new ArrayList<Derivation>();

		while (!myDerivationsQueue.isEmpty()) {
			Derivation d = myDerivationsQueue.poll();
			SymbolString result = d.createResult();

			for (int i = 0; i < result.size(); i++) {
				for (int j = i; j < result.size(); j++) {
					SymbolString LHS = result.subList(i, j + 1);
					for (Production p : getGrammar().getProductionSet()
							.getProductionsWithLHS(LHS)) {
						Derivation tempDerivation = d.copy();
						tempDerivation.addStep(p, result.indexOf(LHS, i));
						temp.add(tempDerivation);
						myNodesGenerated++;
					}
				}
			}
		}
		myDerivationsQueue.addAll(temp);
		return true;
	}

	private boolean raiseCapacity() {
		NODES_TO_GENERATE += INCREMENT_CAPACITY_BY;
		return true;
	}

	protected abstract boolean isPossibleDerivation(SymbolString string);

}
