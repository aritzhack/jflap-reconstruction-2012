package action.alphabets;


import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.JOptionPane;

import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;

import view.formaldef.UsesDefinition;

import action.formaldef.DefinitionCloningAction;


public class RemoveSymbolsAction extends AbstractEditSymbolsAction {



	public RemoveSymbolsAction(Alphabet a, Symbol ... s) {
		super("Remove", a, s);
	}

	@Override
	protected boolean setFields(ActionEvent e) {
		
		return true;
	}

	@Override
	public boolean undo() {
		return this.getAlphabet().removeAll(Arrays.asList(this.getSymbols()));
	}

	@Override
	public boolean redo() {
		// TODO Auto-generated method stub
		return false;
	}




}
