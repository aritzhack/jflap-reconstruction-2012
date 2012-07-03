package view.grammar.parsing;

import java.util.HashSet;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import model.grammar.parsing.ParserException;
import model.grammar.parsing.cyk.CYKParser;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import debug.JFLAPDebug;

public class CYKParseModel extends AbstractTableModel{
	private Symbol[] myTarget;
	private Set<Symbol>[][] myTable;
	private CYKParser myParser;
	private int editableCutoff;
	
	public CYKParseModel(SymbolString input, CYKParser parser){
		myTarget = input.toArray(new Symbol[0]);
		int length = myTarget.length;
		myTable = new Set[length][length];
		for (int i = 0; i < length; i++) {
			for (int j = i; j < length; j++) {
				myTable[i][j] = null;
			}
		}
		myParser = parser;
		myParser.quickParse(input);
	}

	public CYKParser getParser(){
		return myParser;
	}
	
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
	
	public void setValueAt(String value, int row, int col){
		String val = (String) value;
		SymbolString toSS = Symbolizers.symbolize(val, myParser.getGrammar());
		myTable[row][col] = new HashSet<Symbol>(toSS);
	}
	
	
	@Override
	public Set<Symbol> getValueAt(int rowIndex, int columnIndex) {
		return myTable[rowIndex][columnIndex];
	}
	
	public String[] getColumnNames(){
		String[] strings = new String[myTarget.length];
		for(int i=0; i<myTarget.length; i++){
			strings[i] = myTarget[i].toString();
		}
		return strings;
	}

	public boolean isCellEditable(int row, int column) {
		return column-row == editableCutoff;
	}
	
	public void setEditableCutoff(int cutoff){
		this.editableCutoff = cutoff;
		for(int i=0; i<myTable.length;i++){
			for(int j=0; j<myTable.length;j++){
				if(j-i == cutoff){
					setValueAt(new HashSet<Symbol>(), i, j);
				}
			}
		}
	}
}
