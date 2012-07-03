package view.grammar.parsing;

import java.awt.Color;
import java.awt.Component;
import java.util.HashSet;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import debug.JFLAPDebug;

import model.grammar.Grammar;
import model.grammar.parsing.cyk.CYKParser;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import view.grammar.EmptySetCellEditor;
import view.grammar.HighlightTable;
import view.grammar.Magnifiable;


public class CYKParseTable extends HighlightTable implements Magnifiable{
	
	private SymbolString myInput;
	private CYKParseModel model;

	public CYKParseTable(SymbolString input, Grammar gram){
		super(new CYKParseModel(input, new CYKParser(gram)));
		myInput = input;
		model = (CYKParseModel) getModel();
		
		for(int i=0; i<myInput.size();i++){
			TableColumn col = getColumnModel().getColumn(i);
			
			col.setCellRenderer(RENDERER);
			col.setCellEditor(new EmptySetCellEditor());
			col.setHeaderValue(model.getColumnNames()[i]);
		}
		setCellSelectionEnabled(true);
	}


	/** The built in highlight renderer generator. */
	private static final HighlightTable.TableHighlighterRendererGenerator THRG = new TableHighlighterRendererGenerator() {
		public TableCellRenderer getRenderer(int row, int column) {
			if (renderer == null) {
				renderer = new DefaultTableCellRenderer();
				renderer.setBackground(new Color(255, 150, 150));
			}
			return renderer;
		}

		private DefaultTableCellRenderer renderer = null;
	};

	/** Modified to use the set renderer highlighter. */
	public void highlight(int row, int column) {
		highlight(row, column, THRG);
	}

	/** The sets cell renderer. */
	private final TableCellRenderer RENDERER = new EmptySetCellRenderer();

	
	/**
	 * The modified table cell renderer.
	 */
	private class EmptySetCellRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JLabel l = (JLabel) super.getTableCellRendererComponent(table,
					value, isSelected, hasFocus, row, column);
			if(value==null)
				return l;
			if (hasFocus && table.isCellEditable(row, column)){
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
	public void setMagnification(double mag) {
		float size = (float) (mag*JFLAPPreferences.getDefaultTextSize());
        this.setFont(this.getFont().deriveFont(size));
        this.setRowHeight((int) (size+10));
	}
}
