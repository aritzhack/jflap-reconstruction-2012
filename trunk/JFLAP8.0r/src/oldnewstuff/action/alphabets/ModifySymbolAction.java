package oldnewstuff.action.alphabets;


import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import model.change.events.SetToEvent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;
import model.undo.IUndoRedo;
import model.undo.UndoKeeper;



public class ModifySymbolAction extends AbstractAction {

	private Symbol mySymbol;
	private SetToEvent<Symbol> myEvent;
	private UndoKeeper myKeeper;

	public ModifySymbolAction(Symbol s, UndoKeeper keeper) {
		super("Modify Symbol");
		mySymbol = s;
		myKeeper = keeper;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		String from = mySymbol.getString(),
				to = JOptionPane.showInputDialog(null, 
				"Modify the symbol, click ok to complete",
				from);
		if (to == null)
			return;
		myEvent = new SetToEvent<Symbol>(mySymbol, 
										  new Symbol(from), 
										  new Symbol(to));
		myKeeper.beginCombine(myEvent);
		myEvent.redo();
		myKeeper.endCombine();
		
	}

}
