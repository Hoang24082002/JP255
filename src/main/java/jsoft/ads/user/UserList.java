package jsoft.ads.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import jsoft.objects.UserObject;
import jsoft.*;
import jsoft.library.ORDER;
import jsoft.library.Utilities;

/**
 * Servlet implementation class View
 */
@WebServlet("/user/list")
public class UserList extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// định nghĩa kiểu nội dung xuất về trình khách
	private static final String CONTENT_TYPE = "text/html; charset=utf-8";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserList() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		// tìm thông tin đăng nhập
		UserObject user = (UserObject) request.getSession().getAttribute("userLogined");

		if (user != null) {
			view(request, response, user);
		} else {
			response.sendRedirect("/adv/user/login");
		}

	}

	protected void view(HttpServletRequest request, HttpServletResponse response, UserObject user) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// xác định kiểu nội dung xuất về trình khách
		response.setContentType(CONTENT_TYPE);

		// tạo đối tượng thực hiện xuất nội dung
		PrintWriter out = response.getWriter();
		
		// tìm bộ quản lý kết nối
		ConnectionPool cp = (ConnectionPool)getServletContext().getAttribute("CPool");
		
		if(cp ==null) {
			getServletContext().setAttribute("CPool", cp);
		}
		
		// lay tu khoa tim kiem
		String key = request.getParameter("key");
		String saveKey = (key!=null && !key.equalsIgnoreCase("")) ? key.trim() : "";
		
		// tạo đối tượng thực thi chức năng
		UserControl uc = new UserControl(cp);
		
		UserObject similar = new UserObject();
		similar.setUser_id(user.getUser_id());
		similar.setUser_permission(user.getUser_permission());
		similar.setUser_name(saveKey);
		
		// tim tham so xac dinh loai danh sach
		String trash = request.getParameter("trash");
		String titleTypeList, pos;
		if(trash ==null) {
			similar.setUser_delete(false);
			pos = "urlist";
			titleTypeList = "Danh sách người sử dụng";
		} else {
			similar.setUser_delete(true);
			pos = "urtrash";
			titleTypeList = "Danh sách người sử dụng bị xóa";
		}
		
		int page = Utilities.getIntParam(request, "page");
		if(page<1) {
			page = 0;
		}
		
		// lấy cấu trúc
		Triplet<UserObject, Integer, Byte> infos = new Triplet<>(similar, page, (byte) 20);

		ArrayList<String> viewList = uc.viewUser(infos, new Pair<>(USER_SOFT.ID, ORDER.DESC));
		
		// trả về kết nối
		uc.releaseConnection();
		
		// tham chiếu tìm header
		RequestDispatcher h = request.getRequestDispatcher("/header?pos=" + pos);
		if(h != null) {
			h.include(request, response);
		}
		
		out.append("<main id=\"main\" class=\"main\">");
		
		RequestDispatcher error = request.getRequestDispatcher("/error");
		if(error!=null) {
			error.include(request, response);
		}
		
		out.append("<div class=\"pagetitle d-flex\">");
		out.append("<h1>"+titleTypeList+"</h1>");
		out.append("<nav class=\"ms-auto\">");
		out.append("<ol class=\"breadcrumb\">");
		out.append("<li class=\"breadcrumb-item\"><a href=\"/adv/view\"><i class=\"bi bi-house\"></i></a></li>");
		out.append("<li class=\"breadcrumb-item\">Người sử dụng</li>");
		out.append("<li class=\"breadcrumb-item active\">Danh sách</li>");
		out.append("</ol>");
		out.append("</nav>");
		out.append("</div><!-- End Page Title -->");
		
		out.append("<section class=\"section\">");
		out.append("<div class=\"row\">");

		
		out.append("<div class=\"col-lg-12\">");
		
