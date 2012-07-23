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





package view.action.windows;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import oldnewstuff.controller.menus.MenuConstants;
import util.JFLAPConstants;
import view.environment.JFLAPEnvironment;



/**
 * The <CODE>CloseAction</CODE> is an action for removing tabs in an
 * environment. It automatically detects changes in the activation of panes in
 * the environment, and changes its enabledness whether or not a pane in the
 * environment is permanent (i.e. should not be closed).
 * 
 * @author Thomas Finley
 */

public class CloseTabAction extends AbstractAction {
	
	private JFLAPEnvironment myEnvironment;

	/**
	 * Instantiates a <CODE>CloseAction</CODE>.
	 * @param usingIcon 
	 * 
	 * @param environment
	 *            the environment to handle the closing for
	 */
	public CloseTabAction(JFLAPEnvironment e, boolean usingIcon) {
		super("Dismiss Tab", getIcon(usingIcon));
		putValue(ACCELERATOR_KEY, 
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, MenuConstants.getMainMenuMask()));
		myEnvironment = e;
	}


	private static Icon getIcon(boolean usingIcon) {
		return usingIcon ? new ImageIcon(JFLAPConstants.RESOURCE_ROOT + "/ICON/x.gif") : null;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		myEnvironment.closeActiveTab();
	}
	
	@Override
	public boolean isEnabled() {
		return myEnvironment.getTabCount() > 1;
	}
	

}