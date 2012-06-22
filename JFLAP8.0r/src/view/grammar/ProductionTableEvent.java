package view.grammar;

import java.util.Collection;


import model.change.events.AddEvent;
import model.change.events.AdvancedUndoableEvent;
import model.change.events.RemoveEvent;
import model.change.events.UndoableEvent;
import model.formaldef.components.SetComponent;
import model.grammar.Production;
import model.undo.IUndoRedo;

public abstract class ProductionTableEvent extends AdvancedUndoableEvent {

	private Production myProduction;
	private int myIndex;


	public ProductionTableEvent(int type, ProductionDataHelper helper, Production p, int i) {
		super(helper, type);
		myProduction = p;
		myIndex = i;
	}
	
	@Override
	public ProductionDataHelper getSource() {
		return (ProductionDataHelper) super.getSource();
	}
	
	@Override
	public boolean undo() {
		return undo(getSource(), myProduction, myIndex);
	}

	public abstract boolean undo(ProductionDataHelper source, Production p,int i);
	
	@Override
	public boolean redo() {
		return redo(getSource(), myProduction, myIndex);
	}

	public abstract boolean redo(ProductionDataHelper source, Production p, int i);
	
}
