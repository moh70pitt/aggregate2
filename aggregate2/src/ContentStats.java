import static utils.FluentMap.*;

import utils.FluentMap;

public class ContentStats {
  private String name;
  private String providerId;
  private double a_p10;
  private double a_p25;
  private double a_p33;
  private double a_p50;
  private double a_p66;
  private double a_p75;
  private double a_p80;
  private double a_p85;
  private double a_p90;
  private double t_p10;
  private double t_p25;
  private double t_p33;
  private double t_p50;
  private double t_p66;
  private double t_p75;
  private double t_p80;
  private double t_p85;
  private double t_p90;
  private double sr_p10;
  private double sr_p25;
  private double sr_p33;
  private double sr_p50;
  private double sr_p66;
  private double sr_p75;
  private double sr_p80;
  private double sr_p85;
  private double sr_p90;

  public ContentStats(String name, String providerId,
      double a_p10, double a_p25, double a_p33, double a_p50, double a_p66, double a_p75, double a_p80, double a_p85,
      double a_p90,
      double t_p10, double t_p25, double t_p33, double t_p50, double t_p66, double t_p75, double t_p80, double t_p85,
      double t_p90,
      double sr_p10, double sr_p25, double sr_p33, double sr_p50, double sr_p66, double sr_p75, double sr_p80,
      double sr_p85, double sr_p90) {
    super();
    this.name = name;
    this.providerId = providerId;
    this.a_p10 = a_p10;
    this.a_p25 = a_p25;
    this.a_p33 = a_p33;
    this.a_p50 = a_p50;
    this.a_p66 = a_p66;
    this.a_p75 = a_p75;
    this.a_p80 = a_p80;
    this.a_p85 = a_p85;
    this.a_p90 = a_p90;
    this.t_p10 = t_p10;
    this.t_p25 = t_p25;
    this.t_p33 = t_p33;
    this.t_p50 = t_p50;
    this.t_p66 = t_p66;
    this.t_p75 = t_p75;
    this.t_p80 = t_p80;
    this.t_p85 = t_p85;
    this.t_p90 = t_p90;
    this.sr_p10 = sr_p10;
    this.sr_p25 = sr_p25;
    this.sr_p33 = sr_p33;
    this.sr_p50 = sr_p50;
    this.sr_p66 = sr_p66;
    this.sr_p75 = sr_p75;
    this.sr_p80 = sr_p80;
    this.sr_p85 = sr_p85;
    this.sr_p90 = sr_p90;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getProviderId() {
    return providerId;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  public double getA_p10() {
    return a_p10;
  }

  public void setA_p10(double a_p10) {
    this.a_p10 = a_p10;
  }

  public double getA_p25() {
    return a_p25;
  }

  public void setA_p25(double a_p25) {
    this.a_p25 = a_p25;
  }

  public double getA_p33() {
    return a_p33;
  }

  public void setA_p33(double a_p33) {
    this.a_p33 = a_p33;
  }

  public double getA_p50() {
    return a_p50;
  }

  public void setA_p50(double a_p50) {
    this.a_p50 = a_p50;
  }

  public double getA_p66() {
    return a_p66;
  }

  public void setA_p66(double a_p66) {
    this.a_p66 = a_p66;
  }

  public double getA_p75() {
    return a_p75;
  }

  public void setA_p75(double a_p75) {
    this.a_p75 = a_p75;
  }

  public double getA_p80() {
    return a_p80;
  }

  public void setA_p80(double a_p80) {
    this.a_p80 = a_p80;
  }

  public double getA_p85() {
    return a_p85;
  }

  public void setA_p85(double a_p85) {
    this.a_p85 = a_p85;
  }

  public double getA_p90() {
    return a_p90;
  }

  public void setA_p90(double a_p90) {
    this.a_p90 = a_p90;
  }

  public double getT_p10() {
    return t_p10;
  }

  public void setT_p10(double t_p10) {
    this.t_p10 = t_p10;
  }

  public double getT_p25() {
    return t_p25;
  }

  public void setT_p25(double t_p25) {
    this.t_p25 = t_p25;
  }

  public double getT_p33() {
    return t_p33;
  }

  public void setT_p33(double t_p33) {
    this.t_p33 = t_p33;
  }

  public double getT_p50() {
    return t_p50;
  }

  public void setT_p50(double t_p50) {
    this.t_p50 = t_p50;
  }

  public double getT_p66() {
    return t_p66;
  }

  public void setT_p66(double t_p66) {
    this.t_p66 = t_p66;
  }

  public double getT_p75() {
    return t_p75;
  }

  public void setT_p75(double t_p75) {
    this.t_p75 = t_p75;
  }

  public double getT_p80() {
    return t_p80;
  }

  public void setT_p80(double t_p80) {
    this.t_p80 = t_p80;
  }

  public double getT_p85() {
    return t_p85;
  }

  public void setT_p85(double t_p85) {
    this.t_p85 = t_p85;
  }

  public double getT_p90() {
    return t_p90;
  }

  public void setT_p90(double t_p90) {
    this.t_p90 = t_p90;
  }

  public double getSr_p10() {
    return sr_p10;
  }

  public void setSr_p10(double sr_p10) {
    this.sr_p10 = sr_p10;
  }

  public double getSr_p25() {
    return sr_p25;
  }

  public void setSr_p25(double sr_p25) {
    this.sr_p25 = sr_p25;
  }

  public double getSr_p33() {
    return sr_p33;
  }

  public void setSr_p33(double sr_p33) {
    this.sr_p33 = sr_p33;
  }

  public double getSr_p50() {
    return sr_p50;
  }

  public void setSr_p50(double sr_p50) {
    this.sr_p50 = sr_p50;
  }

  public double getSr_p66() {
    return sr_p66;
  }

  public void setSr_p66(double sr_p66) {
    this.sr_p66 = sr_p66;
  }

  public double getSr_p75() {
    return sr_p75;
  }

  public void setSr_p75(double sr_p75) {
    this.sr_p75 = sr_p75;
  }

  public double getSr_p80() {
    return sr_p80;
  }

  public void setSr_p80(double sr_p80) {
    this.sr_p80 = sr_p80;
  }

  public double getSr_p85() {
    return sr_p85;
  }

  public void setSr_p85(double sr_p85) {
    this.sr_p85 = sr_p85;
  }

  public double getSr_p90() {
    return sr_p90;
  }

  public void setSr_p90(double sr_p90) {
    this.sr_p90 = sr_p90;
  }

  public String asJSON() {
    return "{\"name\":\"" + this.getName() + "\",\"provider_id\":\"" + this.providerId + "\"," +
        "\"a_p10\":" + this.a_p10 + "," +
        "\"a_p25\":" + this.a_p25 + "," +
        "\"a_p33\":" + this.a_p33 + "," +
        "\"a_p50\":" + this.a_p50 + "," +
        "\"a_p66\":" + this.a_p66 + "," +
        "\"a_p75\":" + this.a_p75 + "," +
        "\"a_p80\":" + this.a_p80 + "," +
        "\"a_p85\":" + this.a_p85 + "," +
        "\"a_p90\":" + this.a_p90 + "," +
        "\"t_p10\":" + this.t_p10 + "," +
        "\"t_p25\":" + this.t_p25 + "," +
        "\"t_p33\":" + this.t_p33 + "," +
        "\"t_p50\":" + this.t_p50 + "," +
        "\"t_p66\":" + this.t_p66 + "," +
        "\"t_p75\":" + this.t_p75 + "," +
        "\"t_p80\":" + this.t_p80 + "," +
        "\"t_p85\":" + this.t_p85 + "," +
        "\"t_p90\":" + this.t_p90 + "," +
        "\"sr_p10\":" + this.sr_p10 + "," +
        "\"sr_p25\":" + this.sr_p25 + "," +
        "\"sr_p33\":" + this.sr_p33 + "," +
        "\"sr_p50\":" + this.sr_p50 + "," +
        "\"sr_p66\":" + this.sr_p66 + "," +
        "\"sr_p75\":" + this.sr_p75 + "," +
        "\"sr_p80\":" + this.sr_p80 + "," +
        "\"sr_p85\":" + this.sr_p85 + "," +
        "\"sr_p90\":" + this.sr_p90 + "}";
  }

  public String asPartialJSON() {
    return "\"aP\":[" + this.a_p10 + "," + this.a_p25 + "," + this.a_p33 + "," + this.a_p50 + "," + this.a_p66 + ","
        + this.a_p75 + "," + this.a_p80 + "," + this.a_p85 + "," + this.a_p90 + "]," +
        "\"tP\":[" + this.t_p10 + "," + this.t_p25 + "," + this.t_p33 + "," + this.t_p50 + "," + this.t_p66 + ","
        + this.t_p75 + "," + this.t_p80 + "," + this.t_p85 + "," + this.t_p90 + "]," +
        "\"srP\":[" + this.sr_p10 + "," + this.sr_p25 + "," + this.sr_p33 + "," + this.sr_p50 + "," + this.sr_p66 + ","
        + this.sr_p75 + "," + this.sr_p80 + "," + this.sr_p85 + "," + this.sr_p90 + "]";
  }

  public FluentMap asPartialJSON2() {
    return map()
        .put("aP",
            array(this.a_p10, this.a_p25, this.a_p33, this.a_p50,
                this.a_p66, this.a_p75, this.a_p80, this.a_p85,
                this.a_p90))
        .put("tP",
            array(this.t_p10, this.t_p25, this.t_p33, this.t_p50,
                this.t_p66, this.t_p75, this.t_p80, this.t_p85,
                this.t_p90))
        .put("srP", array(this.sr_p10, this.sr_p25, this.sr_p33,
            this.sr_p50, this.sr_p66, this.sr_p75, this.sr_p80,
            this.sr_p85, this.sr_p90));
  }
}
