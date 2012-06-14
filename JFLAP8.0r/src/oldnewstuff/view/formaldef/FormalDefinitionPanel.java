package oldnewstuff.view.formaldef;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import oldnewstuff.view.EditingPanel;
import oldnewstuff.view.JFLAPGUIResources;
import oldnewstuff.view.formaldef.componentpanel.ComponentPanelFactory;
import oldnewstuff.view.formaldef.componentpanel.DefinitionComponentPanel;
import oldnewstuff.view.util.undo.UndoKeeper;

import model.formaldef.Describable;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;

public class FormalDefinitionPanel extends EditingPanel implements JFLAPGUIResources {

	
	private FormalDefinition myDefinition;
	private boolean amAlphabetOnly;
	private boolean amMinimized;

	public FormalDefinitionPanel(FormalDefinition fd, UndoKeeper keeper, boolean editable, boolean alphabetOnly) {
		super(keeper, editable);
		this.setLayout(new GridLayout(0,1));
		this.setDefinition(fd);
		this.setEditable(editable);
		this.setAlphabetOnly(alphabetOnly);
	}

	
	
	public void setAlphabetOnly(boolean alphabetOnly) {
		amAlphabetOnly = alphabetOnly;
		this.update();
	}
	
	private void updateBorder() {
		Border b = BorderFactory.createTitledBorder(DEF_PANEL_BORDER, 
													this.getDefinition().toNtupleString());
		this.setBorder(b);
	}

	@Override
	public void update() {
		this.removeAll();
		this.updateBorder();
		ArrayList<FormalDefinitionComponent> comps = this.getVisibleDefComponents();
		for (FormalDefinitionComponent comp: comps){
			DefinitionComponentPanel panel = 
					ComponentPanelFactory.createForComponent(comp, this.getKeeper(), this.getDefinition());
			if (panel == null) continue;
			panel.setEditable(this.isEditable());
			this.add(panel);
		}
		this.repaint();
	}

	@Override
	public void setEditable(boolean editable) {
		super.setEditable(editable);
		update();
	}
	
	private ArrayList<FormalDefinitionComponent> getVisibleDefComponents() {
		ArrayList<FormalDefinitionComponent> comps = new ArrayList<FormalDefinitionComponent>(); 
		if (this.isMinimized())
			return comps;
		
		if (this.isAlphabetOnly())
			comps.addAll(this.getDefinition().getAlphabets());
		else
			comps.addAll(Arrays.asList(this.getDefinition().getComponents()));
		
		return comps;
	}



	public boolean isMinimized() {
		return amMinimized;
	}



	public boolean isAlphabetOnly() {
		return amAlphabetOnly;
	}



	public FormalDefinition getDefinition() {
		return myDefinition;
	}



	public void setDefinition(FormalDefinition fd) {
		myDefinition = fd;
		this.update();
	}


	@Override
	public Describable getObjectToSave() {
		return myDefinition;
	}


}
