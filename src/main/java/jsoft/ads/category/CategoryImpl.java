package jsoft.ads.category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Quintet;
import org.javatuples.Triplet;

import jsoft.ConnectionPool;
import jsoft.ConnectionPoolImpl;
import jsoft.ads.basic.BasicImpl;
import jsoft.ads.category.CATEGORY_SOFT;
import jsoft.ads.user.USER_SOFT;
import jsoft.ads.user.User;
import jsoft.ads.user.UserImpl;
import jsoft.library.ORDER;
import jsoft.objects.CategoryObject;
import jsoft.objects.SectionObject;
import jsoft.objects.UserObject;

public class CategoryImpl extends BasicImpl implements Category {

	public CategoryImpl(ConnectionPool cp) {
		super(cp, "Category");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean addCategory(CategoryObject item) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO tblcategory(");
		sql.append("category_name, category_section_id, category_notes, category_created_date, category_created_author_id, ");
		sql.append("category_last_modified, category_manager_id, category_delete, category_enable ");
		sql.append(")");
		sql.append("VALUE(?,?,?,?,?,?,?, ?, ?)");
		
		try {
			PreparedStatement pre = this.con.prepareStatement(sql.toString());
			
			pre.setString(1, item.getCategory_name());
			pre.setShort(2, item.getCategory_section_id());
			pre.setString(3, item.getCategory_notes());
			pre.setString(4, item.getSection_created_date());
			pre.setInt(5, item.getCategory_created_author_id());
			pre.setString(6, item.getCategory_last_modified());
			pre.setInt(7, item.getCategory_manager_id());
			pre.setBoolean(8, item.isCategory_delete());
			pre.setBoolean(9, item.isCategory_enable());
			
			return this.add(pre);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				this.con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean editCategory(CategoryObject item, CATEGORY_EDIT_TYPE et) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE tblcategory SET ");

		switch (et) {
		case GENERAL:			
			sql.append("category_name=?, category_section_id=?, category_notes=?, ");
			sql.append("category_enable=?, category_manager_id=? ");
			break;

		case TRASH:
			sql.append("category_delete=1, category_last_modified=? ");
			break;
		case RESTORE:
			sql.append("category_delete=0 ");
			break;
		}
		
		sql.append(" WHERE category_id=?");
		
		
		try {
			PreparedStatement pre = this.con.prepareStatement(sql.toString());

			switch (et) {
			case GENERAL:
				pre.setString(1, item.getCategory_name());
				pre.setInt(2, item.getCategory_section_id());
				pre.setString(3, item.getCategory_notes());
				pre.setBoolean(4, item.isCategory_enable());
				pre.setInt(5, item.getCategory_manager_id());
				
				pre.setInt(6, item.getCategory_id());
				break;

			case TRASH:
				pre.setString(1, item.getCategory_last_modified());
				pre.setInt(2, item.getCategory_id());
				break;
			case RESTORE:
				pre.setInt(1, item.getCategory_id());
				break;
			}
			
			return this.edit(pre);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				this.con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delCategory(CategoryObject item) {
		// TODO Auto-generated method stub
		if (!this.isEmpty(item)) {
			return false;
		}

		StringBuffer sql = new StringBuffer();
		sql.append(
				"DELETE FROM tblcategory WHERE (category_id=?) AND ((category_created_author_id=?) OR (category_manager_id=?))");

		try {
			PreparedStatement pre = this.con.prepareStatement(sql.toString());
			pre.setShort(1, item.getCategory_id());
			pre.setInt(2, item.getCategory_created_author_id());
			pre.setInt(3, item.getCategory_manager_id());

			return this.del(pre);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// tro ve trang thai an toan cua ket noi
			try {
				this.con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		return false;
	}
	
	private boolean isEmpty(CategoryObject item) {
		boolean flag = true;
		String sql = "SELECT article_id FROM tblarticle WHERE article_category_id = ?";
		ResultSet rs = this.get(sql, item.getCategory_id());

		if (rs != null) {
			try {
				if (rs.next()) {
					flag = false;
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flag;
	}

	@Override
	public ResultSet getCategory(short id, UserObject userLogined) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM tblcategory c "); // cate la con cua section
		sql.append("LEFT JOIN tblsection s ON c.category_section_id = s.section_id ");
		sql.append("WHERE (c.category_id=?) AND ((c.category_created_author_id = "+userLogined.getUser_id()+") OR (c.category_manager_id="+userLogined.getUser_id()+"))"); // (ccad OR cmad) ...userLogined.getUser_id
		System.out.println("CateIMPL - " + sql.toString());
		return this.get(sql.toString(), (int)id);
	}

	@Override
	public ArrayList<ResultSet> getCategories(Quartet<CategoryObject, Short, Byte, UserObject> infos, Pair<CATEGORY_SOFT, ORDER> so) {
		// TODO Auto-generated method stub
		CategoryObject similar = infos.getValue0();

		// vị trí bắt đầu lấy bản ghi
		int at = infos.getValue1();

		// Số bản ghi được lấy trong một lần
		byte total = infos.getValue2();
		
		// tai khoan dang nhap
		UserObject user = infos.getValue3();

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT * FROM tblcategory AS c ");
		sql.append("LEFT JOIN tblsection AS s ON c.category_section_id = s.section_id ");
		sql.append("LEFT JOIN tbluser AS u ON c.category_manager_id = u.user_id ");
		sql.append(this.createConditions(similar));
		sql.append("");
		switch (so.getValue0()) {
		case NAME:
			sql.append("ORDER BY c.category_name ").append(so.getValue1().name());
			break;
		case MANAGER:
			sql.append("ORDER BY c.category_manager_id  ").append(so.getValue1().name());
			break;
		default:
			sql.append("ORDER BY c.category_id DESC ");

		}
		sql.append(" LIMIT ").append(at).append(", ").append(total).append(";");

		// so luong ban ghi
		sql.append("SELECT COUNT(category_id) AS total FROM tblcategory;");
		
		// danh sach quyen, phu thuoc va nguoi dang nhap
		sql.append("SELECT * FROM tbluser WHERE ");
		sql.append("(user_permission<=").append(user.getUser_permission()).append(") AND (");
		sql.append("(user_parent_id=").append(user.getUser_id()).append(") OR (user_id=").append(user.getUser_id()).append(")");
		sql.append(");");

		// danh sach chuyen muc
		sql.append("SELECT * FROM tblsection ORDER BY section_id DESC ;");
		
//		System.out.println(sql.toString());
		
		return this.getMR(sql.toString());
	}
	
	private String createConditions(CategoryObject similar) {
		StringBuilder conds = new StringBuilder();
		
		if(similar != null) {
			if(similar.isCategory_delete()) {
				conds.append("(category_delete=1) ");
			} else {
				conds.append("(category_delete=0) ");
			}
		}
		
		if(!conds.toString().equalsIgnoreCase("")) {
			conds.insert(0, " WHERE ");
		}
		
		return conds.toString();
	}

//	public static void main(String[] args) {
//		// tạo bộ quản lý kết nối
//		ConnectionPool cp = new ConnectionPoolImpl();
//
//		// đối tượng thực thi chức năng mức interface
//		Category c = new CategoryImpl(cp);
//		
//		UserObject uc = new UserObject();
//		uc.setUser_id(20);
//		
//		ResultSet rs = c.getCategory((short)35, uc);
//		
//		String row;
//		
//		if(rs!=null) {
//			try {
//				if(rs.next()) {
//					row = "ID: " + rs.getInt("category_id");
//					row += "\tNAME: " + rs.getString("category_name");
//					row += "\tCATEGORY_SECTION_ID: " + rs.getInt("category_section_id");
//					System.out.println(row);
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
//		Quartet<CategoryObject, Short, Byte, UserObject> infos = new Quartet<>(null, (short)0, (byte) 6, null);
//
//		ArrayList<ResultSet> res = c.getCategories(infos, new Pair<>(CATEGORY_SOFT.NAME, ORDER.DESC));
//
//		ResultSet rs = res.get(0);
//
//		String row;
//
//		if (rs != null) {
//			try {
//				while (rs.next()) {
//					row = "ID: " + rs.getInt("category_id");
//					row += "\tNAME: " + rs.getString("category_name");
//					row += "\tCATEGORY_SECTION_ID: " + rs.getInt("category_section_id");
//					System.out.println(row);
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		rs = res.get(1);
//		if (rs != null) {
//			try {
//				while (rs.next()) {
//					System.out.println("Tong so danh muc trong DB:" + rs.getShort("total"));
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
}
