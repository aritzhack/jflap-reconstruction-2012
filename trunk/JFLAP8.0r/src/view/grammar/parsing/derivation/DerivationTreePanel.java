package view.grammar.parsing.derivation;

import model.algorithms.testinput.parse.Derivation;

public class DerivationTreePanel extends DerivationPanel {

	private boolean amInverted;
	
	public DerivationTreePanel(Derivation d, boolean inverted) {
		super((inverted ? "Inverted " : "") + "Derivation Tree");
		amInverted = inverted;
		setDerivation(d);
	}
	
	@Override
	public void setDerivation(Derivation d) {
		super.setDerivation(d);
		//repaint the panel based on new derivation
	}

}
