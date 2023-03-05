package datetool.text;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@link CharSequence} 相关工具类封装
 *
 * @author looly
 * @since 5.5.3
 */
public class CharSequenceUtil {

    // ------------------------------------------------------------------------ sub

    /**
     * 切割指定位置之前部分的字符串
     *
     * @param string         字符串
     * @param toIndexExclude 切割到的位置（不包括）
     * @return 切割后的剩余的前半部分字符串
     */
    public static String subPre(CharSequence string, int toIndexExclude) {
        int fromIndexInclude = 0;
        int toIndexExclude1 = toIndexExclude;
        if (string == null || string.length() == 0) {
            return null == string ? null : string.toString();
        }
        int len = string.length();

        if (toIndexExclude1 < 0) {
            toIndexExclude1 = len + toIndexExclude1;
            if (toIndexExclude1 < 0) {
                toIndexExclude1 = len;
            }
        } else if (toIndexExclude1 > len) {
            toIndexExclude1 = len;
        }

        if (fromIndexInclude == toIndexExclude1) {
            return "";
        }

        return string.toString().substring(fromIndexInclude, toIndexExclude1);
    }

    // ------------------------------------------------------------------------ format

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is {} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示，如果模板为null，返回"null"
     * @param params   参数值
     * @return 格式化后的文本，如果模板为null，返回"null"
     */
    public static String format(CharSequence template, Object... params) {
        if (null == template) {
            return "null";
        }
        boolean isBlank = true;
        // 判断的时候，并将cs的长度赋给了strLen
        if (template.length() != 0) {// 遍历字符
            for (int i = 0; i < template.length(); i++) {
                if (!Character.isWhitespace(template.charAt(i))) {
                    isBlank = false;
                    break;
                }
            }
        }
        if (params == null || params.length == 0 || isBlank) {
            return template.toString();
        }
        String strPattern = template.toString();
        boolean isBlank2 = true;
        // 判断的时候，并将cs的长度赋给了strLen
        if (strPattern.length() != 0) {// 遍历字符
            for (int i = 0; i < strPattern.length(); i++) {
                if (!Character.isWhitespace(strPattern.charAt(i))) {
                    isBlank2 = false;
                    break;
                }
            }
        }
        if (isBlank2) {
            return strPattern;
        }
        final int strPatternLength = strPattern.length();
        final int placeHolderLength = "{}".length();
        List<String> strings = Arrays.stream(params).map(Objects::toString).collect(Collectors.toList());
        // 初始化定义好的长度以获得更好的性能
        final StringBuilder sbuf = new StringBuilder(strPatternLength + 50);

        int handledPosition = 0;// 记录已经处理到的位置
        int delimIndex;// 占位符所在位置
        for (int argIndex = 0; argIndex < strings.size(); argIndex++) {
            delimIndex = strPattern.indexOf("{}", handledPosition);
            if (delimIndex == -1) {// 剩余部分无占位符
                if (handledPosition == 0) { // 不带占位符的模板直接返回
                    return strPattern;
                }
                // 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
                sbuf.append(strPattern, handledPosition, strPatternLength);
                return sbuf.toString();
            }

            // 转义符
            if (delimIndex > 0 && strPattern.charAt(delimIndex - 1) == '\\') {// 转义符
                if (delimIndex > 1 && strPattern.charAt(delimIndex - 2) == '\\') {// 双转义符
                    // 转义符之前还有一个转义符，占位符依旧有效
                    sbuf.append(strPattern, handledPosition, delimIndex - 1);
                    sbuf.append(strings.get(argIndex));
                    handledPosition = delimIndex + placeHolderLength;
                } else {
                    // 占位符被转义
                    argIndex--;
                    sbuf.append(strPattern, handledPosition, delimIndex - 1);
                    sbuf.append('{');
                    handledPosition = delimIndex + 1;
                }
            } else {// 正常占位符
                sbuf.append(strPattern, handledPosition, delimIndex);
                sbuf.append(strings.get(argIndex));
                handledPosition = delimIndex + placeHolderLength;
            }
        }

        // 加入最后一个占位符后所有的字符
        sbuf.append(strPattern, handledPosition, strPatternLength);

        return sbuf.toString();
    }
}
