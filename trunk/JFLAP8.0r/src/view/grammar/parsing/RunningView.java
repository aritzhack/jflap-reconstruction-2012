package view.grammar.parsing;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import debug.JFLAPDebug;

import model.algorithms.testinput.parse.Parser;
import model.change.events.AdvancedChangeEvent;

import util.view.magnify.MagnifiablePanel;

public class RunningView extends MagnifiablePanel{

	public RunningView(String name, Parser parser){
		setName(name);
	}
	
}
