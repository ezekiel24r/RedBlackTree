package edu.csupomona.cs.cs241.prog_assgmnt_2;

/**
 * Created by Eric on 5/24/2015.
 */

public interface Tree<K extends Comparable<K>, V> {

    //enumeration added for color
    public enum RB_Color{BLACK,RED};

    public void add(K key, V value);

    public V remove(K key);

    public V lookup(K key);

    public String toPrettyString();

}