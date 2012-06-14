package oldnewstuff.view;

import java.io.File;

import javax.swing.JPanel;

import oldnewstuff.view.util.undo.UndoKeeper;
import oldnewstuff.view.util.undo.UndoableAction;

import file.xml.XMLCodec;
import file.xml.XMLTransducer;

import model.automata.transducers.Transducer;
import model.formaldef.Describable;

import util.Copyable;

public abstract class EditingPanel<T extends Describable> extends JPanel implements Updateable, Saveable{

	private UndoKeeper myKeeper;
	private boolean amEditable;
	private boolean amDirty;
	
	public EditingPanel(UndoKeeper keeper, boolean editable){
		myKeeper = keeper;
	}
	
	public UndoKeeper getKeeper() {
		return myKeeper;
	}
	public boolean isEditable(){
		return amEditable;
	}
	
	public void setEditable(boolean editable){
		amEditable = editable;
	}

	public void registerAndApply(UndoableAction action){
		myKeeper.registerAndExecuteAction(action, this);
		this.setDirty(true);
	}
	
	private void setDirty(boolean dirty) {
		amDirty = dirty;
	}


	public abstract T getObjectToSave();
	
	@Override
	public String getName() {
		return getObjectToSave().getDescriptionName();
	}
	
	@Override
	public boolean shouldBeSaved() {
		return amDirty;
	}

}
