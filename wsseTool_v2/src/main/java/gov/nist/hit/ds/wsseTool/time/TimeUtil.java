package gov.nist.hit.ds.wsseTool.time;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenience method around the joda time library
 * 
 * @author gerardin
 * 
 */

public class TimeUtil {

	private static final Logger log = LoggerFactory.getLogger(TimeUtil.class);

	public static DateTime getCurrentDate() {
		return new DateTime();
	}

	/**
	 * date of the 2011-02-22T16:35:43.734Z
	 * 
	 * @param date
	 *            string representation of the date
	 * @return true if date conforms, false otherwise
	 */
	public static boolean isDateInUTCFormat2(String date) {
		try {
			DateTimeFormatter parser = ISODateTimeFormat.dateTime().withOffsetParsed();
			parser.parseDateTime(date);
		} catch (Exception e1) {
			try{
				DateTimeFormatter XML_DATE_TIME_FORMAT = ISODateTimeFormat.dateTimeNoMillis().withOffsetParsed();
				XML_DATE_TIME_FORMAT.parseDateTime(date);
				return true;
			}
			catch(Exception e2){
				return false;
			}
		}
		return true;
	}
	
	public static boolean isDateInUTCFormat(String date) {
		try {
			DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
			parser.parseDateTime(date);
		} catch (Exception e1) {
			return false;
		}
		return true;
	}


	/**
	 * date of the form 2011-02-22T16:35:43-06:00 or 2011-02-22T16:35:43.734Z
	 * 
	 * @param date
	 *            string representation of the date
	 * @return true if date conforms, false otherwise
	 */
	public static boolean isDateInUTCorTimeZoneFormat(String date) {
		try {
			new DateTime(date);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static DateTime parseDateString(String date) {
		DateTime d = null;

		try {
			DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
			//per saml2.0 spec : SAML system entities SHOULD NOT rely on time resolution finer than milliseconds.
			d = parser.parseDateTime(date).withMillisOfSecond(0);
			
		} catch (Exception e) {
			log.error("could parse date string {}", date);
		}

		return d;
	}
}
