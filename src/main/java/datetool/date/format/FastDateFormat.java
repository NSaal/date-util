package datetool.date.format;

import datetool.date.DateUtil;

import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>
 * FastDateFormat 是一个线程安全的 {@link java.text.SimpleDateFormat} 实现。
 * </p>
 *
 * <p>
 * 通过以下静态方法获得此对象: <br>
 * {@link #getInstance(String, TimeZone, Locale)}<br>
 * </p>
 *
 * Thanks to Apache Commons Lang 3.5
 * @since 2.16.2
 */
public class FastDateFormat extends Format implements DateParser, DatePrinter {
	private static final long serialVersionUID = 8097890768636183236L;

	private static final ConcurrentMap<Object, FastDateFormat> cInstanceCache = new ConcurrentHashMap<>(7);


	private final FastDatePrinter printer;
	private final FastDateParser parser;

	/**
	 * 使用 pattern, time zone and locale 获得对应的 格式化器
	 *
	 * @param pattern  非空日期格式，使用与 {@link SimpleDateFormat}相同格式
	 * @param timeZone 时区，默认当前时区
	 * @param locale   地区，默认使用当前地区
	 * @return 格式化器
	 * @throws IllegalArgumentException pattern 无效或{@code null}
	 */
	public static FastDateFormat getCacheInstance(final String pattern, TimeZone timeZone, Locale locale) {
		boolean blank = true;
		// 判断的时候，并将cs的长度赋给了strLen
		if (pattern != null && pattern.length() != 0) {// 遍历字符
			for (int i = 0; i < pattern.length(); i++) {
				if (!Character.isWhitespace(pattern.charAt(i))) {
					blank = false;
					break;
				}
			}
		}
		if (blank) {
			throw new IllegalArgumentException(DateUtil.format("pattern must not be blank"));
		}
		if (timeZone == null) {
			timeZone = TimeZone.getDefault();
		}
		if (locale == null) {
			locale = Locale.getDefault();
		}
		final Object[] key = new Object[]{pattern, timeZone, locale};
		FastDateFormat format = cInstanceCache.get(key);
		if (format == null) {
			format = new FastDateFormat(pattern, timeZone, locale);
			final FastDateFormat previousValue = cInstanceCache.putIfAbsent(key, format);
			if (previousValue != null) {
				// another thread snuck in and did the same work
				// we should return the instance that is in ConcurrentMap
				format = previousValue;
			}
		}
		return format;
	}

	// -----------------------------------------------------------------------

	/**
	 * 获得 FastDateFormat 实例，使用默认地区<br>
	 * 支持缓存
	 *
	 * @param pattern 使用{@link java.text.SimpleDateFormat} 相同的日期格式
	 * @return FastDateFormat
	 * @throws IllegalArgumentException 日期格式问题
	 */
	public static FastDateFormat getInstance(final String pattern) {
		return getCacheInstance(pattern, null, null);
	}

	/**
	 * 获得 FastDateFormat 实例<br>
	 * 支持缓存
	 *
	 * @param pattern 使用{@link java.text.SimpleDateFormat} 相同的日期格式
	 * @param timeZone 时区{@link TimeZone}
	 * @return FastDateFormat
	 * @throws IllegalArgumentException 日期格式问题
	 */
	public static FastDateFormat getInstance(final String pattern, final TimeZone timeZone) {
		return getCacheInstance(pattern, timeZone, null);
	}

	/**
	 * 获得 FastDateFormat 实例<br>
	 * 支持缓存
	 *
	 * @param pattern 使用{@link java.text.SimpleDateFormat} 相同的日期格式
	 * @param locale {@link Locale} 日期地理位置
	 * @return FastDateFormat
	 * @throws IllegalArgumentException 日期格式问题
	 */
	public static FastDateFormat getInstance(final String pattern, final Locale locale) {
		return getCacheInstance(pattern, null, locale);
	}

	/**
	 * 获得 FastDateFormat 实例<br>
	 * 支持缓存
	 *
	 * @param pattern 使用{@link java.text.SimpleDateFormat} 相同的日期格式
	 * @param timeZone 时区{@link TimeZone}
	 * @param locale {@link Locale} 日期地理位置
	 * @return FastDateFormat
	 * @throws IllegalArgumentException 日期格式问题
	 */
	public static FastDateFormat getInstance(final String pattern, final TimeZone timeZone, final Locale locale) {
		return getCacheInstance(pattern, timeZone, locale);
	}

	// ----------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 *
	 * @param pattern 使用{@link java.text.SimpleDateFormat} 相同的日期格式
	 * @param timeZone 非空时区 {@link TimeZone}
	 * @param locale {@link Locale} 日期地理位置
	 * @throws NullPointerException if pattern, timeZone, or locale is null.
	 */
	protected FastDateFormat(final String pattern, final TimeZone timeZone, final Locale locale) {
		this(pattern, timeZone, locale, null);
	}

	/**
	 * 构造
	 *
	 * @param pattern 使用{@link java.text.SimpleDateFormat} 相同的日期格式
	 * @param timeZone 非空时区 {@link TimeZone}
	 * @param locale {@link Locale} 日期地理位置
	 * @param centuryStart The start of the 100 year period to use as the "default century" for 2 digit year parsing. If centuryStart is null, defaults to now - 80 years
	 * @throws NullPointerException if pattern, timeZone, or locale is null.
	 */
	protected FastDateFormat(final String pattern, final TimeZone timeZone, final Locale locale, final Date centuryStart) {
		printer = new FastDatePrinter(pattern, timeZone, locale);
		parser = new FastDateParser(pattern, timeZone, locale, centuryStart);
	}
	// ----------------------------------------------------------------------- Constructor end

	// ----------------------------------------------------------------------- Format methods
	@Override
	public StringBuffer format(final Object obj, final StringBuffer toAppendTo, final FieldPosition pos) {
		return toAppendTo.append(printer.format(obj));
	}

	@Override
	public String format(final long millis) {
		return printer.format(millis);
	}

	@Override
	public String format(final Date date) {
		return printer.format(date);
	}

	@Override
	public String format(final Calendar calendar) {
		return printer.format(calendar);
	}

	@Override
	public <B extends Appendable> B format(final long millis, final B buf) {
		return printer.format(millis, buf);
	}

	@Override
	public <B extends Appendable> B format(final Date date, final B buf) {
		return printer.format(date, buf);
	}

	@Override
	public <B extends Appendable> B format(final Calendar calendar, final B buf) {
		return printer.format(calendar, buf);
	}

	// ----------------------------------------------------------------------- Parsing
	@Override
	public Date parse(final String source) throws ParseException {
		return parser.parse(source);
	}

	@Override
	public Date parse(final String source, final ParsePosition pos) {
		return parser.parse(source, pos);
	}

	@Override
	public boolean parse(final String source, final ParsePosition pos, final Calendar calendar) {
		return parser.parse(source, pos, calendar);
	}

	@Override
	public Object parseObject(final String source, final ParsePosition pos) {
		return parser.parseObject(source, pos);
	}

	// ----------------------------------------------------------------------- Accessors
	@Override
	public String getPattern() {
		return printer.getPattern();
	}

	@Override
	public TimeZone getTimeZone() {
		return printer.getTimeZone();
	}

	@Override
	public Locale getLocale() {
		return printer.getLocale();
	}

	// Basics
	// -----------------------------------------------------------------------
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof FastDateFormat)) {
			return false;
		}
		final FastDateFormat other = (FastDateFormat) obj;
		// no need to check parser, as it has same invariants as printer
		return printer.equals(other.printer);
	}

	@Override
	public int hashCode() {
		return printer.hashCode();
	}

	@Override
	public String toString() {
		return "FastDateFormat[" + printer.getPattern() + "," + printer.getLocale() + "," + printer.getTimeZone().getID() + "]";
	}
}
