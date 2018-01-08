
import java.util.*;



public class Badges {
  String id;
  String value;
  String name;
  String type;
  String imgURL;
  String congradulationMSG;

  public Badges(String id, String value, String name, String type, String imgURL, String congradulationMSG){
    this.id = id;
    this.value = value;
    this.name = name;
    this.type = type;
    this.imgURL= imgURL;
    this.congradulationMSG = congradulationMSG;
  }

  public void setId(String id){
	  this.id = id;
  }
  public String getId(){
    return this.id;
  }
  public void setValue(String value){
    this.value= value;
  }
  public String getValue(){
    return this.value;
  }
  public void setName(String name){
    this.name = name;
  }
  public String getName(){
    return this.name;
  }
  public void setType(String type){
    this.type = type;
  }
  public String getType(){
    return this.type;
  }
  public void setImgURL(String imgURL){
    this.imgURL = imgURL;
  }
  public String getImgURL(){
    return this.imgURL;
  }
  public void setCongradulationMSG(String congradulationMSG){
    this.congradulationMSG = congradulationMSG;
  }
  public String getCongradualationMSG(){
    return this.congradulationMSG;
  }
}
