import java.text.DecimalFormat;
import java.util.*;

public class AggregateDB extends DBConnection {
  
  public static DecimalFormat df4 = new DecimalFormat("#.####");

  public AggregateDB(String url, String username, String password) {
    super(url, username, password);
  }

  public String getGrpName(String grp) {
    return first((rs) -> rs.getString("group_name"), //
        "select group_name from ent_group where group_id = ?",
        grp);
  }

  public String getDomain(String course_id) {
    return first((rs) -> rs.getString("domain"), //
        "select domain from ent_course where course_id = ?",
        course_id);
  }

  public String getCourseId(String grp) {
    return first((rs) -> rs.getString("course_id"), //
        "select course_id from ent_group where group_id = ?",
        grp);
  }

  public HashMap<String, String> getNonStudents(String group_id) {
    HashMap<String, String> stds = new HashMap<String, String>();
    query((rs) -> stds.put(rs.getString("user_id"), rs.getString("user_role")), //
        "SELECT user_id, user_role FROM ent_non_student WHERE group_id = ?",
        group_id);
    return stds;
  }

  // returns the ordered list of topics of a course corresponding to the group
  // (class)
  public ArrayList<String[]> getTopicList(String course_id) {
    ArrayList<String[]> topics = new ArrayList<String[]>();
    query((rs) -> topics.add(new String[] {
        rs.getString("topic_name"),
        rs.getString("display_name"),
        rs.getString("order"),
        rs.getString("visible"), // visibility
        "", // current / covered
    }), "select topic_name, display_name, order, visible " + //
        "from ent_topic where course_id = ? and active = 1 " + //
        "order by `order` asc",
        course_id);
    return topics;
  }

  public ArrayList<String> getHiddenTopics(String group_id) {
    ArrayList<String> tpNames = new ArrayList<String>();
    query((rs) -> tpNames.add(rs.getString("topic_name")),
        "select topic_name from ent_hidden_topics where group_id = ?",
        group_id);
    return tpNames;
  }

  // returns all content for each topic -> questions, examples, readings as
  // arrays of string
  // @@@@ JG FIXED
  public ArrayList<String[]> getContentByTopic(String topic) {
    ArrayList<String[]> tpContents = new ArrayList<String[]>();
    query((rs) -> tpContents.add(new String[] {
        rs.getString("content_id"),
        rs.getString("content_name"),
        rs.getString("content_type"),
        rs.getString("display_name"),
        rs.getString("url"),
        rs.getString("desc"),
        rs.getString("comment"),
    }), "select c.content_id, c.content_name, c.content_type, c.display_name, c.url, c.desc, c.comment " + //
        "from ent_content c, rel_topic_content tc, ent_topic t " + //
        "where t.topic_name = ? and t.topic_id = tc.topic_id and tc.content_id=c.content_id " + //
        "  and c.visible = 1 and tc.visible = 1 and t.active = 1 " + //
        "order by c.content_type desc, tc.display_order asc",
        topic);
    return tpContents;
  }

  // now get data with resource instead of content_type
  public HashMap<String, String[]> getContent2(String course_id) {
    HashMap<String, String[]> contents = new HashMap<String, String[]>();
    query((rs) -> contents.put(rs.getString("content_name"), new String[] {
        rs.getString("resource_name"),
        rs.getString("display_name"),
        rs.getString("url"),
        rs.getString("desc"),
        rs.getString("comment"),
        rs.getString("provider_id"),
        "",
    }), "select c.content_id, c.content_name, r.resource_name, c.display_name, " + //
        "       c.url, c.desc, c.comment, c.provider_id " + //
        " from ent_content c, rel_topic_content tc, ent_topic t, ent_resource r " + //
        "where t.course_id = ? and t.topic_id = tc.topic_id and tc.content_id = c.content_id " + //
        "  and c.visible = 1 and tc.visible = 1 and t.active = 1 " + //
        "  and r.resource_id = tc.resource_id " + //
        "order by t.`order`, r.`order` desc, tc.display_order asc",
        course_id);
    return contents;
  }

