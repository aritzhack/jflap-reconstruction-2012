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


import jflap.file.xml.transducers.AutomatonTransducer;
import jflap.model.automaton.Automaton;
import jflap.model.automaton.State;
import jflap.model.automaton.Transition;
import jflap.model.automaton.TransitionLabel;
import jflap.model.automaton.fsa.FSATransition;
import jflap.model.automaton.fsa.FSATransitionLabel;
import jflap.model.automaton.fsa.FiniteStateAutomaton;
import jflap.model.formaldef.symbols.SymbolString;
import jflap.view.formaldef.definitionpanel.alphabetpanel.symbolbar.SymbolBar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * This is the transducer for encoding and decoding
 * {@link jflap.model.automaton.fsa.FiniteStateAutomaton} objects.
 * 
 * @author Thomas Finley
 */

public class FSATransducer extends LinearlizedLabelTransducer<FiniteStateAutomaton> {

	/**
	 * Returns the type string for this transducer, "fsa".
	 * 
	 * @return the string "fsa"
	 */
	public String getType() {
		return "fsa";
	}


	@Override
	protected TransitionLabel createLabel(String[] split,
			FiniteStateAutomaton automaton) {
		SymbolString s = SymbolString.createFromString(split[0], automaton);
		return new FSATransitionLabel(s);
	}


}
