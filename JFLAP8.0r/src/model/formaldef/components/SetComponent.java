package model.formaldef.components;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import model.formaldef.components.symbols.Symbol;

import util.Copyable;

import errors.BooleanWrapper;

public abstract class SetComponent<T extends Copyable> extends FormalDefinitionComponent implements SortedSet<T>{

	
	private TreeSet<T> myComponents;
	
	public SetComponent() {
		myComponents = new TreeSet<T>();
	}
	
	@Override
	public boolean add(T e) {
		return conditionalDistributeChange(myComponents.add(e), ITEM_ADDED, e);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean added = false;
		for (T t: c){
			added = this.add(t) || added;
		}
		return added;
	}

	@Override
	public void clear() {
		for (Object t : this.toArray()){
			this.remove(t);
		}
	}

	@Override
	public boolean contains(Object o) {
		return myComponents.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return myComponents.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return myComponents.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return myComponents.iterator();
	}

	@Override
	public boolean remove(Object o) {
		return conditionalDistributeChange(myComponents.remove(o), ITEM_REMOVED, o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean removed = false;
		for (Object t: c){
			removed = this.remove(t) || removed;
		}
		return removed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return myComponents.retainAll(c);
	}

	@Override
	public int size() {
		return myComponents.size();
	}

	@Override
	public Object[] toArray() {
		return myComponents.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return myComponents.toArray(a);
	}

	@Override
	public Comparator<? super T> comparator() {
		return myComponents.comparator();
	}

	@Override
	public T first() {
		return myComponents.first();
	}

	@Override
	public SortedSet<T> headSet(T toElement) {
		return myComponents.headSet(toElement);
	}

	@Override
	public T last() {
		return myComponents.last();
	}

	@Override
	public SortedSet<T> subSet(T fromElement, T toElement) {
		return myComponents.subSet(fromElement, toElement);
	}

	@Override
	public SortedSet<T> tailSet(T fromElement) {
		return myComponents.tailSet(fromElement);
	}

	@Override
	public String toString() {
		return myComponents.toString();
	}

	@Override
	public FormalDefinitionComponent copy() {
		try {
			SetComponent<T> cloned = this.getClass().newInstance();
			for (T obj: this){
				cloned.add((T) obj.copy());
			}
			return cloned;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	

}
