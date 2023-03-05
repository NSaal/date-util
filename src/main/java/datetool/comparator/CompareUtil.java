package datetool.comparator;

import java.util.Comparator;

/**
 * 比较工具类
 *
 * @author looly
 */
public class CompareUtil {

	/**
	 * {@code null}安全的对象比较，{@code null}对象小于任何对象
	 *
	 * @param <T> 被比较对象类型
	 * @param c1  对象1，可以为{@code null}
	 * @param c2  对象2，可以为{@code null}
	 * @return 比较结果，如果c1 &lt; c2，返回数小于0，c1==c2返回0，c1 &gt; c2 大于0
	 * @see Comparator#compare(Object, Object)
	 */
	public static <T extends Comparable<? super T>> int compare(T c1, T c2) {
		if (c1 == c2) {
			return 0;
		} else if (c1 == null) {
			return -1;
		} else if (c2 == null) {
			return 1;
		}
		return c1.compareTo(c2);
	}

}
