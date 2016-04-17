package word.lib.helpers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mysql {

	protected Connection con = null;

	/**单例模式**/
	private static class SingletonHolder {
		private static final Mysql INSTANCE = new Mysql();
	}

	private Mysql() {
		this.getConnection();
	}

	public static final Mysql getInstance() {
		return SingletonHolder.INSTANCE;
	}
	/**单例模式**/
	
	/**
	 * 该方法用户连接数据库
	 * 
	 * @return 返回Connection的一个实例
	 */
	public void getConnection() {

		if (this.con == null) {
			try {

				Class.forName(StaticVar.DRIVER_NAME).newInstance();

				this.con = DriverManager.getConnection(StaticVar.DB_URL, StaticVar.USER_NAME, StaticVar.DB_PASSWD);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 用于查询sql语句
	 * 
	 * @param sql
	 *            sql语句
	 * @return 返回ResultSet集合
	 */
	public List<Map<String, Object>> select(String sql) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ResultSet res = null;
		Statement state = null;
		try {
			if (!(con == null)) {
				state = con.createStatement();
				res = state.executeQuery(sql);
			}
			ResultSetMetaData metaDate;
			metaDate = res.getMetaData();// 获取列信息,交给metaDate
			int columnlength = metaDate.getColumnCount();
			while (res.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < columnlength; i++) {
					String metaDateKey = metaDate.getColumnName(i + 1);// 获取列名
					Object resultsetValue = res.getObject(metaDateKey);
					if (resultsetValue == null) {
						resultsetValue = "";
					}
					map.put(metaDateKey, resultsetValue);
				}
				list.add(map);
			}

			if (state != null) {
				state.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 向表中插入一个元素，返回插入后的元素的id
	 * 
	 * @param sql
	 * @return
	 */
	public int insert(String sql) {
		int iId = -1;
		Statement state = null;
		try {
			if (con != null) {

				state = con.createStatement();

				int res = state.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

				if (res != 0) {
					ResultSet rs = state.getGeneratedKeys();
					if (rs.next()) {
						iId = rs.getInt(1);
					}
				}
			}

			if (state != null) {
				state.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return iId;
	}

	/**
	 * 修改表中的某个元素的数值
	 * 
	 * @param sql
	 *            sql语句
	 * @return 元素是否被成功修改
	 */
	public boolean update(String sql) {
		boolean updated = false;
		Statement state = null;
		try {

			state = con.createStatement();

			int res = state.executeUpdate(sql);

			if (res == 0) {
				updated = false;
			} else {
				updated = true;
			}

			if (state != null) {
				state.close();
			}
		} catch (SQLException e) {
			updated = false;
			e.printStackTrace();
		}
		return updated;
	}

	/**
	 * 删除表中的某一个表项
	 * 
	 * @param sql
	 *            sql语句
	 * @return 返回是否删除成功
	 */
	public boolean delete(String sql) {
		boolean deleted = false;
		Statement state = null;

		if (con != null) {
			try {
				state = con.createStatement();

				int res = state.executeUpdate(sql);

				if (res == 0) {
					deleted = false;
				} else {
					deleted = true;
				}

			} catch (SQLException e) {
				deleted = false;
				e.printStackTrace();
			}

		}

		if (state != null) {
			try {
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return deleted;
	}

	/**
	 * 批量存储sql语句
	 * 
	 * @param sql
	 */
	public Statement addBatch(Statement state, String sql) {

		try {
			if (state == null) {
				state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			}
			state.addBatch(sql);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return state;
	}

	/**
	 * 执行批量sql
	 * 
	 * @param state
	 */
	public void executeBatch(Statement state) {
		try {
			state.executeBatch();
			state.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}