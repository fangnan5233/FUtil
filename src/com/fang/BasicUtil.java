package com.fang;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;


import com.fang.collection.UArrayList;
import com.fang.collection.UHashMap;
import com.fang.collection.UHashSet;
import com.fang.collection.ULinkedHashMap;
import com.fang.collection.ULinkedList;
import com.fang.lang.Files;
import com.fang.lang.Strs;

abstract class BasicUtil {
	
	/**
	 * 打印到控制台
	 * 
	 * @param t
	 */
	public static <T> void print(Object... objs) {
		for (Object tem : objs) {
			System.out.print(tem);
		}
	}

	/**
	 * 打印到控制台
	 * 
	 * @param objs
	 */
	public static void println(Object... objs) {
		print(objs);
		System.out.println();
	}

	/**
	 * 判断是否为空
	 * 
	 * @param t
	 * @return t = null return true list : length = 0 true map : size = 0 true
	 *         array : lenth = 0 true string : 空串 true
	 */
	public static <T> boolean isEmpty(T t) {
		if (t == null)
			return true;
		if (t instanceof CharSequence) {// 对象是字符串
			return Strs.isBlank((CharSequence) t);
		} else if (t instanceof Collection) {// 对象是list or set
			return ((Collection<?>) t).isEmpty();
		} else if (t instanceof Map) {// 对象是map
			return ((Map<?, ?>) t).isEmpty();
		} else {
			if (t.getClass().isArray()) {// 对象是数组
				return !(Array.getLength(t) > 0);
			}
		}
		return false;
	}

	/**
	 * 判断是否不为空
	 * 
	 * @param t
	 * @return t = null return fals list : length = 0 false map : size = 0 false
	 *         array : lenth = 0 false string : 空串 false
	 */
	public static <T> boolean isNotEmpty(T t) {
		return !isEmpty(t);
	}

	/**
	 * 获取调用此方法的类名
	 * 
	 * @return String
	 */
	public static String getClsName() {
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		return stack[2].getClassName();
	}

	/**
	 * 获取调用此方法的类
	 * 
	 * @return Class
	 */
	public static Class<?> getCls() {
		StackTraceElement stack[] = Thread.currentThread().getStackTrace();
		String className = stack[2].getClassName();
		try {
			Class<?> cls = Class.forName(className);
			return cls;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 数组转成集合
	 * 
	 * @param arr
	 * @param cls
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static <T, E extends Collection<T>> E arrayToCollect(T arr[], Class<E> cls) throws InstantiationException, IllegalAccessException {
		E c = (E) cls.newInstance();
		for (T tem : arr) {
			c.add(tem);
		}
		return c;
	}

	/**
	 * 数组转成map
	 * 
	 * @param arr
	 * @param c
	 *            继承自map的实现类
	 * @param key
	 *            元素中的属性名，通过getter方法获取值
	 * @return
	 * @throws Exception
	 */
	public static <T, E extends Map> E arrayToMap(T arr[], Class<E> c, String key) throws Exception {
		E e = c.newInstance();
		T t = arr[0];
		Class<? extends Object> ct = t.getClass();
		key = Strs.upperCaseFirstLetter(key);
		Method method = ct.getMethod("get" + key);
		for (T tem : arr) {
			Object name = method.invoke(tem);
			e.put(name, tem);
		}
		return e;
	}

	/**
	 * Set,List 遍历操作
	 * 
	 * @param t
	 * @param each
	 *            eg: List<String> list = new ArrayList<String>();
	 *            list.add("t"); list.add("e"); list.add("s"); list.add("t");
	 *            U.forEach(list,new U.Each<String>(){ public void each(String
	 *            one,int index){ System.out.println(one); } });
	 * @throws Exception 
	 */
	public static <T> void forEach(Iterable<T> t, Each<T> each) throws Exception  {
		int index = 0;
		if(t == null){
			return;
		}
		for (Iterator<T> ite = t.iterator(); ite.hasNext();) {
			T one = ite.next();
			each.each(one, index);
			index++;
		}
	}

	/**
	 * map遍历
	 * 
	 * @param map
	 * @param each
	 * @throws Exception 
	 */
	public static <k, V> void forEach(Map<k, V> map, Each<Entry<k, V>> each) throws Exception {
		int index = 0;
		if(map == null){
			return;
		}
		for (Entry<k, V> tem : map.entrySet()) {
			each.each(tem, index);
			index++;
		}
	}

	/**
	 * 数组遍历
	 * 
	 * @param map
	 * @param each
	 * @throws Exception 
	 */
	public static <T> void forEach(T[] t, Each<T> each) throws Exception {
		int index = 0;
		if(t == null){
			return;
		}
		for (T tem : t) {
			each.each(tem, index);
			index++;
		}
	}

	public static abstract class Each<T> {
		public abstract void each(T one, int index) throws Exception;
	}
	
	public static final boolean IS_WINDOWS = System.getProperty("os.name")
			.toLowerCase().contains("windows");

	/**
	 * 获取类加载器的路径
	 * @return
	 */
	public static String getClassPath() {
		return getRealPath("");
	}

	/**
	 * 获取类加载器下的文件绝对路径
	 * @param path 可以使用 ../ | ..\ 上级目录
	 * @return
	 */
	public static String getRealPath(String path){
		URL url = Thread.currentThread().getContextClassLoader().getResource(U.isEmpty(path)?".":path);
		if(url == null){
			return null;
		}else{
			return IS_WINDOWS ? url.getPath().substring(1) : url.getPath();
		}
	}
	
	/**
	 * web项目路径
	 * @return
	 */
	public static String getWebPath(){
		String path = getClassPath();
		path = path.split("WEB-INF")[0];
		return path;
	}
	
	public static <K,V> HashMap<K,V> newHashMap(){
		return new HashMap<K,V>();
	}
	
	public static <K,V> LinkedHashMap<K, V> newLinkedHashMap(){
		return new LinkedHashMap<K, V>();
	}
	
	public static <E> ArrayList<E> newArrayList(){
		return new ArrayList<E>();
	}
	
	public static <E> LinkedList<E> newLinkedList(){
		return new LinkedList<E>();
	}
	
	public static <E> HashSet<E> newHashSet(){
		return new HashSet<E>();
	}
	
	public static <E> TreeSet<E> newTreeSet(){
		return new TreeSet<E>();
	}
	
	public static <K,V> UHashMap<K, V> newUHashMap(){
		return UHashMap.create();
	}
	
	public static <K,V> ULinkedHashMap<K, V> newULinkedHashMap(){
		return ULinkedHashMap.create();
	}
	
	public static <E> UHashSet<E> newUHashSet(){
		return UHashSet.create();
	}
	
	public static <E> UArrayList<E> newUArrayList(){
		return UArrayList.create();
	}
	
	public static <E> ULinkedList<E> newULinkedList(){
		return ULinkedList.create();
	}
	
	/**
	 * 生成随机数
	 * @param l
	 * @param g
	 * @return
	 */
	public static int random(int l,int g){
		int lt = l<g?l:g;//小
		g = (lt ==l)? g:l;//大
		return (int)(Math.random()* (g -lt)) + lt;
	}
	
	/**
	 * new File()
	 * @param path
	 * @return
	 */
	public static java.io.File file(String path) {
		return Files.file(path);
	}
}
