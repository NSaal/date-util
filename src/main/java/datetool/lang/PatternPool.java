package datetool.lang;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/**
 * 常用正则表达式集合，更多正则见:<br>
 * <a href="https://any86.github.io/any-rule/">https://any86.github.io/any-rule/</a>
 *
 * @author Looly
 */
public class PatternPool {

	/**
	 * 时间正则
	 */
	public static final Pattern TIME = Pattern.compile("\\d{1,2}:\\d{1,2}(:\\d{1,2})?");

	// -------------------------------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * Pattern池
	 */
	private static final ConcurrentMap<RegexWithFlag, Pattern> POOL = new ConcurrentHashMap<>();

	/**
	 * 先从Pattern池中查找正则对应的{@link Pattern}，找不到则编译正则表达式并入池。
	 *
	 * @param regex 正则表达式
	 * @param flags 正则标识位集合 {@link Pattern}
	 * @return {@link Pattern}
	 */
	public static Pattern get(String regex, int flags) {
		final RegexWithFlag regexWithFlag = new RegexWithFlag(regex, flags);
		return POOL.computeIfAbsent(regexWithFlag, (key)-> Pattern.compile(regex, flags));
	}

	// ---------------------------------------------------------------------------------------------------------------------------------

	/**
	 * 正则表达式和正则标识位的包装
	 *
	 * @author Looly
	 */
	private static class RegexWithFlag {
		private final String regex;
		private final int flag;

		/**
		 * 构造
		 *
		 * @param regex 正则
		 * @param flag  标识
		 */
		public RegexWithFlag(String regex, int flag) {
			this.regex = regex;
			this.flag = flag;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + flag;
			result = prime * result + ((regex == null) ? 0 : regex.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			RegexWithFlag other = (RegexWithFlag) obj;
			if (flag != other.flag) {
				return false;
			}
			if (regex == null) {
				return other.regex == null;
			} else {
				return regex.equals(other.regex);
			}
		}

	}
}
