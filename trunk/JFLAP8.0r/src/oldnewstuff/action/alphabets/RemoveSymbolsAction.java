package oldnewstuff.action.alphabets;


import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.JOptionPane;

import oldnewstuff.action.formaldef.DefinitionCloningAction;
import oldnewstuff.view.formaldef.FormalDefinitionView;
import oldnewstuff.view.formaldef.UsesDefinition;





import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.symbols.Symbol;




public class RemoveSymbolsAction extends AbstractEditSymbolsAction {



	private FormalDefinition myOldFD;
	private FormalDefinitionView myPanel;
	private FormalDefinition myNewFD;

	public RemoveSymbolsAction(Alphabet a, FormalDefinitionView fd, Symbol ... s) {
		super("Remove", a, s);
		myPanel = fd;
		myNewFD = myPanel.getDefinition();

	}

	@Override
	public boolean undo() {
		myPanel.setDefinition(myOldFD);
		return true;
	}

	@Override
	public boolean redo() {
		if (myOldFD == null){
			myOldFD = (FormalDefinition) myPanel.getDefinition().copy();
		}
		return this.getAlphabet().removeAll(Arrays.asList(this.getSymbols()));
	}




}
