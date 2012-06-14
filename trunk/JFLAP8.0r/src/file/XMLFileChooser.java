package file;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import util.JFLAPConstants;


import file.xml.XMLCodec;


public class XMLFileChooser extends JFileChooser {

	public XMLFileChooser(){
		super(System.getProperties().getProperty("user.dir"));
		this.setFileFilter(XMLCodec.getJFFfileFilter());
	}
}
