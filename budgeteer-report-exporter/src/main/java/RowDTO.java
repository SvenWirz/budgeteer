import java.util.Date;

public class RowDTO { 
	private Date from;
	private Date until;
	private String budget;
	private double netto;
	private double brutto;
	private double hours;
	private double nettoRestmenge;
	private double bruttoRestmenge;
	private double leistungsstand;
	private String rechnungsEmpfaenger;

	
	
	public Date getFrom() {
		return from;
	}



	public void setFrom(Date from) {
		this.from = from;
	}



	public Date getUntil() {
		return until;
	}



	public void setUntil(Date until) {
		this.until = until;
	}



	public String getBudget() {
		return budget;
	}



	public void setBudget(String budget) {
		this.budget = budget;
	}



	public double getNetto() {
		return netto;
	}



	public void setNetto(double netto) {
		this.netto = netto;
	}



	public double getBrutto() {
		return brutto;
	}



	public void setBrutto(double brutto) {
		this.brutto = brutto;
	}



	public double getHours() {
		return hours;
	}



	public void setHours(double hours) {
		this.hours = hours;
	}



	public double getNettoRestmenge() {
		return nettoRestmenge;
	}



	public void setNettoRestmenge(double nettoRestmenge) {
		this.nettoRestmenge = nettoRestmenge;
	}



	public double getBruttoRestmenge() {
		return bruttoRestmenge;
	}



	public void setBruttoRestmenge(double bruttoRestmenge) {
		this.bruttoRestmenge = bruttoRestmenge;
	}



	public double getLeistungsstand() {
		return leistungsstand;
	}



	public void setLeistungsstand(double leistungsstand) {
		this.leistungsstand = leistungsstand;
	}
	



	public String getRechnungsEmpfaenger() {
		return rechnungsEmpfaenger;
	}



	public void setRechnungsEmpfaenger(String rechnungsEmpfaenger) {
		this.rechnungsEmpfaenger = rechnungsEmpfaenger;
	}



	@Override
	public String toString() {
		return "RowDTO [from=" + from + ", until=" + until + ", budget=" + budget + ", netto=" + netto + ", brutto="
				+ brutto + ", hours=" + hours + ", nettoRestmenge=" + nettoRestmenge + ", bruttoRestmenge="
				+ bruttoRestmenge + ", leistungsStand=" + leistungsstand + ", Rechnungsempfaenger="
				+ rechnungsEmpfaenger + "]";
	}
	
	
}
