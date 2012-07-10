package view.grammar.parsing;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import debug.JFLAPDebug;

import model.algorithms.testinput.parse.Parser;
import model.change.events.AdvancedChangeEvent;

import util.view.magnify.MagnifiablePanel;

public abstract class RunningView extends MagnifiablePanel implements ChangeListener{

	public RunningView(String name, Parser parser){
		setName(name);
		parser.addListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e instanceof AdvancedChangeEvent)
			updateStatus((AdvancedChangeEvent) e);
	}
	
	public abstract void updateStatus(AdvancedChangeEvent e);
	
}
