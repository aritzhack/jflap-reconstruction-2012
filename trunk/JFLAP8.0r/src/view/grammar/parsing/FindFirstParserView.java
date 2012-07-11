package view.grammar.parsing;

import debug.JFLAPDebug;
import model.algorithms.testinput.parse.Derivation;
import model.algorithms.testinput.parse.Parser;
import model.change.events.AdvancedChangeEvent;
import model.grammar.Grammar;
import util.view.DropDownMenuPanel;
import view.grammar.parsing.derivation.InteractiveDerivationView;

public abstract class FindFirstParserView<T extends RunningView> extends ParserView {
	private InteractiveDerivationView derivationView;

	public FindFirstParserView(Parser alg) {
		super(alg);
		
	}

	@Override
	public DropDownMenuPanel createParseView(Parser alg) {
		Grammar g = alg.getGrammar();
		T runningView = createRunningView(alg);
		derivationView = new InteractiveDerivationView(new Derivation(g.getStartVariable()));
		
		DropDownMenuPanel mainPanel = new DropDownMenuPanel(runningView, derivationView);
		
		return mainPanel;
	}
	
	public abstract T createRunningView(Parser alg);
	
	@Override
	public void updateStatus(AdvancedChangeEvent e) {
		Parser alg = getAlgorithm();
		
		if(alg.isDone() && alg.isAccept()){
			Derivation d = alg.getDerivation();
			
			derivationView.setDerivation(d);
		}
	}
}
