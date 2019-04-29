package Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Utils.Portion.Portion;

public class CustomFood {
	public final String name;
	public Date time;
	public List<Portion> portions;

	public CustomFood(String name) {
		this.name = name;
		this.time = new Date();
		this.portions = new ArrayList<>();
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final CustomFood food = (CustomFood) o;
		return this.name.equals(food.name) && this.time.equals(food.time) && this.portions.equals(food.portions);
	}

	@Override
	public String toString() {
		return name + " taken in " + time + ".\nhas:\n" + portions.toString();
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public List<Portion> getPortions() {
		return portions;
	}

	public void setPortions(List<Portion> portions) {
		this.portions = portions;
	}

	public String getName() {
		return name;
	}

	public void addPortion(Portion p) {
		this.portions.add(p);
	}
}
