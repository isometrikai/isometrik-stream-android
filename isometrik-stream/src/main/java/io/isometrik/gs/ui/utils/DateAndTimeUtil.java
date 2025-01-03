package io.isometrik.gs.ui.utils;


import static io.isometrik.gs.ui.utils.Constants.ONE;
import static io.isometrik.gs.ui.utils.Constants.THOUSAND;
import static io.isometrik.gs.ui.utils.Constants.ZERO;

import android.annotation.SuppressLint;
import android.content.Context;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import io.isometrik.gs.ui.R;


/*
 * Purpose – This class holds different types of Date and time conversion logic
 * @author 3Embed
 * Created on Nov 25, 2019
 * Modified on
 */
public class DateAndTimeUtil {
  @SuppressLint("DefaultLocale")
  public static String findNumberOfDays(Context context, long endTime) {
    String diffTime;
    long msDiff = Math.max(endTime, Calendar.getInstance().getTimeInMillis()) -
        Math.min(endTime, Calendar.getInstance().getTimeInMillis());
    long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
    long hoursDiff = TimeUnit.MILLISECONDS.toHours(msDiff);
    long minutesDiff = TimeUnit.MILLISECONDS.toMinutes(msDiff);
    long secondDiff = TimeUnit.MILLISECONDS.toSeconds(msDiff);
    long minutes = minutesDiff % 60;
    long seconds = secondDiff % 60;
    if (daysDiff > 1) {
      long hrs = hoursDiff % 24;
      diffTime = String.format(
          String.format("%s %%d:%%d:%%d", context.getString(R.string.dealEnds)), daysDiff, hrs,
          minutes);
    } else {
      diffTime = String.format(
          String.format("%s %%d:%%d:%%d", context.getString(R.string.dealEnds)), hoursDiff,
          minutes, seconds);
    }
    return diffTime;
  }

  @SuppressLint("DefaultLocale")
  public static String findNumberOfHours(long endTime) {
    String diffTime;
    long msDiff = Math.max(endTime, Calendar.getInstance().getTimeInMillis()) -
        Math.min(endTime, Calendar.getInstance().getTimeInMillis());
    int daysDiff = (int) TimeUnit.MILLISECONDS.toDays(msDiff);
    int hoursDiff = (int) TimeUnit.MILLISECONDS.toHours(msDiff);
    int minutesDiff = (int) TimeUnit.MILLISECONDS.toMinutes(msDiff);
    int secondDiff = (int) TimeUnit.MILLISECONDS.toSeconds(msDiff);
    int minutes = minutesDiff % 60;
    int seconds = secondDiff % 60;
    if (daysDiff > 1) {
      long hrs = hoursDiff % 24;
      diffTime = String.format(String.format("%d:%d:%d", daysDiff, hrs, minutes));
    } else if (hoursDiff > 1) {
      diffTime = String.format(String.format("%d:%d:%d", hoursDiff, minutes, seconds));
    } else {
      diffTime = String.format(String.format("%d:%d", minutes, seconds));
    }
    return diffTime;
  }

