package com.allever.lose.weight.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Mac on 18/3/9.
 */

public class DateUtil {
    private static final String TAG = "DateUtil";
    public static final String FORMAT_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_MM_dd_HH_mm = "MM-dd HH:mm";
    public static final String FORMAT_HH_mm = "HH:mm";
    public static final String FORMAT_MM_dd = "MM-dd";

    public static String formatTime(long seconds, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(seconds);
    }

//    /**
//     * 时间戳转换成日期格式字符串
//     * @param seconds 时间戳
//     * @return
//     */
//    public static String time2Date(long seconds) {
//        String format = "yyyy-MM-dd HH:mm:ss";
//        SimpleDateFormat sdf = new SimpleDateFormat(format);
//        return sdf.format(seconds);
//    }

    public static int getYear(long second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(second);
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    public static int getMonth(long second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(second);
        int month = calendar.get(Calendar.MONTH) + 1;
        return month;
    }

    public static int getDay(long second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(second);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    /**
     * 判断年份是否为闰年
     * 判断闰年的条件， 能被4整除同时不能被100整除，或者能被400整除
     */
    public static boolean isLeapYear(int year) {
        boolean isLeapYear = false;
        if (year % 4 == 0 && year % 100 != 0) {
            isLeapYear = true;
        } else if (year % 400 == 0) {
            isLeapYear = true;
        }
        return isLeapYear;
    }

    public static String formatHourMinute(int hour, int minute) {
        StringBuilder builder = new StringBuilder();
        if (hour < 10) {
            builder.append("0" + hour);
        } else {
            builder.append(hour);
        }
        builder.append(": ");
        if (minute < 10) {
            builder.append("0" + minute);
        } else {
            builder.append(minute);
        }
        return builder.toString();
    }

    /**
     * 秒数格式化分钟
     *
     * @param
     */
    public static String second2FormatMinute(int second) {
        StringBuilder builder = new StringBuilder();
        int minute = second / 60;
        int seconds = second % 60;
        builder.append(minute + ": " + seconds);
        return builder.toString();
    }

    /**
     * 获取月份天数
     */
    public static int getMonthDayCount(int year, int month) {
        int dayCount = 30;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                dayCount = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                dayCount = 30;
                break;
            case 2:
                if (DateUtil.isLeapYear(year)) {
                    dayCount = 29;
                } else {
                    dayCount = 28;
                }
                break;
            default:
                break;

        }
        return dayCount;
    }


    /**
     * 求两个时间点的日期集合
     */
    public static List<Date> getIntervalDateList(Date startDate, Date endDate) {
        List<Date> dateList = new ArrayList<>();
        int interval = getIntervalDays(startDate, endDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        for (int i = 0; i < interval; i++) {
            dateList.add(calendar.getTime());
            Log.d(TAG, "getIntervalDateList: day = " + DateUtil.getDay(calendar.getTimeInMillis()));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.d(TAG, "getIntervalDateList: day = " + DateUtil.getDay(calendar.getTimeInMillis()));
        dateList.add(endDate);
        Log.d(TAG, "getIntervalDateList: dateList.size = " + dateList.size());
        return dateList;
    }

    /**
     * 求两个时间点的日期集合
     */
    public static List<String> getIntevalDayStrList(Date startDate, Date endDate) {
        List<String> list = new ArrayList<>();
        for (Date date : getIntervalDateList(startDate, endDate)) {
            list.add(String.valueOf(getDay(date.getTime())));
        }
        return list;
    }


    /**
     * 获取两个时间点的天数
     */
    public static int getIntervalDays(Date dateStart, Date dateEnd) {
        if (dateStart == null) {
            return 0;
        }
        if (dateEnd == null) {
            return 0;
        }
        int intervalDays = (int) ((dateEnd.getTime() - dateStart.getTime()) / 1000 / 60 / 60 / 24);
        Log.d(TAG, "getIntervalDays: intervalDays = " + intervalDays);
        return intervalDays;
    }

    public static int getCurrentDayByInterval(int interval, Date startDate) {
        if (startDate == null) {
            return 0;
        }
        Log.d(TAG, "getCurrentDayByInterval: ");
        Date endDate = new Date();
        long endTimeMs = startDate.getTime() + (interval * 24 * 60 * 60 * 1000);
        endDate.setTime(endTimeMs);
        Log.d(TAG, "getCurrentDayByInterval: " + endDate.getMonth() + "-" + endDate.getDay());
        return endDate.getDay() + 1;
    }


    /**
     * 获取两个时间点的天数
     *
     * @param starttime
     * @param endtime
     * @return List<Date>
     */
    public static List<Date> getDays(String starttime, String endtime) {
        Object[] obj = new Object[2]; //返回结果
        List<Date> list = new ArrayList<Date>();//返回对应的所有日期

        //设置时间格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //开始日期
        Date dateFrom = null;
        Date dateTo = null;
        try {
            dateFrom = dateFormat.parse(starttime);
            dateTo = dateFormat.parse(endtime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar cal = null;
        while (dateFrom.before(dateTo) || dateFrom.equals(dateTo)) {

            cal = Calendar.getInstance();
            //设置日期
            cal.setTime(dateFrom);
            list.add(cal.getTime());
            //屏蔽周末判断
            /*
             * if((cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
             * &&(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)){
             * //进行比较，如果日期不等于周六也不等于周日，工作日+1
             *
             * }
             */
            //日期加1
            cal.add(Calendar.DAY_OF_MONTH, 1);
            dateFrom = cal.getTime();
        }

        return list; //list的size()就是这个时间点之间的天数
    }


    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginDate
     * @param endDate
     * @return List
     */
    @SuppressWarnings("unchecked")
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<>();
        if (endDate == null) {
            return lDate;
        }
        lDate.add(beginDate);//把开始时间加入集合
        Calendar cal = Calendar.getInstance();
//使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
//根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
// 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        lDate.add(endDate);//把结束时间加入集合
        return lDate;
    }


    /**
     * @return format 3-26 10:00
     */
    public static String formatSyncTime(long syncTime) {
        return formatTime(syncTime, FORMAT_MM_dd_HH_mm);
    }

}
