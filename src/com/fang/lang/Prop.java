package com.fang.lang;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Pattern;

import com.fang.U;

public class Prop {
	private Properties prop = null;
	public Prop(){}
	public Prop(Properties prop){
		this.prop = prop;
	}
	public void setProp(Properties prop) {
		this.prop = prop;
	}
	public  Properties getProp() {
		return prop;
	}

	/**
	 * 加载配置文件
	 * @param is
	 * @param charset 字符集名称
	 * @return
	 * @throws IOException
	 */
	public static Prop load(InputStream is,String charset) throws IOException {
		try {
			Properties prop = new Properties();
			prop.load(new InputStreamReader(is, charset == null ? "utf-8" : charset));
			return new Prop(prop);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {}
			}
		}
	}

	public static Prop load(java.io.File f,String charset) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			return load(is,charset);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 当使用相对路径时，默认为 类加载路径 + path，且相对路径第一个字符不能为 /
	 * 
	 * @param path
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	public static Prop load(String path,String charset) throws IOException  {
		boolean abs = false;
		// 判断path是否绝对路径
		if (U.IS_WINDOWS) {// windows
			String regex = "^[A-Za-z]{1,2}:";
			abs = Pattern.matches(regex, path);
		} else {// linux
			abs = path.trim().startsWith("/");
		}
		java.io.File f;
		if (!abs) {// 不是绝对路径的情况，相对类加载器的路径
			f = Files.file(U.getRealPath(path));
			// f = new java.io.File(U.getClassPath(),path);
		} else {
			f = Files.file(path);
		}
		return load(f,charset);
	}

	public  Boolean getBoolean(String key) {
		String v = getString(key);
		if (v != null) {
			return v.trim().equalsIgnoreCase("true");
		}
		return null;
	}

	public  Double getDouble(String key) {
		String v = getString(key);
		if (v != null) {
			return Double.valueOf(v);
		}
		return null;
	}

	public  Long getLong(String key) {
		String v = getString(key);
		if (v != null) {
			return Long.valueOf(v);
		}
		return null;
	}

	public  Integer getInt(String key) {
		String v = getString(key);
		if (v != null) {
			return Integer.valueOf(v);
		}
		return null;
	}

	public  Float getFloat(String key) {
		String v = getString(key);
		if (v != null) {
			return Float.valueOf(v);
		}
		return null;
	}

	public  String getString(String key) {
		if (prop == null)
			throw new RuntimeException("配置文件加载为null");
		return prop.getProperty(key);
	}

	/**
	 * 
	 * @param key
	 * @param defaultValue
	 *            为空时的默认值，不能给null值
	 * @return Integer Long Double Float String Boolean
	 */
	@SuppressWarnings("unchecked")
	public  <T> T getValue(String key, T defaultValue) {
		String v = getString(key);
		if (v != null && defaultValue != null) {
			if (defaultValue instanceof Integer) {
				return (T) Integer.valueOf(v);
			} else if (defaultValue instanceof Long) {
				return (T) Long.valueOf(v);
			} else if (defaultValue instanceof Double) {
				return (T) Double.valueOf(v);
			} else if (defaultValue instanceof Float) {
				return (T) Float.valueOf(v);
			} else if (defaultValue instanceof Boolean) {
				return (T) Boolean.valueOf(v.trim().equalsIgnoreCase("true"));
			} else if (defaultValue instanceof String) {
				return (T) v;
			}
		}
		return defaultValue;
	}
}