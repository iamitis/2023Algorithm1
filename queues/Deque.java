/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size;

    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node newNode = new Node(item, first, null);
        if (isEmpty()) {
            last = newNode;
            first = newNode;
        }
        else {
            first.pre = newNode;
            first = newNode;
        }
        size += 1;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node newNode = new Node(item, null, last);
        if (isEmpty()) {
            first = newNode;
            last = newNode;
        }
        else {
            last.next = newNode;
            last = newNode;
        }
        size += 1;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = first.element;
        first = first.next;
        size -= 1;
        if (isEmpty()) {
            last = null;
        }
        else {
            first.pre = null;
        }
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = last.element;
        last = last.pre;
        size -= 1;
        if (isEmpty()) {
            first = null;
        }
        else {
            last.next = null;
        }
        return item;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class Node {
        Item element;
        Node next;
        Node pre;

        Node(Item element, Node next, Node pre) {
            this.element = element;
            this.next = next;
            this.pre = pre;
        }
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.element;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        Deque<Integer> test = new Deque<>();
        StdOut.println(test.isEmpty());
        test.addFirst(0);
        test.addFirst(1);
        test.addLast(2);
        test.addLast(3);
        for (int i : test) {
            StdOut.print(i + " ");
        }
        StdOut.println();
        StdOut.println(test.removeFirst());
        StdOut.println(test.removeLast());
        for (int i : test) {
            StdOut.print(i + " ");
        }
    }
}