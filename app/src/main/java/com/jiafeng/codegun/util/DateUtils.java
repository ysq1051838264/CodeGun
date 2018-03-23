package com.jiafeng.codegun.util;


import android.content.Context;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期工具类
 *
 * @author lufoz
 */
public class DateUtils {

    public static final String FORMAT_SHORT = "yyyy-MM-dd";
    public static final String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_TIME = "yyyy年MM月dd日 HH:mm";
    public static final String FORMAT_TIME_YMD = "yyyy年MM月dd日";
    public static final String FORMAT_MONTH = "yyyy-MM";
    public static final String FORMAT_HOUR_MIN = "HH:mm";
    public static final String[] weekStrs = new String[]{"周日", "周一", "周二",
            "周三", "周四", "周五", "周六"};

    /**
     * 获取String型系统日期 yyyy-MM-dd
     *
     * @return String格式时间
     */
    public static String currentDate() {
        return dateToString(new Date());
    }

    public static Date getCurrentDate() {
        return stringToDate(currentDate());
    }

    /**
     * 获取String型系统日期 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String currentTime() {
        return dateToString(new Date(), FORMAT_LONG);
    }

    public static Date getCurrentTime() {
        return stringToDate(dateToString(new Date(), FORMAT_LONG), FORMAT_LONG);
    }

    /**
     * 获取当天零点的时间
     *
     * @return
     */
    public static Date getCurrentStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 字符串转日期 yyyy-MM-dd
     *
     * @param dateStr
     * @return
     */
    public static Date stringToDate(String dateStr) {
        Locale systime = Locale.getDefault();
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_SHORT, systime);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串转xxxx年xx月xx日特定格式字符串
     *
     * @param dateStr
     * @return
     */
    public static String stringToFormatString(String dateStr) {
        return DateUtils.dateToString(DateUtils.stringToDate(dateStr), DateUtils.FORMAT_TIME_YMD);
    }

    /**
     * 字符转日期，针对任意格式
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static Date stringToDate(String dateStr, String format) {
        Locale systime = Locale.getDefault();
        SimpleDateFormat sdf = new SimpleDateFormat(format, systime);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期转字符串，yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        return dateToString(date, FORMAT_SHORT);
    }

    /**
     * 日期转字符串，yyyy-MM
     *
     * @param date
     * @return
     */
    public static String dateToMonthString(Date date) {
        return dateToString(date, FORMAT_MONTH);
    }

    /**
     * string转定制的时间字符串HH:mm
     *
     * @param string
     * @return
     */
    public static String stringFormatMinString(String string) {
        return DateUtils.dateToHourMinString(DateUtils.stringToDate(string, DateUtils.FORMAT_LONG));
    }

