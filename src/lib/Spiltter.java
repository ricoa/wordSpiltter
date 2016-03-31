package lib;

import java.io.BufferedReader;
import java.util.HashMap;

import lib.helpers.Functions;

public class Spiltter {

	protected Dict dict=null;
	
	//TODO 6463060/
	protected int character_num=0;//本次处理的中文字符数
	
	public Spiltter(Dict dict){
		this.dict=dict;
	}
	


	/**
	 * 处理分词
	 * 
	 * @param path
	 */
	public void handle(String path) {
		
		try {
			
			//TODO
//			this.updateDict(path);
			this.updateWords(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * 更新字数据
	 * 
	 * @param path
	 * @throws Exception
	 */
	public void updateDict(String path) throws Exception {
		
		BufferedReader reader=Functions.getBufferedReader(path);
		
		/******更新单个字符数据*********/
		char[] line = new char[1024];
		
		//缓存
		HashMap<Character , Integer> map = new HashMap<Character , Integer>();
		
		while (reader.read(line) != -1) {

			int length = line.length;
			for (int i = 0; i < length; i++) {
				if(Functions.isChinese(line[i])){
					this.character_num++;
					
					if(map.get(line[i])!=null){
						
						int num=Integer.parseInt(map.get(line[i]).toString());
						map.put(line[i], num+1);
					}else{
						this.dict.createChar(line[i]);
						map.put(line[i], 1);
					}
				}
			}
			if(map.size()>=1024){
				this.dict.updateCharNum(map);
		        map.clear();
			}
			line = new char[1024];
		}
		if(map.size()>=0){
			this.dict.updateCharNum(map);
	        map.clear();
		}
		this.dict.updateCharacterNumToDB(this.character_num);		
	}
	
	/**
	 * 更新词数据
	 * 
	 * @param path
	 * @throws Exception 
	 */
	public void updateWords(String path) throws Exception {
		if(this.character_num==0){
			this.character_num=Functions.getCharNumInFile(path);
		}
		WordHandler wordHandler=new WordHandler(path,this.character_num);
		wordHandler.handle();		
	}
}
