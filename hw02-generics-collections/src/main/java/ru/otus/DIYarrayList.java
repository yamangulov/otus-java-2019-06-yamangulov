package ru.otus;

import java.util.*;

public class DIYarrayList<E> implements List<E> {

    private Object[] elementData;

    private static final int DEFAULT_CAPACITY = 10;

    private int pointer = 0;

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        Iterator<E> newIterator = new Iterator<E>() {

            int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < pointer && elementData[currentIndex] != null;
            }

            @Override
            public E next() {
                return (E) elementData[currentIndex++];
            }
        };

        return newIterator;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elementData, pointer);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    public DIYarrayList() {
        this.elementData = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public boolean add(E element) {
        resizeElementData(pointer);
        elementData[pointer++] = element;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        ListIterator<E> newListIterator = new ListIterator<E>() {

            int currentIndex = 0;
            int lastRet = -1;

            @Override
            public boolean hasNext() {
                return currentIndex < pointer && elementData[currentIndex] != null;
            }

            @Override
            public E next() {
                return (E) elementData[lastRet = currentIndex++];
            }

            @Override
            public boolean hasPrevious() {
                return currentIndex > 0 && elementData[currentIndex] != null;
            }

            @Override
            public E previous() {
                return (E) elementData[lastRet = currentIndex--];
            }

            @Override
            public int nextIndex() {
                return currentIndex;
            }

            @Override
            public int previousIndex() {
                return currentIndex - 1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(E e) {
                if (lastRet < 0)
                    throw new IllegalStateException();

                try {
                    DIYarrayList.this.set(lastRet, e);
                } catch (IndexOutOfBoundsException ex) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public void add(E e) {
                throw new UnsupportedOperationException();
            }
        };

        return newListIterator;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }


    @Override
    public E get(int index) {
        return (E) elementData[index];
    }

    @Override
    public E set(int index, E element) {
        E oldValue = (E) elementData[index];
        elementData[index] = element;
        return oldValue;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int inputCollectionLength = a.length;
        if (inputCollectionLength == 0) return false;
        for (Object element : a) {
            resizeElementData(pointer);
            elementData[pointer++] = element;
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return this.pointer;
    }

    private void resizeElementData(int pointer) {
        if(pointer == elementData.length - 1) {
            Object[] newElementData = new Object[elementData.length * 2];
            System.arraycopy(elementData, 0, newElementData, 0, pointer);
            elementData = newElementData;
        }
    }

}
