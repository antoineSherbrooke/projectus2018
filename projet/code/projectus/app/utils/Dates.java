package utils;

import models.SprintTasks;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Dates {


    public static final long minute = 60000;
    public static final long hour = 60*minute;
    public static final long day = 24* hour;


    public static Date getEndOfDay(Date day) {
        Calendar calendar = Calendar.getInstance();
        if (day == null) {
            day = new Date();
        }
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,      calendar.getMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND,      calendar.getMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMaximum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    public static Date getStartOfDay(Date day) {
        Calendar calendar = Calendar.getInstance();
        if (day == null) {
            day = new Date();
        }
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,      calendar.getMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND,      calendar.getMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
        return calendar.getTime();
    }


    public static long dayDiff(Date date1, Date date2){
        long diff = date1.getTime() - date2.getTime();
        return (1 - TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)) + 1;
    }


    public static String millisecondsToString(long ms, Unit precision) {
        Long days = ms/day;
        Long hours = (ms - days*day)/hour;
        Long mins = (ms - days*day -hours*hour)/minute;
        String result = "";

        result += " " + ((days>0)? days.toString() + " day" : "");
        result += ((days>1)? "s" : "");
        if (precision.getValue() >= Unit.HOURS.getValue()) {
            result += " " + ((hours>0)? hours.toString() + " hour" : "");
            result += ((hours>1)? "s" : "");
        }
        if (precision.getValue() >= Unit.MINUTES.getValue()) {
            result += " " + ((mins<10)? "0" : "");
            result += ((mins>0)? mins.toString() + " minute" : "");
            result += ((mins>1)? "s" : "");
        }

        return result;
    }

    public static String toStringTimeFormat (int time) {
        String stringToReturn = time/60 + "h";
        if (time % 60 < 10) {
            stringToReturn += "0";
        }
        stringToReturn += time%60;
        return stringToReturn;
    }

    public static LocalDate toLocalDate(Date source) {
        return source.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public enum Unit{
        DAYS(0), HOURS(1),MINUTES(2);
        private int value;
        Unit(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
}
