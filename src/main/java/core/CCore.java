package core;
import java.io.File;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.*;
import org.anyline.entity.DataRow;
import org.noear.solon.Utils;

public class CCore{
	public static String get(LinkedHashMap<String, Object> param,String key) {
		Object id = param.get(key);
		if (id != null) {
			return String.valueOf(id);
		}
		return "";
	}

	public static String sh(String command) {
		return RuntimeUtil.execForStr("sh","-c",command);
	}
	private static final String LOG_DIR = "/mnt/project/";
	//file 是 vue3  unia   java
	//最终结果
	//   /mnt/project/vue3/weiminbanshi/out.txt  前版日志
	//   /mnt/project/java/weiminbanshi/out.txt  后版日志
	public static void sh(String command,String app , String lang) {
		// 创建日志文件名
		String logFileName = String.format("%s/%s/%s/out.txt",
				LOG_DIR,
				lang,
				app
		);
		try {

			// 确保日志目录存在
			if(!FileUtil.exist(LOG_DIR)){
				FileUtil.mkdir(LOG_DIR);
			}
			FileUtil.touch(logFileName); //不存在就创建
			controlLogSize(logFileName); //自动清理超过1M的日志
			// 记录命令开始执行
			String header = String.format("""
                === Command Execution Log ===
                Command: %s
                Start Time: %s
                
                """, command, DateUtil.now());

			FileUtil.appendString(header, logFileName, "UTF-8");

			// 执行命令并获取结果
			String result = RuntimeUtil.execForStr("sh","-c",command);
			CCore.log("result:==============="+result);
			FileUtil.appendString("result:==============="+result, logFileName,"UTF-8");
			// 记录命令输出
			FileUtil.appendString(result + "\n", logFileName, "UTF-8");

			// 记录命令结束
			String footer = String.format("""
                
                End Time: %s
                ==================
                """, DateUtil.now());

			FileUtil.appendString(footer, logFileName, "UTF-8");

		} catch (Exception e) {
			// 记录错误信息到日志文件
			FileUtil.appendString(
					"\nError: " + e.getMessage() + "\n",
					logFileName,
					"UTF-8"
			);
		}
	}
	private static final long MAX_LOG_SIZE = 1024 * 1024; // 1MB
	private static void controlLogSize(String logFile) {
		File file = new File(logFile);
		if (!file.exists()) return;

		if (file.length() > MAX_LOG_SIZE) {
			try {
				// 读取所有行
				List<String> lines = FileUtil.readLines(file, "UTF-8");

				// 计算需要删除的行数（删除约20%的内容）
				int linesToRemove = lines.size() / 5;

				// 保留后80%的内容
				List<String> newLines = lines.subList(linesToRemove, lines.size());

				// 写回文件
				FileUtil.writeLines(newLines, file, "UTF-8");

				// 添加删除记录
				String deleteNote = String.format("""
                    
                    === Log Truncated at %s ===
                    Removed %d old log entries to control file size
                    ==================
                    
                    """, DateUtil.now(), linesToRemove);
				FileUtil.appendString(deleteNote, file, "UTF-8");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String getLastModifiedTime(String filePath) {
		File file = FileUtil.file(filePath);
		if (FileUtil.exist(file)) {
			return toString(FileUtil.lastModifiedTime(file));
		}
		return "";
	}

	public static BigDecimal format2(String d){
		return NumberUtil.round(d,2);
	}
	public static BigDecimal format2(double d){
		return format2(toString(d));
	}
	public static String newGuid(){
		return IdUtil.simpleUUID().replaceAll("-","");
	}
	public static String getID(LinkedHashMap<String, Object> param) {
        String id= get(param, "id");
        if (StrUtil.isBlank(id))
        {
            id = get(param,"ID");
        }
        if (StrUtil.isBlank(id))
        {
            id = get(param, "removedIDs");
        }
        if (StrUtil.isBlank(id))
        {
            id = get(param, "RemovedIDs");
        }
        if (StrUtil.isBlank(id))
        {
            id = get(param, "IDs");
        }
        if (StrUtil.isBlank(id))
        {
            id = get(param, "ids");
        }
        if (StrUtil.isBlank(id))
        {
            id = get(param, "iDs");
        }
        if (StrUtil.isBlank(id))
        {
            id = get(param, "iD");
        }
        return id;
    }


	public static List<String> getStrArray(String str) {
		List<String> list = new ArrayList<String>(100);
		try {
			String[] arr = str.split(",");
			list = CollUtil.newArrayList(arr);
		} catch (Exception e) {

		}
		return list;
	}

	public static List<String> getArrayToList(String[] array) {
		List<String> list = new ArrayList<>();
		try {
			list = Arrays.asList(array);
		} catch (Exception e) {

		}
		return list;
	}

	public static boolean toBool(Object obj){
		return BooleanUtil.toBoolean(CCore.toString(obj));
	}

	public static int toInt(Object o){
		try{
			if(o ==null){
				return -1;
			}
			if (NumberUtil.isInteger(o.toString()))
			{
				return Integer.parseInt(o.toString());
			}
		}catch (Exception err){
		}
		return -1;
	}
	public static boolean isAlphaNumeric(String str) {
		return str.matches("[a-zA-Z0-9]+");
	}
	
	public static int toInt(Object o,int defaultValue){
		try{
			if(o==null){
				return defaultValue;
			}
			if (NumberUtil.isInteger(o.toString())){
				return Integer.parseInt(o.toString());
			}
		}catch (Exception err){
		}
		return defaultValue;
	}

	public static String toString(Object obj){
		try{
			if(obj == null || "".equals(obj)){
				return "";
			}

			String str = obj.toString().trim();
			if("".equals(str)){
				return "";
			}
			str = str.replace("T00:00:00.000+0000","");
			try{
				if(isValidDate(str)){
					FastDateFormat format = DatePattern.NORM_DATETIME_FORMAT;
					if( !str.contains(":")){
						format=DatePattern.NORM_DATE_FORMAT;
					}
					str = DateUtil.format(DateUtil.parse(str),format);
					str = str.replace(" 00:00:00","");
				}

			}catch(Exception err){

			}
			return str;
		}
		catch(Exception e){
			return "";
		}
	}

	public static boolean isEmpty(Object obj) {
	  if (obj == null) {
	   return true;
	  }
	  if(obj instanceof DataRow){
		  DataRow d = (DataRow)obj;
		  return isEmpty(d.getId());
	  }
	  // 判断obj是否是Optional的子类
	  if (obj instanceof Optional) {
	   // 如果是，则调用isPresent方法判断是否为null
	   return !((Optional) obj).isPresent();
	  }
	  // 判断obj是否是CharSequence的子类
	  if (obj instanceof CharSequence) {
	   // 如果是，则获取长度，长度等于0时，就认为这个obj是空字符串
	   return ((CharSequence) obj).length() == 0;
	  }
	  // 判断obj是否为数组
	  if (obj.getClass().isArray()) {
	   // 数组的长度等于0就认为这个数组是空数组
	   return Array.getLength(obj) == 0;
	  }
	  // 判断obj是否为Collection集合的子类
	  if (obj instanceof Collection) {
	   // 用Collection子类的isEmpty方法判断集合是否为空
	   return ((Collection) obj).isEmpty();
	  }
	  // 判断obj是否为Map接口的子类
	  if (obj instanceof Map) {
	   // 如果是，则进行强转，并用子类的isEmpty方法判断集合是否为空
	   return ((Map) obj).isEmpty();
	  }
	  // else
	  return false;
	}
	public static String toString(Object obj,FastDateFormat format){
		try{
			if(obj == null || "".equals(obj)){
				return "";
			}
			String str = obj.toString().trim();
			if("".equals(str)){
				return "";
			}
			try{
				Date d = DateUtil.parse(str);
				str = DateUtil.format(d,format);
				str = str.replace(" 00:00:00","");
			}catch(Exception err){

			}
			return str;
		}
		catch(Exception e){
			return "";
		}
	}

	public static long toLong(Object obj){
		long num = 0;
		if(obj==null){
			return num;
		}
		try
		{
			num = Long.parseLong(obj.toString());
		}
		catch(Exception err){

		}
		return num;
	}
	public static float toFloat(Object obj)
	{
		float num = 0;
		if(obj==null){
			return  num;
		}
		try
		{
			num = Float.parseFloat(obj.toString());
		}
		catch(Exception err){}
		return num;
	}

	public static double toDouble(Object obj)
	{
		double num = 0.0;
		if(obj == null){
			return  num;
		}
		try
		{
			num = Double.parseDouble(obj.toString());;
		}
		catch(Exception err){}
		return num;
	}
	public static Date toDate(Object str){

		if(CCore.isEmpty(str)){
			return new Date();
		}
		if(str instanceof Timestamp timestamp) {
			return org.anyline.util.DateUtil.parse(timestamp);
		}
		String o = CCore.toString(str);
		try	{
			return org.anyline.util.DateUtil.parse(o);
		}
		catch(Exception err){
			return  new Date();
		}
	}

	public static void log(Object msg) {
		Console.log(msg);
	}
	public static <T> T invoke(Object obj, String methodName, Object... args) {
		return ReflectUtil.invoke(obj,methodName,args);
	}
	public static DataRow fixNull(DataRow dr){
		if(dr == null){
			dr = new DataRow();
		}
		return dr;
	}


	public static String[] getListStr(List<String> list){
		//String[] arr = list.toArray(new String[list.size()]);
		String[] strs = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			strs[i] = list.get(i);
		}
		return strs;

	}

	// 判断是否符合多种日期格式
	public static boolean isValidDate(Object dateStr) {
		if(dateStr==null){
			return false;
		}
		// 定义多个日期格式
		String[] datePatterns = {
				"yyyy-MM-dd HH:mm:ss.SSS",
				"yyyy-MM-dd HH:mm:ss",
				"yyyy/MM/dd HH:mm:ss",
				"yyyy.MM.dd HH:mm:ss",
				"yyyy-MM-dd",
				"yyyy/MM/dd",
				"yyyy.MM.dd",
				"yyyy-MM",
				"yyyy/MM",
				"yyyy.MM",
				"EEE MMM dd HH:mm:ss zzz yyyy"
		};

		// 遍历所有格式尝试解析
		for (String pattern : datePatterns) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			if("EEE MMM dd HH:mm:ss zzz yyyy".equals(pattern)){
				dateFormat = new SimpleDateFormat(pattern,Locale.US);
			}
			dateFormat.setLenient(false);  // 严格解析，不允许非法日期
			try {
				Date date = dateFormat.parse(dateStr.toString());  // 尝试解析日期
				return true;  // 如果成功解析，返回 true
			} catch (ParseException e) {
				// 如果该格式无法解析，继续尝试其他格式
			}
		}

		return false;  // 如果所有格式都无法解析，返回 false
	}

	public void unzip(String zip, String to){
		try{
			ZipUtil.unzip(zip, to, CharsetUtil.CHARSET_GBK);
		}
		catch (Exception e1){
			try{
				ZipUtil.unzip(zip, to, CharsetUtil.CHARSET_UTF_8);
			}catch (Exception e2){
				try{
					ZipUtil.unzip(zip, to, CharsetUtil.CHARSET_ISO_8859_1);
				}catch (Exception e3){

				}
			}

		}
	}


}
