package view.grammar.parsing.derivation;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import debug.JFLAPDebug;

import model.algorithms.AlgorithmException;
import model.algorithms.steppable.AlgorithmStep;
import model.algorithms.steppable.SteppableAlgorithm;
import model.algorithms.testinput.parse.Derivation;
import model.grammar.Production;
import model.grammar.Terminal;
import model.grammar.Variable;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableToolbar;
import view.action.StepAction;
import view.action.algorithm.AlgorithmCompleteAction;
import view.action.algorithm.AlgorithmResetAction;

public class InteractiveDerivationView extends MagnifiablePanel {
	private DerivationView myView;
	
	public InteractiveDerivationView(Derivation d) {
		super(new BorderLayout());

		initView(d);
	}

	private void initView(Derivation d) {
		MagnifiableToolbar toolbar = new MagnifiableToolbar();
		DerivationView view = new DerivationView(d.getSubDerivation(0));

		DerivationController alg = new DerivationController(view, d);

		toolbar.add(new StepAction(alg));
		toolbar.add(new AlgorithmCompleteAction(alg));
		toolbar.add(new AlgorithmResetAction(alg));

		toolbar.setFloatable(false);
		add(toolbar, BorderLayout.NORTH);

		add(view, BorderLayout.CENTER);

	}

	public static void main(String[] args) {
		Variable S = new Variable("S");
		Variable A = new Variable("A");
		Terminal b = new Terminal("b");

		Production p1 = new Production(S, A, A);
		Production p2 = new Production(S, A, A, S);
		Production p3 = new Production(A, b);

		// derive bbbb

		Derivation d = new Derivation(p2);
		d.addStep(p1, 2);
		d.addAll(new Production[] { p3, p3, p3, p3 }, new Integer[] { 3, 0, 2,
				1 });

		JFrame fram = new JFrame();
		fram.add(new InteractiveDerivationView(d));
		fram.pack();
		fram.setVisible(true);
	}
}
