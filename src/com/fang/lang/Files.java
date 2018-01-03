package com.fang.lang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.fang.U;
import com.fang.U.Except;

public class Files {

	public static java.io.File file(String path) {
		return new java.io.File(path);
	}

	/**
	 * 读取文件内容到list集合
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 * @throws IOException
	 */
	public static List<String> read(String filePath) throws IOException {
		return read(file(filePath));
	}

	/**
	 * 读取文件内容到list集合
	 * 
	 * @param filePath
	 *            文件路径
	 * @param charsetName
	 *            文件编码
	 * @return
	 * @throws IOException
	 */
	public static List<String> read(String filePath, String charsetName) throws IOException {
		return read(file(filePath), charsetName);
	}

	/**
	 * 读取文本文件内容到list集合，默认字符集 utf-8
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static List<String> read(java.io.File file) throws IOException {
		return read(file, "utf-8");
	}

	/**
	 * 读取文本文件内容到list集合
	 * 
	 * @param file
	 *            文本文件
	 * @param charsetName
	 *            字符集
	 * @return
	 * @throws IOException
	 */
	public static List<String> read(java.io.File file, String charsetName) throws IOException {
		List<String> result = U.newArrayList();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
			String temp = null;
			while ((temp = br.readLine()) != null) {
				result.add(temp);
			}
		} finally {
			if (br != null)
				br.close();
		}
		return result;
	}

	/**
	 * 读取字符文件的所有内容
	 * 
	 * @param file
	 * @param charsetName
	 * @return
	 * @throws IOException
	 */
	public static StringBuilder readAll(java.io.File file, String charsetName) throws IOException {
		StringBuilder sb = new StringBuilder();
		List<String> list = read(file, charsetName);
		for (String tem : list) {
			sb.append(tem);
			sb.append("\r\n");
		}
		return sb;
	}

	/**
	 * 读取字符文件的所有内容
	 * 
	 * @param file
	 * @param charsetName
	 * @return
	 * @throws IOException
	 */
	public static StringBuilder readAll(String filePath, String charsetName) throws IOException {
		StringBuilder sb = new StringBuilder();
		List<String> list = read(filePath, charsetName);
		for (String tem : list) {
			sb.append(tem);
			sb.append("\r\n");
		}
		return sb;
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 */
	public static void delete(String path) {
		java.io.File f = file(path);
		delete(f);
	}

	/**
	 * 删除多个文件
	 * 
	 * @param files
	 */
	public static void delete(String... files) {
		if (U.isEmpty(files))
			return;
		for (int i = 0; i < files.length; i++) {
			delete(files[i]);
		}
	}

	/**
	 * 删除多个文件
	 * 
	 * @param files
	 */
	public static void delete(java.io.File... files) {
		if (U.isEmpty(files))
			return;
		for (int i = 0; i < files.length; i++) {
			delete(files[i]);
		}
	}

	/**
	 * 递归删除文件、文件夹
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void delete(java.io.File f) {
		if (f.isDirectory()) {// 文件夹
			java.io.File[] fs = f.listFiles();
			for (java.io.File tem : fs) {
				delete(tem);
			}
		}
		f.delete();
	}

	/**
	 * 
	 * @param f
	 * @param filter
	 * @param recur
	 *            是否递归 eg: U.Files.delete(new
	 *            java.io.File("d:/where"),Files.endWithFilter(".svn"),true);
	 */
	public static void delete(java.io.File f, FileFilter filter, boolean recur) {
		if (f.isDirectory()) {
			java.io.File[] listFiles = f.listFiles(filter);
			delete(listFiles);
			if (recur) {// 递归删除
				listFiles = f.listFiles();
				for (java.io.File file : listFiles) {
					delete(file, filter, recur);
				}
			}
		}
	}

	/**
	 * 
	 * @param path
	 *            文件路径
	 * @param filter
	 *            过滤器
	 * @param recur
	 *            是否递归删除子文件夹中的文件 eg:
	 *            U.Files.delete("d:/where",Files.endWithFilter(".svn"),true);
	 */
	public static void delete(String path, FileFilter filter, boolean recur) {
		java.io.File f = file(path);
		delete(f, filter, recur);
	}

	/**
	 * 以 xxx 开头的文件或文件夹过滤
	 * 
	 * @param prefix
	 * @return FileFilter
	 */
	public static FileFilter startWithFilter(final String prefix) {
		return new FileFilter() {
			@Override
			public boolean accept(java.io.File f) {
				return f != null && f.getName().startsWith(prefix);
			}
		};
	}

	/**
	 * 以 xxx 结尾的文件或文件夹过滤
	 * 
	 * @param suffix
	 * @return FileFilter
	 */
	public static FileFilter endWithFilter(final String suffix) {
		return new FileFilter() {
			@Override
			public boolean accept(java.io.File f) {
				return f != null && f.getName().endsWith(suffix);
			}
		};
	}

	/**
	 * 
	 * @param regex
	 * @return
	 */
	public static FileFilter regexFilter(final String regex) {
		return new FileFilter() {
			@Override
			public boolean accept(java.io.File pathname) {
				return Pattern.matches(regex, pathname.getName());
			}
		};

	}

	/**
	 * 创建新文件
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static boolean createNewFile(String path) throws IOException {
		java.io.File f = file(path);
		return createNewFile(f);
	}

	/**
	 * 创建新文件
	 * 
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static boolean createNewFile(java.io.File f) throws IOException {
		if (f.exists()) {
			return true;
		}
		if (f.getParentFile().exists()) {
			return f.createNewFile();
		} else {
			return f.getParentFile().mkdirs() && f.createNewFile();
		}
	}

	/**
	 * 流的拷贝
	 * 
	 * @param src
	 *            源文件
	 * @param dest
	 *            目标文件
	 * @throws IOException
	 */
	public static <S, D> void copy(S src, D dest) throws IOException {
		if (src == null) {
			throw Except.newException("源文件不能为空！");
		}
		if (dest == null) {
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
		} finally {
			if (os != null)
				os.close();
			if (is != null)
				is.close();
		}

	}

	/**
	 * 将内容写入文件，utf-8编码
	 * 
	 * @param filePath
	 * @param content
	 * @throws Exception
	 */
	public static <T> void write(String filePath, T content, boolean append) throws Exception {
		write(file(filePath), content, "utf-8", append);
	}

	/**
	 * 将内容写入文件
	 * 
	 * @param file
	 * @param charset
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static <T> void write(java.io.File file, T content, String charset, boolean append) throws Exception {
		BufferedWriter bw = null;
		try {
			createNewFile(file);
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charset));
			if (content instanceof CharSequence) {
				bw.write(content.toString());
			} else if (content instanceof Iterable) {
				for (Iterator iterator = ((Iterable) content).iterator(); iterator.hasNext();) {
					Object o = iterator.next();
					if (o != null) {
						bw.write(o.toString());
					}
					bw.newLine();
				}
			} else if (content instanceof Map) {
				Map m = (Map) content;
				Set keySet = m.keySet();
				Object o = null;
				for (Object tem : keySet) {
					o = m.get(tem);
					bw.write(tem.toString());
					bw.write("=");
					if (o != null) {
						bw.write(o.toString());
					}
					bw.newLine();
				}
			} else if (content.getClass().isArray()) {
				int len = Array.getLength(content);
				for (int i = 0; i < len; i++) {
					Object obj = Array.get(content, i);
					if (obj != null) {
						bw.write(obj.toString());
						bw.newLine();
					}
				}
			} else {
				if (content != null) {
					bw.write(content.toString());
				}
			}
			bw.flush();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {}
			}
		}

	}

	/**
	 * 截取文件路径中的文件名
	 * 
	 * @param path
	 * @return
	 */
	public static String subFileName(String path) {
		int i = (path.lastIndexOf("/") > path.lastIndexOf("\\")) ? path.lastIndexOf("/") : path.lastIndexOf("\\");
		return i > 0 ? path.substring(i + 1) : null;
	}

	/**
	 * 截取文件路径中的文件路径
	 * 
	 * @param path
	 * @return
	 */
	public static String subFilePath(String path) {
		int i = (path.lastIndexOf("/") > path.lastIndexOf("\\")) ? path.lastIndexOf("/") : path.lastIndexOf("\\");
		return i > 0 ? path.substring(0, i + 1) : path;
	}

	/**
	 * 格式化文件路径
	 * 
	 * @param path
	 * @return
	 */
	public static String formatFilePath(String path) {
		return file(path).getAbsolutePath();
	}
}