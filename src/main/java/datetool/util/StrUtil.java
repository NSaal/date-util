package datetool.util;

import datetool.text.CharSequenceUtil;
import datetool.text.StrPool;

/**
 * 字符串工具类
 *
 * @author xiaoleilu
 */
public class StrUtil extends CharSequenceUtil implements StrPool {

	// ------------------------------------------------------------------------ Trim

	/**
	 * 创建StringBuilder对象
	 *
	 * @return StringBuilder对象
	 */
	public static StringBuilder builder() {
		return new StringBuilder();
	}

}