  // Now with resource instead of content_type
  // @@@@ #### TEST!!
  public HashMap<String, ArrayList<String>[]> getTopicContent2(String course_id, HashMap<String, Integer> resourceMap) {
    HashMap<String, ArrayList<String>[]> tpContents = new HashMap<>();
    query((rs) -> {
      ArrayList<String>[] all_content = new ArrayList[resourceMap.size()];
      tpContents.put(rs.getString("topic_name"), all_content);

      for (int i = 0; i < all_content.length; i++)
        all_content[i] = new ArrayList<String>();

      String allcontent = rs.getString("content");
      if (allcontent == null || //
          allcontent.equalsIgnoreCase("[null]") || //
          allcontent.length() == 0) {
        //
      } else {
        String[] content = allcontent.split(";");
        for (int i = 0; i < content.length; i++) {
          String[] item = content[i].split(",");
          int resourceIndex = resourceMap.get(item[1]);
          if (resourceIndex >= 0 && resourceIndex < all_content.length)
            all_content[resourceIndex].add(item[0]);
        }
      }
    }, "select t.topic_name, group_concat(" + //
        "      c.content_name , ',' , " + //
        "      r.resource_name order by c.content_type, " + //
        "      tc.display_order separator ';'" + //
        ") as content " + //
        "from ent_topic t, rel_topic_content tc, ent_content c, ent_resource r " + //
        "where t.course_id = ? " + //
        "and t.active = 1 and c.visible = 1 and tc.visible = 1 " + //
        "and tc.topic_id = t.topic_id and c.content_id = tc.content_id and r.resource_id = tc.resource_id " + //
        "group by t.topic_id",
        course_id);
    return tpContents;
  }

  public void storeComputedModel(
      String user, String course_id, String group_id, String sid,
      String model4topics, String model4content, String model4kc) {
    if (this.existComputedModel(user, course_id)) {
      update("update ent_computed_models set model4topics = ?, " + //
          "model4content = ?, model4kc = ?, last_update = now() " + //
          "where user_id = ? and course_id = ?",
          model4topics, model4content, model4kc, user, course_id);
    } else {
      update("insert into ent_computed_models " + //
          "       (user_id, course_id, last_update, model4topics, model4content, model4kc) " + //
          "values (?, ?, now(), ?, ?, ?)",
          user, course_id, model4topics, model4content, model4kc);
    }

    update("insert into ent_computed_models_history " + //
        "       (user_id, course_id, group_id, session_id, computedon, " + //
        "        model4topics, model4content, model4kc) " + //
        "values (?, ?, ?, ?, now(), ?, ?, ?)",
        user, course_id, group_id, model4topics, model4content, model4kc);
  }

  // Sees if a user has a model for a specific course id
  public boolean existComputedModel(String user, String course_id) {
    return first((rs) -> rs.getInt("npm") > 0, //
        "select count(*) as npm from ent_computed_models where user_id = ? and course_id = ?",
        user, course_id);
  }

  public HashMap<String, String[]> getComputedModelsInCourse(String course_id) {
    return getComputedModels(course_id, null);
  }

  // give usr == null or usr == "" to look for all precomputed models within a
  // course
  public HashMap<String, String[]> getComputedModels(String course_id, String usr) {
    String userQueryExt = usr != null && usr.length() > 0 ? " and user_id = ?" : null;

    HashMap<String, String[]> contents = new HashMap<String, String[]>();
    query((rs) -> contents.put(rs.getString("user_id"), new String[] {
        rs.getString("model4topics"),
        rs.getString("model4content"),
        rs.getString("model4kc"),
        rs.getString("last_update"),
    }), "select user_id, model4topics, model4content, model4kc, last_update " + //
        "from ent_computed_models where course_id = ? " + userQueryExt,
        course_id, param(userQueryExt, usr));
    return contents;
  }

  public boolean insertUsrFeedback(
      String usr, String grp, String sid,
      String srcActivityId, String srcActivityRes, String fbId,
      String fbItemIds, String responses, String recId) {
    boolean notNullEmpty = fbItemIds != null && fbItemIds.length() != 0;
    String[] fbItemArray = notNullEmpty ? fbItemIds.split("\\|") : new String[] { "" };
    String[] resArray = notNullEmpty ? responses.split("\\|") : new String[] { "" };

    if (notNullEmpty && fbItemArray.length != resArray.length) {
      // ...
    }

    for (int i = 0; i < fbItemArray.length; i++)
      update("INSERT INTO ent_user_feedback " + //
          "       (user_id, session_id, group_id, src_content_name, src_content_res, " + //
          "        fb_id, fb_item_id, fb_response_value, item_rec_id, datentime) " + //
          "values (?, ?, ?, ?, ?, ?, ?, ?, ?, now())",
          usr, sid, grp, srcActivityId, srcActivityRes,
          fbId, fbItemArray[i], resArray[i], recId);

    return true;
  }

