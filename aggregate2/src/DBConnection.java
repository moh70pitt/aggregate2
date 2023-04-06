import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {

  protected String url;
  protected String username;
  protected String password;

  protected Connection db;

  public DBConnection(String url, String username, String password) {
    this.url = url;
    this.username = username;
    this.password = password;
  }

  protected final void log(Exception exp) {
    if (exp instanceof SQLException) {
      SQLException sql = (SQLException) exp;
      System.out.println("SQLException: " + sql.getMessage());
      System.out.println("SQLState: " + sql.getSQLState());
      System.out.println("VendorError: " + sql.getErrorCode());
    } else {
      exp.printStackTrace();
    }
  }

  public final boolean connect() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      this.db = DriverManager.getConnection(url, username, password);
      return true;
    } catch (Exception exp) {
      log(exp);
      return false;
    }
  }

  public final boolean disconnect() {
    try {
      this.db.close();
      return true;
    } catch (SQLException exp) {
      log(exp);
      return false;
    }
  }

  public final boolean isClosed() {
    try {
      return this.db.isClosed();
    } catch (SQLException exp) {
      log(exp);
      return true;
    }
  }

  public static final class Param {
    boolean ignore;
    Object value;
  }

  Param param(String queryExt, Object obj) {
    Param param = new Param();
    param.value = obj;
    param.ignore = queryExt == null;
    return param;
  }

  protected final void place(PreparedStatement statement, Object... params) throws SQLException {
    // unwrap Param objects
    List<Object> $params = new ArrayList<>();
    for (Object param : params)
      if (param instanceof Param) {
        Param wrapper = (Param) param;
        if (!wrapper.ignore)
          $params.add(wrapper.value);
      } else
        $params.add(param);

    params = $params.toArray();
    for (int i = 0; i < params.length; i++) {
      Object param = params[i];
      if (param instanceof String)
        statement.setString(1 + i, (String) param);
      else if (param instanceof Integer)
        statement.setInt(1 + i, (Integer) param);
      else if (param instanceof Double)
        statement.setDouble(1 + i, (Double) param);
    }
  }

  protected final <R> R first(Function<ResultSet, R> then, String query, Object... params) {
    try (PreparedStatement statement = db.prepareStatement(query)) {
      place(statement, params);
      ResultSet result = statement.executeQuery();
      if (result.next())
        return then.apply(result);
    } catch (Exception exp) {
      log(exp);
    }
    return null;
  }

  protected final <R> ResultSet query(Consumer<ResultSet> each, String query, Object... params) {
    try (PreparedStatement statement = db.prepareStatement(query)) {
      place(statement, params);
      ResultSet result = statement.executeQuery();
      while (result.next())
        each.accept(result);
    } catch (Exception exp) {
      log(exp);
    }
    return null;
  }

  public static interface Consumer<T> {
    void accept(T t) throws Exception;
  }

  public static interface Function<T, R> {
    R apply(T t) throws Exception;
  }

  protected final int update(String query, Object... params) {
    try (PreparedStatement statement = db.prepareStatement(query)) {
      place(statement, params);
      return statement.executeUpdate();
    } catch (Exception exp) {
      log(exp);
    }
    return 0;
  }
}
