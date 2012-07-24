package edu.iastate.cs228.hw3;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A type of list that keeps elements sorted by "priority"
 *  
 * @author Brian Reber
 */
public class PriorityList<E> implements List<E>, IPriorityList<E> {
	/**
	 * The array of Priority objects. This is used to store information
	 * about the different priority levels.
	 */
	private final Priority[] priorities;
	/**
	 * The "dummy node" that is the start of the list
	 */
	private final Node head;
	/**
	 * The last node of the list
	 */
	private Node tail;
	/**
	 * The size of the full list
	 */
	private int size;
	
	/**
	 * Creates a new PriorityList with the given number of priority levels.
	 * 
	 * @param countPriorities
	 * the number, max + 1 of priority levels
	 */
	@SuppressWarnings("unchecked")
	public PriorityList(int countPriorities) {
		priorities = new PriorityList.Priority[countPriorities];
		
		for (int i = 0; i < priorities.length; i++) {
			priorities[i] = new Priority();
		}
		
		head = new Node(null, null, null, -1);
		tail = head;
	}
	
	/**
	 * A class that stores the size and starting information
	 * about the priority levels.
	 *  
	 * @author Brian Reber
	 */
	private class Priority {
		/**
		 * The starting node of this priority level
		 */
		public Node start;
		/**
		 * The size of this priority level
		 */
		public int size;
	}

	/**
	 * A class storing information about a node in a doubly-linked list.
	 * 
	 * @author Brian Reber
	 */
	private class Node {
		/**
		 * The previous node in the list
		 */
		public Node previous;
		/**
		 * The next node in the list
		 */
		public Node next;
		/**
		 * The data this node holds
		 */
		public E data;
		/**
		 * The priority of this node
		 */
		public int priority;
		
		/**
		 * Creates a new Node with the given data, next node, previous node and
		 * priority.
		 * 
		 * @param data
		 * The data to be held in this node
		 * @param next
		 * The next node in the list
		 * @param previous
		 * The previous node in the list
		 * @param priority
		 * The priority of this node
		 */
		public Node(E data, Node next, Node previous, int priority) {
			this.data = data;
			this.next = next;
			this.previous = previous;
			this.priority = priority;
		}
	}
	
	/**
	 * A List Iterator that traverses a PriorityList.
	 * 
	 * @author Brian Reber
	 */
	private class PriorityListIterator implements ListIterator<E> {
		/**
		 * The priority level this iterator is going over.
		 * -Negative 1 if the whole list
		 */
		private int iteratorPriority;
		/**
		 * The index of the cursor
		 */
		private int cursorIndex = 0;
		/**
		 * The Node that will get returned on a call to next()
		 */
		private Node cursor = null;
		/**
		 * The Node that has just been viewed by the call to next()
		 * or previous()
		 */
		private Node justViewed = null;
		/**
		 * Whether or not we can remove the element we just viewed.
		 * Should be false unless next() or previous() has been called
		 * in the immediate past
		 */
		private boolean canRemove = false;
		/**
		 * Whether we just moved forward or not.
		 */
		private boolean movedForward;
		
		/**
		 * Creates a PriorityListIterator that will start at the first node in 
		 * the list and iterate through the whole list.
		 */
		public PriorityListIterator() {
			this.cursor = head.next;
			this.iteratorPriority = -1;
		}
		
		/**
		 * Creates a PriorityListIterator that will start at the first node at
		 * the given priority and iterate through the sublist with priority
		 * equal to iteratorPriority.
		 * 
		 * @param iteratorPriority
		 * The priority level this iterator should iterate over
		 * @throws IndexOutOfBoundsException if iteratorPriority is greater than the maximum
		 * priority or less than 0
		 */
		public PriorityListIterator(int iteratorPriority) {
			if (iteratorPriority < 0 || iteratorPriority > priorities.length) {
				throw new IndexOutOfBoundsException();
			}
			
			this.iteratorPriority = iteratorPriority;
			this.cursor = priorities[iteratorPriority].start;
		}
		
		/**
		 * Sets whether this iterator is able to remove or not
		 * 
		 * @param canRemove
		 * Whether or not this iterator should be able to remove
		 */
		private void setCanRemove(boolean canRemove) {
			this.canRemove = canRemove;
		}
		
