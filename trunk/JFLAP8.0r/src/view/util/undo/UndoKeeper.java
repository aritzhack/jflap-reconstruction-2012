package view.util.undo;


import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Action;
import javax.swing.undo.UndoManager;

import errors.BooleanWrapper;





public class UndoKeeper{

	private Deque<UndoableAction> myUndoQueue;
	private Deque<UndoableAction> myRedoQueue ;
	private ArrayList<UndoableActionListener> myListeners;

	public enum UndoableActionType{
		UNDO,REDO;
	}

	public UndoKeeper() {
		myListeners = new ArrayList<UndoableActionListener>();
		myUndoQueue = new LinkedList<UndoableAction>();
		myRedoQueue = new LinkedList<UndoableAction>();
	}

	public boolean addUndoableActionListener(UndoableActionListener listener){
		return myListeners.add(listener);
	}
	
	private void broadcastUndoableEvent(UndoableActionEvent event){
		for (UndoableActionListener l: myListeners)
			l.actionPerformed(event);
	}
	
	public void registerAction(UndoableAction act) {
		myUndoQueue.push(act);
		myRedoQueue.clear();
	}

	public void registerAndExecuteAction(UndoableAction action){
		action.redo();
		this.registerAction(action);
		
	}

	
	public boolean undoLast(){
		return undoLast(1);
	}

	public boolean undoLast(int n){
		return genericAct(n, myUndoQueue, myRedoQueue, UndoableActionType.UNDO);
	}

	public boolean genericAct(int n, Deque<UndoableAction> from, Deque<UndoableAction> to, UndoableActionType help) {
		boolean test = true;
//		JFLAPUniverse.getActiveController().getView().clearTemporaryStates();
		while (!from.isEmpty() && n > 0){
			switch(help){
			case UNDO: test = from.peek().undo(); break;
			case REDO: test = from.peek().redo(); break;
			}
			if (!test) break;
			this.broadcastUndoableEvent(new UndoableActionEvent(this, help, from.peek()));
			to.push(from.pop());
			n--;
		}
		return test;
	}

	public boolean redoLast(){
		return redoLast(1);
	}

	public boolean redoLast(int n){
		return genericAct(n, myRedoQueue, myUndoQueue, UndoableActionType.REDO);
	}


	public boolean canRedo() {
		return !myRedoQueue.isEmpty();
	}

	public boolean canUndo() {
		return !myUndoQueue.isEmpty();
	}

	public Deque<UndoableAction> getUndos() {
		return myUndoQueue;
	}
	
	public Deque<UndoableAction> getRedos() {
		return myRedoQueue;
	}
}
