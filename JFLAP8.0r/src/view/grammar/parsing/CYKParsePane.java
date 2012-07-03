package view.grammar.parsing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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

import debug.JFLAPDebug;

import oldnewstuff.view.tree.DefaultTreeDrawer;
import oldnewstuff.view.tree.LeafNodePlacer;
import oldnewstuff.view.tree.TreePanel;

import model.grammar.Grammar;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import view.EditingPanel;
import view.environment.JFLAPEnvironment;
import view.grammar.ProductionTable;
import view.grammar.TableTextSizeSlider;

public class CYKParsePane extends EditingPanel{

	private SymbolString myTarget;
	private Grammar myGrammar;
	private JLabel statusDisplay;
	private DefaultTreeDrawer treeDrawer;
	private TreePanel treePanel;
	private CardLayout treeDerivationLayout;
	private JPanel treeDerivationPane;
	private DefaultTableModel derivationModel;
	private ProductionTable myProductionTable;
	private CYKParseTable parseTable;
	private CYKParseController myController;

	public CYKParsePane(Grammar grammar, ProductionTable table, SymbolString input) {
		super(new UndoKeeper(), false);
		myTarget = input;
		myGrammar = grammar;
		myProductionTable = table;
		setLayout(new BorderLayout());
		initView();
	}

	/**
	 * Initializes the GUI.
	 */
	protected void initView() {
		initVariables();
		treePanel = initTreePanel();

		// Sets up the displays.
		parseTable = new CYKParseTable(myTarget, myGrammar);
		JScrollPane pt = new JScrollPane(parseTable);
		

		ProductionTable table = myProductionTable;
		
		TableTextSizeSlider slider = new TableTextSizeSlider(table);
		slider.addListener(table);
		slider.addListener(parseTable);
		slider.distributeMagnification();
		JScrollPane grammarTable = new JScrollPane(table);
		

		myController = new CYKParseController(parseTable, this, statusDisplay);

		treeDerivationPane.add(initTreePanel(), "0");
		JScrollPane derivationPane = new JScrollPane(initDerivationTable());
		treeDerivationPane.add(derivationPane, "1");
		JSplitPane bottomSplit = createSplit(false, 0.07, initInputToolbar(),
				pt);
//		JSplitPane inputSplit = createSplit(false, 0.3, initInputToolbar(),
//				slider);
		JSplitPane topSplit = createSplit(true, 0.4, grammarTable, slider);
		JSplitPane mainSplit = createSplit(false, 0.25, topSplit,
				bottomSplit);
		add(mainSplit, BorderLayout.CENTER);
		add(statusDisplay, BorderLayout.SOUTH);
	}

	private void initVariables() {
		statusDisplay = new JLabel("Fill in the first diagonal of the table");
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
		toolbar.add(myController.selectedAction);
		toolbar.add(myController.stepAction);
		toolbar.add(myController.allAction);
		toolbar.add(myController.nextAction);
		toolbar.add(myController.derivationAction);
		myController.derivationAction.setEnabled(false);
		// Set up the view customizer controls.
		toolbar.addSeparator();

//		final JComboBox box = new JComboBox(getViewChoices());
//		box.setSelectedIndex(0);
//		ActionListener listener = new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				changeView((String) box.getSelectedItem());
//			}
//		};
//		box.addActionListener(listener);
//		toolbar.add(box);
		return toolbar;
	}
	
	protected TreePanel initTreePanel() {
		treeDrawer.hideAll();
		treeDrawer.setNodePlacer(new LeafNodePlacer());
		return treePanel;
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
	
	protected JTable initDerivationTable() {
		JTable table = new JTable(derivationModel);
		table.setGridColor(Color.lightGray);
		return table;
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

	public void switchToDerivationView() {
		// TODO Auto-generated method stub
		JFLAPDebug.print("TODO!!");
	}
}
