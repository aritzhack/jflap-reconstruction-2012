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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jflap.file.DataException;
import jflap.file.xml.TransducerHelper;
import jflap.file.xml.transducers.AutomatonTransducer;
import jflap.model.automaton.Automaton;
import jflap.model.automaton.LinearTransitionLabel;
import jflap.model.automaton.State;
import jflap.model.automaton.Transition;
import jflap.model.automaton.TransitionLabel;
import jflap.model.automaton.turing.Block;
import jflap.model.automaton.turing.TMTransitionLabel;
import jflap.model.automaton.turing.TuringMachine;
import jflap.model.automaton.turing.TuringMachineTransition;
import jflap.model.formaldef.symbols.SymbolString;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


/**
 * This is the transducer for encoding and decoding
 * {@link jflap.model.automaton.turing.TuringMachine} objects.
 * 
 * @author Thomas Finley
 */

public class TMTransducer extends AutomatonTransducer<TuringMachine> {
	

	/** The attribute name for the tape ID attribute. */
	public static final String TAPE_INDEX_TAG = "tape";
	
	public static final String TRANSITION_SUBLABEL = "sublabel";

	public static final String TAPE_NUM = "numtapes";

	private static final String MOVE_TAG = "move";
	
	@Override
	public String getType() {
		return "turing";
	}

	



	@Override
	protected Document appendAutomatonComponents(TuringMachine structure,
			Element automaton_ele, Document doc) {
		Element tapenumEle = TransducerHelper.createElement(doc, TAPE_NUM, null, structure.getNumTapes());
		automaton_ele.appendChild(tapenumEle);
		return super.appendAutomatonComponents(structure, automaton_ele, doc);
	}





	@Override
	protected TuringMachine readAutomaton(Element automaton_root, TuringMachine automaton) {
		Node tapenode = automaton_root.getElementsByTagName(TAPE_NUM).item(0);
		String tapenum = TransducerHelper.containedText(tapenode);
		automaton.setNumberTapes(Integer.parseInt(tapenum));
		return super.readAutomaton(automaton_root, automaton);
	}



	@Override
	protected TransitionLabel createTransitionLabel(Element labelEle,
			TuringMachine automaton) {
		
		int numTapes = automaton.getNumTapes();
		TMTransitionLabel label = new TMTransitionLabel(numTapes);
		
		NodeList sublabels = labelEle.getElementsByTagName(TRANSITION_SUBLABEL);
		
		for (int i = 0; i < sublabels.getLength(); i++){
			Element e = (Element) sublabels.item(i);
			String labelString = TransducerHelper.containedText(e);
			String[] split = labelString.split(TransitionLabel.LABEL_DELIMETER);
			int index = Integer.parseInt(e.getAttribute(TAPE_INDEX_TAG));
			int move = Integer.parseInt(e.getAttribute(MOVE_TAG));
			
			//TODO: Is this right?
			SymbolString read = SymbolString.createFromString(split[0], automaton.getTapeAlphabet());
			SymbolString write = SymbolString.createFromString(split[1], automaton.getInputAlphabet());

			label.setReadForTape(i, read.getFirst());
			label.setWriteForTape(i, write.getFirst());
			label.setMoveForTape(i, move);
		}
		
		return label;
	}

	@Override
	protected Node createTransitionElement(Transition t, Document document) {
		TMTransitionLabel label = (TMTransitionLabel) t.getLabel();
		Element e = TransducerHelper.createElement(document, TRANSITION_LABEL, null, null);
		for (int i = 0; i < label.getRowCount(); i++){
			SymbolString read = label.getReadForTape(i);
			SymbolString write = label.getWriteForTape(i);

			String s = createLinearLabelString(read, write);
			Element sublabel = TransducerHelper.createElement(document, 
									TRANSITION_SUBLABEL, 
									null, 
									s);
			
			sublabel.setAttribute(TAPE_INDEX_TAG, "" + i);
			sublabel.setAttribute(MOVE_TAG, "" + label.getMoveForTape(i));
		}
		return e;
	}

	@Override
	protected State readStateNode(Element stateNode, TuringMachine a) {
		State s = super.readStateNode(stateNode, a);
		NodeList children = stateNode.getElementsByTagName(STRUCTURE_NAME);
		if (children.getLength() > 0){
			TuringMachine innerTM = this.fromStructureRoot((Element) children.item(0));
			s = new Block(s.getID(), s.getPoint(), s.getName(), innerTM);
		}
		return s;
	}

	@Override
	protected Element createStateElement(State s, Document doc) {
		Element e = super.createStateElement(s, doc);
		if (s instanceof Block){
			e.appendChild(this.toDOM((TuringMachine) ((Block) s).getInnerMachine()));
		}
		return e;
	}

	
	


}
