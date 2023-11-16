package jsoft.ads.article;

import jsoft.ShareControl;
import jsoft.ads.section.SECTION_SOFT;
import jsoft.library.ORDER;
import jsoft.objects.*;
import java.sql.*;
import java.util.ArrayList;

import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Quintet;
import org.javatuples.Triplet;

public interface Article extends ShareControl{
	// các chức năng cập nhật
	public boolean addArticle(ArticleObject item);

	public boolean editArticle(ArticleObject item, ARTICLE_EDIT_TYPE et);

	public boolean delArticle(ArticleObject item);

	// các chức năng lấy dữ liệu
	public ResultSet getArticle(int id);

//	public ResultSet getArticles(ArticleObject similar, int at, byte total);
	public ArrayList<ResultSet> getArticles(Quintet<ArticleObject, Short, Byte, UserObject, SectionObject> infos, Pair<ARTICLE_SOFT, ORDER> so);
}
