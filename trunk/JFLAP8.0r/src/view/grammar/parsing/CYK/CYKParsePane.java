package view.grammar.parsing.CYK;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
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
import model.undo.UndoKeeper;
import oldnewstuff.view.tree.DefaultTreeDrawer;
import oldnewstuff.view.tree.LeafNodePlacer;
import oldnewstuff.view.tree.TreePanel;
import universe.JFLAPUniverse;
import view.EditingPanel;
import view.environment.JFLAPEnvironment;
import view.grammar.ProductionTable;
import view.grammar.TableTextSizeSlider;
import debug.JFLAPDebug;

public class CYKParsePane extends EditingPanel {

	private Grammar myGrammar;
	private DefaultTreeDrawer treeDrawer;
	private TreePanel treePanel;
	private JPanel TDCardPane;
	private ProductionTable myProductionTable;
	private CYKParseController myController;
	private JTextField inputField;
	private JPanel contentPane;

	public CYKParsePane(Grammar grammar, ProductionTable table) {
		super(new UndoKeeper(), false);
		myGrammar = grammar;
		myProductionTable = table;

		setLayout(new BorderLayout());
		setName("CYK Parser");

		initView();
	}

	/**
	 * Initializes the GUI.
	 */
	private void initView() {
		JLabel statusDisplay = new JLabel(
				"Input a string and press Start to begin");

		JPanel treeDerivationPane = initTreeDerivationPane();

		CYKParseTable parseTable = new CYKParseTable(myGrammar);

		TableTextSizeSlider slider = new TableTextSizeSlider(myProductionTable,
				parseTable);
		slider.distributeMagnification();

		myController = new CYKParseController(parseTable, this, statusDisplay);

		JScrollPane parsePane = new JScrollPane(parseTable);
		JScrollPane grammarTable = new JScrollPane(myProductionTable);

		contentPane = new JPanel(new CardLayout());
		contentPane.add(parsePane, "0");
		contentPane.add(treeDerivationPane, "1");

		JPanel bottomPane = new JPanel(new BorderLayout());
		bottomPane.add(contentPane, BorderLayout.CENTER);
		bottomPane.add(slider, BorderLayout.SOUTH);

		JSplitPane topSplit = createSplit(true, 0.3, grammarTable,
				initInputPanel());
		JPanel topPane = new JPanel(new BorderLayout());
		topPane.add(topSplit, BorderLayout.CENTER);
		topPane.add(initInputToolbar(), BorderLayout.SOUTH);

		JSplitPane mainSplit = createSplit(false, 0.25, topPane, bottomPane);

		add(mainSplit, BorderLayout.CENTER);
		add(statusDisplay, BorderLayout.SOUTH);
	}

	private JPanel initTreeDerivationPane() {
		TDCardPane = new JPanel(new CardLayout());

		treePanel = initTreePanel();
		TDCardPane.add(treePanel, "0");

		JScrollPane derivationPane = initDerivationPane();
		TDCardPane.add(derivationPane, "1");

		JToolBar treeControls = initTreeControls();

		JPanel treeDerivationPane = new JPanel(new BorderLayout());
		treeDerivationPane.add(treeControls, BorderLayout.NORTH);
		treeDerivationPane.add(TDCardPane, BorderLayout.CENTER);

		return treeDerivationPane;
	}

	private TreePanel initTreePanel() {
		treeDrawer = new DefaultTreeDrawer(new DefaultTreeModel(
				new DefaultMutableTreeNode())) {
			protected Color getNodeColor(TreeNode node) {
				return node.isLeaf() ? LEAF : INNER;
			}

			private final Color INNER = new Color(100, 200, 120),
					LEAF = new Color(255, 255, 100);
		};
		treeDrawer.hideAll();
		treeDrawer.setNodePlacer(new LeafNodePlacer());
		return new TreePanel(treeDrawer);
	}

	private JScrollPane initDerivationPane() {
		DefaultTableModel derivationModel = new DefaultTableModel(new String[] {
				"Production", "Derivation" }, 0) {
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		JTable derivationTable = new JTable(derivationModel);
		derivationTable.setGridColor(Color.lightGray);
		JScrollPane derivationPane = new JScrollPane(derivationTable);
		return derivationPane;
	}

	private JToolBar initTreeControls() {
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);

		final JComboBox box = new JComboBox(getViewChoices());
		box.setSelectedIndex(0);
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeView((String) box.getSelectedItem());
			}
		};
		box.addActionListener(listener);

		toolbar.add(box);
		toolbar.addSeparator();
		toolbar.add(new AbstractAction("Next derivation") {
			public void actionPerformed(ActionEvent e) {
				stepTreeDerivation();
			}
		});

		return toolbar;
	}

	private String[] getViewChoices() {
		return new String[] { "Noninverted Tree", "Inverted Tree",
				"Derivation Table" };
	}

	private JPanel initInputPanel() {
		inputField = new JTextField();
		JPanel panel = new JPanel();

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		panel.setLayout(gridbag);

		c.fill = GridBagConstraints.BOTH;

		c.weightx = 0.0;
		panel.add(new JLabel("Input"), c);
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		panel.add(inputField, c);
		inputField.addActionListener(myController.startAction);

		return panel;
	}

	/**
	 * Returns the tool bar for the main user input panel.
	 * 
	 * @return the tool bar for the main user input panel
	 */
	private JToolBar initInputToolbar() {
		JToolBar toolbar = new JToolBar();

		toolbar.add(myController.startAction);

		toolbar.add(myController.selectedAction);
		myController.selectedAction.setEnabled(false);

		toolbar.add(myController.stepAction);
		myController.stepAction.setEnabled(false);

		toolbar.add(myController.allAction);
		myController.allAction.setEnabled(false);

		toolbar.add(myController.nextAction);
		myController.nextAction.setEnabled(false);

		toolbar.add(myController.derivationAction);
		myController.derivationAction.setEnabled(false);

		toolbar.setFloatable(false);
		return toolbar;
	}

	/**
	 * Changes the view.
	 * 
	 * @param name
	 *            the view button name that was pressed
	 */
	private void changeView(String name) {
		CardLayout treeDerivationLayout = (CardLayout) TDCardPane.getLayout();
		if (name.equals("Noninverted Tree")) {
			treeDerivationLayout.first(TDCardPane);
			treeDrawer.setInverted(false);
			treePanel.repaint();
		} else if (name.equals("Inverted Tree")) {
			treeDerivationLayout.first(TDCardPane);
			treeDrawer.setInverted(true);
			treePanel.repaint();
		} else if (name.equals("Derivation Table")) {
			treeDerivationLayout.last(TDCardPane);
		}
	}

	public JSplitPane createSplit(boolean horizontal, double ratio,
			Component left, Component right) {
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
		CardLayout card = (CardLayout) contentPane.getLayout();
		card.last(contentPane);
	}

	public void switchToTableView() {
		CardLayout card = (CardLayout) contentPane.getLayout();
		card.first(contentPane);
	}

	public String getInputText() {
		return inputField.getText();
	}

	public Grammar getGrammar() {
		return myGrammar.copy();
	}

	/**
	 * This method is called when the step button is pressed.
	 */
	public void stepTreeDerivation() {
		// TODO: Wait for tree drawing to done
		// boolean worked = false;
		// if (treePanel) {
		// stepAction.setEnabled(false);
		// worked = true;
		// }
		//
		// treePanel.repaint();
		// return worked;
		JFLAPDebug.print("TODO!!");
	}

}
