package view.action.file;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import view.action.EnvironmentAction;
import view.environment.JFLAPEnvironment;

public class SaveAction extends EnvironmentAction {


	public SaveAction(JFLAPEnvironment e) {
		super("Save",e);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	
	}

	@Override
	public void actionPerformed(ActionEvent e, JFLAPEnvironment env) {
		boolean saveAs = !env.hasFile();
		env.save(saveAs);
	}
	

}
