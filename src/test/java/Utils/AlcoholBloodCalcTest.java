package Utils;

import static org.junit.Assert.*;

import  java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

public class AlcoholBloodCalcTest {

	@Test
	public void test() {
		Calendar calendar = Calendar.getInstance();
		System.out.println(calendar.get(Calendar.HOUR_OF_DAY)); // gets hour in 24h format
		System.out.println(calendar.get(Calendar.HOUR));        // gets hour in 12h format
		System.out.println(new Date().getTime()-new Date("11/03/18 09:33:43").getTime());  
	}

}
