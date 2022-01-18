/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package theopenhand.commons;

import java.util.Objects;

/**
 *
 * @author gabri
 * @param <K>
 * @param <V>
 */
public class Pair<K, V> {

    private final K key;
    private final V value;

    /**
     *
     * @param k
     * @param v
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     *
     * @return
     */
    public K getKey() {
        return key;
    }

    /**
     *
     * @return
     */
    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            Pair p = (Pair) obj;
            Object k = p.key;
            Object v = p.value;
            if (k.getClass().equals(key.getClass()) && v.getClass().equals(value.getClass())) {
                return key.equals(k) && value.equals(v);
            }
        }
        return false; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.key);
        hash = 71 * hash + Objects.hashCode(this.value);
        return hash;
    }

}
