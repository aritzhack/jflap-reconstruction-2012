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





package file.xml.transducers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import java.awt.Point;

import javax.xml.stream.events.Attribute;


/**
 * This is an abstract implementation of a transducer that has methods common to
 * all automaton transducers.
 * 
 * @author Thomas Finley
 */

public abstract class AutomatonTransducer<T extends Automaton> extends FormalDefinitionTransducer<T> {



	/**
	 * Given a document, this will return the corresponding automaton encoded in
	 * the DOM document.
	 * 
	 * @param root
	 *            the DOM document to convert
	 * @return the {@link jflap.model.automaton.fsa.FiniteStateAutomaton}instance
	 */
	@Override
	public T fromSpecificContentRoot(Element automata_root, T a) {
		return readAutomaton(automata_root, a);


	}

	@Override
	public String getStructureSpecificTag() {
		return AUTOMATON_TAG;
	}

	protected T readAutomaton(Element automaton_root, T automaton) {
		// Read the states and transitions.
		try {

			for (State s: readStates(automaton_root, automaton))
				automaton.addState(s);
			readTransitions(automaton_root, automaton);

			for (Note n : readNotes(automaton_root, automaton)){
				automaton.addNote(n);
			}

		} catch (Exception e){
			throw new DataException(e.getMessage());
		}
		return automaton;
	}

	/**
	 * Reads the states from the document and adds them to the automaton. Note
	 * that in the event of error, the automaton may have been changed up until
	 * the point where the error was encountered.
	 * 
	 * @param document
	 *            the DOM document to read states from
	 * @param automaton
	 *            the automaton to add states to
	 * @return a map from state identifiers to the specific state
	 * @throws DataException
	 *             in the case of non-numeric, negative, or duplicate IDs
	 * @see #readTransitions
	 */
	protected List<State> readStates(Element root, T automaton) {
		NodeList state_nodes = root.getElementsByTagName(STATE_TAG);
		ArrayList<State> states = new ArrayList<State>();
		for (int k = 0; k < state_nodes.getLength(); k++) {
			try{
				states.add(readStateNode((Element) state_nodes.item(k), automaton));
			} catch(Exception e){
				throw new DataException(e.getMessage());
			}
		}
		return states;
	}

	//Creates a state node
	protected State readStateNode(Element stateNode, T a) {

		// Extract the ID attribute.
		String idString = stateNode.getAttribute(STATE_ID_TAG);
		if (idString == null)
			throw new DataException(
					"State without id attribute encountered!");

		Integer id = parseID(idString);
		// Check for duplicates.
		if (a.getStateWithID(id) != null)
			throw new DataException("The state ID " + id
					+ " appears twice!");
		// Get the fields of this state.
		Map<String, String> e2t = TransducerHelper.elementsToText(stateNode);

		// Try to get the X coord.
		double x = 0, y = 0;
		try {
			x = Double.parseDouble(e2t.get(X_COORD_TAG).toString());
		} catch (Exception e) {
			throw new DataException("The x coordinate "
					+ e2t.get(X_COORD_TAG)
					+ " could not be read for state " + id + ".");
		}
		// Try to get the Y coord.
		try {
			y = Double.parseDouble(e2t.get(Y_COORD_TAG).toString());
		} catch (Exception e) {
			throw new DataException("The y coordinate "
					+ e2t.get(Y_COORD_TAG)
					+ " could not be read for state " + id + ".");
		}

		Point location = new Point((int)x,(int) y);

		String name = ((Element) stateNode).getAttribute(STATE_NAME_TAG);

		State s = a.createState(id, location, name);
		String temp;
		if ((temp = (String) e2t.get(STATE_LABEL_TAG)) != null)
			s.setLabel(temp);
		s.setFinal(e2t.containsKey(STATE_FINAL_TAG));
		s.setInitial(e2t.containsKey(STATE_INITIAL_TAG));
		return s;
	}

	/**
	 * Used to map a string means to encode a state ID to some unique identifier
	 * object.
	 * 
	 * @param string
	 *            a string that encodes a state ID
	 * @return an object that is unique for this state
	 * @throws DataException
	 *             if the state ID is not in a supported format
	 */
	protected static Integer parseID(String string) {
		try {
			int num = Integer.parseInt(string);
			return new Integer(num);
		} catch (NumberFormatException e) {
			return new Integer(-1);
		}
	}

