package jsoft.ads.category;

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
import org.javatuples.Quartet;
import org.javatuples.Quintet;

import jsoft.ConnectionPool;
import jsoft.ads.category.*;
import jsoft.library.ORDER;
import jsoft.objects.CategoryObject;
import jsoft.objects.SectionObject;
import jsoft.objects.UserObject;

/**
 * Servlet implementation class CategoryList
 */
@WebServlet("/category/list")
public class CategoryList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// định nghĩa kiểu nội dung xuất về trình khách
	private static final String CONTENT_TYPE = "text/html; charset=utf-8";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CategoryList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

		// xac dinh kieu noi dung xuat ve trinh khach
		response.setContentType(CONTENT_TYPE);

		// tao doi tuong thuc thi xuat noi dung
		PrintWriter out = response.getWriter();

		// tim bo quan ly ket noi
		ConnectionPool cp = (ConnectionPool) getServletContext().getAttribute("CPool");
		if (cp == null) {
			getServletContext().setAttribute("CPool", cp);
		}

		// lay tu khoa tim kiem
//		String key = request.getParameter("key");
//		String saveKey = (key != null && !key.equalsIgnoreCase("")) ? key.trim() : "";

		// tao doi tuong thuc thi chuc nang
		CategoryControl cc = new CategoryControl(cp);

		CategoryObject similar = new CategoryObject();
		similar.setCategory_created_author_id(user.getUser_id());

		// tham so xac dinh loai danh sach
		String trash = request.getParameter("trash");
		String titleList, pos;
		if (trash == null) {
			similar.setCategory_delete(false);
			pos = "arlist";
			titleList = "Danh sách thể loại";
		} else {
			similar.setCategory_delete(true);
			pos = "artrash";
			titleList = "Danh sách thể loại bị xóa";
		}

		// lay cau truc
		Quartet<CategoryObject, Short, Byte, UserObject> infos = new Quartet<>(similar, (short) 0, (byte) 100, user);
		ArrayList<String> viewList = cc.viewCategory(infos, new Pair<>(CATEGORY_SOFT.ID, ORDER.DESC), user);

		// tra ve ket not
		cc.releaseConnection();

		// tham chiếu tìm header
		RequestDispatcher h = request.getRequestDispatcher("/header?pos=" + pos);
		if (h != null) {
			h.include(request, response);
		}

		out.append("<main id=\"main\" class=\"main\">");

		RequestDispatcher error = request.getRequestDispatcher("/error");
		if (error != null) {
			error.include(request, response);
		}

		out.append("<div class=\"pagetitle d-flex\">");
		out.append("<h1>" + titleList + "</h1>");
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

//			out.append("<div class=\"card\">");
//			out.append("<div class=\"card-body\">");
		if (trash == null) {
			out.append("<div class=\"d-flex justify-content-start\">");

			out.append(
					"<button type=\"button\" class=\"btn btn-primary btn-sm my-2\" data-bs-toggle=\"modal\" data-bs-target=\"#addCategory\">");
			out.append("<i class=\"bi bi-person-plus\"></i> Thêm mới");
			out.append("</button>");

			out.append("<button type=\"button\" class=\"btn btn-primary btn-sm my-2 ms-3\">");
			out.append("<a href=\"/adv/category/list?trash\" class=\"text-light\">");
			out.append("<i class=\"bi bi-trash3\"></i><span>Thùng rác</span>");
			out.append("</a>");
			out.append("</button>");

			out.append("</div>");

			out.append(
					"<div class=\"modal fade\" id=\"addCategory\" data-bs-backdrop=\"static\" data-bs-keyboard=\"false\" tabindex=\"-1\" aria-labelledby=\"staticBackdropLabel\" aria-hidden=\"true\">");
			out.append("<div class=\"modal-dialog modal-lg\">");

			out.append("<form method=\"post\" action=\"/adv/category/list\" class=\"needs-validation\" novalidate>");
			out.append("<div class=\"modal-content\">");
			out.append("<div class=\"modal-header\">");
			out.append(
					"<h1 class=\"modal-title fs-5\" id=\"staticBackdropLabel\"><i class=\"bi bi-person-plus\"></i>Thêm mới chuyên mục</h1>");
			out.append(
					"<button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>");
			out.append("</div>");
			out.append("<div class=\"modal-body\">");

			out.append("<div class=\"row mb-3\">");
			out.append("<div class=\"col-lg-6\">");
			out.append("<label for=\"categoryname\" class=\"form-label\">Tên thể loại</label>");
			out.append(
					"<input type=\"text\" class=\"form-control\" id=\"categoryname\" name=\"txtCategoryName\" required >");
			out.append("<div class=\"invalid-feedback\">Hãy nhập vào tên của thành phần</div>");
			out.append("</div>");

			out.append("<div class=\"col-lg-6\">");
			out.append("<label for=\"categorynameen\" class=\"form-label\">Tên tiếng anh thể loại</label>");
			out.append(
					"<input type=\"email\" class=\"form-control\" id=\"categorynameen\" name=\"txtCategoryNameEn\" required >");
			out.append("<div class=\"invalid-feedback\">Hãy nhập vào tên tiếng anh của thành phần</div>");
			out.append("</div>");
			out.append("</div>");

			out.append("<div class=\"row mb-3\">");
			out.append("<div class=\"col-lg-12\">");
			out.append("<label for=\"categorynote\" class=\"form-label\">Chú thích thể loại</label>");
			out.append(
					"<textarea row=\"8\" class=\"form-control\" id=\"categorynote\" name=\"txtCategoryNote\" required ></textarea>");
			out.append("<div class=\"invalid-feedback\">Hãy nhập vào chú thích của thành phần</div>");
			out.append("</div>");
			out.append("</div>");

			out.append("<div class=\"row mb-3\">");
			out.append("<div class=\"col-lg-6\">");
			out.append("<label for=\"manager\" class=\"form-label\">Người quản lý thể loại</label>");
			out.append("<select class=\"form-select\" id=\"manager\" name=\"slcManager\" required>");			
			out.append(viewList.get(1));		
			out.append("</select>");
			out.append("<div class=\"invalid-feedback\">Nhập hộp thư cho tài khoản</div>");
			out.append("</div>");
			
			
			out.append("<div class=\"col-lg-6\">");
			out.append("<label for=\"sectionChoose\" class=\"form-label\">Chuyên mục</label>");
			out.append("<select class=\"form-select\" id=\"sectionChoose\" name=\"slcSection\" required>");			
			out.append(viewList.get(2));
			out.append("</select>");
			out.append("<div class=\"invalid-feedback\">Nhập hộp thư cho tài khoản</div>");
			out.append("</div>");
			
			out.append("</div>");

			out.append("</div>");
			out.append("<div class=\"modal-footer\">");
			out.append(
					"<button type=\"submit\" class=\"btn btn-primary\"><i class=\"bi bi-person-plus\"></i>Thêm mới</button>");
			out.append(
					"<button type=\"button\" class=\"btn btn-secondary\" data-bs-dismiss=\"modal\"><i class=\"bi bi-x-lg\"></i>Thoát</button>");

			out.append("</div>");
			out.append("</div>"); // modal-content
			out.append("</form>");
			out.append("</div>");
			out.append("</div>");
		}
		
		out.append(viewList.get(0));

