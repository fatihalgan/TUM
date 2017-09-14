/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.util;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author db2admin
 */
public class ServiceSupervisionFeeUtil {
    
    public static int findSupervisionDateIncreaseDates(int makeCallTimeFrame, Date supervisionDate) {
        Calendar after999Days = Calendar.getInstance();
	after999Days.add(Calendar.DAY_OF_YEAR, 999);
        return calculateDaysToAdd(supervisionDate, after999Days.getTime(), makeCallTimeFrame);
    }
    
    public static int findServiceFeeDateIncreaseDates(int receiveCallTimeFrame, Date serviceFeeDate) {
        Calendar after999Days = Calendar.getInstance();
	after999Days.add(Calendar.DAY_OF_YEAR, 999);
        return calculateDaysToAdd(serviceFeeDate, after999Days.getTime(), receiveCallTimeFrame);
    }
    
    public static int calculateDaysToAdd(Date current, Date maximum, int desired) {
        long delta = (maximum.getTime() - current.getTime()) / (86400 * 1000);
        if(delta <= 0) return 0;
        else if(delta <= desired) return (int)(delta - 1);
        else return desired;
    }
}
