package com.myweddi.utils;

public class Duo<T, V> {

    private T key;
    private V val;

    public Duo(T key, V val) {
        this.key = key;
        this.val = val;
    }

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public V getVal() {
        return val;
    }

    public void setVal(V val) {
        this.val = val;
    }
}