//		out.append("<div class=\"card\">");
//		out.append("<div class=\"card-body\">");
		if(trash == null) {
			out.append("<button type=\"button\" class=\"btn btn-primary btn-sm my-2\" data-bs-toggle=\"modal\" data-bs-target=\"#addUser\">");
			out.append("<i class=\"bi bi-person-plus\"></i> Thêm mới");
			out.append("</button>");
			
			out.append("<div class=\"modal fade\" id=\"addUser\" data-bs-backdrop=\"static\" data-bs-keyboard=\"false\" tabindex=\"-1\" aria-labelledby=\"staticBackdropLabel\" aria-hidden=\"true\">");
			out.append("<div class=\"modal-dialog modal-lg\">");
			
			out.append("<form method=\"post\" action=\"/adv/user/list\" class=\"needs-validation\" novalidate>");
			out.append("<div class=\"modal-content\">");
			out.append("<div class=\"modal-header\">");
			out.append("<h1 class=\"modal-title fs-5\" id=\"staticBackdropLabel\"><i class=\"bi bi-person-plus\"></i>Thêm mới người sử dụng</h1>");
			out.append("<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>");
			out.append("</div>");
			out.append("<div class=\"modal-body\">");
			
			out.append("<div class=\"row\">");
			out.append("<div class=\"col-lg-6\">");
			out.append("<label for=\"username\" class=\"form-label\">Tên tài khoản</label>");
			out.append("<input type=\"text\" class=\"form-control\" id=\"username\" name=\"txtUsername\" required >");
			out.append("<div class=\"invalid-feedback\">Hãy nhập vào tên của tài khoản</div>");
			out.append("</div>");
			
			out.append("<div class=\"col-lg-6\">");
			out.append("<label for=\"userfullname\" class=\"form-label\">Tên đầy đủ</label>");
			out.append("<input type=\"email\" class=\"form-control\" id=\"userfullname\" name=\"txtUserfullname\" required >");
			out.append("</div>");
			out.append("</div>");
			
			out.append("<div class=\"row\">");
			out.append("<div class=\"col-lg-6\">");
			out.append("<label for=\"userpass\" class=\"form-label\">Mật khẩu</label>");
			out.append("<input type=\"password\" class=\"form-control\" id=\"userpass\" name=\"txtUserpass\" required >");
			out.append("<div class=\"invalid-feedback\">Nhập mật khẩu cho tài khoản</div>");
			out.append("</div>");
			
			out.append("<div class=\"col-lg-6\">");
			out.append("<label for=\"userpass2\" class=\"form-label\">Xác nhận mật khẩu</label>");
			out.append("<input type=\"password\" class=\"form-control\" id=\"userpass2\" name=\"txtUserpass2\" required >");
			out.append("<div class=\"invalid-feedback\">Nhập lại mật khẩu cho tài khoản</div>");
			out.append("</div>");
			out.append("</div>");
			
			out.append("<div class=\"row\">");
			out.append("<div class=\"col-lg-6\">");
			out.append("<label for=\"useremail\" class=\"form-label\">Hộp thư</label>");
			out.append("<input type=\"email\" class=\"form-control\" id=\"useremail\" name=\"txtUseremail\" required >");
			out.append("<div class=\"invalid-feedback\" class=\"form-text\">Nhập hộp thư cho tài khoản</div>");
			out.append("</div>");
			
			out.append("<div class=\"col-lg-6\">");
			out.append("<label for=\"userphone\" class=\"form-label\">Điện thoại</label>");
			out.append("<input type=\"text\" class=\"form-control\" id=\"userphone\" name=\"txtUserphone\" required >");
			out.append("<div class=\"invalid-feedback\">Nhập điện thoại cho tài khoản</div>");
			out.append("</div>");
			out.append("</div>");
			
			out.append("<div class=\"row mb-3\">");
			out.append("<div class=\"col-lg-6\">");
			out.append("<label for=\"userpermis\" class=\"form-label\">Quyền thực thi</label>");
			out.append("<select class=\"form-select\" id=\"userpermis\" name=\"slcPermis\" required>");
			out.append("<option value=\"1\">Thành viên</option>");
			out.append("<option value=\"2\">Tác giả</option>");
			out.append("<option value=\"3\">Quản lý</option>");
			out.append("<option value=\"4\">Quản trị</option>");
			out.append("<option value=\"5\">Quản trị cấp cao</option>");
			
			out.append("</select>");
			out.append("<div class=\"invalid-feedback\">Nhập hộp thư cho tài khoản</div>");
			out.append("</div>");
			out.append("</div>");
			
			out.append("</div>");
			out.append("<div class=\"modal-footer\">");
			out.append("<button type=\"submit\" class=\"btn btn-primary\"><i class=\"bi bi-person-plus\"></i>Thêm mới</button>");
			out.append("<button type=\"button\" class=\"btn btn-secondary\" data-bs-dismiss=\"modal\"><i class=\"bi bi-x-lg\"></i>Thoát</button>");
			
			out.append("</div>");
			out.append("</div>"); //modal-content
			out.append("</form>");
			out.append("</div>");
			out.append("</div>");
		} 
		out.append(viewList.get(0));
		
