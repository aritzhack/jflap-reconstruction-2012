package file.xml;

import java.lang.Thread.State;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.StateSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.pda.BottomOfStackSymbol;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.acceptors.pda.StackAlphabet;
import model.automata.transducers.OutputAlphabet;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.transducers.moore.MooreMachine;
import model.automata.turing.BlankSymbol;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.formaldef.components.symbols.SymbolString;
import model.grammar.Grammar;
import model.grammar.StartVariable;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;
import model.regex.ExpressionComponent;
import model.regex.RegularExpression;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import file.xml.formaldef.automata.FSATransducer;
import file.xml.formaldef.automata.MealyMachineTransducer;
import file.xml.formaldef.automata.MooreMachineTransducer;
import file.xml.formaldef.automata.PDATransducer;
import file.xml.formaldef.components.alphabet.InputAlphabetTransducer;
import file.xml.formaldef.components.alphabet.OutputAlphabetTransducer;
import file.xml.formaldef.components.alphabet.StackAlphabetTransducer;
import file.xml.formaldef.components.alphabet.TapeAlphabetTransducer;
import file.xml.formaldef.components.alphabet.TerminalsTransducer;
import file.xml.formaldef.components.alphabet.VariablesTransducer;
import file.xml.formaldef.components.states.FinalStateSetTransducer;
import file.xml.formaldef.components.states.FromStateTransducer;
import file.xml.formaldef.components.states.StartStateTransducer;
import file.xml.formaldef.components.states.StateSetTransducer;
import file.xml.formaldef.components.states.StateTransducer;
import file.xml.formaldef.components.states.ToStateTransducer;
import file.xml.formaldef.components.symbols.BottomOfStackSymbolTransducer;
import file.xml.formaldef.components.symbols.StartVariableTransducer;
import file.xml.formaldef.grammar.GrammarTransducer;
import file.xml.formaldef.regex.ExpressionStringTransducer;
import file.xml.formaldef.regex.RegExTransducer;

public class TransducerFactory{

	private static Map<Class, LinkedHashSet<XMLTransducer>> myClassToTransducerMap;
	
	static{
		myClassToTransducerMap = new HashMap<Class, LinkedHashSet<XMLTransducer>>();
		//FSA
		addMapping(FiniteStateAcceptor.class, new FSATransducer());
		addMapping(FinalStateSet.class, new FinalStateSetTransducer());
		addMapping(InputAlphabet.class, new InputAlphabetTransducer());
		addMapping(StateSet.class, new StateSetTransducer());
		addMapping(StartState.class, new StartStateTransducer());
		addMapping(State.class, new StateTransducer(),
								new FromStateTransducer(), 
								new ToStateTransducer());
		//PDA
		addMapping(PushdownAutomaton.class, new PDATransducer());
		addMapping(StackAlphabet.class, new StackAlphabetTransducer());
		addMapping(BottomOfStackSymbol.class, new BottomOfStackSymbolTransducer());
		
		//Moore and Mealy
		addMapping(MooreMachine.class, new MooreMachineTransducer());
		addMapping(MealyMachine.class, new MealyMachineTransducer());
		addMapping(OutputAlphabet.class, new OutputAlphabetTransducer());
		
		//TM - Need to add the TMTransitionFunction stuff too
		addMapping(MultiTapeTuringMachine.class, new TuringMachineTransducer());
		addMapping(TapeAlphabet.class, new TapeAlphabetTransducer());
		
		//Grammar
		addMapping(Grammar.class, new GrammarTransducer());
		addMapping(TerminalAlphabet.class, new TerminalsTransducer());
		addMapping(VariableAlphabet.class, new VariablesTransducer());
		addMapping(StartVariable.class, new StartVariableTransducer());
		
		//RegEx
		addMapping(RegularExpression.class, new RegExTransducer());

	}

	public static void addMapping(Class c, XMLTransducer ... struct) {
		myClassToTransducerMap.put(c,
				new LinkedHashSet<XMLTransducer>(Arrays.asList(struct)));
	}
	
	public static <T> XMLTransducer<T> getTransducerForStructure(T object){
		LinkedHashSet<XMLTransducer> set = myClassToTransducerMap.get(object.getClass());
		if (set == null) return null;
		return (XMLTransducer<T>) set.toArray()[0];
	}
	
	public static XMLTransducer getTransducerForTag(String tag){
		for (LinkedHashSet<XMLTransducer> set: myClassToTransducerMap.values())
		{
			for (XMLTransducer trans:set){
				if (trans.matchesTag(tag))
					return trans;
			}
		}
		return null;
	}
	

}
