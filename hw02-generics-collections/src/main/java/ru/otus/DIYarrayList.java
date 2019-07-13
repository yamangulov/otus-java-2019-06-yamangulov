package ru.otus;

import java.util.*;

public class DIYarrayList<E> extends AbstractList<E> implements List<E> {

    private Object[] elementData;

    private static final int DEFAULT_CAPACITY = 10;

    private int pointer = 0;

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
    public void add(int index, E element) {
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
