package view.sets.edit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.sets.AbstractSet;
import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.Magnifiable;
import util.view.magnify.MagnifiableLabel;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableTextArea;
import util.view.magnify.MagnifiableTextField;
import view.EditingPanel;

@SuppressWarnings("serial")
public class SetDefinitionPanel extends EditingPanel {
	
	private MagnifiableTextField myNameField;
	private MagnifiableTextField myDescriptionField;
	private MagnifiableTextField myElements;
	
	public SetDefinitionPanel (UndoKeeper keeper) {
		super(keeper, true);
		
		add(createComponents());
		this.setBorder(new LineBorder(Color.red));
		
	}
	
	private JComponent createComponents() {
		myNameField = new MagnifiableTextField(JFLAPPreferences.getDefaultTextSize());
		myDescriptionField = new MagnifiableTextField(JFLAPPreferences.getDefaultTextSize());
		myElements = new MagnifiableTextField(JFLAPPreferences.getDefaultTextSize());
		
		MagnifiablePanel panel = new MagnifiablePanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(new MagnifiableLabel("Name", JFLAPPreferences.getDefaultTextSize()));
		panel.add(myNameField);
		panel.add(new MagnifiableLabel("Description (optional)", JFLAPPreferences.getDefaultTextSize()));
		panel.add(myDescriptionField);
		panel.add(new MagnifiableLabel("Elements (separated with spaces or commas)", 
				JFLAPPreferences.getDefaultTextSize()));
		panel.add(myElements);
		
		return panel;
	}
	
	
	public String getSetName() {
		return myNameField.getText();
	}
	
	public String getDescription() {
		return myDescriptionField.getText();
	}
	
	public String getElements () {
		return myElements.getText();
	}
	
	
	private class InputChangeListener implements DocumentListener {

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			System.out.println("change");
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			System.out.println("insert");
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			System.out.println("remove");
		}
		
	}
	
	
	public void createFromExistingSet (AbstractSet set) {
		myNameField.setText(set.getName());
		myDescriptionField.setText(set.getDescription());
		myElements.setText(set.getSetAsString());
	}

}
