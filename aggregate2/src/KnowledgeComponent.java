import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class KnowledgeComponent {
	private int id;
	private String idName;
	private String displayName;
	private String topicName;
	private double threshold1;
	private double threshold2;
	private HashMap<String,double[]> contents; // contains the content names as keys and  [weight, importance(0/1), contributes(0/1)] of the relationship
	

	public KnowledgeComponent(int id, String idName, String displayName, String topicName, double th1, double th2) {
		super();
		this.id = id;
		this.idName = idName;
		this.displayName = displayName;
		this.topicName = topicName;
		this.threshold1  = th1;
		this.threshold2  = th2;
		contents = new HashMap<String,double[]>();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdName() {
		return idName;
	}
	public void setIdName(String idName) {
		this.idName = idName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	
	
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	
	public HashMap<String,double[]> getContents() {
		return contents;
	}
	public void setContents(HashMap<String,double[]> contents) {
		this.contents = contents;
	}
	
	public String serializeContents(){
		String r = "";
		if(contents != null && !contents.isEmpty()) {
			for (Map.Entry<String, double[]> content : contents.entrySet()){
				r += "\"" + content.getKey() +"\",";
			}
		}
			
		if(r.charAt(r.length()-1) == ',') r = r.substring(0,r.length()-1);
		return r;
	}
	
	
	public boolean contentExist(String otherContentName){
		if(contents != null) return contents.containsKey(otherContentName);
		return false;
	}
	
	
		
	public double getThreshold1() {
		return threshold1;
	}

	public void setThreshold1(double threshold1) {
		this.threshold1 = threshold1;
	}

	public double getThreshold2() {
		return threshold2;
	}

	public void setThreshold2(double threshold2) {
		this.threshold2 = threshold2;
	}

	public String toJsonString(){
		return "{"+"\"id\": "+getId()+",\"n\": \""+getIdName()+"\", \"dn\": \""+getDisplayName()+"\", \"cnt\": ["+serializeContents()+"], \"t\": \""+getTopicName()+"\"}";
	}
	
	public ArrayList<String> getContentList(boolean filterImportance, int contributesMatch){
		ArrayList<String> res = new ArrayList<String>();
		if(contents != null && !contents.isEmpty()) {
			for (Map.Entry<String, double[]> content : contents.entrySet()){
				double[] v = content.getValue();
				if(((int)v[2]) == contributesMatch){
					if((filterImportance && v[1] > 0) || !filterImportance) res.add(content.getKey());
					
				}
				

			}
		}
		return res;
	}
	
	public int getLevelCategory(double k){
		if(k<this.getThreshold1()) return 0;
		if(k>=this.getThreshold2()) return 2;
		return 1;
	}
}
