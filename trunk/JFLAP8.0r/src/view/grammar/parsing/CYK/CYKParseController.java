package view.grammar.parsing.CYK;

import java.awt.event.ActionEvent;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import model.grammar.Grammar;
import model.grammar.parsing.cyk.CYKParser;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;

/**
 * Controller for CYK Parsing GUI. Connects what the user interacts with/sees
 * with the actual model code for proper visualization.
 * 
 * @author Ian McMahon
 * 
 */
public class CYKParseController {

	private CYKParseTable table;
	private CYKParsePane pane;
	private JLabel directions;
	private CYKParser parser;
	private int maxLength, currentLength;

	public CYKParseController(CYKParseTable table, CYKParsePane pane,
			JLabel directions) {
		this.table = table;
		this.pane = pane;
		this.directions = directions;

		CYKParseModel model = (CYKParseModel) table.getModel();
		this.parser = model.getParser();

	}

	/**
	 * This method will complete the step for whatever cells are highlighted, as
	 * is appropriate for the current step.
	 */
	public void completeSelected() {
		int col = table.getSelectedColumn();
		for (int i = 0; i <= col; i++) {
			if (table.isCellSelected(i, col) && table.isCellEditable(i, col)) {

				Set<Symbol> parserSet = parser.getNodeAtIndex(col - i, col);
				table.setValueAt(parserSet, i, col);
				stopEditing();
			}
			table.repaint();
		}
	}

	/**
	 * This method will complete the current step. When done with whatever it
	 * must do it will call {@link #nextStep} to move to the next step unless it
	 * is on the last step, in which case a small error message is displayed.
	 */
	public void completeStep() {
		int row = currentLength;
		for (int col = row; col < maxLength; col++) {
			Set<Symbol> parserSet = parser.getNodeAtIndex(col - row, col);
			table.setValueAt(parserSet, row, col);
		}
		stopEditing();
		table.repaint();
		nextStep();
	}

	/**
	 * Moves the controller to the next step of the building of the parse table.
	 * 
	 * @return if the controller could be advanced to the next step
	 */
	public boolean nextStep() {
		if (!done())
			return false;

		currentLength++;

		if (currentLength >= maxLength) {
			setActionsFinished();

		} else {
			directions.setText("Complete the next row of the table.");
		}
		((CYKParseModel) table.getModel()).setEditableRow(currentLength);
		table.repaint();
		return true;
	}

	/**
	 * Disables all actions besides start, and if parser accepted the input,
	 * enables the derivationAction. Called when parser has finished the final
	 * step.
	 */
	private void setActionsFinished() {
		stepAction.setEnabled(false);
		selectedAction.setEnabled(false);
		allAction.setEnabled(false);
		nextAction.setEnabled(false);

		if (parser.isAccept()) {
			derivationAction.setEnabled(true);
			directions
					.setText("Table is complete, input was accepted. Press 'See Derivation' to continue");
			return;
		}
		directions.setText("Input was rejected, try another string");
	}

	/**
	 * Returns true if current step is completely done, correct, and is not
	 * past the final step (throws an error in this case). It will highlight any
	 * cells that are active and incorrect and show a dialog notifying the user.
	 */
	private boolean done() {
		if (currentLength > maxLength) {
			JOptionPane.showMessageDialog(table,
					"The parse table is complete.", "Finished",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		int highlighted = 0;
		int row = currentLength;
		for (int col = row; col < maxLength; col++) {
			Set<Symbol> parserSet = parser.getNodeAtIndex(col - row, col);
			if (!parserSet.equals(table.getValueAt(row, col))) {
				table.highlight(row, col);
				highlighted++;
			}
		}
		if (highlighted == 0)
			return true;
		table.clearSelection();

		JOptionPane.showMessageDialog(pane, "Highlighted sets are incorrect.",
				"Bad Sets", JOptionPane.ERROR_MESSAGE);
		table.dehighlight();
		return false;
	}

	/**
	 * Steps to completion for displaying the parse table.
	 */
	public void completeAll() {
		while (currentLength < maxLength) {
			completeStep();
		}
	}

	/**
	 * Completes the current step and moves to the next one.
	 */
	public AbstractAction stepAction = new AbstractAction("Do Step") {
		public void actionPerformed(ActionEvent e) {
			completeStep();
		}
	};

	/**
	 * Completes the selected cell (if it is part of the current step)
	 */
	public AbstractAction selectedAction = new AbstractAction("Do Selected") {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			completeSelected();
		}
	};

	/**
	 * Completes all steps
	 */
	public AbstractAction allAction = new AbstractAction("Do All") {

		@Override
		public void actionPerformed(ActionEvent e) {
			completeAll();
		}
	};

	/**
	 * Moves to the next step if current step is correct and complete, else
	 * highlights what needs to be done.
	 */
	public AbstractAction nextAction = new AbstractAction("Next") {

		@Override
		public void actionPerformed(ActionEvent e) {
			nextStep();
		}
	};

	/**
	 * Switches to derivation/tree view
	 */
	public AbstractAction derivationAction = new AbstractAction(
			"See Derivation") {

		@Override
		public void actionPerformed(ActionEvent e) {
			derivationAction.setEnabled(false);
			pane.switchToDerivationView();
		}
	};

	/**
	 * Resets input for parsing, switches to parsing view.
	 */
	public AbstractAction startAction = new AbstractAction("Start") {

		@Override
		public void actionPerformed(ActionEvent e) {
			start();
		}

	};

	/**
	 * Creates input from JTextField, switches to parsing view, and resets all
	 * aspects needed to display correctly.
	 */
	private void start() {
		String s = pane.getInputText();
		Grammar g = pane.getGrammar();

		SymbolString input = Symbolizers.symbolize(s, g);

		pane.switchToParseView();
		table.setInput(input);

		maxLength = input.size();
		currentLength = 0;
		directions
				.setText("Fill in the first row with all variables that "+
						"can derive that column's terminal.");

		setActionsStarted();
	}

	/**
	 * Parsing actions enabled, derivation actions disabled
	 */
	private void setActionsStarted() {
		selectedAction.setEnabled(true);
		stepAction.setEnabled(true);
		allAction.setEnabled(true);
		nextAction.setEnabled(true);
		derivationAction.setEnabled(false);

	}

	/**
	 * Simple helper try/catch to stop cell editing
	 */
	private void stopEditing() {
		try {
			table.getCellEditor().stopCellEditing();
		} catch (NullPointerException e) {
		}
	}
}
