package view.automata.tools;

import java.awt.event.MouseEvent;

import model.undo.UndoKeeper;

public class UndoTool extends Tool {
	
	private UndoKeeper keeper;

	public UndoTool(UndoKeeper keeper){
		this.keeper = keeper;
	}

	@Override
	public String getToolTip() {
		// TODO Auto-generated method stub
		return "Undoer - Click anywhere in the editor pane after clicking me.";
	}

	@Override
	public char getActivatingKey() {
		// TODO Auto-generated method stub
		return 'u';
	}

	@Override
	public String getImageURLString() {
		// TODO Auto-generated method stub
		return "/ICON/undo2.jpg";
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		keeper.undoLast();
	}

}
