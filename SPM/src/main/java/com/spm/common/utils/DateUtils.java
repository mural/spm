package com.spm.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.spm.common.exception.UnexpectedException;

/**
 * @author Luciano Rey
 */
public class DateUtils {
	
	/** Seconds in a minute */
	public static final int MINUTE = 60;
	
	/** Seconds in an hour */
	public static final int HOUR = MINUTE * 60;
	
	/** Seconds in a day */
	public static final int DAY = HOUR * 24;
	
	/** Seconds in a week */
	public static final int WEEK = DAY * 7;
	
	/**
	 * Date format like yyyy-MM-ddTHH:mm:ss Z
	 */
	public static final SimpleDateFormat YYYYMMDDTHHMMSSZ_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZZZZ");
	
	/**
	 * Date format like yyyy-MM-dd HH:mm:ss Z
	 */
	public static final SimpleDateFormat YYYYMMDDHHMMSSZ_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
	
	/**
	 * Date format like 2010-10-25 21:30:00
	 */
	public static final SimpleDateFormat YYYYMMDDHHMMSS_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Date format like 10/25/2010 21:30
	 */
	public static final SimpleDateFormat MMDDYYYYHHMM_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	
	/**
	 * Date format like 10/25/2010
	 */
	public static final SimpleDateFormat MMDDYYYY_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	
	/**
	 * Date format like 25/10/2010
	 */
	public static final SimpleDateFormat DDMMYYYY_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	
	/**
	 * Date format like 10-25-2010
	 */
	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy");
	
	/**
	 * Date format like 2010-10-25
	 */
	public static final SimpleDateFormat YYYYMMDD_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * Date format like Fri 5:15 AM
	 */
	public static final SimpleDateFormat EHHMMAA_DATE_FORMAT = new SimpleDateFormat("E hh:mm aa");
	
	/**
	 * Date format like Nov 5 3:45 PM
	 */
	public static final SimpleDateFormat MMMDHHMMAA_DATE_FORMAT = new SimpleDateFormat("MMM d hh:mm aa");
	
	/**
	 * Date format like Nov 5 1985 3:45 PM
	 */
	public static final SimpleDateFormat MMMDYYYYHHMMAA_DATE_FORMAT = new SimpleDateFormat("MMM d yyyy hh:mm aa");
	
	/**
	 * Date format like Nov 5
	 */
	public static final SimpleDateFormat MMMD_DATE_FORMAT = new SimpleDateFormat("MMM d");
	
	/**
	 * Date format like November 5
	 */
	public static final SimpleDateFormat MMMMD_DATE_FORMAT = new SimpleDateFormat("MMMM d");
	
	/**
	 * Date format like 03:45 PM
	 */
	public static final SimpleDateFormat HHMMAA_DATE_FORMAT = new SimpleDateFormat("hh:mm aa");
	
	/**
	 * Date format like Friday 5 November
	 */
	public static final SimpleDateFormat EEEEDMMMM_DATE_FORMAT = new SimpleDateFormat("EEEE d MMMM");
	
	/**
	 * Date format like Friday November
	 */
	public static final SimpleDateFormat EEEEMMMM_DATE_FORMAT = new SimpleDateFormat("EEEE MMMM");
	
	public static final void init() {
		// nothing...
	}
	
	/**
	 * @param dateString The {@link String} to transform to {@link Date}
	 * @return Date
	 */
	public static Date transform(String dateString) {
		return transform(dateString, DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * @param dateString
	 * @param dateFormat
	 * @return Date
	 */
	public static Date transform(String dateString, SimpleDateFormat dateFormat) {
		Date date = null;
		if (StringUtils.isNotEmpty(dateString)) {
			try {
				date = dateFormat.parse(dateString);
			} catch (ParseException e) {
				throw new UnexpectedException(e);
			}
		}
		return date;
	}
	
	/**
	 * @param dateString The {@link String} to transform to {@link Date}
	 * @param dateFormat
	 * @return Date
	 */
	public static Date transform(String dateString, String dateFormat) {
		Date date = null;
		if (StringUtils.isNotEmpty(dateString)) {
			try {
				date = new SimpleDateFormat(dateFormat).parse(dateString);
			} catch (ParseException e) {
				throw new UnexpectedException(e);
			}
		}
		return date;
	}
	
	/**
	 * Transform the {@link Date} to a {@link String} with a format like Friday November 5th
	 * 
	 * @param date The {@link Date} to transform
	 * @return String The transformed {@link Date}
	 */
	public static String unTransformToCardinal(Date date) {
		int day = DateUtils.getDay(date);
		String ordinalSuffix = NumberUtils.getOrdinalSuffix(day);
		StringBuilder builder = new StringBuilder();
		builder.append(unTransform(date, EEEEMMMM_DATE_FORMAT));
		builder.append(" ");
		builder.append(day);
		builder.append(ordinalSuffix);
		return builder.toString();
	}
	
	/**
	 * Transform the {@link Date} to a {@link String} with the {@link DateUtils#DEFAULT_DATE_FORMAT}
	 * 
	 * @param date The {@link Date} to transform
	 * @return String The transformed {@link Date}
	 */
	public static String unTransform(Date date) {
		return unTransform(date, DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * Transform the {@link Date} to a {@link String} using the received {@link SimpleDateFormat}
	 * 
	 * @param date The {@link Date} to transform
	 * @param dateFormat The {@link DateFormat} used to transform the {@link Date}
	 * @return String The transformed {@link Date}
	 */
	public static String unTransform(Date date, DateFormat dateFormat) {
		return date != null ? dateFormat.format(date) : null;
	}
	
	/**
	 * @param date
	 * @param dateFormat
	 * @return String - date in string format
	 */
	public static String unTransform(Date date, String dateFormat) {
		return date != null ? new SimpleDateFormat(dateFormat).format(date) : null;
	}
	
	/**
	 * Creates a {@link Date} for the specified day
	 * 
	 * @param year The year
	 * @param monthOfYear The month number (starting on 0)
	 * @param dayOfMonth The day of the month
	 * @return The {@link Date}
	 */
	public static Date getDate(int year, int monthOfYear, int dayOfMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, monthOfYear, dayOfMonth);
		return calendar.getTime();
	}
	
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
	
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH);
	}
	
	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public static Date addSeconds(Date date, int seconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, seconds);
		return calendar.getTime();
	}
	
	/**
	 * Truncate the date removing hours, minutes, seconds and milliseconds
	 * 
	 * @param date The {@link Date} to truncate
	 * @return The truncated {@link Date}
	 */
	public static Date truncate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date now() {
		return new Date();
	}
}
