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


import jflap.debug.JFLAPDebug;
import jflap.file.DataException;
import jflap.file.xml.transducers.AutomatonTransducer;
import jflap.model.automaton.Automaton;
import jflap.model.automaton.State;
import jflap.model.automaton.Transition;
import jflap.model.automaton.TransitionLabel;
import jflap.model.automaton.pda.PDATransition;
import jflap.model.automaton.pda.PDATransitionLabel;
import jflap.model.automaton.pda.PushdownAutomaton;
import jflap.model.formaldef.symbols.SymbolString;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * This is the transducer for encoding and decoding
 * {@link jflap.model.automaton.pda.PushdownAutomaton} objects.
 * 
 * @author Thomas Finley
 */

public class PDATransducer extends LinearlizedLabelTransducer<PushdownAutomaton> {


	/**
	 * Returns the type string for this transducer, "pda".
	 * 
	 * @return the string "pda"
	 */
	public String getType() {
		return "pda";
	}

	@Override
	protected TransitionLabel createLabel(String[] split,
			PushdownAutomaton automaton) {
		SymbolString read = SymbolString.createFromString(split[PDATransitionLabel.INPUT], 
				automaton.getInputAlphabet());
		SymbolString pop = SymbolString.createFromString(split[PDATransitionLabel.POP],
				automaton.getStackAlphabet());
		SymbolString push = SymbolString.createFromString(split[PDATransitionLabel.PUSH],
				automaton.getStackAlphabet());
		return new PDATransitionLabel(read, pop, push);
	}




}
