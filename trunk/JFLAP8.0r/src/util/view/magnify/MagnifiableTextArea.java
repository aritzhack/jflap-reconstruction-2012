package util.view.magnify;

import java.awt.Font;

import javax.swing.JTextArea;

public class MagnifiableTextArea extends JTextArea implements Magnifiable {

	private int myDefaultSize;
	
	public MagnifiableTextArea (int defaultSize) {
		super();
		myDefaultSize = defaultSize;
	}
	
	
	
	@Override
	public void setMagnification(double mag) {
		float size = (float) (mag*myDefaultSize);
		Font f = this.getFont().deriveFont(size);
		this.setFont(f);
		
	}

}
