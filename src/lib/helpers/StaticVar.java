package lib.helpers;

public class StaticVar {

    public static final String DB_URL = "jdbc:mysql://localhost/word?useUnicode=true&characterEncoding=UTF-8";
    public static final String USER_NAME = "root";
    public static final String DB_PASSWD = "";
    public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    public static final int WORD_APPEAR_TIMES=5;//词出现次数超过该值才记为词
    public static final double BI_MI_GAP=6.6;//双字词互信息阈值
    public static final double N_MI_GAP=6.6;//多字词互信息阈值
}