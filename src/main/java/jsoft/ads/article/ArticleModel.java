package jsoft.ads.article;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Quintet;

import jsoft.ConnectionPool;
import jsoft.objects.ArticleObject;
import jsoft.objects.CategoryObject;
import jsoft.objects.SectionObject;
import jsoft.objects.UserObject;
import jsoft.ads.article.*;
import jsoft.ads.category.CategoryControl;
import jsoft.ads.section.SECTION_EDIT_TYPE;
import jsoft.library.ORDER;


public class ArticleModel {
	private Article a;

	public ArticleModel(ConnectionPool cp) {
		this.a = new AritcleImpl(cp);
	}

	public ConnectionPool getCP() {
		return this.a.getCP();
	}

	public void releaseConnection() {
		this.a.releaseConnection();
	}
	
//	----------------------------------
	public boolean addArticle(ArticleObject item) {
		return this.a.addArticle(item);
	}

	public boolean editArticle(ArticleObject item, ARTICLE_EDIT_TYPE et) {
		return this.a.editArticle(item, et);
	}

	public boolean delArticle(ArticleObject item) {
		return this.a.delArticle(item);
	}
//	----------------------------------
	
	public ArticleObject getArticleObject(int id) {
		ArticleObject item = null;

		ResultSet rs = this.a.getArticle(id);
		if (rs != null) {
			try {
				if (rs.next()) {
					item = new ArticleObject();
					item.setArticle_id(rs.getInt("article_id"));
					item.setArticle_title(rs.getString("article_title"));
					item.setArticle_summary(rs.getString("article_summary"));
					item.setArticle_content(rs.getString("article_content"));
					item.setArticle_image(rs.getString("article_image"));
					item.setArticle_created_date(rs.getString("article_created_date"));
					item.setArticle_last_modified(rs.getString("article_last_modified"));
					item.setArticle_author_name(rs.getString("article_author_name"));
					item.setArticle_tag(rs.getString("article_tag"));

					item.setCategory_id(rs.getShort("category_id"));
					item.setCategory_name(rs.getString("category_name"));

					item.setSection_id(rs.getShort("section_id"));
					item.setSection_name(rs.getString("section_name"));

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return item;
	}
	
	public Quartet<ArrayList<ArticleObject>, Short, ArrayList<SectionObject>, ArrayList<CategoryObject>> getArticles (Quintet<ArticleObject, Short, Byte, UserObject, SectionObject> infos,  Pair<ARTICLE_SOFT, ORDER> so) {
		
		// danh sach article
		ArrayList<ArticleObject> itemArticles = new ArrayList<>();
		
		// tong so ban ghi
		short total = 0;
		
		// danh sach section
		ArrayList<SectionObject> itemSections = new ArrayList<>();
		
		// danh sach category theo section
		ArrayList<CategoryObject> itemCategories = new ArrayList<>();
		
		// goi ben ArticleImpl
		ArrayList<ResultSet> res = this.a.getArticles(infos, so);
		
		// rs - danh sach article
		ResultSet rs = res.get(0);
		ArticleObject itemArticle = null;
		if(rs!=null) {
			try {
				while(rs.next()) {
					itemArticle = new ArticleObject();
					
					itemArticle.setArticle_title(rs.getString("article_title"));
					itemArticle.setArticle_summary(rs.getString("article_summary"));
					itemArticle.setArticle_content(rs.getString("article_content"));
					itemArticle.setArticle_created_date(rs.getString("article_created_date"));
					itemArticle.setArticle_author_name(rs.getString("article_author_name"));
					itemArticle.setArticle_isfee(rs.getBoolean("article_isfee"));
					
					itemArticles.add(itemArticle);
					
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// rs - tong so ban ghi
		rs = res.get(1);
		if (rs != null) {
			try {
				if (rs.next()) {
					total = rs.getShort("total");
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// rs - danh sach section
		rs = res.get(2);
		SectionObject itemSection = null;
		if(rs != null) {
			try {
				while(rs.next()) {
					itemSection = new SectionObject();
					
					itemSection.setSection_id(rs.getShort("section_id"));
					itemSection.setSection_name(rs.getString("section_name"));
					
					itemSections.add(itemSection);
					
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// rs - danh sach category theo section
		rs = res.get(3);
		CategoryObject itemCategory= null;
		if(rs != null) {
			try {
				while(rs.next()) {
					itemCategory = new CategoryObject();
					
					itemCategory.setCategory_id(rs.getShort("category_id"));
					itemCategory.setCategory_name(rs.getString("category_name"));
					
					itemCategories.add(itemCategory);
					
				}
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return new Quartet<>(itemArticles, total, itemSections, itemCategories);
	}
}
