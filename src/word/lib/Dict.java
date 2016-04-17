package word.lib;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import word.lib.helpers.Mysql;
import word.lib.interfaces.IDict;

public class Dict implements IDict{

	protected Mysql mysql=null;
	private int character_num=0;
	
	/**
	 * 初始化
	 */
	public void init() {
		//初始化数据库数据
		this.mysql=Mysql.getInstance();
		
		//初始化数据库中的数据
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
	 * 更新单个字符的个数,批量更新
	 * @param c
	 * @param num
	 */
	public void updateDictByMap(Map<?, ?> map) {
		Set<?> s = map.keySet();        
        Iterator<?> i = s.iterator();
        Statement statement=null;
        boolean update=false;
        while(i.hasNext()) {
            Object o = i.next();
            update=true;
            statement=this.mysql.addBatch(statement,"update `chars` set num=num+"+map.get(o)+" where char_name='"+o+"';");
        }
        if(update){
        	this.mysql.executeBatch(statement);
        }
		
	}
	
	/**
	 * 更新数据到数据库
	 */
	public void updateCharacterNumToDB(int num) {
		this.mysql.update("update `settings` set data=data+'"+num+"' where name='character_num'");
	}
	
	/**
	 * 重置数据库
	 */
	public void resetDict() {
		
		this.mysql.update("TRUNCATE TABLE `chars`;");
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
	 * 是否存在字符
	 * 
	 * @param c
	 * @return
	 */
	public boolean existsChar(char c) {
		List<Map<String, Object>> dict=this.mysql.select("select num from chars where char_name='"+c+"'");
		return !dict.isEmpty();
	}
	
	/**
	 * 字符新增
	 * @param c
	 */
	public void createChar(char c) {
		this.mysql.insert("INSERT INTO `chars` (`id`, `char_name`) VALUES (NULL, '"+c+"');");
	}
	
	/**
	 * 获取字符出现数
	 * @param chars
	 * @return
	 */
	public int getNumOfChars(char c) {
		List<Map<String, Object>> dict=this.mysql.select("select num from chars where char_name='"+c+"'");
		if(dict.isEmpty()){
			return 0;
		}
		return Integer.parseInt(dict.get(0).get("num").toString());
	}
	
	
	
	public void addWord(String word) {
		
		if(word.length()>=2){
			if(!exitsWord(word)){
				System.out.println(word);
				this.mysql.insert("INSERT INTO `words` (`id`, `word_name`) VALUES (NULL, '"+word+"');");
			}
		}
//		System.out.println(word);
	}
	
	public boolean exitsWord(String word){
		List<Map<String, Object>> dict=this.mysql.select("select id from words where word_name='"+word+"'");
		return !dict.isEmpty();
	}
}
