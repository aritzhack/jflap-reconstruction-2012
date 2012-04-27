/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





package view.util.undo;


import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.KeyStroke;

import errors.BooleanWrapper;






/**
 * Redo time.
 * 
 * @author Henry Qin
 */

public class RedoButton extends UndoRelatedButton {
	
	public RedoButton(UndoKeeper keeper){
		super(new RedoAction(keeper));
	}

	@Override
	public String getText() {
		return "";
	}
	
	@Override
	public Icon getIcon() {
		java.net.URL url = getClass().getResource("/ICON/redo.jpg");
		return new javax.swing.ImageIcon(url);
	}


	@Override
	protected Iterable<? extends Action> getActionList() {
		return ((UndoKeeperAction)this.getAction()).getKeeper().getRedos();
	}
	
	@Override
	protected boolean doMultiAction(int n) {
		return ((UndoKeeperAction)this.getAction()).getKeeper().redoLast(n);
	}

}
