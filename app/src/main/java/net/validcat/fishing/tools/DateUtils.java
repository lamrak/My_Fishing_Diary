package net.validcat.fishing.tools;

import android.content.Context;
import android.text.format.Time;

import net.validcat.fishing.R;
import net.validcat.fishing.data.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static final String DATE_FORMAT = "yyyyMMdd";

    static String formatDate(long dateInMilliseconds) {
        Date date = new Date(dateInMilliseconds);
        return DateFormat.getDateInstance().format(date);
    }

    public static String getFullFriendlyDayString(Context context, long dateInMillis) {

        String day = ""; // getDayName(context, dateInMillis);
        return String.format(context.getString(
                R.string.format_full_friendly_date,
                getFormattedMonthDay(context, dateInMillis)), Locale.getDefault()); // day,
    }

    /**
     * Converts db date format to the format "Month day", e.g "June 24".
     * @param context Context to use for resource localization
     * @param dateInMillis The db formatted date string, expected to be of the form specified
     *                in Utility.DATE_FORMAT
     * @return The day in the form of a string formatted "December 6"
     */
    public static String getFormattedMonthDay(Context context, long dateInMillis) {
        Time time = new Time();
        time.setToNow();
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        SimpleDateFormat monthDayFormat = new SimpleDateFormat(context.getString(R.string.date_format), Locale.getDefault());

        return monthDayFormat.format(dateInMillis);
    }

    /**
     * Given a day, returns just the name to use for that day.
     * E.g "today", "tomorrow", "wednesday".
     *
     * @param context Context to use for resource localization
     * @param dateInMillis The date in milliseconds
     * @return
     */
    public static String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if ( julianDay == currentJulianDay +1 ) {
            return context.getString(R.string.tomorrow);
        } else {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }

    public static String getSeason(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        switch (cal.get(Calendar.MONTH)) {
            case 12:
            case 1:
            case 2:
                return Constants.WINTER;
            case 3:
            case 4:
            case 5:
                return Constants.SPRING;
            case 6:
            case 7:
            case 8:
                return Constants.SUMMER;
            case 9:
            case 10:
            case 11:
                return Constants.AUTUMN;
        }
        return Constants.SUMMER;
    }
}


