package word.lib;

import java.io.BufferedReader;
import java.util.HashMap;
import word.lib.helpers.Functions;

/**
 * 字频统计类，用于统计文件的字频
 * @author 凡
 */
public class CharCounter {
	
	int character_num=0;
	private Dict dict;
	
	public CharCounter(Dict dict) {
		this.dict=dict;
	}
	
	public int getCharsNum() {
		return this.character_num;
	}
	
	/**
	 * 统计词频,处理单个文件
	 * @throws Exception 
	 */
	public void handle(String path) throws Exception{
		
		BufferedReader reader=Functions.getBufferedReader(path);
		this.character_num=0;//本次处理的字符数
		
		/******更新单个字符数据*********/
		char[] line = new char[1024];
		
		//缓存
		HashMap<Character , Integer> map = new HashMap<Character , Integer>();
		
		while (reader.read(line) != -1) {
			
			int length = line.length;
			
			for (int i = 0; i < length; i++) {
				if(Functions.isChinese(line[i])){

					//只处理中文汉字
					this.character_num++;
					if(map.get(line[i])!=null){
						int num=Integer.parseInt(map.get(line[i]).toString());
						map.put(line[i], num+1);
					}else{
						if(!this.dict.existsChar(line[i])){
							this.dict.createChar(line[i]);	
						}
						map.put(line[i], 1);
					}
				}
			}
			if(map.size()>=1024){
				//批量更新数据
				this.dict.updateDictByMap(map);
		        map.clear();
			}
			line = new char[1024];
		}
		if(map.size()>=0){
			this.dict.updateDictByMap(map);
	        map.clear();
		}
		this.dict.updateCharacterNumToDB(this.character_num);	
	}
}