	/**
	 * Reads the transitions from the document and adds them to the automaton.
	 * Note that in the event of error, the automaton may have been changed up
	 * until the point where the error was encountered.
	 * 
	 * @param document
	 *            the DOM document to read transitions from
	 * @param automaton
	 *            the automaton to add transitions to
	 * @param id2state
	 *            the map of ID objects to a state
	 * @return 
	 * @throws DataException
	 *             in the case of absent from/to states
	 * @see #createTransition
	 * @see #readStates
	 */
	protected void readTransitions(Element parent, T automaton){
		// Create the transitions.
		NodeList stacks = parent.getElementsByTagName(TRANSITION_STACK);
		ArrayList<Transition> trans = new ArrayList<Transition>();
		for (int i = 0; i<stacks.getLength(); i++){
			Element stackEle = (Element) stacks.item(i);

			//get from state
			String fromName = stackEle.getAttribute(TRANSITION_FROM_TAG);
			if (fromName == null)
				throw new DataException("A transition has no from state!");
			int id = parseID(fromName).intValue();
			State from = automaton.getStateWithID(id);
			if (from == null)
				throw new DataException("A transition is defined from "
						+ "non-existent state " + id + "!");


			// Get the to state.
			String toName = stackEle.getAttribute(TRANSITION_TO_TAG);
			if (toName == null)
				throw new DataException("A transition has no to state!");
			id = parseID(toName).intValue();
			State to = automaton.getStateWithID(id);
			if (to == null)
				throw new DataException("A transition is defined to "
						+ "non-existent state " + id + "!");

			// Now, make the transition.
			NodeList labelNodes = stackEle.getElementsByTagName(TRANSITION_LABEL);
			for (int j = 0; j < labelNodes.getLength(); j++){
				Transition t = createTransition(from, 
						to, 
						(Element) labelNodes.item(j), 
						automaton);
				automaton.addTransition(t);
			}

			//deal with the shapiness of the transition, if the file specifies it. //add controlX and controlY
			String controlX = stackEle.getAttribute(TRANSITION_CONTROL_X);
			String controlY = stackEle.getAttribute(TRANSITION_CONTROL_Y);
			double ctrlX = Double.parseDouble(controlX),
					ctrlY = Double.parseDouble(controlY);
			automaton.getTransitionStackFromStateToState(from, to).setCtrlPoint(ctrlX, ctrlY);

		}

	}

	/**
	 * Used by the {@link #readTransitions}method. This should be overridden by
	 * subclasses if they have non-linear Labels, i.e. Turing Machine.
	 * 
	 * @param from
	 *            the from state
	 * @param to
	 *            the to state
	 * @param node
	 *            the DOM node corresponding to the transition
	 * @return the new transition
	 * @see #readTransitions
	 */
	protected  Transition createTransition(State from, State to,
			Element labelEle, T automaton){
		TransitionLabel label = this.createTransitionLabel(labelEle, automaton);
		Transition t = automaton.createTransition(from, to, label);
		return t;
	}


	protected abstract TransitionLabel createTransitionLabel(Element labelEle, T automaton);


	private Collection<Note> readNotes(Element root, Automaton auto) {

		NodeList noteNodes = root.getElementsByTagName(NOTE_TAG);
		ArrayList<Note> notes = new ArrayList<Note>();
		for (int k = 0; k < noteNodes.getLength(); k++) {
			notes.add(createNote((Element) noteNodes.item(k)));
		}

		return notes;
	}

	private Note createNote(Element noteEle){

		Map<String, String> e2t = TransducerHelper.elementsToText(noteEle);

		String text = e2t.get(NOTE_TEXT_TAG);
		if (text == null)
			throw new DataException("Invalid Note format: \n" +
					"Does not contain string contents");

		// Try to get the X coord.
		double x = 0, y = 0;
		try {
			x = Double.parseDouble(e2t.get(X_COORD_TAG).toString());
		} catch (NumberFormatException e) {
			throw new DataException("The x coordinate "
					+ e2t.get(X_COORD_TAG)
					+ " could not be read for the note with text " + text + ".");
		}
		// Try to get the Y coord.
		try {
			y = Double.parseDouble(e2t.get(Y_COORD_TAG).toString());
		} catch (NumberFormatException e) {
			throw new DataException("The y coordinate "
					+ e2t.get(Y_COORD_TAG)
					+ " could not be read for the note with text " + text + ".");
		}

		return new Note(new Point((int)x,(int) y), text);

	}

	/**
	 * Given a JFLAP automaton, this will return the corresponding DOM encoding
	 * of the structure.
	 * 
	 * @param structure
	 *            the JFLAP automaton to encode
	 * @return a DOM document instance
	 */
	@Override
	public Document appendStructure(T structure, Element automaton_ele, Document doc) {
		
		return appendAutomatonComponents(structure, automaton_ele, doc);
	}

	protected Document appendAutomatonComponents(T structure, Element automaton_ele,
			Document doc) {
		if (!structure.getStates().isEmpty()){
			appendStates(structure, automaton_ele, doc);
			if (!structure.getTransitions().isEmpty())
				appendTransitions(structure, automaton_ele, doc);
		}
		return doc;
	}

	private void appendStates(T structure, Element automaton_ele, Document doc) {

		//add state comment
		automaton_ele.appendChild(TransducerHelper.createComment(doc, COMMENT_STATES));
		for (Object s: structure.getStates()){
			automaton_ele.appendChild(createStateElement( (State) s, doc));
		}

	}

