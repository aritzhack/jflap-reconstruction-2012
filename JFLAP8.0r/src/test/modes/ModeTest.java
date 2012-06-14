package test.modes;

import debug.JFLAPDebug;
import preferences.JFLAPPreferences;
import model.change.ChangeEvent;
import model.change.ChangeListener;
import model.formaldef.components.symbols.Symbol;
import model.formaldef.components.symbols.SymbolString;
import model.formaldef.components.symbols.Terminal;
import model.formaldef.components.symbols.Variable;
import model.grammar.Grammar;
import model.grammar.Production;
import model.undo.UndoKeeper;

public class ModeTest implements ChangeListener{

	public static void main(String[] args) {
		JFLAPPreferences.setUserDefinedMode(false);
		Grammar g = new Grammar();
		UndoKeeper keeper = new UndoKeeper();
		g.addListener(keeper);
		g.getProductionSet().addListener(new ModeTest());
		SymbolString AB = SymbolString.createFromString("AB", true);
		System.out.println(AB);
		System.out.println(g);
		Production p = new Production(new Variable("M"), new Terminal("x"));
		Production p2 = new Production(new Variable("H"), new Terminal("a"));
//		g.getProductionSet().add(p);
		g.getProductionSet().addAll(p2, p);
		System.out.println(g);
		g.getProductionSet().remove(p2);
		System.out.println(g);
		
		keeper.undoLast();
		System.out.println(g);

		keeper.undoLast();
		System.out.println(g);
		
		JFLAPPreferences.setUserDefinedMode(true);
		
		g = new Grammar();
		keeper.clear();
		g.addListener(keeper);
		g.getVariables().add(new Variable("FOOO"));

		

	}

	@Override
	public void stateChanged(ChangeEvent event) {
		System.out.println(event.getName());
	}
}
