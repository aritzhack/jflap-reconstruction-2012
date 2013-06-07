package view.action.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import util.JFLAPConstants;
import view.action.EnvironmentAction;
import view.environment.JFLAPEnvironment;

public class SaveAsAction extends EnvironmentAction {

	public SaveAsAction(JFLAPEnvironment e) {
		super("Save As", e);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, JFLAPConstants.MAIN_MENU_MASK | KeyEvent.SHIFT_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e, JFLAPEnvironment env) {
		env.save(true);
	}

}
