package model.grammar.parsing.brute;

import java.util.*;

import model.grammar.*;
import model.grammar.parsing.*;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.parsing.Derivation;
import model.grammar.parsing.Parser;
import model.grammar.parsing.ParserException;
import model.grammar.parsing.brute.bad.Unrestricted;
import model.grammar.typetest.GrammarType;
import model.formaldef.components.symbols.SymbolString;

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
	private int myNodesGenerated, maxLHSsize;
	private Derivation myAnswerDerivation;
	private Set<Symbol>smaller;
	
	public BruteParser(Grammar g) {
		super(g);
		smaller = Collections.unmodifiableSet(Unrestricted
				.smallerSymbols(g));
		maxLHSsize = 0;
		for(Production p : g.getProductionSet()){
			if(p.getLHS().size()>maxLHSsize){
				maxLHSsize = p.getLHS().size();
			}
		}
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

		loop: while (!myDerivationsQueue.isEmpty()) {
			Derivation d = myDerivationsQueue.poll();
			SymbolString result = d.createResult();
			for(int i=0; i<result.size();i++){
				for(int j=i; j<maxLHSsize+i;j++){
					SymbolString LHS = result.subList(i,j+1);
					for(Production p : getGrammar().getProductionSet().getProductionsWithLHS(LHS)){
						Derivation tempDerivation = d.copy();
						tempDerivation.addStep(p, result.indexOf(LHS, i));
						if(isPossibleDerivation(tempDerivation.createResult())){
							temp.add(tempDerivation);
							myNodesGenerated++;
							if(tempDerivation.createResult().equals(getInput())){
								break loop;
							}
						}
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
	
	public int getNumberOfNodes(){
		return this.myNodesGenerated;
	}
	
	public boolean isPossibleDerivation(SymbolString derivation) {
		return Unrestricted.minimumLength(derivation, smaller) <= getInput().size();
	}

}
