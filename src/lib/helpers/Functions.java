package lib.helpers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Functions {
	public static boolean isChinese(char c) {
		String regEx = "[\u4e00-\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(c + "");
		if (m.find())
			return true;
		return false;
	}

	public static void dd(Object Object) {
		System.out.println(Object);
		System.exit(0);
	}

	public static void pp(HashMap<?, ?> map) {
		Set<?> s = map.keySet();
		Iterator<?> i = s.iterator();
		while (i.hasNext()) {
			Object o = i.next();
			System.out.println(o + " -- " + map.get(o));
		}
	}

	public static BufferedReader getBufferedReader(String path) throws Exception {
		File file = new File(path);
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
		return new BufferedReader(new InputStreamReader(fis, "utf-8"), 4 * 1024 * 1024);
	}

	public static int getCountInString(String str, String sub) {
		int index = 0;
		int count = 0;
		while ((index = str.indexOf(sub)) != -1) {
			str = str.substring(index + sub.length());
			count++;
		}
		
		return count;
	}
	
	public static int getCharNumInFile(String path) throws Exception {
		BufferedReader reader=Functions.getBufferedReader(path);
		String line="";
		int num=0;
		while((line=reader.readLine())!=null){
			num+=line.length();
		}
		return num;
	}

	public static String readToString(File file) {
		Long filelength = file.length(); // 获取文件长度
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(filecontent);// 返回文件内容,默认编码
	}
	
	public void test() {
//		File file=new File(path);
//		String content=readToString(file);
//		content=content.replace(" ","");
//		content=content.replace("#title#","");
//		content=content.replace("#comment#","");
//		content=content.replace("#content#","");
//		content=content.replace("#emotion#","");
//		content=content.replace("#/title#","");
//		content=content.replace("#/comment#","");
//		content=content.replace("#/content#","");
//		content=content.replace("#/emotion#","");
//		content=content.replace("#emotion_sum#","");
//		content=content.replace("#/emotion_sum#","");
//		content=content.replace("&quot;","");
//    	FileWriter fw = new FileWriter("./data2",true);
//		fw.write(content+"\n\n");
//		fw.close();
	}
}
