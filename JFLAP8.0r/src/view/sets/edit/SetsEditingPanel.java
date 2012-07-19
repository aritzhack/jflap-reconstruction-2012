package view.sets.edit;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JComponent;

import debug.JFLAPDebug;

import model.sets.AbstractSet;
import model.sets.CustomFiniteSet;
import model.sets.SetsManager;
import model.sets.elements.Element;
import model.sets.elements.ElementsParser;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.Magnifiable;
import util.view.magnify.MagnifiableButton;
import util.view.magnify.MagnifiablePanel;
import view.EditingPanel;

@SuppressWarnings("serial")
public class SetsEditingPanel extends EditingPanel {
	
	private UndoKeeper myKeeper;
	
	private JComponent myOptionsPanel;
	private SetDefinitionPanel myEditingArea;
	private MagnifiableButton myFinishEditingButton;
	
	public SetsEditingPanel (UndoKeeper keeper) {
		super(keeper, true);
		myKeeper = keeper;
		
//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//		add(showOptionsMenu());
		
		myEditingArea = createSetDefinitionPanel();
		add(myEditingArea);
		add(createFinishButton());
		
	}
	
	public void setOptionsPanelVisible (boolean bool) {
		myOptionsPanel.setVisible(bool);
	}
	
	
	public SetDefinitionPanel createSetDefinitionPanel () {
		return new SetDefinitionPanel(myKeeper);
	}
	
	
	
	public void createFromExistingSet (AbstractSet set) {
		myEditingArea.createFromExistingSet(set);
	}

	
	
	
	private MagnifiableButton createFinishButton() {
		myFinishEditingButton = new MagnifiableButton("Finish", 32);
		myFinishEditingButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = myEditingArea.getSetName();
				String description = myEditingArea.getDescription();
				String elementText = myEditingArea.getElements();
				
				ElementsParser parser = new ElementsParser(elementText);
				try {
					Set<Element> elements = parser.parse();

					CustomFiniteSet set = new CustomFiniteSet(elements);
					set.setName(name);
					set.setDescription(description);
					
					SetsManager.ACTIVE_REGISTRY.add(set);
					
					
					JFLAPDebug.print(set.getCardinality());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				
				JFLAPUniverse.getActiveEnvironment().closeActiveTab();
			}
			
		});
		return myFinishEditingButton;
	
	}
	
	
	public String getName() {
		return "Set Editor";
	}
	
	
	private JComponent showOptionsMenu() {
		
		MagnifiablePanel panel = new MagnifiablePanel();
		panel.setLayout(new GridLayout(0, 1));
		
		MagnifiableButton premade = new MagnifiableButton("Add premade", 
				JFLAPPreferences.getDefaultTextSize());
		
		MagnifiableButton create = new MagnifiableButton("Create new",
				JFLAPPreferences.getDefaultTextSize());
		
		panel.add(premade);
		panel.add(create);
		
		return panel;
	}
	
	
	
	
}
