package datetool.util;

import java.lang.reflect.Array;

/**
 * 数组工具类
 *
 * @author Looly
 */
public class ArrayUtil extends PrimitiveArrayUtil {

	// ---------------------------------------------------------------------- isEmpty

	/**
	 * 数组是否为空
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @return 是否为空
	 */
	public static <T> boolean isEmpty(T[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 数组是否为空<br>
	 * 此方法会匹配单一对象，如果此对象为{@code null}则返回true<br>
	 * 如果此对象为非数组，理解为此对象为数组的第一个元素，则返回false<br>
	 * 如果此对象为数组对象，数组长度大于0情况下返回false，否则返回true
	 *
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(Object array) {
		if (array != null) {
			if (isArray(array)) {
				return 0 == Array.getLength(array);
			}
			return false;
		}
		return true;
	}

	// ---------------------------------------------------------------------- isNotEmpty

	/**
	 * 克隆数组，如果非数组返回{@code null}
	 *
	 * @param <T> 数组元素类型
	 * @param obj 数组对象
	 * @return 克隆后的数组对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T clone(final T obj) {
		if (null == obj) {
			return null;
		}
		if (isArray(obj)) {
			final Object result;
			final Class<?> componentType = obj.getClass().getComponentType();
			if (componentType.isPrimitive()) {// 原始类型
				int length = Array.getLength(obj);
				result = Array.newInstance(componentType, length);
				while (length-- > 0) {
					Array.set(result, length, Array.get(obj, length));
				}
			} else {
				result = ((Object[]) obj).clone();
			}
			return (T) result;
		}
		return null;
	}

	// ------------------------------------------------------------------- indexOf and lastIndexOf and contains

	/**
	 * 返回数组中指定元素所在位置，忽略大小写，未找到返回{@link #INDEX_NOT_FOUND}
	 *
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
	 * @since 3.1.2
	 */
	public static int indexOfIgnoreCase(CharSequence[] array, CharSequence value) {
		if (null != array) {
			for (int i = 0; i < array.length; i++) {
				if (StrUtil.equalsIgnoreCase(array[i], value)) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	// ------------------------------------------------------------------- Wrap and unwrap

	/**
	 * 对象是否为数组对象
	 *
	 * @param obj 对象
	 * @return 是否为数组对象，如果为{@code null} 返回false
	 */
	public static boolean isArray(Object obj) {
		return null != obj && obj.getClass().isArray();
	}

}
