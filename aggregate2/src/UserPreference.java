import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class StoreFeedback
 */
@WebServlet("/UserPreference")
public class UserPreference extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public UserPreference() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
   *      response)
   */

  /*
   * input URL:
   * 
   * TrackAction?
   * usr=peterb&grp=IS172012Fall&sid=TEST001&action_type=pick_topic&
   * action_target=Variables&
   * action_target_sub=&action_src=group&action_src_sub=average
   */

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json");
    response.setHeader("Access-Control-Allow-Origin", "*");
    PrintWriter out = response.getWriter();
    String output = "";

    String usr = request.getParameter("usr"); // user name
    String grp = request.getParameter("grp"); // the class mnemonic (as defined in KT)
    String sid = request.getParameter("sid"); // session id

    String parameterName = request.getParameter("parameter-name");
    String parameterValue = request.getParameter("parameter-value");
    String appName = request.getParameter("app-name");
    String userContext = request.getParameter("user-context");

    ConfigManager cm = new ConfigManager(this); // this object gets the
    AggregateDB agg_db = new AggregateDB(cm.agg_dbstring, cm.agg_dbuser, cm.agg_dbpass);
    agg_db.connect();

    if (agg_db.updateUserPreference(usr, grp, sid, appName, parameterName, parameterValue, userContext))
      output = "{res=1}";
    else
      output = "{res=0}";

    agg_db.disconnect();
    out.print(output);
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // TODO Auto-generated method stub
  }

}
