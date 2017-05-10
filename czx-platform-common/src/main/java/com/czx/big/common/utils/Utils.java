package com.czx.big.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

	private static final String DEFAULT_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
	private static final String RANDOM_TIMESTAMP_FORMAT = "yyyyMMddHHmmssS";
	private static final String DEFAULT_DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final DateFormat DATAFORMAT = new SimpleDateFormat(DEFAULT_TIMESTAMP_FORMAT);
	private static final DateFormat DEFAULT_DATEFORMAT = new SimpleDateFormat(DEFAULT_DATA_FORMAT);
	private static final DateFormat RANDOM_DATAFORMAT = new SimpleDateFormat(RANDOM_TIMESTAMP_FORMAT);
	private static Timestamp timestamp=new Timestamp(System.currentTimeMillis());
	private static int SEQUENCE = 0;  
	private static final int ROTATION = 99999;

	public static String AUTH_LICENSE = System.getProperty("user.dir")+"\\license";

	//将给定的正则表达式编译到模式中。
	private static Pattern p = Pattern.compile("\\$\\{([\\w\\d\\s]*)\\}");

	// \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),    
	// 字符串在编译时会被转码一次,所以是 "\\b"    
	// \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)    
	private static final String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"    
			+"|windows (phone|ce)|blackberry"    
			+"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"    
			+"|laystation portable)|nokia|fennec|htc[-_]"    
			+"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";    
	private static final String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"    
			+"|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";    

	//移动设备正则匹配：手机端、平板  
	static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);    
	static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);    

	/** 
	 * 检测是否是移动设备访问 
	 * @Title: check 
	 * @param userAgent 浏览器标识 
	 * @return true:移动设备接入，false:pc端接入 
	 */  
	public static boolean mobileCheck(String userAgent){    
		if(null == userAgent){    
			userAgent = "";    
		}    
		// 匹配    
		Matcher matcherPhone = phonePat.matcher(userAgent);    
		Matcher matcherTable = tablePat.matcher(userAgent);    
		if(matcherPhone.find() || matcherTable.find()){    
			return true;    
		} else {    
			return false;    
		}    
	} 

	/**
	 * @return 按照日期生成不重复的随机数
	 */
	public static synchronized String randomNID(){  
		if (SEQUENCE > ROTATION) SEQUENCE = 0;
		timestamp.setTime(System.currentTimeMillis());
		return RANDOM_DATAFORMAT.format(timestamp)+(SEQUENCE++);
	}

	/**
	 * 获取UUID
	 * @return
	 */
	public static String randomUUID(){
		UUID id = java.util.UUID.randomUUID();
		return id.toString().replace("-","");
	}

	/**
	 * 去除sql语句的order by
	 * @param sql
	 * @return
	 */
	public static String removeOrders(String sql) {
		Assert.hasText(sql);
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}


	/**
	 * 功能与JavaScript join一致
	 * @param exp
	 * @param args
	 * @return
	 */
	public static String join(String exp,Object... args){
		if(args.length == 1) return args[0].toString();
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<args.length;i++){
			if(i==(args.length-1))
				sb.append(args[i]);
			else
				sb.append(args[i]).append(exp);
		}
		return sb.toString();
	}
	/**
	 * 功能与JavaScript join一致
	 * @param exp
	 * @param args
	 * @return
	 */
	public static String joinStr(String exp,Object... args){
		if(args.length == 1) return args[0].toString();
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<args.length;i++){
			if(i==(args.length-1))
				sb.append("'").append(args[i]).append("'");
			else
				sb.append("'").append(args[i]).append("'").append(exp);
		}
		return sb.toString();
	}

	/**
	 * 功能与JavaScript join一致
	 * @param exp
	 * @param args
	 * @return
	 */
	public static String joinStr1(String exp,Object... args){
		if(args.length == 1) return "'"+args[0].toString()+"'";
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<args.length;i++){
			if(i==(args.length-1))
				sb.append("'").append(args[i]).append("'");
			else
				sb.append("'").append(args[i]).append("'").append(exp);
		}
		return sb.toString();
	}
	/**
	 * 功能与JavaScript join一致
	 * @param <T>
	 * @param exp
	 * @param args
	 * @return
	 */
	public static <T> String joinList(String exp, List<T> args){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<args.size();i++){
			T t = args.get(i);
			if (i == (args.size() - 1))
				sb.append(t);
			else
				sb.append(t).append(exp);
		}
		return sb.toString();
	}

	/**
	 * 获取客户端IP
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static  String getIpAddr(HttpServletRequest request) throws Exception {  
		String ip = request.getHeader("x-forwarded-for");  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("WL-Proxy-Client-IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("HTTP_CLIENT_IP");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
		}  
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
			ip = request.getRemoteAddr();  
		}  
		return ip;
	}

	/**
	 * 获取当前时间timestamp
	 * @return
	 */
	public static String getCurrentTimestamp(){
		timestamp.setTime(System.currentTimeMillis());
		return DATAFORMAT.format(timestamp);
	}
	/**
	 * String转timestamp
	 * @return
	 */
	public static Timestamp String2Timestamp(String tsStr){
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		ts = Timestamp.valueOf(tsStr);//将字符串转换成Timestamp格式
		return ts;
	}

	/**
	 * 删除文件
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	/**
	 * 附件下载
	 * @param request
	 * @param response
	 * @param path WebRoot相对路径
	 * @param fileName 返回的文件名称
	 * @param newFileName 服务器上的文件名称
	 */
	public static void downFile(HttpServletRequest request,HttpServletResponse response,String path,String oldFileName,String newFileName){
		try {
			oldFileName = new String(oldFileName.getBytes("ISO8859-1"), "UTF-8");//转换文件名，防止乱码
			String userAgent = request.getHeader("User-Agent");
	        byte[] bytes = userAgent.contains("MSIE")||(userAgent.indexOf("Gecko")>0 && userAgent.indexOf("rv:11")>0) ? oldFileName.getBytes() : oldFileName.getBytes("UTF-8"); // fileName.getBytes("UTF-8")处理safari的乱码问题
	        oldFileName = new String(bytes, "ISO-8859-1");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName="+oldFileName);
		path = path + "/" +newFileName;  
		InputStream fis= null;
		try {
			File file=new File(path);
			fis = new FileInputStream(file);
			OutputStream os=response.getOutputStream();
			byte[] cache=new byte[1024];
			int length;
			while((length=fis.read(cache))>0){
				os.write(cache,0,length);
			}
			os.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 附件下载
	 * @param request
	 * @param response
	 * @param path WebRoot相对路径
	 * @param fileName 返回的文件名称
	 * @param newFileName 服务器上的文件名称
	 */
	public static void zipDownFile(HttpServletRequest request,HttpServletResponse response,String path,String fileNames){
		try {
			fileNames = new String(fileNames.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
		String msg =df.format(new Date(System.currentTimeMillis()));
		String zipName=msg+".zip";//获取zip文件的名称
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", zipName));
		String[] fileNamesArr = fileNames.split(";");

		Map mapInputStreams=new HashMap();
		for (int i = 0; i < fileNamesArr.length; i++) {
			String[] newAndOldFileStr = fileNamesArr[i].split("#");
			File newFile=new File(path+newAndOldFileStr[0]);
			try {
				InputStream in= new FileInputStream(newFile);
				mapInputStreams.put(newAndOldFileStr[1], in);
			} catch (Exception e) {
				// TODO: handle exception
				System.out.print("系统找不到文件出现异常！");
			}
		}
		ZipUtil zip=new ZipUtil();
		try {
			zip.zip(mapInputStreams,response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * IP范围判定
	 * @param ipSection
	 * @param ip
	 * @return
	 */
	public static boolean ipIsValid(String ipSection, String ip) {  
		if (ipSection == null)  
			throw new NullPointerException("IP段不能为空！");  
		if (ip == null)  
			throw new NullPointerException("IP不能为空！");  
		ipSection = ipSection.trim();  
		ip = ip.trim();  
		final String REGX_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";  
		final String REGX_IPB = REGX_IP + "\\-" + REGX_IP;  
		if (!ipSection.matches(REGX_IPB) || !ip.matches(REGX_IP))  
			return false;  
		int idx = ipSection.indexOf('-');  
		String[] sips = ipSection.substring(0, idx).split("\\.");  
		String[] sipe = ipSection.substring(idx + 1).split("\\.");  
		String[] sipt = ip.split("\\.");  
		long ips = 0L, ipe = 0L, ipt = 0L;  
		for (int i = 0; i < 4; ++i) {  
			ips = ips << 8 | Integer.parseInt(sips[i]);  
			ipe = ipe << 8 | Integer.parseInt(sipe[i]);  
			ipt = ipt << 8 | Integer.parseInt(sipt[i]);  
		}  
		if (ips > ipe) {  
			long t = ips;  
			ips = ipe;  
			ipe = t;  
		}  
		return ips <= ipt && ipt <= ipe;  
	} 
	/**
	 *正则截取字符
	 *word   需要处理的 原始字符串
	 *regEx  正则表达式  表达式里面必须包括有()，因为返回的将是符合 () 中的表达式的内容
	 *return  返回空或者符合 () 中的表达式的内容
	 */
	public static String pattern(String word, String regEx)
	{
		if(word == null)
		{
			return "";
		}
		word = word.trim();
		if(word.isEmpty())
		{
			return "";
		}
		Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher mat = pat.matcher(word);
		if(mat.find())
		{
			return mat.group();
		}
		return "";
	}
	/**防SQL注入攻击结*/
	/**数字格式验证*/
	public static String shu(String word)
	{
		if(word == null){return "";}
		word = word.trim();
		if(word.isEmpty()){return "";}
		Pattern pn = Pattern.compile("^\\d+$");
		Matcher ma = pn.matcher(word);
		if(!ma.find()){return "";}
		if(!ma.matches()){return "";}
		return word;
	}
	/**
	 *日期格式验证
	 *word  需要验证的 原始字符串
	 *return  返回空 或者 格式化以后的字符串
	 */
	public static String toDate(String word)
	{
		if(word == null)
		{
			return "";
		}
		word = word.trim();
		if(word.isEmpty())
		{
			return "";
		}
		String format = "yyyy-MM-dd";
		if(word.length() > 10){format = "yyyy-MM-dd HH:mm:ss";}
		SimpleDateFormat sdf = new SimpleDateFormat(format);// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		//"yyyy-MM-dd HH:mm:ss" //包含时间
		sdf.setLenient(false);// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
		try
		{
			Date date = sdf.parse(word);
			word = sdf.format(date);
		}
		catch (Exception e)
		{
			return "";
		}
		return word;
	}
	/**
	 *将String 转换  为Date
	 *content  需要转换的 原始
	 *return  返回null或者日期
	 */
	public static Date parseDate(String word)
	{
		String format = "yyyy-MM-dd";
		if(word.length() > 10){format = "yyyy-MM-dd HH:mm:ss";}
		SimpleDateFormat sdf = new SimpleDateFormat(format);// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		//"yyyy-MM-dd HH:mm:ss"//格式化包括 时间
		sdf.setLenient(false);// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
		Date date = null;
		try
		{
			date = sdf.parse(word);
		}
		catch (Exception e)
		{
			return null;
		}
		return date;
	}

	public static String dateToString(Date date){
		return DEFAULT_DATEFORMAT.format(date);
	}

	/**
	 * 获取url参数
	 */
	public static Map<String, Object> getUrlParams(String url) {
		Map<String, Object> map = new HashMap<String, Object>();
		url = url.substring(url.lastIndexOf("?") + 1);
		// 这里的pairs是一个字符串数组
		// name=myname&password=1234&sex=male&address=nanjing
		String[] pairs = url.split("&");
		for (String pair : pairs) {
			int sign = pair.indexOf("=");
			// 如果没有找到=号，那么就跳过，跳到下一个字符串（下一个循环）。
			if (sign == -1) {
				continue;
			}
			String aKey = pair.substring(0, sign);
			String aValue = pair.substring(sign + 1);
			map.put(aKey, aValue);
		}
		return map;
	}

	public static String getBasePath(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder(request.getScheme());
		sb.append("://");
		sb.append(request.getServerName());
		sb.append(":");
		sb.append(request.getServerPort());
		sb.append(request.getContextPath());
		sb.append("/");
		return sb.toString();
	}

	/**
	 * 替换${key }参数
	 * @param message
	 * @param params
	 * @return
	 */
	public static String buildMsg(String message, Map<String, Object> params){
		StringBuffer sb = new StringBuffer();

		//创建匹配给定输入与此模式的匹配器。
		Matcher m = p.matcher(message); 

		// 是否匹配成功
		while(m.find()){
			if(params.get(m.group(1).trim())==null)
				m.appendReplacement(sb, "");
			else
				m.appendReplacement(sb, params.get(m.group(1).trim()).toString());
		}

		m.appendTail(sb); 
		return sb.toString();
	}

	public static Map<String, Object> readJson2Map(String json) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			//将json字符串转成map结合解析出来，并打印(这里以解析成map为例)
			@SuppressWarnings("unchecked")
			Map<String, Object> maps = objectMapper.readValue(json, Map.class);
			return maps;
			//            System.out.println(maps.size());
			//            Set<String> key = maps.keySet();
			//            Iterator<String> iter = key.iterator();
			//            while (iter.hasNext()) {
			//                String field = iter.next();
			//                System.out.println(field + ":" + maps.get(field));
			//            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//获取文本文件的格式
	public static String getFileEncode(File targetFile) {
		String charset ="asci";
		byte[] first3Bytes = new byte[3];
		BufferedInputStream bis = null;
		try {
			boolean checked = false;
			bis = new BufferedInputStream(new FileInputStream(targetFile));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1)
				return charset;
			if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "Unicode";//UTF-16LE
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
				charset = "Unicode";//UTF-16BE
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF8";
				checked = true;
			}
			bis.reset();
			if (!checked) {
				int len = 0;
				int loc = 0;
				while ((read = bis.read()) != -1) {
					loc++;
					if (read >= 0xF0)
						break;
					if (0x80 <= read && read <= 0xBF) //单独出现BF以下的，也算是GBK
					break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) 
							//双字节 (0xC0 - 0xDF) (0x80 - 0xBF),也可能在GB编码内
							continue;
						else
							break;
					} else if (0xE0 <= read && read <= 0xEF) { //也有可能出错，但是几率较小
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}
				//TextLogger.getLogger().info(loc + " " + Integer.toHexString(read));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException ex) {
				}
			}
		}
		return charset;
	}

	/**
	 * 设置cookie
	 * @param response
	 * @param name  cookie名字
	 * @param value cookie值
	 * @param maxAge cookie生命周期  以秒为单位
	 */
	public static void addCookie(HttpServletResponse response,String name,String value,int maxAge){
		Cookie cookie = new Cookie(name,value);
		cookie.setPath("/");
		if(maxAge>0)  cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}
	/**
	 * 根据名字获取cookie
	 * @param request
	 * @param name cookie名字
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request,String name){
		Map<String,Cookie> cookieMap = ReadCookieMap(request);
		if(cookieMap.containsKey(name)){
			Cookie cookie = (Cookie)cookieMap.get(name);
			return cookie;
		}else{
			return null;
		}   
	}
	/**
	 * 将cookie封装到Map里面
	 * @param request
	 * @return
	 */
	private static Map<String,Cookie> ReadCookieMap(HttpServletRequest request){  
		Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
		Cookie[] cookies = request.getCookies();
		if(null!=cookies){
			for(Cookie cookie : cookies){
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}
	/** 
	 * 得到本季度第一天的日期 
	 * @Methods Name getFirstDayOfQuarter 
	 * @return Date 
	 */  
	public static Date getFirstDayOfQuarter(Date date)   {     
		Calendar cDay = Calendar.getInstance();     
		cDay.setTime(date);  
		int curMonth = cDay.get(Calendar.MONTH);  
		if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH){    
			cDay.set(Calendar.MONTH, Calendar.JANUARY);  
		}  
		if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE){    
			cDay.set(Calendar.MONTH, Calendar.APRIL);  
		}  
		if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {    
			cDay.set(Calendar.MONTH, Calendar.JULY);  
		}  
		if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {    
			cDay.set(Calendar.MONTH, Calendar.OCTOBER);  
		}  
		cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMinimum(Calendar.DAY_OF_MONTH));  
		return cDay.getTime();     
	}  
	/** 
	 * 得到本季度最后一天的日期 
	 * @Methods Name getLastDayOfQuarter 
	 * @return Date 
	 */  
	public static Date getLastDayOfQuarter(Date date)   {     
		Calendar cDay = Calendar.getInstance();     
		cDay.setTime(date);  
		int curMonth = cDay.get(Calendar.MONTH);  
		if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH){    
			cDay.set(Calendar.MONTH, Calendar.MARCH);  
		}  
		if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE){    
			cDay.set(Calendar.MONTH, Calendar.JUNE);  
		}  
		if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {    
			cDay.set(Calendar.MONTH, Calendar.AUGUST);  
		}  
		if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {    
			cDay.set(Calendar.MONTH, Calendar.DECEMBER);  
		}  
		cDay.set(Calendar.DAY_OF_MONTH, cDay.getActualMaximum(Calendar.DAY_OF_MONTH));  
		return cDay.getTime();     
	}  

	/**
	 * 功能：Java读取txt文件的内容
	 * 步骤：1：先获得文件句柄
	 * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
	 * 3：读取到输入流后，需要读取生成字节流
	 * 4：一行一行的输出。readline()。
	 * 备注：需要考虑的是异常情况
	 * @param filePath
	 */
	public static String readFile(String filePath){
		try {
			StringBuilder sb = new StringBuilder();
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					sb.append(lineTxt);
				}
				read.close();
				return sb.toString();
			} else {
				//String time = AESCoder.encrypt(getCurrentTimestamp());
				//writeFile(time, filePath);
				return "";
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return "";
	}

	public static void writeFile(String content, String filePath) {
		File file = new File(filePath);
		try {
			FileOutputStream fop = new FileOutputStream(file);
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			byte[] contentInBytes = content.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int daysOfTwo(Date fDate, Date oDate) {
		int days = (int) ((oDate.getTime() - fDate.getTime()) / (1000*3600*24));
        return days;
    }
	
	public static Date dateAddDays(Date date, int days){
		Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();
	}
}