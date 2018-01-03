package com.fang.lang;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Wrapper;
import java.util.List;
import java.util.Map;

import com.fang.U;



public class Db {
	/**
	 * 数据源接口
	 * @author fang
	 *
	 */
	public static interface DataSource{
		Connection getConnection();
	}
	/**
	 * 数据源
	 */
	private static DataSource ds = null;
	private static String userName;
	private static String pwd;
	private static String url;
	private static String driver;
	
	public final static List<String[]> JDBC_URL_DRIVER = U.newArrayList();
	static {
		// 数据库名称，动程序类名，url匹配字段，url模板
		JDBC_URL_DRIVER.add(new String[] { "oracle", "oracle.jdbc.driver.OracleDriver", "jdbc:oracle", "jdbc:oracle:thin:@<host>:<port>:<SID>" });
		JDBC_URL_DRIVER.add(new String[] { "mysql", "com.mysql.jdbc.Driver", "jdbc:mysql", "jdbc:mysql://<host>:<port>/<database_name>" });
		JDBC_URL_DRIVER.add(new String[] { "sqlserver2000", "com.microsoft.jdbc.sqlserver.SQLServerDriver", "jdbc:microsoft",
				"jdbc:microsoft:sqlserver://<server_name>:<port>" });
		JDBC_URL_DRIVER.add(new String[] { "sqlserver2005", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver",
				"jdbc:sqlserver://<server_name>:<port>" });
	}

	public static String getDriverClassName(String url) {
		url = url.trim();
		for (String[] tem : JDBC_URL_DRIVER) {
			if (url.startsWith(tem[2]))
				return tem[1];
		}
		return null;
	}

	/**
	 * 配置数据库连接
	 * @param driver
	 * @param url
	 * @param userName
	 * @param pwd
	 * @throws ClassNotFoundException
	 */
	public static void config(String driver, String url, String userName, String pwd) throws ClassNotFoundException {
		Db.url = url;
		Db.userName = userName;
		Db.pwd = pwd;
		Db.driver = driver;
		Class.forName(driver);
	}

	/**
	 * 配置数据源
	 * @param ds
	 */
	public static void config(DataSource ds){
		Db.ds = ds;
	}
	/**
	 * 自动匹配对应的driver
	 * 
	 * @param url
	 * @param userName
	 *            数据库用户名
	 * @param pwd
	 *            用户密码
	 * @throws ClassNotFoundException
	 */
	public static void config(String url, String userName, String pwd) throws ClassNotFoundException {
		Db.url = url;
		Db.userName = userName;
		Db.pwd = pwd;
		Db.driver = getDriverClassName(url);
		Class.forName(driver);
	}

