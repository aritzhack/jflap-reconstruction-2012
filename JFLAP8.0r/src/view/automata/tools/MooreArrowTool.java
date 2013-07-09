package view.automata.tools;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.moore.MooreMachine;
import view.automata.MooreEditorPanel;
import view.automata.Note;
import view.automata.undoing.MooreOutputRenameEvent;
import view.automata.undoing.NoteRenameEvent;

public class MooreArrowTool extends ArrowTool<MooreMachine, FSATransition> {

	public MooreArrowTool(MooreEditorPanel panel, MooreMachine def) {
		super(panel, def);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point p = e.getPoint();
		MooreEditorPanel panel = (MooreEditorPanel) getPanel();
		Object o = panel.objectAtPoint(p);
		if (o instanceof State && SwingUtilities.isLeftMouseButton(e)) {
			State s = (State) o;
			Note output = panel.getOutputNote(s);
			String newOutput = panel.editOutputFunction(s);
			
			if(!newOutput.equals(output.getText())){
				String old = output.getText();
				output.setText(newOutput);
				panel.moveOutputFunction(s);
				getKeeper().registerChange(new MooreOutputRenameEvent(panel, s, output, old));
			}
		} else
			super.mouseClicked(e);
	}

}
