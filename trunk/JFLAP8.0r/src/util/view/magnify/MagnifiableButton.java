package util.view.magnify;

import java.awt.Font;

import javax.swing.AbstractAction;
import javax.swing.JButton;


public class MagnifiableButton extends JButton implements Magnifiable {

	private int myDefaultSize;

	public MagnifiableButton(int defaultSize){
		this("", defaultSize);
	}
	
	public MagnifiableButton(AbstractAction action, int defaultSize){
		super(action);
		myDefaultSize = defaultSize;
	}

	public MagnifiableButton(String string, int defaultSize) {
		super(string);
		myDefaultSize = defaultSize;
	}

	@Override
	public void setMagnification(double mag) {
		float size = (float) (mag*myDefaultSize);
		Font f = this.getFont().deriveFont(size);
		this.setFont(f);
	}

}
