package jsoft.ads.section;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jsoft.ConnectionPool;
import jsoft.ads.user.USER_EDIT_TYPE;
import jsoft.ads.user.UserControl;
import jsoft.library.Utilities;
import jsoft.library.Utilities_date;
import jsoft.objects.SectionObject;
import jsoft.objects.UserObject;

/**
 * Servlet implementation class SectionDR
 */
@WebServlet("/section/dr")
public class SectionDR extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SectionDR() {
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
		// tim thong tin dang nhap
		UserObject user = (UserObject) request.getSession().getAttribute("userLogined");

		short id = Utilities.getShortParam(request, "id");
		int pId = Utilities.getIntParam(request, "pid"); // parent ID

		if (user != null && id > 0) {
			// lay ket noi
			ConnectionPool cp = (ConnectionPool) getServletContext().getAttribute("CPool");
			SectionControl sc = new SectionControl(cp);
			SectionObject dSection = new SectionObject();
			dSection.setSection_id(id);
			dSection.setSection_created_author_id(pId);
			dSection.setSection_last_modified(Utilities_date.getDate());

			SectionObject rSection = new SectionObject();
			rSection.setSection_id(id);

			// tim tham so xac dinh xoa
			String trash = request.getParameter("t");

			// tim tham so xac dinh phuc hoi
			String restore = request.getParameter("r");

			String url = "/adv/section/list"; // cach xu ly to hop 4 trang thai
			boolean result;
			if (trash == null) {
				if (restore == null) {
					result = sc.delSection(dSection);
					url += "?trash";
				} else {
					result = sc.editSection(rSection, SECTION_EDIT_TYPE.RESTORE);
				}
			} else {
				result = sc.editSection(dSection, SECTION_EDIT_TYPE.TRASH);
			}

			// tra ve ket noi
			sc.releaseConnection();

			if (result) {
				response.sendRedirect(url);
			} else {
				response.sendRedirect(url + "&?err=notok");
			}

		} else {
			response.sendRedirect("/adv/section/list?err=del");
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
