package view.formaldef.componentpanel;

import javax.swing.JLabel;
import javax.swing.JPanel;

import view.JFLAPGUIResources;

import model.formaldef.components.FormalDefinitionComponent;

public abstract class DefinitionComponentPanel<T extends FormalDefinitionComponent> extends JPanel implements JFLAPGUIResources{

	private T myComponent;
	private boolean amEditable;
	private JLabel myLabel;

	public DefinitionComponentPanel(T comp, boolean editable) {
		this.setComponent(comp);
		this.setEditable(editable);
	}
	
	public void setComponent(T comp) {
		myComponent = comp;
		this.updateLabel();
		this.updateParts();
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

	public void setEditable(boolean editable) {
		amEditable = editable;
		updateParts();
	}

	public boolean isEditable(){
		return amEditable;
	}
	
	public abstract void updateParts();

	
	
}
