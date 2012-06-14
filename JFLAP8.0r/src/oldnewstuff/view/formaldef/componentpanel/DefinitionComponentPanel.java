package oldnewstuff.view.formaldef.componentpanel;

import gui.undo.UndoKeeper;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import oldnewstuff.view.EditingView;
import oldnewstuff.view.JFLAPGUIResources;
import oldnewstuff.view.formaldef.ExplicitDefinitionPanel;
import oldnewstuff.view.util.SuperMouseAdapter;
import preferences.JFLAPPreferences;




import model.formaldef.components.FormalDefinitionComponent;

public abstract class DefinitionComponentPanel<T extends FormalDefinitionComponent> extends EditingView<T> implements JFLAPGUIResources{

	private T myComponent;
	private JLabel myLabel;
	private ExplicitDefinitionPanel myParent;

	public DefinitionComponentPanel(T comp, UndoKeeper keeper, boolean editable) {
		super(comp, keeper, editable);
		this.setComponent(comp);
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.addMouseListener(new SuperMouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON3)
					getMenu().show(event.getComponent(), event.getX(), event.getY());
			}
			
		});
	}
	
	public ExplicitDefinitionPanel getDefinitionParent(){
		return myParent;
	}
	
	public void setDefinitionParent(ExplicitDefinitionPanel parent){
		myParent = parent;
	}

	public abstract JPopupMenu getMenu();
	
	public void setComponent(T comp) {
		myComponent = comp;
		this.updateLabel();
		updateAndRepaint();
	}
	
	private void updateLabel() {
		if (myLabel == null){
			myLabel = new JLabel();
			myLabel.setFont(JFLAPPreferences.getFormalDefinitionFont());
			this.add(myLabel, 0);
		}
		String label = ""; 
		if (JFLAPPreferences.useDefinitionAbbreviations()){
			label = label + this.getComponent().getCharacterAbbr();
			this.setToolTipText(this.getComponent().getDescriptionName());
		}
		else{
			label = label + this.getComponent().getDescriptionName();
			this.setToolTipText(this.getComponent().getDescription());
		}
			
		myLabel.setText( label + " = ");
		myLabel.setFont(JFLAPPreferences.getFormalDefinitionFont());
	}

	public T getComponent(){
		return myComponent;
	}


}
