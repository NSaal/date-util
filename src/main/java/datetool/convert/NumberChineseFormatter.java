package datetool.convert;

import datetool.lang.Assert;
import datetool.text.CharSequenceUtil;

/**
 * 数字转中文类<br>
 * 包括：
 * <pre>
 * 1. 数字转中文大写形式，比如一百二十一
 * 2. 数字转金额用的大写形式，比如：壹佰贰拾壹
 * 3. 转金额形式，比如：壹佰贰拾壹整
 * </pre>
 *
 * @author fanqun, looly
 **/
public class NumberChineseFormatter {

	/**
	 * 中文形式，奇数位置是简体，偶数位置是记账繁体，0共用<br>
	 * 使用混合数组提高效率和数组复用
	 **/
	private static final char[] DIGITS = {'零', '一', '壹', '二', '贰', '三', '叁', '四', '肆', '五', '伍',
			'六', '陆', '七', '柒', '八', '捌', '九', '玖'};

	/**
	 * 汉字转阿拉伯数字的
	 */
	private static final ChineseUnit[] CHINESE_NAME_VALUE = {
			new ChineseUnit(' ', 1, false),
			new ChineseUnit('十', 10, false),
			new ChineseUnit('拾', 10, false),
			new ChineseUnit('百', 100, false),
			new ChineseUnit('佰', 100, false),
			new ChineseUnit('千', 1000, false),
			new ChineseUnit('仟', 1000, false),
			new ChineseUnit('万', 1_0000, true),
			new ChineseUnit('亿', 1_0000_0000, true),
	};

	/**
	 * 格式化-999~999之间的数字<br>
	 * 这个方法显示10~19以下的数字时使用"十一"而非"一十一"。
	 *
	 * @param amount           数字
	 * @param isUseTraditional 是否使用繁体
	 * @return 中文
	 * @since 5.7.17
	 */
	public static String formatThousand(int amount, boolean isUseTraditional) {
		Assert.checkBetween(amount, -999, 999, "Number support only: (-999 ~ 999)！");

		final String chinese = thousandToChinese(amount, isUseTraditional);
		if (amount < 20 && amount >= 10) {
			// "十一"而非"一十一"
			return chinese.substring(1);
		}
		return chinese;
	}

	/**
	 * 数字字符转中文，非数字字符原样返回
	 *
	 * @param c                数字字符
	 * @param isUseTraditional 是否繁体
	 * @return 中文字符
	 * @since 5.3.9
	 */
	public static String numberCharToChinese(char c, boolean isUseTraditional) {
		if (c < '0' || c > '9') {
			return String.valueOf(c);
		}
		return String.valueOf(numberToChinese(c - '0', isUseTraditional));
	}

	/**
	 * 把一个 0~9999 之间的整数转换为汉字的字符串，如果是 0 则返回 ""
	 *
	 * @param amountPart       数字部分
	 * @param isUseTraditional 是否使用繁体单位
	 * @return 转换后的汉字
	 */
	private static String thousandToChinese(int amountPart, boolean isUseTraditional) {
		if (amountPart == 0) {
			// issue#I4R92H@Gitee
			return String.valueOf(DIGITS[0]);
		}

		int temp = amountPart;

		StringBuilder chineseStr = new StringBuilder();
		boolean lastIsZero = true; // 在从低位往高位循环时，记录上一位数字是不是 0
		for (int i = 0; temp > 0; i++) {
			int digit = temp % 10;
			if (digit == 0) { // 取到的数字为 0
				if (!lastIsZero) {
					// 前一个数字不是 0，则在当前汉字串前加“零”字;
					chineseStr.insert(0, "零");
				}
				lastIsZero = true;
			} else { // 取到的数字不是 0
				chineseStr.insert(0, numberToChinese(digit, isUseTraditional) + getUnitName(i, isUseTraditional));
				lastIsZero = false;
			}
			temp = temp / 10;
		}
		return chineseStr.toString();
	}

	/**
	 * 单个数字转汉字
	 *
	 * @param number           数字
	 * @param isUseTraditional 是否使用繁体
	 * @return 汉字
	 */
	private static char numberToChinese(int number, boolean isUseTraditional) {
		if (0 == number) {
			return DIGITS[0];
		}
		return DIGITS[number * 2 - (isUseTraditional ? 0 : 1)];
	}

	/**
	 * 获取对应级别的单位
	 *
	 * @param index            级别，0表示各位，1表示十位，2表示百位，以此类推
	 * @param isUseTraditional 是否使用繁体
	 * @return 单位
	 */
	private static String getUnitName(int index, boolean isUseTraditional) {
		if (0 == index) {
			return CharSequenceUtil.EMPTY;
		}
		return String.valueOf(CHINESE_NAME_VALUE[index * 2 - (isUseTraditional ? 0 : 1)].name);
	}

	/**
	 * 权位
	 *
	 * @author totalo
	 * @since 5.6.0
	 */
	private static class ChineseUnit {
		/**
		 * 中文权名称
		 */
		private final char name;

		/**
		 * 构造
		 *
		 * @param name    名称
		 * @param value   值，即10的倍数
		 * @param secUnit 是否为节权位
		 */
		public ChineseUnit(char name, int value, boolean secUnit) {
			this.name = name;
			/**
			 * 10的倍数值
			 */
			/**
			 * 是否为节权位，它不是与之相邻的数字的倍数，而是整个小节的倍数。<br>
			 * 例如二十三万，万是节权位，与三无关，而和二十三关联
			 */
		}
	}

}
