package view.automata.transitiontable;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import universe.preferences.JFLAPPreferences;
import view.automata.AutomatonEditorPanel;

/** TransitionTable specific to FiniteStateAcceptors.
 * 
 * @author Ian McMahon
 */
public class FSATransitionTable extends TransitionTable<FiniteStateAcceptor, FSATransition>{

	public FSATransitionTable(FSATransition trans, FiniteStateAcceptor automaton, 
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel) {
		super(1, 1, trans, automaton, panel);
	}

	@Override
	public TableModel createModel() {
		return new AbstractTableModel() {
			private String s = getTransition().getLabelText();
			
			public Object getValueAt(int row, int column) {
				return s;
			}

			public void setValueAt(Object o, int r, int c) {
				s = (String) o;
			}

			public boolean isCellEditable(int r, int c) {
				return true;
			}

			public int getRowCount() {
				return 1;
			}

			public int getColumnCount() {
				return 1;
			}

			public String getColumnName(int c) {
				return "Label";
			}
		};
	}

	@Override
	public FSATransition modifyTransition() {
		String s = (String) getModel().getValueAt(0, 0);
		if(s == null || s.equals(JFLAPPreferences.getEmptyString()))
			s = "";
		
		FSATransition trans = getTransition();
		State from = trans.getFromState(), to = trans.getToState();
		SymbolString symbols = Symbolizers.symbolize(s, getAutomaton());
		
		return new FSATransition(from, to, symbols);
	}

}
