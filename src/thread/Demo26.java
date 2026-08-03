package thread;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;

class DBUtil {
    private static volatile DataSource dataSource = null;

    public static DataSource getInstance() {
        if(dataSource == null ){
            synchronized (DBUtil.class) {
                if(dataSource == null) {
                    // 这个代码是有问题的
//                    dataSource = new MysqlDataSource();
//                    ((MysqlDataSource)dataSource).setUrl("jdbc:mysql://127.0.0.1:3306/java117?characterEncoding-utf8&useSSL=false");
//                    ((MysqlDataSource)dataSource).setUser("root");
//                    ((MysqlDataSource)dataSource).setPassword("118523");

                    // 2. 用局部变量先完整初始化
                    MysqlDataSource ds = new MysqlDataSource();
                    ds.setUrl("jdbc:mysql://127.0.0.1:3306/java117?characterEncoding-utf8&useSSL=false");
                    ds.setUser("root");
                    ds.setPassword("118523");

                    // 3. 完全初始化后再赋值给共享变量
                    dataSource = ds;
                }
            }
        }
        return dataSource;
    }

}
public class Demo26 {

}
