import java.util.ArrayList;


public class KnowledgeComponentGroup extends KnowledgeComponent {
	
	private ArrayList<KnowledgeComponent> kcs;
	private KnowledgeComponent mainKc;
	
	public KnowledgeComponentGroup(int id, String idName, String displayName, String topicName){
		super(id,idName,displayName,topicName, 0, 0);
		mainKc = null;
		kcs = new ArrayList<KnowledgeComponent>();
	}

	public ArrayList<KnowledgeComponent> getKcs() {
		return kcs;
	}

	public void setKcs(ArrayList<KnowledgeComponent> kcs) {
		this.kcs = kcs;
	}
	
	public String serializeKCs(){
		String r = "";
		if(kcs != null) for(KnowledgeComponent kc : kcs){
			r +=  kc.getId() +",";
		}
		if(r.charAt(r.length()-1) == ',') r = r.substring(0,r.length()-1);
		return r;
	}
	public String toJsonString(){
		return "{"+"\"id\": "+getId()+",\"n\": \""+getIdName()+"\", \"dn\": \""+getDisplayName()+"\", "
				+ "\"kcs\": ["+serializeKCs()+"],"
				+ "\"cnt\": ["+serializeContents()+"], \"t\": \""+getTopicName()+"\"}";
	}
	
	public boolean kcExist(KnowledgeComponent kc){
		for(KnowledgeComponent myKc : kcs) if(kc.getId() == myKc.getId()) return true;
		return false;
	}
	
	public int cardinality(){
		return kcs.size();
	}

	public KnowledgeComponent getMainKc() {
		return mainKc;
	}

	public void setMainKc(KnowledgeComponent mainKc) {
		this.mainKc = mainKc;
	}
	
	
}
