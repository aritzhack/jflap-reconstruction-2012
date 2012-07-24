package view.sets.edit;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.LineBorder;

import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import util.view.magnify.MagnifiablePanel;
import view.action.sets.FinishConstructionAction;
import view.sets.SetsDropdownMenu;
import view.sets.state.CreateState;

public class OptionsMenu extends MagnifiablePanel {

	private SetsEditingPanel myContainer;
	private UndoKeeper myKeeper;

	public OptionsMenu (SetsEditingPanel parent, UndoKeeper keeper) {
		myContainer = parent;
		myKeeper = keeper;
		

		initMenu();
		setBorder(new LineBorder(Color.BLUE));
	}

	private void initMenu () {
		MagnifiableButton use = new MagnifiableButton("Use an existing set", JFLAPPreferences.getDefaultTextSize());
		use.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("existing");
				myContainer.expandView(new SetsDropdownMenu());
			}

		});

		MagnifiableButton input = new MagnifiableButton("Input elements for new set", JFLAPPreferences.getDefaultTextSize());
		input.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("input");
				
				SetDefinitionPanel source = new SetDefinitionPanel(myKeeper);
				FinishConstructionAction fin = new FinishConstructionAction(myKeeper, new CreateState(source));
				myContainer.expandView(source);
				myContainer.expandView(new MagnifiableButton(fin, JFLAPPreferences.getDefaultTextSize()));
			}

		});

		add(use);
		add(input);

	}



}
