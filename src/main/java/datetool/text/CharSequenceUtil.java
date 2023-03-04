package datetool.text;

import datetool.collection.CollUtil;
import datetool.text.finder.CharFinder;
import datetool.text.finder.Finder;
import datetool.text.finder.StrFinder;
import datetool.util.ArrayUtil;
import datetool.util.CharUtil;
import datetool.util.StrUtil;

import java.util.List;

/**
 * {@link CharSequence} 相关工具类封装
 *
 * @author looly
 * @since 5.5.3
 */
public class CharSequenceUtil {

	public static final int INDEX_NOT_FOUND = Finder.INDEX_NOT_FOUND;

	/**
	 * 字符串常量：{@code "null"} <br>
	 * 注意：{@code "null" != null}
	 */
	public static final String NULL = "null";

	/**
	 * 字符串常量：空字符串 {@code ""}
	 */
	public static final String EMPTY = "";

	/**
	 * <p>字符串是否为空白，空白的定义如下：</p>
	 * <ol>
	 *     <li>{@code null}</li>
	 *     <li>空字符串：{@code ""}</li>
	 *     <li>空格、全角空格、制表符、换行符，等不可见字符</li>
	 * </ol>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isBlank(null)     // true}</li>
	 *     <li>{@code StrUtil.isBlank("")       // true}</li>
	 *     <li>{@code StrUtil.isBlank(" \t\n")  // true}</li>
	 *     <li>{@code StrUtil.isBlank("abc")    // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isEmpty(CharSequence)} 的区别是：
	 * 该方法会校验空白字符，且性能相对于 {@link #isEmpty(CharSequence)} 略慢。</p>
	 * <br>
	 *
	 * <p>建议：</p>
	 * <ul>
	 *     <li>该方法建议仅对于客户端（或第三方接口）传入的参数使用该方法。</li>
	 *     <li>需要同时校验多个字符串时，建议采用 {@link #hasBlank(CharSequence...)} 或 {@link #isAllBlank(CharSequence...)}</li>
	 * </ul>
	 *
	 * @param str 被检测的字符串
	 * @return 若为空白，则返回 true
	 * @see #isEmpty(CharSequence)
	 */
	public static boolean isBlank(CharSequence str) {
		final int length;
		if ((str == null) || ((length = str.length()) == 0)) {
			return true;
		}

		for (int i = 0; i < length; i++) {
			// 只要有一个非空字符即为非空字符串
			if (false == CharUtil.isBlankChar(str.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * <p>字符串是否为非空白，非空白的定义如下： </p>
	 * <ol>
	 *     <li>不为 {@code null}</li>
	 *     <li>不为空字符串：{@code ""}</li>
	 *     <li>不为空格、全角空格、制表符、换行符，等不可见字符</li>
	 * </ol>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isNotBlank(null)     // false}</li>
	 *     <li>{@code StrUtil.isNotBlank("")       // false}</li>
	 *     <li>{@code StrUtil.isNotBlank(" \t\n")  // false}</li>
	 *     <li>{@code StrUtil.isNotBlank("abc")    // true}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isNotEmpty(CharSequence)} 的区别是：
	 * 该方法会校验空白字符，且性能相对于 {@link #isNotEmpty(CharSequence)} 略慢。</p>
	 * <p>建议：仅对于客户端（或第三方接口）传入的参数使用该方法。</p>
	 *
	 * @param str 被检测的字符串
	 * @return 是否为非空
	 * @see #isBlank(CharSequence)
	 */
	public static boolean isNotBlank(CharSequence str) {
		return false == isBlank(str);
	}

	/**
	 * <p>字符串是否为空，空的定义如下：</p>
	 * <ol>
	 *     <li>{@code null}</li>
	 *     <li>空字符串：{@code ""}</li>
	 * </ol>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isEmpty(null)     // true}</li>
	 *     <li>{@code StrUtil.isEmpty("")       // true}</li>
	 *     <li>{@code StrUtil.isEmpty(" \t\n")  // false}</li>
	 *     <li>{@code StrUtil.isEmpty("abc")    // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isBlank(CharSequence)} 的区别是：该方法不校验空白字符。</p>
	 * <p>建议：</p>
	 * <ul>
	 *     <li>该方法建议用于工具类或任何可以预期的方法参数的校验中。</li>
	 *     <li>需要同时校验多个字符串时，建议采用 {@link #hasEmpty(CharSequence...)} 或 {@link #isAllEmpty(CharSequence...)}</li>
	 * </ul>
	 *
	 * @param str 被检测的字符串
	 * @return 是否为空
	 * @see #isBlank(CharSequence)
	 */
	public static boolean isEmpty(CharSequence str) {
		return str == null || str.length() == 0;
	}

	/**
	 * <p>字符串是否为非空白，非空白的定义如下： </p>
	 * <ol>
	 *     <li>不为 {@code null}</li>
	 *     <li>不为空字符串：{@code ""}</li>
	 * </ol>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isNotEmpty(null)     // false}</li>
	 *     <li>{@code StrUtil.isNotEmpty("")       // false}</li>
	 *     <li>{@code StrUtil.isNotEmpty(" \t\n")  // true}</li>
	 *     <li>{@code StrUtil.isNotEmpty("abc")    // true}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isNotBlank(CharSequence)} 的区别是：该方法不校验空白字符。</p>
	 * <p>建议：该方法建议用于工具类或任何可以预期的方法参数的校验中。</p>
	 *
	 * @param str 被检测的字符串
	 * @return 是否为非空
	 * @see #isEmpty(CharSequence)
	 */
	public static boolean isNotEmpty(CharSequence str) {
		return false == isEmpty(str);
	}

	/**
	 * 如果字符串是 {@code null}，则返回指定默认字符串，否则返回字符串本身。
	 *
	 * <pre>
	 * nullToDefault(null, &quot;default&quot;)  = &quot;default&quot;
	 * nullToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;&quot;
	 * nullToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
	 * nullToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
	 * </pre>
	 *
	 * @param str        要转换的字符串
	 * @param defaultStr 默认字符串
	 * @return 字符串本身或指定的默认字符串
	 */
	public static String nullToDefault(CharSequence str, String defaultStr) {
		return (str == null) ? defaultStr : str.toString();
	}

	/**
	 * 检查字符串是否为null、“null”、“undefined”
	 *
	 * @param str 被检查的字符串
	 * @return 是否为null、“null”、“undefined”
	 * @since 4.0.10
	 */
	public static boolean isNullOrUndefined(CharSequence str) {
		if (null == str) {
			return true;
		}
		return isNullOrUndefinedStr(str);
	}

	/**
	 * 是否为“null”、“undefined”，不做空指针检查
	 *
	 * @param str 字符串
	 * @return 是否为“null”、“undefined”
	 */
	private static boolean isNullOrUndefinedStr(CharSequence str) {
		String strString = str.toString().trim();
		return NULL.equals(strString) || "undefined".equals(strString);
	}

	// ------------------------------------------------------------------------ Trim

	// ------------------------------------------------------------------------ startWith

	/**
	 * 是否以指定字符串开头<br>
	 * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
	 *
	 * @param str        被监测字符串
	 * @param prefix     开头字符串
	 * @param ignoreCase 是否忽略大小写
	 * @return 是否以指定字符串开头
	 * @since 5.4.3
	 */
	public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
		return startWith(str, prefix, ignoreCase, false);
	}

	/**
	 * 是否以指定字符串开头<br>
	 * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false<br>
	 * <pre>
	 *     CharSequenceUtil.startWith("123", "123", false, true);   -- false
	 *     CharSequenceUtil.startWith("ABCDEF", "abc", true, true); -- true
	 *     CharSequenceUtil.startWith("abc", "abc", true, true);    -- false
	 * </pre>
	 *
	 * @param str          被监测字符串
	 * @param prefix       开头字符串
	 * @param ignoreCase   是否忽略大小写
	 * @param ignoreEquals 是否忽略字符串相等的情况
	 * @return 是否以指定字符串开头
	 * @since 5.4.3
	 */
	public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase, boolean ignoreEquals) {
		if (null == str || null == prefix) {
			if (ignoreEquals) {
				return false;
			}
			return null == str && null == prefix;
		}

		boolean isStartWith = str.toString()
				.regionMatches(ignoreCase, 0, prefix.toString(), 0, prefix.length());

		if (isStartWith) {
			return (false == ignoreEquals) || (false == equals(str, prefix, ignoreCase));
		}
		return false;
	}

	/**
	 * 是否以指定字符串开头，忽略相等字符串的情况
	 *
	 * @param str    被监测字符串
	 * @param prefix 开头字符串
	 * @return 是否以指定字符串开头并且两个字符串不相等
	 */
	public static boolean startWithIgnoreEquals(CharSequence str, CharSequence prefix) {
		return startWith(str, prefix, false, true);
	}

	/**
	 * 是否以指定字符串开头，忽略大小写
	 *
	 * @param str    被监测字符串
	 * @param prefix 开头字符串
	 * @return 是否以指定字符串开头
	 */
	public static boolean startWithIgnoreCase(CharSequence str, CharSequence prefix) {
		return startWith(str, prefix, true);
	}

	// ------------------------------------------------------------------------ endWith

	// ------------------------------------------------------------------------ contains

	/**
	 * 指定字符是否在字符串中出现过
	 *
	 * @param str        字符串
	 * @param searchChar 被查找的字符
	 * @return 是否包含
	 * @since 3.1.2
	 */
	public static boolean contains(CharSequence str, char searchChar) {
		return indexOf(str, searchChar) > -1;
	}

	/**
	 * 是否包含特定字符，忽略大小写，如果给定两个参数都为{@code null}，返回true
	 *
	 * @param str     被检测字符串
	 * @param testStr 被测试是否包含的字符串
	 * @return 是否包含
	 */
	public static boolean containsIgnoreCase(CharSequence str, CharSequence testStr) {
		if (null == str) {
			// 如果被监测字符串和
			return null == testStr;
		}
		return indexOfIgnoreCase(str, testStr) > -1;
	}

	/**
	 * 查找指定字符串是否包含指定字符串列表中的任意一个字符串<br>
	 * 忽略大小写
	 *
	 * @param str      指定字符串
	 * @param testStrs 需要检查的字符串数组
	 * @return 是否包含任意一个字符串
	 * @since 3.2.0
	 */
	public static boolean containsAnyIgnoreCase(CharSequence str, CharSequence... testStrs) {
		return null != getContainsStrIgnoreCase(str, testStrs);
	}

	/**
	 * 查找指定字符串是否包含指定字符串列表中的任意一个字符串，如果包含返回找到的第一个字符串<br>
	 * 忽略大小写
	 *
	 * @param str      指定字符串
	 * @param testStrs 需要检查的字符串数组
	 * @return 被包含的第一个字符串
	 * @since 3.2.0
	 */
	public static String getContainsStrIgnoreCase(CharSequence str, CharSequence... testStrs) {
		if (isEmpty(str) || ArrayUtil.isEmpty(testStrs)) {
			return null;
		}
		for (CharSequence testStr : testStrs) {
			if (containsIgnoreCase(str, testStr)) {
				return testStr.toString();
			}
		}
		return null;
	}

	// ------------------------------------------------------------------------ indexOf

	/**
	 * 指定范围内查找指定字符
	 *
	 * @param str        字符串
	 * @param searchChar 被查找的字符
	 * @return 位置
	 */
	public static int indexOf(CharSequence str, char searchChar) {
		return indexOf(str, searchChar, 0);
	}

	/**
	 * 指定范围内查找指定字符
	 *
	 * @param str        字符串
	 * @param searchChar 被查找的字符
	 * @param start      起始位置，如果小于0，从0开始查找
	 * @return 位置
	 */
	public static int indexOf(CharSequence str, char searchChar, int start) {
		if (str instanceof String) {
			return ((String) str).indexOf(searchChar, start);
		} else {
			return indexOf(str, searchChar, start, -1);
		}
	}

	/**
	 * 指定范围内查找指定字符
	 *
	 * @param text       字符串
	 * @param searchChar 被查找的字符
	 * @param start      起始位置，如果小于0，从0开始查找
	 * @param end        终止位置，如果超过str.length()则默认查找到字符串末尾
	 * @return 位置
	 */
	public static int indexOf(CharSequence text, char searchChar, int start, int end) {
		if (isEmpty(text)) {
			return INDEX_NOT_FOUND;
		}
		return new CharFinder(searchChar).setText(text).setEndIndex(end).start(start);
	}

	/**
	 * 指定范围内查找字符串，忽略大小写<br>
	 *
	 * <pre>
	 * StrUtil.indexOfIgnoreCase(null, *, *)          = -1
	 * StrUtil.indexOfIgnoreCase(*, null, *)          = -1
	 * StrUtil.indexOfIgnoreCase("", "", 0)           = 0
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
	 * StrUtil.indexOfIgnoreCase("abc", "", 9)        = -1
	 * </pre>
	 *
	 * @param str       字符串
	 * @param searchStr 需要查找位置的字符串
	 * @return 位置
	 * @since 3.2.1
	 */
	public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
		return indexOfIgnoreCase(str, searchStr, 0);
	}

	/**
	 * 指定范围内查找字符串
	 *
	 * <pre>
	 * StrUtil.indexOfIgnoreCase(null, *, *)          = -1
	 * StrUtil.indexOfIgnoreCase(*, null, *)          = -1
	 * StrUtil.indexOfIgnoreCase("", "", 0)           = 0
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
	 * StrUtil.indexOfIgnoreCase("abc", "", 9)        = -1
	 * </pre>
	 *
	 * @param str       字符串
	 * @param searchStr 需要查找位置的字符串
	 * @param fromIndex 起始位置
	 * @return 位置
	 * @since 3.2.1
	 */
	public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int fromIndex) {
		return indexOf(str, searchStr, fromIndex, true);
	}

	/**
	 * 指定范围内查找字符串
	 *
	 * @param text       字符串，空则返回-1
	 * @param searchStr  需要查找位置的字符串，空则返回-1
	 * @param from       起始位置（包含）
	 * @param ignoreCase 是否忽略大小写
	 * @return 位置
	 * @since 3.2.1
	 */
	public static int indexOf(CharSequence text, CharSequence searchStr, int from, boolean ignoreCase) {
		if (isEmpty(text) || isEmpty(searchStr)) {
			if (StrUtil.equals(text, searchStr)) {
				return 0;
			} else {
				return INDEX_NOT_FOUND;
			}
		}
		return new StrFinder(searchStr, ignoreCase).setText(text).start(from);
	}

	// ------------------------------------------------------------------------ remove

	/**
	 * 去除字符串中指定的多个字符，如有多个则全部去除
	 *
	 * @param str   字符串
	 * @param chars 字符列表
	 * @return 去除后的字符
	 * @since 4.2.2
	 */
	public static String removeAll(CharSequence str, char... chars) {
		if (null == str || ArrayUtil.isEmpty(chars)) {
			return str(str);
		}
		final int len = str.length();
		if (0 == len) {
			return str(str);
		}
		final StringBuilder builder = new StringBuilder(len);
		char c;
		for (int i = 0; i < len; i++) {
			c = str.charAt(i);
			if (false == ArrayUtil.contains(chars, c)) {
				builder.append(c);
			}
		}
		return builder.toString();
	}

	/**
	 * 去掉指定前缀
	 *
	 * @param str    字符串
	 * @param prefix 前缀
	 * @return 切掉后的字符串，若前缀不是 preffix， 返回原字符串
	 */
	public static String removePrefix(CharSequence str, CharSequence prefix) {
		if (isEmpty(str) || isEmpty(prefix)) {
			return str(str);
		}

		final String str2 = str.toString();
		if (str2.startsWith(prefix.toString())) {
			return subSuf(str2, prefix.length());// 截取后半段
		}
		return str2;
	}

	/**
	 * 去掉指定后缀
	 *
	 * @param str    字符串
	 * @param suffix 后缀
	 * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
	 */
	public static String removeSuffix(CharSequence str, CharSequence suffix) {
		if (isEmpty(str) || isEmpty(suffix)) {
			return str(str);
		}

		final String str2 = str.toString();
		if (str2.endsWith(suffix.toString())) {
			return subPre(str2, str2.length() - suffix.length());// 截取前半段
		}
		return str2;
	}

	// ------------------------------------------------------------------------ split

	/**
	 * 切分字符串，去除切分后每个元素两边的空白符，去除空白项
	 *
	 * @param str       被切分的字符串
	 * @param separator 分隔符字符
	 * @return 切分后的集合
	 * @since 3.1.2
	 */
	public static List<String> splitTrim(CharSequence str, char separator) {
		return splitTrim(str, separator, -1);
	}

	/**
	 * 切分字符串，去除切分后每个元素两边的空白符，去除空白项
	 *
	 * @param str       被切分的字符串
	 * @param separator 分隔符字符
	 * @param limit     限制分片数，-1不限制
	 * @return 切分后的集合
	 * @since 3.1.0
	 */
	public static List<String> splitTrim(CharSequence str, char separator, int limit) {
		return split(str, separator, limit, true, true);
	}

	/**
	 * 切分字符串
	 *
	 * @param str         被切分的字符串
	 * @param separator   分隔符字符
	 * @param limit       限制分片数，-1不限制
	 * @param isTrim      是否去除切分字符串后每个元素两边的空格
	 * @param ignoreEmpty 是否忽略空串
	 * @return 切分后的集合
	 * @since 3.0.8
	 */
	public static List<String> split(CharSequence str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
		return StrSplitter.split(str, separator, limit, isTrim, ignoreEmpty);
	}

	// ------------------------------------------------------------------------ sub

	/**
	 * 改进JDK subString<br>
	 * index从0开始计算，最后一个字符为-1<br>
	 * 如果from和to位置一样，返回 "" <br>
	 * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length<br>
	 * 如果经过修正的index中from大于to，则互换from和to example: <br>
	 * abcdefgh 2 3 =》 c <br>
	 * abcdefgh 2 -3 =》 cde <br>
	 *
	 * @param str              String
	 * @param fromIndexInclude 开始的index（包括）
	 * @param toIndexExclude   结束的index（不包括）
	 * @return 字串
	 */
	public static String sub(CharSequence str, int fromIndexInclude, int toIndexExclude) {
		if (isEmpty(str)) {
			return str(str);
		}
		int len = str.length();

		if (fromIndexInclude < 0) {
			fromIndexInclude = len + fromIndexInclude;
			if (fromIndexInclude < 0) {
				fromIndexInclude = 0;
			}
		} else if (fromIndexInclude > len) {
			fromIndexInclude = len;
		}

		if (toIndexExclude < 0) {
			toIndexExclude = len + toIndexExclude;
			if (toIndexExclude < 0) {
				toIndexExclude = len;
			}
		} else if (toIndexExclude > len) {
			toIndexExclude = len;
		}

		if (toIndexExclude < fromIndexInclude) {
			int tmp = fromIndexInclude;
			fromIndexInclude = toIndexExclude;
			toIndexExclude = tmp;
		}

		if (fromIndexInclude == toIndexExclude) {
			return EMPTY;
		}

		return str.toString().substring(fromIndexInclude, toIndexExclude);
	}

	/**
	 * 切割指定位置之前部分的字符串
	 *
	 * @param string         字符串
	 * @param toIndexExclude 切割到的位置（不包括）
	 * @return 切割后的剩余的前半部分字符串
	 */
	public static String subPre(CharSequence string, int toIndexExclude) {
		return sub(string, 0, toIndexExclude);
	}

	/**
	 * 切割指定位置之后部分的字符串
	 *
	 * @param string    字符串
	 * @param fromIndex 切割开始的位置（包括）
	 * @return 切割后后剩余的后半部分字符串
	 */
	public static String subSuf(CharSequence string, int fromIndex) {
		if (isEmpty(string)) {
			return null;
		}
		return sub(string, fromIndex, string.length());
	}

	/**
	 * 截取分隔字符串之前的字符串，不包括分隔字符串<br>
	 * 如果给定的字符串为空串（null或""）或者分隔字符串为null，返回原字符串<br>
	 * 如果分隔字符串为空串""，则返回空串，如果分隔字符串未找到，返回原字符串，举例如下：
	 *
	 * <pre>
	 * StrUtil.subBefore(null, *, false)      = null
	 * StrUtil.subBefore("", *, false)        = ""
	 * StrUtil.subBefore("abc", "a", false)   = ""
	 * StrUtil.subBefore("abcba", "b", false) = "a"
	 * StrUtil.subBefore("abc", "c", false)   = "ab"
	 * StrUtil.subBefore("abc", "d", false)   = "abc"
	 * StrUtil.subBefore("abc", "", false)    = ""
	 * StrUtil.subBefore("abc", null, false)  = "abc"
	 * </pre>
	 *
	 * @param string          被查找的字符串
	 * @param separator       分隔字符串（不包括）
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return 切割后的字符串
	 * @since 3.1.1
	 */
	public static String subBefore(CharSequence string, CharSequence separator, boolean isLastSeparator) {
		if (isEmpty(string) || separator == null) {
			return null == string ? null : string.toString();
		}

		final String str = string.toString();
		final String sep = separator.toString();
		if (sep.isEmpty()) {
			return EMPTY;
		}
		final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
		if (INDEX_NOT_FOUND == pos) {
			return str;
		}
		if (0 == pos) {
			return EMPTY;
		}
		return str.substring(0, pos);
	}

	/**
	 * 截取分隔字符串之前的字符串，不包括分隔字符串<br>
	 * 如果给定的字符串为空串（null或""）或者分隔字符串为null，返回原字符串<br>
	 * 如果分隔字符串未找到，返回原字符串，举例如下：
	 *
	 * <pre>
	 * StrUtil.subBefore(null, *, false)      = null
	 * StrUtil.subBefore("", *, false)        = ""
	 * StrUtil.subBefore("abc", 'a', false)   = ""
	 * StrUtil.subBefore("abcba", 'b', false) = "a"
	 * StrUtil.subBefore("abc", 'c', false)   = "ab"
	 * StrUtil.subBefore("abc", 'd', false)   = "abc"
	 * </pre>
	 *
	 * @param string          被查找的字符串
	 * @param separator       分隔字符串（不包括）
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return 切割后的字符串
	 * @since 4.1.15
	 */
	public static String subBefore(CharSequence string, char separator, boolean isLastSeparator) {
		if (isEmpty(string)) {
			return null == string ? null : EMPTY;
		}

		final String str = string.toString();
		final int pos = isLastSeparator ? str.lastIndexOf(separator) : str.indexOf(separator);
		if (INDEX_NOT_FOUND == pos) {
			return str;
		}
		if (0 == pos) {
			return EMPTY;
		}
		return str.substring(0, pos);
	}

	/**
	 * 截取分隔字符串之后的字符串，不包括分隔字符串<br>
	 * 如果给定的字符串为空串（null或""），返回原字符串<br>
	 * 如果分隔字符串为空串（null或""），则返回空串，如果分隔字符串未找到，返回空串，举例如下：
	 *
	 * <pre>
	 * StrUtil.subAfter(null, *, false)      = null
	 * StrUtil.subAfter("", *, false)        = ""
	 * StrUtil.subAfter(*, null, false)      = ""
	 * StrUtil.subAfter("abc", "a", false)   = "bc"
	 * StrUtil.subAfter("abcba", "b", false) = "cba"
	 * StrUtil.subAfter("abc", "c", false)   = ""
	 * StrUtil.subAfter("abc", "d", false)   = ""
	 * StrUtil.subAfter("abc", "", false)    = "abc"
	 * </pre>
	 *
	 * @param string          被查找的字符串
	 * @param separator       分隔字符串（不包括）
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return 切割后的字符串
	 * @since 3.1.1
	 */
	public static String subAfter(CharSequence string, CharSequence separator, boolean isLastSeparator) {
		if (isEmpty(string)) {
			return null == string ? null : EMPTY;
		}
		if (separator == null) {
			return EMPTY;
		}
		final String str = string.toString();
		final String sep = separator.toString();
		final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
		if (INDEX_NOT_FOUND == pos || (string.length() - 1) == pos) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * 截取分隔字符串之后的字符串，不包括分隔字符串<br>
	 * 如果给定的字符串为空串（null或""），返回原字符串<br>
	 * 如果分隔字符串为空串（null或""），则返回空串，如果分隔字符串未找到，返回空串，举例如下：
	 *
	 * <pre>
	 * StrUtil.subAfter(null, *, false)      = null
	 * StrUtil.subAfter("", *, false)        = ""
	 * StrUtil.subAfter("abc", 'a', false)   = "bc"
	 * StrUtil.subAfter("abcba", 'b', false) = "cba"
	 * StrUtil.subAfter("abc", 'c', false)   = ""
	 * StrUtil.subAfter("abc", 'd', false)   = ""
	 * </pre>
	 *
	 * @param string          被查找的字符串
	 * @param separator       分隔字符串（不包括）
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return 切割后的字符串
	 * @since 4.1.15
	 */
	public static String subAfter(CharSequence string, char separator, boolean isLastSeparator) {
		if (isEmpty(string)) {
			return null == string ? null : EMPTY;
		}
		final String str = string.toString();
		final int pos = isLastSeparator ? str.lastIndexOf(separator) : str.indexOf(separator);
		if (INDEX_NOT_FOUND == pos) {
			return EMPTY;
		}
		return str.substring(pos + 1);
	}

	/**
	 * 截取指定字符串中间部分，不包括标识字符串<br>
	 * <p>
	 * 栗子：
	 *
	 * <pre>
	 * StrUtil.subBetween("wx[b]yz", "[", "]") = "b"
	 * StrUtil.subBetween(null, *, *)          = null
	 * StrUtil.subBetween(*, null, *)          = null
	 * StrUtil.subBetween(*, *, null)          = null
	 * StrUtil.subBetween("", "", "")          = ""
	 * StrUtil.subBetween("", "", "]")         = null
	 * StrUtil.subBetween("", "[", "]")        = null
	 * StrUtil.subBetween("yabcz", "", "")     = ""
	 * StrUtil.subBetween("yabcz", "y", "z")   = "abc"
	 * StrUtil.subBetween("yabczyabcz", "y", "z")   = "abc"
	 * </pre>
	 *
	 * @param str    被切割的字符串
	 * @param before 截取开始的字符串标识
	 * @param after  截取到的字符串标识
	 * @return 截取后的字符串
	 * @since 3.1.1
	 */
	public static String subBetween(CharSequence str, CharSequence before, CharSequence after) {
		if (str == null || before == null || after == null) {
			return null;
		}

		final String str2 = str.toString();
		final String before2 = before.toString();
		final String after2 = after.toString();

		final int start = str2.indexOf(before2);
		if (start != INDEX_NOT_FOUND) {
			final int end = str2.indexOf(after2, start + before2.length());
			if (end != INDEX_NOT_FOUND) {
				return str2.substring(start + before2.length(), end);
			}
		}
		return null;
	}

	// ------------------------------------------------------------------------ repeat

	/**
	 * 重复某个字符
	 *
	 * <pre>
	 * StrUtil.repeat('e', 0)  = ""
	 * StrUtil.repeat('e', 3)  = "eee"
	 * StrUtil.repeat('e', -2) = ""
	 * </pre>
	 *
	 * @param c     被重复的字符
	 * @param count 重复的数目，如果小于等于0则返回""
	 * @return 重复字符字符串
	 */
	public static String repeat(char c, int count) {
		if (count <= 0) {
			return EMPTY;
		}

		char[] result = new char[count];
		for (int i = 0; i < count; i++) {
			result[i] = c;
		}
		return new String(result);
	}

	// ------------------------------------------------------------------------ equals

	/**
	 * 比较两个字符串（大小写敏感）。
	 *
	 * <pre>
	 * equals(null, null)   = true
	 * equals(null, &quot;abc&quot;)  = false
	 * equals(&quot;abc&quot;, null)  = false
	 * equals(&quot;abc&quot;, &quot;abc&quot;) = true
	 * equals(&quot;abc&quot;, &quot;ABC&quot;) = false
	 * </pre>
	 *
	 * @param str1 要比较的字符串1
	 * @param str2 要比较的字符串2
	 * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
	 */
	public static boolean equals(CharSequence str1, CharSequence str2) {
		return equals(str1, str2, false);
	}

	/**
	 * 比较两个字符串（大小写不敏感）。
	 *
	 * <pre>
	 * equalsIgnoreCase(null, null)   = true
	 * equalsIgnoreCase(null, &quot;abc&quot;)  = false
	 * equalsIgnoreCase(&quot;abc&quot;, null)  = false
	 * equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
	 * equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
	 * </pre>
	 *
	 * @param str1 要比较的字符串1
	 * @param str2 要比较的字符串2
	 * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
	 */
	public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
		return equals(str1, str2, true);
	}

	/**
	 * 比较两个字符串是否相等，规则如下
	 * <ul>
	 *     <li>str1和str2都为{@code null}</li>
	 *     <li>忽略大小写使用{@link String#equalsIgnoreCase(String)}判断相等</li>
	 *     <li>不忽略大小写使用{@link String#contentEquals(CharSequence)}判断相等</li>
	 * </ul>
	 *
	 * @param str1       要比较的字符串1
	 * @param str2       要比较的字符串2
	 * @param ignoreCase 是否忽略大小写
	 * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
	 * @since 3.2.0
	 */
	public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
		if (null == str1) {
			// 只有两个都为null才判断相等
			return str2 == null;
		}
		if (null == str2) {
			// 字符串2空，字符串1非空，直接false
			return false;
		}

		if (ignoreCase) {
			return str1.toString().equalsIgnoreCase(str2.toString());
		} else {
			return str1.toString().contentEquals(str2);
		}
	}

	// ------------------------------------------------------------------------ format

	/**
	 * 格式化文本, {} 表示占位符<br>
	 * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
	 * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
	 * 例：<br>
	 * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
	 * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is {} for a<br>
	 * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
	 *
	 * @param template 文本模板，被替换的部分用 {} 表示，如果模板为null，返回"null"
	 * @param params   参数值
	 * @return 格式化后的文本，如果模板为null，返回"null"
	 */
	public static String format(CharSequence template, Object... params) {
		if (null == template) {
			return NULL;
		}
		if (ArrayUtil.isEmpty(params) || isBlank(template)) {
			return template.toString();
		}
		return StrFormatter.format(template.toString(), params);
	}

	// ------------------------------------------------------------------------ str

	/**
	 * {@link CharSequence} 转为字符串，null安全
	 *
	 * @param cs {@link CharSequence}
	 * @return 字符串
	 */
	public static String str(CharSequence cs) {
		return null == cs ? null : cs.toString();
	}

	// ------------------------------------------------------------------------ count

	/**
	 * 统计指定内容中包含指定字符的数量
	 *
	 * @param content       内容
	 * @param charForSearch 被统计的字符
	 * @return 包含数量
	 */
	public static int count(CharSequence content, char charForSearch) {
		int count = 0;
		if (isEmpty(content)) {
			return 0;
		}
		int contentLength = content.length();
		for (int i = 0; i < contentLength; i++) {
			if (charForSearch == content.charAt(i)) {
				count++;
			}
		}
		return count;
	}

	// ------------------------------------------------------------------------ getter and setter


	/**
	 * 以 conjunction 为分隔符将多个对象转换为字符串
	 *
	 * @param <T>         元素类型
	 * @param conjunction 分隔符 {@link StrPool#COMMA}
	 * @param iterable    集合
	 * @return 连接后的字符串
	 * @see CollUtil#join(Iterable, CharSequence)
	 * @since 5.6.6
	 */
	public static <T> String join(CharSequence conjunction, Iterable<T> iterable) {
		return CollUtil.join(iterable, conjunction);
	}

}