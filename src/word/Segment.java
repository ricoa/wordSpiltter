package word;

import java.io.File;

import lib.CharCounter;
import lib.Dict;
import lib.TextSegment;
import lib.WordCounter;
import lib.helpers.Functions;

public class Segment {
	
	public static void main(String[] args) {
		// 时间计算
		long startTime = System.currentTimeMillis();
		// 计算运行时间
		try {

			if (args.length == 0) {

				Segment.showTip();
			}
			
			//处理目录或文件
			String path = args[0];// "src/data/original";
			int option = 0;
			//处理参数
			if(args.length>=2){
				//字典训练为1，词典训练为2，文本分词为3，全部为0
				if(args[1].equals("-train:chars")||args[1].equals("-train:words")||args[1].equals("-segment:text")){
					
					option=args[1].equals("-train:chars")?1:
						(args[1].equals("-train:words")?2:3);
				}else{
					System.out.println("参数错误");
					Segment.showTip();
				}
			}

			File root = new File(path);
			
			if(root.exists()){
				
				Dict dict = new Dict();
				dict.init();
				CharCounter charCounter = new CharCounter(dict);
				WordCounter wordCounter = new WordCounter(dict);
				TextSegment textSegment = new TextSegment(dict);
				
				if(root.isDirectory()){
					
					File[] fs = root.listFiles();

					if (fs == null) {
						Functions.dd("该目录下无可处理文件");
					}
					for (int i = 0; i < fs.length; i++) {

						if (!fs[i].isDirectory()) {
							Segment.handle(fs[i].getAbsolutePath(), charCounter, wordCounter, textSegment, option);
						}
					}
				}else{
					Segment.handle(path, charCounter, wordCounter, textSegment, option);
				}
				
			}else{
				System.out.println("文件或者目录不存在");
				Segment.showTip();
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("\n程序运行时间： " + (endTime - startTime) + "ms");
	}
	
	private static void showTip() {
		Functions.dd("用法：Segment path [options]\nroot：需要处理的文件目录或者文件路径"
				+ "\noptions包括:\n\t-train:chars\t字频统计，用于训练字典"
				+ "\n\t-train:words\t统计取词，用于训练词典"
				+ "\n\t-segment:text\t文本分词，用于对文本分词后输出，输出存放于data/handled目录下"
				+ "\n\t无:\t\t对文本进行字频统计，统计取词后进行文本分词，输出存放于data/handled目录下");
	}
	
	public static void handle(String path,CharCounter charCounter,
			WordCounter wordCounter,TextSegment textSegment,int option) throws Exception {
		switch (option) {
		case 0:
			//全部操作
			charCounter.handle(path);
			wordCounter.setCharsNum(charCounter.getCharsNum());
			wordCounter.handle(path);
			textSegment.handle(path);
			break;
		case 1:
			//仅训练字典
			charCounter.handle(path);			
			break;
		case 2:
			//仅训练词典
			wordCounter.handle(path);
			break;
		case 3:
			//仅文本分词
			textSegment.handle(path);
		default:
			break;
		}
	}
}
