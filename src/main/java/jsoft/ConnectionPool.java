package jsoft;

import java.sql.*;

public interface ConnectionPool {
	
	// phuong thuc lay ket noi
	public Connection getConnection(String objectName) throws SQLException;
	
	// phuong thuc thu hoi ket noi
	public void releaseConnection(Connection con, String objectName) throws SQLException;
}
