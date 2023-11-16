package jsoft.ads.section;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

import jsoft.ConnectionPool;
import jsoft.ConnectionPoolImpl;
import jsoft.ads.basic.BasicImpl;
import jsoft.ads.section.SECTION_SOFT;
import jsoft.ads.category.Category;
import jsoft.ads.category.CategoryImpl;
import jsoft.library.ORDER;
import jsoft.objects.CategoryObject;
import jsoft.objects.SectionObject;
import jsoft.objects.UserObject;

public class SectionImpl extends BasicImpl implements Section {

	public SectionImpl(ConnectionPool cp) {
		super(cp, "Section");
	}

	@Override
	public boolean addSection(SectionObject item) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO tblsection(");
		sql.append("section_name, section_notes, section_created_date, section_manager_id, ");
		sql.append("section_enable, section_delete, section_last_modified, section_created_author_id, ");
		sql.append("section_name_en, section_language) ");
		sql.append("VALUE(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		try {
			PreparedStatement pre = this.con.prepareStatement(sql.toString());

			pre.setString(1, item.getSection_name());
			pre.setString(2, item.getSection_notes());
			pre.setString(3, item.getSection_created_date());
			pre.setInt(4, item.getSection_manager_id());
			pre.setBoolean(5, item.isSection_enable());
			pre.setBoolean(6, item.isSection_delete());
			pre.setString(7, item.getSection_last_modified());
			pre.setInt(8, item.getSection_created_author_id());
			pre.setString(9, item.getSection_name_en());
			pre.setInt(10, item.getSection_language());

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
	public boolean editSection(SectionObject item, SECTION_EDIT_TYPE et) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE tblsection SET ");

		switch (et) {
		case GENERAL:
			sql.append("section_name=?, section_notes=?, section_manager_id=?, ");
			sql.append("section_enable=?, ");
			sql.append("section_name_en=?, section_language=? ");
			break;

		case TRASH:
			sql.append("section_delete=1, section_last_modified=? ");
			break;
		case RESTORE:
			sql.append("section_delete=0 ");
			break;
		}
		
		sql.append(" WHERE section_id=?");

		try {
			PreparedStatement pre = this.con.prepareStatement(sql.toString());

			switch (et) {
			case GENERAL:
				pre.setString(1, item.getSection_name());
				pre.setString(2, item.getSection_notes());
				pre.setInt(3, item.getSection_manager_id());
				pre.setBoolean(4, item.isSection_enable());
				pre.setString(5, item.getSection_name_en());
				pre.setByte(6, item.getSection_language());
				pre.setInt(7, item.getSection_id());
				break;

			case TRASH:
				pre.setString(1, item.getSection_last_modified());
				pre.setInt(2, item.getSection_id());
				break;
			case RESTORE:
				pre.setInt(1, item.getSection_id());
				break;
			}
//			System.out.println(sql.toString());
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
	public boolean delSection(SectionObject item) {
		// TODO Auto-generated method stub
		if (!this.isEmpty(item)) {
			return false;
		}

		StringBuffer sql = new StringBuffer();
		sql.append(
				"DELETE FROM tblsection WHERE (section_id=?) AND ((section_created_author_id=?) OR (section_manager_id=?))");

		try {
			PreparedStatement pre = this.con.prepareStatement(sql.toString());
			pre.setShort(1, item.getSection_id());
			pre.setInt(2, item.getSection_created_author_id());
			pre.setInt(3, item.getSection_manager_id());

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

	private boolean isEmpty(SectionObject item) {
		boolean flag = true;
		String sql = "SELECT category_id FROM tblcategory WHERE category_section_id = ?";
		ResultSet rs = this.get(sql, item.getSection_id());

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
	public ResultSet getSection(short id) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM tblsection AS s ");
		sql.append("LEFT JOIN tbluser AS u ON s.section_manager_id = u.user_id ");
		sql.append("WHERE s.section_id = ?");
		System.out.println(sql.toString());
		return this.get(sql.toString(), (int)id);
	}

	@Override
	public ArrayList<ResultSet> getSections(Quartet<SectionObject, Short, Byte, UserObject> infos, Pair<SECTION_SOFT, ORDER> so) {
		// đối tượng lưu trữ thông tin lọc kết quả
		SectionObject similar = infos.getValue0();

		// Số bản ghi được lấy trong một lần
		byte total = infos.getValue2();
		
		// vị trí bắt đầu lấy bản ghi
		int at = (infos.getValue1()-1)*total;
		
		// tai khoan dang nhap
		UserObject user = infos.getValue3();

		StringBuffer sql = new StringBuffer();

		// danh sach chuyen muc
		sql.append("SELECT * FROM tblsection AS s ");
		sql.append("LEFT JOIN tbluser AS u ON s.section_manager_id = u.user_id ");
		sql.append(this.createConditions(similar));
		sql.append("");
		switch (so.getValue0()) {
		case NAME:
			sql.append("ORDER BY s.section_name ").append(so.getValue1().name());
			break;
		case MANAGER:
			sql.append("ORDER BY s.section_manager_id  ").append(so.getValue1().name());
			break;
		default:
			sql.append("ORDER BY s.section_id DESC ");

		}
		sql.append(" LIMIT ").append(at).append(", ").append(total).append("; ");
		
		// tong so chuyen muc
		sql.append("SELECT COUNT(user_id) AS total FROM tblUser;");
		
		// danh sach quyen, phu thuoc va nguoi dang nhap
		sql.append("SELECT * FROM tbluser WHERE ");
		sql.append("(user_permission<=").append(user.getUser_permission()).append(") AND (");
		sql.append("(user_parent_id=").append(user.getUser_id()).append(") OR (user_id=").append(user.getUser_id()).append(")");
		sql.append(");");
		
//		System.out.print(sql.toString());

		return this.getMR(sql.toString());
	}

	public static void main(String[] args) {
//		// tạo bộ quản lý kết nối
//		ConnectionPool cp = new ConnectionPoolImpl();
//
//		// đối tượng thực thi chức năng mức interface
//		Section s = new SectionImpl(cp);
//		
//		UserObject us = new UserObject();
//		us.setUser_id(23);
//		ResultSet getS = s.getSection((short)1, us);
//		String row;
//		
//		if (getS != null) {
//			System.out.println("not null");
//			try {
//				while (getS.next()) {
//					row = "ID: " + getS.getInt("section_id");
//					row += "\tNAME: " + getS.getString("section_name");
//					row += "\tSECTION_MANAGER_ID: " + getS.getInt("section_manager_id");
//					row += "\tNAME_MANAGER: " + getS.getString("user_name");
//					System.out.println(row);
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else {
//			System.out.println("null");
//		}
//
//		Triplet<SectionObject, Integer, Byte> infos = new Triplet<>(null, 0, (byte) 6);
//
//		ArrayList<ResultSet> res = s.getSections(infos, new Pair<>(SECTION_SOFT.NAME, ORDER.DESC));
//
//		ResultSet rs = res.get(0);
//
//		
//
//		if (rs != null) {
//			try {
//				while (rs.next()) {
//					row = "ID: " + rs.getInt("section_id");
//					row += "\tNAME: " + rs.getString("section_name");
//					row += "\tSECTION_MANAGER_ID: " + rs.getInt("section_manager_id");
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
//					System.out.println("Tong so section trong DB:" + rs.getShort("total"));
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

	
	private String createConditions(SectionObject similar) {
		StringBuilder conds = new StringBuilder();
		
		if(similar != null) {

			// System.out.println(similar.isUser_delete());
			if(similar.isSection_delete()) {
				conds.append("(section_delete=1) ");
			} else {
				conds.append("(section_delete=0) ");
			}
		}
		
		if(!conds.toString().equalsIgnoreCase("")) {
			conds.insert(0, " WHERE ");
		}
		
		return conds.toString();
	}
}
