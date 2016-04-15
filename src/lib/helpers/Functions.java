package lib.helpers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
		BufferedReader reader = Functions.getBufferedReader(path);
		char[] line = new char[1024];
		int num = 0;
		while (reader.read(line) != -1) {

			int length = line.length;
			for (int i = 0; i < length; i++) {
				if (Functions.isChinese(line[i])) {
					num++;
				}
			}

			line = null;
			line = new char[1024];
		}
		reader.close();
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
		// File file=new File(path);
		// String content=readToString(file);
		// content=content.replace(" ","");
		// content=content.replace("#title#","");
		// content=content.replace("#comment#","");
		// content=content.replace("#content#","");
		// content=content.replace("#emotion#","");
		// content=content.replace("#/title#","");
		// content=content.replace("#/comment#","");
		// content=content.replace("#/content#","");
		// content=content.replace("#/emotion#","");
		// content=content.replace("#emotion_sum#","");
		// content=content.replace("#/emotion_sum#","");
		// content=content.replace("&quot;","");
		// FileWriter fw = new FileWriter("./data2",true);
		// fw.write(content+"\n\n");
		// fw.close();
	}

	public static String fileMD5(String inputFile) throws IOException {
		// 缓冲区大小（这个可以抽出一个参数）
		int bufferSize = 256 * 1024;
		FileInputStream fileInputStream = null;
		DigestInputStream digestInputStream = null;
		try {
			// 拿到一个MD5转换器（同样，这里可以换成SHA1）
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			// 使用DigestInputStream
			fileInputStream = new FileInputStream(inputFile);
			digestInputStream = new DigestInputStream(fileInputStream, messageDigest);
			// read的过程中进行MD5处理，直到读完文件
			byte[] buffer = new byte[bufferSize];
			while (digestInputStream.read(buffer) > 0)
				;
			// 获取最终的MessageDigest
			messageDigest = digestInputStream.getMessageDigest();
			// 拿到结果，也是字节数组，包含16个元素
			byte[] resultByteArray = messageDigest.digest();
			// 同样，把字节数组转换成字符串
			return Functions.byteArrayToHex(resultByteArray);
		} catch (NoSuchAlgorithmException e) {
			return null;
		} finally {
			try {
				digestInputStream.close();
			} catch (Exception e) {
			}
			try {
				fileInputStream.close();
			} catch (Exception e) {
			}
		}
	}

	//用于将字节数组换成成16进制的字符串
	public static String byteArrayToHex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
			if (n < b.length - 1) {
				hs = hs + "";
			}
		}
		return hs;
	}
}
