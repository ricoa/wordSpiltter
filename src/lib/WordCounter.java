package lib;

import java.io.BufferedReader;

import com.mysql.jdbc.log.Log;

import lib.helpers.Functions;
import lib.helpers.StaticVar;

public class WordCounter {

	private int chars_num = -1;// 本次处理的汉字数
	private Dict dict;
	private String path;
	private BufferedReader char_reader = null; // 待处理文件流
	private String candidate_word = null; // 候选词
	private char nextChar = 0;// 待处理字符
	private int candidate_word_num;// 候选词数量
	private double candidate_word_frequency; // 候选词频率
	private int nextChar_num; // 待处理字符数
	private double nextChar_frequency;// 待处理字符频率

	public WordCounter(Dict dict) {
		this.dict = dict;
	}

	public WordCounter(int chars_num, Dict dict) {
		this.chars_num = chars_num;
		this.dict = dict;
	}

	public void setCharsNum(int chars_num) {
		this.chars_num = chars_num;
	}
	/**
	 * 处理主函数
	 * 
	 * @param path
	 * @throws Exception
	 */
	public void handle(String path) throws Exception {
		this.path = path;
		if (this.chars_num == -1) {
			// 计算本次处理文本的汉字字符数
			this.chars_num = Functions.getCharNumInFile(path);
		}

		this.char_reader = Functions.getBufferedReader(path);

		char c[] = new char[1];

		// 初始化变量,赋值候选词以及其词频
		while ((this.char_reader.read(c, 0, 1) != -1) && Functions.isChinese(c[0])) {

			this.candidate_word = c[0] + "";
			this.candidate_word_num = this.dict.getNumOfChars(c[0]);

			// 如果字符在字典中不存在，则统计在本处理文本中的出现次数
			if (this.candidate_word_num == 0) {

				this.candidate_word_num = this.getNumOfCharsFromFile(this.candidate_word);
				this.candidate_word_frequency = this.candidate_word_num / this.chars_num;
			} else {
				this.candidate_word_frequency = this.candidate_word_num / this.dict.getAllCharacterNum();
			}
			break;
		}

		// 处理字符
		while ((this.char_reader.read(c, 0, 1) != -1) && Functions.isChinese(c[0])) {

			if (this.nextChar == '\0') {
				// 赋值待处理字符
				this.nextChar = c[0];
				this.nextChar_num = this.dict.getNumOfChars(this.nextChar);

				// 如果字符在字典中不存在，则统计在本处理文本中的出现次数
				if (this.nextChar_num == 0) {
					this.nextChar_num = this.getNumOfCharsFromFile(this.nextChar + "");
					this.nextChar_frequency = this.nextChar_num / this.chars_num;
				} else {
					this.nextChar_frequency = this.nextChar_num / this.dict.getAllCharacterNum();
				}

				// 计算组合词的频率
				String word_merged = this.candidate_word + this.nextChar;
				int word_merged_num = this.getNumOfCharsFromFile(word_merged);
				double word_merged_frequency = word_merged_num / this.chars_num;

				// 判断词典是否已经存在该词
				if (this.dict.exitsWord(word_merged)) {
					// 存在直接赋值给候选词
					this.candidate_word = word_merged;
					this.candidate_word_num = word_merged_num;
					this.candidate_word_frequency = word_merged_frequency;

					this.nextChar = 0;
					this.nextChar_num = 0;
				} else {
					double m_i = Math.log(word_merged_frequency / (candidate_word_frequency * nextChar_frequency));
					double gap;
					if (word_merged.length() > 2) {
						gap = StaticVar.BI_MI_GAP;// 双字词互信息阈值
					} else {
						gap = StaticVar.N_MI_GAP;// 多字词互信息阈值
					}
					if (m_i > gap) {
						// 组合词互信息超过阈值才记为词
						this.candidate_word = word_merged;
						this.candidate_word_num = word_merged_num;
						this.candidate_word_frequency = word_merged_frequency;
						this.nextChar = 0;
						this.nextChar_num = 0;
					} else {
						this.updateWordToDict();
					}
				}
			}
		}
		this.char_reader.close();
	}

	/**
	 * 从文件获取对应的字符串的出现次数
	 * 
	 * @param chars
	 * @return
	 * @throws Exception
	 */
	public int getNumOfCharsFromFile(String chars) throws Exception {

		BufferedReader reader = Functions.getBufferedReader(this.path);
		String line = "";
		int num = 0;
		while ((line = reader.readLine()) != null) {
			num += Functions.getCountInString(line, chars);
		}
		reader.close();
		return num;
	}

	/**
	 * 更新候选词到词典
	 */
	public void updateWordToDict() {
		this.dict.addWord(this.candidate_word);
		this.candidate_word = this.nextChar + "";
		this.candidate_word_num = this.nextChar_num;
		this.candidate_word_frequency = this.candidate_word_num / this.dict.getAllCharacterNum();
		this.nextChar = 0;
		this.nextChar_num = 0;
	}
}
