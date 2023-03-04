package datetool.lang;

import datetool.clone.CloneSupport;
import datetool.collection.ArrayIter;
import datetool.collection.ListUtil;

import java.io.Serializable;
import java.util.*;

/**
 * 不可变数组类型（元组），用于多值返回<br>
 * 多值可以支持每个元素值类型不同
 *
 * @author Looly
 */
public class Tuple extends CloneSupport<Tuple> implements Iterable<Object>, Serializable {
	private static final long serialVersionUID = -7689304393482182157L;

	private final Object[] members;

	/**
	 * 构造
	 *
	 * @param members 成员数组
	 */
	public Tuple(Object... members) {
		this.members = members;
	}

	@Override
	public Iterator<Object> iterator() {
		return new ArrayIter<>(members);
	}

}
