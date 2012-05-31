package model.grammar.transform;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.Production;

public abstract class ProductionRemovalAlgorithm extends GrammarTransformAlgorithm {

	private Map<Production, Boolean> myToRemoveMap;
	private Set<Production> myToAddSet;
	
	public ProductionRemovalAlgorithm(Grammar g) {
		super(g);
	}

	@Override
	public boolean reset() throws AlgorithmException {
		myToRemoveMap = new TreeMap<Production, Boolean>();
		myToAddSet = new TreeSet<Production>();
		populateRemoveMap();
		return super.reset();
	}
	
	private void populateRemoveMap() {
		for (Production p: this.getOriginalGrammar().getProductionSet()){
			if(isOfTargetForm(p))
				myToRemoveMap.put(p, false);
		}
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{new IdentifyRemovesStep(),
				new AdjustGrammarStep()};
	}
	
	@Override
	public String getDescriptionName() {
		return createTargetTypeName() + " Remover";
	}
	
	public abstract String getTargetProductionType();
		
	public int getNumberUnidentifiedTargets() {
		return getUnidentifiedTargets().size();
	}

	public Set<Production> getUnidentifiedTargets() {
		Set<Production> prods = myToRemoveMap.keySet();
		for (Entry<Production, Boolean> entry: myToRemoveMap.entrySet()){
			if(entry.getValue()) //if has been identified
				prods.remove(entry.getKey());
		}
		return prods;
	}

	public boolean identifyProductionToBeRemoved(Production p) {
		if(!myToRemoveMap.containsKey(p))
			return false;
		
		return myToRemoveMap.put(p, true);
	}

	public int getNumAddsRemaining() {
		return myToAddSet.size();
	}

	public int getNumRemovesRemaining() {
		return myToRemoveMap.size();
	}

	private boolean addAllRemovesNeeded() {
		boolean removed = false;
		for (Production p: myToRemoveMap.keySet())
			removed = identifyProductionToBeRemoved(p) || removed;
		return removed;
	}

	private String createTargetTypeName(){
		return getTargetProductionType() + "production";
	}

	private boolean doAllAdjustments() {
		for (Production p : myToRemoveMap.keySet()) {
			if (!performRemove(p))
				return false;
		}
		for (Production p : myToAddSet) {
			if (!performAdd(p))
				return false;
		}
		return true;
	}

	private boolean performAdd(Production p) {
		if (!myToAddSet.contains(p))
			return false;
		myToAddSet.remove(p);
		return this.getTransformedGrammar().getProductionSet().add(p);
	}

	private boolean performRemove(Production p) {
		if (!myToRemoveMap.containsKey(p))
			return false;
		myToRemoveMap.remove(p);
		myToAddSet.addAll(getProductionsToAddForRemoval(p));
		return this.getTransformedGrammar().getProductionSet().remove(p);
	}

	public abstract boolean isOfTargetForm(Production p);

	public abstract Set<Production> getProductionsToAddForRemoval(Production p);

	public static int[] getIndeciesOfTarget(SymbolString rhs, Symbol target) {
		int[] index = new int[rhs.size()];
		int j = 0;
		for (int i = 0; i < rhs.size(); i++){
			if (rhs.get(i).equals(target)){
				index[j++] = i;
			}
		}
		
		return Arrays.copyOfRange(index, 0, j);
	}
	
	private class IdentifyRemovesStep implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Remove all " + createTargetTypeName() + "s";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return addAllRemovesNeeded();
		}

		@Override
		public boolean isComplete() {
			return getNumberUnidentifiedTargets() == 0;
		}
		
	}

	private class AdjustGrammarStep implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Adjust Grammar";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return doAllAdjustments();
		}

		@Override
		public boolean isComplete() {
			return getNumRemovesRemaining()+getNumAddsRemaining() == 0;
		}
		
	}
}
