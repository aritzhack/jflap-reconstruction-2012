package view.numsets;

import javax.swing.JComponent;
import javax.swing.JTextField;

import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import view.EditingPanel;
import view.grammar.Magnifiable;

@SuppressWarnings("serial")
public class UserDefinedSetEditor extends EditingPanel implements Magnifiable {

	private JTextField nameField;
	private JTextField descriptionField;

	private JTextField elementField;

	public UserDefinedSetEditor(UndoKeeper keeper, boolean editable) {
		super(keeper, editable);
		
		JFLAPUniverse.registerEnvironment(this);
	}


	public JComponent createPane () {

		return null;
	}

	@Override
	public void setMagnification(double mag) {
		// TODO Auto-generated method stub

	}





}
