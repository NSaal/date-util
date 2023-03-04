package datetool.map;

import datetool.collection.CollUtil;
import datetool.collection.ListUtil;
import datetool.util.ObjectUtil;

import java.io.Serializable;
import java.util.*;

/**
 * 可重复键和值的Map<br>
 * 通过键值单独建立List方式，使键值对一一对应，实现正向和反向两种查找<br>
 * 无论是正向还是反向，都是遍历列表查找过程，相比标准的HashMap要慢，数据越多越慢
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author looly
 */
public class TableMap<K, V> implements Map<K, V>, Iterable<Map.Entry<K, V>>, Serializable {
	private static final long serialVersionUID = 1L;

	private final List<K> keys;
	private final List<V> values;

	/**
	 * 构造
	 *
	 * @param size 初始容量
	 */
	public TableMap(int size) {
		this.keys = new ArrayList<>(size);
		this.values = new ArrayList<>(size);
	}

	@Override
	public int size() {
		return keys.size();
	}

	@Override
	public boolean isEmpty() {
		return CollUtil.isEmpty(keys);
	}

	@Override
	public boolean containsKey(Object key) {
		//noinspection SuspiciousMethodCalls
		return keys.contains(key);
	}

	@Override
	public boolean containsValue(Object value) {
		//noinspection SuspiciousMethodCalls
		return values.contains(value);
	}

	@Override
	public V get(Object key) {
		//noinspection SuspiciousMethodCalls
		final int index = keys.indexOf(key);
		if (index > -1) {
			return values.get(index);
		}
		return null;
	}

	/**
	 * 获取指定key对应的所有值
	 *
	 * @param key 键
	 * @return 值列表
	 * @since 5.2.5
	 */
	public List<V> getValues(K key) {
		return CollUtil.getAny(
				this.values,
				ListUtil.indexOfAll(this.keys, (ele) -> ObjectUtil.equal(ele, key))
		);
	}

	@Override
	public V put(K key, V value) {
		keys.add(key);
		values.add(value);
		return null;
	}

	/**
	 * 移除指定的所有键和对应的所有值
	 *
	 * @param key 键
	 * @return 最后一个移除的值
	 */
	@Override
	public V remove(Object key) {
		V lastValue = null;
		int index;
		//noinspection SuspiciousMethodCalls
		while ((index = keys.indexOf(key)) > -1) {
			lastValue = removeByIndex(index);
		}
		return lastValue;
	}

	/**
	 * 移除指定位置的键值对
	 *
	 * @param index 位置，不能越界
	 * @return 移除的值
	 */
	public V removeByIndex(final int index) {
		keys.remove(index);
		return values.remove(index);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		keys.clear();
		values.clear();
	}

	@Override
	public Set<K> keySet() {
		return new HashSet<>(this.keys);
	}

	@Override
	public Collection<V> values() {
		return Collections.unmodifiableList(this.values);
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		final Set<Entry<K, V>> hashSet = new LinkedHashSet<>();
		for (int i = 0; i < size(); i++) {
			hashSet.add(MapUtil.entry(keys.get(i), values.get(i)));
		}
		return hashSet;
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return new Iterator<Entry<K, V>>() {
			private final Iterator<K> keysIter = keys.iterator();
			private final Iterator<V> valuesIter = values.iterator();

			@Override
			public boolean hasNext() {
				return keysIter.hasNext() && valuesIter.hasNext();
			}

			@Override
			public Entry<K, V> next() {
				return MapUtil.entry(keysIter.next(), valuesIter.next());
			}

			@Override
			public void remove() {
				keysIter.remove();
				valuesIter.remove();
			}
		};
	}

}