	/**
	 * 获取一个数据库连接
	 *  有限使用 Datasource 数据源连接
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws SQLException {
		if(ds != null){
			return ds.getConnection();
		}
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
	public static <T extends AutoCloseable & Wrapper> void close(T... t) throws SQLException {
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
				} catch (Exception e) { /* 不抛出异常 */
				}
			}
		}
	}

	/**
	 * insert update delete
	 * 
	 * @param conn
	 * @param sql
	 * @param commit
	 *            是否自动提交
	 * @param where
	 * @throws SQLException
	 */
	public static void excute(Connection conn, String sql, boolean commit, Object... where) throws SQLException {
		PreparedStatement stmt = null;
		try {
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(sql);
			if (where != null && where.length > 0) {
				for (int i = 0; i < where.length; i++) {
					stmt.setObject(i + 1, where[i]);
				}
			}
			stmt.executeUpdate();
			if (commit) {
				conn.commit();
			}
		} finally {
			closeQuiet(stmt);
		}
	}

	/**
	 * 默认自动提交
	 * 
	 * @param conn
	 * @param sql
	 * @param where
	 * @throws SQLException
	 */
	public static void excute(Connection conn, String sql, Object... where) throws SQLException {
		excute(conn, sql, true, where);
	}

	/**
	 * 批处理操作 insert update delete
	 * 
	 * @param conn
	 * @param sql
	 * @param commit
	 *            是否自动提交
	 * @param params
	 * @throws SQLException
	 */
	public static void excuteBatch(Connection conn, String sql, boolean commit, Object[][] params) throws SQLException {
		PreparedStatement stmt = null;
		try {
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				for (int j = 0; j < params[i].length; j++) {
					stmt.setObject(j + 1, params[i][j]);
				}
				stmt.addBatch();
			}
			stmt.executeBatch();
			if (commit) {
				conn.commit();
			}
		} finally {
			closeQuiet(stmt);
		}
	}

	/**
	 * 默认自动提交
	 * 
	 * @param conn
	 * @param sql
	 * @param params
	 * @throws SQLException
	 */
	public static void excuteBatch(Connection conn, String sql, Object[][] params) throws SQLException {
		excuteBatch(conn, sql, true, params);
	}

	/**
	 * select
	 * 
	 * @param conn
	 * @param sql
	 * @param where
	 * @return List<Map<String,Object>>
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> select(Connection conn, String sql, Object... where) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			if (U.isNotEmpty(where)) {
				for (int i = 0; i < where.length; i++) {
					stmt.setObject(i + 1, where[i]);
				}
			}
			rs = stmt.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount();
			String columnName[] = new String[columnCount];
			for (int i = 0; i < columnCount; i++) {
				columnName[i] = md.getColumnLabel(i + 1);
			}
			List<Map<String, Object>> result = U.newArrayList();
			while (rs.next()) {
				Map<String, Object> record = U.newLinkedHashMap();
				for (int i = 0; i < columnCount; i++) {
					record.put(columnName[i], rs.getObject(i + 1));
				}
				result.add(record);
			}
			return result;
		} finally {
			closeQuiet(stmt, rs);
		}
	}

	/**
	 * select查询
	 * 
	 * @param sql
	 * @param where
	 * @return List<Map<String, Object>>
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> select(String sql, Object... where) throws SQLException {
		Connection conn = null;
		try {
			conn =getConnection();
			List<Map<String, Object>> select = select(conn, sql, where);
			return select;
		} finally {
			closeQuiet(conn);
		}
	}

	/**
	 * 只获取第一条结果
	 * @param conn
	 * @param sql
	 * @param where
	 * @return Map<String,Object>
	 * @throws SQLException
	 */
	public static Map<String,Object> selectOne(Connection conn,String sql,Object ...where ) throws SQLException{
		List<Map<String,Object>> list = select(conn, sql, where);
		if(U.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 只获取第一条结果
	 * @param sql
	 * @param where
	 * @return Map<String,Object>
	 * @throws SQLException
	 */
	public static Map<String,Object> selectOne(String sql,Object ...where ) throws SQLException{
		List<Map<String,Object>> list = select(sql, where);
		if(U.isNotEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 执行 insert,update,delete
	 * @param sql
	 * @param where
	 * @throws SQLException
	 */
	public static void excute(String sql, Object... where) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn =getConnection();
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(sql);
			if (where != null && where.length > 0) {
				for (int i = 0; i < where.length; i++) {
					stmt.setObject(i + 1, where[i]);
				}
			}
			stmt.executeUpdate();
			conn.commit();
		} finally {
			closeQuiet(conn, stmt);
		}
	}

	/**
	 * 保存表数据
	 * @param tableName
	 * @param param
	 * @throws SQLException
	 */
	public static void insert(String tableName,Map<String,Object> param) throws SQLException{
		Connection conn = null;
		try {
			conn =getConnection();
			insert(conn, tableName, param);
		} finally{
			close(conn);
		}
		
	}
	/**
	 * 插入表 insert
	 * @param conn
	 * @param tableName
	 * @param param
	 * @throws SQLException 
	 */
	public static void insert(Connection conn,String tableName,Map<String,Object> param) throws SQLException{
		try {
			StringBuilder sb = new StringBuilder();
			StringBuilder w = new StringBuilder();
			List<Object> list = U.newArrayList();
			for(Map.Entry<String, Object> entry : param.entrySet()){
				String key = entry.getKey();
				if(Strs.isNotBlank(key)){
					sb.append(",").append(key);
					w.append(",?");
					list.add(entry.getValue());
				}
			}
			if(list.size() > 0){
				sb.deleteCharAt(0);
				w.deleteCharAt(0);
				String sql = "insert into " + tableName + " ( "+ sb.toString() + " ) values ( " + w.toString() + " )";
				excute(conn,sql,list.toArray());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 更新表 update
	 * @param tableName 表名
	 * @param conn
	 * @param param
	 * @param where
	 * @throws SQLException
	 */
	public static void update(String tableName,Connection conn,Map<String,Object> param,Map<String,Object> where) throws SQLException{
		try {
			StringBuilder sb = new StringBuilder();
			StringBuilder wSb = new StringBuilder();
			List<Object> list = U.newArrayList();
			for(Map.Entry<String, Object> entry : param.entrySet()){
				String key = entry.getKey();
				if(Strs.isNotBlank(key)){
					sb.append(",").append(key).append(" = ? ");
					list.add(entry.getValue());
				}
			}
			sb.deleteCharAt(0);
			sb.insert(0, "update " + tableName + " set ");
			if(where.size() > 0){
				for(Map.Entry<String, Object> entry : where.entrySet()){
					String key = entry.getKey();
					if(Strs.isNotBlank(key)){
						wSb.append(",").append(key).append(" = ? ");
						list.add(entry.getValue());
					}
				}
			}
			if(wSb.length() > 0){
				wSb.deleteCharAt(0);
				sb.append(" where ");
				sb.append(wSb.toString());
			}
			excute(conn, sb.toString(),list.toArray());
		} catch (SQLException e) {
			throw e;
		}
	}
	
	/**
	 * 自动获取Connection 批量执行
	 * 
	 * @param sql
	 * @param params
	 * @throws SQLException
	 */
	public static void excuteBatch(String sql, Object[][] params) throws SQLException {
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			conn =getConnection();
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				for (int j = 0; j < params[i].length; j++) {
					stmt.setObject(j + 1, params[i][j]);
				}
				stmt.addBatch();
			}
			stmt.executeBatch();
		} finally {
			closeQuiet(conn, stmt);
		}
	}
}
