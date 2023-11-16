package jsoft.ads.user;

import jsoft.*;
import jsoft.library.Utilities;
import jsoft.objects.*;

import java.util.*;
import org.javatuples.*;

public class UserLibrary {
	public static String viewUser(Pair<ArrayList<UserObject>, Short> datas, Triplet<UserObject, Integer, Byte> infos) {
		
		ArrayList<UserObject> items = datas.getValue0();
		int total = datas.getValue1();
		UserObject user = infos.getValue0();
		
		int page = infos.getValue1();
		byte totalPage = infos.getValue2();
		
		
		StringBuffer tmp = new StringBuffer();

		tmp.append("<div class=\"card\">");
		tmp.append("<div class=\"card-body\">");
//		tmp.append("<h5 class=\"card-title\">Table with stripped rows</h5>\n");

		tmp.append("<table class=\"table table-striped table-sm \">");
		tmp.append("<thead>");
		tmp.append("<tr>");
		tmp.append("<th scope=\"col\">STT</th>");
		tmp.append("<th scope=\"col\">Ngày tạo</th>");
		tmp.append("<th scope=\"col\">Tài khoản</th>");
		tmp.append("<th scope=\"col\">Họ tên đầy đủ</th>");
		
//		System.out.println("----" + user.isUser_delete() + "----");
		
		if (user.isUser_delete()) {
			tmp.append("<th scope=\"col\">Ngày xóa</th>");
			tmp.append("<th scope=\"col\" colspan=\"2\" class=\"text-center\">Thực hiện</th>");
			tmp.append("<th scope=\"col\">#</th>");
		} else {
			tmp.append("<th scope=\"col\">Hộp thư</th>");
			tmp.append("<th scope=\"col\">Lần đăng nhập</th>");
			tmp.append("<th scope=\"col\" colspan=\"3\" class=\"text-center\">Thực hiện</th>");
			tmp.append("<th scope=\"col\">#</th>");
		}
		tmp.append("</tr>");
		tmp.append("</thead>");

		tmp.append("<tbody>");

		items.forEach(item -> {
			tmp.append("<tr class=\"align-items-center\">");
			tmp.append("<th scope=\"row\" class=\"align-middle\">" + (items.indexOf(item) + 1) + "</th>");
			tmp.append("<td class=\"align-middle\">" + item.getUser_created_date() + "</td>");
			tmp.append("<td class=\"align-middle\">" + item.getUser_name() + "</td>");
			tmp.append("<td class=\"align-middle\">" + item.getUser_fullname() + "</td>");

			if (user.isUser_delete()) {
				tmp.append("<td class=\"align-middle\">" + item.getUser_last_modified() + "</td>");
				tmp.append(
						"<td class=\"align-middle\"><a href=\"/adv/user/dr?id="+item.getUser_id()+"&r\" class=\"btn btn-primary btn-sm\"><i class=\"bi bi-reply\"></i></a></td>");
				tmp.append(
						"<td class=\"align-middle\"><button class=\"btn btn-danger btn-sm\" data-bs-toggle=\"modal\" data-bs-target=\"#delUser"
								+ item.getUser_id() + "\"><i class=\"bi bi-trash\"></i></button></td>");
				tmp.append(UserLibrary.viewDelUser(item));
				tmp.append("<th scope=\"row\">" + item.getUser_id() + "</th>");

			} else {
				tmp.append("<td class=\"align-middle\">" + item.getUser_email() + "</td>");
				tmp.append("<td class=\"align-middle\">" + item.getUser_logined() + "</td>");
				tmp.append(
						"<td class=\"align-middle\"><a href=\"#\" class=\"btn btn-primary btn-sm\"><i class=\"bi bi-eye\"></i></a></td>");

				if (item.getUser_id() == user.getUser_id()) {
					tmp.append("<td class=\"align-middle\"><a href=\"/adv/user/profiles?id=" + item.getUser_id()
							+ "\" class=\"btn btn-secondary btn-sm\"><i class=\"bi bi-pencil-square\"></i></a></td>");
					tmp.append(
							"<td class=\"align-middle\"><a href=\"#\" class=\"btn btn-danger btn-sm disabled\"><i class=\"bi bi-trash\"></i></a></td>");

					tmp.append("<th scope=\"row\">" + item.getUser_id() + "</th>");
				} else {
					if (user.getUser_permission() >= 4) {
						tmp.append("<td class=\"align-middle\"><a href=\"/adv/user/profiles?id=" + item.getUser_id()
								+ "\" class=\"btn btn-secondary btn-sm\"><i class=\"bi bi-pencil-square\"></i></a></td>");
						tmp.append(
								"<td class=\"align-middle\"><button class=\"btn btn-danger btn-sm\" data-bs-toggle=\"modal\" data-bs-target=\"#delUser"
										+ item.getUser_id() + "\"><i class=\"bi bi-trash\"></i></button></td>");
						tmp.append(UserLibrary.viewDelUser(item));
						tmp.append("<th scope=\"row\">" + item.getUser_id() + "</th>");
					} else {
						// xac dinh quyen de them sua xoa
						if (item.getUser_parent_id() == user.getUser_id()) {
							tmp.append("<td class=\"align-middle\"><a href=\"/adv/user/profiles?id=" + item.getUser_id()
									+ "\" class=\"btn btn-secondary btn-sm\"><i class=\"bi bi-pencil-square\"></i></a></td>");
						} else {
							tmp.append(
									"<td class=\"align-middle\"><a href=\"#\" class=\"btn btn-secondary btn-sm disabled\" ><i class=\"bi bi-pencil-square\"></i></a></td>");
						}

						tmp.append(
								"<td class=\"align-middle\"><a href=\"#\" class=\"btn btn-danger btn-sm disabled\" ><i class=\"bi bi-trash\"></i></a></td>");

					}
				}
			}

			tmp.append("</tr>");
		});

		tmp.append("</tbody>");
		tmp.append("</table>");

		tmp.append("</div>");
		tmp.append("</div>");
		
		tmp.append(UserLibrary.getPaging("/adv/user/list", page, total, totalPage));
		
		tmp.append(UserLibrary.createChart(items));
		
		
		return tmp.toString();
	}

