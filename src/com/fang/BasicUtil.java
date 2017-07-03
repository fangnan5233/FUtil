package com.fang;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BasicUtil {

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
		Pattern p = Pattern.compile("(\\.\\./)|(\\.\\.\\\\)");
		if(path.startsWith("..")){
			Matcher matcher = p.matcher(path);
			int count = 0;
			int end = -1;
			while(matcher.find()){
				end = matcher.end();
				count ++;
			}
			URL resource = BasicUtil.class.getClassLoader().getResource("");
			File f = new File(resource.getPath());
			for(int i = 0;i< count;i++){
				f = f.getParentFile();
			}
			return f.getAbsolutePath() +File.separator+ path.substring(end);
		}
		URL resource = BasicUtil.class.getClassLoader().getResource(path);
		path = resource.getPath();
		// 如果是windows操作系统截取第一个/
		return IS_WINDOWS ? path.substring(1) : path;
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
	
}
