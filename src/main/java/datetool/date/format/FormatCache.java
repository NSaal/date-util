package datetool.date.format;

import datetool.lang.Assert;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 日期格式化器缓存<br>
 * Thanks to Apache Commons Lang 3.5
 *
 * @since 2.16.2
 */
abstract class FormatCache<F extends Format> {

	private final ConcurrentMap<Object, F> cInstanceCache = new ConcurrentHashMap<>(7);

	/**
	 * 使用 pattern, time zone and locale 获得对应的 格式化器
	 *
	 * @param pattern  非空日期格式，使用与 {@link SimpleDateFormat}相同格式
	 * @param timeZone 时区，默认当前时区
	 * @param locale   地区，默认使用当前地区
	 * @return 格式化器
	 * @throws IllegalArgumentException pattern 无效或{@code null}
	 */
	public F getInstance(final String pattern, TimeZone timeZone, Locale locale) {
		Assert.notBlank(pattern, "pattern must not be blank");
		if (timeZone == null) {
			timeZone = TimeZone.getDefault();
		}
		if (locale == null) {
			locale = Locale.getDefault();
		}
		final Object[] key = new Object[]{pattern, timeZone, locale};
		F format = cInstanceCache.get(key);
		if (format == null) {
			format = createInstance(pattern, timeZone, locale);
			final F previousValue = cInstanceCache.putIfAbsent(key, format);
			if (previousValue != null) {
				// another thread snuck in and did the same work
				// we should return the instance that is in ConcurrentMap
				format = previousValue;
			}
		}
		return format;
	}

	/**
	 * 创建格式化器
	 *
	 * @param pattern  非空日期格式，使用与 {@link SimpleDateFormat}相同格式
	 * @param timeZone 时区，默认当前时区
	 * @param locale   地区，默认使用当前地区
	 * @return 格式化器
	 * @throws IllegalArgumentException pattern 无效或{@code null}
	 */
	abstract protected F createInstance(String pattern, TimeZone timeZone, Locale locale);

}
