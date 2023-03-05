package datetool.date;

import datetool.date.format.DateParser;
import datetool.date.format.FastDateParser;
import datetool.date.format.GlobalCustomFormat;

import java.text.ParsePosition;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 针对{@link Calendar} 对象封装工具类
 *
 * @author looly
 * @since 5.3.0
 */
public class CalendarUtil {

    /**
     * 创建Calendar对象，时间为默认时区的当前时间
     *
     * @return Calendar对象
     * @since 4.6.6
     */
    public static Calendar calendar() {
        return Calendar.getInstance();
    }

    /**
     * 转换为Calendar对象
     *
     * @param date 日期对象
     * @return Calendar对象
     */
    public static Calendar calendar(Date date) {
        if (date instanceof DateTime) {
            return ((DateTime) date).toCalendar();
        } else {
            return calendar(date.getTime());
        }
    }

    /**
     * 转换为Calendar对象，使用当前默认时区
     *
     * @param millis 时间戳
     * @return Calendar对象
     */
    public static Calendar calendar(long millis) {
        return calendar(millis, TimeZone.getDefault());
    }

    /**
     * 转换为Calendar对象
     *
     * @param millis   时间戳
     * @param timeZone 时区
     * @return Calendar对象
     * @since 5.7.22
     */
    public static Calendar calendar(long millis, TimeZone timeZone) {
        final Calendar cal = Calendar.getInstance(timeZone);
        cal.setTimeInMillis(millis);
        return cal;
    }

    /**
     * 是否为上午
     *
     * @param calendar {@link Calendar}
     * @return 是否为上午
     */
    public static boolean isAM(Calendar calendar) {
        return Calendar.AM == calendar.get(Calendar.AM_PM);
    }

    /**
     * 是否为下午
     *
     * @param calendar {@link Calendar}
     * @return 是否为下午
     */
    public static boolean isPM(Calendar calendar) {
        return Calendar.PM == calendar.get(Calendar.AM_PM);
    }

    /**
     * 修改日期为某个时间字段起始时间
     *
     * @param calendar  {@link Calendar}
     * @param dateField 保留到的时间字段，如定义为 {@link DateField#SECOND}，表示这个字段不变，这个字段以下字段全部归0
     * @return 原{@link Calendar}
     */
    public static Calendar truncate(Calendar calendar, DateField dateField) {
        return DateModifier.modify(calendar, dateField.getValue(), DateModifier.ModifyType.TRUNCATE);
    }

    /**
     * 修改日期为某个时间字段四舍五入时间
     *
     * @param calendar  {@link Calendar}
     * @param dateField 时间字段
     * @return 原{@link Calendar}
     */
    public static Calendar round(Calendar calendar, DateField dateField) {
        return DateModifier.modify(calendar, dateField.getValue(), DateModifier.ModifyType.ROUND);
    }

    /**
     * 修改日期为某个时间字段结束时间
     *
     * @param calendar  {@link Calendar}
     * @param dateField 保留到的时间字段，如定义为 {@link DateField#SECOND}，表示这个字段不变，这个字段以下字段全部取最大值
     * @return 原{@link Calendar}
     */
    public static Calendar ceiling(Calendar calendar, DateField dateField) {
        return DateModifier.modify(calendar, dateField.getValue(), DateModifier.ModifyType.CEILING);
    }

    /**
     * 修改日期为某个时间字段结束时间<br>
     * 可选是否归零毫秒。
     *
     * <p>
     * 有时候由于毫秒部分必须为0（如MySQL数据库中），因此在此加上选项。
     * </p>
     *
     * @param calendar            {@link Calendar}
     * @param dateField           时间字段
     * @param truncateMillisecond 是否毫秒归零
     * @return 原{@link Calendar}
     */
    public static Calendar ceiling(Calendar calendar, DateField dateField, boolean truncateMillisecond) {
        return DateModifier.modify(calendar, dateField.getValue(), DateModifier.ModifyType.CEILING, truncateMillisecond);
    }

    /**
     * 修改秒级别的开始时间，即忽略毫秒部分
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     * @since 4.6.2
     */
    public static Calendar beginOfSecond(Calendar calendar) {
        return truncate(calendar, DateField.SECOND);
    }

