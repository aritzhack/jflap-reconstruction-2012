package view.algorithms.conversion.regextofa;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import file.xml.graph.AutomatonEditorData;
import model.algorithms.conversion.regextofa.RegularExpressionToNFAConversion;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import universe.JFLAPUniverse;
import view.ViewFactory;
import view.automata.AutomatonDisplayPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.simulate.TooltipAction;
import view.automata.tools.DeexpressionTransitionTool;
import view.automata.tools.NonTransitionArrowTool;
import view.automata.tools.REtoFATransitionTool;
import view.automata.tools.ToolBar;
import view.environment.JFLAPEnvironment;

public class RegularExpressionToFAPanel extends AutomatonDisplayPanel<FiniteStateAcceptor, FSATransition>{

	private RegularExpressionToNFAConversion myAlg;
	private JLabel mainLabel;
	private JLabel detailLabel;

	public RegularExpressionToFAPanel(
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> editor,
			RegularExpressionToNFAConversion convert) {
		super(editor, editor.getAutomaton(), "Convert RE to NFA");
		myAlg = convert;
		updateSize();
		initView();
	}

	private void initView() {
		JPanel labels = new JPanel(new BorderLayout());
		mainLabel = new JLabel(" ");
		detailLabel = new JLabel(" ");
		labels.add(mainLabel, BorderLayout.NORTH);
		labels.add(detailLabel, BorderLayout.SOUTH);

		add(labels, BorderLayout.NORTH);
		
		ToolBar tools = createToolbar();
		JScrollPane scroll = new JScrollPane(getEditorPanel());
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(tools, BorderLayout.NORTH);
		panel.add(scroll, BorderLayout.CENTER);
		
		add(panel, BorderLayout.CENTER);
	}

	private ToolBar createToolbar() {
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getEditorPanel();
		FiniteStateAcceptor fsa = panel.getAutomaton();
		
		NonTransitionArrowTool<FiniteStateAcceptor, FSATransition> arrow = new NonTransitionArrowTool<FiniteStateAcceptor, FSATransition>(panel, fsa );
		REtoFATransitionTool trans = new REtoFATransitionTool(panel, myAlg);
		DeexpressionTransitionTool deex = new DeexpressionTransitionTool(panel, myAlg);
		
		ToolBar tools = new ToolBar(arrow, trans, deex);
		tools.addToolListener(panel);
		panel.setTool(arrow);
		
		tools.addSeparator();
		tools.add(new AbstractAction("Step") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(myAlg.canStep())
					myAlg.step();
			}
		});
		
		tools.add(new AbstractAction("Step to Completion") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(myAlg.canStep())
					myAlg.stepToCompletion();
			}
		});
		
		tools.add(new AbstractAction("Export") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				export();
			}
		});
		return tools;
	}

	private void export() {
		JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();
		if(myAlg.isRunning())
			JOptionPane.showMessageDialog(env, "The conversion is not completed yet!");
		else{
			AutomatonEditorData<FiniteStateAcceptor, FSATransition> data = new AutomatonEditorData<FiniteStateAcceptor, FSATransition>(getEditorPanel());
			
			JFLAPUniverse.registerEnvironment(ViewFactory.createAutomataView(data));
		}
	}

}
