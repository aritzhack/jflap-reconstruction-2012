package model.automata;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import errors.BooleanWrapper;

import model.formaldef.UsesSymbols;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.symbols.Symbol;
import model.formaldef.components.alphabets.symbols.SymbolString;




public abstract class TransitionLabel implements Cloneable, 
												Comparable<TransitionLabel>,
												UsesSymbols{

	
	/* (non-Javadoc)
	 * @see automata.ITransitionLabel#hashCode()
	 */
	@Override
	public int hashCode(){
		int hash = 1;
		for (SymbolString s: this.getArray()){
			hash *= s.hashCode();
		}
		return hash;
	}

	/**
	 * Makes a shallow clone. For a deep clone, use copy(formaldef);
	 */
	@Override
	public abstract TransitionLabel clone();
	
	

	public boolean equals(Object o){
		if (!this.getClass().isAssignableFrom(o.getClass()))
			return false;
		boolean equals = true;
		SymbolString[] ar1 = this.getArray(),
					   ar2 = ((TransitionLabel) o).getArray();
		for(int i = 0; i < this.getArray().length; i++)
			equals = equals && (ar1[i].equals(ar2[i]));
		return equals;
	}

	public boolean purgeOfSymbol(Symbol s){
		boolean res = false;
		for (SymbolString string: this.getArray()){
			res = string.purgeOfSymbol(s) || res;
		}
		return res;
	}
	
	
	@Override
	public abstract String toString();	
	
	public abstract SymbolString[] getArray();
	
}
