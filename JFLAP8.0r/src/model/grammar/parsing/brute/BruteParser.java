package model.grammar.parsing.brute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.parsing.Derivation;
import model.grammar.parsing.Parser;
import model.grammar.parsing.ParserException;
import model.grammar.typetest.GrammarType;

/**
 * 
 * @author Peggy Li
 * 
 */

public abstract class BruteParser extends Parser {

	private int NODES_TO_GENERATE = 10000;

	private Map<SymbolString, Set<SymbolString>> productionMap;
	private Queue<Derivation> derivationsQueue;
	private int nodesGenerated;
	private Derivation answerDerivation;

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
		nodesGenerated = 0;

		createProductionMap();
		initializeQueue();

		return true;
	}

	@Override
	public boolean isAccept() {

		// TODO Auto-generated method stub
		for (Derivation d : derivationsQueue) {
			if (d.createResult().equals(getInput())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return capacityReached() || isAccept() || derivationsQueue.isEmpty();
	}

	@Override
	public GrammarType getRequiredGrammarType() throws ParserException {
		// TODO Auto-generated method stub
		return GrammarType.UNRESTRICTED;
	}

	private boolean capacityReached() {
		return nodesGenerated >= NODES_TO_GENERATE;
	}

	@Override
	public boolean stepParser() {

		makeNextReplacement();
		return true;
	}

	/**
	 * Creates map where key = LHS and value = all RHS
	 */
	public void createProductionMap() {
		if (productionMap != null)
			return;

		productionMap = new HashMap<SymbolString, Set<SymbolString>>();
		for (Production p : getGrammar().getProductionSet()) {
			SymbolString left = p.getLHS(), right = p.getRHS();
			if (!productionMap.containsKey(left)) {
				productionMap.put(left, new HashSet<SymbolString>());
			}
			productionMap.get(left).add(right);
		}

	}

	private void initializeQueue() {
		derivationsQueue = new LinkedList<Derivation>();

		SymbolString start = new SymbolString(getGrammar().getStartVariable());
		Derivation d = new Derivation(new Production(new SymbolString(), start));
			derivationsQueue.add(d);
			nodesGenerated++;
		}
	

	private boolean makeNextReplacement() {

		// hold new Derivations, else queue not empty until end => cannot step
		ArrayList<Derivation> temp = new ArrayList<Derivation>();

		while (!derivationsQueue.isEmpty()) {
			Derivation d = derivationsQueue.poll();
			SymbolString result = d.createResult();
			
			for(int i=0; i<result.size();i++){
				for(int j=i; j<result.size();j++){
					SymbolString LHS = result.subList(i,j+1);
					for(Production p : getGrammar().getProductionSet().getProductionsWithLHS(LHS)){
						Derivation tempDerivation = d.copy();
						tempDerivation.addStep(p, result.indexOf(LHS, i));
						temp.add(tempDerivation);
						nodesGenerated++;
					}
				}
			}
		}
		derivationsQueue.addAll(temp);
		return true;
	}

	protected abstract boolean isPossibleDerivation(SymbolString string);

}
