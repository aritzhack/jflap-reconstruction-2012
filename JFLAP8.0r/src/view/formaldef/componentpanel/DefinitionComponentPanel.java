package view.formaldef.componentpanel;

import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import oldnewstuff.view.EditingPanel;
import oldnewstuff.view.JFLAPGUIResources;
import oldnewstuff.view.util.SuperMouseAdapter;


import model.formaldef.components.FormalDefinitionComponent;
import model.undo.UndoKeeper;

public abstract class DefinitionComponentPanel<T extends FormalDefinitionComponent> extends JPanel implements JFLAPGUIResources, ChangeListener{

	private JLabel myLabel;

	public DefinitionComponentPanel(T comp, UndoKeeper keeper) {
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		comp.addListener(this);
		comp.addListener(keeper);
		myLabel = new JLabel();
		myLabel.setText(comp.getCharacterAbbr() + " = ");
		this.add(myLabel);
		this.setToolTipText(comp.getDescriptionName());
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		update(e);
		repaint();
	}


	public abstract void update(ChangeEvent e);

}
