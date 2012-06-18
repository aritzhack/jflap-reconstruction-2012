package view.grammar;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;


public class SelectingEditor extends DefaultCellEditor {

	public SelectingEditor() {
	    this(new JTextField());
	   }
	
	public SelectingEditor(JTextField textField) {
	    super(textField);
	   }
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	    Component c = super.getTableCellEditorComponent(table, value, isSelected, row, column);
	    if ( c instanceof JTextComponent) {
	     final JTextComponent jtc = (JTextComponent)c;
	     jtc.requestFocus();
	     SwingUtilities.invokeLater(new Runnable()
	     {
	         public void run()
	         {
	             jtc.selectAll();
	         }
	     });
	    }
	    return c;
   }
	
	public static void setUpSelectingEditor(JTable table){
		for(int i=0; i<table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setCellEditor(new SelectingEditor());
	}
}
