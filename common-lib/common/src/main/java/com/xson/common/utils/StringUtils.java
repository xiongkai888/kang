package com.xson.common.utils;

import android.os.Build;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String工具类
 * Created by tianyi on 2014/12/25.
 */
public class StringUtils {
    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }
    /**
     * 判断一个对象是否为空；
     */
    public final static boolean isEmpty(Object o) {
        return (o == null);
    }

    public final static boolean isEmpty(String[] array) {
        if (array == null || array.length == 0)
            return true;
        else
            return false;
    }

    public final static boolean isEmpty(int[] array) {
        if (array == null || array.length == 0)
            return true;
        else
            return false;
    }

    public final static boolean isEmpty(StringBuffer sb) {
        if (sb == null || sb.length() == 0)
            return true;
        else
            return false;
    }

    public final static boolean isEmpty(List list) {
        if (list == null || list.size() == 0)
            return true;
        else
            return false;
    }

    public final static boolean isEmpty(Set set) {
        if (set == null || set.size() == 0)
            return true;
        else
            return false;
    }

    public final static boolean isEmpty(Map map) {
        if (map == null || map.size() == 0)
            return true;
        else
            return false;
    }

    /**
     * 检测两个字符串是否相同；
     */
    public final static boolean isSame(String value1, String value2) {
        if (isEmpty(value1) && isEmpty(value2)) {
            return true;
        } else if (!isEmpty(value1) && !isEmpty(value2)) {
            return (value1.trim().equalsIgnoreCase(value2.trim()));
        } else {
            return false;
        }
    }
    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    public static float toFloat(Object obj) {
        if (obj == null)
            return 0f;
        return toFloat(obj.toString(), 0f);
    }

    public static float toFloat(String str, float defValue) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    public static double toDouble(Object obj) {
        if (obj == null)
            return 0f;
        return toDouble(obj.toString(), 0f);
    }

    public static double toDouble(String str, float defValue) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
        }
        return defValue;
    }


    /**
     * 检测变量的值是否为一个整型数据；
     */
    public final static boolean isInt(String value) {
        if (isEmpty(value))
            return false;

        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
    /**
     * 判断变量的值是否为double类型
     */
    public final static boolean isDouble(String value) {
        if (isEmpty(value))
            return false;
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * num为实数，n为要保留的小数位数。
     * @param num
     * @param n
     * @return
     */
    public static double round(double num,int n){
        return Math.round(num*Math.pow(10,n))/Math.pow(10,n);
    }
    public static double round(int num,int n){
        return Math.round(1.0*num*Math.pow(10,n))/Math.pow(10,n);
    }
    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 格式化字符串显示；
     */
    public final static String format(String value) {
        return format(value, "");
    }

    public final static String format(String value, String defaultValue) {
        if (isEmpty(value))
            return defaultValue;
        else
            return value.trim();
    }

    /**
     * 判断指定数据是否存在于指定的数组中；
     */
    public final static boolean isContain(String[] array, String value) {
        if (isEmpty(array) || isEmpty(value))
            return false;

        int size = size(array);
        for (int i = 0; i < size; i++) {
            if (isSame(array[i], value))
                return true;
        }

        return false;
    }

    public final static boolean isContain(String content, String value) {
        if (isEmpty(content) || isEmpty(value))
            return false;

        return (content.indexOf(value) != -1);
    }

    public final static boolean isContain(List list, Object object) {
        if (isEmpty(list))
            return false;

        return list.contains(object);
    }

    /**
     * 获取指定集合的大小；
     */
    public final static int size(List list) {
        if (isEmpty(list))
            return 0;
        else
            return list.size();
    }

    public final static int size(Map map) {
        if (isEmpty(map))
            return 0;
        else
            return map.size();
    }

    public final static int size(String[] array) {
        if (isEmpty(array))
            return 0;
        else
            return array.length;
    }

    public final static int size(Object[] array) {
        if (isEmpty(array))
            return 0;
        else
            return array.length;
    }

    /**
     * 判定输入汉字
     * @param c
     * @return
     */
    public  final static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 根据UnicodeBlock方法判断中文标点符号
     * 中文的标点符号主要存在于以下5个UnicodeBlock中，

     U2000-General Punctuation (百分号，千分号，单引号，双引号等)

     U3000-CJK Symbols and Punctuation ( 顿号，句号，书名号，〸，〹，〺 等；PS: 后面三个字符你知道什么意思吗？ : )    )

     UFF00-Halfwidth and Fullwidth Forms ( 大于，小于，等于，括号，感叹号，加，减，冒号，分号等等)

     UFE30-CJK Compatibility Forms  (主要是给竖写方式使用的括号，以及间断线﹉，波浪线﹌等)

     UFE10-Vertical Forms (主要是一些竖着写的标点符号，    等等)
     * @param c
     * @return
     */
    public boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS) {
            return true;
        } else {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && ub == Character.UnicodeBlock.VERTICAL_FORMS;
        }
    }

    /**
     * 判定输入汉字
     * @param c
     * @return
     */
    public  final static boolean isChineseWithoutPunctuation(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A) {
            return true;
        }
        return false;
    }

    /**
     * Returns a string containing the tokens joined by delimiters.
     * @param tokens an array objects to be joined.
     */
    public static String join(CharSequence delimiter, int[] tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (int token: tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    public static String join(CharSequence delimiter, CharSequence[] tokens) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence token: tokens) {
            if (sb.length() > 0) {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    /**
     * 用空格拆分字符串
     * @param text
     * @param blockSize
     * @return
     */
    public static String divide(String text, int blockSize) {
        return divide(text, blockSize, "  ");
    }

    /**
     * 用一段字符来分隔另一段字符串
     * @param text
     * @param blockSize
     * @param splitChar
     * @return
     */
    public static String divide(String text, int blockSize, String splitChar) {
        StringBuilder sb = new StringBuilder();
        for(int size = text.length(), i = 0; i < size; i++) {
            if(i > 0 && i % blockSize == 0) {
                sb.append(splitChar);
            }
            sb.append(text.charAt(i));
        }
        return sb.toString();
    }

    public static String trimNull(String text) {
        return text == null ? "" : text;
    }
    /**
     * 判断手机格式是否正确
     *
     * @param mobile
     *            手机号码
     * @return 是/否
     */
    public static boolean isMobile(String mobile) {
        Pattern p_10086 = Pattern
                .compile("^((13[4-9])|(147)|(15[0-2,7-9])|(178)|(18[2-4,7-8]))\\d{8}|(1705)\\d{7}$");
        Pattern p_10010 = Pattern
                .compile("^((13[0-2])|(145)|(15[5-6])|(176)|(18[5,6]))\\d{8}|(1709)\\d{7}$");
        Pattern p_10001 = Pattern
                .compile("^((133)|(153)|(177)|(173)|(18[0,1,9]))\\d{8}$");
        Matcher m_10086 = p_10086.matcher(mobile);
        Matcher m_10010 = p_10010.matcher(mobile);
        Matcher m_10001 = p_10001.matcher(mobile);
        if (m_10086.matches() || m_10010.matches() || m_10001.matches()) {
            return true;
        }

        return false;
    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

}
