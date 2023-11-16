package jsoft;

import java.util.*;
import java.sql.*;

public class ConnectionPoolImpl implements ConnectionPool {
	// trình điều khiển làm việc vs MySQL
	private String driver;

	// đường dẫn thực thi
	private String url;

	// tài khoản làm việc vs CSDL
	private String username;
	private String userpass;

	// đối tượng lưu trữ kết nối
	private Stack<Connection> pool;

	public ConnectionPoolImpl() {
		// xác định trình điều khiển
		this.driver = "com.mysql.cj.jdbc.Driver";

		// xác định đường dẫn thực thi
		this.url = "jdbc:mysql://localhost:3306/jp225_data?allowMultiQueries=true";

		// xác định tài khoản làm việc
		this.username = "jp225_Hoangfit";
		this.userpass = "Hoang24@";

		// nạp trình điều khiển
		this.loadDriver();

		// khởi tạo đối tượng lưu trữ kết nối
		this.pool = new Stack<>();
	}

	private void loadDriver() {
		try {
			Class.forName(this.driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Connection getConnection(String objectName) throws SQLException {
		// TODO Auto-generated method stub
		if (this.pool.isEmpty()) {
			// khởi tạo kết nối mới
			System.out.println(objectName + " have created a new Connection.");
			return DriverManager.getConnection(this.url, this.username, this.userpass);
		} else {
			// lấy kết nối được lưu trũ
			System.out.println(objectName + " popped the Connection.");
			return this.pool.pop();
		}
	}

	@Override
	public void releaseConnection(Connection con, String objectName) throws SQLException {
		// TODO Auto-generated method stub

		// yêu cầu đối tượng trả về kết nối
		System.out.println(objectName + " have push the Connection.");
		this.pool.push(con);
	}

	protected void finalize() throws Throwable {
		this.pool.clear();
		this.pool = null;

		System.out.println("Connection pool is closed.");
	}

}
