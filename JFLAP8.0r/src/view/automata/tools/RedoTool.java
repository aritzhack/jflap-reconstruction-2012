package view.automata.tools;

import java.awt.event.MouseEvent;

import model.undo.UndoKeeper;

public class RedoTool extends Tool {
	
	private UndoKeeper keeper;

	public RedoTool(UndoKeeper keeper){
		this.keeper = keeper;
	}

	@Override
	public String getToolTip() {
		return "Redoer - Click anywhere in the editor pane after clicking me.";
	}

	@Override
	public char getActivatingKey() {
		return 'r';
	}

	@Override
	public String getImageURLString() {
		return "/ICON/redo.jpg";
	}

	@Override
	public void mousePressed(MouseEvent e) {
		keeper.redoLast();
	}
}
