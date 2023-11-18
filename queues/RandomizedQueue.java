/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int size;

    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        items[size++] = item;
        if (size == items.length) {
            resize(items.length * 2);
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int delIndex = StdRandom.uniformInt(size);
        Item delItem = items[delIndex];
        items[delIndex] = items[--size];
        items[size] = null;
        if (size > 0 && size < items.length / 4) {
            resize(items.length / 4);
        }
        return delItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int sampleIndex = StdRandom.uniformInt(size);
        return items[sampleIndex];
    }

    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private void resize(int length) {
        Item[] newItems = (Item[]) new Object[length];
        for (int i = 0; i < size; i++) {
            newItems[i] = items[i];
        }
        items = newItems;
    }

    private class RandomIterator implements Iterator<Item> {
        private int[] randomIndexes = StdRandom.permutation(size);
        private int pos = 0;

        @Override
        public boolean hasNext() {
            return pos != size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return items[randomIndexes[pos++]];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> test = new RandomizedQueue<>();
        StdOut.println(test.isEmpty());
        test.enqueue(0);
        test.enqueue(1);
        test.enqueue(2);
        test.enqueue(3);
        for (int i : test) {
            StdOut.print(i + " ");
        }
        StdOut.println();
        StdOut.println(test.dequeue());
        StdOut.println(test.dequeue());
        for (int i : test) {
            StdOut.print(i + " ");
        }
    }
}
