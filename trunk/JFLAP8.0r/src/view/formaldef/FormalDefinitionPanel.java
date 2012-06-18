package view.formaldef;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import util.JFLAPConstants;
import view.formaldef.componentpanel.ComponentPanelFactory;
import view.formaldef.componentpanel.DefinitionComponentPanel;

import model.formaldef.FormalDefinition;
import model.formaldef.components.FormalDefinitionComponent;
import model.undo.UndoKeeper;

public class FormalDefinitionPanel extends JPanel implements JFLAPConstants {

	


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
