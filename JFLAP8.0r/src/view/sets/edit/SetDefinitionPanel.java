package view.sets.edit;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

import model.sets.AbstractSet;
import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableLabel;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableScrollPane;
import util.view.magnify.MagnifiableTextArea;
import util.view.magnify.MagnifiableTextField;
import util.view.thinscroller.ThinScrollBarScrollPane;
import view.EditingPanel;
import view.sets.PropertiesPanel;

@SuppressWarnings("serial")
public class SetDefinitionPanel extends EditingPanel {
	
	private MagnifiableTextField myNameTextField;
	private MagnifiableTextField myDescriptionTextField;
	private JTextComponent myElementTextArea;
	
	public SetDefinitionPanel (UndoKeeper keeper) {
		super(keeper, true);
		
		add(createComponents());
		this.setBorder(new LineBorder(Color.red));
		
	}
	
	private JComponent createComponents() {
		int size = JFLAPPreferences.getDefaultTextSize();
		myNameTextField = new MagnifiableTextField(size);
		myDescriptionTextField = new MagnifiableTextField(size);
		myElementTextArea = new MagnifiableTextArea(size);
		
		MagnifiablePanel panel = new MagnifiablePanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new MagnifiableLabel("Name", JFLAPPreferences.getDefaultTextSize()));
		panel.add(myNameTextField);
		panel.add(new MagnifiableLabel("Description (optional)", JFLAPPreferences.getDefaultTextSize()));
		panel.add(myDescriptionTextField);
		panel.add(new MagnifiableLabel("Elements (separated with spaces or commas)", 
				JFLAPPreferences.getDefaultTextSize()));
		panel.add(myElementTextArea);
		
		MagnifiableScrollPane scroller = new MagnifiableScrollPane(panel);
		return panel;
	}
	
	
//	private JComponent makeElementsPanel() {
//		int size = JFLAPPreferences.getDefaultTextSize();
//		myElementTextArea = new MagnifiableTextField(size);
//		ThinScrollBarScrollPane scroller = new ThinScrollBarScrollPane(size, 
//				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
//				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		scroller.setViewportView(myElementTextArea);
//		this.add(myElementTextArea);
//		
//		MagnifiablePanel panel = new MagnifiablePanel();
//		panel.add(new MagnifiableLabel("{", size));
////		this.add(scroller);
//		panel.add(new MagnifiableLabel("}", size));
//		return myElementTextArea;
//	}
	
	
	
	public String getSetName() {
		return myNameTextField.getText();
	}
	
	public String getDescription() {
		return myDescriptionTextField.getText();
	}
	
	public String getElements () {
		return myElementTextArea.getText();
	}
	
	
	public void createFromExistingSet (AbstractSet set) {
		myNameTextField.setText(set.getName());
		myDescriptionTextField.setText(set.getDescription());
		myElementTextArea.setText(set.getSetAsString());
		add(new PropertiesPanel(set));
	}
	


}
