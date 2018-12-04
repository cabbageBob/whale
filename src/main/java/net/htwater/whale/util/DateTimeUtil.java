package net.htwater.whale.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *
 * @author shitianlong
 */
public class DateTimeUtil {
    private static final String COMMON_PATTERN="yyyy-MM-dd HH:mm:ss";
    private static final String  CONDENSE_PATTERN="yyyyMMddHHmm";
    private static final SimpleDateFormat COMMON_FORMAT = new SimpleDateFormat(COMMON_PATTERN);
    private static final SimpleDateFormat CONDENSE_FORMAT = new SimpleDateFormat(CONDENSE_PATTERN);
    private static final int HOUR_8 = 8;
    public static final String HOUR_OF_HYDROLOGICAL_DAY="08:00:00";
    public static final int HYDROLOGICAL_HOUR_1=1;
    public static final int HYDROLOGICAL_HOUR_6=2;
    public static final int HYDROLOGICAL_HOUR_12=3;
    public static final int HYDROLOGICAL_HOUR_24=4;
    public static final int HYDROLOGICAL_DAY_1=5;
    public static final int HYDROLOGICAL_DAY_3=6;
    public static final int HYDROLOGICAL_DAY_7=7;
    public static final int HYDROLOGICAL_DAY_10=8;
    public static final int HYDROLOGICAL_DAY_30=9;
    public static final int CUSTOM_TIME=-1;
    /**
     * get hydrological day
     * @param day days forward :0 is today
     * @return Date
     */
    public static Date getEvenDayInDate(int day){
        Calendar calendar=Calendar.getInstance();
        if(calendar.get(Calendar.HOUR_OF_DAY)<8){
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH,-1*day);
        return calendar.getTime();
    }
    /**
     * get hydrological day
     * @param day days forward :0 is today
     * @return String
     */
    public static String getEvenDay(int day){
        return COMMON_FORMAT.format(getEvenDayInDate(day));
    }
    /**
     * get hydrological hour
     * @param hour hour forward
     * @return date
     */
    public static Date getEvenHourInDate(int hour){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.HOUR_OF_DAY,-1*hour);
        return calendar.getTime();
    }
    /**
     * get hydrological hour
     * @param hour days hour
     * @return String
     */
    public static String getEvenHour(int hour){
        return COMMON_FORMAT.format(getEvenHourInDate(hour));
    }

    public static String[] getStartAndEndTime(int type) {
        String[] result={getEvenDay(0),getEvenDay(0)};
        switch (type){
            case HYDROLOGICAL_DAY_1:
                result[0]=getEvenDay(1);
                break;
            case HYDROLOGICAL_DAY_3:
                result[0]=getEvenDay(3);
                break;
            case HYDROLOGICAL_DAY_7:
                result[0]=getEvenDay(7);
                break;
            case HYDROLOGICAL_DAY_30:
                result[0]=getEvenDay(30);
                break;
            default:
                result[0]=getEvenDay(3);
                break;
        }
        return result;
    }
    public static LocalDateTime getToday8H(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        int hour = localDateTime.getHour();
        if(hour < HOUR_8){
            localDateTime = localDateTime.minusDays(1);
        }
        return localDateTime.withHour(8).withMinute(0).withSecond(0).withNano(0);
    }

    public static LocalDateTime getToday8H() {
        return getToday8H(new Date());
    }

    public static String[] getStartAndEndTime(String date) {
        String[] result={getEvenDay(0),getEvenDay(0)};
        Date end=DateUtil.offset(DateUtil.parseDate(date+" 08:00:00")
                , DateField.DAY_OF_MONTH, 1);
        result[1]=COMMON_FORMAT.format(end);
        return result;
    }

    /**
     * get different format picture time by duration
     * @param duration see constant in this class
     * @return string
     */
    public static String getContourPictureNameByDuration(int duration){
        Map<Integer,String> map=new HashMap<>(10);
        map.put(HYDROLOGICAL_HOUR_1,"PRE_CON_HOU_1_");
        map.put(HYDROLOGICAL_HOUR_6,"PRE_CON_HOU_6_");
        map.put(HYDROLOGICAL_HOUR_12,"PRE_CON_HOU_12_");
        map.put(HYDROLOGICAL_HOUR_24,"PRE_CON_HOU_24_");
        map.put(HYDROLOGICAL_DAY_1,"PRE_CON_DAY_1_");
        map.put(HYDROLOGICAL_DAY_3,"PRE_CON_DAY_3_");
        map.put(HYDROLOGICAL_DAY_7,"PRE_CON_DAY_7_");
        map.put(HYDROLOGICAL_DAY_10,"PRE_CON_DAY_10_");
        map.put(HYDROLOGICAL_DAY_30,"PRE_CON_DAY_30_");
        map.put(CUSTOM_TIME,"CUSTOM");
        DateTimeFormatter format = DateTimeFormatter.ofPattern(COMMON_PATTERN);
        return duration==-1?map.get(duration)+UUID.randomUUID().toString()
                :map.get(duration)+CONDENSE_FORMAT.format(getEvenDayInDate(0));
    }

    public static String parseDateToString(Date date){
        return COMMON_FORMAT.format(date);
    }
}
