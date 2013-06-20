package view.automata;

import universe.preferences.JFLAPPreferences;

public class SelectedStateDrawer extends StateDrawer {

	public SelectedStateDrawer(){
		super();
		this.setInnerColor(JFLAPPreferences.getSelectedStateColor());
	}
}
