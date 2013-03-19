package view.action.lsystem;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import model.lsystem.LSystemException;
import model.lsystem.NLSystem;

import universe.JFLAPUniverse;
import view.environment.JFLAPEnvironment;
import view.lsystem.LSystemRenderView;
import view.lsystem.LSystemInputView;


public class LSystemRenderAction extends AbstractAction {
	private LSystemInputView myView;
	
	public LSystemRenderAction(LSystemInputView view){
		super("Render L-System");
		myView = view;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		NLSystem l = myView.getLSystem();
		
		if(l == null)
			return;
		if(l.getAxiom().size() == 0)
			throw new LSystemException("The axiom must have one or more symbols.");
		
		
		LSystemRenderView render = new LSystemRenderView(l);
		JFLAPEnvironment environ = JFLAPUniverse.getActiveEnvironment();
		environ.addSelectedComponent(render);
	}

}
