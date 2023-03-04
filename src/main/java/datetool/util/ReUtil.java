package datetool.util;

import datetool.lang.PatternPool;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则相关工具类<br>
 * 常用正则请见 Validator
 *
 * @author xiaoleilu
 */
public class ReUtil {

	/**
	 * 在给定字符串中查找给定规则的字符，如果找到则使用{@link Consumer}处理之<br>
	 * 如果内容中有多个匹配项，则只处理找到的第一个结果。
	 *
	 * @param pattern  匹配的正则
	 * @param content  被匹配的内容
	 * @param consumer 匹配到的内容处理器
	 * @since 5.7.15
	 */
	public static void get(Pattern pattern, CharSequence content, Consumer<Matcher> consumer) {
		if (null == content || null == pattern || null == consumer) {
			return;
		}
		final Matcher m = pattern.matcher(content);
		if (m.find()) {
			consumer.accept(m);
		}
	}

	/**
	 * 指定内容中是否有表达式匹配的内容
	 *
	 * @param regex   正则表达式
	 * @param content 被查找的内容
	 * @return 指定内容中是否有表达式匹配的内容
	 * @since 3.3.1
	 */
	public static boolean contains(String regex, CharSequence content) {
		if (null == regex || null == content) {
			return false;
		}

		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return contains(pattern, content);
	}

	/**
	 * 指定内容中是否有表达式匹配的内容
	 *
	 * @param pattern 编译后的正则模式
	 * @param content 被查找的内容
	 * @return 指定内容中是否有表达式匹配的内容
	 * @since 3.3.1
	 */
	public static boolean contains(Pattern pattern, CharSequence content) {
		if (null == pattern || null == content) {
			return false;
		}
		return pattern.matcher(content).find();
	}

	/**
	 * 给定内容是否匹配正则
	 *
	 * @param regex   正则
	 * @param content 内容
	 * @return 正则为null或者""则不检查，返回true，内容为null返回false
	 */
	public static boolean isMatch(String regex, CharSequence content) {
		if (content == null) {
			// 提供null的字符串为不匹配
			return false;
		}

		if (StrUtil.isEmpty(regex)) {
			// 正则不存在则为全匹配
			return true;
		}

		// Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		final Pattern pattern = PatternPool.get(regex, Pattern.DOTALL);
		return isMatch(pattern, content);
	}

	/**
	 * 给定内容是否匹配正则
	 *
	 * @param pattern 模式
	 * @param content 内容
	 * @return 正则为null或者""则不检查，返回true，内容为null返回false
	 */
	public static boolean isMatch(Pattern pattern, CharSequence content) {
		if (content == null || pattern == null) {
			// 提供null的字符串为不匹配
			return false;
		}
		return pattern.matcher(content).matches();
	}

}
