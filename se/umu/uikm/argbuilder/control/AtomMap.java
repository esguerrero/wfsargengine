/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.umu.uikm.argbuilder.control;

/**
 *
 * @author Esteban
 */

public class AtomMap<K, V> {
  private final K key;
  private V value;

  public AtomMap(K key, V value) {
    this.key = key;
    this.value = value;
  }

  public K getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }

  public void setValue(V value) {
    this.value = value;
  }
} 