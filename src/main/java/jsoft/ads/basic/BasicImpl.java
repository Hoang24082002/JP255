package jsoft.ads.basic;

import java.sql.*;
import java.util.ArrayList;

import jsoft.*;

public class BasicImpl implements Basic {
	// bộ quản lý kết nối của riêng Basic
	private ConnectionPool cp;
	
	// kết nối để Basic làm việc vs CSDL
	protected Connection con;
	
	// đối tượng làm việc với Basic
	private String objectName;
	
	
	public BasicImpl(ConnectionPool cp, String objectName) {
		//  xác định đối tượng làm việc vs Basic
		this.objectName = objectName;
		
		// xác định bộ quản lý kết nối
		if(cp==null) {
			this.cp = new ConnectionPoolImpl();
		} else {
			this.cp = cp;			
		}
		
		// hỏi xin kết nối để làm việc
		try {
			this.con = this.cp.getConnection(this.objectName);
			
			// kiểm tra trạng thái thực thi của kết nối
			if(this.con.getAutoCommit()) {
				this.con.setAutoCommit(false); // hủy thực thi tự động
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private synchronized boolean exe(PreparedStatement pre) {
		if(pre != null) {
			try {
				int result = pre.executeUpdate();// thực thi cập nhật
				
				if(result == 0) {
					this.con.rollback();
					return false;
				}
				
				// xác nhận thực thi sau cùng
				this.con.commit();
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				// trở lại trạng thái an toàn của kết nối
				try {
					this.con.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} finally {
				pre = null;
			}
		}
		
		return false;
	}

	@Override
	public synchronized boolean add(PreparedStatement pre) {
		// TODO Auto-generated method stub
		return this.exe(pre);
	}

	@Override
	public synchronized boolean edit(PreparedStatement pre) {
		// TODO Auto-generated method stub
		return this.exe(pre);
	}

	@Override
	public synchronized boolean del(PreparedStatement pre) {
		// TODO Auto-generated method stub
		return this.exe(pre);
	}

	@Override
	public ResultSet get(String sql, int id) {
		// TODO Auto-generated method stub
		
		// Biên dịch
		try {
			PreparedStatement pre = this.con.prepareStatement(sql);
			
			if(id > 0) {
				pre.setInt(1, id);
			}
			
			// thực thi
			return pre.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			try {
				this.con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			sql = null;
		}
		return null;
	}

	@Override
	public synchronized ResultSet get(ArrayList<String> sql, String name, String pass) {
		// TODO Auto-generated method stub
		try {
			String str_select = sql.get(0);
			PreparedStatement pre_select = this.con.prepareStatement(str_select);
			
			pre_select.setString(1, name);
			pre_select.setString(2, pass);
			
			ResultSet rs =  pre_select.executeQuery();
			if(rs != null) {
				String str_updateLogined = sql.get(1);
				PreparedStatement pre_updateLogined = this.con.prepareStatement(str_updateLogined);
				pre_updateLogined.setString(1, name);
				pre_updateLogined.setString(2, pass);
				
				int result = pre_updateLogined.executeUpdate();
				if(result==0) {
					this.con.rollback();
					return null;
				} else {
					if(!this.con.getAutoCommit()) {
						this.con.commit();						
					}
					return rs;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			try {
				this.con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			sql = null;
		}
		
		return null;
	}

	@Override
	public ResultSet gets(String sql) {
		// TODO Auto-generated method stub
		return this.get(sql, 0);
	}

	@Override
	public ResultSet[] gets(String[] sql) {
		// TODO Auto-generated method stub
		ResultSet[] tmp = new ResultSet[sql.length];
		for( int i = 0; i < sql.length; i++) {
			tmp[i] = this.get(sql[i], 0);
		}
		return tmp;
	}

	@Override
	public ConnectionPool getCP() {
		// TODO Auto-generated method stub
		return this.cp;
	}

	@Override
	public void releaseConnection() {
		// TODO Auto-generated method stub
		try {
			this.cp.releaseConnection(this.con, this.objectName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<ResultSet> getMR(String multiSelect) {
		// TODO Auto-generated method stub
		ArrayList<ResultSet> res = new ArrayList<>();
		
		try {
			PreparedStatement pre = this.con.prepareStatement(multiSelect);
			
			boolean result = pre.execute();
			
			do {
				if(result) {
					res.add(pre.getResultSet());					
				}
				
				// di chuyen sang ResultSet tiep theo
				result = pre.getMoreResults(PreparedStatement.KEEP_CURRENT_RESULT); 
			} while(result);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}

}
