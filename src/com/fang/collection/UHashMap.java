package com.fang.collection;

import java.util.HashMap;
import java.util.Map;
/**
 * 继承自 HashMap
 * @author fang
 *
 * @param <K>
 * @param <V>
 */
public class UHashMap<K,V> extends HashMap<K,V> {

	public UHashMap(){
	}

	public  static <K,V> UHashMap<K,V> create(){
		return new UHashMap<K,V>();
	}
	
	public UHashMap<K,V> set(K key,V value){
		super.put(key, value);
		return (UHashMap<K, V>) this;
	}
	
	public UHashMap<K,V> setAll(Map<K,V> map){
		super.putAll(map);
		return this;
	}
	
	public UHashMap<K,V> delete(K key){
		super.remove(key);
		return this;
	}
	
	public <T> T getAs(Object key) {
		return (T)get(key);
	}
	
	public String getStr(Object key) {
		Object s = get(key);
		return s != null ? s.toString() : null;
	}
	
	public Integer getInt(Object key) {
		Number n = (Number)get(key);
		return n != null ? n.intValue() : null;
	}
	
	public Long getLong(Object key) {
		Number n = (Number)get(key);
		return n != null ? n.longValue() : null;
	}
	
	public Number getNumber(Object key) {
		return (Number)get(key);
	}
	
	public Boolean getBoolean(Object key) {
		return (Boolean)get(key);
	}
	
	/**
	 * key 存在，并且 value 不为 null
	 */
	public boolean notNull(Object key) {
		return get(key) != null;
	}
	
	/**
	 * key 不存在，或者 key 存在但 value 为null
	 */
	public boolean isNull(Object key) {
		return get(key) == null;
	}
	
}
