package model.formaldef.rules;

import java.awt.Panel;
import java.awt.Dialog.ModalityType;
import java.awt.Window;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.AlphabetActionType;
import model.formaldef.components.alphabets.symbols.Symbol;

import errors.BooleanWrapper;




public class JFLAPRulebook {

	public static final IRule[] RULES = new IRule[]{new BaseRule(), 
		new GroupingRule(), 
		new TerminalsRule(), 
		new VariablesRule()};
	
	
	
	public static BooleanWrapper checkRules(AlphabetActionType action,
			FormalDefinition formalDefinition, Alphabet a, Symbol ... symbols) {
		switch (action){
		case ADD: return checkAdd(formalDefinition, a, symbols[0]);
		case MODIFY: return checkModify(formalDefinition, a, symbols[0], symbols[1]);
		case REMOVE:return checkRemove(formalDefinition, a, symbols[0]);
		}
		
		return new BooleanWrapper(true);
	}

	private static BooleanWrapper checkRemove(
			FormalDefinition formalDefinition, Alphabet a, Symbol symbol) {
		BooleanWrapper bw = new BooleanWrapper(true);
		for (IRule rule : getRulesApplicableTo(formalDefinition, a)){
			bw = rule.canRemove(formalDefinition, a, symbol);
			if (bw.isFalse()) break;
		}
		return bw;
	}

	private static BooleanWrapper checkModify(
			FormalDefinition formalDefinition, Alphabet a, Symbol s1,
			Symbol s2) {
		BooleanWrapper bw = new BooleanWrapper(true);
		for (IRule rule : getRulesApplicableTo(formalDefinition, a)){
			bw = rule.canModify(formalDefinition, a, s1, s2);
			if (bw.isFalse()) break;
		}
		
		
		return bw;
	}

	private static BooleanWrapper checkAdd(FormalDefinition formalDefinition, Alphabet a,
			Symbol symbol) {
		BooleanWrapper bw = new BooleanWrapper(true);
		for (IRule rule : getRulesApplicableTo(formalDefinition, a)){
			bw = rule.canAdd(formalDefinition, a, symbol);
			if (bw.isFalse()) break;
		}
		

		return bw;
		
	}

	private static Iterable<IRule> getRulesApplicableTo(
			FormalDefinition parent, Alphabet a) {
		ArrayList<IRule> rules = new ArrayList<IRule>();
		for (IRule rule: RULES){
			if (rule.shouldBeApplied(parent, a))
				rules.add(rule);
		}
		return rules;
	}
	
	public static void showDialog() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JDialog dialogue = new JDialog((Window) null, "Alphabet Rules");
		for (IRule rule: RULES){
			panel.add(new JLabel(rule.toString()));
		}
		dialogue.setModalityType(ModalityType.APPLICATION_MODAL);
		dialogue.add(panel);
		dialogue.pack();
		dialogue.setVisible(true);
	}

	
}
