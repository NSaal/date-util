package datetool.date;

import datetool.lang.Assert;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 日期范围
 *
 * @author looly
 * @since 4.1.0
 */
public class DateRange  implements Iterable<DateTime>, Iterator<DateTime>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 锁保证线程安全
	 */
	private final Lock lock = new ReentrantLock();
	/**
	 * 起始对象
	 */
	private final DateTime start;
	/**
	 * 结束对象
	 */
	private final DateTime end;
	/**
	 * 下一个对象
	 */
	private DateTime next;
	/**
	 * 步进
	 */
	private final Stepper<DateTime> stepper;
	/**
	 * 索引
	 */
	private int index = 0;
	/**
	 * 是否包含第一个元素
	 */
	private final boolean includeStart;
	/**
	 * 是否包含最后一个元素
	 */
	private final boolean includeEnd;

	/**
	 * 构造，包含开始和结束日期时间
	 *
	 * @param start 起始日期时间（包括）
	 * @param end 结束日期时间（包括）
	 * @param unit 步进单位
	 */
	public DateRange(Date start, Date end, DateField unit) {
		this(start, end, unit, 1);
	}

	/**
	 * 构造，包含开始和结束日期时间
	 *
	 * @param start 起始日期时间（包括）
	 * @param end 结束日期时间（包括）
	 * @param unit 步进单位
	 * @param step 步进数
	 */
	public DateRange(Date start, Date end, DateField unit, int step) {
		this(start, end, unit, step, true, true);
	}

	/**
	 * 构造
	 *
	 * @param start 起始日期时间
	 * @param end 结束日期时间
	 * @param unit 步进单位
	 * @param step 步进数
	 * @param isIncludeStart 是否包含开始的时间
	 * @param isIncludeEnd 是否包含结束的时间
	 */
	public DateRange(Date start, Date end, DateField unit, int step, boolean isIncludeStart, boolean isIncludeEnd) {

		Assert.notNull(DateUtil.date(start), "First element must be not null!");
		this.start = DateUtil.date(start);
		this.end = DateUtil.date(end);
		this.stepper = (current, end1, index) -> {
			final DateTime dt = DateUtil.date(start).offsetNew(unit, (index + 1) * step);
			if (dt.isAfter(end1)) {
				return null;
			}
			return dt;
		};
		this.next = safeStep(this.start);
		this.includeStart = isIncludeStart;
		this.includeEnd = isIncludeEnd;
	}
	@Override
	public boolean hasNext() {
		lock.lock();
		try {
			if (0 == this.index && this.includeStart) {
				return true;
			}
			if (null == this.next) {
				return false;
			} else if (!includeEnd && this.next.equals(this.end)) {
				return false;
			}
		} finally {
			lock.unlock();
		}
		return true;
	}

	@Override
	public DateTime next() {
		lock.lock();
		try {
			if (!this.hasNext()) {
				throw new NoSuchElementException("Has no next range!");
			}
			return nextUncheck();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 获取下一个元素，并将下下个元素准备好
	 */
	private DateTime nextUncheck() {
		DateTime current;
		if(0 == this.index){
			current = start;
			if(!this.includeStart){
				// 获取下一组元素
				index ++;
				return nextUncheck();
			}
		} else {
			current = next;
			this.next = safeStep(this.next);
		}

		index++;
		return current;
	}

	/**
	 * 不抛异常的获取下一步进的元素，如果获取失败返回{@code null}
	 *
	 * @param base  上一个元素
	 * @return 下一步进
	 */
	private DateTime safeStep(DateTime base) {
		final int index = this.index;
		DateTime next = null;
		try {
			next = stepper.step(base, this.end, index);
		} catch (Exception e) {
			// ignore
		}

		return next;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Can not remove ranged element!");
	}

	@Override
	public Iterator<DateTime> iterator() {
		return this;
	}

	/**
	 * 步进接口，此接口用于实现如何对一个对象按照指定步进增加步进<br>
	 * 步进接口可以定义以下逻辑：
	 *
	 * <pre>
	 * 1、步进规则，即对象如何做步进
	 * 2、步进大小，通过实现此接口，在实现类中定义一个对象属性，可灵活定义步进大小
	 * 3、限制range个数，通过实现此接口，在实现类中定义一个对象属性，可灵活定义limit，限制range个数
	 * </pre>
	 *
	 * @param <DateTime> 需要增加步进的对象
	 * @author Looly
	 */
	@FunctionalInterface
	public interface Stepper<DateTime> {
		/**
		 * 增加步进<br>
		 * 增加步进后的返回值如果为{@code null}则表示步进结束<br>
		 * 用户需根据end参数自行定义边界，当达到边界时返回null表示结束，否则Range中边界对象无效，会导致无限循环
		 *
		 * @param current 上一次增加步进后的基础对象
		 * @param end     结束对象
		 * @param index   当前索引（步进到第几个元素），从0开始计数
		 * @return 增加步进后的对象
		 */
		DateTime step(DateTime current, DateTime end, int index);
	}
}
