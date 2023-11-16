package jsoft.ads.article;

import java.util.ArrayList;

import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.javatuples.Quintet;

import jsoft.ConnectionPool;
import jsoft.ads.section.SECTION_EDIT_TYPE;
import jsoft.ads.section.SectionModel;
import jsoft.library.ORDER;
import jsoft.objects.ArticleObject;
import jsoft.objects.CategoryObject;
import jsoft.objects.SectionObject;
import jsoft.objects.UserObject;

public class ArticleControl {
	private ArticleModel am;

	public ArticleControl(ConnectionPool cp) {
		this.am = new ArticleModel(cp);
	}

	public ConnectionPool getCP() {
		return this.am.getCP();
	}

	public void releaseConnection() {
		this.am.releaseConnection();
	}
	
//	------------------------------------------
	public boolean addArticle(ArticleObject item) {
		return this.am.addArticle(item);
	}

	public boolean editArticle(ArticleObject item, ARTICLE_EDIT_TYPE et) {
		return this.am.editArticle(item, et);
	}

	public boolean delArticle(ArticleObject item) {
		return this.am.delArticle(item);
	}

//	---------------------------------------------
	public SectionObject getArticleObject(short id) {
		return this.am.getArticleObject(id);
	}
	
	public ArrayList<String> viewArticle(Quintet<ArticleObject, Short, Byte, UserObject, SectionObject> infos,  Pair<ARTICLE_SOFT, ORDER> so, UserObject user) {
		
		Quartet<ArrayList<ArticleObject>, Short, ArrayList<SectionObject>, ArrayList<CategoryObject>> datas = this.am.getArticles(infos, so);
		
		return ArticleLibrary.viewArticle(infos.getValue0(), datas.getValue0(), datas.getValue1(), datas.getValue2(), datas.getValue3());
	}
}
