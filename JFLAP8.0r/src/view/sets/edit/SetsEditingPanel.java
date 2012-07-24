package view.sets.edit;

import java.awt.Component;

import javax.swing.BoxLayout;

import model.sets.AbstractSet;
import model.undo.UndoKeeper;
import view.EditingPanel;
import view.sets.state.State;
import debug.JFLAPDebug;

@SuppressWarnings("serial")
public class SetsEditingPanel extends EditingPanel {
	
	private UndoKeeper myKeeper;
	private State myState;
	
	private OptionsMenu myOptionsMenu;
	private SetDefinitionPanel mySetDefinition;
	
	public SetsEditingPanel (UndoKeeper keeper) {
		super(keeper, true);
		myKeeper = keeper;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setSize(getPreferredSize().width, getPreferredSize().height);
		myOptionsMenu = new OptionsMenu(this, myKeeper);
		
		add(myOptionsMenu);
		
	}
	
	public void changeState(State state) {
		myState = state;
	}
	
	public State getCurrentState() {
		return myState;
	}
	
	
	public Component createPanel() {
		return myState.createEditingPanel(myKeeper);
	}
	
	
	public SetDefinitionPanel createSetDefinitionPanel () {
		return new SetDefinitionPanel(myKeeper);
	}
	
	
	public void createFromExistingSet (AbstractSet set) {
		mySetDefinition = new SetDefinitionPanel(myKeeper);
		mySetDefinition.createFromExistingSet(set);
		expandView(mySetDefinition);
	}

	
	
	public String getName() {
		return "Sets Editor";
	}
	
	
	public void expandView (Component comp) {
		myOptionsMenu.setVisible(false);
		this.add(comp);
		this.repaint();
		
		JFLAPDebug.print("Painted new component");
	}
	
}