  public boolean insertTrackAction(String usr, String grp, String sid,
      String action, String comment) {
    return update(
        "insert into ent_tracking (datentime, user_id, session_id, group_id, action, comment) values (now(3), ?, ?, ?, ?, ?)",
        usr, sid, grp, action, comment) > 0;
  }

  // resource name , resource display name,desc,visible,update_state_on
  // Example: qz , question, "this is the description" , 1, 101
  // update_state_on: digits represent in order the options for updating the user
  // model:
  // 1: activity done, 2: in window close, and 3: window close if activity done.
  // For example 010 will update UM when the content window is closed.
  public ArrayList<String[]> getResourceList(String course_id) {
    ArrayList<String[]> rsrcs = new ArrayList<String[]>();
    query((rs) -> rsrcs.add(new String[] {
        rs.getString("resource_name"),
        rs.getString("display_name"),
        rs.getString("desc"),
        rs.getString("visible"),
        rs.getString("update_state_on"),
        rs.getString("order"),
        rs.getString("window_width"),
        rs.getString("window_height"),
    }), "select resource_name, display_name, `desc`, visible, update_state_on, " + //
        "       `order`, window_width, window_height " + //
        "from ent_resource " + //
        "where course_id= ? order by `order`",
        course_id);
    return rsrcs;
  }

  public ArrayList<String[]> getSubGroups(String group_id) {
    ArrayList<String[]> subgrps = new ArrayList<String[]>();
    query((rs) -> subgrps.add(new String[] {
        rs.getString("subgroup_name"),
        rs.getString("subgroup_users"),
        rs.getString("type"),
    }), "select subgroup_name, subgroup_users, type from ent_subgroups " + //
        "where group_id = ?",
        group_id);
    return subgrps;
  }

  public ArrayList<String[]> getParameters(String user_id, String group_id) {
    ArrayList<String[]> params = new ArrayList<String[]>();
    query((rs) -> params.add(new String[] {
        rs.getString("level"),
        rs.getString("params_vis"),
        rs.getString("params_svcs"),
        rs.getString("user_manual"),
    }), "select level, params_vis, params_svcs, user_manual from ent_parameters " + //
        "where (group_id = ? and level = 'group') or (user_id = ? and group_id = ?)",
        group_id, user_id, group_id);
    return params;
  }

  public String[] getTimeLine(String group_id) {
    // TODO: kamil, should it be first result or last or ...?
    return first((rs) -> new String[] {
        rs.getString("currentTopics"),
        rs.getString("coveredTopics")
    }, "select currenttopics, coveredtopics from ent_timeline " + //
        "where group_id = ?",
        group_id);
  }

  // Other methods to insert Groups, non students, select current/covered topics,

  // Add a group
  public boolean registerGroup(String grp, String grpName, String cid, String term, String year, String creatorId) {
    return update(
        "insert into ent_group (group_id, group_name, course_id, creation_date, term, year, creator_id) values (?, ?, ?, now(), ?, ?, ?)",
        grp, grpName, cid, term, year, creatorId) > 0;
  }

  public boolean addNonStudent(String grp, String usr, String role) {
    return update("INSERT INTO ent_non_student (group_id, user_id, user_role) values (?, ?, ?)", grp, usr, role) > 0;
  }

  public HashMap<String, String[]> getProvidersInfo(String course_id) {
    HashMap<String, String[]> providers = new HashMap<String, String[]>();
    query((rs) -> providers.put(rs.getString("provider_id"), new String[] {
        rs.getString("um_svc_url"),
        rs.getString("activity_svc_url"),
        null,
        null,
    }), "select p.provider_id, p.um_svc_url, p.activity_svc_url " + //
        "from ent_provider p, ent_resource r, rel_resource_provider rp " + //
        "where r.resource_id = rp.resource_id and rp.provider_id = p.provider_id and r.course_id = ?",
        course_id);
    return providers;
  }

