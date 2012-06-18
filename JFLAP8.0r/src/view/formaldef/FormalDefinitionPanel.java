package view.formaldef;

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
import view.formaldef.componentpanel.ComponentPanelFactory;
import view.formaldef.componentpanel.DefinitionComponentPanel;

import model.formaldef.Describable;
import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.undo.UndoKeeper;

public class FormalDefinitionPanel extends JPanel implements JFLAPGUIResources {

	


	public FormalDefinitionPanel(FormalDefinition fd, UndoKeeper keeper, boolean editable) {
		this.setLayout(new GridLayout(0,1));
		Border b = BorderFactory.createTitledBorder(DEF_PANEL_BORDER, 
				fd.toNtupleString());
		this.setBorder(b);
		for (FormalDefinitionComponent comp: fd.getComponents()){
			DefinitionComponentPanel panel = 
					ComponentPanelFactory.createForComponent(comp, keeper, editable);
			if (panel == null) continue;
			
			this.add(panel);
		}
		
	}


}
