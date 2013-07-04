package view.automata.views;

import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.mealy.MealyMachine;

public class MealyView extends AutomataView<MealyMachine, FSATransition>{

	public MealyView(MealyMachine model) {
		super(model);
	}

}
