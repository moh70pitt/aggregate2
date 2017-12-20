import java.util.ArrayList;


/*
 * levels[0] = k;
   levels[1] = progress;
   levels[2] = att;
   levels[3] = sr;
   levels[4] = completion; // completion for now is just progress
   levels[5] = anns;
   levels[6] = likes;
   levels[7] = time;
   levels[8] = subs;
 */
public class Activity {
	private String name;
	private double[] levels;
	//private ArrayList<Double> attemptSeqRes;
	private String attemptsResSeq; // 1.0,0.0,0.0,1.0
	public Activity(String name) {
		super();
		this.name = name;
		this.attemptsResSeq = "";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double[] getLevels() {
		return levels;
	}
	public void setLevels(double[] levels) {
		this.levels = levels;
	}
//	public ArrayList<Double> getAttemptSeqRes() {
//		return attemptSeqRes;
//	}
//	public void setAttemptSeqRes(ArrayList<Double> attemptSeqRes) {
//		this.attemptSeqRes = attemptSeqRes;
//	}
//	public String getAttSeqAsString(){
//		String res = "";
//		if(attemptSeqRes != null && attemptSeqRes.size() == 0){
//			for(Double a : attemptSeqRes){
//				res += a +",";
//			}
//		}
//		return res;
//	}
	public String getAttemptsResSeq() {
		return attemptsResSeq;
	}
	public void setAttemptsResSeq(String attemptsResSeq) {
		this.attemptsResSeq = attemptsResSeq;
	}
	
	
}
