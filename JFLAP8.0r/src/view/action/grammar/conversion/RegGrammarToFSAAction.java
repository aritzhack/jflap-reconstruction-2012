package view.action.grammar.conversion;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import model.algorithms.conversion.gramtoauto.GrammarToAutomatonConverter;
import model.algorithms.conversion.gramtoauto.RGtoFSAConverter;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.grammar.Grammar;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import view.algorithms.conversion.GrammarToAutoConversionPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.grammar.GrammarView;

public class RegGrammarToFSAAction extends AbstractAction{

	private GrammarView myView;

	public RegGrammarToFSAAction(GrammarView view){
		super("Convert Regular Grammar to FSA");
		myView = view;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Grammar g = myView.getDefinition();
		RGtoFSAConverter convert  = new RGtoFSAConverter(g);
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> editor = new AutomatonEditorPanel<FiniteStateAcceptor, FSATransition>(convert.getConvertedAutomaton(), new UndoKeeper(), true);
		GrammarToAutoConversionPanel<FiniteStateAcceptor, FSATransition> panel = new GrammarToAutoConversionPanel<FiniteStateAcceptor, FSATransition>(convert, editor, "Convert to FA");
		
		JFLAPUniverse.getActiveEnvironment().addSelectedComponent(panel);
	}

}