	protected Element createStateElement(State s, Document doc) {
		Map<String, Object> attri = new LinkedHashMap<String, Object>();
		attri.put(STATE_ID_TAG, s.getID());
		attri.put(STATE_NAME_TAG, s.getName());
		Element ele = TransducerHelper.createElement(doc, STATE_TAG, attri, null);
		TransducerHelper.appendChildNode(ele, X_COORD_TAG, s.getPoint().x, doc);
		TransducerHelper.appendChildNode(ele, Y_COORD_TAG, s.getPoint().y, doc);

		if (s.isFinal()) TransducerHelper.appendChildNode(ele, STATE_FINAL_TAG, null, doc);

		if (s.isInitial()) TransducerHelper.appendChildNode(ele, STATE_INITIAL_TAG, null, doc);

		return ele;
	}


	private void appendTransitions(T structure, Element automaton_ele,
			Document doc) {
		//add state comment
		automaton_ele.appendChild(TransducerHelper.createComment(doc, COMMENT_TRANSITIONS));
		for (Object ts: structure.getTransitionStacks()){
			automaton_ele.appendChild(createTransitionStackElement((TransitionStack) ts, doc));
		}
	}


	/**
	 * Produces a DOM element that encodes a given transition. This
	 * implementation creates a transition element with only the "from" and "to"
	 * elements inserted. The intention is that subclasses will use this to get
	 * the minimal transition element, and fill in whatever else is required
	 * themselves.
	 * 
	 * @param stack
	 *            the transition to encode
	 * @param document
	 *            the document to create the state in
	 * @return the newly created element that encodes the state
	 * @see #createStateElement
	 * @see #toDOM
	 */
	protected Element createTransitionStackElement(TransitionStack<Transition> stack,
			Document document) {
		State from = stack.getFrom(),
				to = stack.getTo();
		Map<String, Object> attri = new HashMap<String, Object>();
		attri.put(TRANSITION_FROM_TAG, from.getID());
		attri.put(TRANSITION_TO_TAG, to.getID());
		attri.put(TRANSITION_CONTROL_X, stack.getCtrlPt().getX());
		attri.put(TRANSITION_CONTROL_Y, stack.getCtrlPt().getY());


		// Start the creation of the transition.
		Element te = TransducerHelper.createElement(document, TRANSITION_STACK, attri, null);
		// Encode the labels.
		for(Transition t: stack){
			te.appendChild(createTransitionElement(t, document));
		}
		// Return the completed transition stack encoding element.
		return te;
	}


	protected abstract Node createTransitionElement(Transition t, Document document);


	protected String createLinearLabelString(SymbolString ... strings) {
		String s = "";
		s += nonLambdaString(strings[0]);
		for (int i = 1; i < strings.length; i++){
			s +=  TransitionLabel.LABEL_DELIMETER + nonLambdaString(strings[i]);
		}
		return s;
	}



	private String nonLambdaString(SymbolString s) {
		if (s.isEmpty()) return " ";
		return s.toString();
	}



	public static final String AUTOMATON_TAG = "automaton";


	/** The tag name for individual block elements. */
	public static final String BLOCK_TAG = "block";

	/** The tag name for individual state elements. */
	public static final String STATE_TAG = "state";

	/** The attribute name for the state ID. */
	public static final String STATE_ID_TAG = "id";

	/** The tag name for the X coordinate. */
	public static final String X_COORD_TAG = "x";

	/** The tag name for the Y coordinate. */
	public static final String Y_COORD_TAG = "y";

	/** The tag name for the optional label of the state. */
	public static final String STATE_LABEL_TAG = "label";

	/** The tag name for the optional special name of the state. */
	public static final String STATE_NAME_TAG = "name";

	/** The tag name for the final state. */
	public static final String STATE_FINAL_TAG = "final";

	/** The tag name for the optional special name of the state. */
	public static final String STATE_INITIAL_TAG = "initial";

	/** The tag name for the transition stack. */
	public static final String TRANSITION_STACK = "transitions";

	/** The tag name for the individual transition elements. */
	public static final String TRANSITION_LABEL = STATE_LABEL_TAG;

	/** The tag name for the from state ID. */
	public static final String TRANSITION_FROM_TAG = "from";

	/** The tag name for the to state ID. */
	public static final String TRANSITION_TO_TAG = "to";

	/**The tag name for the x coordinate of the control point for a transition. */
	public static final String TRANSITION_CONTROL_X = "control" + X_COORD_TAG;

	/**The tag name for the y coordinate of the control point for a transition. */
	public static final String TRANSITION_CONTROL_Y = "control" + Y_COORD_TAG;

	/** The comment for the list of states. */
	private static final String COMMENT_STATES = "The list of states.";

	/** The comment for the list of transitions. */
	private static final String COMMENT_TRANSITIONS = "The list of transitions.";


	/** The tag name for the individual note elements. */
	public static final String NOTE_TAG = "note";

	/** The tag name for the text of the note elements. */
	public static final String NOTE_TEXT_TAG = "text";


	/**The tag name for the block transition */
	private static final String IS_BLOCK = "block";
}
