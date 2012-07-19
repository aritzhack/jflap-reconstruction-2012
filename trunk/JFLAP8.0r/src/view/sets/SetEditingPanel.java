package view.sets;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableLabel;
import util.view.magnify.MagnifiableTextArea;
import util.view.magnify.MagnifiableTextField;
import view.EditingPanel;

public class SetEditingPanel extends EditingPanel {
	
	private JTextField myNameField;
	private JTextField myDescriptionField;
	private JTextArea myElementsField;
	
	private JComponent myCentralPanel;
	
	private JCheckBox myFiniteField;

	
	public SetEditingPanel(UndoKeeper keeper, boolean editable) {
		super(keeper, editable);
		
		setLayout(new BorderLayout());
		
		myCentralPanel = createCentralPanel();
		add(myCentralPanel, BorderLayout.CENTER);
		
	}

	
	private JComponent createCentralPanel () {
		
		JPanel outer = new JPanel();
		outer.setLayout(new BoxLayout(outer, BoxLayout.LINE_AXIS));
		
		MagnifiableLabel nameLabel = new MagnifiableLabel(
				"Name", JFLAPPreferences.getDefaultTextSize());
		MagnifiableLabel descriptionLabel = new MagnifiableLabel(
				"Description", JFLAPPreferences.getDefaultTextSize());
		MagnifiableLabel elementsLabel = new MagnifiableLabel(
				"Enter elements separated by commas and/or spaces", 
				JFLAPPreferences.getDefaultTextSize());
		
		myNameField = new MagnifiableTextField(JFLAPPreferences.getDefaultTextSize());
		myDescriptionField = new MagnifiableTextField(JFLAPPreferences.getDefaultTextSize());
		myElementsField = new MagnifiableTextArea(JFLAPPreferences.getDefaultTextSize());
		myFiniteField = new JCheckBox("Infinite");
		
		outer.add(nameLabel);
		outer.add(myNameField);
		outer.add(descriptionLabel);
		outer.add(myDescriptionField);
		outer.add(elementsLabel);
		outer.add(myElementsField);
		outer.add(myFiniteField);
		
		
		return outer;
	}


	@Override
	public String getName () {
		return "Set Editor";
	}
	
}

