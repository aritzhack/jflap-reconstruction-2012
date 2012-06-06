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

	private static final int NODES_TO_GENERATE = 10;

	private Map<SymbolString, Set<SymbolString>> productionMap;
	private Queue<Derivation> derivationsQueue;
	private int nodesGenerated;

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
			//System.out.println(d.createResult());
			if (d.createResult().equals(getInput() )) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return isAccept() || capacityReached();
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
		System.out.printf("Size of derivations queue = %d\n%s\n", derivationsQueue.size(), derivationsQueue);

		return true;
	}



	/**
	 * Create derivation for each production whose LHS = start variable
	 * @return
	 */
	private boolean initializeStartProductions() {
		Variable start = getGrammar().getStartVariable();
		Set<SymbolString> productions = productionMap.get(new SymbolString(start));

		// store start productions somewhere to not have to recreate?
		for (SymbolString rhs : productions) {
			Derivation d = new Derivation(new Production(start, rhs));
			derivationsQueue.add(d);
		}
		System.out.printf("Size of deriv. queue = %d\nDerivations = %s\n", derivationsQueue.size(), derivationsQueue);

		nodesGenerated += productions.size();
		System.out.printf("%d total derivations so far", nodesGenerated);

		return true;
	}



	/**
	 * Creates map where key = LHS and value = all RHS
	 */
	public void createProductionMap () {
		if (productionMap != null)	return;

		productionMap = new HashMap<SymbolString, Set<SymbolString>>();
		for (Production p : getGrammar().getProductionSet()) {
			SymbolString left = p.getLHS(), right = p.getRHS();
			if (!productionMap.containsKey(left)) {
				productionMap.put(left, new HashSet<SymbolString>());
			}
			productionMap.get(left).add(right);
		}

		System.out.println("LHS -> RHS productions map: \t" + productionMap);
	}


	private void initializeQueue() {
		derivationsQueue = new LinkedList<Derivation>();

		Variable start = getGrammar().getStartVariable();
		Set<SymbolString> productions = productionMap.get(new SymbolString(start));

		// store start productions somewhere to not have to recreate?
		for (SymbolString rhs : productions) {
			Derivation d = new Derivation(new Production(start, rhs));
			derivationsQueue.add(d);
		}
		System.out.printf("\nSize of deriv. queue = %d\nDerivations = %s\n", derivationsQueue.size(), derivationsQueue);

		nodesGenerated += productions.size();
		System.out.printf("%d total derivations so far\n", nodesGenerated);

	}

	private boolean makeNextReplacement() {

		// hold new Derivations, else queue not empty until end => cannot step
		ArrayList<Derivation> temp = new ArrayList<Derivation>();

		while (!derivationsQueue.isEmpty()) {
			Derivation d = derivationsQueue.poll();
			
			for (int i = 0; i < d.getLength(); i++) {
				Production prod = d.getProduction(i);
				SymbolString rhs = prod.getRHS();
				for (int j = 0; j < rhs.size(); j++) {
					SymbolString s = new SymbolString(rhs.get(j));
					if (!productionMap.containsKey(s)) 	{
						Derivation change = new Derivation(prod);
						temp.add(change);
						continue;
					}
					Set<SymbolString> replacements = productionMap.get(s);
					for (SymbolString sub : replacements) {
						SymbolString copy = rhs.clone();
						copy.replace(j, sub);

						Derivation change = new Derivation(prod);
						change.addStep(new Production(s, sub), j);
						temp.add(change);

						System.out.printf("\n" +
								"Original derivation: %s\t" +
								"Replacement: %s -> %s at index %d\t\t" +
								"New derivation: %s -> %s\n", 
								prod, s, sub, j, prod.getLHS(), change.createResult());
						
						//System.out.printf("Added derivation with new production %s -> %s\n", prod.getLHS(), copy);
					}

				}

			}

		}

		derivationsQueue.addAll(temp);
		return true;
	}



	protected abstract boolean isPossibleDerivation (SymbolString string);


}
