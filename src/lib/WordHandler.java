package lib;

import java.io.BufferedReader;
import lib.helpers.Functions;
import lib.helpers.StaticVar;

public class WordHandler {

	private BufferedReader char_reader=null;
	private int char_num;//本次处理的汉字数
	private Dict dict;
	private String path;
	char c[]=new char[1];
	private String word1="";
	private String word2="";
	private float word1_num=0;
	private float word2_num=0;
	private float word1_frequency=0;
	private float word2_frequency=0;
	
	public WordHandler(String path,int char_num) throws Exception {
		this.char_num = char_num;
		this.char_reader=Functions.getBufferedReader(path);
		this.dict=new Dict();
		this.dict.init();
		this.path=path;
	}

	public void handle() throws Exception {
		
		//处理字符
		while(this.char_reader.read(this.c, 0, 1)!=-1){
			
			if(this.word1.equals("")){
				//汉字才处理
				if(Functions.isChinese(c[0])){
					this.word1=this.c[0]+"";
					this.word1_num=this.dict.getNumOfChars(this.word1);
					this.word1_frequency=this.word1_num/this.dict.getAllCharacterNum();
					
				}
				continue;
			}
			
			if(this.word2.equals("")){
				if(Functions.isChinese(c[0])){
					this.word2=c[0]+"";
					this.word2_num=this.dict.getNumOfChars(this.word2);
					this.word2_frequency=this.word2_num/this.dict.getAllCharacterNum();
					
					float word_merged_num=this.getNumOfChars(word1+word2);
					float word_merged_frequency=word_merged_num/this.char_num;
					
					//判断词典是否已经存在该词
					if(this.dict.exitsWord(word1+word2)){
						this.word1=this.word1+this.word2;
						this.word1_num=word_merged_num;
						this.word1_frequency=word_merged_frequency;
						
						this.word2="";
						this.word2_num=0;
					}else{
						if(word_merged_num>=StaticVar.WORD_APPEAR_TIMES){
						//词出现了一定次数才记为词
//						if(word1.equals("太")){
//							System.out.println(word_merged_num);
//							System.out.println(this.char_num);
//							System.out.println(word_merged_frequency/(word1_frequency*word2_frequency));
//							Functions.dd(word1_num);
//						}
						if(word_merged_frequency/(word1_frequency*word2_frequency)>StaticVar.WORD_FREQUENCY_GAP){
							//出现比例超过临界值才记为词
							this.word1=this.word1+this.word2;
							this.word1_num=word_merged_num;
							this.word1_frequency=word_merged_frequency;
							
							this.word2="";
							this.word2_num=0;
						}else{
							this.updateWordToDict();
						}
					}else{
						this.updateWordToDict();
					}
					}
					
				}else{
					//分隔符则更新词典
					this.updateWordToDict();
				}
			}
		}
	}
	
	public int getNumOfChars(String chars) throws Exception {
	
		BufferedReader reader = Functions.getBufferedReader(path);
		String line="";
		int num=0;
		while((line=reader.readLine())!=null){
			num+=Functions.getCountInString(line, chars);
		}
		return num;
	}
	
	
	public void updateWordToDict() {
		this.dict.addWord(this.word1);
		this.word1=this.word2;
		this.word1_num=this.word2_num;
		this.word1_frequency=this.word1_num/this.dict.getAllCharacterNum();
		this.word2="";
		this.word2_num=0;
	}
}
