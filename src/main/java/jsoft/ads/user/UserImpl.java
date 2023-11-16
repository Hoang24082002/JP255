package jsoft.ads.user;

import java.sql.*;

import org.javatuples.*;

import com.mysql.cj.xdevapi.Result;

import jsoft.objects.*;

import jsoft.ads.basic.*;
import jsoft.library.ORDER;
import jsoft.*;

import java.util.*;

public class UserImpl extends BasicImpl implements User {

	public UserImpl(ConnectionPool cp) {
		super(cp, "User");
	}

	@Override
	public boolean addUser(UserObject item) {
		// TODO Auto-generated method stub
		if (this.isExisting(item)) {
			return false;
		}

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO tbluser(");
		sql.append("user_name, user_pass, user_fullname, user_birthday, user_mobilephone, ");
		sql.append("user_homephone, user_officephone, user_email, user_address, user_jobarea, ");
		sql.append("user_job, user_position, user_applyyear, user_permission, user_notes, ");
		sql.append("user_roles, user_logined, user_created_date, user_last_modified,");
		sql.append("user_last_logined, user_parent_id, user_actions");
		sql.append(")");
		sql.append("VALUE(?,md5(?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		// bien dich
		try {
			PreparedStatement pre = this.con.prepareStatement(sql.toString());
			pre.setString(1, item.getUser_name());
			pre.setString(2, item.getUser_pass());
			pre.setString(3, item.getUser_fullname());
			pre.setString(4, item.getUser_birthday());
			pre.setString(5, item.getUser_mobilephone());
			pre.setString(6, item.getUser_homephone());
			pre.setString(7, item.getUser_officephone());
			pre.setString(8, item.getUser_email());
			pre.setString(9, item.getUser_address());
			pre.setString(10, item.getUser_jobarea());
			pre.setString(11, item.getUser_job());
			pre.setString(12, item.getUser_position());
			pre.setShort(13, item.getUser_applyyear());
			pre.setByte(14, item.getUser_permission());
			pre.setString(15, item.getUser_notes());
			pre.setString(16, item.getUser_roles());
			pre.setShort(17, item.getUser_logined());
			pre.setString(18, item.getUser_created_date());
			pre.setString(19, item.getUser_last_modified());
			pre.setString(20, item.getUser_last_logined());
			pre.setInt(21, item.getUser_parent_id());
			pre.setByte(22, item.getUser_actions());

			return this.add(pre);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			// tro ve trang thai an toan cua ket noi
			try {
				this.con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		return false;
	}

	private boolean isExisting(UserObject item) {
		boolean flag = false;
		String sql = "SELECT user_id FROM tblUser WHERE user_name = '" + item.getUser_name() + "'";
		ResultSet rs = this.get(sql, 0);
		if (rs != null) {
			try {
				if (rs.next()) {
					flag = true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return flag;
	}

	@Override
	public boolean editUser(UserObject item, USER_EDIT_TYPE et) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE tbluser SET ");
		switch(et) {
		case GENERAL:
			sql.append("user_fullname=?, user_birthday=?, user_mobilephone=?, ");
			sql.append("user_homephone=?, user_officephone=?, user_email=?, user_address=?, user_jobarea=?, ");
			sql.append("user_job=?, user_position=?, user_applyyear=?, user_notes=?, ");
			sql.append("user_roles=?, user_last_modified=?, ");
			sql.append("user_actions=? ");	
			break;
		case SETTINGS:
			sql.append("user_permission=?, user_roles=? ");
			break;
		case PASS:
			sql.append("user_pass=md5(?) ");
			break;
		case TRASH:
			sql.append("user_delete=1, user_last_modified=? ");
			break;
		case RESTORE:
			sql.append("user_delete=0 ");
			break;
		}
		sql.append(" WHERE user_id=?");

		// bien dich
		try {
			PreparedStatement pre = this.con.prepareStatement(sql.toString());
			
			switch(et) {
			case GENERAL:
				pre.setString(1, item.getUser_fullname());
				pre.setString(2, item.getUser_birthday());
				pre.setString(3, item.getUser_mobilephone());
				pre.setString(4, item.getUser_homephone());
				pre.setString(5, item.getUser_officephone());
				pre.setString(6, item.getUser_email());
				pre.setString(7, item.getUser_address());
				pre.setString(8, item.getUser_jobarea());
				pre.setString(9, item.getUser_job());
				pre.setString(10, item.getUser_position());
				pre.setShort(11, item.getUser_applyyear());

				pre.setString(12, item.getUser_notes());
				pre.setString(13, item.getUser_roles());

				pre.setString(14, item.getUser_last_modified());

				pre.setByte(15, item.getUser_actions());
				pre.setInt(16, item.getUser_id());
				break;
			case SETTINGS:
				pre.setByte(1, item.getUser_permission());
				pre.setString(2, item.getUser_roles());
				pre.setInt(3, item.getUser_id());
				break;
			case PASS:
				pre.setString(1, item.getUser_pass());
				pre.setInt(2, item.getUser_id());
				break;
			case TRASH:
				pre.setString(1, item.getUser_last_modified());
				pre.setInt(2, item.getUser_id());
				break;
			case RESTORE:
				pre.setInt(1, item.getUser_id());
				break;
			}
			
			return this.edit(pre);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			// tro ve trang thai an toan cua ket noi
			try {
				this.con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		System.out.print(sql.toString());
		return false;
	}

	@Override
	public boolean delUser(UserObject item) {
		// TODO Auto-generated method stub
		if (!this.isEmptyUserObject(item)) {
			return false;
		}

		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM tbluser WHERE user_id=?");

		try {
			PreparedStatement pre = this.con.prepareStatement(sql.toString());
			pre.setInt(1, item.getUser_id());

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
		System.out.print(sql.toString());
		return false;
	}

	private boolean isEmptyUserObject(UserObject item) {
		boolean flag = true;

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT article_id FROM tblarticle WHERE article_author_name='").append(item.getUser_name())
				.append("' ;");
		sql.append("SELECT product_id FROM tblproduct WHERE product_manager_id=" + item.getUser_id() + "; ");
		sql.append("SELECT user_id FROM tbluser WHERE user_parent_id=" + item.getUser_id() + "; ");

		ArrayList<ResultSet> res = this.getMR(sql.toString());

		for (ResultSet rs : res) {
			if (rs != null) {
				try {
					if (rs.next()) {
						flag = false;
						break;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

//		res.forEach(rs -> {
//			if(rs!=null) {
//				try {
//					if(rs.next()) {
//						flag = false;
//					}
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});

		return flag;
	}

	private boolean isEmpty(Object item) {
		boolean flag = true;
		StringBuffer sql = new StringBuffer();

		if (item instanceof UserObject) {
			sql.append("SELECT article_id FROM tblarticle WHERE article_author_name='")
					.append(((UserObject) item).getUser_name()).append("' ;");
			sql.append("SELECT product_id FROM tblproduct WHERE product_manager_id=" + ((UserObject) item).getUser_id()
					+ "; ");
			sql.append("SELECT user_id FROM tbluser WHERE user_parent_id=" + ((UserObject) item).getUser_id() + "; ");
		} else if (item instanceof SectionObject) {
			sql.append("category_id FROM tblcategory_section_id = ").append(((SectionObject) item).getSection_id());
		}

		ArrayList<ResultSet> res = this.getMR(sql.toString());

		for (ResultSet rs : res) {
			if (rs != null) {
				try {
					if (rs.next()) {
						flag = false;
						break;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return flag;
	}

	@Override
	public ResultSet getUser(int id) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM tblUser WHERE (user_id = ?) AND (user_delete=0)";
		return this.get(sql, id);
	}

	@Override
	public ResultSet getUser(String username, String userpass) {
		// TODO Auto-generated method stub

		String sql = "SELECT * FROM tblUser WHERE (user_name = ?) AND (user_pass = md5(?)) AND (user_delete=0)";
		String sqlUpdateLogined = "UPDATE tblUser SET user_logined = user_logined+1 WHERE (user_name = ?) AND (user_pass = md5(?)) AND (user_delete=0)";
		ArrayList<String> sqls = new ArrayList<>();
		sqls.add(sql);
		sqls.add(sqlUpdateLogined);
		return this.get(sqls, username, userpass);
	}

	@Override
	public ArrayList<ResultSet> getUsers(Triplet<UserObject, Integer, Byte> infos, Pair<USER_SOFT, ORDER> so) {
		// TODO Auto-generated method stub

		// đối tượng lưu trữ thông tin lọc kết quả
		UserObject similar = infos.getValue0();

		// vị trí bắt đầu lấy bản ghi
		int at = infos.getValue1();

		// Số bản ghi được lấy trong một lần
		byte total = infos.getValue2();

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT * FROM tblUser ");
		sql.append(this.createConditions(similar));
//		System.out.print(this.createConditions(similar).toString());
		switch (so.getValue0()) {
		case NAME:
			sql.append("ORDER BY user_name ").append(so.getValue1().name());
			break;
		case FULLNAME:
			sql.append("ORDER BY user_fullname ").append(so.getValue1().name());
			break;
		default:
			sql.append("ORDER BY user_id DESC");
		}
		sql.append(" LIMIT " + at + ", " + total + ";");

		sql.append("SELECT COUNT(user_id) AS total FROM tblUser WHERE user_delete = 0;");
		
		System.out.print(sql.toString());

		return this.getMR(sql.toString());
	}

	public static void main(String[] args) {
		// tạo bộ quản lý kết nối
		ConnectionPool cp = new ConnectionPoolImpl();

		// đối tượng thực thi chức năng mức interface
		User u = new UserImpl(cp);

		// gia lap ADD
		// tạo đối tượng lưu chữ thông tin thêm mới
		UserObject nUser = new UserObject();
		nUser.setUser_name("adVanHoangDao123");
		nUser.setUser_created_date("06/06/2023");
		nUser.setUser_email("let911318@gmail.com");
		nUser.setUser_parent_id(56); // admin
		nUser.setUser_pass("12345678");

		nUser.setUser_fullname("Dao Van Hoang");
		nUser.setUser_address("TP Hai Duong");

		nUser.setUser_id(51);
		boolean result = u.editUser(nUser, USER_EDIT_TYPE.GENERAL);
		if (!result) {
			System.out.println("------------------ADD FAIL---------------------");
		}

		// lấy tập kết quả
		Triplet<UserObject, Integer, Byte> infos = new Triplet<>(null, 0, (byte) 30);

		ArrayList<ResultSet> res = u.getUsers(infos, new Pair<>(USER_SOFT.ID, ORDER.DESC));

		ResultSet rs = res.get(0);

		String row;

		if (rs != null) {
			try {
				while (rs.next()) {
					row = "ID: " + rs.getInt("user_id");
//					row += "\tNAME: " + rs.getString("user_name");
					row += "\tPARENT: " + rs.getInt("user_parent_id");
					row += "\tPASS: " + rs.getString("user_pass");
					System.out.println(row);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		rs = res.get(1);
		if (rs != null) {
			try {
				while (rs.next()) {
					System.out.println("Tong so tai khoan trong DB:" + rs.getShort("total"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private String createConditions(UserObject similar) {
		StringBuilder conds = new StringBuilder();
		
		if(similar != null) {
			byte permis = similar.getUser_permission(); // tài khoản đăng nhập truyền cho
			conds.append("(user_permission<=").append(permis).append(")");
			
			if(permis < 4) {
				int id = similar.getUser_id();
				if(id > 0) {
					conds.append(" AND ( (user_parent_id=").append(id).append(") OR (user_id=").append(id).append(") )");
				}
			}
			
			// tu khoa tim kiem
			String key = similar.getUser_name();
			if(key != null && !key.equalsIgnoreCase("")) {
				conds.append(" AND (");
				conds.append(" (user_name LIKE '%"+key+"%') OR");
				conds.append(" (user_fullname LIKE '%"+key+"%') OR");
				conds.append(" (user_address LIKE '%"+key+"%') OR");
				conds.append(" (user_email LIKE '%"+key+"%')");
				conds.append(" )");
			}
			
			// System.out.println(similar.isUser_delete());
			if(similar.isUser_delete()) {
				conds.append(" AND (user_delete=1)");
			} else {
				conds.append(" AND (user_delete=0)");
			}
		}
		
		if(!conds.toString().equalsIgnoreCase("")) {
			conds.insert(0, " WHERE ");
		}
		
		return conds.toString();
	}

}
