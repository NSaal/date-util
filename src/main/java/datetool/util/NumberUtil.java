package datetool.util;

import datetool.lang.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * 数字工具类<br>
 * 对于精确值计算应该使用 {@link BigDecimal}<br>
 * JDK7中<strong>BigDecimal(double val)</strong>构造方法的结果有一定的不可预知性，例如：
 *
 * <pre>
 * new BigDecimal(0.1)
 * </pre>
 * <p>
 * 表示的不是<strong>0.1</strong>而是<strong>0.1000000000000000055511151231257827021181583404541015625</strong>
 *
 * <p>
 * 这是因为0.1无法准确的表示为double。因此应该使用<strong>new BigDecimal(String)</strong>。
 * </p>
 * 相关介绍：
 * <ul>
 * <li>http://www.oschina.net/code/snippet_563112_25237</li>
 * <li>https://github.com/venusdrogon/feilong-core/wiki/one-jdk7-bug-thinking</li>
 * </ul>
 *
 * @author Looly
 */
public class NumberUtil {

	/**
	 * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
	 *
	 * @param v1    被除数
	 * @param v2    除数
	 * @param scale 精确度，如果为负值，取绝对值
	 * @return 两个参数的商
	 */
	public static double div(float v1, float v2, int scale) {
		return div(v1, v2, scale, RoundingMode.HALF_UP);
	}

	/**
	 * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
	 *
	 * @param v1           被除数
	 * @param v2           除数
	 * @param scale        精确度，如果为负值，取绝对值
	 * @param roundingMode 保留小数的模式 {@link RoundingMode}
	 * @return 两个参数的商
	 */
	public static double div(float v1, float v2, int scale, RoundingMode roundingMode) {
		return div(Float.toString(v1), Float.toString(v2), scale, roundingMode).doubleValue();
	}

	/**
	 * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
	 *
	 * @param v1           被除数
	 * @param v2           除数
	 * @param scale        精确度，如果为负值，取绝对值
	 * @param roundingMode 保留小数的模式 {@link RoundingMode}
	 * @return 两个参数的商
	 */
	public static BigDecimal div(String v1, String v2, int scale, RoundingMode roundingMode) {
		return div(toBigDecimal(v1), toBigDecimal(v2), scale, roundingMode);
	}

