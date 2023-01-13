package karma.converter.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateTimeUtils {

    public static final SimpleDateFormat[] ACCEPTED_TIMESTAMP_FORMATS = {
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US),
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US),
            new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US),
            new SimpleDateFormat("dd,MMM,yyyy HH:mm:ss aa", Locale.US),
            new SimpleDateFormat("dd MMM,yyyy HH:mm:ss aa", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US),
            new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US),
            new SimpleDateFormat("dd-MMM-yyyy", Locale.US),
            new SimpleDateFormat("dd MMM, yyyy", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd", Locale.US),
    };

    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final SimpleDateFormat PATTERN_YYYY_MM_DD_HH_MM = new SimpleDateFormat("dd-MMM-yyyy | hh:mm:ss a", Locale.US);
    public static final SimpleDateFormat PATTERN_DD_MM_YYYY = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    public static final SimpleDateFormat ddMMMYY = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    public static final SimpleDateFormat mmYYMMHHMMSSFormat = new SimpleDateFormat("'yyyy-MM-dd HH:mm:ss'", Locale.US);
    public static final SimpleDateFormat mmYYMonthformat = new SimpleDateFormat("MM-yyyy", Locale.US);
    public static final SimpleDateFormat mmmYYMonthformat = new SimpleDateFormat("MMM-yyyy", Locale.US);
    public static final SimpleDateFormat ddmmyyyyFormat = new SimpleDateFormat("dd MMM,yyyy hh:mm aa", Locale.US);
    public static final SimpleDateFormat ddmmmmyyyyFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.US);
    public static final SimpleDateFormat hhmmaFormat = new SimpleDateFormat("hh:mm a", Locale.US);
    public static final int SECOND_MILLIS = 1000;
    public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    public static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    public static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final long SEC = 1000;
    private static final long MIN = SEC * 60;
    private static final long HOUR = MIN * 60;
    private static final long DAY = HOUR * 24;
    public static final long YESTERDAY = DAY * 2;
    private static final long YEAR = DAY * 365;
    private static final long MONTH;
    private static final int[] DAY_IN_MONTH;

    static {
        int dayInFebruary = 28;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        if (year % 4 == 0 && year % 100 == 0 && year % 400 == 0) {
            dayInFebruary = 29;
        }
        DAY_IN_MONTH = new int[]{31, dayInFebruary, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        MONTH = DAY * (DAY_IN_MONTH[Calendar.getInstance().get(Calendar.MONTH)]);
    }

    private DateTimeUtils() {
    }

    public static long getTimeFromStringDate(String format, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        try {
            return sdf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getFormattedDate(SimpleDateFormat format, long time) {
        return format.format(time);
    }

    public static String getFormattedDate(String format, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        return sdf.format(date);
    }

    public static Date parseTimestamp(String timestamp) {
        for (SimpleDateFormat format : ACCEPTED_TIMESTAMP_FORMATS) {
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                return format.parse(timestamp);
            } catch (ParseException ex) {
                continue;
            }
        }

        // All attempts to parse have failed
        return null;
    }


    /**
     * Returns the GMT offset in seconds as a String.
     *
     * @return GMT offset in seconds
     */
    public static String getGMTOffsetInSeconds() {
        return String.valueOf(getGMTOffsetInSecondsLong());
    }

    public static long getGMTOffsetInSecondsLong() {
        final Calendar calendar = new GregorianCalendar();
        final TimeZone timeZone = calendar.getTimeZone();
        return TimeUnit.SECONDS.convert(timeZone.getRawOffset() + (timeZone.inDaylightTime(new Date()) ? timeZone.getDSTSavings() : 0), TimeUnit.MILLISECONDS);
    }

}