  // @@@@ JULIO KCMAP
  // Read the list of concepts which are "active" (would be shown)
  public HashMap<String, KnowledgeComponent> getAllKCs(String domain) {
    HashMap<String, KnowledgeComponent> allKCs = new HashMap<String, KnowledgeComponent>();
    query((rs) -> {
      String name = rs.getString("component_name");
      System.out.println(name);// added by @Jordan

      int id = rs.getInt("id");
      int cardinality = rs.getInt("cardinality");
      double th1 = rs.getDouble("threshold1");
      double th2 = rs.getDouble("threshold2");

      KnowledgeComponent c;
      if (cardinality == 1) {
        c = new KnowledgeComponent(id, name,
            rs.getString("display_name"),
            rs.getString("main_topic"),
            th1, th2);
        allKCs.put(name, c);
      } else {
        c = new KnowledgeComponentGroup(id, name,
            rs.getString("display_name"),
            rs.getString("main_topic"));
        allKCs.put(name, c);

        String[] kcNames = rs.getString("component_name").split("~"); // this supposes to be the format
        if (kcNames != null)
          for (String kcName : kcNames) {
            KnowledgeComponent kc = allKCs.get(kcName);
            if (kc != null && !((KnowledgeComponentGroup) c).kcExist(kc))
              ((KnowledgeComponentGroup) c).getKcs().add(kc);
          }
        String mainComponent = rs.getString("main_component");
        if (mainComponent != null && mainComponent.length() > 0) {
          KnowledgeComponent mainKc = allKCs.get(mainComponent);
          if (mainKc != null)
            ((KnowledgeComponentGroup) c).setMainKc(mainKc);
        }
      }

      String[] contentRels = rs.getString("contents").split("\\|");
      // System.out.println(name);
      if (contentRels != null) {
        for (String cr : contentRels) {
          String[] cn = cr.split(",");
          // System.out.println(name+" : "+cn[0]);
          double[] v = new double[3];
          // System.out.println(" "+cn[0] + " :a " +cr);
          v[0] = Double.parseDouble(cn[1]);
          v[1] = Double.parseDouble(cn[2]);
          v[2] = Double.parseDouble(cn[3]);
          try {
            c.getContents().put(cn[0], v);
          } catch (Exception e) {
          }
        }
      }
    }, "select kc.id, kc.component_name, kc.cardinality, kc.display_name, threshold1, threshold2, " + //
        "      group_concat(cc.content_name, ',', cast(cc.weight as char), ',', cast(cc.importance as char), ',', " + //
        "                   cast(cc.contributesk as char) order by cc.content_name separator '|' ) as contents, " + //
        "      kc.main_topic, kc.main_component " + //
        "from kc_component kc, kc_content_component cc " + //
        "where kc.component_name = cc.component_name and kc.active = 1 " + //
        "  and cc.active = 1 and kc.domain = ? and cc.domain = kc.domain " + //
        "group by kc.cardinality asc, cc.component_name asc",
        domain);
    return allKCs;
  }

  // @@@@ JULIO
  // Get stats of content
  public HashMap<String, ContentStats> getContentStats(String domain) {
    HashMap<String, ContentStats> ctntStats = new HashMap<String, ContentStats>();
    query((rs) -> ctntStats.put(rs.getString("content_name"),
        new ContentStats(
            rs.getString("content_name"),
            rs.getString("provider_id"),
            rs.getDouble("a_p10"), rs.getDouble("a_p25"),
            rs.getDouble("a_p33"), rs.getDouble("a_p50"),
            rs.getDouble("a_p66"), rs.getDouble("a_p75"),
            rs.getDouble("a_p80"), rs.getDouble("a_p85"),
            rs.getDouble("a_p90"),
            rs.getDouble("t_p10"), rs.getDouble("t_p25"),
            rs.getDouble("t_p33"), rs.getDouble("t_p50"),
            rs.getDouble("t_p66"), rs.getDouble("t_p75"),
            rs.getDouble("t_p80"), rs.getDouble("t_p85"),
            rs.getDouble("t_p90"),
            rs.getDouble("sr_p10"), rs.getDouble("sr_p25"),
            rs.getDouble("sr_p33"), rs.getDouble("sr_p50"),
            rs.getDouble("sr_p66"), rs.getDouble("sr_p75"),
            rs.getDouble("sr_p80"), rs.getDouble("sr_p85"),
            rs.getDouble("sr_p90") //
        )), "select content_name, provider_id, " + //
            "a_p10, a_p25, a_p33, a_p50, a_p66, a_p75, a_p80, a_p85, a_p90, " + //
            "t_p10, t_p25, t_p33, t_p50, t_p66, t_p75, t_p80, t_p85, t_p90, " + //
            "sr_p10, sr_p25, sr_p33, sr_p50, sr_p66, sr_p75, sr_p80, sr_p85, sr_p90 " + //
            "from stats_content where domain = ?",
        domain);
    return ctntStats;
  }

