package lib;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lib.helpers.Functions;
import lib.helpers.Mysql;
import lib.interfaces.IDict;
public class Dict implements IDict{

	protected Mysql mysql=null;
	private int character_num=0;
	
	@Override
	public void init() {
		//初始化数据库数据
		this.mysql=new Mysql();
		
		List<Map<String, Object>> setting = this.mysql.select("select `data` from settings where `name` like 'character_num' limit 1");
		if(setting.isEmpty()){
			this.mysql.insert("INSERT INTO `settings` (`id`, `name`, `data`) VALUES (NULL, 'character_num', '0');");
			this.resetDict();
		}else{
			Map<String, Object> character_num = setting.get(0);
			
			if(character_num.get("data").equals("0")){
				this.resetDict();
			}
			this.character_num=Integer.parseInt(character_num.get("data").toString());
		}
	}
	
	/**
	 * 清空数据库
	 */
	public void resetDict() {
		
		this.mysql.update("TRUNCATE TABLE `dicts`;");
		this.mysql.update("TRUNCATE TABLE `words`;");
		this.mysql.update("TRUNCATE TABLE `records`;");
	}

	/**
	 * 获取处理过的总字符数
	 * @return
	 */
	public int getAllCharacterNum() {
		return character_num;
	}

	/**
	 * 增加处理过的字符数
	 */
	public void incAllCharacterNum() {
		this.character_num +=1;
	}
	
	/**
	 * 更新数据到数据库
	 */
	public void updateCharacterNumToDB(int num) {
		this.mysql.update("update `settings` set data=data+'"+num+"' where name='character_num'");
	}
	
	/**
	 * 字符不存在则新增
	 * @param c
	 */
	public void createChar(char c) {
		
		List<Map<String, Object>> dict=this.mysql.select("select num from dicts where char_name='"+c+"'");
		if(dict.isEmpty()){
			this.mysql.insert("INSERT INTO `dicts` (`id`, `char_name`) VALUES (NULL, '"+c+"');");
			
		}
	}
	
	/**
	 * 更新单个字符的个数
	 * @param c
	 * @param num
	 */
	public void updateCharNum(Map map) {
		Set s = map.keySet();        
        Iterator i = s.iterator();
        Statement statement=null;
        while(i.hasNext()) {
            Object o = i.next();
            statement=this.mysql.addBatch(statement,"update `dicts` set num=num+"+map.get(o)+" where char_name='"+o+"';");
        }
		this.mysql.executeBatch(statement);
	}
	
	public int getNumOfChars(String chars) {
		List<Map<String, Object>> dict=this.mysql.select("select num from dicts where char_name='"+chars+"'");
		if(dict.isEmpty()){
			return 0;
		}
		
		return Integer.parseInt(dict.get(0).get("num").toString());
	}
	
	public void addWord(String word) {
		System.out.println(word);
	}
	
	public boolean exitsWord(String word){
		List<Map<String, Object>> dict=this.mysql.select("select id from words where word_name='"+word+"'");
		return !dict.isEmpty();
	}
}
