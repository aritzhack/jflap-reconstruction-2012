/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





package file.xml.transducers.automata;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jflap.file.DataException;
import jflap.file.xml.TransducerHelper;
import jflap.file.xml.transducers.AutomatonTransducer;
import jflap.model.automaton.Automaton;
import jflap.model.automaton.State;
import jflap.model.automaton.Transition;
import jflap.model.automaton.TransitionLabel;
import jflap.model.automaton.mealy.MealyMachine;
import jflap.model.automaton.mealy.MealyTransition;
import jflap.model.automaton.mealy.MooreMachine;
import jflap.model.automaton.mealy.MooreState;
import jflap.model.automaton.mealy.MooreTransition;
import jflap.model.automaton.mealy.MooreTransitionLabel;
import jflap.model.formaldef.symbols.SymbolString;



import org.w3c.dom.*;



/**
 * This is the transducer for encoding and decoding {@link
 * jflap.model.automaton.mealy.MooreMachine} objects.
 * 
 * @author Jinghui Lim
 *
 */
public class MooreTransducer extends LinearlizedLabelTransducer<MooreMachine>
{
    /**
     * The tag name for the state output string transition elements.
     */
    public static final String STATE_OUTPUT_NAME = "output";
    
    /**
     * Returns the type string for this transducer, "moore".
     * 
     * @return the string "moore"
     */
    public String getType() 
    {
        return "moore";
    }


	@Override
	protected State readStateNode(Element stateNode, MooreMachine a) {
		MooreState state = new MooreState(super.readStateNode(stateNode, a));
		Node output = stateNode.getElementsByTagName(STATE_OUTPUT_NAME).item(0);
		state.setOutput(SymbolString.createFromString(TransducerHelper.containedText(output), a.getOutputAlphabet()));
		return state;
	}

	@Override
	protected Element createStateElement(State s, Document doc) {
		Element e = super.createStateElement(s, doc);
		TransducerHelper.appendChildNode(e, STATE_OUTPUT_NAME, ((MooreState) s).getOutput(), doc);
		return e;
	}

	@Override
	protected TransitionLabel createLabel(String[] split, MooreMachine automaton) {
		SymbolString input = SymbolString.createFromString(split[0], automaton.getInputAlphabet());
		return new MooreTransitionLabel(input);
	}
	
	
    

}
