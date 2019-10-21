package com.ftf.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TimeUtils {

    public static String NormalFormat = "yyyy-MM-dd HH:mm:ss";
    public static String YMDFormat = "yyyy-MM-dd";

    /**
     * 由出生日期获得年龄
     *
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static int getAge(String birthDay, String formal) throws Exception {
        Date date = getDateFromTime(formal, birthDay);

        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(date);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        return age;
    }

    /**
     * 根据当前日期获得上周的开始时间
     *
     * @return
     * @author zhaoxuepu
     */
    public static String getLastWeekBegin(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar calendar1 = Calendar.getInstance();
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
        int offset1 = 1 - dayOfWeek;
        calendar1.add(Calendar.DATE, offset1 - 7);
        String lastBeginDate = sdf.format(calendar1.getTime());
        return lastBeginDate;
    }

    /**
     * 根据当前日期获得上周的结束时间
     *
     * @return
     * @author zhaoxuepu
     */
    public static String getLastWeekEnd(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
        int offset2 = 7 - dayOfWeek;
        calendar2.add(Calendar.DATE, offset2 - 7);
        String lastEndDate = sdf.format(calendar2.getTime());
        return lastEndDate;
    }

    /**
     * 根据当前日期获得本周的开始时间
     *
     * @return
     * @author zhaoxuepu
     */
    public static String getLastWeekBeginV2(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar calendar1 = Calendar.getInstance();
        int dayWeek = calendar1.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
        if (1 == dayWeek) {
            calendar1.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
        calendar1.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天  
        int day = calendar1.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
        calendar1.add(Calendar.DATE, calendar1.getFirstDayOfWeek() - day);
        String lastBeginDate = sdf.format(calendar1.getTime());
        return lastBeginDate;
    }

    /**
     * 根据当前日期获得本周的结束时间
     *
     * @return
     * @author zhaoxuepu
     */
    public static String getLastWeekEndV2(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar calendar1 = Calendar.getInstance();
        int dayWeek = calendar1.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天  
        if (1 == dayWeek) {
            calendar1.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
        calendar1.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天  
        int day = calendar1.get(Calendar.DAY_OF_WEEK);
//	    根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
        calendar1.add(Calendar.DATE, calendar1.getFirstDayOfWeek() - day);
//	    String imptimeBegin = sdf.format(calendar1.getTime());
//	    System.out.println("所在周星期一的日期：" + imptimeBegin);  
        calendar1.add(Calendar.DATE, 6);
        String lastEndDate = sdf.format(calendar1.getTime());
        return lastEndDate;


    }

    /**
     * 今天在当前月中的第几天
     *
     * @return
     */
    public static int getDayWithMonth() {
        Date date = new Date();
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        int a = ca.get(Calendar.DAY_OF_MONTH);
        return a;
    }

    /**
     * 今天在当前年中的第几天
     *
     * @return
     */
    public static int getDayWithYear() {
        Date date = new Date();
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        int i = ca.get(Calendar.DAY_OF_YEAR);
        return i;
    }


    /**
     * 当前月第一天
     */
    public static String getCurMonthBeginDay(String format) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        //设置为第一天
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = sf.format(gcLast.getTime());
        //打印本月第一天
//		System.out.println(day_first);
        return day_first;
    }

    /**
     * 当前月最后一天
     */
    public static String getCurMonthLastDay(String format) {
        DateFormat df = new SimpleDateFormat(format);
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = df.format(ca.getTime());
        return last;
    }

    /**
     * 2个时间比大小
     *
     * @param time1
     * @param time2
     * @param format
     * @return
     * @throws ParseException
     */
    public static int compareDate(String time1, String time2, String format) throws ParseException {
        DateFormat df = new SimpleDateFormat(format);
        java.util.Date d1 = df.parse(time1);
        java.util.Date d2 = df.parse(time2);

        if (d1.getTime() > d2.getTime()) {
            return 1;
        } else if (d1.getTime() < d2.getTime()) {
            return -1;
        } else {// 相等
            return 0;
        }
    }

    /**
     * 2个时间比大小
     * 默认格式为yyyy-mm-dd hh:mm:ss
     *
     * @param time1
     * @param time2
     * @param format
     * @return 1:arg1>arg2,-1:arg2>arg1,0 :相等
     * @throws ParseException
     */
    public static int compareDate(String time1, String time2) throws ParseException {
        DateFormat df = new SimpleDateFormat(NormalFormat);
        java.util.Date d1 = df.parse(time1);
        java.util.Date d2 = df.parse(time2);

        if (d1.getTime() > d2.getTime()) {
            return 1;
        } else if (d1.getTime() < d2.getTime()) {
            return -1;
        } else {// 相等
            return 0;
        }
    }

    /**
     * 2个时间相差天数
     *
     * @param time1
     * @param time2
     * @param format
     * @return
     * @throws ParseException11
     */
    public static int getDayCompareDate(String time1, String time2, String format) throws ParseException {
        DateFormat df = new SimpleDateFormat(format);
        java.util.Date d1 = df.parse(time1);
        java.util.Date d2 = df.parse(time2);

        long mis = d1.getTime() - d2.getTime();
        return (int) (mis / (24 * 60 * 60 * 1000));
    }


    /**
     * 2个时间相差小时
     *
     * @param time1
     * @param time2
     * @param format
     * @return
     * @throws ParseException
     */
    public static int getHourCompareDate(String time1, String time2, String format) throws ParseException {
        DateFormat df = new SimpleDateFormat(format);
        java.util.Date d1 = df.parse(time1);
        java.util.Date d2 = df.parse(time2);

        long mis = d1.getTime() - d2.getTime();
        return (int) (mis / (60 * 60 * 1000));
    }

    /**
     * 2个时间相差分钟
     *
     * @param time1
     * @param time2
     * @param format
     * @return
     * @throws ParseException
     */
    public static int getMinCompareDate(String time1, String time2, String format) throws ParseException {
        DateFormat df = new SimpleDateFormat(format);
        java.util.Date d1 = df.parse(time1);
        java.util.Date d2 = df.parse(time2);

        long mis = d1.getTime() - d2.getTime();
        return (int) (mis / (60 * 1000));
    }

    /**
     * 时间转毫秒数
     */
    public static long getMsecFromData(String dateTime, String format) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(new SimpleDateFormat(format).parse(dateTime));
            return c.getTimeInMillis();
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 毫秒数转时间
     *
     * @param sstime
     * @param format
     * @return
     */
    public static String getDataByMsec(String sstime, String format) {
        try {
            Date date = new Date(sstime);
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            return "";
        }
    }

    /**
     * 毫秒数转时间
     *
     * @return
     */
    public static String getTimeFromMis(long mis, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);// 设置日期格式
        String time = df.format(new Date(mis));
        return time;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);// 设置日期格式
        String time = df.format(new Date());
        return time;
    }

    public static void main(String[] args) {
        List<String> monthEveryDays = getMonthEveryDays("2019-10-14");
        System.err.println(monthEveryDays);
    }

    /**
     * 获取当前时间（无参数）
     *
     * @return
     */
    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 字符串转date
     *
     * @param formal
     * @param time
     * @return
     */
    public static Date getDateFromTime(String format, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);// 小写的mm表示的是分钟
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获得某个时间后几天的时间
     *
     * @param format 时间格式
     * @param time   当前时间
     * @return
     */
    public static String getTimeDayAfter(String format, String time, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);// 小写的mm表示的是分钟
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return getTimeDayAfter(format, date, day);
    }

    /**
     * 获得某个时间后几天的时间
     *
     * @param format 时间格式
     * @param time   当前时间
     * @return
     */
    public static String getTimeDayAfter(String format, Date date, int day) {
        SimpleDateFormat dft = new SimpleDateFormat(format);// 设置日期格式

        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);

        return dft.format(now.getTime());
    }

    /**
     * 获得某个时间后几天的时间(毫秒数)
     *
     * @param format 时间格式
     * @param time   当前时间
     * @return
     */
    public static long getMisDayAfter(Date date, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);

        return now.getTime().getTime();
    }

    /**
     * 获得某个时间后几个小时的时间
     *
     * @param format 时间格式
     * @param time   当前时间
     * @return
     */
    public static String getTimeHoursAfter(String format, Date date, int hour) {
        SimpleDateFormat dft = new SimpleDateFormat(format);// 设置日期格式

        Calendar dar = Calendar.getInstance();
        dar.setTime(date);
        dar.add(java.util.Calendar.HOUR_OF_DAY, hour);

        return dft.format(dar.getTime());
    }

    /**
     * 获得某个时间后几分钟的时间
     *
     * @param format 时间格式
     * @param time   当前时间
     * @return
     */
    public static String getTimeMinuteAfter(String format, Date date, int minute) {
        SimpleDateFormat dft = new SimpleDateFormat(format);// 设置日期格式

        Calendar dar = Calendar.getInstance();
        dar.setTime(date);
        dar.add(java.util.Calendar.MINUTE, minute);

        return dft.format(dar.getTime());
    }


    /**
     * 获得某个时间后几分钟的时间
     *
     * @param format 时间格式
     * @param time   当前时间
     * @return
     */
    public static String getTimeMinuteAfter(String format, String time, int minute) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);// 小写的mm表示的是分钟
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return getTimeMinuteAfter(format, date, minute);
    }

    /**
     * 获得某个时间后几天的时间
     *
     * @param format 时间格式
     * @param time   当前时间
     * @return
     */
    public static String getTimeDayAfterV2(String format, String time, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);// 小写的mm表示的是分钟
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return getTimeDayAfterV2(format, date, day);
    }

    /**
     * 获得某个时间前几天的时间
     *
     * @param format 时间格式
     * @param time   当前时间
     * @return
     */
    public static String getTimeDayAfterV2(String format, Date date, int day) {
        SimpleDateFormat dft = new SimpleDateFormat(format);// 设置日期格式
//
//		Calendar now = Calendar.getInstance();
//		now.setTime(date);
//		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
//
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -day);
        date = calendar.getTime();

        return dft.format(date).toString();
    }

    /**
     * 取得当月天数
     */
    public static int getCurrentMonthLastDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 得到指定月的天数
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取当月第几天
     */
    public static int getDayByChooseDay() {
        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前月份
     */
    public static int getMonthOfYear() {
        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        return month;
    }

    //获取昨天日期
    public static String getYesterdayDate(String fotmat) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date time = cal.getTime();
        return new SimpleDateFormat(fotmat).format(time);

    }


    /**
     * 仅显示年月日，例如 2015-08-11.
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 显示年月日时分秒，例如 2015-08-11 09:51:53.
     */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";




    /**
     * 获取某年某月每一天日期
     *
     * @return
     */
    public static List<String> getMonthEveryDays(String dateStr) {
        List<String> dateList = new ArrayList<>();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            Integer num = getDaysByYearMonth(year, month);
            for (int i = 1; i <= num; i++) {
                dateList.add(String.format("%s-%02d-%02d", year, month, i));
            }
            return dateList;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateList;
    }


    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * @Description: 时间转字符串
     * @Author LuoJing
     * @Date 2019/10/16 12:09
     */
    public static String getStrDateToStr(String strDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(date);
    }

}
