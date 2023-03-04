package datetool.util;

import datetool.text.CharPool;

/**
 * 字符工具类<br>
 * 部分工具来自于Apache Commons系列
 *
 * @author looly
 * @since 4.0.1
 */
public class CharUtil implements CharPool {

	/**
	 * 是否空白符<br>
	 * 空白符包括空格、制表符、全角空格和不间断空格<br>
	 *
	 * @param c 字符
	 * @return 是否空白符
	 * @see Character#isWhitespace(int)
	 * @see Character#isSpaceChar(int)
	 * @since 4.0.10
	 */
	public static boolean isBlankChar(char c) {
		return isBlankChar((int) c);
	}

	/**
	 * 是否空白符<br>
	 * 空白符包括空格、制表符、全角空格和不间断空格<br>
	 *
	 * @param c 字符
	 * @return 是否空白符
	 * @see Character#isWhitespace(int)
	 * @see Character#isSpaceChar(int)
	 * @since 4.0.10
	 */
	public static boolean isBlankChar(int c) {
		return Character.isWhitespace(c)
				|| Character.isSpaceChar(c)
				|| c == '\ufeff'
				|| c == '\u202a'
				|| c == '\u0000'
				// issue#I5UGSQ，Hangul Filler
				|| c == '\u3164'
				// Braille Pattern Blank
				|| c == '\u2800'
				// MONGOLIAN VOWEL SEPARATOR
				|| c == '\u180e';
	}

	/**
	 * 比较两个字符是否相同
	 *
	 * @param c1              字符1
	 * @param c2              字符2
	 * @param caseInsensitive 是否忽略大小写
	 * @return 是否相同
	 * @since 4.0.3
	 */
	public static boolean equals(char c1, char c2, boolean caseInsensitive) {
		if (caseInsensitive) {
			return Character.toLowerCase(c1) == Character.toLowerCase(c2);
		}
		return c1 == c2;
	}

}
