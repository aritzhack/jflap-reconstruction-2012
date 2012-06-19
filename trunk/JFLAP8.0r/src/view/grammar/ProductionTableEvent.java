package view.grammar;

import java.util.Collection;


import model.change.events.AddEvent;
import model.change.events.AdvancedUndoableEvent;
import model.change.events.RemoveEvent;
import model.change.events.UndoableEvent;
import model.formaldef.components.SetComponent;
import model.grammar.Production;
import model.undo.IUndoRedo;

public class ProductionTableEvent extends AdvancedUndoableEvent {

	private Production myProduction;
	private int myIndex;


	public ProductionTableEvent(ProductionDataHelper helper, Production p, int i) {
		this(helper, ITEM_ADDED, i);
		myProduction = p;
	}
	
	public ProductionTableEvent(ProductionDataHelper helper, int i){
		this(helper, ITEM_REMOVED, i);
		
	}
	
	private ProductionTableEvent(ProductionDataHelper helper, int type,
			int i) {
		super(helper, type);
		myIndex = i;
	}

	@Override
	public ProductionDataHelper getSource() {
		return (ProductionDataHelper) super.getSource();
	}
	
	@Override
	public boolean undo() {
		return getSource();
	}

	@Override
	public boolean redo() {
		return myEvent.redo();
	}


	@Override
	public String getName() {
		switch(this.getType()){
		case ITEM_ADDED: 
		return myEvent.getName();
	}
	
}
