package jsoft.ads.category;

import jsoft.*;
import jsoft.library.ORDER;
import jsoft.objects.*;

import java.sql.*;
import java.util.*;

import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Quintet;
import org.javatuples.Triplet;

public class CategoryModel {
	private Category c;
	public CategoryModel(ConnectionPool cp) {
		this.c = new CategoryImpl(cp);
	}
	
	public ConnectionPool getCP() {
		return this.c.getCP();
	}

	public void releaseConnection() {
		this.c.releaseConnection();
	}
	
//	---------------------------------------
	public boolean addCategory(CategoryObject item) {
		return this.c.addCategory(item);
	}
	
	public boolean editCategory(CategoryObject item, CATEGORY_EDIT_TYPE et) {
		return this.c.editCategory(item, et);
	}
	
	public boolean delCategory(CategoryObject item) {
		return this.c.delCategory(item);
	}
	
//	----------------------------------------
	
	public CategoryObject getCategoryObject(short id, UserObject userLogined) {
		CategoryObject item = null;
		ResultSet rs = this.c.getCategory(id, userLogined);
		if(rs!=null) {
			try {
				if(rs.next()) {
					item = new CategoryObject();
					item.setCategory_id(rs.getShort("category_id"));
					item.setCategory_name(rs.getString("category_name"));
					item.setCategory_created_date(rs.getString("category_created_date"));
					item.setCategory_manager_id(rs.getInt("category_manager_id"));
					item.setCategory_notes(rs.getString("category_notes"));
					item.setCategory_section_id(rs.getShort("category_section_id"));
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return item;
	}
	
	public Quintet<ArrayList<CategoryObject>,
					Short, 
					HashMap<Integer, String>, 
					ArrayList<UserObject>, 
					ArrayList<SectionObject>> getCategoryObjects(Quartet<CategoryObject, Short, Byte, UserObject> infos, Pair<CATEGORY_SOFT, ORDER> so) {
		
		
		ArrayList<CategoryObject> items = new ArrayList<>();// danh sach danh muc		
		HashMap<Integer, String> managerName = new HashMap<>();// danh sach nguoi quan ly
		CategoryObject item = null;		
		ArrayList<ResultSet> res = this.c.getCategories(infos, so);	
		ResultSet rs = res.get(0);
		if(rs!=null) {
			try {
				while(rs.next()) {
					item = new CategoryObject();
					item.setCategory_id(rs.getShort("category_id"));
					item.setCategory_name(rs.getString("category_name"));
					item.setCategory_created_date(rs.getString("category_created_date"));
					item.setCategory_delete(rs.getBoolean("category_delete"));
					item.setCategory_manager_id(rs.getInt("category_manager_id"));
					item.setCategory_created_author_id(rs.getInt("category_created_author_id"));
					item.setCategory_name_en(rs.getString("category_name_en"));
					item.setCategory_delete(rs.getBoolean("category_delete"));
					item.setCategory_enable(rs.getBoolean("category_enable"));
					
					items.add(item);
					
					managerName.put(rs.getInt("user_id"), rs.getString("user_fullname") + "("+rs.getString("user_name")+")");
//					managerName.put(20, "aaaaa");
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// tong ban ghi
		rs = res.get(1);
		short total = 0;
		if(rs!= null) {
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
		
		// danh sach quyen, phu thuoc va nguoi dang nhap
		rs = res.get(2);
		ArrayList<UserObject> users = new ArrayList<>();
		UserObject user = null;
		if(rs!=null) {
			try {
				while(rs.next()) {
					user = new UserObject();
					user.setUser_id(rs.getInt("user_id"));
					user.setUser_name(rs.getString("user_name"));
					user.setUser_fullname(rs.getString("user_fullname"));
					
					users.add(user);
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// danh sach chuyen muc
		rs = res.get(3);
		ArrayList<SectionObject> sections = new ArrayList<>();
		SectionObject section = null;
		if(rs!=null) {
			try {
				while(rs.next()) {
					section = new SectionObject();
					section.setSection_id(rs.getShort("section_id"));
					section.setSection_name(rs.getString("section_name"));

					sections.add(section);
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return new Quintet<>(items, total, managerName, users, sections);
	}
}
