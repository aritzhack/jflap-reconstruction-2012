package view.grammar.parsing;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import debug.JFLAPDebug;

import model.grammar.Grammar;
import model.grammar.StartVariable;
import model.grammar.parsing.ParserException;
import universe.JFLAPUniverse;
import view.environment.JFLAPEnvironment;
import view.grammar.GrammarView;

public class BruteParseAction extends AbstractAction {

	private GrammarView myView;

	public BruteParseAction(GrammarView view){
		super("Brute Force Parse");
		myView = view;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Grammar g = myView.getDefintion();
		if (g == null)
			return;
		if(g.getStartVariable()==null) throw new ParserException("The Start Variable must be set before you can continue");
		BruteParsePane bpp = new BruteParsePane(myView);
		JFLAPEnvironment environ = JFLAPUniverse.getActiveEnvironment();
		environ.addSelectedComponent(bpp);
	}

}
