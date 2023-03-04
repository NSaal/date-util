package datetool.io;

/**
 * 文件工具类
 *
 * @author looly
 */
public class FileUtil  {

	// -------------------------------------------------------------------------------------------- out start

	/**
	 * 获取当前系统的换行分隔符
	 *
	 * <pre>
	 * Windows: \r\n
	 * Mac: \r
	 * Linux: \n
	 * </pre>
	 *
	 * @return 换行符
	 * @since 4.0.5
	 */
	public static String getLineSeparator() {
		return System.lineSeparator();
		// return System.getProperty("line.separator");
	}

	// -------------------------------------------------------------------------------------------- out end

}
