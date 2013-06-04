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

package view.lsystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.Terminal;
import model.lsystem.CommandAlphabet;
import model.lsystem.LSystem;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableLabel;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableScrollPane;
import util.view.magnify.MagnifiableSplitPane;
import util.view.magnify.MagnifiableTextField;
import view.formaldef.BasicFormalDefinitionView;
import view.grammar.productions.ProductionDataHelper;
import view.grammar.productions.ProductionTable;
import view.grammar.productions.ProductionTableModel;

/**
 * The <CODE>LSystemInputPane</CODE> is a pane used to input and display the
 * textual representation of an L-system.
 * 
 * @author Thomas Finley
 */

public class LSystemInputView extends BasicFormalDefinitionView<LSystem> {
	/** An empty L-system. */
	private static final LSystem SYSTEM = new LSystem();

	/** The axiom text field. */
	private MagnifiableTextField axiomField;

	/** The production table. */
	private ProductionTable myProdTable;

	/** The parameter table model. */
	private ParameterTableModel parameterModel;

	/** The parameter table view. */
	private ParameterTable parameterTable;

	/** The set of input listeners. */
	private Set<LSystemInputListener> myListeners = new HashSet<LSystemInputListener>();

	/** The event reused in firing off the notifications. */
	private LSystemInputEvent reusedEvent = new LSystemInputEvent(this);

	/** The cached L-system. Firing an L-S input event invalidates this. */
	private LSystem cachedSystem = null;

	private TableCellRenderer myRenderer = new NumberBoldingRenderer();

	/**
	 * Instantiates an empty <CODE>LSystemInputPane</CODE>.
	 */
	public LSystemInputView() {
		this(SYSTEM);
	}

	/**
	 * Instantiates an <CODE>LSystemInputPane</CODE> for a given
	 * <CODE>LSystem</CODE>.
	 * 
	 * @param lsystem
	 *            the lsystem to display
	 */
	public LSystemInputView(LSystem lsystem) {
		super(lsystem, new UndoKeeper(), true);
	}

	/**
	 * Initializes the data structures and the subviews.
	 * 
	 * @param lsystem
	 *            the L-system to initialize the views on
	 */
	private void initializeStructures(LSystem lsystem) {
		// Create the axiom text field.
		axiomField = new MagnifiableTextField(
				JFLAPPreferences.getDefaultTextSize());
		String axiom = lsystem.getAxiom().toString();
		axiomField
				.setText(axiom == JFLAPPreferences.getEmptyStringSymbol() ? ""
						: axiom);

		// Create the parameter table model.
		parameterModel = new ParameterTableModel(lsystem.getParameters());
		// We may as well use this as our cached system.
		cachedSystem = lsystem;
	}

	/**
	 * Creates the listener to update the edited-ness.
	 */
	public void initializeListener() {
		axiomField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				fireLSystemInputEvent();
			}

			public void removeUpdate(DocumentEvent e) {
				fireLSystemInputEvent();
			}