	/**
	 * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
	 *
	 * @param v1           被除数
	 * @param v2           除数
	 * @param scale        精确度，如果为负值，取绝对值
	 * @param roundingMode 保留小数的模式 {@link RoundingMode}
	 * @return 两个参数的商
	 * @since 3.0.9
	 */
	public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale, RoundingMode roundingMode) {
		Assert.notNull(v2, "Divisor must be not null !");
		if (null == v1) {
			return BigDecimal.ZERO;
		}
		if (scale < 0) {
			scale = -scale;
		}
		return v1.divide(v2, scale, roundingMode);
	}

	// ------------------------------------------------------------------------------------------- isXXX

	/**
	 * 是否为数字，支持包括：
	 *
	 * <pre>
	 * 1、10进制
	 * 2、16进制数字（0x开头）
	 * 3、科学计数法形式（1234E3）
	 * 4、类型标识形式（123D）
	 * 5、正负数标识形式（+123、-234）
	 * </pre>
	 *
	 * @param str 字符串值
	 * @return 是否为数字
	 */
	public static boolean isNumber(CharSequence str) {
		if (StrUtil.isBlank(str)) {
			return false;
		}
		char[] chars = str.toString().toCharArray();
		int sz = chars.length;
		boolean hasExp = false;
		boolean hasDecPoint = false;
		boolean allowSigns = false;
		boolean foundDigit = false;
		// deal with any possible sign up front
		int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
		if (sz > start + 1) {
			if (chars[start] == '0' && (chars[start + 1] == 'x' || chars[start + 1] == 'X')) {
				int i = start + 2;
				if (i == sz) {
					return false; // str == "0x"
				}
				// checking hex (it can't be anything else)
				for (; i < chars.length; i++) {
					if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f') && (chars[i] < 'A' || chars[i] > 'F')) {
						return false;
					}
				}
				return true;
			}
		}
		sz--; // don't want to loop to the last char, check it afterwords
		// for type qualifiers
		int i = start;
		// loop to the next to last char or to the last char if we need another digit to
		// make a valid number (e.g. chars[0..5] = "1234E")
		while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				foundDigit = true;
				allowSigns = false;

			} else if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent
					return false;
				}
				hasDecPoint = true;
			} else if (chars[i] == 'e' || chars[i] == 'E') {
				// we've already taken care of hex.
				if (hasExp) {
					// two E's
					return false;
				}
				if (false == foundDigit) {
					return false;
				}
				hasExp = true;
				allowSigns = true;
			} else if (chars[i] == '+' || chars[i] == '-') {
				if (!allowSigns) {
					return false;
				}
				allowSigns = false;
				foundDigit = false; // we need a digit after the E
			} else {
				return false;
			}
			i++;
		}
		if (i < chars.length) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				// no type qualifier, OK
				return true;
			}
			if (chars[i] == 'e' || chars[i] == 'E') {
				// can't have an E at the last byte
				return false;
			}
			if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent
					return false;
				}
				// single trailing decimal point after non-exponent is ok
				return foundDigit;
			}
			if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
				return foundDigit;
			}
			if (chars[i] == 'l' || chars[i] == 'L') {
				// not allowing L with an exponent
				return foundDigit && !hasExp;
			}
			// last character is illegal
			return false;
		}
		// allowSigns is true iff the val ends in 'E'
		// found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
		return false == allowSigns && foundDigit;
	}

	// ------------------------------------------------------------------------------------------- compare

	/**
	 * 比较大小，值相等 返回true<br>
	 * 此方法通过调用{@link Double#doubleToLongBits(double)}方法来判断是否相等<br>
	 * 此方法判断值相等时忽略精度的，即0.00 == 0
	 *
	 * @param num1 数字1
	 * @param num2 数字2
	 * @return 是否相等
	 * @since 5.4.2
	 */
	public static boolean equals(double num1, double num2) {
		return Double.doubleToLongBits(num1) == Double.doubleToLongBits(num2);
	}

	/**
	 * 比较大小，值相等 返回true<br>
	 * 此方法通过调用{@link Float#floatToIntBits(float)}方法来判断是否相等<br>
	 * 此方法判断值相等时忽略精度的，即0.00 == 0
	 *
	 * @param num1 数字1
	 * @param num2 数字2
	 * @return 是否相等
	 * @since 5.4.5
	 */
	public static boolean equals(float num1, float num2) {
		return Float.floatToIntBits(num1) == Float.floatToIntBits(num2);
	}

	/**
	 * 比较大小，值相等 返回true<br>
	 * 此方法通过调用{@link BigDecimal#compareTo(BigDecimal)}方法来判断是否相等<br>
	 * 此方法判断值相等时忽略精度的，即0.00 == 0
	 *
	 * @param bigNum1 数字1
	 * @param bigNum2 数字2
	 * @return 是否相等
	 */
	public static boolean equals(BigDecimal bigNum1, BigDecimal bigNum2) {
		//noinspection NumberEquality
		if (bigNum1 == bigNum2) {
			// 如果用户传入同一对象，省略compareTo以提高性能。
			return true;
		}
		if (bigNum1 == null || bigNum2 == null) {
			return false;
		}
		return 0 == bigNum1.compareTo(bigNum2);
	}

	/**
	 * 数字转{@link BigDecimal}<br>
	 * null或""或空白符转换为0
	 *
	 * @param numberStr 数字字符串
	 * @return {@link BigDecimal}
	 * @since 4.0.9
	 */
	public static BigDecimal toBigDecimal(String numberStr) {
		if (StrUtil.isBlank(numberStr)) {
			return BigDecimal.ZERO;
		}

		try {
			// 支持类似于 1,234.55 格式的数字
			final Number number = parseNumber(numberStr);
			if (number instanceof BigDecimal) {
				return (BigDecimal) number;
			} else {
				return new BigDecimal(number.toString());
			}
		} catch (Exception ignore) {
			// 忽略解析错误
		}

		return new BigDecimal(numberStr);
	}


	/**
	 * 解析转换数字字符串为int型数字，规则如下：
	 *
	 * <pre>
	 * 1、0x开头的视为16进制数字
	 * 2、0开头的忽略开头的0
	 * 3、其它情况按照10进制转换
	 * 4、空串返回0
	 * 5、.123形式返回0（按照小于0的小数对待）
	 * 6、123.56截取小数点之前的数字，忽略小数部分
	 * </pre>
	 *
	 * @param number 数字，支持0x开头、0开头和普通十进制
	 * @return int
	 * @throws NumberFormatException 数字格式异常
	 * @since 4.1.4
	 */
	public static int parseInt(String number) throws NumberFormatException {
		if (StrUtil.isBlank(number)) {
			return 0;
		}

		if(StrUtil.containsIgnoreCase(number, "E")){
			// 科学计数法忽略支持，科学计数法一般用于表示非常小和非常大的数字，这类数字转换为int后精度丢失，没有意义。
			throw new NumberFormatException(StrUtil.format("Unsupported int format: [{}]", number));
		}

		if (StrUtil.startWithIgnoreCase(number, "0x")) {
			// 0x04表示16进制数
			return Integer.parseInt(number.substring(2), 16);
		}

		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException e) {
			return parseNumber(number).intValue();
		}
	}

	/**
	 * 将指定字符串转换为{@link Number} 对象<br>
	 * 此方法不支持科学计数法
	 *
	 * @param numberStr Number字符串
	 * @return Number对象
	 * @throws NumberFormatException 包装了{@link ParseException}，当给定的数字字符串无法解析时抛出
	 * @since 4.1.15
	 */
	public static Number parseNumber(String numberStr) throws NumberFormatException {
		if (StrUtil.startWithIgnoreCase(numberStr, "0x")) {
			// 0x04表示16进制数
			return Long.parseLong(numberStr.substring(2), 16);
		}

		try {
			final NumberFormat format = NumberFormat.getInstance();
			if (format instanceof DecimalFormat) {
				// issue#1818@Github
				// 当字符串数字超出double的长度时，会导致截断，此处使用BigDecimal接收
				((DecimalFormat) format).setParseBigDecimal(true);
			}
			return format.parse(numberStr);
		} catch (ParseException e) {
			final NumberFormatException nfe = new NumberFormatException(e.getMessage());
			nfe.initCause(e);
			throw nfe;
		}
	}

}
