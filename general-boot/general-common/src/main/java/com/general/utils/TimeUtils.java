package com.general.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description: 时间工具类
 * @Author LuoJing
 * @Date 2019/10/2114:20
 */
public class TimeUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";


    /**
     * @Description: 获取过去7天的日期
     * @Author LuoJing
     * @Date 2019/12/17 17:31
     */
    public static List<String> listHistory7DaysDate(Date date) {
        List<String> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = Calendar.getInstance();
        for (int i = 6; i >= 0; i--) {
            calendar.setTime(date);
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - i);
            Date today = calendar.getTime();
            String time = sdf.format(today);
            System.out.println(time);
            list.add(time);
        }
        return list;
    }

}
