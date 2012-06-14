package oldnewstuff.model.change.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import debug.JFLAPDebug;

import model.undo.IUndoRedo;

public class CompoundUndoableChangeEvent extends UndoableChangeEvent {

	private List<UndoableChangeEvent> mySubEvents;
	private String myName;

	public CompoundUndoableChangeEvent(Object source, String name) {
		super(COMPOUND_EVENT, source);
		myName = name;
		mySubEvents = new ArrayList<UndoableChangeEvent>();
	}

	@Override
	public String getName() {
		return myName;
	}
	public void addSubEvents(UndoableChangeEvent ... sub){
		mySubEvents.addAll(Arrays.asList(sub));
	}
	
	@Override
	public boolean undo() {
		boolean undone = false;
		for (IUndoRedo event: mySubEvents){
			if (event.undo()) undone = true;
		}
		return undone;
	}

	@Override
	public boolean redo() {
		boolean redone = false;
		for (IUndoRedo event: mySubEvents){
			if (event.redo()) redone = true;
		}
		return redone;

	}
	
	@Override
	public boolean applyChange() {
		boolean changed = false;
		for (UndoableChangeEvent event: mySubEvents){
			if (event.applyChange()) changed = true;
		}
		return changed;
	}

}
