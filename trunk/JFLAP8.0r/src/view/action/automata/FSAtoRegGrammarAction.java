package view.action.automata;

import view.algorithms.conversion.AutomatonConversionPanel;
import view.algorithms.conversion.FSAtoRegGrammarPanel;
import view.automata.views.AutomatonView;
import view.automata.views.FSAView;
import model.algorithms.conversion.autotogram.FSAVariableMapping;
import model.algorithms.conversion.autotogram.FSAtoRegGrammarConversion;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;

public class FSAtoRegGrammarAction extends AutomatonToGrammarAction<FiniteStateAcceptor, FSATransition, FSAVariableMapping>{

	public FSAtoRegGrammarAction(FSAView view) {
		super(view);
	}

	@Override
	public AutomatonConversionPanel<FiniteStateAcceptor, FSATransition, FSAVariableMapping> createConversionPanel() {
		FSAtoRegGrammarConversion convert = new FSAtoRegGrammarConversion((FiniteStateAcceptor) getAutomaton());
		return new FSAtoRegGrammarPanel(getEditorPanel(), convert);
	}

}