//			out.append("<p>This is an examle page with no contrnt. You can use it as a starter for your custom pages.</p>");

//			out.append("</div>"); // end card-body
//			out.append("</div>"); // end card

		out.append("</div>");
		out.append("</div>");

		out.append("<div class=\"row\">");
		out.append("<div class=\"col-lg-12\">");
		// chart
		out.append("</div>");
		out.append("</div>");
		out.append("</section>");

		out.append("</main><!-- End #main -->");

		// tham chiếu tìm footer
		RequestDispatcher f = request.getRequestDispatcher("/footer");
		if (f != null) {
			f.include(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// tim thong tin dang nhap
		UserObject user = (UserObject) request.getSession().getAttribute("userLogined");

		// thiết lập tập ký tự cần lấy
		request.setCharacterEncoding("utf-8");
		
		// lay thong tin
		String categoryName = request.getParameter("txtCategoryName");
		String categoryNotes = request.getParameter("txtCategoryNote");
		String categoryNameEn = request.getParameter("txtCategoryNameEn");
		int manager = jsoft.library.Utilities.getIntParam(request, "slcManager");
		short sectionShoose = jsoft.library.Utilities.getShortParam(request, "slcSection");
		
		if (categoryName != null && !categoryName.equalsIgnoreCase("") 
				&& categoryNotes != null && !categoryNotes.equalsIgnoreCase("") 
				&& categoryNameEn != null && !categoryNameEn.equalsIgnoreCase("")
				&& manager > 0
				&& sectionShoose > 0) {
			CategoryObject nCategory = new CategoryObject();
			nCategory.setCategory_name(categoryName);
			nCategory.setCategory_name_en(categoryNameEn);
			nCategory.setCategory_notes(categoryNotes);
			nCategory.setCategory_manager_id(manager);
			nCategory.setCategory_section_id(sectionShoose);
			nCategory.setCategory_created_author_id(user.getUser_id());
			nCategory.setCategory_created_date(jsoft.library.Utilities_date.getDate());
//			nCategory.setCategory_delete(false);
//			nCategory.setCategory_enable(true);
			
			// tim bo quan ly ket noi
			ConnectionPool cp = (ConnectionPool) getServletContext().getAttribute("CPool");			
			CategoryControl cc = new CategoryControl(cp);
			
			// thuc hien them moi
			boolean result = cc.addCategory(nCategory);
			
			// tra ve ket noi
			cc.releaseConnection();
			
			if(result) {
				response.sendRedirect("/adv/category/list");
			} else {
				response.sendRedirect("/adv/category/list?err=add");
			}
		} else {
			response.sendRedirect("/adv/category/list?err=valueadd");
		}
	}

}