    /**
     * 修改秒级别的结束时间，即毫秒设置为999
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     * @since 4.6.2
     */
    public static Calendar endOfSecond(Calendar calendar) {
        return ceiling(calendar, DateField.SECOND);
    }

    /**
     * 修改某小时的开始时间
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     */
    public static Calendar beginOfHour(Calendar calendar) {
        return truncate(calendar, DateField.HOUR_OF_DAY);
    }

    /**
     * 修改某小时的结束时间
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     */
    public static Calendar endOfHour(Calendar calendar) {
        return ceiling(calendar, DateField.HOUR_OF_DAY);
    }

    /**
     * 修改某分钟的开始时间
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     */
    public static Calendar beginOfMinute(Calendar calendar) {
        return truncate(calendar, DateField.MINUTE);
    }

    /**
     * 修改某分钟的结束时间
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     */
    public static Calendar endOfMinute(Calendar calendar) {
        return ceiling(calendar, DateField.MINUTE);
    }

    /**
     * 修改某天的开始时间
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     */
    public static Calendar beginOfDay(Calendar calendar) {
        return truncate(calendar, DateField.DAY_OF_MONTH);
    }

    /**
     * 修改某天的结束时间
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     */
    public static Calendar endOfDay(Calendar calendar) {
        return ceiling(calendar, DateField.DAY_OF_MONTH);
    }

    /**
     * 修改给定日期当前周的开始时间，周一定为一周的开始时间
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     */
    public static Calendar beginOfWeek(Calendar calendar) {
        return beginOfWeek(calendar, true);
    }

    /**
     * 修改给定日期当前周的开始时间
     *
     * @param calendar           日期 {@link Calendar}
     * @param isMondayAsFirstDay 是否周一做为一周的第一天（false表示周日做为第一天）
     * @return {@link Calendar}
     * @since 3.1.2
     */
    public static Calendar beginOfWeek(Calendar calendar, boolean isMondayAsFirstDay) {
        calendar.setFirstDayOfWeek(isMondayAsFirstDay ? Calendar.MONDAY : Calendar.SUNDAY);
        // WEEK_OF_MONTH为上限的字段（不包括），实际调整的为DAY_OF_MONTH
        return truncate(calendar, DateField.WEEK_OF_MONTH);
    }

    /**
     * 修改某周的结束时间，周日定为一周的结束
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     */
    public static Calendar endOfWeek(Calendar calendar) {
        return endOfWeek(calendar, true);
    }

    /**
     * 修改某周的结束时间
     *
     * @param calendar          日期 {@link Calendar}
     * @param isSundayAsLastDay 是否周日做为一周的最后一天（false表示周六做为最后一天）
     * @return {@link Calendar}
     */
    public static Calendar endOfWeek(Calendar calendar, boolean isSundayAsLastDay) {
        calendar.setFirstDayOfWeek(isSundayAsLastDay ? Calendar.MONDAY : Calendar.SUNDAY);
        // WEEK_OF_MONTH为上限的字段（不包括），实际调整的为DAY_OF_MONTH
        return ceiling(calendar, DateField.WEEK_OF_MONTH);
    }

    /**
     * 修改某月的开始时间
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     */
    public static Calendar beginOfMonth(Calendar calendar) {
        return truncate(calendar, DateField.MONTH);
    }

    /**
     * 修改某月的结束时间
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     */
    public static Calendar endOfMonth(Calendar calendar) {
        return ceiling(calendar, DateField.MONTH);
    }

    /**
     * 修改某季度的开始时间
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     * @since 4.1.0
     */
    public static Calendar beginOfQuarter(Calendar calendar) {
        //noinspection MagicConstant
        calendar.set(Calendar.MONTH, calendar.get(DateField.MONTH.getValue()) / 3 * 3);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return beginOfDay(calendar);
    }

    /**
     * 获取某季度的结束时间
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     * @since 4.1.0
     */
    @SuppressWarnings({"MagicConstant", "ConstantConditions"})
    public static Calendar endOfQuarter(Calendar calendar) {
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(DateField.MONTH.getValue()) / 3 * 3 + 2;

        final Calendar resultCal = Calendar.getInstance(calendar.getTimeZone());
        resultCal.set(year, month, Month.of(month).getLastDay(DateUtil.isLeapYear(year)));

        return endOfDay(resultCal);
    }

