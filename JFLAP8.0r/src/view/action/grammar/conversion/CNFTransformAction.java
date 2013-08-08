package view.action.grammar.conversion;

import java.awt.event.ActionEvent;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import model.algorithms.transform.grammar.CNFConverter;
import model.algorithms.transform.grammar.LambdaProductionRemover;
import model.grammar.Grammar;
import model.grammar.Production;
import universe.JFLAPUniverse;
import view.environment.JFLAPEnvironment;
import view.grammar.GrammarView;
import view.grammar.transform.LambdaPanel;

public class CNFTransformAction extends AbstractAction {

	private GrammarView myView;

	public CNFTransformAction(GrammarView v) {
		super("Transform Grammar");
		myView = v;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Grammar g = myView.getDefinition().copy();
		hypothesizeLambda(g);
	}

	public static void hypothesizeLambda(Grammar g) {
		LambdaProductionRemover remover = new LambdaProductionRemover(g);
		Set<Production> lambdaDerivers = remover.getAllIdentifyTargets();
		for (Production p : lambdaDerivers) {
			if (p.isStartProduction(g.getStartVariable())) {
				JOptionPane
						.showMessageDialog(
								JFLAPUniverse.getActiveEnvironment(),
								"WARNING : The start variable derives lambda.\n"
										+ "New Grammar will not produce lambda String.",
								"Start Derives Lambda",
								JOptionPane.ERROR_MESSAGE);
			}
		}
		if (lambdaDerivers.size() > 0) {
			LambdaPanel lp = new LambdaPanel(g, remover);
			JFLAPUniverse.getActiveEnvironment().addSelectedComponent(lp);
		}
		hypothesizeUnit(g);
	}

	public static void hypothesizeUnit(Grammar g) {
		// TODO Auto-generated method stub
		
	}

}
