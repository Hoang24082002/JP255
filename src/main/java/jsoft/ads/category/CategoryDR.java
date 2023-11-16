package jsoft.ads.category;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jsoft.ConnectionPool;
import jsoft.ads.section.SECTION_EDIT_TYPE;
import jsoft.ads.section.SectionControl;
import jsoft.library.Utilities;
import jsoft.library.Utilities_date;
import jsoft.objects.CategoryObject;
import jsoft.objects.SectionObject;
import jsoft.objects.UserObject;

/**
 * Servlet implementation class CategoryDR
 */
@WebServlet("/category/dr")
public class CategoryDR extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CategoryDR() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		UserObject user = (UserObject) request.getSession().getAttribute("userLogined");

		short id = Utilities.getShortParam(request, "id");
		int pId = Utilities.getIntParam(request, "pid"); // parent ID
		
		if (user != null && id > 0) {
			// lay ket noi
			ConnectionPool cp = (ConnectionPool) getServletContext().getAttribute("CPool");
			CategoryControl cc = new CategoryControl(cp);
			CategoryObject dCate = new CategoryObject();
			dCate.setCategory_id(id);
			dCate.setCategory_created_author_id(pId);
			dCate.setCategory_last_modified(Utilities_date.getDate());

			CategoryObject rCate = new CategoryObject();
			rCate.setCategory_id(id);

			// tim tham so xac dinh xoa
			String trash = request.getParameter("t");

			// tim tham so xac dinh phuc hoi
			String restore = request.getParameter("r");

			String url = "/adv/category/list"; // cach xu ly to hop 4 trang thai
			boolean result;
			if (trash == null) {
				if (restore == null) {
					result = cc.delCategory(dCate);
					url += "?trash";
				} else {
					result = cc.editCategory(rCate, CATEGORY_EDIT_TYPE.RESTORE);
				}
			} else {
				result = cc.editCategory(dCate, CATEGORY_EDIT_TYPE.TRASH);
			}

			// tra ve ket noi
			cc.releaseConnection();

			if (result) {
				response.sendRedirect(url);
			} else {
				response.sendRedirect(url + "&?err=notok");
			}

		} else {
			response.sendRedirect("/adv/category/list?err=del");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
