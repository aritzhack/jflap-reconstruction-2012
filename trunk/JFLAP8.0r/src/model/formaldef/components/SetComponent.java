package model.formaldef.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


import model.change.ChangeEvent;
import model.change.ChangeListener;
import model.change.events.CompoundUndoableChangeEvent;
import model.change.events.SetComponentEvent;
import model.change.events.SetComponentModifyEvent;
import model.change.events.SetToEvent;
import model.change.rules.applied.SelfIdenticalRule;
import model.undo.IUndoRedo;

public abstract class SetComponent<T extends SetSubComponent<T>> extends FormalDefinitionComponent 
implements SortedSet<T>,
ChangeListener{
	private TreeSet<T> myComponents;

	public SetComponent() {
		myComponents = new TreeSet<T>();
		this.addRules(new SelfIdenticalRule<T>(ITEM_ADD, this),
				new SelfIdenticalRule<T>(ITEM_REMOVE, this),
				new SelfIdenticalRule<T>(ITEM_MODIFY, this));
	}


	public boolean modify(T from, T to){
		return from.setTo(to);
	}

	public boolean addAll(T ... toAdd) {
		return this.addAll(Arrays.asList(toAdd));
	}

	@Override
	public boolean add(T e) {
		return applyChange(new AddEvent(this,e));
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return applyChange(new AddEvent(this, new ArrayList<T>(c)));
	}

	@Override
	public void clear() {
		applyChange(new RemoveEvent(this, new ArrayList<T>(myComponents)));
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
		return applyChange(new RemoveEvent(this, (T) o));
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		Collection<T> replace = new ArrayList(c);
		return applyChange(new RemoveEvent(this, replace));
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		Set<T> leftover = new TreeSet<T>(myComponents);
		leftover.removeAll(c);
		return applyChange(new RemoveEvent(this, leftover));
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
		return this.getDescriptionName() + ": " + myComponents.toString();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.isOfType(SET_TO) && e.comesFrom(this.first())){
			SetToEvent<T, T> event = (SetToEvent<T, T>) e;
			e = new SetComponentModifyEvent<T>(this, event);
			distributeChange(e);
			return;
		}
		applyChange(e);
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
	@Override
	public boolean equals(Object obj) {
		return Arrays.equals(this.toArray(), ((SetComponent<T>) obj).toArray());
	}

	public SetComponentEvent<T> createRemoveEvent(T ... toRemove){
		return new RemoveEvent(this, toRemove);
	}
	
	public SetComponentEvent<T> createAddEvent(T ... toAdd){
		return new AddEvent(this, toAdd);
	}
	
	private class AddEvent extends SetComponentEvent<T>{


		public AddEvent(SetComponent<T> source, Collection<T> toAdd ) {
			super(ITEM_ADD, source, toAdd);
		}

		public AddEvent(SetComponent<T> source, T ... o) {
			this(source, Arrays.asList(o));
		}
		@Override
		public boolean undo() {
			return removeAll(getItems());
		}

		@Override
		public boolean redo() {
			return addAll(getItems());
		}

		@Override
		public String getName() {
			return "Add " + getItems().get(0).getDescriptionName() + "s";
		}

		@Override
		public boolean applyChange() {
			boolean added = myComponents.addAll(getItems());
			if (added){
				for (T item : getItems())
						item.addListener(SetComponent.this);
			}
			return added;
		}

	}


	private class RemoveEvent extends SetComponentEvent<T>{


		public RemoveEvent(SetComponent<T> source, Collection<T> replace ) {
			super(ITEM_REMOVE, source, replace);
		}

		public RemoveEvent(SetComponent<T> source, T ... o) {
			this(source, Arrays.asList(o));
		}

		@Override
		public boolean undo() {
			return addAll(getItems());
		}

		@Override
		public boolean redo() {
			return removeAll(getItems());
		}

		@Override
		public String getName() {
			return "Remove " + getItems().get(0).getDescriptionName();
		}

		@Override
		public boolean applyChange() {
			boolean removed = myComponents.removeAll(getItems());
			if (removed){
				for (T item : getItems())
						item.addListener(SetComponent.this);
			}
			return removed;
		}

	}
}
