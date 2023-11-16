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

import jsoft.ConnectionPool;
import jsoft.ads.section.SECTION_EDIT_TYPE;
import jsoft.ads.section.SECTION_SOFT;
import jsoft.ads.section.SectionControl;
import jsoft.library.ORDER;
import jsoft.objects.CategoryObject;
import jsoft.objects.SectionObject;
import jsoft.objects.UserObject;

/**
 * Servlet implementation class CategoryProfiles
 */
@WebServlet("/category/profiles")
public class CategoryProfiles extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// dinh nghia kieu noi dung xuat ve trinh khach
	private static final String CONTENT_TYPE = "text/html; charset=utf-8";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CategoryProfiles() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
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
		Short id = jsoft.library.Utilities.getShortParam(request, "id");

		// tạo đối tượng thực hiện xuất nội dung
		PrintWriter out = response.getWriter();
		CategoryObject eCategory = null;
		
		// tìm bộ quản lý kết nối
		ConnectionPool cp = (ConnectionPool) getServletContext().getAttribute("CPool");
		// tạo đối tượng thực thi chức năng
		CategoryControl cc = new CategoryControl(cp);
		if (cp == null) {
			getServletContext().setAttribute("CPool", cp);
		}
		if (id > 0) {
			eCategory = cc.getCategoryObject(id, user);
		}


		// tham chiếu tìm header
		RequestDispatcher h = request.getRequestDispatcher("/header?pos=cylist");
		if (h != null) {
			h.include(request, response);
		}

		out.append("<main id=\"main\" class=\"main\">");

		RequestDispatcher error = request.getRequestDispatcher("/error");
		if (error != null) {
			error.include(request, response);
		}

		out.append("<div class=\"pagetitle d-flex\">");
		out.append("<h1>Danh sách thể loại</h1>");
		out.append("<nav class=\"ms-auto\">");
		out.append("<ol class=\"breadcrumb\">");
		out.append("<li class=\"breadcrumb-item\"><a href=\"/adv/view\"><i class=\"bi bi-house\"></i></a></li>");
		out.append("<li class=\"breadcrumb-item\">Thể loại</li>");
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

		out.append("</ul>");
		out.append("<div class=\"tab-content pt-2\">");
		
		String name = "", notes = "", name_en = "", name_manager = "";
		int category_manager_id = 0;
		boolean section_enable = false;

		boolean isEdit = false;
		if(eCategory!=null) {
			name = eCategory.getCategory_name();
			notes = eCategory.getCategory_notes();
			name_en = eCategory.getCategory_name_en();
			category_manager_id = eCategory.getCategory_manager_id();

			isEdit = true;
		}
		
		CategoryObject similar = new CategoryObject();
		similar.setCategory_manager_id(eCategory.getCategory_manager_id());
		similar.setCategory_section_id(eCategory.getCategory_section_id());
		Quartet<CategoryObject, Short, Byte, UserObject> infos = new Quartet<>(similar, (short)1, (byte) 15, user);
		ArrayList<String> viewList = cc.viewCategory(infos, new Pair<>(CATEGORY_SOFT.ID, ORDER.DESC), user);
		// trả về kết nối
		cc.releaseConnection();

		out.append("<div class=\"tab-pane fade show active profile-overview\" id=\"overview\">");
		out.append("<h5 class=\"card-title\">Tóm tắt</h5>");
		out.append(
				"<p class=\"small fst-italic\"> Do "+user.getUser_name()+" tạo</p>");

		out.append("<h5 class=\"card-title\">Chi tiết</h5>");

		out.append("<div class=\"row\">");
		out.append("<div class=\"col-lg-3 col-md-4 label \">Tên thể loại</div>");
		out.append("<div class=\"col-lg-9 col-md-5\">"+name+"</div>");
		out.append("</div>");

		out.append("<div class=\"row\">");
		out.append("<div class=\"col-lg-3 col-md-4 label\">Chú thích</div>");
		out.append("<div class=\"col-lg-9 col-md-8\">"+notes+"</div>");
		out.append("</div>");

		out.append("</div>");

		out.append("<div class=\"tab-pane fade profile-edit pt-3\" id=\"edit\">");

		out.append("<!-- Profile Edit Form -->");
		out.append("<form method=\"post\" action=\"/adv/category/profiles\">");
		
		out.append("<div class=\"row mb-3 row align-items-center text-end\">");
		out.append("<label for=\"name-category\" class=\"col-md-3 col-lg-2 col-form-label\">Tên thể loại</label>");
		out.append("<div class=\"col-md-3 col-lg-4 mb-3\">");
		out.append("<input name=\"txtCategoryName\" type=\"text\" class=\"form-control\" id=\"name-category\" value=\""+name+"\">");
		out.append("</div>");

		out.append("<div class=\"row mb-3 row align-items-center text-end\">");
		out.append("<label for=\"notes-category\" class=\"col-md-3 col-lg-2 col-form-label\">Chú thích</label>");
		out.append("<div class=\"col-md-9 col-lg-10\">");
		out.append(
				"<textarea name=\"txtCategoryNote\" class=\"form-control\" id=\"notes-category\" style=\"height: 100px\">"+notes+"</textarea>");
		out.append("</div>");
		out.append("</div>");
		
		out.append("<div class=\"row mb-3 row text-start\">");
		
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

		out.append("<div class=\"text-center\">");
		out.append("<button type=\"submit\" class=\"btn btn-primary\"><i class=\"bi bi-save\"></i> Lưu thay đổi</button>");
		out.append("</div>");
		
		// Truyền id theo cơ thế biến form ẩn để thực hiện edit
		if(isEdit) {
			out.append("<input type=\"hidden\" name=\"idForPost\" value=\""+id+"\" >");
//			out.append("<input type=\"hidden\" name=\"act\" value=\"edit\" >");
		}
		
		out.append("</form><!-- End Profile Edit Form -->");

		out.append("</div>");

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
		// tim thong tin dang nhap
		UserObject user = (UserObject) request.getSession().getAttribute("userLogined");

		// thiết lập tập ký tự cần lấy
		request.setCharacterEncoding("utf-8");
		
		// lấy id của chuyen muc để chỉnh sửa
		short id = jsoft.library.Utilities.getShortParam(request, "idForPost");
		
		if(id > 0) {
			// lay thong tin
			String categoryName = request.getParameter("txtCategoryName");
			String categoryNotes = request.getParameter("txtCategoryNote");
			int manager = jsoft.library.Utilities.getIntParam(request, "slcManager");
			short sectionShoose = jsoft.library.Utilities.getShortParam(request, "slcSection");
			
			if (categoryName != null && !categoryName.equalsIgnoreCase("") 
					&& categoryNotes != null && !categoryNotes.equalsIgnoreCase("") 
					&& manager > 0
					&& sectionShoose > 0) {
				CategoryObject eCategory = new CategoryObject();
				eCategory.setCategory_id(id);
				eCategory.setCategory_name(categoryName);
				eCategory.setCategory_notes(categoryNotes);
				eCategory.setCategory_manager_id(manager);
				eCategory.setCategory_section_id(sectionShoose);
				
				// tim bo quan ly ket noi
				ConnectionPool cp = (ConnectionPool) getServletContext().getAttribute("CPool");			
				CategoryControl cc = new CategoryControl(cp);
				
				// thuc hien cap nhat
				boolean result = cc.editCategory(eCategory, CATEGORY_EDIT_TYPE.GENERAL);
				
				// tra ve ket noi
				cc.releaseConnection();
				
				// check err
				if (result) {
					response.sendRedirect("/adv/category/list");
				} else {
					response.sendRedirect("/adv/category/list?err=edit");
				}
			}
		} else {
			// Không tồn tại id
			response.sendRedirect("/adv/category/list?err=profiles");
		}
	}

}