	public static StringBuilder viewDelUser(UserObject item) {
		StringBuilder tmp = new StringBuilder();
		
		String title;
//		System.out.println("--" + item.isUser_delete() + "--");
		if(item.isUser_delete()) {
			title = "Xóa vĩnh viễn";
		} else {
			title = "Xóa tài khoản";
		}

		tmp.append("<div class=\"modal fade\" id=\"delUser" + item.getUser_id()
				+ "\" tabindex=\"-1\" aria-labelledby=\"UserLabel" + item.getUser_id() + "\" aria-hidden=\"true\">");
		tmp.append("<div class=\"modal-dialog modal-lg\">");
		tmp.append("<div class=\"modal-content\">");
		tmp.append("<div class=\"modal-header\">");
		tmp.append("<h1 class=\"modal-title fs-5\" id=\"UserLabel" + item.getUser_id() + "\">"+title+"</h1>");
		tmp.append(
				"<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>");
		tmp.append("</div>");
		tmp.append("<div class=\"modal-body\">");
		
		//System.out.print(item.isUser_delete());
		
		if(item.isUser_delete()) {
			tmp.append("Bạn sẽ xóa vĩnh viễn tài khoản <b>").append(item.getUser_fullname())
			.append("(" + item.getUser_name() + ")? </b> <br>");
			tmp.append("Tài khoản không thể phục hồi được nữa.");
		} else {
			tmp.append("Bạn có chắc chắn xóa tài khoản <b>").append(item.getUser_fullname())
			.append("(" + item.getUser_name() + ")? </b> <br>");
			tmp.append("Hệ thống tạm thời lưu vào thùng rác, tài khoản có thể phục hồi trong vòng 30 ngày.");
		}
		tmp.append("</div>");
		tmp.append("<div class=\"modal-footer\">");
		if(item.isUser_delete()) {
			tmp.append("<a href=\"/adv/user/dr?id=" + item.getUser_id() + "&pid=" + item.getUser_parent_id()
			+ "\" class=\"btn btn-danger\">Xóa vĩnh viễn</a>");
		} else {
			tmp.append("<a href=\"/adv/user/dr?id=" + item.getUser_id() + "&t&pid=" + item.getUser_parent_id()
			+ "\" class=\"btn btn-danger\">Xóa</a>"); // dr = delete + restore
		}
		tmp.append("<button type=\"button\" class=\"btn btn-secondary\" data-bs-dismiss=\"modal\">Hủy</button>");
		tmp.append("</div>");
		tmp.append("</div>");
		tmp.append("</div>");
		tmp.append("</div>");							
		
		return tmp;
	}
	
