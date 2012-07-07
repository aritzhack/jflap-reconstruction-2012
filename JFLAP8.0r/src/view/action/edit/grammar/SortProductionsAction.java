package view.action.edit.grammar;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.AbstractAction;

import model.change.events.UndoableEvent;
import model.grammar.Production;
import model.undo.IUndoRedo;
import model.undo.UndoKeeper;

import view.action.UndoingAction;
import view.grammar.ProductionDataHelper;
import view.grammar.ProductionTableModel;

public class SortProductionsAction extends UndoingAction implements IUndoRedo{

	private ProductionTableModel myModel;
	private Production[] oldOrdering;
	private Production[] newOrdering;

	public SortProductionsAction(UndoKeeper keeper, ProductionTableModel model) {
		super("Sort Productions", keeper);
		myModel = model;
	}

	@Override
	public IUndoRedo createEvent(ActionEvent e) {
		oldOrdering = myModel.getOrderedProductions();
		newOrdering = Arrays.copyOf(oldOrdering, oldOrdering.length);
		Arrays.sort(newOrdering);
		return this;
	}

	@Override
	public boolean undo() {
		return myModel.applyOrdering(oldOrdering);
	}

	@Override
	public boolean redo() {
		return myModel.applyOrdering(newOrdering);
	}

	@Override
	public String getName() {
		return (String) super.getValue(NAME);
	}
	

}