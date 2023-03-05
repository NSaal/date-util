package datetool.lang;

import datetool.date.DateUtil;

import java.util.function.Supplier;

/**
 * 断言<br>
 * 断言某些对象或值是否符合规定，否则抛出异常。经常用于做变量检查
 *
 * @author Looly
 */
public class Assert {


	// ----------------------------------------------------------------------------------------------------------- Check not null

	/**
	 * 断言对象是否不为{@code null} ，如果为{@code null} 抛出{@link IllegalArgumentException} 异常 Assert that an object is not {@code null} .
	 *
	 * <pre class="code">
	 * Assert.notNull(clazz, "The class must not be null");
	 * </pre>
	 *
	 * @param <T>              被检查对象泛型类型
	 * @param object           被检查对象
	 * @param errorMsgTemplate 错误消息模板，变量使用{}表示
	 * @param params           参数
	 * @return 被检查后的对象
	 * @throws IllegalArgumentException if the object is {@code null}
	 */
	public static <T> T notNull(T object, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
		if (null == object) {
			throw ((Supplier<IllegalArgumentException>) () -> new IllegalArgumentException(DateUtil.format(errorMsgTemplate, params))).get();
		}
		return object;
	}

	// ----------------------------------------------------------------------------------------------------------- Check empty

}
