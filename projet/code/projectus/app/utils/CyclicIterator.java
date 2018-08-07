package utils;


import java.util.ArrayList;
import java.util.Collection;

public class CyclicIterator<T> extends ArrayList<T> {
    private int pointer;

    public CyclicIterator(int initialCapacity) {
        super(initialCapacity);
        this.pointer = size()-1;
    }
    public CyclicIterator() {
        super();
        this.pointer = size()-1;
    }
    public CyclicIterator(Collection<? extends T> c) {
        super(c);
        this.pointer = size()-1;
    }

    /** give next element in the list.
     * if the end of the list is reached, gives the first element
     */
    public T next() {
        if (size() == 0) {
            return null;
        } else {
            if (pointer < size() - 1) {
                pointer++;
            } else {
                pointer = 0;
            }
            return this.get(pointer);
        }
    }
}
