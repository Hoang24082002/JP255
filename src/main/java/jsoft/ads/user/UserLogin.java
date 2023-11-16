package jsoft.ads.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jsoft.ConnectionPool;
import jsoft.objects.UserObject;

/**
 * Servlet implementation class UserLogin
 */
@WebServlet("/user/login")
public class UserLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// định nghĩa kiểu nội dung xuất về trình khách
	private static final String CONTENT_TYPE = "text/html; charset=utf-8";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserLogin() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Thường đc dùng để cung cấp một giao diện (GUI - cấu trúc HTML) <br>
	 * Đc gọi trong 2 trường hợp:<br>
	 * - Thông qua URL / URI <br>
	 * - Thông qua sự kiện của form (method="get")<br>
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 * @param request  - Lưu trữ các yêu cầu xử lý, các dữ liệu được gửi lên bởi
	 *                 Client
	 * @param response - Lưu trữ các đáp ứng dữ liệu sẽ được trả về Client
	 * 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		// xác định kiểu nội dung xuất về trình khách
		response.setContentType(CONTENT_TYPE);

		// tạo đối tượng thực hiện xuất nội dung
		PrintWriter out = response.getWriter();
		out.append("<!doctype html>");
		out.append("<html lang=\"en\">");
		out.append("<head>");
		out.append("<meta charset=\"utf-8\">");
		out.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
		out.append("<title>Login V3</title>");
		out.append("<link href=\"/adv/adcss/all.min.css\" rel=\"stylesheet\">");
		out.append("<link href=\"/adv/adcss/bootstrap.min.css\" rel=\"stylesheet\">");
		out.append("<link href=\"/adv/adcss/main.css\" rel=\"stylesheet\">");
		out.append("<script language=\"javascript\" src=\"/adv/adjavascript/loginV3.js\"></script>");
		out.append("</head>");
		out.append("<body>");
		out.append("<div class=\"container-xxl\">");
		out.append("<div class=\"row\">");
		out.append(
				"<div class=\"col-xxl-6 offset-xxl-3\"> <!-- offset-3: ko có nội dung <=> col-3: ko có nội dung ( đỡ phải tạo div col-3) -->");

		// tìm tham số báo lỗi nếu có
		String error = request.getParameter("err");
		if (error != null) {
			out.append("<div class=\"alert border-secondary alert-dismissible fade show\" role=\"alert\">");

			switch (error) {
			case "param":
				out.append("Tham số lấy giá trị không chính xác");
				break;
			case "value":
				out.append("Không tồn tại giá trị cho tài khoản");
				break;
			case "notok":
				out.append("Đăng nhập không thành công");
				break;
			default:
				out.append("Có lỗi trong quá trình đăng nhập");
			}
			out.append(
					"<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button>");
			out.append("</div>");
		}

		out.append("<div class=\"loginTitle text-bg-primary py-3 mt-5\">");
		out.append(
				"<h3 class=\"text-center fw-bold text-uppercase\"><i class=\"fa-solid fa-user-tie space\"></i>&nbsp;Login</h3>");
		out.append("</div>");
		out.append("</div>");
		out.append("</div>");
		out.append("<div class=\"row\">");
		out.append("<div class=\"col-xxl-6 offset-xxl-3\">");
		out.append("<div class=\"loginForm text-bg-light py-2\">");
		out.append("<form class=\"px-3\" method=\"post\">");
		out.append("<div class=\"row py-2\">");
		out.append("<div class=\"col-sm-4 text-end fs-5\" >Username</div>");
		out.append("<div class=\"col-sm-6\">");
		out.append(
				"<input onKeyup=\"checkValiLogin()\" type=\"text\" class=\"form-control\" name=\"txtName\" id=\"name\" />");
		out.append("<div id=\"errName\"></div>");
		out.append("</div>");
		out.append("</div>");
		out.append("<div class=\"row py-2\">");
		out.append("<div class=\"col-sm-4 text-end fs-5\">Password</div>");
		out.append("<div class=\"col-sm-6\">");
		out.append(
				"<input onKeyup=\"checkValiLogin()\" type=\"password\" class=\"form-control\" name=\"txtPass\" id=\"pass\" />");
		out.append("<div id=\"errPass\"></div>");
		out.append("</div>");
		out.append("</div>");
		out.append("<div class=\"row py-2\" >");
		out.append("<div class=\"col-sm-6 offset-sm-4 fs-5\">");
		out.append("<input type=\"checkbox\" class=\"form-check-input\" id=\"chkSave\"/>");
		out.append("<label for=\"chkSave\" class=\"form-check-lable\">");
		out.append("Save account on this PC?");
		out.append("</label>");
		out.append("</div>");
		out.append("</div>");
		out.append("<div class=\"row py-2\">");
		out.append("<div class=\"col-sm-12 text-center fs-5\">");
		out.append(
				"<a href=\"#\" class=\"text-decoration-none\"><i class=\"fa-solid fa-key\"></i>&nbsp;Forget Password?</a>&nbsp;&nbsp;|&nbsp;&nbsp;");
		out.append(
				"<a href=\"#\" class=\"text-decoration-none\"><i class=\"fa-solid fa-circle-question\"></i>&nbsp;Help!</a>	");
		out.append("</div>");
		out.append("</div>");
		out.append("<div class=\"row py-3\">");
		out.append("<div class=\"col-sm-12 text-center fs-5\">");
		out.append(
				"<button type=\"submit\" class=\"btn btn-primary fw-semibold\" ><i class=\"fa-solid fa-right-to-bracket\"></i>&nbsp;Login</button>&nbsp;");
		out.append(
				"<button type=\"button\" class=\"btn btn-secondary fw-semibold\" ><i class=\"fa-regular fa-circle-xmark\"></i>&nbsp;Exit</button>");
		out.append("</div>");
		out.append("</div>");
		out.append("<div class=\"row py-3\">");
		out.append("<div class=\"col-sm-11 text-end fs-6\">");
		out.append(
				"<a href=\"\" class=\"text-decoration-none\"><i class=\"fa-solid fa-language\"></i>&nbsp;Vietnamese</a>");
		out.append("</div>");
		out.append("</div>");
		out.append("</form>");
		out.append("</div>");
		out.append("</div>");
		out.append("</div>");
		out.append("</div>");
		out.append("<script src=\"/adv/adjavascript/bootstrap.bundle.min.js\"></script>");
		out.append("</body>");
		out.append("</html>");
	}

	/**
	 * Thường được dùng để xử lý dữ liệu do doGet chuyển cho<br>
	 * Được gọi trong sự kiện của from ( method="post" )<br>
	 * 
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// doGet(request, response);
		String username = request.getParameter("txtName");
		String userpass = request.getParameter("txtPass");
		if (username != null && userpass != null) {
			username = username.trim();
			userpass = userpass.trim();

			if (!username.equalsIgnoreCase("") && !userpass.equalsIgnoreCase("")) {
				// Tham chiếu ngữ cảnh ứng dụng
				ServletContext application = getServletConfig().getServletContext();

				// Tìm bộ quản lý kết nối trong không gian ngữ cảnh
				ConnectionPool cp = (ConnectionPool) application.getAttribute("CPool");

				// Tạo đối tượng thực thi chúc năng
				UserControl uc = new UserControl(cp);
				if (cp == null) {
					application.setAttribute("CPool", uc.getCP());
				}

				// Thực hiện đăng nhập
				UserObject user = uc.getUserObject(username, userpass);

				// Trả về kết nối
				uc.releaseConnection();

				if (user != null) {
					// Tham chiếu phiên làm việc
					HttpSession session = request.getSession(true);

					// Đưa thông tin đăng nhập vào phiên
					session.setAttribute("userLogined", user);

					// Trở về giao diện chính
					response.sendRedirect("/adv/view");

				} else {
					response.sendRedirect("/adv/user/login?err=notok");
				}

			} else {
				response.sendRedirect("/adv/user/login?err=value");
			}

		} else {
			response.sendRedirect("/adv/user/login?err=param");

		}
	}

}
