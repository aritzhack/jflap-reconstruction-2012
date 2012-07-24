package util.view.magnify;

import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import debug.JFLAPDebug;

import universe.preferences.JFLAPPreferences;

public class MagnifiableList extends JList implements Magnifiable {

	
	public MagnifiableList () {
		super(new DefaultListModel());
	}
	
	@Override
	public void setMagnification(double mag) {
		float size = (float) (mag*JFLAPPreferences.getDefaultTextSize());
		Font f = this.getFont().deriveFont(Font.PLAIN, size);
		this.setFont(f);
	}

}
