
import java.util.*;



public class Logs{
  String value;
  String description;
  String totalPoint;

  public Logs(){
    
  }
  public Logs(String value, String description, String totalPoint){
    this.value = value;
    this.description = description;
    this.totalPoint = totalPoint;
  }
  public void setValue(String value){
    this.value = value;
  }
  public String getValue(){
    return this.value;
  }
  public void setDescription(String description){
    this.description = description;
  }
  public String getDescription(){
    return this.description;
  }
  public void setTotalPoint(String totalPoint){
    this.totalPoint = totalPoint;
  }
  public String getTotalPoint(){
    return this.totalPoint;
  }
}
