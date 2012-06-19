package model.algorithms.conversion.autotogram;

import java.awt.PageAttributes.OriginType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import model.algorithms.AlgorithmException;
import model.algorithms.AlgorithmStep;
import model.automata.InputAlphabet;
import model.automata.State;
import model.automata.turing.TuringMachine;
import model.automata.turing.TuringMachineMove;
import model.automata.turing.TuringMachineTransition;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;
import model.grammar.Production;
import model.grammar.ProductionSet;
import errors.BooleanWrapper;

public class TMtoGrammarConversion extends AutomatonToGrammarConversion<TuringMachine, TMVariableMapping, TuringMachineTransition>{

	public TMtoGrammarConversion(TuringMachine automaton)
			throws AlgorithmException {
		super(automaton);
		
	}

	@Override
	public String getDescriptionName() {
		return "Turing machine to Grammar conversion";
	}

	@Override
	public String getDescription() {
		return "Algorithm (from Linz book) to convert TM to Unrestricted Grammar";
	}

	@Override
	public boolean isStartMapping(TMVariableMapping mapping) {
		return false;
	}

	@Override
	public Production[] convertTransition(TuringMachineTransition trans) {
		List<Production> productionList = new ArrayList<Production>();
		State i = trans.getFromState();
		State j = trans.getToState();
		Symbol c = trans.getRead(0).getFirst();
		Symbol d = trans.getWrite(0).getFirst();
		InputAlphabet sigmaAndBlank = getAutomaton().getInputAlphabet().copy();
		sigmaAndBlank.add(getAutomaton().getBlankSymbol().copy());
		for(Symbol a : sigmaAndBlank){
			for(Symbol p : sigmaAndBlank){
				for(Symbol q : getAutomaton().getTapeAlphabet()){
					Variable Vaic = getVarForMapping(new TMVariableMapping(a, i, c));
					Variable Vpq = getVarForMapping(new TMVariableMapping(p, q));
					Variable Vad = getVarForMapping(new TMVariableMapping(a, d));
					Variable Vpjq = getVarForMapping(new TMVariableMapping(p, j, q));
					Terminal aTerminal = new Terminal(a.getString()), pTerminal = new Terminal(p.getString());
					if(trans.getMove(0)==TuringMachineMove.RIGHT){
						productionList.add(new Production(new SymbolString(Vaic,Vpq), new SymbolString(Vad,Vpjq)));
					} else{
						productionList.add(new Production(new SymbolString(Vpq,Vaic), new SymbolString(Vpjq,Vad)));
					}
					productionList.add(new Production(new SymbolString(aTerminal, Vpq), new SymbolString(aTerminal, pTerminal)));
					productionList.add(new Production(new SymbolString(Vpq, aTerminal), new SymbolString(pTerminal, aTerminal)));
					if(getAutomaton().getFinalStateSet().contains(j)){
						Variable Vajq = getVarForMapping(new TMVariableMapping(a, j, q));
						productionList.add(new Production(Vajq, new Terminal(a.getString())));
					}
				}
			}
		}
		return productionList.toArray(new Production[0]);
	}

	@Override
	public Set<TMVariableMapping> getAllNecessaryMappings() {
		Set<TMVariableMapping> mappingSet = new HashSet<TMVariableMapping>();
		for(TuringMachineTransition transition : getAutomaton().getTransitions()){
			State i = transition.getFromState();
			State j = transition.getToState();
			Symbol c = transition.getRead(0).getFirst();
			Symbol d = transition.getWrite(0).getFirst();
			InputAlphabet sigmaAndBlank = getAutomaton().getInputAlphabet().copy();
			sigmaAndBlank.add(getAutomaton().getBlankSymbol().copy());
			for(Symbol a : sigmaAndBlank){
				for(Symbol p : sigmaAndBlank){
					for(Symbol q : getAutomaton().getTapeAlphabet()){
						mappingSet.add(new TMVariableMapping(a, i, c));
						mappingSet.add(new TMVariableMapping(p, q));
						mappingSet.add(new TMVariableMapping(a, d));
						mappingSet.add(new TMVariableMapping(p, j, q));
						if(getAutomaton().getFinalStateSet().contains(j)){
							mappingSet.add(new TMVariableMapping(a, j, q));
						}
					}
				}
			}
		}
		return mappingSet;
	}

	@Override
	public BooleanWrapper[] checkOfProperForm(TuringMachine fd) {
		List<BooleanWrapper> bw = new ArrayList<BooleanWrapper>();
		if(fd.getNumTapes()!=1) bw.add(new BooleanWrapper(false, "The Turing machine has multiple tapes"));
		for(TuringMachineTransition trans : fd.getTransitions()){
			if(trans.getMove(0)==TuringMachineMove.STAY){
				bw.add(new BooleanWrapper(false, "The Turing machine has Stay transitions"));
				break;
			}
		}
		
		return bw.toArray(new BooleanWrapper[0]);
	}
	
	@Override
	public AlgorithmStep[] initializeAllSteps() {
		AlgorithmStep[] old = super.initializeAllSteps();
		return new AlgorithmStep[]{old[1], new CreateAdditionalProductions(), old[2]};
	}
	
	private class CreateAdditionalProductions implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			// TODO Auto-generated method stub
			return "Create Additional Productions";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return addAllExtraProductions();
		}

		@Override
		public boolean isComplete() {
			return !getConvertedGrammar().getProductionSet().isEmpty();
		}
		
	}
	private boolean addAllExtraProductions() {
		ProductionSet p = getConvertedGrammar().getProductionSet();
		String s = "S", t="T";
		while (getConvertedGrammar().getVariables().containsSymbolWithString(s)){
			s += "'";
		}
		while (getConvertedGrammar().getVariables().containsSymbolWithString(t)){
			t+= "'";
		}
		char open = getConvertedGrammar().getOpenGroup();
		char close = getConvertedGrammar().getCloseGroup();
		Variable S = new Variable(open+s+close), T = new Variable(open+t+close);
		
		getConvertedGrammar().setStartVariable(S);
		
		Variable Vblankblank = getVarForMapping(new TMVariableMapping(getOriginalDefinition().getBlankSymbol(),getOriginalDefinition().getBlankSymbol()));
		p.add(new Production(S, T));
		p.add(new Production(S, Vblankblank, S));
		p.add(new Production(S, S, Vblankblank));
		p.add(new Production(new Terminal(getOriginalDefinition().getBlankSymbol().getString()), new SymbolString()));
		for(Symbol a : getAutomaton().getInputAlphabet()){
			p.add(new Production(T, T, getVarForMapping(new TMVariableMapping(a,a))));
			p.add(new Production(T, getVarForMapping(new TMVariableMapping(a, getOriginalDefinition().getStates().getStateWithID(0), a))));
		}
		
		return true;
	}
}
