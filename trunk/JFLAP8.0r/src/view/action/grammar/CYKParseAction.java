package view.action.grammar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import model.grammar.Grammar;
import model.grammar.parsing.ParserException;
import model.grammar.typetest.matchers.CNFChecker;
import model.symbols.symbolizer.Symbolizers;

import universe.JFLAPUniverse;
import view.environment.JFLAPEnvironment;
import view.grammar.GrammarView;
import view.grammar.parsing.cyk.CYKParsePane;
import view.grammar.productions.ProductionTable;

public class CYKParseAction extends AbstractAction{

	private GrammarView myView;

	public CYKParseAction(GrammarView view){
		super("CYK Parse");
		myView = view;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Grammar g = myView.getDefintion();
		if (g == null)
			return;
		if(g.getStartVariable()==null) throw new ParserException("The Start Variable must be set before you can continue");
		if(!new CNFChecker().matchesGrammar(g)) throw new ParserException("The grammar must be in CNF form to be parsed!");
		
		CYKParsePane cpp = new CYKParsePane(g, (ProductionTable) myView.getCentralPanel());
		JFLAPEnvironment environ = JFLAPUniverse.getActiveEnvironment();
		environ.addSelectedComponent(cpp);
	}

}
