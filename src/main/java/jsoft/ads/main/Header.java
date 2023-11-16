package jsoft.ads.main;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jsoft.objects.UserObject;

/**
 * Servlet implementation class Header
 */
@WebServlet("/header")
public class Header extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Header() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		
		UserObject user = (UserObject) request.getSession().getAttribute("userLogined");
		
		String url = "/adv/view";
		
		String pos = request.getParameter("pos");
		if(pos != null) {
			if(pos.contains("ur")) {
				String func = pos.substring(2);
				switch(func) {
				case "list":
					url = "/adv/user/list";
					break;
				case "trash":
					url = "/adv/user/list?trash";
					break;
				}
			}
		}
		
		// lay tu khoa tim kiem
		String key = request.getParameter("key");
		String saveKey = (key!=null && key.equalsIgnoreCase("")) ? key.trim() : "";
		
		
		out.append("<!DOCTYPE html>");
		out.append("<html lang=\"en\">");
		
		out.append("<head>");
		out.append("<meta charset=\"utf-8\">");
		out.append("<meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\">");
		
		out.append("<title>Template View</title>");
		out.append("<meta content=\"\" name=\"description\">");
		out.append("<meta content=\"\" name=\"keywords\">");
		
		out.append("<!-- Favicons -->");
		out.append("<link href=\"assets/img/favicon.png\" rel=\"icon\">");
		out.append("<link href=\"assets/img/apple-touch-icon.png\" rel=\"apple-touch-icon\">");
		
		out.append("<!-- Google Fonts -->");
		out.append("<link href=\"https://fonts.gstatic.com\" rel=\"preconnect\">");
		out.append(
				"<link href=\"https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i|Nunito:300,300i,400,400i,600,600i,700,700i|Poppins:300,300i,400,400i,500,500i,600,600i,700,700i\" rel=\"stylesheet\">");
		
		out.append("<!-- Vendor CSS Files -->");
		out.append("<link href=\"/adv/adcss/bootstrap.min.css\" rel=\"stylesheet\">");
		out.append("<link href=\"/adv/adcss/bootstrap-icons/bootstrap-icons.css\" rel=\"stylesheet\">");
		// out.append("<link href=\"assets/vendor/boxicons/css/boxicons.min.css\" rel=\"stylesheet\">");
		// out.append("<link href=\"assets/vendor/remixicon/remixicon.css\" rel=\"stylesheet\">");
		
		out.append("<script src=\"/adv/adjavascript/bootstrap.bundle.min.js\"></script>");
		
		out.append("<!-- Template Main CSS File -->");
		out.append("<link href=\"/adv/adcss/style.css\" rel=\"stylesheet\">");
		
		out.append("</head>");
		
		out.append("<body>");
		
		out.append("<!-- ======= Header ======= -->");
		out.append("<header id=\"header\" class=\"header fixed-top d-flex align-items-center\">");
		
		out.append("<div class=\"d-flex align-items-center justify-content-between\">");
		out.append("<a href=\"index.html\" class=\"logo d-flex align-items-center\">");
		out.append("<img src=\"assets/img/logo.png\" alt=\"\">");
		out.append("<span class=\"d-none d-lg-block\">NiceAdmin</span>");
		out.append("</a>");
		out.append("<i class=\"bi bi-list toggle-sidebar-btn\"></i>");
		out.append("</div><!-- End Logo -->");
		
		out.append("<div class=\"search-bar\">");
		out.append("<form class=\"search-form d-flex align-items-center\" method=\"POST\" action=\""+url+"\">");
		out.append("<input type=\"text\" name=\"keyword\" placeholder=\"Search\" title=\"Enter search keyword\" value=\""+saveKey+"\">");
		out.append("<button type=\"submit\" title=\"Search\"><i class=\"bi bi-search\"></i></button>");
		out.append("</form>");
		out.append("</div><!-- End Search Bar -->");
		
		out.append("<nav class=\"header-nav ms-auto\">");
		out.append("<ul class=\"d-flex align-items-center\">");
		
		out.append("<li class=\"nav-item d-block d-lg-none\">");
		out.append("<a class=\"nav-link nav-icon search-bar-toggle \" href=\"#\">");
		out.append("<i class=\"bi bi-search\"></i>");
		out.append("</a>");
		out.append("</li><!-- End Search Icon-->");
		
		out.append("<li class=\"nav-item dropdown\">");
		
		out.append("<a class=\"nav-link nav-icon\" href=\"#\" data-bs-toggle=\"dropdown\">");
		out.append("<i class=\"bi bi-bell\"></i>");
		out.append("<span class=\"badge bg-primary badge-number\">4</span>");
		out.append("</a><!-- End Notification Icon -->");
		
		out.append("<ul class=\"dropdown-menu dropdown-menu-end dropdown-menu-arrow notifications\">");
		out.append("<li class=\"dropdown-header\">");
		out.append("You have 4 new notifications");
		out.append("<a href=\"#\"><span class=\"badge rounded-pill bg-primary p-2 ms-2\">View all</span></a>");
		out.append("</li>");
		out.append("<li>");
		out.append("<hr class=\"dropdown-divider\">");
		out.append("</li>");
		
		out.append("<li class=\"notification-item\">");
		out.append("<i class=\"bi bi-exclamation-circle text-warning\"></i>");
		out.append("<div>");
		out.append("<h4>Lorem Ipsum</h4>");
		out.append("<p>Quae dolorem earum veritatis oditseno</p>");
		out.append("<p>30 min. ago</p>");
		out.append("</div>");
		out.append("</li>");
		
		out.append("<li>");
		out.append("<hr class=\"dropdown-divider\">");
		out.append("</li>");
		
		out.append("<li class=\"notification-item\">");
		out.append("<i class=\"bi bi-x-circle text-danger\"></i>");
		out.append("<div>");
		out.append("<h4>Atque rerum nesciunt</h4>");
		out.append("<p>Quae dolorem earum veritatis oditseno</p>");
		out.append("<p>1 hr. ago</p>");
		out.append("</div>");
		out.append("</li>");
		
		out.append("<li>");
		out.append("<hr class=\"dropdown-divider\">");
		out.append("</li>");
		
		out.append("<li class=\"notification-item\">");
		out.append("<i class=\"bi bi-check-circle text-success\"></i>");
		out.append("<div>");
		out.append("<h4>Sit rerum fuga</h4>");
		out.append("<p>Quae dolorem earum veritatis oditseno</p>");
		out.append("<p>2 hrs. ago</p>");
		out.append("</div>");
		out.append("</li>");
		
		out.append("<li>");
		out.append("<hr class=\"dropdown-divider\">");
		out.append("</li>");
		
		out.append("<li class=\"notification-item\">");
		out.append("<i class=\"bi bi-info-circle text-primary\"></i>");
		out.append("<div>");
		out.append("<h4>Dicta reprehenderit</h4>");
		out.append("<p>Quae dolorem earum veritatis oditseno</p>");
		out.append("<p>4 hrs. ago</p>");
		out.append("</div>");
		out.append("</li>");
		
		out.append("<li>");
		out.append("<hr class=\"dropdown-divider\">");
		out.append("</li>");
		out.append("<li class=\"dropdown-footer\">");
		out.append("<a href=\"#\">Show all notifications</a>");
		out.append("</li>");
		
		out.append("</ul><!-- End Notification Dropdown Items -->");
		
		out.append("</li><!-- End Notification Nav -->");
		
		out.append("<li class=\"nav-item dropdown\">");
		
		out.append("<a class=\"nav-link nav-icon\" href=\"#\" data-bs-toggle=\"dropdown\">");
		out.append("<i class=\"bi bi-chat-left-text\"></i>");
		out.append("<span class=\"badge bg-success badge-number\">3</span>");
		out.append("</a><!-- End Messages Icon -->");
		
		out.append("<ul class=\"dropdown-menu dropdown-menu-end dropdown-menu-arrow messages\">");
		out.append("<li class=\"dropdown-header\">");
		out.append("You have 3 new messages");
		out.append("<a href=\"#\"><span class=\"badge rounded-pill bg-primary p-2 ms-2\">View all</span></a>");
		out.append("</li>");
		out.append("<li>");
		out.append("<hr class=\"dropdown-divider\">");
		out.append("</li>");
		
		out.append("<li class=\"message-item\">");
		out.append("<a href=\"#\">");
		out.append("<img src=\"assets/img/messages-1.jpg\" alt=\"\" class=\"rounded-circle\">");
		out.append("<div>");
		out.append("<h4>Maria Hudson</h4>");
		out.append("<p>Velit asperiores et ducimus soluta repudiandae labore officia est ut...</p>");
		out.append("<p>4 hrs. ago</p>");
		out.append("</div>");
		out.append("</a>");
		out.append("</li>");
		out.append("<li>");
		out.append("<hr class=\"dropdown-divider\">");
		out.append("</li>");
		
		out.append("<li class=\"message-item\">");
		out.append("<a href=\"#\">");
		out.append("<img src=\"assets/img/messages-2.jpg\" alt=\"\" class=\"rounded-circle\">");
		out.append("<div>");
		out.append("<h4>Anna Nelson</h4>");
		out.append("<p>Velit asperiores et ducimus soluta repudiandae labore officia est ut...</p>");
		out.append("<p>6 hrs. ago</p>");
		out.append("</div>");
		out.append("</a>");
		out.append("</li>");
		out.append("<li>");
		out.append("<hr class=\"dropdown-divider\">");
		out.append("</li>");
		
		out.append("<li class=\"message-item\">");
		out.append("<a href=\"#\">");
		out.append("<img src=\"assets/img/messages-3.jpg\" alt=\"\" class=\"rounded-circle\">");
		out.append("<div>");
		out.append("<h4>David Muldon</h4>");
		out.append("<p>Velit asperiores et ducimus soluta repudiandae labore officia est ut...</p>");
		out.append("<p>8 hrs. ago</p>");
		out.append("</div>");
		out.append("</a>");
		out.append("</li>");
		out.append("<li>");
		out.append("<hr class=\"dropdown-divider\">");
		out.append("</li>");
		
		out.append("<li class=\"dropdown-footer\">");
		out.append("<a href=\"#\">Show all messages</a>");
		out.append("</li>");
		
		out.append("</ul><!-- End Messages Dropdown Items -->");
		
		out.append("</li><!-- End Messages Nav -->");
		
		out.append("<li class=\"nav-item dropdown pe-3\">");
		
		out.append(
				"<a class=\"nav-link nav-profile d-flex align-items-center pe-0\" href=\"#\" data-bs-toggle=\"dropdown\">");
		out.append("<img src=\"/adv/adimgs/profile-img.jpg\" alt=\"Profile\" class=\"rounded-circle\">");
		out.append("<span class=\"d-none d-md-block dropdown-toggle ps-2\">"+ user.getUser_name() +"</span>");
		out.append("</a><!-- End Profile Iamge Icon -->");
		
		out.append("<ul class=\"dropdown-menu dropdown-menu-end dropdown-menu-arrow profile\">");
		out.append("<li class=\"dropdown-header\">");
		out.append("<h6>"+ user.getUser_fullname() +"</h6>");
		out.append("<span>"+ user.getUser_name() +"</span>");
		out.append("</li>");
		out.append("<li>");
		out.append("<hr class=\"dropdown-divider\">");
		out.append("</li>");
		
		out.append("<li>");
		out.append("<a class=\"dropdown-item d-flex align-items-center\" href=\"/adv/user/profiles?id="+user.getUser_id()+"\">");
		out.append("<i class=\"bi bi-person\"></i>");
		out.append("<span>My Profile</span>");
		out.append("</a>");
		out.append("</li>");
		out.append("<li>");
		out.append("<hr class=\"dropdown-divider\">");
		out.append("</li>");
		
		out.append("<li>");
		out.append("<a class=\"dropdown-item d-flex align-items-center\" href=\"#\">");
		out.append("<i class=\"bi bi-gear\"></i>");
		out.append("<span>Account Settings</span>");
		out.append("</a>");
		out.append("</li>");
		out.append("<li>");
		out.append("<hr class=\"dropdown-divider\">");
		out.append("</li>");
		
		out.append("<li>");
		out.append("<a class=\"dropdown-item d-flex align-items-center\" href=\"pages-faq.html\">");
		out.append("<i class=\"bi bi-question-circle\"></i>");
		out.append("<span>Need Help?</span>");
		out.append("</a>");
		out.append("</li>");
		out.append("<li>");
		out.append("<hr class=\"dropdown-divider\">");
		out.append("</li>");
		
		out.append("<li>");
		out.append("<a class=\"dropdown-item d-flex align-items-center\" href=\"/adv/user/logout\">");
		out.append("<i class=\"bi bi-box-arrow-right\"></i>");
		out.append("<span>Sign Out</span>");
		out.append("</a>");
		out.append("</li>");
		
		out.append("</ul><!-- End Profile Dropdown Items -->");
		out.append("</li><!-- End Profile Nav -->");
		
		out.append("</ul>");
		out.append("</nav><!-- End Icons Navigation -->");
		
		out.append("</header><!-- End Header -->");
		
		// tham chiếu tìm sidebar
		RequestDispatcher s = request.getRequestDispatcher("/sidebar");
		if(s != null) {
			s.include(request, response);
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
