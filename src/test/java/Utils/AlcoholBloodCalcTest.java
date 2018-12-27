package Utils;

import java.util.ArrayList;
import  java.util.Calendar;
import org.junit.Test;


import static org.junit.Assert.*;
@SuppressWarnings("static-method")
public class AlcoholBloodCalcTest {


	@Test
	public void testByWikipediaExample1() {
		AlcoholBloodCalc c=new AlcoholBloodCalc().setWeight(80).setForMale();
		ArrayList<Portion> l=new ArrayList<Portion>();
		
		Calendar twoHoursAgo=Calendar.getInstance();
		twoHoursAgo.add(Calendar.HOUR_OF_DAY, -2);
		
		Portion SD1=new Portion();
		SD1.setAlchohol_by_volume(100);
		SD1.setAmount(12.7);
		SD1.setTime(twoHoursAgo.getTime());
		l.add(SD1);
		
		Portion SD2=new Portion();
		SD2.setAlchohol_by_volume(100);
		SD2.setAmount(12.7);
		SD2.setTime(twoHoursAgo.getTime());
		l.add(SD2);
		
		Portion SD3=new Portion();
		SD3.setAlchohol_by_volume(100);
		SD3.setAmount(12.7);
		SD3.setTime(twoHoursAgo.getTime());
		l.add(SD3);
		
		assertEquals(0.0325,c.CalcForNow(l),0.0001);
	}
	
	@Test
	public void testByWikipediaExample2() {
		AlcoholBloodCalc c=new AlcoholBloodCalc().setWeight(70).setForFemale();
		ArrayList<Portion> l=new ArrayList<Portion>();
		
		Calendar twoHoursAgo=Calendar.getInstance();
		twoHoursAgo.add(Calendar.HOUR_OF_DAY, -2);
		
		Portion SD1=new Portion();
		SD1.setAlchohol_by_volume(100);
		SD1.setAmount(12.7);
		SD1.setTime(twoHoursAgo.getTime());
		l.add(SD1);
		
		Portion SD2=new Portion();
		SD2.setAlchohol_by_volume(100);
		SD2.setAmount(12.7);
		SD2.setTime(twoHoursAgo.getTime());
		l.add(SD2);
		
		Portion halfSD=new Portion();
		halfSD.setAlchohol_by_volume(50);
		halfSD.setAmount(12.7);
		halfSD.setTime(twoHoursAgo.getTime());
		l.add(halfSD);
		
		assertEquals(0.03649,c.CalcForNow(l),0.0001);
	}
	
	@Test
	public void trivialTest() {
		assertEquals(0,new AlcoholBloodCalc().CalcForNow(new ArrayList<Portion>()),0);
	}
}
