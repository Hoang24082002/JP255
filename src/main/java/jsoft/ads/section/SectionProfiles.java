package jsoft.ads.section;

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
import jsoft.ads.user.UserControl;
import jsoft.library.ORDER;
import jsoft.objects.SectionObject;
import jsoft.objects.UserObject;

/**
 * Servlet implementation class SectionProfiles
 */
@WebServlet("/section/profiles")
public class SectionProfiles extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// dinh nghia kieu noi dung xuat ve trinh khach
	private static final String CONTENT_TYPE = "text/html; charset=utf-8";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SectionProfiles() {
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
		// TODO Auto-generated method stub
		// xác định kiểu nội dung xuất về trình khách
		response.setContentType(CONTENT_TYPE);

		// Tìm id của người sử dụng để cập nhật
		Short id = jsoft.library.Utilities.getShortParam(request, "id");

		// tạo đối tượng thực hiện xuất nội dung
		PrintWriter out = response.getWriter();
		SectionObject eSection = null;
		
		// tìm bộ quản lý kết nối
		ConnectionPool cp = (ConnectionPool) getServletContext().getAttribute("CPool");
		// tạo đối tượng thực thi chức năng
		SectionControl sc = new SectionControl(cp);
		if (cp == null) {
			getServletContext().setAttribute("CPool", cp);
		}
		if (id > 0) {
			eSection = sc.getSectionObject(id);
			
		}


		// tham chiếu tìm header
		RequestDispatcher h = request.getRequestDispatcher("/header?pos=snlist");
		if (h != null) {
			h.include(request, response);
		}

		out.append("<main id=\"main\" class=\"main\">");

		RequestDispatcher error = request.getRequestDispatcher("/error");
		if (error != null) {
			error.include(request, response);
		}

		out.append("<div class=\"pagetitle d-flex\">");
		out.append("<h1>Danh sách thành phần</h1>");
		out.append("<nav class=\"ms-auto\">");
		out.append("<ol class=\"breadcrumb\">");
		out.append("<li class=\"breadcrumb-item\"><a href=\"/adv/view\"><i class=\"bi bi-house\"></i></a></li>");
		out.append("<li class=\"breadcrumb-item\">Thành phần</li>");
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
		int section_manager_id = 0;
		byte section_language = 0;
		boolean section_enable = false;
		short logined = 0;
		boolean isEdit = false;
		if(eSection!=null) {
			name = eSection.getSection_name();
			notes = eSection.getSection_notes();
			name_en = eSection.getSection_name_en();
			section_manager_id = eSection.getSection_manager_id();
			section_language = eSection.getSection_language();
			section_enable = eSection.isSection_enable();

			isEdit = true;
		}
		
		SectionObject similar = new SectionObject();
		similar.setSection_created_author_id(user.getUser_id());
		similar.setSection_manager_id(eSection.getSection_manager_id());
		Quartet<SectionObject, Short, Byte, UserObject> infos = new Quartet<>(similar, (short)1, (byte) 15, user);
		ArrayList<String> viewList = sc.viewSection(infos, new Pair<>(SECTION_SOFT.ID, ORDER.DESC), user);
		// trả về kết nối
		sc.releaseConnection();

		out.append("<div class=\"tab-pane fade show active profile-overview\" id=\"overview\">");
		out.append("<h5 class=\"card-title\">Tóm tắt</h5>");
		out.append(
				"<p class=\"small fst-italic\"> Do "+user.getUser_name()+" tạo</p>");

		out.append("<h5 class=\"card-title\">Chi tiết</h5>");

		out.append("<div class=\"row\">");
		out.append("<div class=\"col-lg-3 col-md-4 label \">Tên thành phần</div>");
		out.append("<div class=\"col-lg-9 col-md-5\">"+name+"</div>");
		out.append("</div>");

		out.append("<div class=\"row\">");
		out.append("<div class=\"col-lg-3 col-md-4 label\">Chú thích</div>");
		out.append("<div class=\"col-lg-9 col-md-8\">"+notes+"</div>");
		out.append("</div>");

		out.append("</div>");

		out.append("<div class=\"tab-pane fade profile-edit pt-3\" id=\"edit\">");

		out.append("<!-- Profile Edit Form -->");
		out.append("<form method=\"post\" action=\"/adv/section/profiles\">");
		
		out.append("<div class=\"row mb-3 row align-items-center text-end\">");
		out.append("<label for=\"name-section\" class=\"col-md-3 col-lg-2 col-form-label\">Tên chuyên mục</label>");
		out.append("<div class=\"col-md-3 col-lg-4 mb-3\">");
		out.append("<input name=\"txtSectionName\" type=\"text\" class=\"form-control\" id=\"name-section\" value=\""+name+"\">");
		out.append("</div>");

		out.append("<div class=\"row mb-3 row align-items-center text-end\">");
		out.append("<label for=\"notes-section\" class=\"col-md-3 col-lg-2 col-form-label\">Chú thích</label>");
		out.append("<div class=\"col-md-9 col-lg-10\">");
		out.append(
				"<textarea name=\"txtSectionNote\" class=\"form-control\" id=\"notes-section\" style=\"height: 100px\">"+notes+"</textarea>");
		out.append("</div>");
		out.append("</div>");
		
		out.append("<div class=\"row mb-3 row align-items-center text-end\">");
		out.append("<label for=\"manager\" class=\"col-md-3 col-lg-2 col-form-label\">Người quản lý chuyên mục</label>");
		out.append("<div class=\"col-md-3 col-lg-4\">");
		out.append("<select class=\"form-select\" id=\"manager\" name=\"slcManager\" required>");
		out.append(viewList.get(1));
		out.append("</select>");
		out.append("</div>");
		out.append("<div class=\"invalid-feedback\">Nhập hộp thư cho tài khoản</div>");

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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// tim thong tin dang nhap
		UserObject user = (UserObject) request.getSession().getAttribute("userLogined");

		// thiết lập tập ký tự cần lấy
		request.setCharacterEncoding("utf-8");
		
		// lấy id của chuyen muc để chỉnh sửa
		short id = jsoft.library.Utilities.getShortParam(request, "idForPost");
//		System.out.println(id);
		
		if(id > 0) {
			// lấy thông tin
			String sectionName = request.getParameter("txtSectionName");
			String sectionNote = request.getParameter("txtSectionNote");
			int manager = jsoft.library.Utilities.getIntParam(request, "slcManager");			
			
			if (sectionName != null && !sectionName.equalsIgnoreCase("") 
					&& sectionNote != null && !sectionNote.equalsIgnoreCase("") 
					&& manager > 0) {
				SectionObject eSection = new SectionObject();
				eSection.setSection_id(id);
				eSection.setSection_name(jsoft.library.Utilities.encode(sectionName));
				eSection.setSection_notes(jsoft.library.Utilities.encode(sectionNote));
				eSection.setSection_created_date(jsoft.library.Utilities_date.getDate());
				eSection.setSection_manager_id(manager);
//				eSection.setSection_created_author_id(user.getUser_id());
				
				// tim bo quan ly ket noi
				ConnectionPool cp = (ConnectionPool) getServletContext().getAttribute("CPool");
				SectionControl sc = new SectionControl(cp);
				if (cp == null) {
					getServletContext().setAttribute("CPool", sc.getCP());
				}
				
				// thuc hien cap nhat
				boolean result = sc.editSection(eSection, SECTION_EDIT_TYPE.GENERAL);
				
				// tra ve ket noi
				sc.releaseConnection();
				
				//
				if (result) {
					response.sendRedirect("/adv/section/list");
				} else {
					response.sendRedirect("/adv/section/list?err=edit");
				}
			}
		} else {
			// Không tồn tại id
			response.sendRedirect("/adv/section/list?err=profiles");
		}

	}

}
