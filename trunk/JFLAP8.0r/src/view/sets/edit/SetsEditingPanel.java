package view.sets.edit;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BoxLayout;

import model.sets.AbstractSet;
import model.undo.UndoKeeper;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.SizeSlider;
import view.EditingPanel;
import view.sets.state.State;
import debug.JFLAPDebug;

@SuppressWarnings("serial")
public class SetsEditingPanel extends EditingPanel {
	
	private UndoKeeper myKeeper;
	private State myState;
	
	private OptionsMenu myOptionsMenu;
	private MagnifiablePanel myCentralPanel;
	private SetDefinitionPanel mySetDefinition;
	
	public SetsEditingPanel (UndoKeeper keeper, boolean editable) {
		super(keeper, editable);
		myKeeper = keeper;
		
		setLayout(new BorderLayout());
		setSize(getPreferredSize().width, getPreferredSize().height);
		myOptionsMenu = new OptionsMenu(this, myKeeper);
		add(myOptionsMenu, BorderLayout.NORTH);
		
		myCentralPanel = new MagnifiablePanel();
		myCentralPanel.setLayout(new BoxLayout(myCentralPanel, BoxLayout.Y_AXIS));
		add(myCentralPanel, BorderLayout.CENTER);
		
		SizeSlider slider = new SizeSlider();
		slider.distributeMagnification();
		add(slider, BorderLayout.SOUTH);
		
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
		if (myOptionsMenu.isVisible())
			myOptionsMenu.setVisible(false);
		myCentralPanel.add(comp);
		this.repaint();
		
		JFLAPDebug.print("Painted new component");
	}
	
}
