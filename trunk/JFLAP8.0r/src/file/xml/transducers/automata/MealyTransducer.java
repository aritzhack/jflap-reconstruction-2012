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
import jflap.model.automaton.mealy.*;
import jflap.model.formaldef.symbols.SymbolString;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;



/**
 * This is the transducer for encoding and decoding 
 * {@link jflap.model.automaton.mealy.MealyMachine} objects.
 * 
 * @author Jinghui Lim
 *
 */
public class MealyTransducer extends LinearlizedLabelTransducer<MealyMachine> 
{
    /**
     * The tag name for the output string transition elements.
     */
    public static final String TRANSITION_OUTPUT_NAME = "transout";
    
    
    
    /**
     * Returns the type string for this transducer, "mealy".
     * 
     * @return the string "mealy"
     */
    public String getType() 
    {
        return "mealy";
    }


	@Override
	protected TransitionLabel createLabel(String[] split, MealyMachine automaton) {
		SymbolString in = SymbolString.createFromString(split[0], automaton.getInputAlphabet());
		SymbolString out = SymbolString.createFromString(split[1], automaton.getOutputAlphabet());

        return new MealyTransitionLabel( in, out);
	}
	
	
	
}
