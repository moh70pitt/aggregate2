import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class GetSummaryData usr, grp, sid should be
 * provided by the log in (authentication) <br />
 * mod can take the values: <br />
 *
 * - all: retrieve all initial information when loading MG including peers and
 * averages and top students groups <br />
 * http://localhost:8080/aggregate/GetSummaryData?usr=adl01&grp=ADL&sid=TESTADL01&cid=1&mod=all&models=-1&avgtop=4
 * avgtop indicates how many top students to include in the top group average
 * models indicates how many individual models will be inluded in the response.
 * Since a class can have many students, we can set a bound. -1 means everybody,
 * 0 will at least include the current user <br />
 *
 * -user: retrieve the current user updated model. Last activity id and result
 * should be provided to let the server know if recommendations are needed
 * http://localhost:8080/aggregate/GetSummaryData?usr=adl01&grp=ADL&sid=TESTADL01&cid=1&mod=user&lastActivityId=jDouble1&res=0
 */
@WebServlet("/GetSummaryData")
public class GetSummaryData extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static int number_recommendation = 5;
  private boolean verbose = true;
  private long time0;
  private long time1;

  public GetSummaryData() {
    super();
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    try {
      System.out.println("GetSummaryData start...");
      response.setContentType("application/json;charset=utf-8");
      response.setHeader("Access-Control-Allow-Origin", "*");
      // response.setHeader("Access-Control-Allow-Origin", "http://localhost");
      time0 = Calendar.getInstance().getTimeInMillis();
      ConfigManager cm = new ConfigManager(this);
      verbose = cm.agg_verbose.equalsIgnoreCase("yes");
      if (verbose)
        System.out.println("-------------------------------------------------------");

      PrintWriter out = response.getWriter();

      String mod = request.getParameter("mod"); // all / user / class
      String grp = request.getParameter("grp"); // the class mnemonic (as
                                                // defined in KT)
      String usr = request.getParameter("usr"); // user name
      String sid = request.getParameter("sid"); // session id
      String cid = request.getParameter("cid"); // course id (for content)

      System.out.println("mod: " + mod);
      System.out.println("grp: " + grp);
      System.out.println("usr :" + usr);
      System.out.println("sid: " + sid);
      System.out.println("cid: " + cid);

      // @@@@ JULIO KCMAP
      boolean kcMap = request.getParameter("kcmap") != null; // include Knowledge Components (concept map).
      if (kcMap)
        cm.agg_kcmap = true; // just change if received by URL

      String nmodels = request.getParameter("models"); // how many models to retrieve (-1: all, 0: only the user)
      String ntop = request.getParameter("avgtop"); // how many top students
                                                    // for the top group

      // these parameters are in the case the mod=user
      String last_content_id = request.getParameter("lastActivityId");
      String last_content_res = request.getParameter("res");
      if (last_content_id == null)
        last_content_id = "";
      if (last_content_res == null)
        last_content_res = "-1";

      String last_content_seq = request.getParameter("seq");
      String last_content_old_progress = request.getParameter("prg");

      /*
       * the param that says whether student model should be updated and whether the
       * result of last activity should be corrected
       */
      String updatesm = request.getParameter("updatesm");
      String computeGroupLevelsStr = request.getParameter("computeGroupLevels");
      boolean computeGroupLevels = computeGroupLevelsStr != null && computeGroupLevelsStr.equals("true");

      boolean includeNullUsers = cm.agg_include_null_users.equalsIgnoreCase("yes");

      String removeZeroProgressUsersStr = request.getParameter("removeZeroProgressUsers");
      boolean removeZeroProgressUsers = removeZeroProgressUsersStr != null
          && removeZeroProgressUsersStr.contentEquals("true");

      String aggUmInterfaceKey = request.getParameter("aggKey");
      cm.agg_uminterface_key = aggUmInterfaceKey != null ? aggUmInterfaceKey : cm.agg_uminterface_key;

      // if problems to get the variables, defaults are nmodels=-1 (retrieve all),
      // top=3
      int n = -1; // this variable controls how many models will be retrieved
      int top = 3; // this variable controls how many top students to consider
                   // in the top N group
      try {
        n = Integer.parseInt(nmodels);
      } catch (NumberFormatException e) {
        n = -1;
      }
      try {
        top = Integer.parseInt(ntop);
      } catch (NumberFormatException e) {
        top = 3;
      }
      if (cid == null || cid.length() == 0) {
        cid = "-1";
      }

      // the main object
      Aggregate aggregate;
      String output = "";

      if (mod == null || mod.length() == 0 || mod.equalsIgnoreCase("all")) {
        // this crates all structures, fill the information and computes the
        // up to date user model
        time1 = Calendar.getInstance().getTimeInMillis();
        aggregate = new Aggregate(usr, grp, cid, sid, false, cm);
        if (verbose)
          System.out.println("Construct Aggregate      " + (Calendar.getInstance().getTimeInMillis() - time1));

        // Get stores models for class peers
        time1 = Calendar.getInstance().getTimeInMillis();
        aggregate.fillClassLevels(includeNullUsers, removeZeroProgressUsers, true);
        if (verbose)
          System.out.println("Get class levels         " + (Calendar.getInstance().getTimeInMillis() - time1));

        time1 = Calendar.getInstance().getTimeInMillis();
        aggregate.computeGroupLevels(top);
        if (verbose)
          System.out.println("Compute group levels     " + (Calendar.getInstance().getTimeInMillis() - time1));

        if (cm.agg_proactiverec_enabled || cm.agg_reactiverec_enabled) {
          time1 = Calendar.getInstance().getTimeInMillis();
          aggregate.fillRecommendations("", "", 0); // with these parameters, only proactive
                                                    // recommendations are included (sequencing)
          if (verbose)
            System.out.println(
                "Recommendations            " + (Calendar.getInstance().getTimeInMillis() - time1));
        }

        // time1 = Calendar.getInstance().getTimeInMillis();
        // aggregate.getContentStats();
        // if(verbose) System.out.println("Gen JSON " +
        // (Calendar.getInstance().getTimeInMillis()-time1));

        time1 = Calendar.getInstance().getTimeInMillis();
        output = aggregate.genAllJSON(n, top);
        if (verbose)
          System.out.println("Gen JSON                 " + (Calendar.getInstance().getTimeInMillis() - time1));

      } else if (mod.equalsIgnoreCase("user")) {
        // parameter true indicate that the user model should be constructed
        time1 = Calendar.getInstance().getTimeInMillis();
        aggregate = new Aggregate(usr, grp, cid, sid, true, cm);
        if (verbose)
          System.out.println("Construct Aggregate+UM   " + (Calendar.getInstance().getTimeInMillis() - time1));

        time1 = Calendar.getInstance().getTimeInMillis();

        aggregate.fillClassLevels(includeNullUsers, removeZeroProgressUsers, computeGroupLevels);
        if (verbose)
          System.out.println("Get class levels         " + (Calendar.getInstance().getTimeInMillis() - time1));

        if (computeGroupLevels) {
          aggregate.computeGroupLevels(top);
        }

        // compute sequencing
        // if(cm.agg_sequencing.equalsIgnoreCase("yes")){
        // time1 = Calendar.getInstance().getTimeInMillis();
        // aggregate.sequenceContent();
        // if(verbose) System.out.println("Sequencing " +
        // (Calendar.getInstance().getTimeInMillis()-time1));
        // }

        if (updatesm != null && updatesm.equals("true")) {
          // The following line is added to get the result of the last activity
          last_content_res = getLastActivityResult(aggregate, last_content_id);

          aggregate.sendStudentModelUpdateRequest(last_content_id, last_content_res);
        }

        if (cm.agg_proactiverec_enabled || cm.agg_reactiverec_enabled) {
          time1 = Calendar.getInstance().getTimeInMillis();
          aggregate.fillRecommendations(last_content_id, last_content_res, number_recommendation);
          if (verbose)
            System.out
                .println("Recommendations          " + (Calendar.getInstance().getTimeInMillis() - time1));
        }

        time1 = Calendar.getInstance().getTimeInMillis();
        aggregate.fillFeedbackForm(last_content_id, last_content_res);
        if (verbose)
          System.out.println("Feedback form            " + (Calendar.getInstance().getTimeInMillis() - time1));

        // I need to know two parameter last_content_point and is_recommended
        time1 = Calendar.getInstance().getTimeInMillis();
        if (last_content_id != null && last_content_id.length() > 0 && last_content_seq != null
            && last_content_old_progress != null)
          aggregate.updatePointsAndBadges(last_content_id, last_content_res, last_content_seq,
              last_content_old_progress);
        if (verbose)
          System.out
              .println("Update points and badges      " + (Calendar.getInstance().getTimeInMillis() - time1));

        time1 = Calendar.getInstance().getTimeInMillis();
        output = aggregate.genUserJSON(computeGroupLevels, last_content_id, last_content_res);
        if (verbose)
          System.out.println("Generate JSON            " + (Calendar.getInstance().getTimeInMillis() - time1));

      } else if (mod.equalsIgnoreCase("class")) {

      }
      time1 = Calendar.getInstance().getTimeInMillis();
      out.print(output);
      if (verbose)
        System.out.println("Printing output          " + (Calendar.getInstance().getTimeInMillis() - time1));
      if (verbose)
        System.out.println("TOTAL                    " + (Calendar.getInstance().getTimeInMillis() - time0));
      if (verbose)
        System.out.println("-------------------------------------------------------");
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  private String getLastActivityResult(Aggregate aggregate, String last_content_id) {
    String last_content_res;
    String attemptsResSeq = aggregate.userContentSequences.get(last_content_id);
    if (attemptsResSeq != null && attemptsResSeq.isEmpty() == false) {
      String[] attmps = attemptsResSeq.split(",");
      last_content_res = attmps[attmps.length - 1];// the last attempt in the sequence
    } else {
      /*
       * if attempts-seq data is not availlable, we set the last activity result to
       * the student's progress
       */
      last_content_res = "" + aggregate.userContentLevels.get(last_content_id)[1]; // 2nd value in the
                                                                                   // array is for
                                                                                   // progress
    }

    return last_content_res;
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }
}
