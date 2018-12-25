package Utils;

public class AlcoholBloodCalc {
	
	private final double WIB=0.806; // constant. Percentage of water body in blood.
	private int SD; // Standard drinks. Number of 10 grams of ethanol.
	private final double CF=1.2; // Conversion factor
	private double BW=0.58; //body water constant. 0.58 for male and 0.49 for female.
	private double WT=67.0; //body weight in KG
	private double MR=0.015; //metabolism constant. 0.015 for male and 0.017 for females
	private int DP; // drinking period in hours.
	
	AlcoholBloodCalc setForMale() {
		this.BW=0.58;
		this.MR=0.015;
		return this;
	}
	
	AlcoholBloodCalc setForFemale() {
		this.BW=0.49;
		this.MR=0.017;
		return this;
	}

	AlcoholBloodCalc setWeight(double w) {
		this.WT=w;
		return this;
	}
	
}
