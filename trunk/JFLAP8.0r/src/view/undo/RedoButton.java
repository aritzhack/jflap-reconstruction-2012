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

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.KeyStroke;

import util.view.ActionLinkedButton;

import model.undo.UndoKeeper;

import errors.BooleanWrapper;






/**
 * Redo time.
 * 
 * @author Henry Qin
 */

public class RedoButton extends ActionLinkedButton {
	
	private ImageIcon myIcon;

	public RedoButton(UndoKeeper keeper){
		super(new RedoAction(keeper));
	}

	@Override
	public String getText() {
		return "";
	}
	
	@Override
	public Icon getIcon() {
		if (myIcon == null){
			String url = System.getProperty("user.dir")+"/src/ICON/redo.jpg";
			myIcon = new ImageIcon(url);
		}
		return myIcon;
	}

}
