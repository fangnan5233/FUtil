package com.fang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Wrapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类大全：只有你想不到的！！
 * @author fang
 * @version 1.0
 */
public class U extends BasicUtil{
	
	/**
	 * 打印到控制台
	 * @param t
	 */
	public static <T> void print(Object...objs){
		for(Object tem : objs){
			System.out.print(tem);
		}
	}

	/**
	 * 打印到控制台
	 * @param objs
	 */
	public static void println(Object...objs){
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
			return Str.isBlank((CharSequence) t);
		} else if (t instanceof Collection) {// 对象是list or set
			return ((Collection<?>) t).isEmpty();
		} else if (t instanceof Map) {// 对象是map
			return ((Map<?,?>) t).isEmpty();
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
	 * @param arr
	 * @param cls
	 * @return
	 */
	public static <T,E extends Collection<T>> E arrayToCollect(T arr[],Class<E> cls ){
		try {
			E c = (E) cls.newInstance();
			for(T tem :arr){
				c.add(tem);
			}
			return c;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	/**
	 * 数组转成map
	 * @param arr
	 * @param c 继承自map的实现类
	 * @param key 元素中的属性名，通过getter方法获取值
	 * @return
	 * @throws Exception
	 */
	public static <T,E extends Map> E arrayToMap(T arr[],Class<E> c,String key) throws Exception {
		E e = c.newInstance();
		T t = arr[0];
		Class<? extends Object> ct = t.getClass();
		key = Str.upperCaseFirstLetter(key);
		Method method = ct.getMethod("get" + key);
		for(T tem : arr){
			Object name = method.invoke(tem);
			e.put(name, tem);
		}
		return e;
	}
	
	
	/**
	 * Set,List 遍历操作
	 * @param t
	 * @param each
	 * eg:
	 * 	List<String> list = new ArrayList<String>();
	 * 	list.add("t");
	 * 	list.add("e");
	 * 	list.add("s");
	 * 	list.add("t");
	 * 	U.forEach(list,new U.Each<String>(){
	 * 		public void each(String one,int index){
	 * 			System.out.println(one);
	 * 		}
	 * 	});
	 */
	public static <T> void forEach(Iterable<T> t,Each<T> each){
		int index = 0;
		for (Iterator<T> ite = t.iterator(); ite.hasNext();) {
			T one =  ite.next();
			each.each(one,index);
			index ++;
		}
	}
	
	/**
	 * map遍历
	 * @param map
	 * @param each
	 */
	public static <k,V> void forEach(Map<k,V> map,Each<Entry<k, V>> each){
		int index = 0;
		for(Entry<k, V> tem : map.entrySet()){
			each.each(tem,index);
			index++;
		}
	}
	
	/**
	 * 数组遍历
	 * @param map
	 * @param each
	 */
	public static <T> void forEach(T[] t,Each<T> each){
		int index = 0;
		for(T tem : t){
			each.each(tem,index);
			index++;
		}
	}
	
	public static abstract  class Each<T>{
		public abstract void each(T one,int index);
	}
	/**
	 * 字符串相关
	 */
	public static class Str {
		/**
		 * 验证是否为数字正则
		 */
		public static final Pattern NUMBER_PATTERN = Pattern
				.compile("^(-?\\d+)(\\.\\d+)?$");

		/**
		 * 判断整个字符串是否为数字
		 * 
		 * @param str
		 * @return
		 */
		public static boolean isDigit(CharSequence str) {
			Matcher matcher = NUMBER_PATTERN.matcher(str);
			return matcher.matches();
		}

		/**
		 * 判断字符串是否为空白串
		 * 
		 * @param str
		 * @return boolean 是空串返回true
		 */
		public static boolean isBlank(CharSequence str) {
			if (str == null) {// 空值
				return true;
			} else {
				int length = str.length();
				if (length == 0) {// 长度为0,
					return true;
				} else {// 长度不为0，判断每一个字符是否为""
					for (int i = 0; i < length; i++) {
						char c = str.charAt(i);
						if (c > 32) {// 包含非空字符返回true，空格
							// ascii值为32，小于32的字符全部为空白字符
							return false;
						}
					}
					return true;
				}
			}
		}

		/**
		 * 判断字符串是否为空白串
		 * 
		 * @param str
		 * @return boolean 不是空串返回true
		 */
		public static boolean isNotBlank(CharSequence str) {
			return !isBlank(str);
		}

		/**
		 * 所有字符串都为空
		 * @param str
		 * @return
		 */
		public static boolean isAllBlank(CharSequence ...str){
			for(CharSequence c :str){
				if(isNotBlank(c))//包含不为空的情况即返回false
					return false;
			}
			return true;
		}
		
		/**
		 * 所有字符串都不为空
		 * @param str
		 * @return
		 */
		public static boolean isAllNotBlank(CharSequence ...str){
			for(CharSequence c :str){
				if(isBlank(c))//包含为空的情况即返回false
					return false;
			}
			return true;
		}
		
		/**
		 * 将首字母大写
		 * @param str
		 * @return
		 */
		public static String upperCaseFirstLetter(String str) {  
		    char[] ch = str.toCharArray();  
		    if (ch[0] >= 'a' && ch[0] <= 'z') {  
		        ch[0] = (char) (ch[0] - 32);  
		    }  
		    return new String(ch);  
		} 
	}

	/**
	 * 日期相关
	 */
	public static class Date {
		public static final String COMMON_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
		public static final String COMMON_DATE_PATTERN = "yyyy-MM-dd";
		public static final String COMMON_TIME_PATTERN = "HH:mm:ss";
		private static final SimpleDateFormat COMMONFORMAT = new SimpleDateFormat(
				COMMON_DATETIME_PATTERN);

		/**
		 * 将date 格式化成字符串 
		 * pattern : yyyy-MM-dd HH:mm:ss
		 * @param date
		 * @return
		 */
		public static String toString(java.util.Date date) {
			if (date == null)
				return null;
			return COMMONFORMAT.format(date);
		}

		/**
		 * 将date类型格式化成字符串
		 * 
		 * @param date
		 * @param pattern
		 * @return
		 */
		public static String toString(java.util.Date date, String pattern) {
			if (date == null)
				return null;
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		}

		/**
		 * 将时间戳格式化
		 * @param s
		 * @return
		 */
		public static String toString(String s){
			if(s != null && Str.isDigit(s)){
				java.util.Date d = toDate(Long.valueOf(s));
				return toString(d);
			}
			return null;
		}
		/**
		 * 将时间戳格式化
		 * @param s
		 * @return
		 */
		public static String toString(String s,String pattern){
			if(s != null && Str.isDigit(s)){
				java.util.Date d = toDate(Long.valueOf(s));
				return toString(d,pattern);
			}
			return null;
		}
		
		/**
		 * 将格式化的时间 转换成另一格式
		 * eg:2012-12-12 yyyy-MM-dd yyyyMMdd
		 * 	  20121212
		 * @param src 
		 * @param srcPattern 
		 * @param destPattern 
		 * @return
		 */
		public static String transPattern(String src,String srcPattern,String destPattern){
			java.util.Date date = toDate(src, srcPattern);
			return toString(date, destPattern);
		}
		/**
		 * 将字符串转换成日期
		 * 
		 * @param str
		 * @param pattern
		 * @return
		 */
		public static java.util.Date toDate(String str, String pattern) {
			if (str == null)
				return null;
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			try {
				return sdf.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * 将字符串转换成日期<br/>
		 * pattern : yyyy-MM-dd HH:mm:ss
		 * @param str
		 */
		public static java.util.Date toDate(String str) {
			if (str == null)
				return null;
			try {
				return COMMONFORMAT.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		/**
		 * 时间戳转换成date
		 * @param l
		 * @return
		 */
		public static java.util.Date toDate(long l){
			return new java.util.Date(l);
		}
		
	}

	/**
	 * 文件相关
	 */
	public static class File {

		public static java.io.File file(String path){
			return new java.io.File(path);
		}
		
		/**
		 * 读取文件内容到list集合
		 * @param filePath 文件路径
		 * @return
		 * @throws IOException
		 */
		public static List<String> read(String filePath) throws IOException{
			return read(file(filePath));
		}
		
		/**
		 * 读取文件内容到list集合
		 * @param filePath 文件路径
		 * @param charsetName 文件编码
		 * @return
		 * @throws IOException
		 */
		public static List<String> read(String filePath,String charsetName) throws IOException{
			return read(file(filePath),charsetName);
		}
		
		/**
		 * 读取文本文件内容到list集合，默认字符集 utf-8
		 * @param file
		 * @return
		 * @throws IOException
		 */
		public static List<String> read(java.io.File file) throws IOException{
			return read(file, "utf-8");
		}
		
		
		/**
		 * 读取文本文件内容到list集合
		 * @param file 文本文件
		 * @param charsetName 字符集
		 * @return
		 * @throws IOException
		 */
		public static List<String> read(java.io.File file,String charsetName) throws IOException{
			List<String> result = newArrayList();
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file),charsetName));
				String temp = null;
				while((temp = br.readLine()) != null){
					result.add(temp);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}finally{
				if(br != null)
					br.close();
			}
			return result;
		}
		
		/**
		 * 读取字符文件的所有内容
		 * @param file
		 * @param charsetName
		 * @return
		 * @throws IOException
		 */
		public static StringBuilder readAll(java.io.File file,String charsetName) throws IOException{
			StringBuilder sb = new StringBuilder();
			List<String> list = read(file, charsetName);
			for(String tem : list){
				sb.append(tem);
				sb.append("\r\n");
			}
			return sb;
		}
		
		/**
		 * 读取字符文件的所有内容
		 * @param file
		 * @param charsetName
		 * @return
		 * @throws IOException
		 */
		public static StringBuilder readAll(String filePath,String charsetName) throws IOException{
			StringBuilder sb = new StringBuilder();
			List<String> list = read(filePath, charsetName);
			for(String tem : list){
				sb.append(tem);
				sb.append("\r\n");
			}
			return sb;
		}
		
		/**
		 * 删除文件
		 * @param path
		 */
		public static void delete(String path){
			java.io.File f = file(path);
			delete(f);
		}
		
		/**
		 * 删除多个文件
		 * @param files
		 */
		public static void delete(String ... files){
			if(isEmpty(files))
				return;
			for(int i = 0;i< files.length ;i++){
				delete(files[i]);
			}
		}
		
		/**
		 *  删除多个文件
		 * @param files
		 */
		public static void delete(java.io.File ... files){
			if(isEmpty(files))
				return;
			for(int i = 0;i< files.length ;i++){
				delete(files[i]);
			}
		}
		
		/**
		 * 递归删除文件、文件夹
		 * @param src
		 * @param dest
		 * @throws IOException
		 */
		public static void delete(java.io.File f){
			if(f.isDirectory()){//文件夹
				java.io.File[] fs = f.listFiles();
				for(java.io.File tem :fs){
					delete(tem);
				}
			}
			f.delete();
		}
		
		/**
		 * 
		 * @param f
		 * @param filter 
		 * @param recur 是否递归
		 * eg:
		 * 	U.File.delete(new java.io.File("d:/where"),File.endWithFilter(".svn"),true);
		 */
		public static void delete(java.io.File f,FileFilter filter,boolean recur){
			if(f.isDirectory()){
				java.io.File[] listFiles = f.listFiles(filter);
				delete(listFiles);
				if(recur){//递归删除
					listFiles = f.listFiles();
					for (java.io.File file : listFiles) {
						delete(file, filter, recur);
					}
				}
			}
		}
		
		/**
		 * 
		 * @param path 文件路径
		 * @param filter 过滤器
		 * @param recur 是否递归删除子文件夹中的文件
		 * eg:
		 * 	U.File.delete("d:/where",File.endWithFilter(".svn"),true);
		 */
		public static void delete(String path,FileFilter filter,boolean recur){
			java.io.File f = file(path);
			delete(f, filter, recur);
		}
		
		/**
		 * 以 xxx 开头的文件或文件夹过滤
		 * @param prefix
		 * @return FileFilter
		 */
		public static FileFilter startWithFilter(final String prefix){
			return new FileFilter() {
				@Override
				public boolean accept(java.io.File f) {
					return f != null && f.getName().startsWith(prefix);
				}
			};
		}
		
		/**
		 * 以 xxx 结尾的文件或文件夹过滤
		 * @param suffix
		 * @return FileFilter
		 */
		public static FileFilter endWithFilter(final String suffix){
			return new FileFilter() {
				@Override
				public boolean accept(java.io.File f) {
					return f!= null && f.getName().endsWith(suffix);
				}
			};
		}
		
		
		/**
		 * 
		 * @param regex
		 * @return
		 */
		public static FileFilter regexFilter(final String regex){
			return new FileFilter() {
				@Override
				public boolean accept(java.io.File pathname) {
					return Pattern.matches(regex, pathname.getName());
				}
			};
			
		}
		
		/**
		 * 创建新文件
		 * @param path
		 * @return
		 * @throws IOException
		 */
		public static boolean createNewFile(String path) throws IOException{
			java.io.File f = file(path);
			return createNewFile(f);
		}
		
		/**
		 * 创建新文件
		 * @param f
		 * @return
		 * @throws IOException
		 */
		public static boolean createNewFile(java.io.File f) throws IOException{
			if(f.exists()){
				return true;
			}
			if(f.getParentFile().exists()){
				return f.createNewFile();
			}else{
				return f.getParentFile().mkdirs() && f.createNewFile();
			}
		}
		
		/**
		 * 流的拷贝
		 * @param src 源文件
		 * @param dest 目标文件
		 * @throws IOException
		 */
		public static <S, D> void copy(S src, D dest) throws IOException {
			if(src == null){
				throw Except.newException("源文件不能为空！");
			}
			if(dest == null){
				throw Except.newException("目标文件不能为空！");
			}
			InputStream is = null;
			OutputStream os = null;
			try {
				// 1、打开源
				if (src instanceof InputStream) {
					is = (InputStream) src;
				} else if (src instanceof CharSequence) {
					is = new FileInputStream(src.toString());
				} else if (src instanceof java.io.File) {
					is = new FileInputStream((java.io.File) src);
				}

				if (dest instanceof OutputStream) {
					os = (OutputStream) dest;
				} else if (dest instanceof CharSequence) {
					createNewFile(dest.toString());
					os = new FileOutputStream(dest.toString());
				} else if (dest instanceof java.io.File) {
					createNewFile((java.io.File) dest);
					os = new FileOutputStream((java.io.File) dest);
				}

				byte b[] = new byte[1024];
				int len = -1;
				while ((len = is.read(b)) != -1) {
					os.write(b, 0, len);
				}
				os.flush();
			} catch (IOException e) {
				throw e;
			} finally {
				if(os != null)
					os.close();
				if(is != null)
					is.close();
			}
			
		}

		/**
		 * 将内容写入文件，utf-8编码
		 * @param filePath
		 * @param content
		 * @throws Exception
		 */
		public static <T> void write(String filePath ,T content,boolean append) throws Exception{
			write(file(filePath),content,"utf-8",append);
		}
		
		/**
		 * 将内容写入文件
		 * @param file
		 * @param charset
		 * @throws Exception 
		 */
		public static <T> void write(java.io.File file,T content,String charset,boolean append) throws Exception{
			BufferedWriter bw = null;
			try {
				createNewFile(file);
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,append),charset));
				if(content instanceof CharSequence){
					bw.write(content.toString());
				}else if(content instanceof Iterable){
					for (Iterator iterator = ((Iterable)content).iterator(); iterator.hasNext();) {
						Object o = iterator.next();
						if(o != null){
							bw.write(o.toString());
						}
						bw.newLine();
					}
				}else if(content instanceof Map){
					Map m = (Map)content;
					Set keySet = m.keySet();
					Object o = null;
					for(Object tem : keySet){
						o = m.get(tem);
						bw.write(tem.toString());
						bw.write("=");
						if(o != null){
							bw.write(o.toString());
						}
						bw.newLine();
					}
				}else if(content.getClass().isArray()){
					int len = Array.getLength(content);
					for(int i = 0;i< len;i++){
						Object obj = Array.get(content, i);
						if(obj != null){
							bw.write(obj.toString());
							bw.newLine();
						}
					}
				}else {
					if(content != null){
						bw.write(content.toString());
					}
				}
				bw.flush();
			} catch (Exception e) {
				throw e;
			}finally{
				if(bw != null){
					try {
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		
		/**
		 * 截取文件路径中的文件名
		 * @param path
		 * @return
		 */
		public static String subFileName(String path){
			int i = (path.lastIndexOf("/") > path.lastIndexOf("\\"))?  path.lastIndexOf("/"):path.lastIndexOf("\\");
			return i>0? path.substring(i+1) : null;
		}
		/**
		 * 截取文件路径中的文件路径
		 * @param path
		 * @return
		 */
		public static String subFilePath(String path){
			int i = (path.lastIndexOf("/") > path.lastIndexOf("\\"))?  path.lastIndexOf("/"):path.lastIndexOf("\\");
			return i>0? path.substring(0,i+1) : path;
		}
		
		/**
		 * 格式化文件路径
		 * @param path
		 * @return
		 */
		public static String formatFilePath(String path){
			return file(path).getAbsolutePath();
			//return path.replaceAll("[\\\\]+[/]+|[/]+[\\\\]+|[\\\\]{2,}|[/]{2}", java.io.File.separator);
		}
	}

	/**
	 * 配置文件相关
	 */
	public static class Prop {
		private static Properties prop = null;
		public static Properties getProp(){
			return prop;
		}
		/**
		 * 加载配置文件
		 * @param is
		 * @return
		 */
		public static boolean load(InputStream is){
			boolean r = false;
			try {
				prop = new Properties();
				prop.load(is);
				r = true;
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(is != null){
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return r;
		}
		
		public static boolean load(java.io.File f){
			boolean r = false;
			InputStream is = null;
			try {
				is = new FileInputStream(f);
				return load(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return r;
		}

		/**
		 * 当使用相对路径时，默认为   类加载路径  + path，且相对路径第一个字符不能为 /
		 * @param path
		 * @return
		 */
		public static boolean load(String path){
			boolean r = false;
			try {
				boolean abs = false;
				//判断path是否绝对路径
				if(U.IS_WINDOWS){//windows
					String regex = "^[A-Za-z]{1,2}:";
					abs = Pattern.matches(regex, path);
				}else{//linux
					abs = path.trim().startsWith("/");
				}
				java.io.File f ;
				if(!abs){//不是绝对路径的情况，相对类加载器的路径
					f = file(BasicUtil.getRealPath(path));
					//f = new java.io.File(U.getClassPath(),path);
				}else{
					f = file(path);
				}
				return load(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return r;
		}
		
		public static Boolean getBoolean(String key){
			String v = getString(key);
			if(v != null){
				return v.trim().equalsIgnoreCase("true");
			}
			return null;
		}
		
		public static Double getDouble(String key){
			String v = getString(key);
			if(v != null){
				return Double.valueOf(v);
			}
			return null;
		}
		
		public static Long getLong(String key){
			String v = getString(key);
			if(v != null){
				return Long.valueOf(v);
			}
			return null;
		}
		
		public static Integer getInt(String key){
			String v = getString(key);
			if(v != null){
				return Integer.valueOf(v);
			}
			return null;
		}
		
		public static Float getFloat(String key){
			String v = getString(key);
			if(v != null){
				return Float.valueOf(v);
			}
			return null;
		}
		
		public static String getString(String key){
			if(prop == null)
				throw new RuntimeException("配置文件加载为null");
			return prop.getProperty(key);
		}
		
		/**
		 * 
		 * @param key
		 * @param defaultValue 为空时的默认值，不能给null值
		 * @return Integer Long Double Float String Boolean
		 */
		public static <T> T getValue(String key, T defaultValue){
			String v = getString(key);
			if(v != null && defaultValue != null){
				if(defaultValue instanceof Integer){
					return (T)Integer.valueOf(v);
				}else if(defaultValue instanceof Long){
					return (T)Long.valueOf(v);
				}else if(defaultValue instanceof Double){
					return (T)Double.valueOf(v);
				}else if(defaultValue instanceof Float){
					return (T)Float.valueOf(v);
				}else if(defaultValue instanceof Boolean){
					return (T)Boolean.valueOf(v.trim().equalsIgnoreCase("true"));
				}else if(defaultValue instanceof String){
					return (T)v;
				}
			}
			return defaultValue;
		}
	}


	/**
	 * 数据库相关
	 */
	public static class Db{
		private static String userName;
		private static String pwd;
		private static String url;
		private static String driver;
		
		public final static List<String[]> JDBC_URL_DRIVER = newArrayList();
		static{
			//数据库名称，动程序类名，url匹配字段，url模板 
			JDBC_URL_DRIVER.add(new String[]{"oracle","oracle.jdbc.driver.OracleDriver","jdbc:oracle","jdbc:oracle:thin:@<host>:<port>:<SID>"});
			JDBC_URL_DRIVER.add(new String[]{"mysql","com.mysql.jdbc.Driver","jdbc:mysql","jdbc:mysql://<host>:<port>/<database_name>"});
			JDBC_URL_DRIVER.add(new String[]{"sqlserver2000","com.microsoft.jdbc.sqlserver.SQLServerDriver","jdbc:microsoft","jdbc:microsoft:sqlserver://<server_name>:<port>"});
			JDBC_URL_DRIVER.add(new String[]{"sqlserver2005","com.microsoft.sqlserver.jdbc.SQLServerDriver","jdbc:sqlserver","jdbc:sqlserver://<server_name>:<port>"});
		}
		public static String getDriverClassName(String url){
			url = url.trim();
			for(String[] tem :JDBC_URL_DRIVER){
				if(url.startsWith(tem[2]))
					return tem[1];
			}
			return null;
		}
		
		public static void config(String driver,String url,String userName,String pwd) throws ClassNotFoundException{
			Db.url = url;
			Db.userName = userName;
			Db.pwd = pwd;
			Db.driver = driver;
			Class.forName(driver);
		}
		
		/**
		 * 自动匹配对应的driver
		 * @param url 
		 * @param userName 数据库用户名
		 * @param pwd 用户密码
		 * @throws ClassNotFoundException
		 */
		public static void config(String url,String userName,String pwd) throws ClassNotFoundException{
			Db.url = url;
			Db.userName = userName;
			Db.pwd = pwd;
			Db.driver = getDriverClassName(url);
			Class.forName(driver);
		}
		
		/**
		 * 获取一个数据库连接
		 * @return
		 * @throws Exception
		 */
		public static Connection getConnection() throws SQLException{
			return DriverManager.getConnection(url, userName, pwd);
		}
		
		/**
		 * 关闭数据库连接等
		 * 
		 * @param t
		 *            继承自 AutoCloseable & Wrapper：ResultSet Connection Statement
		 * @throws Exception
		 */
		@SuppressWarnings("unchecked")
		public static <T extends AutoCloseable & Wrapper> void close(T... t)
				throws SQLException {
			if (t.length == 0)
				return;
			for (T tem : t) {
				if (tem != null) {
					try {
						tem.close();
					} catch (Exception e) {
						throw new SQLException(e);
					}
				}
			}
		}

		/**
		 * 关闭数据库连接等，不抛出异常
		 * 
		 * @param t
		 *            继承自 AutoCloseable & Wrapper：ResultSet Connection Statement
		 */
		@SuppressWarnings("unchecked")
		public static <T extends AutoCloseable & Wrapper> void closeQuiet(T... t) {
			if (t.length == 0)
				return;
			for (T tem : t) {
				if (tem != null) {
					try {
						tem.close();
					} catch (Exception e) { /*不抛出异常*/ }
				}
			}
		}
		
		/**
		 * insert update delete
		 * @param conn
		 * @param sql
		 * @param where
		 * @throws SQLException
		 */
		public static void excute(Connection conn ,String sql,Object ...where) throws SQLException{
			PreparedStatement stmt = null;
			try {
				conn.setAutoCommit(false);
				stmt = conn.prepareStatement(sql);
				if(where != null && where.length > 0){
					for(int i = 0;i< where.length ;i++){
						stmt.setObject(i+1, where[i]);
					}
				}
				stmt.executeUpdate();
				conn.commit();
			} catch (Exception e) {
				throw e;
			}finally{
				closeQuiet(stmt);
			}
		}
		
		/**
		 * 批处理操作 insert update delete
		 * @param conn
		 * @param sql
		 * @param params
		 * @throws SQLException
		 */
		public static void excuteBatch(Connection conn,String sql,Object[][] params) throws SQLException{
			PreparedStatement stmt = null;
			try {
				conn.setAutoCommit(false);
				stmt = conn.prepareStatement(sql);
				for(int i = 0;i<params.length;i++){
					for(int j = 0;j<params[i].length;j++){
						stmt.setObject(j+1, params[i][j]);
					}
					stmt.addBatch();
				}
				stmt.executeBatch();
				conn.commit();
			} catch (Exception e) {
				throw e;
			} finally {
				closeQuiet(stmt);
			}
		}
		
		/**
		 * select
		 * @param conn
		 * @param sql
		 * @param where
		 * @return List<Map<String,Object>>
		 * @throws SQLException
		 */
		public static List<Map<String,Object>> select(Connection conn,String sql,Object ... where) throws SQLException{
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				stmt = conn.prepareStatement(sql);
				if(isNotEmpty(where)){
					for(int i = 0;i< where.length ;i++){
						stmt.setObject(i+1, where[i]);
					}
				}
				rs = stmt.executeQuery();
				ResultSetMetaData md = rs.getMetaData();
				int columnCount = md.getColumnCount();
				String columnName[] = new String[columnCount];
				for(int i = 0;i< columnCount;i++){
					columnName[i] = md.getColumnLabel(i+1);
				}
				List<Map<String,Object>> result = newArrayList();
				while(rs.next()){
					Map<String,Object> record = newLinkedHashMap();
					for(int i = 0;i< columnCount;i++){
						record.put(columnName[i], rs.getObject(i+1));
					}
					result.add(record);
				}
				return result;
			} catch (Exception e) {
				throw e;
			}finally{
				closeQuiet(stmt, rs);
			}
		}
	
		/**
		 * select查询
		 * @param sql
		 * @param params
		 * @return
		 * @throws SQLException
		 */
		public static List<Map<String,Object>> select(String sql,Object...params) throws SQLException{
			Connection conn = getConnection();
			return select(conn,sql, params);
		}
	}

	
	
	public static class Except{
		
		public static RuntimeException newException(String message,Throwable cause){
			return new RuntimeException(message, cause);
		}
		
		public static RuntimeException newException(String message){
			return new RuntimeException(message);
		}
		
		public static RuntimeException newException(Throwable cause){
			return new RuntimeException(cause);
		}
		
		public static <T extends Throwable> T newException(String message,Class<T> cls){
			try {
				Constructor<T> c = cls.getConstructor(String.class);
				return c.newInstance(message);
			} catch (Exception e) {
				throw new RuntimeException(message);
			}
		}
		
		public static <T extends Throwable> T newException(Throwable cause,Class<T> cls){
			try {
				Constructor<T> c = cls.getConstructor(Throwable.class);
				return c.newInstance(cause);
			} catch (Exception e) {
				throw new RuntimeException(cause);
			}
		}
		
		public static <T extends Throwable> T newException(String message,Throwable cause,Class<T> cls){
			try {
				Constructor<T> c = cls.getConstructor(String.class,Throwable.class);
				return c.newInstance(message,cause);
			} catch (Exception e) {
				throw new RuntimeException(message,cause);
			}
		}
	}
	
	
	/**
	 * new File()
	 * @param path
	 * @return
	 */
	public static java.io.File file(String path){
		return File.file(path);
	}

}
