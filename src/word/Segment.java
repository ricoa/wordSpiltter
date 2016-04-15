package word;

import java.io.File;

import lib.CharCounter;
import lib.Dict;
import lib.WordCounter;
import lib.helpers.Functions;

public class Segment {
	
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
			
			CharCounter counter=new CharCounter(dict);
			WordCounter w_Counter=new WordCounter(dict);
			
			for (int i = 0; i < fs.length; i++) {

				if (!fs[i].isDirectory()) {
//					System.out.println(Functions.fileMD5(fs[i].getAbsolutePath()));
					counter.handle(fs[i].getAbsolutePath());

					System.out.println(counter.getCharsNum());
//					w_Counter.setCharsNum(counter.getCharsNum());
//					w_Counter.handle(fs[i].getAbsolutePath());
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
