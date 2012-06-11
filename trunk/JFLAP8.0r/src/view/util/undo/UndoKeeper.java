package view.util.undo;


import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Action;
import javax.swing.undo.UndoManager;

import view.EditingPanel;
import view.util.undo.old.UndoableActionEvent;
import view.util.undo.old.UndoableActionListener;

import errors.BooleanWrapper;





public class UndoKeeper{

	private Deque<ActionWrapper> myUndoQueue;
	private Deque<ActionWrapper> myRedoQueue ;
	private ArrayList<UndoableActionListener> myListeners;

	public enum UndoableActionType{
		UNDO,REDO;
	}

	public UndoKeeper() {
		myListeners = new ArrayList<UndoableActionListener>();
		myUndoQueue = new LinkedList<ActionWrapper>();
		myRedoQueue = new LinkedList<ActionWrapper>();
	}

	public boolean addUndoableActionListener(UndoableActionListener listener){
		return myListeners.add(listener);
	}
	
	private void broadcastUndoableEvent(UndoableActionEvent event){
		for (UndoableActionListener l: myListeners)
			l.actionPerformed(event);
	}
	
	public void registerAction(UndoableAction act, EditingPanel pane) {
		ActionWrapper wrap = new ActionWrapper(act, pane);
		myUndoQueue.push(wrap);
		myRedoQueue.clear();
	}

	public void registerAndExecuteAction(UndoableAction action, EditingPanel pane){
		this.registerAction(action, pane);
		action.redo();
		pane.update();
		
	}

	
	public boolean undoLast(){
		return undoLast(1);
	}

	public boolean undoLast(int n){
		return genericAct(n, myUndoQueue, myRedoQueue, UndoableActionType.UNDO);
	}

	public boolean genericAct(int n, Deque<ActionWrapper> from, Deque<ActionWrapper> to, UndoableActionType help) {
		boolean test = true;
//		JFLAPUniverse.getActiveController().getView().clearTemporaryStates();
		while (!from.isEmpty() && n > 0){
			ActionWrapper wrap = from.peek();
			switch(help){
			case UNDO: test = wrap.action.undo(); break;
			case REDO: test = wrap.action.redo(); break;
			}
			if (!test) break;
			this.broadcastUndoableEvent(new UndoableActionEvent(this, help, from.peek().action));
			to.push(from.pop());
			n--;
			wrap.target.update();
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

	private class ActionWrapper{

		private UndoableAction action;
		private EditingPanel target;

		public ActionWrapper(UndoableAction act, EditingPanel pane) {
			action = act;
			target = pane;
		}
		
	}

}
