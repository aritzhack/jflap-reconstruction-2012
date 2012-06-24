package view.action.file;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import view.action.EnvironmentAction;
import view.environment.JFLAPEnvironment;

public class SaveAsAction extends EnvironmentAction {

	public SaveAsAction(JFLAPEnvironment e) {
		super("Save As", e);
	}

	@Override
	public void actionPerformed(ActionEvent e, JFLAPEnvironment env) {
		env.save(true);
	}

}
