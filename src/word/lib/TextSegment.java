package word.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import word.lib.helpers.Functions;
import word.lib.helpers.StaticVar;

/**
 * 文本分词类
 * 
 * @author 凡
 */
public class TextSegment {

	private Dict dict;
	private String path;
	private BufferedReader char_reader = null; // 待处理文件流
	private String candidate_word = null; // 候选词
	private char nextChar = 0;// 待处理字符
	private BufferedWriter fw;
	public TextSegment(Dict dict) {
		this.dict = dict;
	}


	/**
	 * 处理主函数
	 * 
	 * @param path
	 * @throws Exception
	 */
	public void handle(String path) throws Exception {
		
		File file = new File(path);
		
		FileWriter fs = new FileWriter("data/handled/output_"+file.getName(),true);
		fw=new BufferedWriter(fs);
		this.path = path;
		this.char_reader = Functions.getBufferedReader(path);
		char c[] = new char[1];
		boolean isChar=false;//是否输出了非汉字字符
		// 初始化变量,赋值候选词
		while ((this.char_reader.read(c, 0, 1) != -1)) {

			if(Functions.isChinese(c[0])){
				this.candidate_word = c[0] + "";
				break;
			}else{
				fw.write(c[0]);
				if(!Functions.isInteger(c[0]+"")){
					fw.write("  ");
				}
				
			}
		}

		// 处理字符
		while ((this.char_reader.read(c, 0, 1) != -1)) {
			
			if(!Functions.isChinese(c[0])){
				fw.write(this.candidate_word+"  ");
				fw.write(c[0]);
				if(!Functions.isInteger(c[0]+"")){
					fw.write("  ");
				}
				while ((this.char_reader.read(c, 0, 1) != -1)) {

					if(Functions.isChinese(c[0])){
						this.candidate_word = c[0] + "";
						break;
					}else{
						fw.write(c[0]);
						if(!Functions.isInteger(c[0]+"")){
							fw.write("  ");
						}
					}
				}
				continue;
			}
			if (this.nextChar == '\0') {
				// 赋值待处理字符
				this.nextChar = c[0];
				
				String word_merged = this.candidate_word + this.nextChar;
				
				// 判断词典是否已经存在该词
				if (this.dict.exitsWord(word_merged)) {
					// 存在直接赋值给候选词
					//最长匹配
					this.candidate_word = word_merged;
					this.nextChar = 0;
					
				} else {
					this.writeWordToText();
				}
			}
		}
		this.char_reader.close();
		fw.close();
		fs.close();
	}


	/**
	 * 文件写入
	 * @throws IOException 
	 */
	public void writeWordToText() throws IOException {
		fw.write(this.candidate_word+"  ");
		this.candidate_word = this.nextChar + "";
		this.nextChar = 0;
	}
}
