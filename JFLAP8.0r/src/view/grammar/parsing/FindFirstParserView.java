package view.grammar.parsing;

import debug.JFLAPDebug;
import model.algorithms.testinput.parse.Derivation;
import model.algorithms.testinput.parse.Parser;
import model.change.events.AdvancedChangeEvent;
import model.grammar.Grammar;
import util.view.DropDownMenuPanel;
import view.grammar.parsing.derivation.InteractiveDerivationView;

public abstract class FindFirstParserView<T extends RunningView> extends
		ParserView {
	private DropDownMenuPanel mainPanel;
	private T runningView;

	public FindFirstParserView(Parser alg) {
		super(alg);
	}

	@Override
	public DropDownMenuPanel createParseView(Parser alg) {
		Grammar g = alg.getGrammar();
		runningView = createRunningView(alg);

		mainPanel = new DropDownMenuPanel(runningView);

		return mainPanel;
	}

	public abstract T createRunningView(Parser alg);

	public T getRunningView(){
		return runningView;
	}
	
	@Override
	public void updateStatus(AdvancedChangeEvent e) {
		Parser alg = getAlgorithm();

		if (alg.isDone() && alg.isAccept()) {
			Derivation d = alg.getDerivation();

			InteractiveDerivationView derivationView = new InteractiveDerivationView(
					d);
			mainPanel.addOption(derivationView);
		}

		super.updateStatus(e);
	}
}
