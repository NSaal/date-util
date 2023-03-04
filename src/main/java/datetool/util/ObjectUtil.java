package datetool.util;

import datetool.collection.IterUtil;
import datetool.comparator.CompareUtil;
import datetool.exceptions.UtilException;
import datetool.map.MapUtil;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 对象工具类，包括判空、克隆、序列化等操作
 *
 * @author Looly
 */
public class ObjectUtil {

	/**
	 * 比较两个对象是否相等。<br>
	 * 相同的条件有两个，满足其一即可：<br>
	 * <ol>
	 * <li>obj1 == null &amp;&amp; obj2 == null</li>
	 * <li>obj1.equals(obj2)</li>
	 * <li>如果是BigDecimal比较，0 == obj1.compareTo(obj2)</li>
	 * </ol>
	 *
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return 是否相等
	 * @see Objects#equals(Object, Object)
	 */
	public static boolean equal(Object obj1, Object obj2) {
		if (obj1 instanceof BigDecimal && obj2 instanceof BigDecimal) {
			return NumberUtil.equals((BigDecimal) obj1, (BigDecimal) obj2);
		}
		return Objects.equals(obj1, obj2);
	}

	/**
	 * 检查对象是否为null<br>
	 * 判断标准为：
	 *
	 * <pre>
	 * 1. == null
	 * 2. equals(null)
	 * </pre>
	 *
	 * @param obj 对象
	 * @return 是否为null
	 */
	public static boolean isNull(Object obj) {
		//noinspection ConstantConditions
		return null == obj || obj.equals(null);
	}

	/**
	 * 判断指定对象是否为空，支持：
	 *
	 * <pre>
	 * 1. CharSequence
	 * 2. Map
	 * 3. Iterable
	 * 4. Iterator
	 * 5. Array
	 * </pre>
	 *
	 * @param obj 被判断的对象
	 * @return 是否为空，如果类型不支持，返回false
	 * @since 4.5.7
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj) {
		if (null == obj) {
			return true;
		}

		if (obj instanceof CharSequence) {
			return StrUtil.isEmpty((CharSequence) obj);
		} else if (obj instanceof Map) {
			return MapUtil.isEmpty((Map) obj);
		} else if (obj instanceof Iterable) {
			return IterUtil.isEmpty((Iterable) obj);
		} else if (obj instanceof Iterator) {
			return IterUtil.isEmpty((Iterator) obj);
		} else if (ArrayUtil.isArray(obj)) {
			return ArrayUtil.isEmpty(obj);
		}

		return false;
	}

	/**
	 * 如果给定对象为{@code null}返回默认值
	 *
	 * <pre>
	 * ObjectUtil.defaultIfNull(null, null)      = null
	 * ObjectUtil.defaultIfNull(null, "")        = ""
	 * ObjectUtil.defaultIfNull(null, "zz")      = "zz"
	 * ObjectUtil.defaultIfNull("abc", *)        = "abc"
	 * ObjectUtil.defaultIfNull(Boolean.TRUE, *) = Boolean.TRUE
	 * </pre>
	 *
	 * @param <T>          对象类型
	 * @param object       被检查对象，可能为{@code null}
	 * @param defaultValue 被检查对象为{@code null}返回的默认值，可以为{@code null}
	 * @return 被检查对象为{@code null}返回默认值，否则返回原值
	 * @since 3.0.7
	 */
	public static <T> T defaultIfNull(final T object, final T defaultValue) {
		return isNull(object) ? defaultValue : object;
	}

	/**
	 * 如果被检查对象为 {@code null}， 返回默认值（由 defaultValueSupplier 提供）；否则直接返回
	 *
	 * @param source               被检查对象
	 * @param defaultValueSupplier 默认值提供者
	 * @param <T>                  对象类型
	 * @return 被检查对象为{@code null}返回默认值，否则返回自定义handle处理后的返回值
	 * @throws NullPointerException {@code defaultValueSupplier == null} 时，抛出
	 * @since 5.7.20
	 */
	public static <T> T defaultIfNull(T source, Supplier<? extends T> defaultValueSupplier) {
		if (isNull(source)) {
			return defaultValueSupplier.get();
		}
		return source;
	}

	/**
	 * 克隆对象<br>
	 * 如果对象实现Cloneable接口，调用其clone方法<br>
	 * 如果实现Serializable接口，执行深度克隆<br>
	 * 否则返回{@code null}
	 *
	 * @param <T> 对象类型
	 * @param obj 被克隆对象
	 * @return 克隆后的对象
	 */
	public static <T> T clone(T obj) {
		T result = ArrayUtil.clone(obj);
		if (null == result) {
			if (obj instanceof Cloneable) {
				result = ReflectUtil.invoke(obj, "clone");
			} else {
				result = cloneByStream(obj);
			}
		}
		return result;
	}

	/**
	 * 序列化后拷贝流的方式克隆<br>
	 * 对象必须实现Serializable接口
	 *
	 * @param <T> 对象类型
	 * @param obj 被克隆对象
	 * @return 克隆后的对象
	 * @throws UtilException IO异常和ClassNotFoundException封装
	 */
	public static <T> T cloneByStream(T obj) {
		return SerializeUtil.clone(obj);
	}

}
