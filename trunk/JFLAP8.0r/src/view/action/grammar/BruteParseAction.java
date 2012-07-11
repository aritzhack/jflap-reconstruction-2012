package view.action.grammar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import debug.JFLAPDebug;

import model.algorithms.testinput.parse.ParserException;
import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;
import model.grammar.Grammar;
import model.grammar.StartVariable;
import universe.JFLAPUniverse;
import view.environment.JFLAPEnvironment;
import view.grammar.GrammarView;
import view.grammar.parsing.brute.BruteParserView;
import view.grammar.productions.ProductionTable;

public class BruteParseAction extends AbstractAction {

	private GrammarView myView;

	public BruteParseAction(GrammarView view){
		super("Brute Force Parse");
		myView = view;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Grammar g = myView.getDefinition();
		if (g == null)
			return;
		if(g.getStartVariable()==null) throw new ParserException("The Start Variable must be set before you can continue");
		BruteParserView bpp = new BruteParserView(UnrestrictedBruteParser.createNewBruteParser(g));
		JFLAPEnvironment environ = JFLAPUniverse.getActiveEnvironment();
		environ.addSelectedComponent(bpp);
	}

}