		/**
		 * Returns true if there are more elements ahead of the current position
		 * within the given priority level (the whole list if iteratorPriority was not specifed).
		 * 
		 * @return true if there are more elements within the given priority level
		 * (the whole list if iteratorPriority was not specified.
		 */
		@Override
		public boolean hasNext() {
			if (iteratorPriority == -1) {
				return size > 0 && cursor != null;
			} else {
				return priorities[iteratorPriority].size > 0 && cursorIndex < priorities[iteratorPriority].size && cursor != null;
			}
		}

		/**
		 * Gets the next element in the iterator's priority level (the whole list if
		 * a priority level was not specified).
		 * 
		 * @return the next element in the iterator's priority level
		 * @throws NoSuchElementException if there is not a next element
		 */
		@Override
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			justViewed = cursor;
			cursor = cursor.next;
			
			movedForward = true;
			cursorIndex++;
			canRemove = true;
			return justViewed.data;
		}

		/**
		 * Returns true if there are more elements prior to the current element 
		 * within the given priority level (the whole list if iteratorPriority was not specifed).
		 * 
		 * @return true if there are more elements prior to the current element 
		 * within the given priority level (the whole list if iteratorPriority was not specified.
		 */
		@Override
		public boolean hasPrevious() {
			return (cursorIndex > 0 && cursor != null && cursor.previous != null) || 
				(cursor == null && cursorIndex != 0);
		}

