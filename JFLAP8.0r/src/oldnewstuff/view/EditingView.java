package oldnewstuff.view;

import gui.undo.UndoKeeper;
import gui.undo.UndoableAction;
import util.Copyable;
import model.formaldef.Describable;


public abstract class EditingView<T extends Copyable> extends JFLAPView<T> {

	private UndoKeeper myKeeper;
	private boolean amEditable;
	private T myOldModel;
	
	public EditingView(T model, UndoKeeper keeper, boolean editable){
		super(model, keeper, editable);
		myKeeper = keeper;
		myOldModel = (T) model.copy();
	}
	
	public void initializeComponents(T model, Object ... args) {
		myKeeper = (UndoKeeper) args[0];
		amEditable = (Boolean) args[1];
	}
	
	public void setModel(T model) {
		myOldModel = this.getModel();
		super.setModel(model);
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

	public void registerChange(){
		myKeeper.registerChange(myOldModel, getModel(), this);
		myOldModel = (T) this.getModel().copy();
		this.updateAndRepaint();
	}
	
	public boolean isDirty(){
		return myKeeper.canUndo();
	}

	public abstract void cancelAllEditing();
	
}
