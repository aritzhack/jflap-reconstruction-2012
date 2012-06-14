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





package oldnewstuff.action.windows;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import oldnewstuff.controller.JFLAPController;







/**
 * The <code>CloseButton</code> is a button for removing tabs in
 * an environment. It automatically detects changes in the activation 
 * of panes in the environment, and changes its enabledness whether 
 * a pane in the environment is permanent (i.e. should not be closed).
 * 
 * @see CloseTabAction
 * @author Jinghui Lim
 *
 */
public class CloseButton extends JButton implements PropertyChangeListener 
{
    
    /**
     * Instantiates a <code>CloseButton</code>, and sets its values
     * with {@link #setDefaults()}.
     * @param jflapController 
     * 
     * @param frame the environment to handle the closing for
     */
    public CloseButton(JFLAPController jflapController) 
    {
        super(new CloseTabAction(jflapController, true));
        jflapController.addPropertyChangeListener(this);
        this.setText("");
        setPreferredSize(new Dimension(22, 22));
    }

	@Override
	public void propertyChange(PropertyChangeEvent e) {
			this.setEnabled(this.getAction().isEnabled());
	}

}
