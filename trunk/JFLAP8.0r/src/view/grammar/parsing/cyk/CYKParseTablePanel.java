package view.grammar.parsing.cyk;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashSet;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import model.algorithms.testinput.InputUsingAlgorithm;
import model.algorithms.testinput.parse.cyk.CYKParser;
import model.change.events.AdvancedChangeEvent;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import util.view.tables.HighlightTable;
import view.grammar.parsing.RunningView;

public class CYKParseTablePanel extends RunningView{

	private HighlightTable myTable;
	private final TableCellRenderer RENDERER = new EmptySetCellRenderer();

	public CYKParseTablePanel(CYKParser parser){
		this(new CYKParseModel(parser));
	}
	
	public CYKParseTablePanel(CYKParseModel model) {
		super("CYK Parse Table", model.getParser());
		this.setLayout(new BorderLayout());
		myTable = new HighlightTable(model);
		
		JScrollPane scroll = new JScrollPane(myTable);
		add(scroll, BorderLayout.CENTER);
		
	}
	
	/**
	 * Resets the target string to be parsed to input. Reinitializes the table
	 * with a new, corresponding table model.
	 * 
	 * @param input
	 *            the new target to be parsed.
	 */
	public void setInput(SymbolString input) {
		CYKParser parser = getModel().getParser();
		if(input == null){
			myTable.setModel(new CYKParseModel(parser));
			return;
		}
		
		CYKParseModel model = new CYKParseModel(parser, input);
		myTable.setModel(model);
		
		model.setEditableRow(0);

		for (int i = 0; i < input.size(); i++) {
			TableColumn col = myTable.getColumnModel().getColumn(i);

			col.setCellRenderer(RENDERER);
			col.setCellEditor(new EmptySetCellEditor());
			col.setHeaderValue(model.getColumnNames()[i]);
		}
		myTable.setCellSelectionEnabled(true);
	}
	
	public CYKParseModel getModel(){
		return (CYKParseModel) myTable.getModel();
	}
	
	@Override
	public void setMagnification(double mag) {
		super.setMagnification(mag);
		float size = (float) (mag*JFLAPPreferences.getDefaultTextSize());
		myTable.setFont(this.getFont().deriveFont(size));
		myTable.setRowHeight((int) (size+10));
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
			if (value == null)
				return l;
			if (hasFocus && table.isCellEditable(row, column)) {
				l.setText(l.getText().replaceAll("\\[", ""));
				l.setText(l.getText().replaceAll("\\]", ""));
				return l;
			}
			if (!value.equals(new HashSet<Symbol>()))
				return l;
			l.setText(JFLAPConstants.EMPTY_SET_SYMBOL.getString());
			return l;
		}
	}


	@Override
	public void updateStatus(AdvancedChangeEvent e) {
		if(e.comesFrom(getModel().getParser())){
			if(e.getType() == InputUsingAlgorithm.INPUT_SET)
				setInput((SymbolString) e.getArg(0));
		}
	}
}
