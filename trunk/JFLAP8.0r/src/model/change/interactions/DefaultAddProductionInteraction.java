package model.change.interactions;

import java.util.List;
import java.util.Set;

import debug.JFLAPDebug;

import model.change.ChangeDistributor;
import model.change.ChangeEvent;
import model.change.events.CompoundUndoableChangeEvent;
import model.change.events.SetComponentEvent;
import model.change.events.UndoableChangeEvent;
import model.formaldef.components.symbols.Symbol;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;

public class DefaultAddProductionInteraction extends Interaction{

	private Grammar myGrammar;

	public DefaultAddProductionInteraction(Grammar distributor) {
		super(ITEM_ADD, distributor);
		myGrammar = distributor;
	}

	@Override
	protected void addAndApplyInteractions(ChangeEvent e, CompoundUndoableChangeEvent event) {
		SetComponentEvent<Production> e2 = (SetComponentEvent<Production>) e;
		List<Production> items = e2.getItems();
		VariableAlphabet vars = myGrammar.getVariables();
		TerminalAlphabet terms = myGrammar.getTerminals();
		for (Production p : items){
			
			Symbol[] toAdd = p.getVariablesUsed().toArray(new Symbol[0]);
			UndoableChangeEvent temp = vars.createAddEvent(toAdd);
			temp.applyChange();
			event.addSubEvents(temp);
			
			toAdd = p.getTerminalsUsed().toArray(new Symbol[0]);
			temp = terms.createAddEvent(toAdd);
			temp.applyChange();
			event.addSubEvents(temp);
		}
				
	}


}
