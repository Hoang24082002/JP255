package jsoft.ads.main;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class error
 */
@WebServlet("/error")
public class error extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public error() {
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

		PrintWriter out = response.getWriter();

		String err = request.getParameter("err");
		if (err != null) {

			out.append("<div class=\"toast-container position-fixed top-0 start-50 translate-middle-x p-3\">");
			out.append(
					"<div id=\"liveToast\" class=\"toast\" role=\"alert\" aria-live=\"assertive\" aria-atomic=\"true\">");
			out.append("<div class=\"toast-header\">");
			//out.append("<img src=\"...\" class=\"rounded me-2\" alt=\"...\">");
			out.append("<strong class=\"me-auto\">Có lỗi</strong>");
			out.append("<small>Mô đun người sử dụng</small>");
			out.append(
					"<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"toast\" aria-label=\"Close\"></button>");
			out.append("</div>");
			out.append("<div class=\"toast-body\">");
			switch(err) {
			case "add":
				out.append("Lỗi khi thêm mới");
				break;
			case "valueadd":
				out.append("Lỗi khi lấy giá trị thêm mới");
				break;
			case "edit":
				out.append("Lỗi chỉnh sửa thông tin");
				break;
			case "valueUpdate":
				out.append("Lỗi khi cập nhật");
				break;
			case "acclogin":
				out.append("Không thể xóa được tài khoản đang đăng nhập");
				break;
			case "del":
				out.append("Lỗi khi xóa");
				break;
				default:
					out.append("Có lỗi, vui lòng kiểm tra lại");
			}
			out.append("</div>");
			out.append("</div>");
			out.append("</div>");
			
			
			// script
			out.append("<script language=\"javascript\" >");
			out.append("const viewToast = document.getElementById('liveToast');");
			out.append("const toast = new bootstrap.Toast(viewToast);");
			out.append("toast.show();");
			out.append("</script>");

		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
