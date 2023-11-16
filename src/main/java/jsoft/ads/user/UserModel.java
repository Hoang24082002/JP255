package jsoft.ads.user;

import jsoft.*;
import jsoft.library.ORDER;
import jsoft.objects.*;

import java.sql.*;
import java.util.*;

import org.javatuples.Pair;
import org.javatuples.Triplet;

public class UserModel {
	private User u;
	public UserModel(ConnectionPool cp) {
		this.u = new UserImpl(cp);
	}
	
	public ConnectionPool getCP() {
		return this.u.getCP();
	}
	
	public void releaseConnection() {
		this.u.releaseConnection();
	}
	
//	---------------------------------------
	public boolean addUser(UserObject item) {
		return this.u.addUser(item);
	}
	
	public boolean editUser(UserObject item, USER_EDIT_TYPE et) {
		return this.u.editUser(item, et);
	}
	
	public boolean delUser(UserObject item) {
		return this.u.delUser(item);
	}
	
//	-------------------------------------------
	public UserObject getUserObject(int id) {
		UserObject item = null;
		ResultSet rs = this.u.getUser(id);
		if(rs!=null) {
			try {
				if(rs.next()) {
					item = new UserObject();
					item.setUser_id(rs.getInt("user_id"));
					item.setUser_name(rs.getString("user_name"));
					item.setUser_birthday(rs.getString("user_birthday"));
					item.setUser_fullname(rs.getString("user_fullname"));
					item.setUser_email(rs.getString("user_email"));
					item.setUser_address(rs.getString("user_address"));
					item.setUser_created_date(rs.getString("user_created_date"));
					item.setUser_last_logined(rs.getString("user_last_logined"));
					item.setUser_homephone(rs.getString("user_homephone"));
					item.setUser_mobilephone(rs.getString("user_mobilephone"));
					item.setUser_notes(rs.getString("user_notes"));
					item.setUser_job(rs.getString("user_job"));
					item.setUser_jobarea(rs.getString("user_jobarea"));
					item.setUser_permission(rs.getByte("user_permission"));
					item.setUser_last_modified(rs.getString("user_last_modified"));
					item.setUser_delete(rs.getBoolean("user_delete"));
					//System.out.println(rs.getBoolean("user_delete"));
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return item;
	}
	
	public UserObject getUserObject(String username, String userpass) {
		UserObject item = null;
		ResultSet rs = this.u.getUser(username, userpass);
		if(rs!=null) {
			try {
				if(rs.next()) {
					item = new UserObject();
					item.setUser_id(rs.getInt("user_id"));
					item.setUser_name(rs.getString("user_name"));
					item.setUser_pass(rs.getString("user_pass"));
					item.setUser_fullname(rs.getString("user_fullname"));
					item.setUser_email(rs.getString("user_email"));
					item.setUser_address(rs.getString("user_address"));
					item.setUser_created_date(rs.getString("user_created_date"));
					item.setUser_last_logined(rs.getString("user_last_logined"));
					item.setUser_homephone(rs.getString("user_homephone"));
					item.setUser_mobilephone(rs.getString("user_mobilephone"));
					
					item.setUser_permission(rs.getByte("user_permission"));
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return item;
	}
	
	public Pair<ArrayList<UserObject>, Short> getUserObjects(Triplet<UserObject, Integer, Byte> infos, Pair<USER_SOFT, ORDER> so) {
		ArrayList<UserObject> items = new ArrayList<>();
		
		UserObject item = null;
		
		ArrayList<ResultSet> res = this.u.getUsers(infos, so);
		
		ResultSet rs = res.get(0);
		
		if(rs!=null) {
			try {
				while(rs.next()) {
					item = new UserObject();
					item.setUser_id(rs.getInt("user_id"));
					item.setUser_name(rs.getString("user_name"));
					item.setUser_fullname(rs.getString("user_fullname"));
					item.setUser_email(rs.getString("user_email"));
					item.setUser_address(rs.getString("user_address"));
					item.setUser_created_date(rs.getString("user_created_date"));
					item.setUser_last_logined(rs.getString("user_last_logined"));
					item.setUser_homephone(rs.getString("user_homephone"));
					item.setUser_mobilephone(rs.getString("user_mobilephone"));
					item.setUser_logined(rs.getShort("user_logined"));
					item.setUser_permission(rs.getByte("user_permission"));
					item.setUser_last_modified(rs.getString("user_last_modified"));
					item.setUser_delete(rs.getBoolean("user_delete"));
					
					items.add(item);
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// lay tong so ban ghi
		rs = res.get(1);
		short total = 0;
		if(rs!=null) {
			try {
				if(rs.next()) {
					total = rs.getShort("total");
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return new Pair<>(items ,total);
	}
}
