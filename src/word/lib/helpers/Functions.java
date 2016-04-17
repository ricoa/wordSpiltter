package word.lib.helpers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Functions {

	/**
	 * 判断是否汉字
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		String regEx = "[\u4e00-\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(c + "");
		if (m.find())
			return true;
		return false;
	}

	/**
	 * 是否是整数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	public static String getCodeOfFile(String path) throws Exception {
		InputStream inputStream = new FileInputStream(path);
		byte[] head = new byte[3];
		inputStream.read(head);
		String code = "gb2312";
		if (head[0] == -1 && head[1] == -2)
			code = "UTF-16";
		if (head[0] == -2 && head[1] == -1)
			code = "Unicode";
		if (head[0] == -17 && head[1] == -69 && head[2] == -65)
			code = "UTF-8";
		return code;
	}

	/**
	 * 输出函数
	 * 
	 * @param Object
	 */
	public static void dd(Object Object) {
		System.out.println(Object);
		System.exit(0);
	}

	/**
	 * 输出函数
	 * 
	 * @param map
	 */
	public static void pp(HashMap<?, ?> map) {
		Set<?> s = map.keySet();
		Iterator<?> i = s.iterator();
		while (i.hasNext()) {
			Object o = i.next();
			System.out.println(o + " -- " + map.get(o));
		}
	}

	/**
	 * 获取文件流
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static BufferedReader getBufferedReader(String path) throws Exception {
		File file = new File(path);
		String code = Functions.getCodeOfFile(path);// 获取文件编码

		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
		return new BufferedReader(new InputStreamReader(fis, "utf-8"), 4 * 1024 * 1024);
	}

	/**
	 * 获取子字符在字符串中数量
	 * 
	 * @param str
	 * @param sub
	 * @return
	 */
	public static int getCountInString(String str, String sub) {
		int index = 0;
		int count = 0;
		while ((index = str.indexOf(sub)) != -1) {
			str = str.substring(index + sub.length());
			count++;
		}

		return count;
	}

	/**
	 * 获取文件汉字字符数
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
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

	/**
	 * 读取文件内容
	 * 
	 * @param file
	 * @return
	 */
	public static String readToString(File file) {
		Long filelength = file.length(); // 获取文件长度
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			String code = Functions.getCodeOfFile(file.getAbsolutePath());
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
			return new String(filecontent, code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 读取文件
	 * 
	 * @param file
	 * @return
	 */
	public static String readToString2(File file) {
		Long filelength = file.length(); // 获取文件长度
		char[] filecontent = new char[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			BufferedInputStream fis = new BufferedInputStream(in);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "gbk"), 4 * 1024 * 1024);
			reader.read(filecontent);
			reader.close();
			fis.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(filecontent);// 返回文件内容,默认编码
	}

	/**
	 * 合并文件
	 * 
	 * @param path
	 */
	public static void merge(String path) {
		File file = new File(path);
		String content = readToString(file);
		content = content.replace("&nbsp", "");
		content = content.replace("\n\n", "\n");
		content = content.replace("  ", " ");
		FileWriter fw;
		try {
			fw = new FileWriter("./sogou", true);
			fw.write(content + "\n");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取文件的md5值
	 * 
	 * @param inputFile
	 * @return
	 * @throws IOException
	 */
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

	// 用于将字节数组换成成16进制的字符串
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

	// log(base,d)函数
	public static double log(double d, double base) {
		return (Math.log(d) / Math.log(base));
	}
}
