package jsoft.ads.article;

import java.util.ArrayList;
import java.util.HashMap;

import jsoft.ads.section.SectionLibrary;
import jsoft.objects.ArticleObject;
import jsoft.objects.CategoryObject;
import jsoft.objects.SectionObject;
import jsoft.objects.UserObject;

public class ArticleLibrary {
	public static ArrayList<String> viewArticle(ArticleObject article, ArrayList<ArticleObject> iteamArticles, Short total, ArrayList<SectionObject> iteamSections, ArrayList<CategoryObject> itemCategories) {

		ArrayList<String> view = new ArrayList<>();

		StringBuffer tmp = new StringBuffer();

		tmp.append("<div class=\"card\">");
		tmp.append("<div class=\"card-body\">");
//		tmp.append("<h5 class=\"card-title\">Table with stripped rows</h5>");

		tmp.append("<table class=\"table table-striped\">");
		tmp.append("<thead>");
		tmp.append("<tr>");
		tmp.append("<th scope=\"col\">STT</th>");
		tmp.append("<th scope=\"col\">Ngày tạo</th>");
		tmp.append("<th scope=\"col\">Tên</th>");

		if (article.isArticle_delete()) {
			tmp.append("<th scope=\"col\">Ngày xóa</th>");
			tmp.append("<th scope=\"col\" colspan=\"2\" class=\"text-center\">Thực hiện</th>");
			tmp.append("<th scope=\"col\">#</th>");
		} else {
			tmp.append("<th scope=\"col\">Người tạo</th>");
			tmp.append("<th scope=\"col\" colspan=\"3\">Thực hiện</th>");
			tmp.append("<th scope=\"col\">#</th>");
		}
		tmp.append("</tr>");
		tmp.append("</thead>");

		tmp.append("<tbody>");

		iteamArticles.forEach(item -> {
			tmp.append("<tr>");
			tmp.append("<th scope=\"row\">" + (iteamArticles.indexOf(item) + 1) + "</th>");
			tmp.append("<td>" + item.getArticle_created_date() + "</td>");
			tmp.append("<td>" + item.getArticle_title() + "</td>");

//			System.out.println("----" + section.isSection_delete() + "----");

			if (article.isArticle_delete()) {
				tmp.append("<td>" + item.getArticle_last_modified() + "</td>");
				tmp.append(
						"<td class=\"align-middle\"><a href=\"/adv/article/dr?id="+item.getArticle_id()+"&r\" class=\"btn btn-primary btn-sm\"><i class=\"bi bi-reply\"></i></a></td>");
				tmp.append(
						"<td class=\"align-middle\"><button class=\"btn btn-danger btn-sm\" data-bs-toggle=\"modal\" data-bs-target=\"#delArticle"
								+ item.getArticle_id() + "\"><i class=\"bi bi-trash\"></i></button></td>");
//				tmp.append(SectionLibrary.viewDelSection(item));
				tmp.append("<th scope=\"row\">" + item.getArticle_id() + "</th>");
			} else {
				tmp.append("<td>" + item.getArticle_author_name() + "</td>");
				tmp.append(
						"<td class=\"align-middle\"><a href=\"#\" class=\"btn btn-primary btn-sm\"><i class=\"bi bi-eye\"></i></a></td>");
				tmp.append("<td class=\"align-middle\"><a href=\"/adv/section/profiles?id=" + item.getArticle_id()
						+ "\" class=\"btn btn-secondary btn-sm\"><i class=\"bi bi-pencil-square\"></i></a></td>");
				tmp.append(
						"<td class=\"align-middle\"><button class=\"btn btn-danger btn-sm\" data-bs-toggle=\"modal\" data-bs-target=\"#delArticle"
								+ item.getArticle_id() + "\"><i class=\"bi bi-trash\"></i></button></td>");
//				tmp.append(SectionLibrary.viewDelSection(item));

				tmp.append("<th scope=\"row\">" + item.getArticle_id() + "</th>");
			}

			tmp.append("</tr>");
		});

		tmp.append("</tbody>");
		tmp.append("</table>");

		tmp.append("</div>");
		tmp.append("</div>");

		view.add(tmp.toString());
//		System.out.println(section.getSection_manager_id());
//		view.add(SectionLibrary.viewManagerOption(datas, section.getSection_manager_id()));

		return view;
	}
}
