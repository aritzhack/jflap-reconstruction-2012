package view.grammar.parsing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import model.grammar.Grammar;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import model.undo.UndoKeeper;
import oldnewstuff.view.tree.DefaultTreeDrawer;
import oldnewstuff.view.tree.LeafNodePlacer;
import oldnewstuff.view.tree.TreePanel;
import universe.JFLAPUniverse;
import view.EditingPanel;
import view.environment.JFLAPEnvironment;
import view.grammar.ProductionTable;
import view.grammar.TableTextSizeSlider;

/**
 * The parse pane is an abstract class that defines the interface common between
 * parsing panes.
 * 
 * @author Thomas Finley, Ian McMahon
 */

public abstract class ParsePane extends EditingPanel {
	private Grammar myGrammar;
	private JLabel statusDisplay;
	private JTextField inputField;
	private DefaultTreeDrawer treeDrawer;
	private TreePanel treePanel;
	private CardLayout treeDerivationLayout;
	private JPanel treeDerivationPane;
	private DefaultTableModel derivationModel;
	private ProductionTable myProductionTable;

	/**
	 * Instantiates a new parse pane. This will not place components. A call to
	 * {@link #initView} by a subclass is necessary.
	 * @param table TODO
	 * @param grammar
	 *            the grammar that is being parsed
	 */
	public ParsePane(Grammar grammar, ProductionTable table) {
		super(new UndoKeeper(), false);
		setLayout(new BorderLayout());
		myGrammar = grammar;
		
		myProductionTable = new ProductionTable(myGrammar, getKeeper(), false);
		myProductionTable.setModel(table.getModel());
	}
	
	/**
	 * Initializes the GUI.
	 */
	protected void initView() {
		initVariables();
		treePanel = initTreePanel();

		// Sets up the displays.
		JComponent pt = initParseTable();
		JScrollPane parseTable = pt == null ? null : new JScrollPane(pt);
		JScrollPane grammarTable = initProductionTable();

		treeDerivationPane.add(initTreePanel(), "0");
		JScrollPane derivationPane = new JScrollPane(initDerivationTable());
		treeDerivationPane.add(derivationPane, "1");
		JSplitPane bottomSplit = createSplit(true, 0.3, grammarTable,
				treeDerivationPane);
		JSplitPane topSplit = createSplit(true, 0.4, parseTable,
				initInputPanel());
		JSplitPane mainSplit = createSplit(false, 0.3, topSplit,
				bottomSplit);
		add(mainSplit, BorderLayout.CENTER);
		add(statusDisplay, BorderLayout.SOUTH);
	}

	private void initVariables() {
		statusDisplay = new JLabel("Input a string to begin.");
		inputField = new JTextField();
		initTreeDerivationVariables();
		derivationModel = new DefaultTableModel(new String[] { "Production",
				"Derivation" }, 0) {
			public boolean isCellEditable(int r, int c) {
				return false;
			}};
	}
	

	private void initTreeDerivationVariables() {
		treeDrawer = new DefaultTreeDrawer(new DefaultTreeModel(
				new DefaultMutableTreeNode())) {
			protected Color getNodeColor(TreeNode node) {
				return node.isLeaf() ? LEAF : INNER;
			}

			private final Color INNER = new Color(100, 200, 120),
					LEAF = new Color(255, 255, 100);
		};
		treePanel = new TreePanel(treeDrawer);
		treeDerivationLayout = new CardLayout();
		treeDerivationPane = new JPanel(treeDerivationLayout);
	}

	

	/**
	 * Initializes a table for the grammar.
	 * 
	 * @param grammar
	 *            the grammar
	 * @return a table to display the grammar
	 */
	protected JScrollPane initProductionTable() {

		ProductionTable table = myProductionTable;
		
		TableTextSizeSlider slider = new TableTextSizeSlider(table);
		slider.addListener(table);
		//TODO: add other listeners to this magnifier
		slider.distributeMagnification();
		add(slider, BorderLayout.NORTH);
		JScrollPane scroll = new JScrollPane(table);
		return scroll;
	}

	/**
	 * Returns the interface that holds the input area.
	 */
	protected JPanel initInputPanel() {
		JTextField inputDisplay = new JTextField();
		JTextField stackDisplay = new JTextField();
		
		JPanel bigger = new JPanel(new BorderLayout());
		JPanel panel = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		panel.setLayout(gridbag);

		c.fill = GridBagConstraints.BOTH;

		c.weightx = 0.0;
		panel.add(new JLabel("Input"), c);
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(getInputField(), c);
		getInputField().addActionListener(startAction);
		// c.weightx = 0.0;
		// JButton startButton = new JButton(startAction);
		// panel.add(startButton, c);

		c.weightx = 0.0;
		c.gridwidth = 1;
		panel.add(new JLabel("Input Remaining"), c);
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		inputDisplay.setEditable(false);
		panel.add(inputDisplay, c);

		c.weightx = 0.0;
		c.gridwidth = 1;
		panel.add(new JLabel("Stack"), c);
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		stackDisplay.setEditable(false);
		panel.add(stackDisplay, c);

		bigger.add(panel, BorderLayout.CENTER);
		bigger.add(initInputToolbar(), BorderLayout.NORTH);

		return bigger;
	}

