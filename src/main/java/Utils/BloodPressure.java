package Utils;

import java.util.Date;

/** this class holds blood presure's measure data
 * @author Shalev Kuba
 * @since 2018-12-26
 * */
public class BloodPressure {

	public Integer systolic;
	public Integer diastolic;
	public Date date;

	public BloodPressure() {

	}

	public BloodPressure(final Integer systolic, final Integer diastolic, final Date date) {
		this.diastolic = diastolic;
		this.systolic = systolic;
		this.date = date;
	}

	public void setSystolic(final Integer systolic) {
		this.systolic = systolic;
	}

	public void setDiastolic(final Integer diastolic) {
		this.diastolic = diastolic;
	}

	public Integer getDiastolic() {
		return this.diastolic;
	}

	public Integer getSystolic() {
		return this.systolic;
	}

	@Override
	public String toString() {
		final String[] splited2 = this.date.toString().split(" ")[3].split(":");
		return "at " + Integer.parseInt(splited2[0]) + ":" + Integer.parseInt(splited2[1]) + " the measure was "
				+ this.systolic.toString() + " by " + this.diastolic.toString();
	}
}
