package oldnewstuff.view.tools;

import gui.undo.RedoButton;
import gui.undo.UndoButton;
import gui.undo.UndoKeeper;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import oldview.formaldef.FormalDefinitionEditorView;
import oldview.tools.Tool;

public class JFLAPToolBar extends JToolBar implements ActionListener{


	private Tool myActiveTool;
	private List<ToolButton> myButtons;
	private List<ToolChangeListener> myListeners;

	public JFLAPToolBar(UndoKeeper keeper, Tool ... tools){
		myButtons = new ArrayList<ToolButton>();
		myListeners = new ArrayList<ToolChangeListener>();
		this.addAll(tools);
		this.add(new UndoButton(keeper));
		this.add(new RedoButton(keeper));
	}

	public void add(Tool tool) {
		ToolButton button = new ToolButton(tool);
		myButtons.add(button);
		this.add(button);
		button.setToolTipText(tool.getShortcutToolTip());
		button.addActionListener(this);
		if (this.getActiveTool() == null)  this.setActiveTool(button);
	}

	public void addAll(Tool ... tools){
		for (Tool t: tools)
			this.add(t);
	}

	/**
	 * If a tool is clicked, sets the new current tool.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.setActiveTool((ToolButton)e.getSource());
	}

	private void setActiveTool(ToolButton source) {
		this.toggleAllOff();
		source.setSelected(true);
//		JFLAPUniverse.getActiveController().setCursor(source.getTool().getCursor());
		this.broadcastToolChangeEvent(new ToolEvent(myActiveTool, source.getTool()));
		myActiveTool = source.getTool();
	}

	private void broadcastToolChangeEvent(ToolEvent toolEvent) {
		for (ToolChangeListener tcl: myListeners)
			tcl.toolChanged(toolEvent);
	}

	public Tool getActiveTool(){
		return myActiveTool;
	}

	private void toggleAllOff() {
		for (ToolButton tb: myButtons)
			tb.setSelected(false);
	}

	public void addToolChangeListener(ToolChangeListener tcl) {
		myListeners.add(tcl);
		tcl.toolChanged(new ToolEvent(getActiveTool(), getActiveTool()));
	}


}
