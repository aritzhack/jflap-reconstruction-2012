package view.numsets.create;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.undo.UndoKeeper;
import view.EditingPanel;
import view.grammar.Magnifiable;

@SuppressWarnings("serial")
public class UserDefinedSetEditor extends EditingPanel implements Magnifiable {

	private JComponent myName;
	
	private JComponent myDescription;

	private JComponent myElements;
	

	public UserDefinedSetEditor(UndoKeeper keeper, boolean editable) {
		super(keeper, editable);
	}
	
	public UserDefinedSetEditor () {
		this(new UndoKeeper(), true);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(createNameComponent());
//		add(createDescriptionComponent());
//		add(createElementEntryComponent());
	}


	public JComponent createNameComponent () {
		JPanel namePanel = new JPanel();
		
		JLabel label = new JLabel("Name");
		myName = new JTextField((int)namePanel.getPreferredSize().getWidth());
		
		namePanel.add(label);
		namePanel.add(myName);
		
		return namePanel;
	}
	
	
	public JComponent createDescriptionComponent() {
		
		return null;
	}
	
	
	public JComponent createElementEntryComponent () {
		
		return null;
	}

	@Override
	public void setMagnification(double mag) {
		// TODO Auto-generated method stub

	}





}
