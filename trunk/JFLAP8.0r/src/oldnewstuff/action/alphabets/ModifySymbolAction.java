package oldnewstuff.action.alphabets;


import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;



public class ModifySymbolAction extends AbstractEditSymbolsAction {

	private String newString, oldString;

	public ModifySymbolAction( Alphabet a, Symbol s) {
		super("Modify", a, s);
	}



	@Override
	protected boolean setFields(ActionEvent e) {
		oldString = this.getSymbol(0).getString();
		newString = JOptionPane.showInputDialog(null, 
				"Modify the symbol, click ok to complete",
				oldString);
		if (newString == null)
			return false;
		return true;
	}



	@Override
	public boolean undo() {
		return this.getAlphabet().modify(new Symbol(oldString), 
							new Symbol(newString));
	}



	@Override
	public boolean redo() {
		return this.getAlphabet().modify(new Symbol(newString), 
				new Symbol(oldString));
	}

}
