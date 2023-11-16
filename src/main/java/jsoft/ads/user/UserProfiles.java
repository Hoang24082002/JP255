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

import org.apache.tomcat.util.security.MD5Encoder;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.mysql.cj.protocol.x.SyncFlushDeflaterOutputStream;

import jsoft.objects.UserObject;
import jsoft.*;
import jsoft.library.ORDER;

/**
 * Servlet implementation class View
 */
@WebServlet("/user/profiles")
public class UserProfiles extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// định nghĩa kiểu nội dung xuất về trình khách
	private static final String CONTENT_TYPE = "text/html; charset=utf-8";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserProfiles() {
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

	protected void view(HttpServletRequest request, HttpServletResponse response, UserObject user)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// xác định kiểu nội dung xuất về trình khách
		response.setContentType(CONTENT_TYPE);

		// Tìm id của người sử dụng để cập nhật
		int id = jsoft.library.Utilities.getIntParam(request, "id");

		// tạo đối tượng thực hiện xuất nội dung
		PrintWriter out = response.getWriter();
		UserObject eUser = null;
		if (id > 0) {
			// tìm bộ quản lý kết nối
			ConnectionPool cp = (ConnectionPool) getServletContext().getAttribute("CPool");
			// tạo đối tượng thực thi chức năng
			UserControl uc = new UserControl(cp);
			if (cp == null) {
				getServletContext().setAttribute("CPool", cp);
			}

			eUser = uc.getUserObject(id);
			// trả về kết nối
			uc.releaseConnection();
		}

		// tham chiếu tìm header
		RequestDispatcher h = request.getRequestDispatcher("/header?pos=urlist");
		if (h != null) {
			h.include(request, response);
		}

		out.append("<main id=\"main\" class=\"main\">");

		RequestDispatcher error = request.getRequestDispatcher("/error");
		if (error != null) {
			error.include(request, response);
		}

		out.append("<div class=\"pagetitle d-flex\">");
		out.append("<h1>Danh sách người sử dụng</h1>");
		out.append("<nav class=\"ms-auto\">");
		out.append("<ol class=\"breadcrumb\">");
		out.append("<li class=\"breadcrumb-item\"><a href=\"/adv/view\"><i class=\"bi bi-house\"></i></a></li>");
		out.append("<li class=\"breadcrumb-item\">Người sử dụng</li>");
		out.append("<li class=\"breadcrumb-item active\">Cập nhật chi tiết</li>");
		out.append("</ol>");
		out.append("</nav>");
		out.append("</div><!-- End Page Title -->");

		out.append("<section class=\"section profile\">");
		out.append("<div class=\"row\">");

//		out.append("<div class=\"card\">");
//		out.append("<div class=\"card-body\">");

		out.append("<div class=\"col-xl-4\">");

		out.append("<div class=\"card\">");
		out.append("<div class=\"card-body profile-card pt-4 d-flex flex-column align-items-center\">");
		
		if(eUser!=null) {
			out.append("<img src=\"assets/img/profile-img.jpg\" alt=\"Profile\" class=\"rounded-circle\">");
			out.append("<h2>"+eUser.getUser_fullname()+"</h2>");
			out.append("<h3>"+eUser.getUser_name()+"</h3>");
			out.append("<div class=\"social-links mt-2\">");
			out.append("<a href=\"#\" class=\"twitter\"><i class=\"bi bi-twitter\"></i></a>");
			out.append("<a href=\"#\" class=\"facebook\"><i class=\"bi bi-facebook\"></i></a>");
			out.append("<a href=\"#\" class=\"instagram\"><i class=\"bi bi-instagram\"></i></a>");
			out.append("<a href=\"#\" class=\"linkedin\"><i class=\"bi bi-linkedin\"></i></a>");
			out.append("</div>");			
		}
		
		out.append("</div>");
		out.append("</div>");

		out.append("</div>"); // end cod-xl-4

		out.append("<div class=\"col-xl-8\">");

		out.append("<div class=\"card\">");
		out.append("<div class=\"card-body pt-3\">");
		out.append("<!-- Bordered Tabs -->");
		out.append("<ul class=\"nav nav-tabs nav-tabs-bordered\">");

		out.append("<li class=\"nav-item\">");
		out.append(
				"<button class=\"nav-link active\" data-bs-toggle=\"tab\" data-bs-target=\"#overview\"><i class=\"bi bi-info-square\"></i> Tổng quát</button>");
		out.append("</li>");

		out.append("<li class=\"nav-item\">");
		out.append(
				"<button class=\"nav-link\" data-bs-toggle=\"tab\" data-bs-target=\"#edit\"><i class=\"bi bi-pencil-square\"></i> Chỉnh sửa</button>");
		out.append("</li>");

		out.append("<li class=\"nav-item\">");
		out.append(
				"<button class=\"nav-link\" data-bs-toggle=\"tab\" data-bs-target=\"#settings\"><i class=\"bi bi-gear-fill\"></i> Cài đặt</button>");
		out.append("</li>");

		out.append("<li class=\"nav-item\">");
		out.append(
				"<button class=\"nav-link\" data-bs-toggle=\"tab\" data-bs-target=\"#change-password\"><i class=\"bi bi-unlock-fill\"></i> Đổi mật khẩu</button>");
		out.append("</li>");

		out.append("</ul>");
		out.append("<div class=\"tab-content pt-2\">");
		
		String summary = "", name = "", fullname="", dateOB="";
		String address="", email="", phone="", job="", jobarea="";
		short logined = 0;
		boolean isEdit = false;
		if(eUser!=null) {
			summary = eUser.getUser_notes();
			name = eUser.getUser_name();
			fullname = eUser.getUser_fullname();
			address = eUser.getUser_address();
			email = eUser.getUser_email();
			phone = eUser.getUser_homephone();
			job = eUser.getUser_job();
			jobarea = eUser.getUser_jobarea();
			logined = eUser.getUser_logined();
			dateOB = eUser.getUser_birthday();
			isEdit = true;
		}

		out.append("<div class=\"tab-pane fade show active profile-overview\" id=\"overview\">");
		out.append("<h5 class=\"card-title\">Tóm tắt</h5>");
		out.append(
				"<p class=\"small fst-italic\">"+summary+"</p>");

		out.append("<h5 class=\"card-title\">Chi tiết</h5>");

		out.append("<div class=\"row\">");
		out.append("<div class=\"col-lg-3 col-md-4 label \">Full Name</div>");
		out.append("<div class=\"col-lg-6 col-md-5\">"+fullname+"</div>");
		out.append("<div class=\"col-lg-3 col-md-3\">("+name+")</div>");
		out.append("</div>");

		out.append("<div class=\"row\">");
		out.append("<div class=\"col-lg-3 col-md-4 label\">Lần đăng nhập</div>");
		out.append("<div class=\"col-lg-9 col-md-8\">"+logined+"</div>");
		out.append("</div>");

		out.append("<div class=\"row\">");
		out.append("<div class=\"col-lg-3 col-md-4 label\">Nghề nghiệp</div>");
		out.append("<div class=\"col-lg-9 col-md-8\">"+job+"</div>");
		out.append("</div>");

		out.append("<div class=\"row\">");
		out.append("<div class=\"col-lg-3 col-md-4 label\">Lĩnh vực</div>");
		out.append("<div class=\"col-lg-9 col-md-8\">"+jobarea+"</div>");
		out.append("</div>");

		out.append("<div class=\"row\">");
		out.append("<div class=\"col-lg-3 col-md-4 label\">Địa chỉ</div>");
		out.append("<div class=\"col-lg-9 col-md-8\">"+address+"</div>");
		out.append("</div>");

		out.append("<div class=\"row\">");
		out.append("<div class=\"col-lg-3 col-md-4 label\">Điện thoại</div>");
		out.append("<div class=\"col-lg-9 col-md-8\">"+phone+"</div>");
		out.append("</div>");

		out.append("<div class=\"row\">");
		out.append("<div class=\"col-lg-3 col-md-4 label\">Hộp thư</div>");
		out.append("<div class=\"col-lg-9 col-md-8\">"+email+"</div>");
		out.append("</div>");

		out.append("</div>");

		out.append("<div class=\"tab-pane fade profile-edit pt-3\" id=\"edit\">");

		out.append("<!-- Profile Edit Form -->");
		out.append("<form method=\"post\" action=\"/adv/user/profiles\">");
		out.append("<div class=\"row mb-3 row align-items-center\">");
		out.append("<label for=\"profileImage\" class=\"col-md-3 col-lg-2 text-end\">Hình ảnh</label>");
		out.append("<div class=\"col-md-9 col-lg-10\">");
		out.append("<img src=\"assets/img/profile-img.jpg\" alt=\"Profile\">");
		out.append("<div class=\"pt-2\">");
		out.append(
				"<a href=\"#\" class=\"btn btn-primary btn-sm\" title=\"Upload new profile image\"><i class=\"bi bi-upload\"></i></a>");
		out.append(
				"<a href=\"#\" class=\"btn btn-danger btn-sm\" title=\"Remove my profile image\"><i class=\"bi bi-trash\"></i></a>");
		out.append("</div>");
		out.append("</div>");
		out.append("</div>");

		out.append("<div class=\"row mb-3 row align-items-center text-end\">");
		out.append("<label for=\"fullName\" class=\"col-md-3 col-lg-2 col-form-label\">Họ tên</label>");
		out.append("<div class=\"col-md-7 col-lg-8\">");
		out.append("<div class=\"input-group\">");
		out.append(
				"<input name=\"txtUserFullname\" type=\"text\" class=\"form-control\" id=\"fullName\" value=\""+fullname+"\">");
		out.append(
				"<input name=\"txtUserAlias\" type=\"text\" class=\"form-control\" id=\"alias\" readonly value=\""+fullname+"\">");
		out.append("</div>");
		out.append("</div>");
		out.append("<div class=\"col-md-2 col-lg-2\">");
		out.append(
				"<input name=\"name\" type=\"text\" class=\"form-control\" id=\"name\" disabled value=\""+name+"\">");
		out.append("</div>");
		out.append("</div>");

		out.append("<div class=\"row mb-3 row align-items-center text-end\">");
		out.append("<label for=\"notes\" class=\"col-md-3 col-lg-2 col-form-label\">Tóm tắt</label>");
		out.append("<div class=\"col-md-9 col-lg-10\">");
		out.append(
				"<textarea name=\"txtNotes\" class=\"form-control\" id=\"notes\" style=\"height: 100px\">"+summary+"</textarea>");
		out.append("</div>");
		out.append("</div>");

		out.append("<div class=\"row mb-3 row align-items-center text-end\">");
		out.append("<label for=\"date\" class=\"col-md-3 col-lg-2 col-form-label\">Ngày sinh</label>");
		out.append("<div class=\"col-md-3 col-lg-4\">");
		out.append(
				"<input name=\"txtBirthday\" type=\"date\" class=\"form-control\" id=\"date\" value=\""+dateOB+"\">");
		out.append("</div>");
		out.append("<label for=\"slcGender\" class=\"col-md-3 col-lg-2 col-form-label\">Giới tính</label>");
		out.append("<div class=\"col-md-3 col-lg-4\">");
		out.append("<select class=\"form-control\" id=\"slcGender\" name=\"slcGender\" >");
		out.append("<option value=\"\"> --- </option>");
		out.append("<option value=\"0\"> Nam </option>");
		out.append("<option value=\"1\"> Nữ </option>");
		out.append("</select>");
		out.append("</div>");
		out.append("</div>");

		out.append("<div class=\"row mb-3 row align-items-center text-end\">");
		out.append("<label for=\"Job\" class=\"col-md-3 col-lg-2 col-form-label\">Nghề nghiệp</label>");
		out.append("<div class=\"col-md-3 col-lg-4\">");
		out.append("<input name=\"txtJob\" type=\"text\" class=\"form-control\" id=\"Job\" value=\""+job+"\">");
		out.append("</div>");
		out.append("<label for=\"Country\" class=\"col-md-3 col-lg-2 col-form-label\">Lĩnh vực</label>");
		out.append("<div class=\"col-md-3 col-lg-4\">");
		out.append("<input name=\"txtJobarea\" type=\"text\" class=\"form-control\" id=\"Country\" value=\""+jobarea+"\">");
		out.append("</div>");
		out.append("</div>");

		out.append("<div class=\"row mb-3 row align-items-center text-end\">");
		out.append("<label for=\"Address\" class=\"col-md-3 col-lg-2 col-form-label\">Địa chỉ</label>");
		out.append("<div class=\"col-md-9 col-lg-10\">");
		out.append(
				"<input name=\"txtAddress\" type=\"text\" class=\"form-control\" id=\"Address\" value=\""+address+"\">");
		out.append("</div>");
		out.append("</div>");

		out.append("<div class=\"row mb-3 row align-items-center text-end\">");
		out.append("<label for=\"Phone\" class=\"col-md-3 col-lg-2 col-form-label\">Điện thoại</label>");
		out.append("<div class=\"col-md-9 col-lg-10\">");
		out.append("<div class=\"input-group\">");
		out.append(
				"<input name=\"txtPhone\" type=\"text\" class=\"form-control\" id=\"Phone\" value=\""+phone+"\" placeholder=\"Mobile phone\">");
		out.append(
				"<input name=\"txtHPhone\" type=\"text\" class=\"form-control\" id=\"hPhone\" value=\""+phone+"\" placeholder=\"Home phone\">");
		out.append(
				"<input name=\"txtOPhone\" type=\"text\" class=\"form-control\" id=\"oPhone\" value=\""+phone+"\" placeholder=\"Office phone\">");
		out.append("</div>"); //input group
		out.append("</div>");
		out.append("</div>");

		out.append("<div class=\"row mb-3 row align-items-center text-end\">");
		out.append("<label for=\"Email\" class=\"col-md-3 col-lg-2 col-form-label\">Hộp thư</label>");
		out.append("<div class=\"col-md-3 col-lg-4\">");
		out.append(
				"<input name=\"txtEmail\" type=\"email\" class=\"form-control\" id=\"Email\" value=\""+email+"\">");
		out.append("</div>");
		out.append("<label for=\"Facebook\" class=\"col-md-3 col-lg-2 col-form-label\">Facebook Profile</label>");
		out.append("<div class=\"col-md-3 col-lg-4\">");
		out.append(
				"<input name=\"facebook\" type=\"text\" class=\"form-control\" id=\"Facebook\" value=\"https://facebook.com/#\">");
		out.append("</div>");
		out.append("</div>");


		out.append("<div class=\"text-center\">");
		out.append("<button type=\"submit\" class=\"btn btn-primary\"><i class=\"bi bi-save\"></i> Lưu thay đổi</button>");
		out.append("</div>");
		
		// Truyền id theo cơ thế biến form ẩn để thực hiện edit
		if(isEdit) {
			out.append("<input type=\"hidden\" name=\"idForPost\" value=\""+id+"\" >");
			out.append("<input type=\"hidden\" name=\"act\" value=\"edit\" >");
		}
		
		out.append("</form><!-- End Profile Edit Form -->");

		out.append("</div>");

		out.append("<div class=\"tab-pane fade pt-3\" id=\"settings\">");
		out.append("<!-- Settings Form -->");
		out.append("<form>");

		out.append("<div class=\"row mb-3\">");
		out.append("<label for=\"fullName\" class=\"col-md-4 col-lg-3 col-form-label\">Email Notifications</label>");
		out.append("<div class=\"col-md-8 col-lg-9\">");
		out.append("<div class=\"form-check\">");
		out.append("<input class=\"form-check-input\" type=\"checkbox\" id=\"changesMade\" checked>");
		out.append("<label class=\"form-check-label\" for=\"changesMade\">");
		out.append("Changes made to your account");
		out.append("</label>");
		out.append("</div>");
		out.append("<div class=\"form-check\">");
		out.append("<input class=\"form-check-input\" type=\"checkbox\" id=\"newProducts\" checked>");
		out.append("<label class=\"form-check-label\" for=\"newProducts\">");
		out.append("Information on new products and services");
		out.append("</label>");
		out.append("</div>");
		out.append("<div class=\"form-check\">");
		out.append("<input class=\"form-check-input\" type=\"checkbox\" id=\"proOffers\">");
		out.append("<label class=\"form-check-label\" for=\"proOffers\">");
		out.append("Marketing and promo offers");
		out.append("</label>");
		out.append("</div>");
		out.append("<div class=\"form-check\">");
		out.append("<input class=\"form-check-input\" type=\"checkbox\" id=\"securityNotify\" checked disabled>");
		out.append("<label class=\"form-check-label\" for=\"securityNotify\">");
		out.append("Security alerts");
		out.append("</label>");
		out.append("</div>");
		out.append("</div>");
		out.append("</div>");

		out.append("<div class=\"text-center\">");
		out.append("<button type=\"submit\" class=\"btn btn-primary\">Save Changes</button>");
		out.append("</div>");
		out.append("</form><!-- End settings Form -->");

		out.append("</div>");

		out.append("<div class=\"tab-pane fade pt-3\" id=\"change-password\">");
		
		
		out.append("<!-- Change Password Form -->");
		out.append("<form method=\"post\" action=\"/adv/user/profiles\">");
		if(eUser.getUser_id() == user.getUser_id()) {
			out.append("<div class=\"row mb-3\">");
			out.append("<label for=\"currentPassword\" class=\"col-md-4 col-lg-3 col-form-label\">Mật khẩu hiện tại</label>");
			out.append("<div class=\"col-md-8 col-lg-9\">");
			out.append("<input name=\"txtpassword\" type=\"password\" class=\"form-control\" id=\"currentPassword\">");
			out.append("</div>");
			out.append("</div>");			
		} else {
			out.append("<div class=\"row mb-3\">");
			out.append("<label for=\"currentPassword\" class=\"col-md-4 col-lg-3 col-form-label\">Mật khẩu hiện tại</label>");
			out.append("<div class=\"col-md-8 col-lg-9\">");
			out.append("<input name=\"txtpassword\" type=\"password\" class=\"form-control\" id=\"currentPassword\" value=\"\" disabled>");
			out.append("</div>");
			out.append("</div>");
		}

		out.append("<div class=\"row mb-3\">");
		out.append("<label for=\"newPassword\" class=\"col-md-4 col-lg-3 col-form-label\">Mật khẩu mới</label>");
		out.append("<div class=\"col-md-8 col-lg-9\">");
		out.append("<input name=\"txtnewpassword\" type=\"password\" class=\"form-control\" id=\"newPassword\">");
		out.append("</div>");
		out.append("</div>");

		out.append("<div class=\"row mb-3\">");
		out.append("<label for=\"renewPassword\" class=\"col-md-4 col-lg-3 col-form-label\">Nhập lại mật khẩu mới</label>");
		out.append("<div class=\"col-md-8 col-lg-9\">");
		out.append("<input name=\"txtrenewpassword\" type=\"password\" class=\"form-control\" id=\"renewPassword\">");
		out.append("</div>");
		out.append("</div>");

		out.append("<div class=\"text-center\">");
		out.append("<button type=\"submit\" class=\"btn btn-primary\">Change Password</button>");
		out.append("</div>");
		
		// Truyền id theo cơ thế biến form ẩn để thực hiện edit
		if(isEdit) {
			out.append("<input type=\"hidden\" name=\"idForPost\" value=\""+id+"\" >");
			out.append("<input type=\"hidden\" name=\"act\" value=\"editPass\" >");
		}
		
		out.append("</form><!-- End Change Password Form -->");

		out.append("</div>");

		out.append("</div><!-- End Bordered Tabs -->");

		out.append("</div>");
		out.append("</div>");

		out.append("</div>");

//		out.append("</div>"); // end card-body
//		out.append("</div>"); // end card

		out.append("</div>");
		out.append("</section>");

		out.append("</main><!-- End #main -->");

		// tham chiếu tìm sidebar
		RequestDispatcher f = request.getRequestDispatcher("/footer");
		if (f != null) {
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
		UserObject user = (UserObject) request.getSession().getAttribute("userLogined");

		// thiết lập tập ký tự cần lấy
		request.setCharacterEncoding("utf-8");
		
		// lấy id của người sử dụng để chỉnh sửa
		int id = jsoft.library.Utilities.getIntParam(request, "idForPost");
		String act = request.getParameter("act");
		if(id>0) {
			if(act!=null && act.equalsIgnoreCase("edit")) {
				// lấy thông tin
				String fullname = request.getParameter("txtUserFullname");
				String notes = request.getParameter("txtNotes");
				String birthday = request.getParameter("txtBirthday");
				String job = request.getParameter("txtJob");
				String jobarea = request.getParameter("txtJobarea");
				String address = request.getParameter("txtAddress");
				String phone = request.getParameter("txtPhone");
				String email = request.getParameter("txtEmail");
				
				if (fullname != null && !fullname.equalsIgnoreCase("")
						&& email != null && !email.equalsIgnoreCase("") 
						&& phone != null && !phone.equalsIgnoreCase("") ) {

					// Tạo đối tượng UserObject
					UserObject eUser = new UserObject();
					eUser.setUser_id(id);
					eUser.setUser_fullname(jsoft.library.Utilities.encode(fullname));
					eUser.setUser_address(jsoft.library.Utilities.encode(address));
					eUser.setUser_parent_id(user.getUser_id()); // lay tu tai khoan dang nhap
					eUser.setUser_email(email);
					eUser.setUser_notes(jsoft.library.Utilities.encode(notes));
					eUser.setUser_homephone(phone);
					eUser.setUser_last_modified(jsoft.library.Utilities_date.getDate());
					eUser.setUser_job(jsoft.library.Utilities.encode(job));
					eUser.setUser_jobarea(jsoft.library.Utilities.encode(jobarea));
					eUser.setUser_birthday(birthday);
					
					
					ConnectionPool cp = (ConnectionPool) getServletContext().getAttribute("CPool");
					UserControl uc = new UserControl(cp);
					if (cp == null) {
						getServletContext().setAttribute("CPool", uc.getCP());
					}
					
					// thuc hien cap nhat
					boolean result = uc.editUser(eUser, USER_EDIT_TYPE.GENERAL);
					
					
					
					// tra ve ket noi
					uc.releaseConnection();
					
					//
					if (result) {
						response.sendRedirect("/adv/user/list");
					} else {
						response.sendRedirect("/adv/user/list?err=edit");
					}
					
				} else {
					response.sendRedirect("/adv/user/list?err=valueUpdate");
				}			
			} else {// Sua mat khau
								
				// laa thong tin
				String currentPassword = request.getParameter("txtpassword");
				String newPassword = request.getParameter("txtnewpassword");
				String renewPassword = request.getParameter("txtrenewpassword");
				
				if(id != user.getUser_id()) {
					if(newPassword != null && !newPassword.equalsIgnoreCase("")
							&& renewPassword != null && !renewPassword.equalsIgnoreCase("")
							&& jsoft.library.Utilities_text.checkValidPass(newPassword, renewPassword)
							) {
						UserObject eUser = new UserObject();
						eUser.setUser_id(id);
						eUser.setUser_pass(jsoft.library.Utilities.encode(newPassword));
						
						ConnectionPool cp = (ConnectionPool) getServletContext().getAttribute("CPool");
						UserControl uc = new UserControl(cp);
						if (cp == null) {
							getServletContext().setAttribute("CPool", uc.getCP());
						}
						
						// thuc hien cap nhat
						boolean result = uc.editUser(eUser, USER_EDIT_TYPE.PASS);
						
						// tra ve ket noi
						uc.releaseConnection();
						//
						if (result) {
							response.sendRedirect("/adv/user/list");
						} else {
							response.sendRedirect("/adv/user/list?err=edit");
						}
					} else {
						response.sendRedirect("/adv/user/list?err=valueUpdate");
					}					
				} else {					
					if(currentPassword != null && !currentPassword.equalsIgnoreCase("")
							&&newPassword != null && !newPassword.equalsIgnoreCase("")
							&& renewPassword != null && !renewPassword.equalsIgnoreCase("")
							&& jsoft.library.Utilities_text.checkValidPass(newPassword, renewPassword)
							&& jsoft.library.Utilities_text.checkValidPass(jsoft.library.Utilities.getMd5(currentPassword), jsoft.library.Utilities.decode(user.getUser_pass()))
							) {
						UserObject eUser = new UserObject();
						eUser.setUser_id(id);
						eUser.setUser_pass(jsoft.library.Utilities.encode(newPassword));
						
						ConnectionPool cp = (ConnectionPool) getServletContext().getAttribute("CPool");
						UserControl uc = new UserControl(cp);
						if (cp == null) {
							getServletContext().setAttribute("CPool", uc.getCP());
						}
						
						// thuc hien cap nhat
						boolean result = uc.editUser(eUser, USER_EDIT_TYPE.PASS);
						
						// tra ve ket noi
						uc.releaseConnection();
						//
						if (result) {
							response.sendRedirect("/adv/user/list");
						} else {
							response.sendRedirect("/adv/user/list?err=edit");
						}
					} else {
						response.sendRedirect("/adv/user/list?err=valueUpdate");
					}
				}
				
			}
		} else {
			// Không tồn tại id
			response.sendRedirect("/adv/user/list?err=profiles");
		}

	}

}