  public List<Logs> getFiveRecentPoints(String user_id, String group_id) {
    List<Logs> points = new ArrayList<Logs>();
    query((rs) -> points.add(new Logs(
        rs.getString("recent_point"),
        rs.getString("description"),
        rs.getString("total_point") //
    )), "select * from ent_point e " + //
        "where e.user_id = ? and e.group_id = ? and e.total_point + 0 > 0 " + //
        "order by e.total_point + 0 desc " + //
        "limit 5",
        user_id, group_id);
    return points;
  }

  public Logs getMostRecentLogForEachStudent(String user_id, String group_id) {
    return first((rs) -> new Logs(
        rs.getString("recent_point"),
        rs.getString("description"),
        rs.getString("total_point") //
    ), "select * " + //
        "from ent_point where user_id = ? and group_id = ? " + //
        "order by total_point + 0 desc limit 1",
        user_id, group_id);
  }

  public List<Badges> getBadgesForEachStudent(String user_id, String group_id) {
    List<Badges> badges = new ArrayList<Badges>();
    query((rs) -> badges.add(new Badges(
        rs.getString("badge_id"),
        rs.getString("value"),
        rs.getString("name"),
        rs.getString("type"),
        rs.getString("img_URL"),
        rs.getString("congrat_description") //
    )), "select e.* " + //
        "from rel_user_badge r, ent_badge e " + //
        "where r.user_id = ? and r.group_id = ? and r.badge_id = e.badge_id",
        user_id, group_id);
    return badges;
  }

  public String getTotalRecForEachStudent(String user_id, String group_id) {
    return first((rs) -> rs.getString("total_rec"), //
        "select total_rec from ent_user_rec where user_id = ? and group_id = ?",
        user_id, group_id);
  }

  public boolean updateTotalRecForEachStudent(String user_id, String group_id, String new_total) {
    if (update("update ent_user_rec set total_rec = ? where user_id = ? and group_id = ?",
        new_total, user_id, group_id) == 0) {
      update("insert into ent_user_rec(user_id, group_id, total_rec) values(?, ?, ?)",
          user_id, group_id, new_total);
      return true;
    }
    return false;
  }

  public boolean insertTotalRecForEachStudent(String user_id, String group_id, String new_total) {
    return update("insert into ent_user_rec(user_id, group_id, total_rec) values(?, ? , ?)",
        user_id, group_id, new_total) > 0;
  }

  public String getBadgeIDBasedOnValue(String value) {
    return first((rs) -> rs.getString("badge_id"), //
        "select badge_id from ent_badge where value = ?",
        value);
  }

  public Badges getBadgeById(String id) {
    return first((rs) -> new Badges(
        rs.getString("badge_id"),
        rs.getString("value"),
        rs.getString("name"),
        rs.getString("type"),
        rs.getString("img_URL"),
        rs.getString("congrat_description") //
    ), "select * from ent_badge where badge_id = ?",
        id);
  }

  public boolean insertNewBadge(
      String badge_id, String value, String name, String type, String img_URL, String congrat_description) {
    return update(
        "insert into ent_badge(badge_id, value, name, type, img_URL, congrat_description) values(?, ?, ?, ?, ?, ?)",
        badge_id, value, name, type, img_URL, congrat_description) > 0;
  }

