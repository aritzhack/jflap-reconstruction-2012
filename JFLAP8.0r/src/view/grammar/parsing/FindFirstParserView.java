package view.grammar.parsing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.algorithms.testinput.parse.Derivation;
import model.algorithms.testinput.parse.Parser;
import model.change.events.AdvancedChangeEvent;
import universe.preferences.JFLAPPreferences;
import util.view.DropDownMenuPanel;
import util.view.magnify.MagnifiablePanel;
import view.grammar.parsing.derivation.InteractiveDerivationView;

public abstract class FindFirstParserView<T extends RunningView> extends ParserView {

	private DropDownMenuPanel mainPanel;

	public FindFirstParserView(Parser alg) {
		super(alg);
		
	}

	@Override
	public DropDownMenuPanel createParseView(Parser alg) {
		T runningView = createRunningView(alg);
		mainPanel = new DropDownMenuPanel(runningView);
		
		return mainPanel;
	}
	
	public abstract T createRunningView(Parser alg);
	
	@Override
	public void updateStatus(AdvancedChangeEvent e) {
		Parser alg = getAlgorithm();
		
		if(alg.isDone() && alg.isAccept()){
			Derivation d = alg.getDerivation();
			
			InteractiveDerivationView derivationView = new InteractiveDerivationView(d);
			mainPanel.addOption(derivationView);
		}
	}
}