	/**
	 * Returns the choices for the view.
	 * 
	 * @return an array of strings for the choice of view
	 */
	protected String[] getViewChoices() {
		return new String[] { "Noninverted Tree", "Inverted Tree",
				"Derivation Table" };
	}

	/**
	 * Returns the tool bar for the main user input panel.
	 * 
	 * @return the tool bar for the main user input panel
	 */
	protected JToolBar initInputToolbar() {
		JToolBar toolbar = new JToolBar();
		toolbar.add(startAction);
		stepAction.setEnabled(false);
		toolbar.add(stepAction);

		// Set up the view customizer controls.
		toolbar.addSeparator();

		final JComboBox box = new JComboBox(getViewChoices());
		box.setSelectedIndex(0);
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeView((String) box.getSelectedItem());
			}
		};
		box.addActionListener(listener);
		toolbar.add(box);
		return toolbar;
	}

	/**
	 * Changes the view.
	 * 
	 * @param name
	 *            the view button name that was pressed
	 */
	protected void changeView(String name) {
		if (name.equals("Noninverted Tree")) {
			treeDerivationLayout.first(treeDerivationPane);
			treeDrawer.setInverted(false);
			treePanel.repaint();
		} else if (name.equals("Inverted Tree")) {
			treeDerivationLayout.first(treeDerivationPane);
			treeDrawer.setInverted(true);
			treePanel.repaint();
		} else if (name.equals("Derivation Table")) {
			treeDerivationLayout.last(treeDerivationPane);
		}
	}
	
	public AbstractAction stepAction = new AbstractAction("Step") {
		public void actionPerformed(ActionEvent e) {
			step();
		}
	};
	public AbstractAction startAction = new AbstractAction("Start") {
		public void actionPerformed(ActionEvent e) {
			SymbolString newInput = Symbolizers.symbolize(inputField.getText(), myGrammar);
			input(newInput);
		}
	};

	/**
	 * Inits a parse table.
	 * 
	 * @return a table to hold the parse table
	 */
	protected abstract JTable initParseTable();

	/**
	 * Inits a new tree panel.
	 * 
	 * @return a new display for a parse tree
	 */
	protected TreePanel initTreePanel() {
		treeDrawer.hideAll();
		treeDrawer.setNodePlacer(new LeafNodePlacer());
		return treePanel;
	}

	/**
	 * Inits a new derivation table.
	 * 
	 * @return a new display for the derivation of the parse
	 */
	protected JTable initDerivationTable() {
		JTable table = new JTable(derivationModel);
		table.setGridColor(Color.lightGray);
		return table;
	}

	/**
	 * This method is called when there is new input to parse.
	 * 
	 * @param string
	 *            a new input string
	 */
	protected abstract void input(SymbolString string);

	/**
	 * This method is called when the step button is pressed.
	 */
	protected abstract boolean step();

	/**
	 * Prints this component. This will print only the tree section of the
	 * component.
	 * 
	 * @param g
	 *            the graphics object to print to
	 */
	public void printComponent(Graphics g) {
		treeDerivationPane.print(g);
	}

	/**
	 * Children are not painted here.
	 * 
	 * @param g
	 *            the graphics object to paint to
	 */
	public void printChildren(Graphics g) {

	}

	public JSplitPane createSplit(boolean horizontal, double ratio, Component left, Component right) {
		JFLAPEnvironment environment = JFLAPUniverse.getActiveEnvironment();
		JSplitPane split = new JSplitPane(
				horizontal ? JSplitPane.HORIZONTAL_SPLIT
						: JSplitPane.VERTICAL_SPLIT, true, left, right);
		Dimension dim = environment.getSize();
		Component[] comps = environment.getComponents();
		if (comps.length != 0)
			dim = comps[0].getSize();
		int size = horizontal ? dim.width : dim.height;
		split.setDividerLocation((int) ((double) size * ratio));
		split.setResizeWeight(ratio);
		return split;
	}
	
	public Grammar getGrammar(){
		return myGrammar;
	}

	public DefaultTableModel getDerivationModel() {
		return derivationModel;
	}

	public JLabel getStatusDisplay() {
		return statusDisplay;
	}

	public JTextField getInputField() {
		return inputField;
	}

}
