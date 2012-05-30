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





package action.windows;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import controller.JFLAPController;
import controller.menus.MenuConstants;

/**
 * The <CODE>CloseWindowAction</CODE> invokes the close method on the <CODE>EnvironmentFrame</CODE>
 * to which they belong.
 * 
 * @author Thomas Finley
 */

public class CloseWindowAction extends AbstractAction {
	private JFLAPController myController;

	/**
	 * Instantiates a <CODE>CloseWindowAction</CODE>.
	 * 
	 * @param frame
	 *            the <CODE>EnvironmentFrame</CODE> to dismiss when an action
	 *            is registered
	 */
	public CloseWindowAction(JFLAPController c) {
		super("Close", null);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W,
				MenuConstants.getMainMenuMask()));
		myController = c;
	}

	/**
	 * Handles the closing of the window.
	 */
	public void actionPerformed(ActionEvent event) {
		myController.close(true);
	}
}