    /**
     * string转定制的时间+15分钟后转成字符串HH:mm
     *
     * @param string
     * @return
     */
    public static String stringFormatMinAdd15MinString(String string, int index) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.stringToDate(string, DateUtils.FORMAT_LONG));
        calendar.add(Calendar.MINUTE, 15 * index);
        return DateUtils.dateToHourMinString(calendar.getTime());
    }

    /**
     * 日期转字符串，HH:mm
     *
     * @param date
     * @return
     */
    public static String dateToHourMinString(Date date) {
        return dateToString(date, FORMAT_HOUR_MIN);
    }

    /**
     * string转字符串，HH:mm
     *
     * @param dateStr
     * @return
     */
    public static String stringToHourMinString(String dateStr) {
        return dateToString(stringToDate(dateStr, FORMAT_LONG), FORMAT_HOUR_MIN);
    }

    /**
     * 日期转字符串，针对任意格式
     *
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date, String format) {
        Locale systime = Locale.getDefault();
        SimpleDateFormat sdf = new SimpleDateFormat(format, systime);
        return sdf.format(date);
    }

    public static String dateToStringForTitle(Date date) {
        Date today = getCurrentDate();
        int differDay = getDaysDiff(today, date);
        switch (differDay) {
            case 0:
                return "今天";
            case 1:
                return "昨天";
            case 2:
                return "前天";
            default:
                return dateToString(date);
        }
    }

    public static int getAge(Date birthday) {
        return getAge(new Date(), birthday);
    }

    public static int getAge(Date measureDate, Date birthday) {
        return measureDate.getYear() - birthday.getYear();
    }

    /**
     * 把日期精确到天
     *
     * @param date
     * @return
     */
    public static Date getDayPrecision(Date date) {
        return stringToDate(dateToString(date));
    }


    /**
     * 获取两个日期相差的天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDaysDiff(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        diff = diff / 86400000;// 1000*60*60*24;
        return (int) diff;
    }

    /**
     * 对比数据分享的时候，获取两个日期相差的天数、小时数、分钟数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getTimeDiff(Date startDate, Date endDate) {
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数

        long diff = endDate.getTime() - startDate.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            Date date1 = dateFormat.parse(dateToString(startDate));
            Date date2 = dateFormat.parse(dateToString(endDate));
            day = (date2.getTime() - date1.getTime()) / (24 * 3600 * 1000);//计算差多少天
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long hour = diff % nd / nh;//计算差多少小时
        long min = diff % nd % nh / nm;//计算差多少分钟

        if (day > 0) {
            return (int) day;
        } else if (hour > 0) {
            return (int) hour;
        } else {
            return (int) min;
        }

    }

    /**
     * 对比数据分享的时候，获取两个日期相差的天数、小时数、分钟数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getTimeUnitDiff(Date startDate, Date endDate) {
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数

        long diff = endDate.getTime() - startDate.getTime();

        long day = 0;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = dateFormat.parse(dateToString(startDate));
            Date date2 = dateFormat.parse(dateToString(endDate));
            day = (date2.getTime() - date1.getTime()) / (24 * 3600 * 1000);//计算差多少天
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long hour = diff % nd / nh;//计算差多少小时
        long min = diff % nd % nh / nm;//计算差多少分钟

        if (day > 0) {
            return "天";
        } else if (hour > 0) {
            return "小时";
        } else {
            return "分钟";
        }
    }

    @SuppressWarnings("deprecation")
    public static int getMonthsDiff(Date startDate, Date endDate) {
        int months = 0;// 相差月份
        int y1 = startDate.getYear();
        int y2 = endDate.getYear();
        months = endDate.getMonth() - startDate.getMonth() + (y2 - y1) * 12;
        return months;
    }

    public static Date getDate(Date date, int dateType, int value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(dateType, value);
        return cal.getTime();
    }

    /**
     * 获取这天是这个月的第几天
     *
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    /**
     * 获取与指定日期相差value天的日期,正数往后推，负数往前推
     *
     * @param date
     * @param value
     * @return
     */
    public static Date getDifferDay(Date date, int value) {
        return getDate(date, Calendar.DATE, value);
    }

    /**
     * 获取与指定日期相差value个月的日期,正数往后推，复数往前推
     *
     * @param date
     * @param value
     * @return
     */
    public static Date getDifferMonth(Date date, int value) {
        return getDate(date, Calendar.MONTH, value);
    }

    /**
     * 获取与指定日期相差value年的日期,正数往后推，复数往前推
     *
     * @param date
     * @param value
     * @return
     */
    public static Date getDifferYear(Date date, int value) {
        return getDate(date, Calendar.YEAR, value);
    }

    /**
     * 给定日期返回这个日期所处于的月份(返回这个月的第一天)
     *
     * @param date
     * @return
     */
    public static Date getMonthFromDay(Date date) {
        return stringToDate((date.getYear() + 1900) + "-"
                + (date.getMonth() + 1) + "-01");
    }

    /**
     * 获取制定日期的当月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 给定日期返回这个日期所处于的季度（返回这个季度的第一天）
     *
     * @param date
     * @return
     */
    public static Date getSeasonFromDay(Date date) {
        String season = "01";
        int m = date.getMonth();
        if (m >= 3 && m < 6) {
            season = "04";
        } else if (m >= 6 && m < 9) {
            season = "07";
        } else if (m >= 9) {
            season = "10";
        }
        return stringToDate((date.getYear() + 1900) + "-" + season + "-01");
    }

    /**
     * 给定日期返回这个日期处于的年份（返回这个年份的第一天）
     *
     * @param date
     * @return
     */
    public static Date getYearFromDay(Date date) {
        return stringToDate((date.getYear() + 1900) + "-01-01");
    }

    /**
     * 把HH:mm转为成日期
     *
     * @param timeString
     * @return
     */
    public static long getClockTime(String timeString) {
        Calendar calendar = Calendar.getInstance();
        long time = 0;
        String[] strs = timeString.split(":");
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(strs[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(strs[1]));
        // 将秒和毫秒设置为0
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        time = calendar.getTimeInMillis();
        if (time < System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1);
        }
        time = calendar.getTimeInMillis();
        return time;
    }

    /**
     * 获取系统的时间：hh：mm
     *
     * @return
     */
    public static int[] getTimeData() {
        int[] time = new int[2];
        Calendar cal = Calendar.getInstance();
        time[0] = cal.get(Calendar.HOUR_OF_DAY);
        time[1] = cal.get(Calendar.MINUTE);
        return time;
    }

    public static String getTimeDataString() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        return hour + ":" + minute;
    }


    public static String getHistoryTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_LONG);
        String[] time = sdf.format(date).split(" ");
        return time[1];

    }

    public static int[] getData(Date date) {

        String[] infos = DateUtils.dateToString(date).split("\\-");
        int[] dates = new int[infos.length];
        for (int i = 0; i < infos.length; i++) {
            dates[i] = Integer.parseInt(infos[i]);
        }

        return dates;
    }

    public static boolean isStandMaxMonth(Date birthday) {
        int[] brithdays = DateUtils.getData(birthday);
        int[] nows = DateUtils.getData(new Date());

        int month = (nows[0] - brithdays[0]) * 12 + nows[1] - brithdays[1];

        if (nows[2] >= brithdays[2]) {
            month = month + 1;
        } else {
            month = month - 1;
        }
        return month < 36;
    }

    @SuppressWarnings("deprecation")
    public static boolean isSameDay(Date date0, Date date1) {
        return date0.getYear() == date1.getYear()
                && date0.getMonth() == date1.getMonth()
                && date0.getDate() == date1.getDate();
    }

    public static boolean isSameMonth(Date date0, Date date1) {
        return date0.getYear() == date1.getYear()
                && date0.getMonth() == date1.getMonth();
    }

    @SuppressWarnings("deprecation")
    public static String getWeekString(Date date) {
        return weekStrs[date.getDay()];

    }

    /**
     * @publishTime 发表时间
     */
    public static String convertShortTip(String publishTime) {
        return convertShortTip(DateUtils.stringToDate(publishTime, DateUtils.FORMAT_LONG));
    }

    public static String getTimestampString(Context context, Date messageDate) {
        Locale curLocale = context.getResources().getConfiguration().locale;
        Date today = new Date();
        String languageCode = curLocale.getLanguage();

        boolean isChinese = languageCode.contains("zh");

        String format;

        if (isSameDay(today, messageDate)) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(messageDate);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            format = "HH:mm";

            if (hour > 17) {
                if (isChinese) {
                    format = "晚上 hh:mm";
                }

            } else if (hour >= 0 && hour <= 6) {
                if (isChinese) {
                    format = "凌晨 hh:mm";
                }
            } else if (hour > 11 && hour <= 17) {
                if (isChinese) {
                    format = "下午 hh:mm";
                }

            } else {
                if (isChinese) {
                    format = "上午 hh:mm";
                }
            }
        } else if (isSameDay(DateUtils.getDate(today, Calendar.DATE, -1), messageDate)) {
            if (isChinese) {
                format = "昨天 HH:mm";
            } else {
                format = "MM-dd HH:mm";
            }
        } else {
            if (isChinese) {
                format = "M月d日 HH:mm";
            } else {
                format = "MM-dd HH:mm";
            }
        }

        if (isChinese) {
            return new SimpleDateFormat(format, Locale.CHINA).format(messageDate);
        } else {
            return new SimpleDateFormat(format, Locale.US).format(messageDate);
        }
    }

    /**
     * @publishTime 发表时间
     */
    public static String convertShortTip(Date d) {
        if (d == null) {
            return "";
        }
        Date today = new Date();
        long sec = (System.currentTimeMillis() - d.getTime()) / 1000; // 获取两者相差的秒数

        if (sec < 60) {
            return "刚刚";
        } else if (sec < 60 * 60) {
            return (sec / 60) + "分钟前";
        } else if (sec < 60 * 60 * 24) {
            return (sec / (60 * 60)) + "小时前";
        } else {
            Date lastYear = getDifferYear(today, -1);
            if (lastYear.getTime() < d.getTime()) {
                //在今年之内
                Date lastMonth = getDifferMonth(today, -1);
                if (lastMonth.getTime() < d.getTime()) {
                    //在一个月之内
                    float diff = (today.getTime() - d.getTime()) / (60 * 60 * 24 * 1000.0f);
                    int dayDiff = (int) diff;
                    /*if (diff - dayDiff > 0.000001f) {
                        dayDiff++;
                    }*/
                    return dayDiff + "天前";
                }
                for (int i = 1; i <= 12; i++) {
                    Date monthDate = getDifferMonth(today, -i);
                    if (monthDate.getTime() < d.getTime()) {
                        return (i - 1) + "个月前";
                    }
                }
                return "";
            } else {
                int year = today.getYear() - d.getYear();
                if (year == 1)
                    return "去年";
                else
                    return year + "年前";
            }
        }
    }


    /**
     * 分钟转成xxx小时xx分
     *
     * @param min
     * @return
     */
    public static String minTransformToStr(int min) {
        return min / 60 + "小时" + min % 60 + "分";
    }

    /**
     * 分钟转成xxx小时xx分
     *
     * @param min
     * @return
     */
    public static String[] minTransformToStr2(int min) {
        return new String[]{min / 60 + "小时", min % 60 + "分"};
    }


    public static String getSportTime(int minute) {
        if (minute > 60) {
            int i = minute / 60;
            int j = minute % 60;
            return i + " 小时 " + j + " 分钟";

        } else if (minute == 60) {
            return "1 小时";
        } else {
            return minute + " 分钟";
        }
    }

    /**
     * 获取时区,正数表示东时区,负数表示西时区
     */
    public static int getTimeZone() {

        TimeZone timeZone = TimeZone.getDefault();
        int millSecond = timeZone.getRawOffset();
        return millSecond / (1000 * 60 * 60);
    }
}
