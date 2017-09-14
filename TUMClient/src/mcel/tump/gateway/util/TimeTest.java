package mcel.tump.gateway.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class TimeTest {

	public static void main(String[] args) {
		Date now = new Date();
		Timestamp start = new Timestamp(now.getYear(), now.getMonth(), now.getDate(), 0, 0, 0, 0);
		System.out.println(start.toLocaleString());
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Timestamp end =  new Timestamp(cal.getTimeInMillis());
		System.out.println(end.toLocaleString());
	}
}
