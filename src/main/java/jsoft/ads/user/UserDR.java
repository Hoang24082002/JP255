package jsoft.ads.user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jsoft.ConnectionPool;
import jsoft.library.*;
import jsoft.objects.*;

/**
 * Servlet implementation class UserDR
 */
@WebServlet("/user/dr")
public class UserDR extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserDR() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		UserObject user = (UserObject)request.getSession().getAttribute("userLogined");
		int id = Utilities.getIntParam(request, "id");
		int pId = Utilities.getIntParam(request, "pid"); // parent ID
		
		if(user != null && id > 0) {
			if(user.getUser_id() != id) {
				if(user.getUser_permission() >= 4 || user.getUser_id() == pId) {
					ConnectionPool cp = (ConnectionPool)getServletContext().getAttribute("CPool");
					UserControl uc = new UserControl(cp);
					UserObject dUser = new UserObject();
					dUser.setUser_id(id);
					dUser.setUser_parent_id(pId);
					dUser.setUser_last_modified(Utilities_date.getDate());
					
					UserObject rUser = new UserObject();
					rUser.setUser_id(id);
					
					// tim tham so xac dinh xoa
					String trash = request.getParameter("t");
					
					// tim tham so xac dinh phuc hoi
					String restore = request.getParameter("r");
					
					String url = "/adv/user/list"; // cach xu ly to hop 4 trang thai
					boolean result;
					if(trash == null) {
						if(restore == null) {
							result = uc.delUser(dUser);						
							url+="?trash";							
						} else {
							result = uc.editUser(rUser, USER_EDIT_TYPE.RESTORE);
							
							System.out.println("id xoa:" + rUser.getUser_id() + " can duoc phuc hoi");							
						}
					} else {
							result = uc.editUser(dUser, USER_EDIT_TYPE.TRASH);							
					}
					
					uc.releaseConnection();
					if(result) {
						response.sendRedirect(url);
					} else {
						response.sendRedirect(url+"&?err=notok");
					}
				} else {
					response.sendRedirect("/adv/user/list?err=nopermis");
				}
			} else {
				response.sendRedirect("/adv/user/list?err=acclogin");
			}
		} else {
			response.sendRedirect("/adv/user/list?err=del");
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
