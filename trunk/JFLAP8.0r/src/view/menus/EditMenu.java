package view.menus;

import java.awt.Component;

import javax.swing.Action;
import javax.swing.JMenu;

import model.undo.UndoKeeper;

import util.ISelector;
import view.EditingPanel;
import view.action.edit.DeleteAction;
import view.action.edit.grammar.SortProductionsAction;
import view.environment.JFLAPEnvironment;
import view.environment.TabChangeListener;
import view.environment.TabChangedEvent;
import view.grammar.GrammarView;
import view.grammar.ProductionTable;
import view.grammar.ProductionTableModel;
import view.undoing.UndoRelatedMenuItem;
import view.undoing.redo.MenuRedoAction;
import view.undoing.redo.RedoAction;
import view.undoing.redo.RedoButton;
import view.undoing.undo.MenuUndoAction;
import view.undoing.undo.UndoAction;
import view.undoing.undo.UndoButton;

public class EditMenu extends JMenu implements TabChangeListener{

	public EditMenu(JFLAPEnvironment e) {
		super("Edit");
		e.addTabListener(this);
		this.update(e.getCurrentView());
	}
	
	public Action[] createActions(JFLAPEnvironment e) {
		return new Action[]{new MenuUndoAction(e),
				new MenuRedoAction(e)};
	}

	@Override
	public void tabChanged(TabChangedEvent e) {
		update(e.getCurrentView());

	}

	private void update(Component view) {
		this.removeAll();
		UndoKeeper keeper;
		try {
			keeper = ((EditingPanel) view).getKeeper();
		}catch(Exception ex){
			ex.printStackTrace();
			keeper = null;
		}
		this.add(new UndoRelatedMenuItem(new RedoAction(keeper)));
		this.add(new UndoRelatedMenuItem(new UndoAction(keeper)));		
		
		//Grammar Options
		if (view instanceof GrammarView){
			GrammarView v = (GrammarView) view;
			ProductionTable table = (ProductionTable) v.getCentralPanel();
			this.add(new DeleteAction(table));
			this.add(new SortProductionsAction(keeper, 
					(ProductionTableModel) table.getModel()));
		}
			
			
	}

}
