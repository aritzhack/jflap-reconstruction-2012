package view.grammar.parsing.cyk;

import java.util.HashSet;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import debug.JFLAPDebug;

import model.algorithms.testinput.parse.ParserException;
import model.algorithms.testinput.parse.cyk.CYKParser;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;

/**
 * Table Model for CYK Parser GUI. 
 * @author Ian McMahon
 *
 */
public class CYKParseModel extends AbstractTableModel{
	private Symbol[] myTarget;
	private Set<Symbol>[][] myTable;
	private CYKParser myParser;
	private int editableRow;
	
	public CYKParseModel(CYKParser parser){
		myTarget = new Symbol[0];
		myTable = new Set[0][0];
		myParser = parser;
	}
	
	public CYKParseModel(CYKParser parser, SymbolString input){
		myParser = parser;
		initTargetAndTable(input);
		myParser.quickParse(input);
	}
	
	/**
	 * Initializes the table based off of the length of the input
	 * and the Symbols the it consists of. 
	 * @param input
	 * 		SymbolString representation of parsing target string
	 */
	private void initTargetAndTable(SymbolString input){
		myTarget = input.toArray(new Symbol[0]);
		int length = myTarget.length;
		myTable = new Set[length][length];
	}

	/**
	 * Returns internal parser.
	 */
	public CYKParser getParser(){
		return myParser;
	}
	
	/**
	 * Returns Symbol[] representation of the target string.
	 */
	public Symbol[] getTarget(){
		return myTarget;
	}
	
	@Override
	public int getColumnCount() {
		return myTarget.length;
	}

	@Override
	public int getRowCount() {
		return myTarget.length;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		if(value instanceof String){
			setValueAt((String) value, row, col);
			return;
		}
		if(!(value instanceof Set)) throw new ParserException("The entered value is not a set!");
		myTable[row][col] = (Set<Symbol>) value;
	}
	
	/**
	 * Helper method to initialize table cell with a Set the first time
	 * a value is entered (as it will be a String instead of a Set)
	 */
	private void setValueAt(String value, int row, int col){
		String val = (String) value;
		SymbolString toSS = Symbolizers.symbolize(val, myParser.getGrammar());
		myTable[row][col] = new HashSet<Symbol>(toSS);
	}
	
	
	@Override
	public Set<Symbol> getValueAt(int rowIndex, int columnIndex) {
		JFLAPDebug.print("Get");
		return myTable[rowIndex][columnIndex];
	}
	
	/**
	 * Returns the names for setting column headers, each is one Symbol
	 * in target string
	 */
	public String[] getColumnNames(){
		String[] strings = new String[myTarget.length];
		for(int i=0; i<myTarget.length; i++){
			strings[i] = myTarget[i].toString();
		}
		return strings;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return row == editableRow && myTable[row][column] != null;
	}
	
	/**
	 * Based on what "step" in the parsing it is, activates only the
	 * corresponding table cells to be edited.
	 */
	public void setEditableRow(int row){
		this.editableRow = row;
		if(row >= myTarget.length) return;
		for(int i=row; i<myTable.length;i++){
			setValueAt(new HashSet<Symbol>(), row, i);
		}
	}
}
