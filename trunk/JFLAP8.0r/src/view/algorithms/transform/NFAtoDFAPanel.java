package view.algorithms.transform;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import model.algorithms.transform.fsa.NFAtoDFAConverter;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableScrollPane;
import view.automata.AutomatonDisplayPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.simulate.TooltipAction;
import view.automata.tools.NonTransitionArrowTool;
import view.automata.tools.StateExpanderTool;
import view.automata.tools.ToolBar;
import view.automata.tools.TransitionExpanderTool;

public class NFAtoDFAPanel extends AutomatonDisplayPanel<FiniteStateAcceptor, FSATransition>{

	private NFAtoDFAConverter myAlg;
	private AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> myDFApanel;

	public NFAtoDFAPanel(AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> nfa, NFAtoDFAConverter convert) {
		super(nfa, nfa.getAutomaton(), "NFA to DFA");
		myAlg = convert;
		updateSize();
		initView();
	}

	private void initView() {
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> nfa = getEditorPanel();
		myDFApanel = new AutomatonEditorPanel<FiniteStateAcceptor, FSATransition>(myAlg.getDFA(), new UndoKeeper(), true);
	
		MagnifiableScrollPane nScroll = new MagnifiableScrollPane(nfa), dScroll = new MagnifiableScrollPane(myDFApanel);
		MagnifiablePanel right = new MagnifiablePanel(new BorderLayout());
		
		ToolBar tools = createTools();
		right.add(tools, BorderLayout.NORTH);
		right.add(dScroll, BorderLayout.CENTER);

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, nScroll, right);
		add(split, BorderLayout.CENTER);
	}

	private ToolBar createTools() {
		NonTransitionArrowTool<FiniteStateAcceptor, FSATransition> arrow = new NonTransitionArrowTool<FiniteStateAcceptor, FSATransition>(myDFApanel, myDFApanel.getAutomaton());
		TransitionExpanderTool trans = new TransitionExpanderTool(myDFApanel, myAlg);
		StateExpanderTool state = new StateExpanderTool(myDFApanel, myAlg);
		
		ToolBar tools = new ToolBar(arrow, trans, state);
		tools.addToolListener(myDFApanel);
		myDFApanel.setTool(arrow);
		
		tools.addSeparator();
		tools.add(new TooltipAction("Complete",
				"This will finish all expansion.") {
			public void actionPerformed(ActionEvent e) {
				myAlg.stepToCompletion();
			}
		});
		tools.add(new TooltipAction("Done?", "Are we finished?") {
			public void actionPerformed(ActionEvent e) {
				done();
			}
		});
		
		return tools;
	}
	
	private void done() {
//		int statesRemaining = myAlg.numUnexpandedStates(), transitionsRemaining = answer
//				.getTransitions().length
//				- dfa.getTransitions().length;
//		if (statesRemaining + transitionsRemaining != 0) {
//			String states = statesRemaining == 0 ? "All the states are there.\n"
//					: statesRemaining + " more state"
//							+ (statesRemaining == 1 ? "" : "s")
//							+ " must be placed.\n";
//			String trans = transitionsRemaining == 0 ? "All the transitions are there.\n"
//					: transitionsRemaining + " more transition"
//							+ (transitionsRemaining == 1 ? "" : "s")
//							+ " must be placed.\n";
//			String message = "The DFA has not been completed.\n" + states
//					+ trans;
			JOptionPane.showMessageDialog(JFLAPUniverse.getActiveEnvironment(), "Do this");
			return;
		}
	}

