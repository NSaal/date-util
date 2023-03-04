package datetool.map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 安全的ConcurrentHashMap实现<br>
 * 此类用于解决在JDK8中调用{@link ConcurrentHashMap#computeIfAbsent(Object, Function)}可能造成的死循环问题。<br>
 * 方法来自Dubbo，见：issues#2349<br>
 * <p>
 * 相关bug见：@see <a href="https://bugs.openjdk.java.net/browse/JDK-8161372">https://bugs.openjdk.java.net/browse/JDK-8161372</a>
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class SafeConcurrentHashMap<K, V> extends ConcurrentHashMap<K, V> {
	private static final long serialVersionUID = 1L;

	// region == 构造 ==

	/**
	 * 构造，默认初始大小（16）
	 */
	public SafeConcurrentHashMap() {
		super();
	}

	/**
	 * 构造
	 *
	 * @param initialCapacity 预估初始大小
	 */
	public SafeConcurrentHashMap(int initialCapacity) {
		super(initialCapacity);
	}


}
