package datetool.date;

import datetool.text.CharSequenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 秒表封装<br>
 * 此工具用于存储一组任务的耗时时间，并一次性打印对比。<br>
 * 比如：我们可以记录多段代码耗时时间，然后一次性打印（StopWatch提供了一个prettyString()函数用于按照指定格式打印出耗时）
 *
 * <p>
 * 此工具来自：https://github.com/spring-projects/spring-framework/blob/master/spring-core/src/main/java/org/springframework/util/StopWatch.java
 *
 * <p>
 * 使用方法如下：
 *
 * <pre>
 * StopWatch stopWatch = new StopWatch("任务名称");
 *
 * // 任务1
 * stopWatch.start("任务一");
 * Thread.sleep(1000);
 * stopWatch.stop();
 *
 * // 任务2
 * stopWatch.start("任务二");
 * Thread.sleep(2000);
 * stopWatch.stop();
 *
 * // 打印出耗时
 * Console.log(stopWatch.prettyPrint());
 *
 * </pre>
 *
 * @author Spring Framework, Looly
 * @since 4.6.6
 */
public class StopWatch {

	/**
	 * 秒表唯一标识，用于多个秒表对象的区分
	 */
	private final String id;
	private List<TaskInfo> taskList;

	/**
	 * 任务名称
	 */
	private String currentTaskName;
	/**
	 * 开始时间
	 */
	private long startTimeNanos;

	/**
	 * 总运行时间
	 */
	private long totalTimeNanos;
	// ------------------------------------------------------------------------------------------- Constructor start

	/**
	 * 构造，不启动任何任务
	 */
	public StopWatch() {
		this(CharSequenceUtil.EMPTY);
	}

	/**
	 * 构造，不启动任何任务
	 *
	 * @param id 用于标识秒表的唯一ID
	 */
	public StopWatch(String id) {
		this(id, true);
	}

	/**
	 * 构造，不启动任何任务
	 *
	 * @param id           用于标识秒表的唯一ID
	 * @param keepTaskList 是否在停止后保留任务，{@code false} 表示停止运行后不保留任务
	 */
	public StopWatch(String id, boolean keepTaskList) {
		this.id = id;
		if (keepTaskList) {
			this.taskList = new ArrayList<>();
		}
	}
	// ------------------------------------------------------------------------------------------- Constructor end

	/**
	 * 开始默认的新任务
	 *
	 * @throws IllegalStateException 前一个任务没有结束
	 */
	public void start() throws IllegalStateException {
		start(CharSequenceUtil.EMPTY);
	}

	/**
	 * 开始指定名称的新任务
	 *
	 * @param taskName 新开始的任务名称
	 * @throws IllegalStateException 前一个任务没有结束
	 */
	public void start(String taskName) throws IllegalStateException {
		if (null != this.currentTaskName) {
			throw new IllegalStateException("Can't start StopWatch: it's already running");
		}
		this.currentTaskName = taskName;
		this.startTimeNanos = System.nanoTime();
	}

	/**
	 * 获取所有任务的总花费时间
	 *
	 * @param unit 时间单位，{@code null}表示默认{@link TimeUnit#NANOSECONDS}
	 * @return 花费时间
	 * @since 5.7.16
	 */
	public long getTotal(TimeUnit unit){
		return unit.convert(this.totalTimeNanos, TimeUnit.NANOSECONDS);
	}

	/**
	 * 获取所有任务的总花费时间（纳秒）
	 *
	 * @return 所有任务的总花费时间（纳秒）
	 */
	public long getTotalTimeNanos() {
		return this.totalTimeNanos;
	}

	/**
	 * 获取任务信息，类似于：
	 * <pre>
	 *     StopWatch '[id]': running time = [total] ns
	 * </pre>
	 *
	 * @return 任务信息
	 */
	public String shortSummary() {
		return shortSummary(null);
	}

	/**
	 * 获取任务信息，类似于：
	 * <pre>
	 *     StopWatch '[id]': running time = [total] [unit]
	 * </pre>
	 *
	 * @param unit 时间单位，{@code null}则默认为{@link TimeUnit#NANOSECONDS}
	 * @return 任务信息
	 */
	public String shortSummary(TimeUnit unit) {
		if(null == unit){
			unit = TimeUnit.NANOSECONDS;
		}
		return CharSequenceUtil.format("StopWatch '{}': running time = {} {}",
				this.id, getTotal(unit), DateUtil.getShotName(unit));
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(shortSummary());
		if (null != this.taskList) {
			for (TaskInfo task : this.taskList) {
				sb.append("; [").append(task.getTaskName()).append("] took ").append(task.getTimeNanos()).append(" ns");
				long percent = Math.round(100.0 * task.getTimeNanos() / getTotalTimeNanos());
				sb.append(" = ").append(percent).append("%");
			}
		} else {
			sb.append("; no task info kept");
		}
		return sb.toString();
	}

	/**
	 * 存放任务名称和花费时间对象
	 *
	 * @author Looly
	 */
	public static final class TaskInfo {

		private final String taskName;
		private final long timeNanos;

		/**
		 * 构造
		 *
		 * @param taskName  任务名称
		 * @param timeNanos 花费时间（纳秒）
		 */
		TaskInfo(String taskName, long timeNanos) {
			this.taskName = taskName;
			this.timeNanos = timeNanos;
		}

		/**
		 * 获取任务名
		 *
		 * @return 任务名
		 */
		public String getTaskName() {
			return this.taskName;
		}

		/**
		 * 获取指定单位的任务花费时间
		 *
		 * @param unit 单位
		 * @return 任务花费时间
		 * @since 5.7.16
		 */
		public long getTime(TimeUnit unit) {
			return unit.convert(this.timeNanos, TimeUnit.NANOSECONDS);
		}

		/**
		 * 获取任务花费时间（单位：纳秒）
		 *
		 * @return 任务花费时间（单位：纳秒）
		 * @see #getTimeMillis()
		 * @see #getTimeSeconds()
		 */
		public long getTimeNanos() {
			return this.timeNanos;
		}

		/**
		 * 获取任务花费时间（单位：毫秒）
		 *
		 * @return 任务花费时间（单位：毫秒）
		 * @see #getTimeNanos()
		 * @see #getTimeSeconds()
		 */
		public long getTimeMillis() {
			return getTime(TimeUnit.MILLISECONDS);
		}

		/**
		 * 获取任务花费时间（单位：秒）
		 *
		 * @return 任务花费时间（单位：秒）
		 * @see #getTimeMillis()
		 * @see #getTimeNanos()
		 */
		public double getTimeSeconds() {
			return DateUtil.nanosToSeconds(this.timeNanos);
		}
	}
}