			public void insertUpdate(DocumentEvent e) {
				fireLSystemInputEvent();
			}
		});
		TableModelListener tml = new TableModelListener() {
			public void tableChanged(TableModelEvent e) {
				fireLSystemInputEvent();
			}
		};
		parameterModel.addTableModelListener(tml);
		myProdTable.getModel().addTableModelListener(tml);
	}

	/**
	 * Returns the L-system this pane displays.
	 * 
	 * @return the L-system this pane displays
	 */
	public LSystem getLSystem() {
		// Make sure we're not editing anything.
		if (myProdTable.getCellEditor() != null)
			myProdTable.getCellEditor().stopCellEditing();
		if (parameterTable.getCellEditor() != null)
			parameterTable.getCellEditor().stopCellEditing();
		// Do we already have a cached copy?
		try {
			if (cachedSystem == null) {
				Grammar g = ((ProductionTableModel) myProdTable.getModel())
						.getGrammar();
				cachedSystem = new LSystem(Symbolizers.symbolize(
						axiomField.getText(), g), g,
						parameterModel.getParameters());
			}
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"L-System Error", JOptionPane.ERROR_MESSAGE);
		}
		return cachedSystem;
	}

	/**
	 * Adds an L-system input listener.
	 * 
	 * @param listener
	 *            the listener to start sending change events to
	 */
	public void addLSystemInputListener(LSystemInputListener listener) {
		myListeners.add(listener);
	}

	/**
	 * Removes an L-system input listener.
	 * 
	 * @param listener
	 *            the listener to stop sending change events to
	 */
	public void removeLSystemInputListener(LSystemInputListener listener) {
		myListeners.remove(listener);
	}

	/**
	 * Fires a notification to listeners that the L-system was changed.
	 */
	protected void fireLSystemInputEvent() {
		cachedSystem = null;
		Iterator it = myListeners.iterator();
		while (it.hasNext())
			((LSystemInputListener) (it.next())).lSystemChanged(reusedEvent);
	}

	/**
	 * This will edit the value for a particular parameter in the parameter
	 * table. If no such value exists yet it shall be created. The value field
	 * in the table shall be edited.
	 * 
	 * @param item
	 *            the key of the value we want to edit
	 */
	private void setEditing(String item) {
		int i;
		for (i = 0; i < parameterModel.getRowCount(); i++) {
			String value = (String) parameterModel.getValueAt(i, 0);
			if (value != null && value.equals(item))
				break;
		}
		if (i == parameterModel.getRowCount()) // We need to create it.
			parameterModel.setValueAt(item, --i, 0);
		int column = parameterTable.convertColumnIndexToView(1);
		parameterTable.editCellAt(i, column);
		parameterTable.requestFocus();
	}

	@Override
	public String getName() {
		return "L System";
	}

	@Override
	public JComponent createCentralPanel(LSystem model, UndoKeeper keeper,
			boolean editable) {
		initializeStructures(model);
		MagnifiablePanel central = new MagnifiablePanel(new BorderLayout());

		// Create the view for the axiom text field.
		MagnifiablePanel axiomView = new MagnifiablePanel(new BorderLayout());
		axiomView.add(
				new MagnifiableLabel("Axiom: ", JFLAPPreferences
						.getDefaultTextSize()), BorderLayout.WEST);
		axiomView.add(axiomField, BorderLayout.CENTER);
		central.add(axiomView, BorderLayout.NORTH);
		// Create the view for the grammar pane and the rest.
		parameterTable = new ParameterTable(parameterModel);
		MagnifiableScrollPane scroller = new MagnifiableScrollPane(
				parameterTable);

		Dimension bestSize = new Dimension(400, 200);
		/*
		 * parameterTable.setPreferredSize(bestSize);
		 * productionInputPane.getTable().setPreferredSize(bestSize);
		 */
		// Create the grammar view that holds replacement productions.
		Set<SymbolString> replacements = model
				.getSymbolStringsWithReplacements();
		Grammar g = new Grammar();
		g.getTerminals().addAll(new CommandAlphabet());
		ProductionSet prods = g.getProductionSet();

		for (SymbolString s : replacements) {
			SymbolString[] r = model.getReplacements(s);
			for (int i = 0; i < r.length; i++) {
				Production p = new Production(s, r[i]);
				prods.add(p);
			}
		}

		myProdTable = new ProductionTable(g, keeper, true, new ProductionTableModel(g, keeper, new LSystemDataHelper(g, keeper))) {
			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				if (column == 0)
					return myRenderer;
				return super.getCellRenderer(row, column);
			}
		};
		myProdTable.setPreferredSize(bestSize);
		

		MagnifiableScrollPane prodScroller = new MagnifiableScrollPane(
				myProdTable);
		prodScroller.setPreferredSize(bestSize);
		scroller.setPreferredSize(bestSize);
		MagnifiableSplitPane split = new MagnifiableSplitPane(
				JSplitPane.VERTICAL_SPLIT, prodScroller, scroller);
		central.add(split, BorderLayout.CENTER);
		// Finally, show the grid.
		parameterTable.setShowGrid(true);
		parameterTable.setGridColor(Color.lightGray);

		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		/*
		 * final JComboBox box = new JComboBox ((String[])
		 * Renderer.ASSIGN_WORDS.toArray(new String[0]));
		 * box.setLightWeightPopupEnabled(false);
		 * scroller.setCorner(JScrollPane.UPPER_RIGHT_CORNER, box);
		 * box.addItemListener(new ItemListener() { public void
		 * itemStateChanged(ItemEvent e) { if (e.getStateChange() != e.SELECTED)
		 * return; String s = (String) e.getItem(); box.setSelectedIndex(-1); //
		 * No selection! setEditing(s); } });
		 */

		final JPopupMenu menu = new JPopupMenu();
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setEditing(e.getActionCommand());
			}
		};
		String[] words = (String[]) Renderer.ASSIGN_WORDS
				.toArray(new String[0]);
		for (int i = 0; i < words.length; i++) {
			menu.add(words[i]).addActionListener(listener);
		}
		JLabel c = new JLabel();
		c.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				menu.show((Component) e.getSource(), e.getPoint().x,
						e.getPoint().y);
			}
		});
		c.setText(" P");
		scroller.setCorner(JScrollPane.UPPER_RIGHT_CORNER, c);
		initializeListener();
		return central;
	}

	/**
	 * The modified table cell renderer. Replaces square brackets with {},
	 * renders empty sets as the Empty Set Symbol, and deals with highlighting
	 * of table cells (notifying the header renderer when necessary).
	 */
	private class NumberBoldingRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			JLabel l = (JLabel) super.getTableCellRendererComponent(table,
					value, isSelected, hasFocus, row, column);
			if (l != null) {
				String[] lhs = l.getText().trim().replaceAll("\\s+", " ").split(" ");
				if (lhs.length > 1 && Character.isDigit(lhs[0].charAt(0))) {
					int index = lhs[0].charAt(0) - '0' + 1;
					if (index < lhs.length) {
						StringBuilder left = new StringBuilder(), right = new StringBuilder();
						for(int i=1; i < lhs.length; i++){
							lhs[i] = lhs[i].replaceAll("&", "&amp;");
							lhs[i] = lhs[i].replaceAll("\"", "&quot;");
							lhs[i] = lhs[i].replaceAll("<", "&lt;");
							lhs[i] = lhs[i].replaceAll(">", "&gt;");
							if(i < index)
								left.append(lhs[i]+" ");
							else if(i > index)
								right.append(lhs[i]+" ");
						}
						l.setText(String.format("<html>%s<b>%s</b>%s</html>",
								left.toString(), lhs[index] + " ",
								right.toString()).trim());
					}
				}
			}
			return l;
		}
	}
	
	private class LSystemDataHelper extends ProductionDataHelper{

		public LSystemDataHelper(Grammar model, UndoKeeper keeper) {
			super(model, keeper);
		}
		
		@Override
		protected Production objectToProduction(Object[] input) {
			if (isEmptyString((String) input[0]))
				input[0] = "";
			if (isEmptyString((String) input[2]))
				input[2] = "";
			String[] LHS = ((String) input[0]).trim().replaceAll(" +", " ").split(" "),
					RHS = ((String) input[2]).trim().replaceAll(" +", " ").split(" ");
			
			for(String l : LHS)
				if(isParenthesisCommand(l))
					myGrammar.getLanguageAlphabet().add(new Terminal(l));
			for(String r : RHS)
				if(isParenthesisCommand(r))
					myGrammar.getLanguageAlphabet().add(new Terminal(r));
			return super.objectToProduction(input);
		}
		
		private boolean isParenthesisCommand(String s){
			CommandAlphabet alph = new CommandAlphabet();
			for(Symbol symbol: alph.getParenCommands()){
				if(s.endsWith(")") && s.startsWith(symbol.getString()+"("))
					return true;
			}
			return false;
		}
	}
}
