package oldnewstuff.view.formaldef.componentpanel.symbols;

import gui.undo.UndoKeeper;
import gui.undo.UndoingActionMenu;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import oldnewstuff.action.formaldef.SetSpecialSymbolAction;
import oldnewstuff.view.formaldef.componentpanel.DefinitionComponentPanel;
import preferences.JFLAPPreferences;





import model.formaldef.components.symbols.SpecialSymbol;

public class SpecialSymbolPanel extends DefinitionComponentPanel<SpecialSymbol> {

	private JLabel myLabel;
	
	public SpecialSymbolPanel(SpecialSymbol comp, UndoKeeper keeper,
			boolean editable) {
		super(comp, keeper, editable);
		
		
	}

	@Override
	public void update(SpecialSymbol s) {
		if (myLabel != null)
			this.remove(myLabel);	
		this.add(myLabel = new JLabel());
		myLabel.setText(this.getComponent().toSymbolObject().toString());
		myLabel.setFont(JFLAPPreferences.getFormalDefinitionFont());
	}

	@Override
	public JPopupMenu getMenu() {
		return new UndoingActionMenu(getKeeper(), this, 
				new SetSpecialSymbolAction());
	}

}
