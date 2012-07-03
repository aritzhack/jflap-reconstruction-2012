package view.grammar.parsing;

import java.awt.event.ActionEvent;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import model.grammar.parsing.cyk.CYKParser;
import model.symbols.Symbol;

public class CYKParseController {

	
	private CYKParseTable table;
	private CYKParsePane pane;
	private JLabel directions;
	private CYKParser parser;
	private int maxLength, currentLength;
	
	public CYKParseController(CYKParseTable table, CYKParsePane pane, JLabel directions){
		this.table = table;
		this.pane = pane;
		this.directions = directions;
		
		CYKParseModel model = (CYKParseModel) table.getModel();
		
		this.parser = model.getParser();
		this.maxLength = model.getTarget().length;
		this.currentLength = 0;
		model.setEditableCutoff(0);
	}
	
	/**
	 * This method will complete the step for whatever cells are highlighted, as
	 * is appropriate for the current step.
	 */
	public void completeSelected() {
		int col = table.getSelectedColumn();
		for(int i=0; i<=col; i++){
			if(!table.isCellSelected(i, col)) continue;
			
			Set<Symbol> parserSet = parser.getNodeAtIndex(i, col);
			table.setValueAt(parserSet, i, col);
			try{
				table.getCellEditor().stopCellEditing();
			}catch(NullPointerException e) {
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
		for(int row=0; row+currentLength < maxLength; row++){
			int col = row+currentLength;
			Set<Symbol> parserSet = parser.getNodeAtIndex(row, col);
			table.setValueAt(parserSet, row, col);
		}try{
			table.getCellEditor().stopCellEditing();
		}catch(NullPointerException e) {
		}
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
		
		if(currentLength>=maxLength){
			stepAction.setEnabled(false);
			selectedAction.setEnabled(false);
			allAction.setEnabled(false);
			nextAction.setEnabled(false);
			derivationAction.setEnabled(true);
			directions.setText("Table is complete, press 'See Derivation' to continue");
		}
		((CYKParseModel) table.getModel()).setEditableCutoff(currentLength);
		return true;
	}
	
	private boolean done(){
		if(currentLength > maxLength){
			JOptionPane.showMessageDialog(table,
					"The parse table is complete.", "Finished",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		int highlighted = 0;
		for(int row=0; row+currentLength < maxLength; row++){
			int col = row+currentLength;
			Set<Symbol> parserSet = parser.getNodeAtIndex(row, col);
			if(!parserSet.equals(table.getValueAt(row, col))){
				table.highlight(row, col);
				highlighted++;
			}
		}
		if (highlighted == 0)
			return true;
		table.clearSelection();
		try {
			table.getCellEditor().stopCellEditing();
		} catch (NullPointerException e) {
		}
		JOptionPane.showMessageDialog(pane,
				"Highlighted sets are incorrect.", "Bad Sets",
				JOptionPane.ERROR_MESSAGE);
		table.dehighlight();
		return false;
	}
	
	
	public void completeAll(){
		while(currentLength<maxLength){
			completeStep();
		}
	}

	
	public AbstractAction stepAction = new AbstractAction("Do Step") {
		public void actionPerformed(ActionEvent e) {
			completeStep();
		}
	};
	
	public AbstractAction selectedAction = new AbstractAction("Do Selected") {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			completeSelected();
		}
	};
	
	public AbstractAction allAction = new AbstractAction("Do All") {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			completeAll();
		}
	};
	
	public AbstractAction nextAction = new AbstractAction("Next") {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			nextStep();
		}
	};
	
	public AbstractAction derivationAction = new AbstractAction("See Derivation") {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			pane.switchToDerivationView();
		}
	};
}
