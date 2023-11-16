package jsoft.ads.section;

import jsoft.*;
import jsoft.ads.user.UserLibrary;
import jsoft.library.Utilities;
import jsoft.objects.*;

import java.util.*;
import org.javatuples.*;

public class SectionLibrary {
	public static ArrayList<String> viewSection(ArrayList<SectionObject> items, SectionObject section, UserObject user,
			HashMap<Integer, String> manager_name, ArrayList<UserObject> datas) {

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

		if (section.isSection_delete()) {
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
			tmp.append("<td>" + item.getSection_created_date() + "</td>");
			tmp.append("<td>" + item.getSection_name() + "</td>");

//			System.out.println("----" + section.isSection_delete() + "----");

			if (section.isSection_delete()) {
				tmp.append("<td>" + item.getSection_last_modified() + "</td>");
				tmp.append(
						"<td class=\"align-middle\"><a href=\"/adv/section/dr?id="+item.getSection_id()+"&r\" class=\"btn btn-primary btn-sm\"><i class=\"bi bi-reply\"></i></a></td>");
				tmp.append(
						"<td class=\"align-middle\"><button class=\"btn btn-danger btn-sm\" data-bs-toggle=\"modal\" data-bs-target=\"#delSection"
								+ item.getSection_id() + "\"><i class=\"bi bi-trash\"></i></button></td>");
				tmp.append(SectionLibrary.viewDelSection(item));
				tmp.append("<th scope=\"row\">" + item.getSection_id() + "</th>");
			} else {
				tmp.append("<td>" + manager_name.get(item.getSection_manager_id()) + "</td>");
				tmp.append(
						"<td class=\"align-middle\"><a href=\"#\" class=\"btn btn-primary btn-sm\"><i class=\"bi bi-eye\"></i></a></td>");
				tmp.append("<td class=\"align-middle\"><a href=\"/adv/section/profiles?id=" + item.getSection_id()
						+ "\" class=\"btn btn-secondary btn-sm\"><i class=\"bi bi-pencil-square\"></i></a></td>");
				tmp.append(
						"<td class=\"align-middle\"><button class=\"btn btn-danger btn-sm\" data-bs-toggle=\"modal\" data-bs-target=\"#delSection"
								+ item.getSection_id() + "\"><i class=\"bi bi-trash\"></i></button></td>");
				tmp.append(SectionLibrary.viewDelSection(item));

				tmp.append("<th scope=\"row\">" + item.getSection_id() + "</th>");
			}

			tmp.append("</tr>");
		});

		tmp.append("</tbody>");
		tmp.append("</table>");

		tmp.append("</div>");
		tmp.append("</div>");

		view.add(tmp.toString());
//		System.out.println(section.getSection_manager_id());
		view.add(SectionLibrary.viewManagerOption(datas, section.getSection_manager_id()));

		return view;
	}

	public static StringBuilder viewDelSection(SectionObject item) {
		StringBuilder tmp = new StringBuilder();

		String title;

		if (item.isSection_delete()) {
			title = "Xóa vĩnh viễn";
		} else {
			title = "Xóa thành phần";
		}

		tmp.append("<div class=\"modal fade\" id=\"delSection" + item.getSection_id()
				+ "\" tabindex=\"-1\" aria-labelledby=\"SectionLabel" + item.getSection_id()
				+ "\" aria-hidden=\"true\">");
		tmp.append("<div class=\"modal-dialog modal-lg\">");
		tmp.append("<div class=\"modal-content\">");
		tmp.append("<div class=\"modal-header\">");
		tmp.append("<h1 class=\"modal-title fs-5\" id=\"SectionLabel" + item.getSection_id() + "\">" + title + "</h1>");
		tmp.append(
				"<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>");
		tmp.append("</div>");
		tmp.append("<div class=\"modal-body\">");

		if (item.isSection_delete()) {
			tmp.append("Bạn sẽ xóa vĩnh viễn thành phần <b>").append(item.getSection_name())
					.append("(" + item.getSection_name_en() + ")? </b> <br>");
			tmp.append("Thành phần không thể phục hồi được nữa.");
		} else {
			tmp.append("Bạn có chắc chắn xóa tài khoản <b>").append(item.getSection_name())
					.append("(" + item.getSection_name_en() + ")? </b> <br>");
			tmp.append("Hệ thống tạm thời lưu vào thùng rác, thành phần có thể phục hồi trong vòng 30 ngày.");
		}
		tmp.append("</div>");
		tmp.append("<div class=\"modal-footer\">");
		if (item.isSection_delete()) {
			tmp.append("<a href=\"/adv/section/dr?id=" + item.getSection_id() + "&pid="
					+ item.getSection_created_author_id() + "\" class=\"btn btn-danger\">Xóa vĩnh viễn</a>");
		} else {
			tmp.append("<a href=\"/adv/section/dr?id=" + item.getSection_id() + "&t&pid="
					+ item.getSection_created_author_id() + "\" class=\"btn btn-danger\">Xóa</a>"); // dr = delete +
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
//		System.out.println("SectionLibrary: " + selectedId);
//		users.forEach(i -> {
//			System.out.println(i.getUser_id() + " - ");
//		});
		users.forEach(item -> {
			if (item.getUser_id() == selectedId) {
				tmp.append("<option value=\"").append(item.getUser_id()).append("\" selected>");
			} else {
				tmp.append("<option value=\"").append(item.getUser_id()).append("\">");
			}
			tmp.append(Utilities.decode(item.getUser_fullname())).append("(").append(item.getUser_name()).append(")");
//			tmp.append("aaaaaa");
			tmp.append("</option>");
		});

		return tmp.toString();
	}
}
