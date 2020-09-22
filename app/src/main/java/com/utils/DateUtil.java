
package com.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Utility class for date manipulation.
 * This class gives a simple interface for common Date, Calendar and Timezone
 * operations.
 * It is possible to apply subsequent transformations to an initial date, and
 * retrieve the changed Date object at any point.
 *
 */
public class DateUtil {
    
    //-------------------------------------------------------------- Attributes
    private Calendar cal;
    
    //------------------------------------------------------------ Constructors
    
    /** Inizialize a new instance with the current date */
    public DateUtil() {
        this(new Date());
    }
    
    /** Inizialize a new instance with the given date */
    public DateUtil(Date d) {
        cal = Calendar.getInstance();
        cal.setTime(d);
      
    }
    
    //---------------------------------------------------------- Public methods
    public String comapreDate(Date date1, Date date2){
        if(date1.equals(date2)){
            return "equals";
        }
        if(date1.before(date2)){
            return "before";
        }
        if(date1.after(date2)){
            return "after";
        }
        return null;
    }
    /** Set a new time */
    public void setTime(Date d) {
        cal.setTime(d);
    }
    
    /** Get the current time */
    public Date getTime() {
        return cal.getTime();
    }
    
    /** Get the current TimeZone */
    public String getTZ() {
        return cal.getTimeZone().getID();
    }
    
    /**
     * Convert the time to the midnight of the currently set date.
     * The internal date is changed after this call.
     *
     * @return a reference to this DateUtil, for concatenation.
     */
    public DateUtil toMidnight() {
        
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
        
        return this;
    }
    
    /**
     * Convert the time to the midnight of the currently set date.
     * The internal date is changed after this call.
     *
     * @return a reference to this DateUtil, for concatenation.
     */
    public DateUtil getDateWithTime(int hr, int min, int sec, int milliSec) {
        
        cal.set(Calendar.HOUR_OF_DAY, hr);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);
        cal.set(Calendar.MILLISECOND,milliSec);
        
        return this;
    }
    
    /**
     * Convert the time to the midnight of the currently set date.
     * The internal date is changed after this call.
     *
     * @return a reference to this DateUtil, for concatenation.
     */
    public DateUtil getDateWithDateTime(int year, int month, int date, int hr, int min, int sec, int milliSec) {
        
    	cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, date);
        cal.set(Calendar.HOUR_OF_DAY, hr);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);
        cal.set(Calendar.MILLISECOND,milliSec);
        
        return this;
    }
    
    /**
     * Convert the time to the midnight of the currently set date.
     * The internal date is changed after this call.
     *
     * @return a reference to this DateUtil, for concatenation.
     */
    public DateUtil toEndOfDay() {
        
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND,0);
        
        return this;
    }
    
    /**
     * This method is used to return current time stamp
     * @return
     */
    public static Timestamp getCurrentTimeStamp()
    {
    	Date dt = new Date();
    	return (new Timestamp(dt.getTime()));
    }
    /**
     * Convert the current time to the start of week time of the currently set date.
     * The internal date is changed after this call.
     *
     * @return a reference to this DateUtil, for concatenation.
     */
    public DateUtil startOfWeek() {
    	cal.set(Calendar.DAY_OF_WEEK, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
        
        return this;
    }
    
    /**
     * Convert the current time to the end of week time of the currently set date.
     * The internal date is changed after this call.
     *
     * @return a reference to this DateUtil, for concatenation.
     */
    public DateUtil endOfWeek() {
    	cal.set(Calendar.DAY_OF_WEEK, 7);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND,0);
        
        return this;
    }
    
    /**
     * Convert the current time to the start of week time of the currently set date.
     * The internal date is changed after this call.
     *
     * @return a reference to this DateUtil, for concatenation.
     */
    public DateUtil startOfMonth() {
    	cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
        
        return this;
    }
    
    /**
     * Convert the current time to the end of week time of the currently set date.
     * The internal date is changed after this call.
     *
     * @return a reference to this DateUtil, for concatenation.
     */
    public DateUtil endOfMonth(int lastDayOfMonth) {
    	cal.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND,0);
        
        return this;
    }
    
    /**
     * This method takes input long date value and date format type, and converts the date into given date format.  
     * @param date
     * @param formatType
     * @return
     */
    public String myDateFormatter(long date , String formatType){
    	String dateString = "";
    	try {
		  // Create Date object.
    	  Date d = new Date(date);
    	  //Create object of SimpleDateFormat and pass the desired date format.
    	  SimpleDateFormat sdf = new SimpleDateFormat(formatType);
    	  dateString = sdf.format(d);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	return dateString;
    }
    public String usingDateFormatter(long input){
        Date date = new Date(input);
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setCalendar(cal);
        cal.setTime(date);
        return sdf.format(date);
    }

    public static String formatDate(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd", Locale.getDefault());
        return dateFormat.format(timeInMillis*1000L);
    }
    public static String formatTime(long timeInMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return dateFormat.format(timeInMillis*1000L);
    }

    public static boolean hasSameDate(long millisFirst, long millisSecond) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return dateFormat.format(millisFirst*1000L).equals(dateFormat.format(millisSecond*1000L));
    }
}
