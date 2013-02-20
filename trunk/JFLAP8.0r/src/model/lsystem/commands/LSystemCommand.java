package model.lsystem.commands;

import model.formaldef.Describable;
import model.grammar.Terminal;

public abstract class LSystemCommand extends Terminal implements Describable {

	public LSystemCommand(String s) {
		super(s);
	}

}
