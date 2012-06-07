package file.xml.transducers.automata;

import java.util.Arrays;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import jflap.debug.JFLAPDebug;
import jflap.file.xml.TransducerHelper;
import jflap.file.xml.transducers.AutomatonTransducer;
import jflap.model.JFLAPModel;
import jflap.model.automaton.Automaton;
import jflap.model.automaton.LinearTransitionLabel;
import jflap.model.automaton.Transition;
import jflap.model.automaton.TransitionLabel;
import jflap.model.formaldef.FormalDefinition;

public abstract class LinearlizedLabelTransducer<T extends Automaton<? extends Transition<? extends LinearTransitionLabel>>> 
																extends AutomatonTransducer<T> {

	@Override
	protected TransitionLabel createTransitionLabel(Element labelEle, T automaton) {
		String s = TransducerHelper.containedText(labelEle);
		String[] split = new String[]{s};
		if (s.indexOf(TransitionLabel.LABEL_DELIMETER) >= 0) split = s.split(TransitionLabel.LABEL_DELIMETER,-1);
		for (String str: split)
			s.trim();
		return createLabel(split, automaton);
	}

	@Override
	protected Node createTransitionElement(Transition t, Document document) {
		return createLinearTransitionElement(t, document);
	}
	
	private Node createLinearTransitionElement(Transition t, Document document) {
		return TransducerHelper.createElement(document, 
				TRANSITION_LABEL, 
				null, 
				createLinearLabelString(t.getLabel().getArray()));
	}
	
	protected abstract TransitionLabel createLabel(String[] split, T automaton);


}
