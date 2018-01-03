package com.fang.collection;

import java.util.Collection;
import java.util.HashSet;

public class UHashSet<E> extends HashSet<E>{

	public UHashSet(){}
	
	public static <E> UHashSet<E> create(){
		return new UHashSet<E>();
	}
	
	public UHashSet<E> set(E e){
		super.add(e);
		return this;
	}
	
	public UHashSet<E> setAll(Collection<E> c){
		super.addAll(c);
		return this;
	}

	public UHashSet<E> delete(E e){
		super.remove(e);
		return this;
	}
	
	public UHashSet<E> delete(int index){
		super.remove(index);
		return this;
	}


}
