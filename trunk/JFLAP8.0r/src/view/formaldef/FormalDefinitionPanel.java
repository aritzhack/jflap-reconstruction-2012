package view.formaldef;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import view.JFLAPGUIResources;
import view.formaldef.componentpanel.ComponentPanelFactory;
import view.formaldef.componentpanel.DefinitionComponentPanel;

import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;

public class FormalDefinitionPanel extends JPanel implements JFLAPGUIResources{

	
	private FormalDefinition myDefinition;
	private boolean amAlphabetOnly;
	private boolean amEditable;
	private boolean amMinimized;

	public FormalDefinitionPanel(FormalDefinition fd, boolean editable, boolean alphabetOnly) {
		this.setLayout(new GridLayout(0,1));
		this.setDefinition(fd);
		this.setEditable(editable);
		this.setAlphabetOnly(alphabetOnly);
	}

	
	
	public void setAlphabetOnly(boolean alphabetOnly) {
		amAlphabetOnly = alphabetOnly;
		this.updateComponents();
	}


	private void updateComponents() {
		this.removeAll();
		this.updateBorder();
		ArrayList<FormalDefinitionComponent> comps = this.getVisibleDefComponents();
		for (FormalDefinitionComponent comp: comps){
			DefinitionComponentPanel panel = ComponentPanelFactory.createForComponent(comp);
			if (panel == null) continue;
			panel.setEditable(this.isEditable());
			this.add(panel);
		}
		this.repaint();
	}



	private boolean isEditable() {
		return amEditable;
	}



	private void updateBorder() {
		Border b = BorderFactory.createTitledBorder(DEF_PANEL_BORDER, 
													this.getDefinition().toNtupleString());
		this.setBorder(b);
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



	public void setEditable(boolean editable) {
		amEditable = editable;
		updateComponents();
	}

	public void setDefinition(FormalDefinition fd) {
		myDefinition = fd;
		this.updateComponents();
	}
	
}
