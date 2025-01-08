package core;
import cn.hutool.core.util.StrUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
/**
 * 字符串类型的处理工具类
 */
public class CStr {

    public static String urlEncode(String v){
    	String str ="";
    	try {
			 str = URLEncoder.encode(v,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return str;
    }
    public static int getWordCount(String s) {
        if(CCore.isEmpty(s)){
            return 0;
        }
        int length = 0;
        for(int i = 0; i < s.length(); i++) {
            int ascii = Character.codePointAt(s, i);
            if(ascii >= 0 && ascii <=255)
                length++;
            else length += 2;
        }
        return length;
    }
    /**
     * 将字符串截取成单个字符的list
     * @param str
     * @return
     */
    public static List<String> split(String str) {
    	List<String> list = new ArrayList<String>();
        try {
            if (CCore.isEmpty(str)) return list;
            char[] arr = str.toCharArray();
            for (char item : arr) {
                list.add(CCore.toString(item));
            }
        } catch(Exception e) {}
        return list;
    }

    /**
     * 根据传入的数字获取对应的中文数字
     * @param str
     * @return
     */
    public static String getChineseNumber(int str) {
        try {
            Map<Integer, String> values = new HashMap<Integer,String>();
            values.put(1, "一");
            values.put(2, "二");
            values.put(3, "三");
            values.put(4, "四");
            values.put(5, "五");
            values.put(6, "六");
            values.put(7, "七");
            values.put(8, "八");
            values.put(9, "九");
            values.put(10, "十");
            values.put(11, "十一");
            values.put(12, "十二");
            return values.get(str);
        } catch (Exception err) {
            return "";
        }
    }

    public static int substringCount(String content, String keyword) {
        if (content.contains(keyword)) {
            String strReplaced = content.replaceAll(keyword, "");
            return (content.length() - strReplaced.length()) / keyword.length();
        }
        return 0;
    }

    /**
     * 根据传入的位置坐标截取字符串
     * @param str
     * @param startIndex 开始位置
     * @param endIndex 结束为止
     * @return
     */
    public static String subStr(String str, int startIndex, int endIndex) {
        if(CCore.isEmpty(str) || startIndex < 0 || endIndex > str.length()) return str;
        try {
            str = str.trim();
            return str.substring(startIndex, endIndex);
        } catch(Exception e) {
            return str;
        }
    }

    /**
     * 首字母小写
     * @param str
     * @return
     */
    public static String firstCharToLower(String str) {
        if (!CCore.isEmpty(str)) { str = str.substring(0, 1).toLowerCase() + str.substring(1);}
        return str;
    }
    /**
     * 首字母大写
     * @param str
     * @return
     */
    public static String firstCharToUpper(String str) {
        if (!CCore.isEmpty(str)) { str = str.substring(0, 1).toUpperCase() + str.substring(1);}
        return str;
    }

    /**
     * 判断一个字符串是否包含另一个字符串
     * @param parent
     * @param children
     * @return
     */
    public static boolean contains(String parent, String children) {
        try {
            parent = "," + CCore.toString(parent) + ",";
            children = "," + CCore.toString(children) + ",";
            return parent.contains(children);
        } catch(Exception e) {
            return false;
        }
    }

    /**
     * 判断一个字符串是否包含另一个字符串(忽略大写小)
     * @param parent
     * @param children
     * @return
     */
    public static boolean containsIgnoreCase(String parent, String children) {
        if(parent == null || children == null) return false;
        if (StrUtil.containsIgnoreCase(parent, children)) { return true;}
        return false;
    }

    //Abcdefg
    //ABCD
    //可以得到 Abcd
    public static String substringIgnoreCase(String all, String part)
    {
        if (CCore.isEmpty(part) ||CCore.isEmpty(all))
        {
            return "";
        }
        int i = all.toLowerCase().indexOf(part.toLowerCase());
        //CultureInfo.InvariantCulture.CompareInfo.IndexOf(all, part, CompareOptions.IgnoreCase)
        return all.substring(i, part.length());
    }

    /**
     * 判断是否全是大写
     * @param str
     * @return
     */
    public static boolean isAllUpper(String str) {
        if (CCore.isEmpty(str)) { return false; }
        if (str == str.toUpperCase()) { return true; }
        return false;
    }

    /**
     * 判断是否全是小写
     * @param str
     * @return
     */
    public static boolean isAllLower(String str) {
        if (CCore.isEmpty(str)) { return false; }
        if (str == str.toLowerCase()) { return true; }
        return false;
    }

    /**
     * 从字符串中删除一个字符串
     * @param parent
     * @param children
     * @return
     */
    public static String replace(String parent, String children) {
        if (CCore.isEmpty(parent) || CCore.isEmpty(children)) { return parent; }
        parent = parent.replace(children, "");
        return parent;
    }

    public static String remove(String str,String obj) {
        List<String> ss = getStrArray(str);
        ss.remove(obj);
        return getArrayStr(ss);
    }

    public static List<String> findDuplicates(List<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }

        // 使用LinkedHashMap保持顺序
        Map<String, Integer> countMap = new LinkedHashMap<>();

        // 统计每个元素出现的次数
        for (String key : keys) {
            countMap.put(key, countMap.getOrDefault(key, 0) + 1);
        }

        // 收集重复的元素
        return countMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 把字符串转按照, 分割成list
     * @param str
     * @return
     */
    public static List<String> getStrArray(String str) {
        List<String> strs = new ArrayList<String>();
        if (CCore.isEmpty(str)) { return strs; }
        str = str.replaceAll("，",",");
        String[] ret = str.split(",");
        for (String re : ret) {
            if (re == null) { strs.add("");}
            else { strs.add(re.trim());}
        }
        return strs;
    }

    public static List<String> getStrArray(Object str) {
    	String strs = CCore.toString(str);
        return CStr.getStrArray(strs);
    }

    public static List<String> getStrArrayBySplit(Object str, String split) {
    	String strs = CCore.toString(str);
        return getStrArrayBySplit(strs, split);
    }

    /**
     * 根据传入的分割符号分割字符串
     * @param str
     * @param split
     * @return
     */
    public static List<String> getStrArrayBySplit(String str, String split) {
        List<String> strs = new ArrayList<String>();
        if (CCore.isEmpty(str)) { return strs;}
        String[] ret = str.split(split);
        for (String re : ret) { strs.add(CCore.toString(re).trim());}
        return strs;
    }
    public static String getArrayStr(List<String> strs) { return getArrayStr(strs, ","); }
    public static String getArrayStr(List<String> strs, String split) {
    	String s = "";
        for(String str : strs) { s += str + split; }
        return delLastComma(s);
    }

    public static List<String> dropDuplicate(List<String> strs)
    {
        List<String> ret = new ArrayList<String>();
        if (strs != null && strs.size() > 0)
        {
            for(String str : strs)
            {
                if (!ret.contains(str))
                    ret.add(str);
            }
        }
        return ret;
    }
    public static String dropDuplicate(String strs)
    {

        if (CCore.isEmpty(strs))
        {
            return "";
        }
        if(strs.startsWith(",")){
            strs = strs.substring(1);
        }
        if(strs.endsWith(",")){
            strs = strs.substring(0,strs.length()-1);
        }
        List<String> s = CStr.getStrArray(strs);
        s = dropDuplicate(s);
        return getArrayStr(s);
    }

    /**
     *  删除最后结尾的一个逗号
     */
    public static String delLastComma(String str) {
        if (!CCore.isEmpty(str) && str.endsWith(",")) {
            str=str.substring(0, str.lastIndexOf(","));
        }
        if (!CCore.isEmpty(str) && str.startsWith(",")) {
            str=str.substring(1, str.length());
        }
        return str;
    }
    public static String delLastAnd(String str) {
        if (!CCore.isEmpty(str) && (str.endsWith("and ") || str.endsWith("and"))) {
            return str.substring(0, str.lastIndexOf("and"));
        }
        return str;
    }
    public static String delLastOr(String str) {
        if (!CCore.isEmpty(str) && (str.endsWith("or ") || str.endsWith("or"))) {
            return str.substring(0, str.lastIndexOf("or"));
        }
        return str;
    }

    public static String delLast(String str, int lastLen)
    {
        if (!CCore.isEmpty(str) && str.length() >= lastLen)
        {
            return str.substring(0,str.length()-lastLen);
        }

        return str;
    }

    /**
     * 获取字符串中中文的个数
     * @param str
     */
    public static int getChinaCount(String str) {
        if(str.length() == 0) return 0;
        return str.length()- str.replaceAll("[\u4e00-\u9fa5]", "").length();
    }

    /**
     * 判断字符串首字母是否是大写
     * @param str
     */
    public static boolean isUpperWithFirst(String str) {
        if(CCore.isEmpty(str) || str.length()==0) return false;
        String first = str.substring(0, 1);
        if(first.toCharArray()[0] >= 'A' && first.toCharArray()[0] <= 'Z') return true;
        else return false;
    }




    /**
     * 判断字符串首字母是否是大写
     * @param str
     */
    public static boolean isLowerWithFirst(String str) {
        if(str.length() == 0) return false;
        String first = str.substring(0, 1);
        if(first.toCharArray()[0] >= 'a' && first.toCharArray()[0] <= 'z') return true;
        else return false;
    }

    public static List<String> getWords(String str) {
        str = str.replaceAll(" ", "");
        List<String> list = new ArrayList<String>();

        int iter = 0;
        String temp = "";
        while(iter < str.length()) {
            iter++;
        }
        if(!CCore.isEmpty(temp)) {
            list.add(temp);
        }
        return list;
    }

    public static String replaceToBr(String oldString){
        Pattern pattern=Pattern.compile("(\r\n|\r|\n|\n\r)");//正则表达式的匹配一定要是这样，单个替换\r|\n的时候会错误
        Matcher matcher=pattern.matcher(oldString);
        String newString=matcher.replaceAll("<br>");
        return newString;
    }

 // 参数类型是Map<String,String> 因为支付只能用string的参数。如果诸君还需要修改的话，那也可以适当的做调整
    /**
    *
    * map转str
    * @param map
    * @return
    */
    public static String getMapToString(Map<String, String> map) {
        Set<String> keySet = map.keySet();
        // 将set集合转换为数组
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        // 给数组排序(升序)
        Arrays.sort(keyArray);
        // 因为String拼接效率会很低的，所以转用StringBuilder。博主会在这篇博文发后不久，会更新一篇String与StringBuilder开发时的抉择的博文。
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
            // 参数值为空，则不参与签名 这个方法trim()是去空格
            if (map.get(keyArray[i]).trim().length() > 0) {
        sb.append(keyArray[i]).append("=").append(map.get(keyArray[i]).trim());
            }
            if (i != keyArray.length - 1) {
                sb.append("&");
            }
        }
        return sb.toString();
    }
    /**
    * 2018年10月24日更新
    * String转map
    * @param str
    * @return
    */
    public static Map<String, String> getStringToMap(String str) {
        // 感谢bojueyou指出的问题
        // 判断str是否有值
        if (null == str || "".equals(str)) {
            return null;
        }
        // 根据&截取
        String[] strings = str.split("&");
        // 设置HashMap长度
        int mapLength = strings.length;
        // 判断hashMap的长度是否是2的幂。
        if ((strings.length % 2) != 0) {
            mapLength = mapLength + 1;
        }
        Map<String, String> map = new HashMap<>(mapLength);
        // 循环加入map集合
        for (int i = 0; i < strings.length; i++) {
            // 截取一组字符串
            String[] strArray = strings[i].split("=");
            // strArray[0]为KEY strArray[1]为值
            map.put(strArray[0], strArray[1]);
        }
        return map;
    }
}