  public boolean insertNewBadgeForEachStudent(String user_id, String group_id, String badge_id) {
    return update("insert into rel_user_badge(user_id, group_id, badge_id) values(?, ?, ?)",
        user_id, group_id, badge_id) > 0;
  }

  public boolean insertRecentPoint(
      String user_id, String group_id, String recent_point, String description, String total_point) {
    Integer value = first((rs) -> rs.getInt("total"),
        "select count(*) as total from ent_point where user_id = ? and group_id = ?",
        user_id, group_id);
    int total = value == null ? -1 : value;

    if (total != 5) {
      update("insert into ent_point(user_id, group_id, recent_point, description, total_point) " + //
          "                        values(?, ?, ?, ?, ?)",
          user_id, group_id, recent_point, description, total_point);

      for (int i = 0; i < 4; i++) {
        update("insert into ent_point(user_id, group_id, recent_point, description, total_point) " + //
            "                  values(?, ?, '0', ' ', ?)",
            user_id, group_id, -(i + 1));
      }
    } else {
      int minimum = first((rs) -> rs.getInt("minimum"),
          "select min(total_point + 0) as minimum from ent_point where user_id = ? and group_id = ?",
          user_id, group_id);

      update("update ent_point set recent_point = ?, description = ?, total_point = ? " + //
          "where user_id = ? and group_id = ? and total_point = ?",
          recent_point, description, total_point, user_id, group_id, minimum);
    }

    return true;
  }

  public boolean insertRecentPointToHistoryTable(
      String user_id, String group_id, String recent_point,
      String description, String total_point, String content_name, String provider_id) {
    return update("insert into ent_point_history " + //
        "      (user_id, group_id, recent_point, description, total_point, content_name, provider_id) " + //
        "values(?, ?, ?, ?, ?, ?, ?)",
        user_id, group_id, recent_point, description,
        total_point, content_name, provider_id) > 0;
  }

  // Register a change in the users preference regarding the GUI they are using
  // for accessing the educational content
  public boolean updateUserPreference(
      String user_id, String group_id, String session_id, String app_name,
      String param_name, String param_value, String user_context) {
    return update("insert into ent_user_preferences " + //
        "      (user_id, group_id, session_id, app_name, parameter_name, " + //
        "       parameter_value, user_context, datetime) " + //
        "values(?, ?, ?, ?, ?, ?, ?, current_timestamp(3))",
        user_id, group_id, session_id, app_name,
        param_name, param_value, user_context) > 0;
  }

  public HashMap<String, String> getLastUserPreferences(String user_id, String group_id, String app_name) {
    HashMap<String, String> userPrefs = new HashMap<String, String>();
    query((rs) -> userPrefs.put(
        rs.getString("parameter_name"),
        rs.getString("parameter_value") //
    ), "select pref.* from (" + //
        "      select parameter_name,max(datetime) as latest_date from ent_user_preferences " + //
        "      where user_id = ? and group_id = ? and app_name = ? group by parameter_name" + //
        ") latest join ent_user_preferences pref on latest.parameter_name = pref.parameter_name " +
        "and latest.latest_date = pref.datetime " +
        "where user_id = ? and group_id = ? and app_name = ?",
        user_id, group_id, app_name,
        user_id, group_id, app_name);
    return userPrefs;
  }

  public Map<String, Map<String, String>> getLastGroupPreferences(String group_id, String app_name) {
    Map<String, Map<String, String>> groupPrefs = new HashMap<String, Map<String, String>>();
    query((rs) -> {
      String userId = rs.getString("user_id");
      String parameterName = rs.getString("parameter_name");
      String parameterValue = rs.getString("parameter_value");

      groupPrefs.putIfAbsent(userId, new HashMap<String, String>());
      groupPrefs.get(userId).put(parameterName, parameterValue);
    }, "select pref.* from (" + //
        "      select user_id, parameter_name, max(datetime) as latest_date from ent_user_preferences " + //
        "      where group_id = ? and app_name = ? group by user_id,parameter_name " + //
        ") latest join ent_user_preferences pref on latest.user_id = pref.user_id " + //
        "                                       and latest.parameter_name = pref.parameter_name " + //
        "                                       and latest.latest_date =pref.datetime " +
        "where group_id = ? and app_name = ?",
        group_id, app_name,
        group_id, app_name);
    return groupPrefs;
  }
}
