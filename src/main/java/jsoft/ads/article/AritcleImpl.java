package jsoft.ads.article;

import java.sql.Array;
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
import jsoft.library.ORDER;
import jsoft.objects.ArticleObject;
import jsoft.objects.SectionObject;
import jsoft.objects.UserObject;

public class AritcleImpl extends BasicImpl implements Article {

	public AritcleImpl(ConnectionPool cp) {
		super(cp, "Article");
	}

	@Override
	public boolean addArticle(ArticleObject item) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("INSERT INTO tblArticle(");
		sql.append("article_title, article_summary, article_content, article_created_date, ");
		sql.append("article_image, article_category_id, article_section_id, article_visited, ");
		sql.append("article_author_name, article_enable, article_url_link, article_tag, ");
		sql.append("article_fee, article_isfee, ");
		sql.append("article_delete, article_author_permission, ");
		sql.append("article_source, article_forhome");
		sql.append(")");
		sql.append("VALUE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
		try {
			PreparedStatement pre = this.con.prepareStatement(sql.toString());
			pre.setString(1, item.getArticle_title());
			pre.setString(2, item.getArticle_summary());
			pre.setString(3, item.getArticle_content());
			pre.setString(4, item.getArticle_created_date());
			pre.setString(5, item.getArticle_image());
			pre.setShort(6, item.getArticle_category_id());
			pre.setShort(7, item.getArticle_section_id());
			pre.setShort(8, item.getArticle_visited());
			pre.setString(9, item.getArticle_author_name());
			pre.setBoolean(10, item.isArticle_enable());
			pre.setString(11, item.getArticle_url_link());
			pre.setString(12, item.getArticle_tag());
			pre.setInt(13, item.getArticle_fee());
			pre.setBoolean(14, item.isArticle_isfee());
			pre.setBoolean(15, item.isArticle_delete());
			pre.setByte(16, item.getArticle_author_permission());
			pre.setString(17, item.getArticle_source());
			pre.setBoolean(18, item.isArticle_forhome());
			
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
	public boolean editArticle(ArticleObject item, ARTICLE_EDIT_TYPE et) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE tblarticle SET ");
		
		switch (et) {
		case GENERAL:
			sql.append("article_title=?, article_summary=?, article_content=?, article_image=?, article_category_id=?, ");
			sql.append("article_section_id=?, article_author_name=?, article_enable=?, article_url_link=?, article_tag=?, ");
			sql.append("article_fee=?, article_isfee=?, article_modified_author_name=?, article_source=?, article_forhome=? ");
			break;
		case TRASH:
			sql.append("article_delete=1, article_last_modified=? ");
			break;
		case RESTORE:
			sql.append("article_delete=0, article_restored_date=?");
			break;
		}
		
		sql.append(" WHERE article_id=?");
		
		try {
			PreparedStatement pre = this.con.prepareStatement(sql.toString());
			
			switch (et) {
			case GENERAL:
				pre.setString(1, item.getArticle_title());
				pre.setString(2, item.getArticle_summary());
				pre.setString(3, item.getArticle_content());
				pre.setString(4, item.getArticle_image());
				pre.setShort(5, item.getArticle_category_id());
				pre.setShort(6, item.getArticle_section_id());
				pre.setString(7, item.getArticle_author_name());
				pre.setBoolean(8, item.isArticle_enable());
				pre.setString(9, item.getArticle_url_link());
				pre.setString(10, item.getArticle_tag());
				pre.setInt(11, item.getArticle_fee());
				pre.setBoolean(12, item.isArticle_isfee());
				pre.setString(13, item.getArticle_modified_author_name());
				pre.setString(14, item.getArticle_source());
				pre.setBoolean(15, item.isArticle_forhome());
				
				pre.setInt(16, item.getArticle_id());
				break;
			case TRASH:
				pre.setString(1, item.getArticle_last_modified());
				pre.setInt(2, item.getArticle_id());
				break;
			case RESTORE:
				pre.setString(1, item.getArticle_restored_date());
				pre.setInt(2, item.getArticle_id());
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
	public boolean delArticle(ArticleObject item) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM tblarticle WHERE article_id=? ");
		
		try {
			PreparedStatement pre = this.con.prepareStatement(sql.toString());
			pre.setInt(1, item.getArticle_id());
			return this.del(pre);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
//	private boolean isEmptyArticleObject(ArticleObject item) {
//		boolean flag = true;
//		
//		return flag;
//	}

	@Override
	public ResultSet getArticle(int id) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM tblarticle ");
		sql.append("LEFT JOIN tblcategory on article_category_id = category_id ");
		sql.append("LEFT JOIN tblsection on category_section_id = section_id ");
		sql.append("WHERE (article_id=?) AND (article_delete=0) AND (article_enable=1)");
		return this.get(sql.toString(), id);
	}

	@Override
	public ArrayList<ResultSet> getArticles(Quintet<ArticleObject, Short, Byte, UserObject, SectionObject> infos,  Pair<ARTICLE_SOFT, ORDER> so) {
		// TODO Auto-generated method stub
		
		// đối tượng lưu trữ thông tin lọc kết quả
		ArticleObject similar = infos.getValue0();
		
		// vị trí bắt đầu bản ghi
		int at = infos.getValue1();
		
		// số bản ghi đc lấy
		byte total = infos.getValue2();
		
		// tai khoan dang nhap
		UserObject user = infos.getValue3();
		
		// thanh phan khi select
		SectionObject section = infos.getValue4();
		
		StringBuffer sql = new StringBuffer();
		
		// danh sach bai viet 0
		sql.append("SELECT * FROM tblArticle as s ");
		switch (so.getValue0()) {
		case NAME:
			sql.append("ORDER BY s.article_title ").append(so.getValue1().name());
			break;
		default:
			sql.append("ORDER BY s.article_id DESC ");
		}
		sql.append("LIMIT " + at + ", " + total + "; ");
		
		
		// tong so bai viet 1
		sql.append("SELECT COUNT(article_id) AS total FROM tblArticle; ");
		
		// danh sach section 2
		sql.append("SELECT * FROM tblsection ORDER BY section_id; ");
		
		// danh sach category theo section 3
		sql.append("SELECT * FROM tblcategory WHERE ").append("category_section_id = ").append(section.getSection_id()).append(" ORDER BY category_id; ");
		
		System.out.println("ArticleImpl - "+ sql.toString());
		return this.getMR(sql.toString());
	}
	
	private String createConditions(SectionObject similar) {
		StringBuilder conds = new StringBuilder();
		
		if(similar != null) {

			// System.out.println(similar.isUser_delete());
			if(similar.isSection_delete()) {
				conds.append("(article_delete=1) ");
			} else {
				conds.append("(article_delete=0) ");
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
//		Article a = new AritcleImpl(cp);
//		
//		Triplet<ArticleObject, Integer, Byte> infos = new Triplet<>(null, 0, (byte) 15);
//		
//		ArrayList<ResultSet> res = a.getArticles(infos);
//		
//		ResultSet rs = res.get(0);
//		
//		String row;
//		
//		if(rs != null) {
//			try {
//				while(rs.next()) {
//					row = "ID: " + rs.getInt("article_id");
//					row += "\tTITLE: " + rs.getString("article_title");
//					System.out.println(row);
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		rs = res.get(1);
//		if(rs != null) {
//			try {
//				while(rs.next()) {
//					System.out.println("Tong so bai bao trong DB: " + rs.getShort("total"));
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//	}
}

