package Utils;

import java.util.Date;

public class BloodPressure {

	public Integer systolic;
	public Integer diastolic;
	public Date date;
	public BloodPressure() {

	}

	public BloodPressure(Integer systolic,Integer diastolic,Date date) {
		this.diastolic = diastolic;
		this.systolic = systolic;
		this.date=date;
	}

	public void setSystolic(Integer systolic) {
		this.systolic = systolic;
	}

	public void setDiastolic(Integer diastolic) {
		this.diastolic = diastolic;
	}

	public Integer getDiastolic() {
		return this.diastolic;
	}

	public Integer getSystolic() {
		return this.systolic;
	}
	
	public String toString() {
		String[] splited2 =this.date.toString().split(" ")[3].split(":");
		return "at " + Integer.parseInt(splited2[0]) + ":" + Integer.parseInt(splited2[1]) + " the measure was "
				+ this.systolic.toString() + " by " + this.diastolic.toString();
	}
}
