package word;

import java.io.File;

import lib.Dict;
import lib.Spiltter;
import lib.helpers.Functions;

public class Main {
	
	public static void main(String[] args) {

		try {

			// 默认处理路径
			String directory = "src/data/original";

			if (args.length != 0) {

				directory = args[0];
			}

			// 时间计算
			long startTime = System.currentTimeMillis();
			
			File root = new File(directory);
			File[] fs = root.listFiles();

			if (fs == null) {
				Functions.dd("该目录下无可处理文件");
			}

			Dict dict=new Dict();
			dict.init();
			
			Spiltter spiltter=new Spiltter(dict);
			
			for (int i = 0; i < fs.length; i++) {

				if (!fs[i].isDirectory()) {
					spiltter.handle(fs[i].getAbsolutePath());
				}
			}
			
			// 计算运行时间
			long endTime = System.currentTimeMillis();
			System.out.println("\n程序运行时间： " + (endTime - startTime) + "ms");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
