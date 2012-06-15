package model.undo;


import java.awt.Container;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.Action;
import javax.swing.undo.UndoManager;

import model.change.events.UndoableChangeEvent;

import debug.JFLAPDebug;


import oldnewstuff.model.change.ChangeEvent;
import oldnewstuff.model.change.ChangeListener;
import util.Copyable;


import errors.BooleanWrapper;





public class UndoKeeper implements ChangeListener{

	private Deque<IUndoRedo> myUndoQueue;
	private Deque<IUndoRedo> myRedoQueue ;
	private Set<UndoKeeperListener> myListeners;
	private boolean amLocked;

	public enum UndoableActionType{
		UNDO,REDO;
	}

	public UndoKeeper() {
		myUndoQueue = new LinkedList<IUndoRedo>();
		myRedoQueue = new LinkedList<IUndoRedo>();
		myListeners = new HashSet<UndoKeeperListener>();
	}

	public <T extends Copyable> void registerChange(IUndoRedo toAdd){
		if (amLocked) return;
		myUndoQueue.push(toAdd);
	}

	public boolean undoLast(){
		return undoLast(1);
	}

	public boolean undoLast(int n){
		return genericAct(n, myUndoQueue, myRedoQueue, UndoableActionType.UNDO);
	}

	public boolean genericAct(int n, Deque<IUndoRedo> from, Deque<IUndoRedo> to, UndoableActionType help) {
		if (amLocked) return false;
		amLocked = true;
		boolean test = true;
		while (!from.isEmpty() && n > 0){
			IUndoRedo wrap = from.peek();
			switch(help){
			case UNDO: test = wrap.undo(); break;
			case REDO: test = wrap.redo(); break;
			}
			if (!test) break;
			to.push(from.pop());
			n--;
			JFLAPDebug.print(wrap.getName());

			broadcastStateChange();
		}
		amLocked = false;
		return test;
	}

	private void broadcastStateChange() {
		for (UndoKeeperListener listener: myListeners){
			listener.keeperStateChanged();
		}
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

	public boolean addUndoListener(UndoKeeperListener listener) {
		return myListeners.add(listener);
	}

	public boolean removeUndoListener(UndoKeeperListener listener){
		return myListeners.remove(listener);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e instanceof UndoableChangeEvent)
			this.registerChange((IUndoRedo) e);
	}

	public void clear() {
		myUndoQueue.clear();
		myRedoQueue.clear();
	}


}
