package util.view.magnify;

import java.awt.LayoutManager;

import javax.swing.JPanel;


public abstract class MagnifiablePanel extends JPanel implements Magnifiable {

	public MagnifiablePanel() {
		super();
	}

	public MagnifiablePanel(boolean b) {
		super(b);
	}

	public MagnifiablePanel(LayoutManager lm, boolean b) {
		super(lm, b);
	}

	public MagnifiablePanel(LayoutManager lm) {
		super(lm);
	}

	
	
}
