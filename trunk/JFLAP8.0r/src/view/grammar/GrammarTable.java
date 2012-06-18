/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





package view.grammar;


import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.*;

import oldnewstuff.view.EditingPanel;
import view.formaldef.componentpanel.DefinitionComponentPanel;

import model.grammar.Grammar;
import model.grammar.ProductionSet;
import model.undo.UndoKeeper;


/**
 * The <CODE>GrammarTable</CODE> is a simple extension to the <CODE>JTable</CODE>
 * that standardizes how grammar tables look.
 * 
 * @author Thomas Finley
 */

public class GrammarTable extends DefinitionComponentPanel<ProductionSet> {

	private ProductionTable myTable;



	public GrammarTable(ProductionSet model, UndoKeeper keeper, boolean editing) {
		super(model, keeper, editing);
		this.setLayout(new BorderLayout());
		myTable = new ProductionTable(model);
		JScrollPane scroller = new JScrollPane();
		add(new TableTextSizeSlider(myTable), BorderLayout.NORTH);
		scroller.setViewportView(myTable);
		add(scroller, BorderLayout.CENTER);
	}




	@Override
	public String getName() {
		return "Grammar Table";
	}




	private class ProductionTable extends HighlightTable{
		
		/**
		 * Instantiates a <CODE>GrammarTable</CODE> with a given table model.
		 * @param model 
		 * 
		 * @param model
		 *            the table model for the new grammar table
		 */
		public ProductionTable(ProductionSet model) {
			this(new ProductionTableModel(model));
	
		}
	
		
		
		public ProductionTable(ProductionTableModel model) {
			super(model);
			initView();
		}
	
	
	
		public Grammar getGrammar(){
			return this.getGrammarModel().getGrammar();
		}
		
		public void setGrammar(Grammar g){
			this.getGrammarModel().setGrammar(g);
		}
		
		/**
		 * Handles the highlighting of a particular row.
		 * 
		 * @param row
		 *            the row to highlight
		 */
		public void highlight(int row) {
			highlight(row, 0);
			highlight(row, 2);
		}
	
		/**
		 * This constructor helper function customizes the view of the table.
		 */
		private void initView() {
			setTableHeader(new JTableHeader(getColumnModel()));
			getTableHeader().setReorderingAllowed(false);
			getTableHeader().setResizingAllowed(true);
			
			//set up LHS column
			TableColumn lhs = getColumnModel().getColumn(0);
			lhs.setPreferredWidth(70);
			lhs.setMaxWidth(200);
			lhs.setCellEditor(new AlphabetLinkedSelectingEditor());
			
			//set up arrow column
			TableColumn arrows = getColumnModel().getColumn(1);
			arrows.setMaxWidth(30);
			arrows.setMinWidth(30);
			arrows.setPreferredWidth(30);
			
			//set up RHS column
			TableColumn rhs = getColumnModel().getColumn(2);
			rhs.setCellEditor(new AlphabetLinkedSelectingEditor());
	
			
			setShowGrid(true);
			setGridColor(Color.lightGray);
			this.rowHeight = 30;
			this.setFont(DEFAULT_FONT);
			getColumnModel().getColumn(2).setCellRenderer(RENDERER);
		}
		
		public void setFontSize(float size) {
			this.setFont(this.getFont().deriveFont(size));
		}
	
		/**
		 * Returns the model for this grammar table.
		 * 
		 * @return the grammar table model for this table
		 */
		public ProductionTableModel getGrammarModel() {
			return (ProductionTableModel) super.getModel();
		}
	
		/** Modified to use the set renderer highlighter. */
		public void highlight(int row, int column) {
			highlight(row, column, THRG);
		}
	
	
		/** The built in highlight renderer generator, modified. */
		private TableHighlighterRendererGenerator THRG = new TableHighlighterRendererGenerator() {
			public TableCellRenderer getRenderer(int row, int column) {
				if (renderer == null) {
					renderer = new DefaultTableCellRenderer();
					renderer.setBackground(new Color(255, 150, 150));
				}
				return renderer;
			}
	
			private DefaultTableCellRenderer renderer = null;
		};
	
		/** The lambda cell renderer. */
		private TableCellRenderer RENDERER = new DefaultTableCellRenderer();
		
		
		@Override
		public TableCellRenderer getCellRenderer(int row, int column) {
			return super.getCellRenderer(row, column);
		}
	
	
	
		private class AlphabetLinkedSelectingEditor extends SelectingEditor{
	
			public AlphabetLinkedSelectingEditor() {
				super(new AlphabetLinkedTextField());
			}
			
		}
		
	
	}

	
}
