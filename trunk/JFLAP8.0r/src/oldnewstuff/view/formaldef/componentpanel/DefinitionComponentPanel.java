package oldnewstuff.view.formaldef.componentpanel;

import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import oldnewstuff.view.EditingPanel;
import oldnewstuff.view.JFLAPGUIResources;
import oldnewstuff.view.util.SuperMouseAdapter;
import oldnewstuff.view.util.undo.UndoKeeper;


import model.formaldef.components.FormalDefinitionComponent;

public abstract class DefinitionComponentPanel<T extends FormalDefinitionComponent> extends EditingPanel implements JFLAPGUIResources, ChangeListener{

	private T myComponent;
	private JLabel myLabel;

	public DefinitionComponentPanel(T comp, UndoKeeper keeper, boolean editable) {
		super(keeper, editable);
		this.setComponent(comp);
		this.addMouseListener(new SuperMouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON3)
					getMenu().show(event.getComponent(), event.getX(), event.getY());
			}
			
		});
	}
	

	public abstract JPopupMenu getMenu();
	
	public void setComponent(T comp) {
		myComponent = comp;
		comp.addListener(this);
		this.updateLabel();
		this.update();
		this.repaint();
	}
	
	private void updateLabel() {
		if (myLabel == null){
			myLabel = new JLabel();
			myLabel.setFont(DEF_PANEL_FONT);
			this.add(myLabel, 0);
		}
		
		myLabel.setText(this.getComponent().getCharacterAbbr() + " = ");
		this.setToolTipText(this.getComponent().getDescriptionName());
	}

	public T getComponent(){
		return myComponent;
	}


}
