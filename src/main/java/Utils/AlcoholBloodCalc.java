/** This class Calculates the percentage of alcohol in blood by given 
 *  alcoholic drinks and the time they have been consumed.
 *  The calculation relies on the EBAC formula.
 * @author Or Feldman
 * @since 2018-12-25*/
package Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;


public class AlcoholBloodCalc {
	
	 static double diffInHours(Date d1,Date d2) {
		return ((d1.getTime()-d2.getTime())/(60*60*1000));
	}
	
	private final double WIB=0.806; // constant. Percentage of water body in blood.
	private double SD; // Standard drinks. Number of 10 grams of ethanol.
	private final double CF=1.2; // Conversion factor
	private double BW=0.58; //body water constant. 0.58 for male and 0.49 for female.
	private double WT=67.0; //body weight in KG
	private double MR=0.015; //metabolism constant. 0.015 for male and 0.017 for females
	private double DP; // drinking period in hours.
	
	public AlcoholBloodCalc setForMale() {
		this.BW=0.58;
		this.MR=0.015;
		return this;
	}
	
	public AlcoholBloodCalc setForFemale() {
		this.BW=0.49;
		this.MR=0.017;
		return this;
	}

	public AlcoholBloodCalc setWeight(double w) {
		this.WT=w;
		return this;
	}
	
	public double CalcForNow(List<Portion> drinks){
		
		
		Calendar nowC=Calendar.getInstance();
		nowC.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
		Date now=nowC.getTime();
		List<Portion> relevantDrinks=drinks.stream().filter(p->diffInHours(now,p.getTime())<=10).collect(Collectors.toList()); 
		
		for(Portion p : relevantDrinks)
			if(diffInHours(now, p.getTime())>DP)
				DP=diffInHours(now, p.getTime());
		
		double alcInMilis=0;
		for(Portion p: relevantDrinks)
			alcInMilis+=p.getAmount()*(p.getAlchohol_by_volume()/100);
		
		SD=alcInMilis/12.7; // 12.7 Milliliters is one standard drink
		
		double result= (((CF * SD * WIB) / (BW * WT)) - DP * MR); // calculating according to EBAC formula
		if(result<=0)
			return 0;
		return result;
	}
	
}
