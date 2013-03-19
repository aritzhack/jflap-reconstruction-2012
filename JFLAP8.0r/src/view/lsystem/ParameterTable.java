package view.lsystem;

import java.awt.Font;

import universe.preferences.JFLAPPreferences;
import util.view.magnify.Magnifiable;
import util.view.tables.HighlightTable;

public class ParameterTable extends HighlightTable implements Magnifiable{
	public ParameterTable(ParameterTableModel model){
		super(model);
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setResizingAllowed(false);
	}

	@Override
	public void setMagnification(double mag) {
		float size = (float) (mag*JFLAPPreferences.getDefaultTextSize());
		Font font = getFont().deriveFont(size);
        this.setFont(font);
        this.getTableHeader().setFont(font);
        this.setRowHeight((int) (size+10));
	}
	
	
}
