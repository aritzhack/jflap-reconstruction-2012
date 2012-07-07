package view.numsets;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.undo.UndoKeeper;
import view.grammar.Magnifiable;

public class SetDefinitionPanel extends JPanel implements Magnifiable {

	public SetDefinitionPanel (UndoKeeper keeper) {

		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		JTextArea elements = new JTextArea();
		elements.setName("Name: ");

		add(elements);
	}



	@Override
	public void setMagnification(double mag) {
		for(Component c: this.getComponents()) {
			if (c instanceof Magnifiable)
				((Magnifiable) c).setMagnification(mag);
		}
	}
}
