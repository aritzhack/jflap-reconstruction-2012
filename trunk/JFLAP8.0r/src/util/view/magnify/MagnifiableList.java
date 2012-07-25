package util.view.magnify;

import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class MagnifiableList extends JList implements Magnifiable {
	
	private int myDefaultSize;

	public MagnifiableList(int defaultSize) {
		super();
		myDefaultSize = defaultSize;
	}
	
	public MagnifiableList () {
		super(new DefaultListModel());
	}
	
	@Override
	public void setMagnification(double mag) {
		float size = (float) (mag*myDefaultSize);
		Font f = this.getFont().deriveFont(Font.PLAIN, size);
		this.setFont(f);
	}

}
