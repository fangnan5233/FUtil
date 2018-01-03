package com.fang.collection;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fang.lang.Getter;
/**
 * 继承自 LinkedHashMap
 * @author fang
 *
 * @param <K>
 * @param <V>
 */
public class ULinkedHashMap<K,V> extends LinkedHashMap<K,V> implements Getter{

	public ULinkedHashMap(){
	}

	public  static <K,V> ULinkedHashMap<K,V> create(){
		return new ULinkedHashMap<K,V>();
	}
	
	public ULinkedHashMap<K,V> set(K key,V value){
		super.put(key, value);
		return (ULinkedHashMap<K, V>) this;
	}
	
	public ULinkedHashMap<K,V> setAll(Map<K,V> map){
		super.putAll(map);
		return this;
	}
	
	public ULinkedHashMap<K,V> delete(K key){
		super.remove(key);
		return this;
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

	@Override
	public <T> T getter(Object o) {
		return (T)get(o);
	}
	
	@Override
	public <T> T getter(Object o, T def) {
		Object v = this.getter(o);
		return (T) (v==null ? def : v);
	}
	
}
