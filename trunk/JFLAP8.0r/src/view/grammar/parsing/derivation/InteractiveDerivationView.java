package view.grammar.parsing.derivation;

import java.awt.BorderLayout;

import model.algorithms.testinput.parse.Derivation;
import util.view.magnify.MagnifiablePanel;
import view.algorithms.toolbar.SteppableToolbar;

public class InteractiveDerivationView extends MagnifiablePanel {
	
	private DerivationView view;
	private DerivationController alg;
	private SteppableToolbar toolbar;

	public InteractiveDerivationView(Derivation d) {
		super(new BorderLayout());
		setName("Derivation View");
		view = new DerivationView(d.getSubDerivation(0));

		alg = new DerivationController(view, d);
		toolbar = new SteppableToolbar(alg, false);
		
		add(toolbar, BorderLayout.NORTH);

		add(view, BorderLayout.CENTER);
		
		setDerivation(d);
	}

	public void setDerivation(Derivation d) {
		alg.setDerivation(d);
		
	}
}
