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





package view.undo;


import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.KeyStroke;

import util.view.ActionLinkedButton;

import model.undo.UndoKeeper;

import errors.BooleanWrapper;
import errors.JFLAPError;



/**
 * First, let's make it work, then we'll make the interface so you don't have to click undo and then click randomly.
 * 
 * @author Henry Qin
 */

public class UndoButton extends ActionLinkedButton {

	public UndoButton(UndoKeeper keeper) {
		super(new UndoAction(keeper));
	}


	@Override
	public String getText() {
		return "";
	}



	@Override
	public Icon getIcon() {
		return new ImageIcon("/ICON/undo2.jpg");
	}

}
