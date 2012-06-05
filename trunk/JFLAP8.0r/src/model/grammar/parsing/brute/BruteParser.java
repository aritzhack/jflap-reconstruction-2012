package model.grammar.parsing.brute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import model.formaldef.components.symbols.SymbolString;
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

	private static final int NODES_TO_GENERATE = 5000;

	private Map<SymbolString, Set<SymbolString>> productionMap;
	private List<BruteParseNode> possibleDerivations;
	private Queue<SymbolString> derivationStack;
	
	private int nodesGenerated;

	public BruteParser(Grammar g) {
		super(g);
		this.resetParserStateOnly();
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
		// TODO Auto-generated method stub
		nodesGenerated = 0;
		
		possibleDerivations = new ArrayList<BruteParseNode>();
		productionMap = new HashMap<SymbolString, Set<SymbolString>>();
		derivationStack = new LinkedList<SymbolString>();
		
		return true;
	}

	@Override
	public boolean isAccept() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return nodesGenerated >= NODES_TO_GENERATE;
	}

	@Override
	public GrammarType getRequiredGrammarType() throws ParserException {
		// TODO Auto-generated method stub
		return GrammarType.UNRESTRICTED;
	}

	@Override
	public boolean stepParser() {
		// TODO Auto-generated method stub

		parse();
		return false;
	}

	

	public boolean parse() {

		createProductionMap();
		

		return true;
	}


	

	public void createProductionMap () {
		for (Production p : getGrammar().getProductionSet()) {
			SymbolString left = p.getLHS(), right = p.getRHS();
			if (!productionMap.containsKey(left)) {
				productionMap.put(left, new HashSet<SymbolString>());
			}
			productionMap.get(left).add(right);
		}

		System.out.println(productionMap);
	}


	public abstract boolean isPossibleDerivation (SymbolString string);


}
