package jsoft.ads.category;

import java.util.ArrayList;
import java.util.HashMap;

import jsoft.ads.section.SectionLibrary;
import jsoft.library.Utilities;
import jsoft.objects.CategoryObject;
import jsoft.objects.SectionObject;
import jsoft.objects.UserObject;

public class CategoryLibrary {
	public static ArrayList<String> viewCategory(	ArrayList<CategoryObject> items, 
													CategoryObject category, 
													UserObject user,
													HashMap<Integer, String> manager_name, 
													ArrayList<UserObject> userManagers,
													ArrayList<SectionObject> sectionList	) {

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

		if (category.isCategory_delete()) {
			tmp.append("<th scope=\"col\">Ngày xóa</th>");
			tmp.append("<th scope=\"col\" colspan=\"2\" class=\"text-center\">Thực hiện</th>");
			tmp.append("<th scope=\"col\">#</th>");
		} else {
			tmp.append("<th scope=\"col\">Người quản lý</th>");
			tmp.append("<th scope=\"col\" colspan=\"3\">Thực hiện</th>");
			tmp.append("<th scope=\"col\">#</th>");
		}
		tmp.append("</tr>");
		tmp.append("</thead>");

		tmp.append("<tbody>");

		items.forEach(item -> {
			tmp.append("<tr>");
			tmp.append("<th scope=\"row\">" + (items.indexOf(item) + 1) + "</th>");
			tmp.append("<td>" + item.getCategory_created_date() + "</td>");
			tmp.append("<td>" + item.getCategory_name() + "</td>");

			if (category.isCategory_delete()) {
				tmp.append("<td>" + item.getCategory_last_modified() + "</td>");
				tmp.append(
						"<td class=\"align-middle\"><a href=\"/adv/category/dr?id="+item.getCategory_id()+"&r\" class=\"btn btn-primary btn-sm\"><i class=\"bi bi-reply\"></i></a></td>");
				tmp.append(
						"<td class=\"align-middle\"><button class=\"btn btn-danger btn-sm\" data-bs-toggle=\"modal\" data-bs-target=\"#delCategory"
								+ item.getCategory_id() + "\"><i class=\"bi bi-trash\"></i></button></td>");
				tmp.append(CategoryLibrary.viewDelCategory(item));
				tmp.append("<th scope=\"row\">" + item.getCategory_id() + "</th>");
			} else {
				tmp.append("<td>" + manager_name.get(item.getCategory_manager_id()) + "</td>");
				tmp.append(
						"<td class=\"align-middle\"><a href=\"#\" class=\"btn btn-primary btn-sm\"><i class=\"bi bi-eye\"></i></a></td>");
				tmp.append("<td class=\"align-middle\"><a href=\"/adv/category/profiles?id=" + item.getCategory_id()
						+ "\" class=\"btn btn-secondary btn-sm\"><i class=\"bi bi-pencil-square\"></i></a></td>");
				tmp.append(
						"<td class=\"align-middle\"><button class=\"btn btn-danger btn-sm\" data-bs-toggle=\"modal\" data-bs-target=\"#delCategory"
								+ item.getCategory_id() + "\"><i class=\"bi bi-trash\"></i></button></td>");
				tmp.append(CategoryLibrary.viewDelCategory(item));

				tmp.append("<th scope=\"row\">" + item.getCategory_id() + "</th>");
			}

			tmp.append("</tr>");
		});

		tmp.append("</tbody>");
		tmp.append("</table>");

		tmp.append("</div>");
		tmp.append("</div>");

		// danh sach danh muc 0
		view.add(tmp.toString());
		
		// danh sach option User manager 1
		view.add(CategoryLibrary.viewManagerOption(userManagers, category.getCategory_manager_id()));
		
		// danh sach chuyen muc 2
		view.add(CategoryLibrary.viewSectionOption(sectionList, category.getCategory_section_id()));

		return view;
	}
	
	public static StringBuilder viewDelCategory(CategoryObject item) {
		StringBuilder tmp = new StringBuilder();

		String title;

		if (item.isCategory_delete()) {
			title = "Xóa vĩnh viễn";
		} else {
			title = "Xóa thành phần";
		}

		tmp.append("<div class=\"modal fade\" id=\"delCategory" + item.getCategory_id()
				+ "\" tabindex=\"-1\" aria-labelledby=\"CategoryLabel" + item.getCategory_id()
				+ "\" aria-hidden=\"true\">");
		tmp.append("<div class=\"modal-dialog modal-lg\">");
		tmp.append("<div class=\"modal-content\">");
		tmp.append("<div class=\"modal-header\">");
		tmp.append("<h1 class=\"modal-title fs-5\" id=\"CategoryLabel" + item.getCategory_id() + "\">" + title + "</h1>");
		tmp.append(
				"<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>");
		tmp.append("</div>");
		tmp.append("<div class=\"modal-body\">");

		if (item.isCategory_delete()) {
			tmp.append("Bạn sẽ xóa vĩnh viễn danh mục <b>").append(item.getCategory_name())
					.append("(" + item.getCategory_name_en() + ")? </b> <br>");
			tmp.append("Danh mục không thể phục hồi được nữa.");
		} else {
			tmp.append("Bạn có chắc chắn xóa danh mục <b>").append(item.getCategory_name())
					.append("(" + item.getCategory_name_en() + ")? </b> <br>");
			tmp.append("Hệ thống tạm thời lưu vào thùng rác, danh mục có thể phục hồi trong vòng 30 ngày.");
		}
		tmp.append("</div>");
		tmp.append("<div class=\"modal-footer\">");
		if (item.isCategory_delete()) {
			tmp.append("<a href=\"/adv/category/dr?id=" + item.getCategory_id() + "&pid="
					+ item.getCategory_created_author_id() + "\" class=\"btn btn-danger\">Xóa vĩnh viễn</a>");
		} else {
			tmp.append("<a href=\"/adv/category/dr?id=" + item.getCategory_id() + "&t&pid="
					+ item.getCategory_created_author_id() + "\" class=\"btn btn-danger\">Xóa</a>"); // dr = delete +
																									// restore
		}
		tmp.append("<button type=\"button\" class=\"btn btn-secondary\" data-bs-dismiss=\"modal\">Hủy</button>");
		tmp.append("</div>");
		tmp.append("</div>");
		tmp.append("</div>");
		tmp.append("</div>");

		return tmp;
	}

	public static String viewManagerOption(ArrayList<UserObject> users, int selectedId) {
		StringBuilder tmp = new StringBuilder();
//		System.out.println("CateLib - selectUserManager = " + selectedId);
		users.forEach(item -> {
			if (item.getUser_id() == selectedId) {
				tmp.append("<option value=\"").append(item.getUser_id()).append("\" selected>");
			} else {
				tmp.append("<option value=\"").append(item.getUser_id()).append("\">");
			}
			tmp.append(Utilities.decode(item.getUser_fullname())).append("(").append(item.getUser_name()).append(")");
			tmp.append("</option>");
		});

		return tmp.toString();
	}
	
	public static String viewSectionOption(ArrayList<SectionObject> sections, int selectedId) {
		StringBuilder tmp = new StringBuilder();
//		System.out.println("CateLib - section = " + selectedId);
		sections.forEach(item -> {
			if (item.getSection_id() == selectedId) {
				tmp.append("<option value=\"").append(item.getSection_id()).append("\" selected>");
			} else {
				tmp.append("<option value=\"").append(item.getSection_id()).append("\">");
			}
			tmp.append(Utilities.decode(item.getSection_name()));
			tmp.append("</option>");
		});

		return tmp.toString();
	}
}
