package view.action.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import debug.JFLAPDebug;

import util.JFLAPConstants;
import view.action.EnvironmentAction;
import view.environment.JFLAPEnvironment;

public class SaveAction extends EnvironmentAction {


	public SaveAction(JFLAPEnvironment e) {
		super("Save",e);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, JFLAPConstants.MAIN_MENU_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e, JFLAPEnvironment env) {
		boolean saveAs = !env.hasFile();
		env.save(saveAs);
	}
	

}
