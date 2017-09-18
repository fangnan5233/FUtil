package com.fang.collection;

import java.util.ArrayList;
import java.util.Collection;
/**
 * 继承自 ArrayList
 * @author fang
 *
 * @param <E>
 */
public class UArrayList<E> extends ArrayList<E> {

	public UArrayList(){
	}
	
	public static <E> UArrayList<E> create(){
		return new UArrayList<E>();
	}
	
	public UArrayList<E> set(E e){
		super.add(e);
		return this;
	}
	
	public UArrayList<E> set(Collection<E> c){
		super.addAll(c);
		return this;
	}

	public UArrayList<E> delete(E e){
		super.remove(e);
		return this;
	}
	
	public UArrayList<E> delete(int index){
		super.remove(index);
		return this;
	}
	
	public <E> E getAs(int index){
		return (E)this.get(index);
	}
	
	public String getStr(int index) {
		Object s = get(index);
		return s != null ? s.toString() : null;
	}
	
	public Integer getInt(int index) {
		Number n = (Number)get(index);
		return n != null ? n.intValue() : null;
	}
	
	public Long getLong(int index) {
		Number n = (Number)get(index);
		return n != null ? n.longValue() : null;
	}
	
	public Number getNumber(int index) {
		return (Number)get(index);
	}
	
	public Boolean getBoolean(int index) {
		return (Boolean)get(index);
	}
	
	
	public boolean notNull(int index) {
		return get(index) != null;
	}
	
	public boolean isNull(int index) {
		return get(index) == null;
	}
	
}