	public static StringBuffer createChart(ArrayList<UserObject> items) {
		
		StringBuffer data = new StringBuffer();
		
		StringBuffer accounts = new StringBuffer();
		
		items.forEach(item -> {
			data.append(item.getUser_logined());
			accounts.append("'"+ Utilities.decode(item.getUser_fullname())).append(" (").append(item.getUser_name()).append(")'");
			
			if(items.indexOf(item) < items.size()) {
				data.append(",");
				accounts.append(",");
			}
		});
		
		StringBuffer tmp = new StringBuffer();
		
		tmp.append("<div class=\"card\">");
		tmp.append("<div class=\"card-body\">");
		tmp.append("<h5 class=\"card-title\">Biểu đồ đăng nhập hệ thống</h5>");

		tmp.append("<!-- Bar Chart -->");
		tmp.append("<div id=\"barChart\"></div>");

		tmp.append("<script>");
		tmp.append("document.addEventListener(\"DOMContentLoaded\", () => {");
		tmp.append("new ApexCharts(document.querySelector(\"#barChart\"), {");
		tmp.append("series: [{");
		tmp.append("data: ["+data.toString()+"]");
		tmp.append("}],");
		tmp.append("chart: {");
		tmp.append("type: 'bar',");
		if(items.size() <= 15 ) {
			tmp.append("height: 350,");
		} else if(items.size() <= 30) {
			tmp.append("height: 600,");
		} else {
			tmp.append("height: 750,");
		}
		tmp.append("},");
		tmp.append("plotOptions: {");
		tmp.append("bar: {");
		tmp.append("borderRadius: 4,");
		tmp.append("horizontal: true,");
		tmp.append("}");
		tmp.append("},");
		tmp.append("dataLabels: {");
		tmp.append("enabled: false");
		tmp.append("},");
		tmp.append("xaxis: {");
		tmp.append("categories: ["+accounts.toString()+"],");
		tmp.append("}, ");
		tmp.append("yaxis: {");
		tmp.append("show: true,");
		tmp.append("labels: {");
		tmp.append("show: true,");
		tmp.append("align: 'right',");
		tmp.append("minWidth: 0,");
		tmp.append("maxWidth: 300,");
		tmp.append("style: {color:[], fontSize: '15px', fontFamily: 'Arial, san-serif', fontWeight: 400, cssClass:'apexcharts-yaxis-label'},");
		tmp.append("},");
		tmp.append("}");
		tmp.append("}).render();");
		tmp.append("});");
		tmp.append("</script>");
		tmp.append("<!-- End Bar Chart -->");

		tmp.append("</div>");
		tmp.append("</div>");
		
		
		return tmp;
	}
	
	public static String getPaging(String url, int page, int total, byte totalperpage) {
		// tinh toan tong so trang
		int countPage = total / totalperpage;
		if(total % totalperpage != 0) {
			countPage++;
		}
		
		if(page > countPage || page <= 0) {
			page = 1;
		}
		
		
		
		StringBuffer tmp = new StringBuffer();
		
		tmp.append("<nav aria-label=\"...\">");
		tmp.append("<ul class=\"pagination justify-content-center\">");
		tmp.append("<li class=\"page-item\"><a class=\"page-link\" href=\""+url+"\"><span aria-hidden=\"true\">&laquo;</span></a></li>");
		
		// left 
		String leftCurrent = "";
		int count = 0;
		for(int i = page - 1; i > 0; i--) {
			leftCurrent = "<li class=\"page-item\"><a class=\"page-link\" href=\""+url+"?page="+i+"\">"+i+"</a></li>" + leftCurrent;
			if(++count >= 2) {
				break;
			}
		}
		if(page >= 4) {		
			leftCurrent = "<li class=\"page-item disabled\"><a class=\"page-link\" href=\"#\">...</a></li>" + leftCurrent;
		}
		tmp.append(leftCurrent);
		
		tmp.append("<li class=\"page-item active\" aria-current=\"page\"><a class=\"page-link\" href=\"#\">"+page+"</a></li>");
		
		// right
		String rightCurrent ="";
		count = 0;
		for(int i = page + 1; i <= countPage; i++) {
			rightCurrent += "<li class=\"page-item\"><a class=\"page-link\" href=\""+url+"?page="+i+"\">"+i+"</a></li>";
			if(++count >= 2) {
				break;
			}
		}
		if(countPage > page + 4) {
			rightCurrent += "<li class=\"page-item disabled\"><a class=\"page-link\" href=\"#\">...</a></li>";
		}
		tmp.append(rightCurrent);
		
		tmp.append("<li class=\"page-item\"><a class=\"page-link\" href=\""+url+"?page="+countPage+"\" tabindex=\"-1\" aria-disabled=\"true\" ><span aria-hidden=\"true\">&raquo;</span></a></li>");
		tmp.append("</ul>");
		tmp.append("</nav>");
		
		return tmp.toString();
	}
}
