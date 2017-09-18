package com.fang.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class ULinkedList<E> extends LinkedList<E> {

	public ULinkedList(){
	}
	
	public static <E> ULinkedList<E> create(){
		return new ULinkedList<E>();
	}
	
	public ULinkedList<E> set(E e){
		super.add(e);
		return this;
	}
	
	public ULinkedList<E> set(Collection<E> c){
		super.addAll(c);
		return this;
	}

	public ULinkedList<E> delete(E e){
		super.remove(e);
		return this;
	}
	
	public ULinkedList<E> delete(int index){
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
