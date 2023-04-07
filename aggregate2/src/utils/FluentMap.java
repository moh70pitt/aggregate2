package utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class FluentMap {

  private Map<String, Object> map = new HashMap<>();

  private static Object unwrap(Object object) {
    if (object instanceof FluentMap)
      return ((FluentMap) object).map;
    return object;
  }

  private static Object[] unwrap(Object... objects) {
    Object[] unwrapped = new Object[objects.length];
    for (int i = 0; i < objects.length; i++)
      unwrapped[i] = unwrap(objects[i]);
    return unwrapped;
  }

  public static Object[] array(Object... objects) {
    return unwrap(objects);
  }

  public static FluentMap map() {
    return new FluentMap();
  }

  public FluentMap put(String key, Object value) {
    map.put(key, unwrap(value));
    return this;
  }

  public FluentMap extend(FluentMap map) {
    this.map.putAll(map.map);
    return this;
  }

  @Override
  public String toString() {
    return new JSONObject(map).toString();
  }

  public static void main(String[] args) {
    FluentMap map = map();
    FluentMap map1 = map();

    map.put("test", map1);
    
    System.out.println(map);
    map1.put("1", "two").put("1", "two");

    System.out.println(map);
  }
}
