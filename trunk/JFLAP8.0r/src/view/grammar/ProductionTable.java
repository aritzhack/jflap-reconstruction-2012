package view.grammar;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import preferences.JFLAPPreferences;

import debug.JFLAPDebug;

import model.formaldef.components.SetComponent;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.undo.UndoKeeper;
import util.JFLAPConstants;

public class ProductionTable extends HighlightTable 
						implements JFLAPConstants, Magnifiable, ChangeListener{

	private boolean amEditable;

	private UndoKeeper myKeeper;


	/**
	 * Instantiates a <CODE>GrammarTable</CODE> with a given table model.
	 * @param model 
	 * 
	 * @param model
	 *            the table model for the new grammar table
	 * @param keeper 
	 * @param editable 
	 */
	public ProductionTable(Grammar g, UndoKeeper keeper, boolean editable) {
		super(new ProductionTableModel(g, keeper));
		amEditable = editable;
		g.getProductionSet().addListener(this);
		initView();
		myKeeper = keeper;
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if ( e.getModifiers() == JFLAPConstants.MAIN_MENU_MASK &&
						e.getKeyCode() == KeyEvent.VK_D &&
						amEditable){
					int[] rows = getSelectedRows();
					myKeeper.beginCombine();
					boolean shouldAdd = false;
					for (int i : rows){
						shouldAdd = ((ProductionTableModel) getModel()).remove(i);
						if (!shouldAdd) break;
					}
					myKeeper.endCombine(shouldAdd);

					e.consume();
					clearSelection();
					updateUI();
				}
			}
		});
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
		lhs.setCellEditor(createEditor());

		//set up arrow column
		TableColumn arrows = getColumnModel().getColumn(1);
		arrows.setMaxWidth(30);
		arrows.setMinWidth(30);
		arrows.setPreferredWidth(30);

		//set up RHS column
		TableColumn rhs = getColumnModel().getColumn(2);
		rhs.setCellEditor(createEditor());


		setShowGrid(true);
		setGridColor(Color.lightGray);
		this.rowHeight = 30;
		this.setFont(DEFAULT_FONT);
		getColumnModel().getColumn(2).setCellRenderer(RENDERER);
	}

	private TableCellEditor createEditor() {
		//		return new SelectingEditor(new AlphabetLinkedTextField());
		return new SelectingEditor(new JTextField());
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




	@Override
	public void setMagnification(double mag) {
		float size = (float) (mag*JFLAPPreferences.getDefaultTextSize());
        this.setFont(this.getFont().deriveFont(size));
        this.setRowHeight((int) (size+10));
	}




	@Override
	public void stateChanged(ChangeEvent e) {
		this.updateUI();
	}

}
