package model.undo;

import java.util.LinkedList;


public class CompoundUndoRedo implements IUndoRedo {

	private IUndoRedo myBase;
	private LinkedList<IUndoRedo> myStack;

	public CompoundUndoRedo(IUndoRedo base) {
		myBase = base;
		myStack = new LinkedList<IUndoRedo>();
		add(myBase);
	}

	public void add(IUndoRedo change){
		myStack.addLast(change);
	}
	
	@Override
	public boolean undo() {
		for (int i = myStack.size()-1; i >= 0; i--){
			IUndoRedo undo = myStack.get(i);
			undo.undo();
		}
		return true;
	}

	@Override
	public boolean redo() {
		for (IUndoRedo redo: myStack)
			redo.redo();
		return true;
	}

	@Override
	public String getName() {
		return myBase.getName();
	}

}
