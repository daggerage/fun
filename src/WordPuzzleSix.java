import java.sql.*;
import java.util.*;

public class WordPuzzleSix {
    private final int LENGTH =6;
    private final int MIN_LENGTH=3;//单词最小长度
    private Connection conn;//存储数据库连接
    private HashSet<String> wordComb;//存储所有单词的组合
    private ArrayList<String> resultNames;//存储所有结果单词String
    private String chs;//存储输入字符串

    //初始化
    WordPuzzleSix(String chs){
        if(chs.length()==LENGTH){
            conn=getConn();
            this.chs=chs;
            wordComb=new HashSet<String>();
        }
        else{
            System.out.println("number of characters less than 3!");
        }
    }
    //获取数据库连接
    private  Connection getConn() {
        String driver = "com.mysql.jdbc.Driver";//MySQL数据库驱动
        String url = "jdbc:mysql://localhost:3306/fun?characterEncoding=utf-8&useSSL=false";//数据库路径
        String username = "root";
        String password = "";
        try {
            Class.forName(driver);//加载数据库驱动
            conn = DriverManager.getConnection(url, username, password);//获取数据库连接
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    //关闭数据库连接
    private void closeConn(){
        try {
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void swap(char[] s,int a,int b){
        char temp;
        temp=s[a];
        s[a]=s[b];
        s[b]=temp;
    }
    //递归求全排列
    private void permuatation(char[] s,int start,int len) {
        if (start > len-1) {
            String word = "";
            for (int i = 0; i < len; i++) {
                word += s[i];
            }
            for (int i = MIN_LENGTH; i <= LENGTH; i++) {
                wordComb.add(word.substring(0,i));//利用HashSet顺带求全组合
            }
        }
        else{
            for (int i = start; i < len; i++) {
                swap(s,start,i);
                permuatation(s,start+1,len);
                swap(s,start,i);
            }
        }
    }

    private String formatSql(HashSet<String> hs){
        String sql = "SELECT * FROM words WHERE word IN (";
        int i=0;
        for(String name:hs){
            if(i==0){
                sql+=String.format("'%s'",name);
            }
            else{
                sql+=String.format(",'%s'",name);
            }
            i++;
        }
        sql+=")";
        return sql;
    }

    private void printResult(){
        System.out.println("#####"+chs+"#####");
        int i=0;
        for (String name:resultNames){
            i++;
            System.out.println(i+" "+name);
        }
    }

    public void findAllWords(){
        char[] c=new char[LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            c[i]=chs.charAt(i);
        }
        permuatation(c,0, LENGTH);

        resultNames=new ArrayList<String>();
        String sql=formatSql(wordComb);
        try {
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(sql);
            while (rs.next()){
                String name=rs.getString("word");
                resultNames.add(name);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        resultNames.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        });
        printResult();

        closeConn();
    }

    //一开始使用的方法，已废弃
    private Word select(String word){
        PreparedStatement pstm;
        try {
            String sql = "SELECT * FROM words WHERE word=?";
            pstm=conn.prepareStatement(sql);
            pstm.setString(1,word);
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
                String wordName=rs.getString("word");
                Word w=new Word(wordName);
                return w;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
//Select方法用到的实体(Entity)类，已废弃
class Word{
    public String word;
    Word(String word){
        this.word=word;
    }
}