		/**
		 * Gets the previous element in the iterator's priority level (the whole list if
		 * a priority level was not specified).
		 * 
		 * @return the previous element in the iterator's priority level
		 * @throws NoSuchElementException if there is not a previous element
		 */
		@Override
		public E previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}
			E ret = null;
			
			if (cursor == null && iteratorPriority != -1) {
				cursor = priorities[iteratorPriority].start;
				justViewed = null;
				
				ret = cursor.data;
			} else if (cursor == null && cursorIndex != 0) {
				cursor = tail;
				justViewed = null;
				
				ret = cursor.data;
			} else {
				ret = justViewed.data;
				justViewed = cursor.previous;
				cursor = justViewed.next;
			}
			
			movedForward = false;
			cursorIndex--;
			canRemove = true;
			
			return ret;
		}

		/**
		 * Gets the index of the element that would be returned by a call to next()
		 * 
		 * @return the index of the element that would be returned by a call to next 
		 */
		@Override
		public int nextIndex() {
			return cursorIndex;
		}

		/**
		 * Gets the index of the element that would be returned by a call to previous()
		 * 
		 * @return the index of the element that would be returned by a call to previous 
		 */
		@Override
		public int previousIndex() {
			return cursorIndex - 1;
		}

		/**
		 * Removes the element that was just viewed from the list
		 */
		@Override
		public void remove() {
			if (!canRemove) {
				throw new IllegalStateException();
			}
			int tempPriority;
			
			if (movedForward) {
				tempPriority = justViewed.priority;
				// removing element at cursor
				cursor.previous = justViewed.previous;
				cursor.previous.next = cursor;
			} else {
				// Not the end of the list
				if (justViewed != null && cursorIndex != ((iteratorPriority == -1) ? size() : sizeWithPriority(iteratorPriority))) {
					tempPriority = justViewed.priority;
					justViewed.previous.next = cursor;
					cursor.previous = justViewed.previous;
				} else {
					// The end of the list needs to be treated a bit differently
					justViewed = cursor;
					cursor = cursor.previous;
					tempPriority = justViewed.priority;
					cursor.next = null;
					tail = cursor;
				}
			}

			cursorIndex--;
			canRemove = false;
			priorities[tempPriority].size--;
			size--;
			justViewed = null;
		}

		/**
		 * Sets the element last returned to the given element
		 * 
		 * @param e
		 * The element to replace the previously viewed element
		 */
		@Override
		public void set(E e) {
			if (!canRemove) {
				throw new IllegalStateException();
			}
			
			// If we just moved forward, we want to set the element
			// we just viewed
			if (movedForward) {
				justViewed.data = e;
			} else {
				// Otherwise, since cursor represents the element that will
				// be returned by next(), we need to set that one
				cursor.data = e;
			}
		}

		/**
		 * Adds an element before the cursor position with the same
		 * priority level as the element at the cursor position.
		 * 
		 * @param e
		 * The element to add to the list
		 */
		@Override
		public void add(E e) {
			Node n = null;
			// The normal situation - adding in the middle of the list
			if (cursor != null) {
				n = new Node(e, cursor, cursor.previous, cursor.previous.priority);
				cursor.previous.next = n; 
				cursor.previous = n;
			} else if (cursor == null && cursorIndex == 0 && iteratorPriority != -1) {
				// Adding at the beginning of the empty priority sub-list
				
				// Find the last node with a priority level less than the iterator's priority
				Node temp = head.next;
				while (temp.next.priority < iteratorPriority) {
					temp = temp.next;
				}
				
				n = new Node(e, temp.next, temp, iteratorPriority);
				if (temp.next != null) {
					temp.next.previous = n;
				}
				temp.next = n;
			} else if (cursor == null && cursorIndex == 0) {
				// Adding at the beginning of the list
				n = new Node(e, null, head, 0);
				head.next = n; 
			} else {
				// Addint at the end of the list
				n = new Node(e, null, tail, tail.priority);
				tail.next = n; 
			}
			
			// If we are at the end, update the tail reference
			if (n.next == null && cursorIndex == size()) {
				tail = n;
			}
			// If the next element was the starting node of the same priority
			// we need to reset that
			if (n.next == priorities[n.priority].start || (
					cursor == null && cursorIndex == 0 && iteratorPriority != -1)) {
				priorities[n.priority].start = n;
			}
			
			priorities[n.priority].size++;
			size++;
			cursorIndex++;
			movedForward = true;
			// Set just viewed to be the element we just added
			justViewed = n;
			cursor = justViewed.next;
		}
	}
	
	/**
	 * Adds the given item at the end of the sublist with the given priority.
	 * 
	 * @param priority
	 * The priority level to add the given element to the end of
	 * @param item
	 * The item to add at the end of the given priority level
	 */
	@Override
	public void addWithPriority(int priority, E item) {
		if (priority < 0 || priority >= priorities.length) {
			throw new IllegalArgumentException();
		}
		
		// We need to find the closest priority level prior to the given
		// one that has a starting node so we can start iterating from there. 
		// If the given priority already has a starting node, we continue on
		int tempPriority = priority;
		while (tempPriority > 0 && priorities[tempPriority].start == null) {
			tempPriority--;
		}

		// Set the current node to the starting node of the previously found priority
		// If the starting priority is 0, we start at the head
		Node current = (tempPriority == 0) ? head : priorities[tempPriority].start;
		int i = 1;
		
		// If the priority is 0, we want to check to see if we currently have a starting
		// node for that priority level. If so, we want to move to it (if this wasn't done,
		// the head node would be counted, throwing it off
		if (tempPriority == 0 && priorities[tempPriority].size > 0) {
			current = current.next;
		}
		// Find the last element in the current priority
		while (i < priorities[tempPriority].size && current.next != null) {
			current = current.next;
			i++;
		}
		
		// Set the current (last element)'s next value to a new node
		current.next = new Node(item, current.next, current, priority);
		
		// If the node following the one just added had a next value, we
		// need to set that next's previous value to the node just created
		if (current.next.next != null) {
			current.next.next.previous = current.next;
		} else {
			tail = current.next;
		}
		
		// If this is the first element in the given priority, set the new node as the starting value
		if (priorities[priority].size == 0) {
			priorities[priority].start = current.next;
		}
		
		priorities[priority].size++;
		size++;
	}

	/**
	 * Removes the first element in the sublist with the given priority
	 * 
	 * @param priority
	 * The priority of the sublist to remove the first element from
	 * @return the element removed from the list
	 * @throws IllegalArgumentException if the priority is less than zero or greater than
	 * the maximum priority
	 */
	@Override
	public E removeWithPriority(int priority) {
		if (priority < 0 || priority >= priorities.length) {
			throw new IllegalArgumentException();
		}
		
		if (priorities[priority].size == 0) {
			throw new NoSuchElementException();
		}
		
		// Set the starting node for the given priority
		Node temp = priorities[priority].start;
		priorities[priority].start = temp.next;
		if (temp.next != null) {
			temp.next.previous = temp.previous;
		}
		temp.previous.next = temp.next;
		priorities[priority].size--;
		
		return temp.data;
	}

	/**
	 * Returns the number of elements in the sublist for the given priority.
	 * 
	 * @param priority 
	 * The priority of the sublist
	 * @return number of elements in the sublist with the given priority
	 * @throws IllegalArgumentException if the priority is less than zero or greater than
	 * the maximum priority 
	 */
	@Override
	public int sizeWithPriority(int priority) {
		if (priority < 0 || priority >= priorities.length) {
			throw new IllegalArgumentException();
		}
		
		return priorities[priority].size;
	}

	/**
	 * Gets an iterator that will iterate over the elements with the given priority
	 * 
	 * @param priority
	 * The priority of the elements the iterator should iterate over
	 * @return an iterator that will iterate over only the elements with the given priority
	 * @throws IllegalArgumentException if the priority is less than zero or greater than
	 * the maximum priority 
	 */
	@Override
	public ListIterator<E> iteratorWithPriority(int priority) {
		if (priority < 0 || priority >= priorities.length) {
			throw new IllegalArgumentException();
		}
		
		return new PriorityListIterator(priority);
	}
	
	/**
	 * Gets the maximum priority of the list
	 * 
	 * @return the maximum priority of the list
	 */
	@Override
	public int getMaxPriority() {
		return priorities.length - 1;
	}

	/**
	 * Gets the size of the entire list
	 * 
	 * @return the size of the entire list
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Checks to see if the list is empty
	 * 
	 * @return true if size == 0, false otherwise
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Checks whether this list contains the given object.
	 * 
	 * @param o
	 * The object to check the list for
	 * @return true if the list contains the given element, false otherwise
	 */
	@Override
	public boolean contains(Object o) {
		Iterator<E> iter = iterator();
		
		while (iter.hasNext()) {
			E val = iter.next();
			if ((val == null && o == null) || (val != null && val.equals(o))) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Adds the element at the end of the list, with the same priority level as the
	 * last element currently in the list (0 if list is empty).
	 * 
	 * @param e
	 * The element to add to the list
	 */
	@Override
	public boolean add(E e) {
		add(size(), e);
		
		return true;
	}

	/**
	 * Adds the element at the given index, with the same priority level as the
	 * element currently at position index in the list (0 if list is empty).
	 * 
	 * @param index
	 * The index to add the element at
	 * @param element
	 * The element to add to the list
	 */
	@Override
	public void add(int index, E element) {
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException();
		}
		
		if (index == 0) {
			int priority = (size == 0) ? 0 : head.next.priority;
			if (head.next == null) {
				head.next = new Node(element, head.next, head, priority);
				tail = head.next;
			} else {
				head.next = new Node(element, head.next, head, priority);
			}
			priorities[priority].start = head.next;
			priorities[priority].size++;
		} else {
			int priorityTemp = 0;
			int count = 0;
			while (count + priorities[priorityTemp].size < index) {
				count += priorities[priorityTemp].size;
				priorityTemp++;
			}
			Node current = priorities[priorityTemp].start;
			for (int i = count + 1; i < index; i++) {
				current = current.next;
			}
			int priority = (current.next == null) ? current.priority : current.next.priority;
			current.next = new Node(element, current.next, current, priority);
			
			if (index == size()) {
				tail = current.next;
			}
			
			if (current.next.next != null && current.next.next.equals(priorities[priority].start)) {
				priorities[priority].start = current.next;
			}
			priorities[priority].size++;
		}

		size++;
	}

	/**
	 * Adds the Collection of elements at the end of the list, with the same priority level as the
	 * last element currently in the list (0 if list is empty).
	 * 
	 * @param c
	 * The Collection of elements to add to the list
	 */
	@Override
	public boolean addAll(Collection<? extends E> c) {
		return addAll(size(), c);
	}

	/**
	 * Adds the Collection of elements at the given index, with the same priority level as the
	 * element currently at position index in the list (0 if list is empty).
	 * 
	 * @param index
	 * The index at which to start adding the collection
	 * @param c
	 * The Collection of elements to add to the list
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException();
		}
		
		int i = 0;
		for (E e : c) {
			add(index + i, e);
			i++;
		}
		
		return true;
	}

	/**
	 * Removes all elements from the list
	 */
	@Override
	public void clear() {
		//Remove references to start of priorities list
		for (int i = 0; i < priorities.length; i++) {
			priorities[i].size = 0;
			priorities[i].start = null;
		}
		
		//remove reference to head.next, therefore losing the rest of the list
		head.next = null;
		size = 0;
	}

	/**
	 * Gets the element at the given index.
	 * 
	 * @param index
	 * The index of the item to get
	 * @return The element at the given index
	 * @throws IndexOutOfBoundsException if index is less than zero 
	 * or greater than size()
	 */
	@Override
	public E get(int index) {
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException();
		}
		
		int priorityTemp = 0;
		int count = 0;
		while (count + priorities[priorityTemp].size <= index) {
			count += priorities[priorityTemp].size;
			priorityTemp++;
		}
		Node current = priorities[priorityTemp].start;
		for (int i = count; i < index; i++) {
			current = current.next;
		}
		return current.data;
	}

	/**
	 * Sets the element at the given index to the given element.
	 * 
	 * @param index
	 * The index of the element to replace
	 * @param element
	 * The element to replace the current element
	 * @return The element that was replaced
	 * @throws IndexOutOfBoundsException if the index is less than
	 * zero or greater than or equal to size()
	 */
	@Override
	public E set(int index, E element) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		E ret = null;
		
		if (index == 0) {
			ret = head.next.data;
			head.next.data = element;
		} else {
			int priorityTemp = 0;
			int count = 0;
			while (count + priorities[priorityTemp].size < index) {
				count += priorities[priorityTemp].size;
				priorityTemp++;
			}
			Node current = priorities[priorityTemp].start;
			for (int i = count + 1; i < index; i++) {
				current = current.next;
			}

			ret = current.next.data;
			current.next.data = element;
		}
		
		return ret;
	}
	
	/**
	 * Removes the first instance of the given object from the List.
	 * 
	 * @param o 
	 * The object to be removed from the list
	 * @return true if the element was found, false if it was not in the list
	 */
	@Override
	public boolean remove(Object o) {
		Node current = head.next;
		
		while (current != null) {
			if ((o == null && current.data == null) || (o != null && o.equals(current.data))) {
				int tempPriority = current.priority;
				
				if (current.next != null) {
					current.next.previous = current.previous;
				}
				current.previous.next = current.next;
				
				priorities[tempPriority].size--;
				size--;
				return true;
			}
			current = current.next;
		}
		
		return false;
	}
	
	/**
	 * Removes the element at the given index.
	 * 
	 * @param index
	 * The index of the element to remove
	 * @return the element that was just removed
	 * @throws IndexOutOfBoundsException if index is less than
	 * 0 or greater than or equal to size()
	 */
	@Override
	public E remove(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		
		int priorityTemp = 0;
		int count = 0;
		while (count + priorities[priorityTemp].size < index) {
			count += priorities[priorityTemp].size;
			priorityTemp++;
		}
		Node current = priorities[priorityTemp].start;
		for (int i = count + 1; i <= index; i++) {
			current = current.next;
		}
		
		if (current.next != null) {
			current.next.previous = current.previous;
		}
		current.previous.next = current.next;
		
		priorities[priorityTemp].size--;
		size--;
		
		return current.data;
	}

	/**
	 * Finds the index of the given Object
	 * 
	 * @param o the Object to try and locate in the PriorityList
	 * @return the index of the Object o if it exists. -1 if it is not
	 * in the list
	 */
	@Override
	public int indexOf(Object o) {
		int index = 0;
		
		for (int i = 0; i < size; i++) {
			if ((o == null && get(i) == null) || (o != null && o.equals(get(i)))) {
				return index;
			}
			index++;
		}
		
		return -1;
	}

	/**
	 * Gets an iterator that will iterate over the entire list.
	 * 
	 * @return an iterator that can iterate over the entire list
	 */
	@Override
	public Iterator<E> iterator() {
		return new PriorityListIterator();
	}
	
	/**
	 * Gets a ListIterator that will iterate over the entire list.
	 * 
	 * @return a ListIterator that will iterate over the entire list
	 */
	@Override
	public ListIterator<E> listIterator() {
		return new PriorityListIterator();
	}

	/**
	 * Gets a ListIterator set so that the next call
	 * to next() will give the element at position index.
	 * 
	 * @param index
	 * The index of the element that will be returned by the
	 * first call to next() in the ListIterator
	 * @return a ListIterator that can iterate over the entire list. The first
	 * call to next on this ListIterator will return the element at position index.
	 * @throws IndexOutOfBoundsException if index is less than
	 * 0 or greater than or equal to size()
	 */
	@Override
	public ListIterator<E> listIterator(int index) {
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException();
		}
		
		PriorityListIterator li = new PriorityListIterator();
		for (int i = 0; i < index; i++) {
			li.next();
		}
		
		// Should not be able to call remove right after they get the ListIterator
		li.setCanRemove(false);
		
		return li;
	}

	/**
	 * This operation is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * This operation is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * This operation is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * This operation is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * This operation is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * This operation is not supported.
	 * 
	 * @throws UnsupportedOperationException
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}
}
