package view.grammar.parsing.cyk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import debug.JFLAPDebug;

import model.algorithms.testinput.parse.Parser;
import model.algorithms.testinput.parse.cyk.CYKParser;
import model.change.events.AdvancedChangeEvent;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import util.view.tables.HighlightTable;
import util.view.tables.SelectingEditor;
import view.grammar.parsing.RunningView;

/**
 * Running View for the CYK Parsing algorithm. Interactive table used to represent the
 * CYK algorithm, which automatically moves to the next diagonal 
 * when the current diagonal is complete.
 * 
 * @author Ian McMahon
 *
 */
@SuppressWarnings("serial")
public class CYKParseTablePanel extends RunningView {

	private HighlightTable myTable;
	private DefaultTableCellRenderer myRenderer;
	private SelectingEditor myEditor;
	private CYKParser myParser;

	public CYKParseTablePanel(Parser parser) {
		super("CYK Parse Table", parser);
		myParser = (CYKParser) parser;
		setLayout(new BorderLayout());

		CYKParseModel myModel = new CYKParseModel(myParser);
		myTable = new HighlightTable(myModel);
		JScrollPane panel = new JScrollPane(myTable);
		
		myRenderer = new EmptySetCellRenderer();
		myEditor = new SetCellEditor();
		
		add(panel, BorderLayout.CENTER);
	}

	/**
	 * Private table model that listens to the CYK parser, allows for user input,
	 * and changes accordingly.
	 */
	private class CYKParseModel extends AbstractTableModel implements
			ChangeListener {

		public CYKParseModel(CYKParser parser) {
			parser.addListener(this);
		}

		@Override
		public int getColumnCount() {
			SymbolString input = myParser.getInput();
			return input == null ? 0 : input.size();
		}

		@Override
		public int getRowCount() {
			return getColumnCount();
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return myParser.isCellEditable(rowIndex, columnIndex);
		}

		@Override
		public Set<Symbol> getValueAt(int rowIndex, int columnIndex) {
			return myParser.getValueAt(rowIndex, columnIndex);
		}

		/**
		 * Only called on user input, creates a set of Symbols from the String that 
		 * the user enters into the specified cell, sets the cell at that value,
		 * and highlights the cell if incorrect (and clears highlighting if correct)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			Set<Symbol>	attemptSet = new HashSet<Symbol>(Symbolizers.symbolize(
						(String) aValue, myParser.getGrammar()));
			
			if (!myParser.insertSet(rowIndex, columnIndex, attemptSet)){
				myTable.highlight(rowIndex, columnIndex);
				myTable.clearSelection();
			}
			else
				myTable.dehighlight(rowIndex, columnIndex);
		}

		/**
		 * Stores the input as a Symbol[] to be accessed as column header names
		 */
		private Symbol[] getColumnNames() {
			SymbolString input = myParser.getInput();
			if (input == null)
				return new Symbol[0];
			return input.toArray(new Symbol[0]);
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			if (e instanceof AdvancedChangeEvent
					&& ((AdvancedChangeEvent) e).comesFrom(myParser)) {
				fireTableDataChanged();
				myTable.createDefaultColumnsFromModel();
				
				for (int i = 0; i < getColumnCount(); i++) {
					TableColumn col = myTable.getColumnModel().getColumn(i);

					col.setCellRenderer(myRenderer);
					col.setCellEditor(myEditor);					
					col.setHeaderValue(getColumnNames()[i]);
					
					for(int j = 0; j < getColumnCount(); j++){
						if(!isCellEditable(i, j)) myTable.dehighlight(i, j);
					}
				}
			}
		}
		
	}

	/**
	 * Autofills the selected cell, if it is editable. 
	 */
	public void doSelected(){
		int row = myTable.getSelectedRow();
		int col = myTable.getSelectedColumn();
		myParser.autofillCell(row, col);
		myTable.dehighlight(row, col);
		
	}
	
	
	/**
	 * The modified table cell renderer. Removes square brackets when selected,
	 * and renders empty sets as the Empty Set Symbol.
	 */
	private class EmptySetCellRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JLabel l = (JLabel) super.getTableCellRendererComponent(table,
					value, isSelected, hasFocus, row, column);
			if (value == null){
				if(table.isCellEditable(row, column)) l.setText("[]");
				return l;
			}
			if (row == -1){
				return l;
			}
			if (hasFocus && table.isCellEditable(row, column)) {
				l.setText(replaceSetCharacters(l.getText()));
				return l;
			}
			if (!value.equals(new HashSet<Symbol>()))
				return l;
			l.setText(JFLAPConstants.EMPTY_SET_SYMBOL.getString());
			return l;
		}
	}

	
//	private void highlightHeader(int start, int end){
//		for(int i=start; i<=end; i++){
//			JLabel label = (JLabel) myRenderer.getTableCellRendererComponent(myTable, "", false, false, -1, i);
//			label.setForeground(Color.RED);
//		}
//	}
//	
//	private void dehighlightHeader(){
//		for(int i=0; i<myTable.getColumnCount(); i++){
//			JLabel label = (JLabel) myRenderer.getTableCellRendererComponent(myTable, "", false, false, -1, i);
//			label.setForeground(Color.black);
//		}
//	}
	
	/**
	 * Helper method to aid in the rendering/editing of sets
	 * so that square brackets are not counted as input.
	 */
	private String replaceSetCharacters(String labelText){
		String replacement = labelText.replaceAll("\\[", "");
		replacement = replacement.replaceAll("\\]", "");
		return replacement;
	}
	
	
	@Override
	public void setMagnification(double mag) {
		super.setMagnification(mag);
		float size = (float) (mag * JFLAPPreferences.getDefaultTextSize());
		myTable.setFont(this.getFont().deriveFont(size));
		myTable.getTableHeader().setFont(this.getFont().deriveFont(size));
		myTable.setRowHeight((int) (size + 10));
	}
}