    /**
     * 修改某年的开始时间
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     */
    public static Calendar beginOfYear(Calendar calendar) {
        return truncate(calendar, DateField.YEAR);
    }

    /**
     * 修改某年的结束时间
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     */
    public static Calendar endOfYear(Calendar calendar) {
        return ceiling(calendar, DateField.YEAR);
    }

    /**
     * 比较两个日期是否为同一天
     *
     * @param cal1 日期1
     * @param cal2 日期2
     * @return 是否为同一天
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && //
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && //
                cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA);
    }

    /**
     * 比较两个日期是否为同一周
     *
     * @param cal1  日期1
     * @param cal2  日期2
     * @param isMon 是否为周一。国内第一天为星期一，国外第一天为星期日
     * @return 是否为同一周
     * @since 5.7.21
     */
    public static boolean isSameWeek(Calendar cal1, Calendar cal2, boolean isMon) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }

        // 防止比较前修改原始Calendar对象
        cal1 = (Calendar) cal1.clone();
        cal2 = (Calendar) cal2.clone();

        // 把所传日期设置为其当前周的第一天
        // 比较设置后的两个日期是否是同一天：true 代表同一周
        if (isMon) {
            cal1.setFirstDayOfWeek(Calendar.MONDAY);
            cal1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            cal2.setFirstDayOfWeek(Calendar.MONDAY);
            cal2.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        } else {
            cal1.setFirstDayOfWeek(Calendar.SUNDAY);
            cal1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            cal2.setFirstDayOfWeek(Calendar.SUNDAY);
            cal2.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        }
        return isSameDay(cal1, cal2);
    }

    /**
     * 比较两个日期是否为同一月
     *
     * @param cal1 日期1
     * @param cal2 日期2
     * @return 是否为同一月
     * @since 5.4.1
     */
    public static boolean isSameMonth(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && //
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }

    /**
     * <p>检查两个Calendar时间戳是否相同。</p>
     *
     * <p>此方法检查两个Calendar的毫秒数时间戳是否相同。</p>
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return 两个Calendar时间戳是否相同。如果两个时间都为{@code null}返回true，否则有{@code null}返回false
     * @since 5.3.11
     */
    public static boolean isSameInstant(Calendar date1, Calendar date2) {
        if (null == date1) {
            return null == date2;
        }
        if (null == date2) {
            return false;
        }

        return date1.getTimeInMillis() == date2.getTimeInMillis();
    }

    /**
     * 获得指定日期区间内的年份和季度<br>
     *
     * @param startDate 起始日期（包含）
     * @param endDate   结束日期（包含）
     * @return 季度列表 ，元素类似于 20132
     * @since 4.1.15
     */
    public static LinkedHashSet<String> yearAndQuarter(long startDate, long endDate) {
        LinkedHashSet<String> quarters = new LinkedHashSet<>();
        final Calendar cal = calendar(startDate);
        while (startDate <= endDate) {
            // 如果开始时间超出结束时间，让结束时间为开始时间，处理完后结束循环
            quarters.add(yearAndQuarter(cal));

            cal.add(Calendar.MONTH, 3);
            startDate = cal.getTimeInMillis();
        }

        return quarters;
    }

    /**
     * 获得指定日期年份和季度<br>
     * 格式：[20131]表示2013年第一季度
     *
     * @param cal 日期
     * @return 年和季度，格式类似于20131
     */
    public static String yearAndQuarter(Calendar cal) {
        return String.valueOf(cal.get(Calendar.YEAR)) + (cal.get(Calendar.MONTH) / 3 + 1);
    }

    /**
     * 获取指定日期字段的最小值，例如分钟的最小值是0
     *
     * @param calendar  {@link Calendar}
     * @param dateField {@link DateField}
     * @return 字段最小值
     * @see Calendar#getActualMinimum(int)
     * @since 5.4.2
     */
    public static int getBeginValue(Calendar calendar, DateField dateField) {
        return getBeginValue(calendar, dateField.getValue());
    }

    /**
     * 获取指定日期字段的最小值，例如分钟的最小值是0
     *
     * @param calendar  {@link Calendar}
     * @param dateField {@link DateField}
     * @return 字段最小值
     * @see Calendar#getActualMinimum(int)
     * @since 4.5.7
     */
    public static int getBeginValue(Calendar calendar, int dateField) {
        if (Calendar.DAY_OF_WEEK == dateField) {
            return calendar.getFirstDayOfWeek();
        }
        return calendar.getActualMinimum(dateField);
    }

    /**
     * 获取指定日期字段的最大值，例如分钟的最大值是59
     *
     * @param calendar  {@link Calendar}
     * @param dateField {@link DateField}
     * @return 字段最大值
     * @see Calendar#getActualMaximum(int)
     * @since 5.4.2
     */
    public static int getEndValue(Calendar calendar, DateField dateField) {
        return getEndValue(calendar, dateField.getValue());
    }

    /**
     * 获取指定日期字段的最大值，例如分钟的最大值是59
     *
     * @param calendar  {@link Calendar}
     * @param dateField {@link DateField}
     * @return 字段最大值
     * @see Calendar#getActualMaximum(int)
     * @since 4.5.7
     */
    public static int getEndValue(Calendar calendar, int dateField) {
        if (Calendar.DAY_OF_WEEK == dateField) {
            return (calendar.getFirstDayOfWeek() + 6) % 7;
        }
        return calendar.getActualMaximum(dateField);
    }

    /**
     * Calendar{@link Instant}对象
     *
     * @param calendar Date对象
     * @return {@link Instant}对象
     * @since 5.0.5
     */
    public static Instant toInstant(Calendar calendar) {
        return null == calendar ? null : calendar.toInstant();
    }

    /**
     * {@link Calendar} 转换为 {@link LocalDateTime}，使用系统默认时区
     *
     * @param calendar {@link Calendar}
     * @return {@link LocalDateTime}
     * @since 5.0.5
     */
    public static LocalDateTime toLocalDateTime(Calendar calendar) {
        return LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());
    }

    /**
     * {@code null}安全的{@link Calendar}比较，{@code null}小于任何日期
     *
     * @param calendar1 日期1
     * @param calendar2 日期2
     * @return 比较结果，如果calendar1 &lt; calendar2，返回数小于0，calendar1==calendar2返回0，calendar1 &gt; calendar2 大于0
     * @since 4.6.2
     */
    public static int compare(Calendar calendar1, Calendar calendar2) {
        if (calendar1 == calendar2) {
            return 0;
        } else if (calendar1 == null) {
            return -1;
        } else if (calendar2 == null) {
            return 1;
        }
        return calendar1.compareTo(calendar2);
    }

    /**
     * 计算相对于dateToCompare的年龄，长用于计算指定生日在某年的年龄
     *
     * @param birthday      生日
     * @param dateToCompare 需要对比的日期
     * @return 年龄
     */
    public static int age(Calendar birthday, Calendar dateToCompare) {
        return age(birthday.getTimeInMillis(), dateToCompare.getTimeInMillis());
    }

    /**
     * 将指定Calendar时间格式化为纯中文形式，比如：
     *
     * <pre>
     *     2018-02-24 12:13:14 转换为 二〇一八年二月二十四日（withTime为false）
     *     2018-02-24 12:13:14 转换为 二〇一八年二月二十四日十二时十三分十四秒（withTime为true）
     * </pre>
     *
     * @param calendar {@link Calendar}
     * @param withTime 是否包含时间部分
     * @return 格式化后的字符串
     * @since 5.3.9
     */
    public static String formatChineseDate(Calendar calendar, boolean withTime) {
        final StringBuilder result = new StringBuilder();

        // 年
        final String year = String.valueOf(calendar.get(Calendar.YEAR));
        final int length = year.length();
        for (int i = 0; i < length; i++) {
            String str;
            char c = year.charAt(i);
            if (c < '0' || c > '9') {
                str = String.valueOf(c);
            } else {
                char result1;
                if (0 == c - '0') {
                    result1 = '零';
                } else {
                    result1 = new char[]{'零', '一', '壹', '二', '贰', '三', '叁', '四', '肆', '五', '伍',
                            '六', '陆', '七', '柒', '八', '捌', '九', '玖'}[(c - '0') * 2 - 1];
                }
                str = String.valueOf(result1);
            }
            result.append(str);
        }
        result.append('年');

        // 月
        int month = calendar.get(Calendar.MONTH) + 1;
        result.append(formatThousand(month));
        result.append('月');

        // 日
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        result.append(formatThousand(day));
        result.append('日');

        // 只替换年月日，时分秒中零不需要替换
        String temp = result.toString().replace('零', '〇');
        result.delete(0, result.length());
        result.append(temp);


        if (withTime) {
            // 时
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            result.append(formatThousand(hour));
            result.append('时');
            // 分
            int minute = calendar.get(Calendar.MINUTE);
            result.append(formatThousand(minute));
            result.append('分');
            // 秒
            int second = calendar.get(Calendar.SECOND);
            result.append(formatThousand(second));
            result.append('秒');
        }

        return result.toString();
    }

    /**
     * 计算相对于dateToCompare的年龄，长用于计算指定生日在某年的年龄
     *
     * @param birthday      生日
     * @param dateToCompare 需要对比的日期
     * @return 年龄
     */
    protected static int age(long birthday, long dateToCompare) {
        if (birthday > dateToCompare) {
            throw new IllegalArgumentException("Birthday is after dateToCompare!");
        }

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateToCompare);

        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        final boolean isLastDayOfMonth = dayOfMonth == cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        cal.setTimeInMillis(birthday);
        int age = year - cal.get(Calendar.YEAR);

        final int monthBirth = cal.get(Calendar.MONTH);
        if (month == monthBirth) {

            final int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
            final boolean isLastDayOfMonthBirth = dayOfMonthBirth == cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            if ((!isLastDayOfMonth || !isLastDayOfMonthBirth) && dayOfMonth < dayOfMonthBirth) {
                // 如果生日在当月，但是未达到生日当天的日期，年龄减一
                age--;
            }
        } else if (month < monthBirth) {
            // 如果当前月份未达到生日的月份，年龄计算减一
            age--;
        }

        return age;
    }

    /**
     * 通过给定的日期格式解析日期时间字符串。<br>
     * 传入的日期格式会逐个尝试，直到解析成功，返回{@link Calendar}对象，否则抛出{@link DateException}异常。
     * 方法来自：Apache Commons-Lang3
     *
     * @param str           日期时间字符串，非空
     * @param parsePatterns 需要尝试的日期时间格式数组，非空, 见SimpleDateFormat
     * @return 解析后的Calendar
     * @throws IllegalArgumentException if the date string or pattern array is null
     * @throws DateException            if none of the date patterns were suitable
     * @since 5.3.11
     */
    public static Calendar parseByPatterns(String str, String... parsePatterns) throws DateException {
        return parseByPatterns(str, null, parsePatterns);
    }

    /**
     * 通过给定的日期格式解析日期时间字符串。<br>
     * 传入的日期格式会逐个尝试，直到解析成功，返回{@link Calendar}对象，否则抛出{@link DateException}异常。
     * 方法来自：Apache Commons-Lang3
     *
     * @param str           日期时间字符串，非空
     * @param locale        地区，当为{@code null}时使用{@link Locale#getDefault()}
     * @param parsePatterns 需要尝试的日期时间格式数组，非空, 见SimpleDateFormat
     * @return 解析后的Calendar
     * @throws IllegalArgumentException if the date string or pattern array is null
     * @throws DateException            if none of the date patterns were suitable
     * @since 5.3.11
     */
    public static Calendar parseByPatterns(String str, Locale locale, String... parsePatterns) throws DateException {
        return parseByPatterns(str, locale, true, parsePatterns);
    }

    /**
     * 通过给定的日期格式解析日期时间字符串。<br>
     * 传入的日期格式会逐个尝试，直到解析成功，返回{@link Calendar}对象，否则抛出{@link DateException}异常。
     * 方法来自：Apache Commons-Lang3
     *
     * @param str           日期时间字符串，非空
     * @param locale        地区，当为{@code null}时使用{@link Locale#getDefault()}
     * @param lenient       日期时间解析是否使用严格模式
     * @param parsePatterns 需要尝试的日期时间格式数组，非空, 见SimpleDateFormat
     * @return 解析后的Calendar
     * @throws IllegalArgumentException if the date string or pattern array is null
     * @throws DateException            if none of the date patterns were suitable
     * @see Calendar#isLenient()
     * @since 5.3.11
     */
    public static Calendar parseByPatterns(String str, Locale locale, boolean lenient, String... parsePatterns) throws DateException {
        if (str == null || parsePatterns == null) {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }

        final TimeZone tz = TimeZone.getDefault();
        final Locale defaultValue = Locale.getDefault();
        //noinspection ConstantConditions
        final Locale lcl = null == locale || locale.equals(null) ? defaultValue : locale;
        final ParsePosition pos = new ParsePosition(0);
        final Calendar calendar = Calendar.getInstance(tz, lcl);
        calendar.setLenient(lenient);

        for (final String parsePattern : parsePatterns) {
            if (GlobalCustomFormat.isCustomFormat(parsePattern)) {
                final Date parse = GlobalCustomFormat.parse(str, parsePattern);
                if (null == parse) {
                    continue;
                }
                calendar.setTime(parse);
                return calendar;
            }

            final FastDateParser fdp = new FastDateParser(parsePattern, tz, lcl);
            calendar.clear();
            try {
                if (fdp.parse(str, pos, calendar) && pos.getIndex() == str.length()) {
                    return calendar;
                }
            } catch (final IllegalArgumentException ignore) {
                // leniency is preventing calendar from being set
            }
            pos.setIndex(0);
        }

        throw new DateException("Unable to parse the date: {}", str);
    }

    /**
     * 使用指定{@link DateParser}解析字符串为{@link Calendar}
     *
     * @param str     日期字符串
     * @param lenient 是否宽容模式
     * @param parser  {@link DateParser}
     * @return 解析后的 {@link Calendar}，解析失败返回{@code null}
     * @since 5.7.14
     */
    public static Calendar parse(CharSequence str, boolean lenient, DateParser parser) {
        final Calendar calendar = Calendar.getInstance(parser.getTimeZone(), parser.getLocale());
        calendar.clear();
        calendar.setLenient(lenient);

        return parser.parse(null == str ? null : str.toString(), new ParsePosition(0), calendar) ? calendar : null;
    }

    private static String formatThousand(int amount) {
        if (amount < -999 || amount > 999) {
            throw new IllegalArgumentException(DateUtil.format("Number support only: (-999 ~ 999)！"));
        }

        String chinese;
        if (amount == 0) {
            // issue#I4R92H@Gitee
            chinese = String.valueOf('零');
        } else {
            int temp = amount;
            StringBuilder chineseStr = new StringBuilder();
            boolean lastIsZero = true; // 在从低位往高位循环时，记录上一位数字是不是 0
            for (int i = 0; temp > 0; i++) {
                int digit = temp % 10;
                if (digit == 0) { // 取到的数字为 0
                    if (!lastIsZero) {
                        // 前一个数字不是 0，则在当前汉字串前加“零”字;
                        chineseStr.insert(0, "零");
                    }
                    lastIsZero = true;
                } else { // 取到的数字不是 0
                    char result;
                    result = new char[]{'零', '一', '壹', '二', '贰', '三', '叁', '四', '肆', '五', '伍',
                            '六', '陆', '七', '柒', '八', '捌', '九', '玖'}[digit * 2 - 1];
                    String result1 = "";
                    if (0 != i) {
                        result1 = String.valueOf(new char[]{' ', '十', '拾', '百', '佰', '千', '仟', '万', '亿',}[i * 2 - (false ? 0 : 1)]);
                    }
                    chineseStr.insert(0, result + result1);
                    lastIsZero = false;
                }
                temp = temp / 10;
            }
            chinese = chineseStr.toString();
        }

        if (amount < 20 && amount >= 10) {
            // "十一"而非"一十一"
            return chinese.substring(1);
        }
        return chinese;
    }
}
