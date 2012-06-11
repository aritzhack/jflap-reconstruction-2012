package view;

import java.io.File;

import javax.swing.JPanel;


public interface Saveable {

	public boolean save(File f);
	
	public boolean shouldBeSaved();

}
