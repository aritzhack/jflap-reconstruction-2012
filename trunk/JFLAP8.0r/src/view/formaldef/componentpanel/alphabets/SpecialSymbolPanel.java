package view.formaldef.componentpanel.alphabets;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;

import preferences.JFLAPPreferences;


import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.symbols.SpecialSymbol;
import model.formaldef.components.symbols.Symbol;
import model.undo.UndoKeeper;

import view.action.ModifySymbolAction;
import view.action.SetSpecialSymbolAction;
import view.formaldef.componentpanel.DefinitionComponentPanel;

public class SpecialSymbolPanel extends DefinitionComponentPanel<SpecialSymbol>{

	private JButton myButton;
	private SpecialSymbol mySpecialSymbol;

	public SpecialSymbolPanel(SpecialSymbol comp, UndoKeeper keeper, boolean modify) {
		super(comp, keeper, modify );
		mySpecialSymbol = comp;
		JToolBar bar = new JToolBar();
		bar.add(myButton = new JButton(comp.symbolOnlyString()));
		myButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3 && 
						isEditable() && !mySpecialSymbol.isPermanent())
					getMenu().show(e.getComponent(), e.getX(), e.getY());
			}
		});
		this.add(bar);
		bar.setFloatable(false);
	}

	@Override
	public void update(ChangeEvent e) {
		if (e.getSource() instanceof SpecialSymbol)
			myButton.setText(mySpecialSymbol.getSymbol().toString());
	}

	public JPopupMenu getMenu() {
		JPopupMenu menu = new JPopupMenu();
		menu.add(new SetSpecialSymbolAction(mySpecialSymbol, getKeeper()));
		return menu;

	}

	@Override
	public void setMagnification(double mag) {
		super.setMagnification(mag);
		float size = (float) (mag*JFLAPPreferences.getDefaultTextSize());
		Font f = myButton.getFont().deriveFont(size);
		myButton.setFont(f);
	}
}
