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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import view.JFLAPGUIResources;

import controller.JFLAPController;
import controller.menus.MenuConstants;

/**
 * The <CODE>CloseAction</CODE> is an action for removing tabs in an
 * environment. It automatically detects changes in the activation of panes in
 * the environment, and changes its enabledness whether or not a pane in the
 * environment is permanent (i.e. should not be closed).
 * 
 * @author Thomas Finley
 */

public class CloseTabAction extends AbstractAction implements PropertyChangeListener, JFLAPGUIResources {
	
	private JFLAPController myController;

	/**
	 * Instantiates a <CODE>CloseAction</CODE>.
	 * @param usingIcon 
	 * 
	 * @param environment
	 *            the environment to handle the closing for
	 */
	public CloseTabAction(JFLAPController c, boolean usingIcon) {
		super("Dismiss Tab", getIcon(usingIcon));
		putValue(ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, MenuConstants.getMainMenuMask()));
		myController = c;
		myController.addPropertyChangeListener(this);
	}


	private static Icon getIcon(boolean usingIcon) {
		return usingIcon ? new ImageIcon(CloseTabAction.class.getResource("/ICON/x.gif")) : null;
	}


	/**
	 * Handles the closing on the environment.
	 * 
	 * @param e
	 *            the action event
	 */
	public void actionPerformed(ActionEvent e) {
		myController.closeActiveTab(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getPropertyName().equals(TAB_CHANGED)){
			this.setEnabled(myController.canCloseATab());
		}
		
	}
}