//		out.append("<p>This is an examle page with no contrnt. You can use it as a starter for your custom pages.</p>");
		
//		out.append("</div>"); // end card-body
//		out.append("</div>"); // end card
		
		out.append("</div>");
		out.append("</div>");
		
		out.append("<div class=\"row\">");
		out.append("<div class=\"col-lg-12\">");
		// chart
		out.append("</div>");
		out.append("</div>");
		out.append("</section>");
		
		out.append("</main><!-- End #main -->");
		
		// tham chiếu tìm sidebar
		RequestDispatcher f = request.getRequestDispatcher("/footer");
		if(f != null) {
			f.include(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		UserObject user = (UserObject)request.getSession().getAttribute("userLogined");
		
		// thiết lập tập ký tự cần lấy
		request.setCharacterEncoding("utf-8");
		
		// lấy thông tin
		String name = request.getParameter("txtUsername");
		String pass1 = request.getParameter("txtUserpass");
		String pass2 = request.getParameter("txtUserpass2");
		String email = request.getParameter("txtUseremail");
		String phone = request.getParameter("txtUserphone");
		byte permis = jsoft.library.Utilities.getByteParam(request, "slcPermis");
		
		if(name != null && !name.equalsIgnoreCase("") 
				&& jsoft.library.Utilities_text.checkValidPass(pass1, pass2)
				&& email != null && !email.equalsIgnoreCase("")
				&& phone != null && !phone.equalsIgnoreCase("")
				&& permis > 0) {
			// lấy tiếp thông tin
			String fullname = request.getParameter("txtUserfullname");
			// Tạo đối tượng UserObject
			UserObject nUser = new UserObject();
			nUser.setUser_name(name);
			nUser.setUser_fullname(jsoft.library.Utilities.encode(fullname));
			nUser.setUser_permission(permis);
			nUser.setUser_pass(pass1);
			nUser.setUser_parent_id(user.getUser_id()); // lay tu tai khoan dang nhap
			nUser.setUser_email(email);
			nUser.setUser_homephone(phone);
			nUser.setUser_created_date(jsoft.library.Utilities_date.getDate());
			
			//
			ConnectionPool cp = (ConnectionPool)getServletContext().getAttribute("CPool");
			UserControl uc = new UserControl(cp);
			if(cp==null) {
				getServletContext().setAttribute("CPool", uc.getCP());
			}
			
			// thuc hien them moi
			boolean result = uc.addUser(nUser);
			
			// tra ve ket noi
			uc.releaseConnection();
			
			//
			if(result) {
				response.sendRedirect("/adv/user/list");
			} else {
				response.sendRedirect("/adv/user/list?err=add");
			}
			
		} else {
			String key = request.getParameter("keyword");
			response.sendRedirect("/adv/user/list?key=" + key);
		}
	}

}