  /**
   * required time stamp in output date format input is gmt time stamp
   */
  public static String getTransactionTime(String txnTimestamp) {
    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    SimpleDateFormat output = new SimpleDateFormat("dd/MMMM/yyyy HH:mm");
    Date d = null;
    try {
      d = input.parse(txnTimestamp);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    String formatted = output.format(d);
    return formatted;
  }

  /**
   * required time stamp in output date format input is gmt time stamp
   */
  public static String getTrackingTransactionTime(String txnTimestamp) {
    Long time = Long.parseLong(txnTimestamp);
    SimpleDateFormat output = new SimpleDateFormat("dd MMMM yyyy  HH:mm");
    Date date = null;
    date = new Date(time * 1000L);
    String formatted = output.format(date);
    return formatted;
  }

    /**
     * required time stamp in output date format input is gmt time stamp
     */
    public static String getLiveStreamTime(String txnTimestamp) {
      long timestamp = Long.parseLong(txnTimestamp);
      SimpleDateFormat sdf = new SimpleDateFormat("dd EEE hh:mm a",Locale.US);
      Calendar cal = Calendar.getInstance(Locale.ENGLISH);
      cal.setTimeInMillis(timestamp);
      cal.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
      Date currenTimeZone = cal.getTime();
      return sdf.format(currenTimeZone);
    }


  /**dth MMM YYYY
   * required time stamp in output date format input is gmt time stamp
   */
  public static String getPromoCodeValidTime(String txnTimestamp) {
    Long time = Long.parseLong(txnTimestamp);
    SimpleDateFormat output = new SimpleDateFormat("MMM yyyy");
    Date date = null;
    date = new Date(time * 1000L);
    String day ="";
    try{
      int d = Integer.parseInt(new SimpleDateFormat("d").format(date));
      if (d >= 11 && d <= 13) {
        day = String.format("%d%s",d,"th");
      }else {
        switch (d % 10) {
          case 1:
            day = String.format("%d%s", d, "st");
            break;
          case 2:
            day = String.format("%d%s", d, "nd");
            break;
          case 3:
            day = String.format("%d%s", d, "rd");
            break;
          default:
            day = String.format("%d%s", d, "th");
            break;
        }
      }

    }catch (Exception e){
      day = new SimpleDateFormat("d").format(date);
    }
    String formatted = (day.isEmpty()) ? output.format(date) : String.format("%s %s",day,output.format(date));
    return formatted;
  }

  /**hh:mm a
   * required time stamp in output date format input is gmt time stamp
   */
  public static String getPromoCodeValidTimeformat(String txnTimestamp) {
    Long time = Long.parseLong(txnTimestamp);
    SimpleDateFormat output = new SimpleDateFormat("hh:mm a");
    Date date = null;
    date = new Date(time * 1000L);
    String formatted = output.format(date);
    return formatted;
  }

  public static String timeAgo(String timeFormat) {
    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    long timeAgo = 0;
    try {
      Date date = (Date) input.parse(timeFormat);
      timeAgo = date.getTime() / 1000;
    } catch (ParseException e) {
      e.printStackTrace();
    }
    long curTime = (System.currentTimeMillis()) / 1000;
    long timeElapsed = curTime - timeAgo;
    long seconds = timeElapsed;
    int minutes = Math.round(timeElapsed / 60);
    int hours = Math.round(timeElapsed / 3600);
    int days = Math.round(timeElapsed / 86400);
    int weeks = Math.round(timeElapsed / 604800);
    int months = Math.round(timeElapsed / 2600640);
    int years = Math.round(timeElapsed / 31207680);
    if (seconds <= 60) {
      return "just now";
    } else if (minutes <= 60) {
      if (minutes == 1) {
        return "a minute ago";
      } else {
        return minutes + " minutes ago";
      }
    } else if (hours <= 24) {
      if (hours == 1) {
        return "an hour ago";
      } else {
        return hours + " hours ago";
      }
    } else if (days <= 7) {
      if (days == 1) {
        return "yesterday";
      } else {
        return days + " days ago";
      }
    } else if (weeks <= 4.3) {
      if (weeks == 1) {
        return "a week ago";
      } else {
        return weeks + " weeks ago";
      }
    } else if (months <= 12) {
      if (months == 1) {
        return "a month ago";
      } else {
        return months + " months ago";
      }
    } else {
      if (years == 1) {
        return "one year ago";
      } else {
        return years + " years ago";
      }
    }
  }

  /**
   * find the remaining time
   *
   * @return String
   */
  public static String findRemainingTime(long endTime) {
    Timestamp timestamp = new Timestamp(endTime);
    return null;
  }

  /**
   * required Date for get the formatted date like Mon, MAR 10
   */
  public static String scheduleDate(Date date) {
    return new SimpleDateFormat("EEE, MMM dd", Locale.getDefault()).format(date);
  }

  /**
   * required Date for get the formatted time like 10:10 AM
   */
  public static String scheduleTime(Date date) {
    return new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(date);
  }

  /**
   * getCurrentDate get current system date.
   */
  public static Date getCurrentDate() {
    return Calendar.getInstance().getTime();
  }

  /**
   * getCalendarForBookLater get current date and set time to 10 minute above
   */
  public static Calendar getCalendarForBookLater(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.MINUTE, 10);
    return cal;
  }

//  /**
//   * required Date for get the formatted time like 1992-03-22 10:19:10
//   */
//  public static String getRideBookingDateFormat(Date date) {
//    return new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.getDefault()).format(date);
//  }

  /**
   * get the time stamp based on month
   *
   * @param index month index
   * @return timestamp
   */
  public static String getCurrentTimestamp(int index) {
    Date d = new Date();
    Calendar c = new GregorianCalendar();
    c.setTime(d);
    switch (index) {
      case ZERO:
        c.add(Calendar.MONTH, -1);
        break;
      case ONE:
        c.add(Calendar.MONTH, -6);
        break;
    }
    Date oneMonthAgo = c.getTime();
    long oneMonthAgoMillis = oneMonthAgo.getTime() / THOUSAND;
    return String.valueOf(oneMonthAgoMillis);
  }

  /**
   * get the timestamp based on year
   *
   * @param year year
   * @return timestamp
   */
  public static String getTimeStampBasedOnYear(int year) {
    String strDate = "01-01-" + year;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    Date date = null;
    try {
      date = (Date) formatter.parse(strDate);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return String.valueOf(date.getTime() / THOUSAND);
  }

  /**
   * get the timestamp based on year
   *
   * @param year year
   * @return timestamp
   */
  public static String getTimeStampEndOnYear(int year) {
    String strDate = "31-12-" + year;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    Date date = null;
    try {
      date = formatter.parse(strDate);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return String.valueOf(date.getTime() / THOUSAND);
  }
}