package com.fang.lang;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
@SuppressWarnings({"rawtypes","unchecked"})
public interface Getter {

	/**
	 * 
	 * @param o
	 * @return
	 */
	public <T> T getter(Object o);
	/**
	 * 
	 * @param o
	 * @param def 默认值
	 * @return
	 */
	public <T> T getter(Object o,T def);
	
	
	//java 1.8
	/*default <T> T getter(Object o) {
		if(this instanceof List){
			return (T)((List)this).get((int)o);
		}else if(this instanceof Map){
			return (T)((Map)this).get((int)o);
		}else{
			Class<? extends Getter> cls = this.getClass();
			try {
				Method method = cls.getMethod("get"+Strs.upperCaseFirstLetter((String)o));
				Object ret = method.invoke(this);
				return (T)ret;
			} catch (Exception e) {
				return null;
			}
		}
	}*/
	
}
