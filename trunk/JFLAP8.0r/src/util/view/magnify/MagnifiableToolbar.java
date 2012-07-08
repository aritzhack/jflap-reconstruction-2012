package util.view.magnify;

import javax.swing.JToolBar;

public abstract class MagnifiableToolbar extends JToolBar implements Magnifiable {

	public MagnifiableToolbar() {

	}

	public MagnifiableToolbar(int arg0) {
		super(arg0);

	}

	public MagnifiableToolbar(String arg0) {
		super(arg0);

	}

	public MagnifiableToolbar(String arg0, int arg1) {
		super(arg0, arg1);
	}

}
