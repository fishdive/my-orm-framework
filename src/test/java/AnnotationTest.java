import cn.jvtc.dao.UserDao;
import cn.jvtc.po.User;
import org.junit.Test;

public class AnnotationTest {
    @Test
    /**
     * 自动SQL查询全部
     */
    public void selectAll(){
        UserDao userDao=new UserDao();
        for (User user : userDao.selectAll(new User())) {
            System.out.println(user.toString());
        }
    }
    @Test
    /**
     * 自动SQL主键查询单个
     */
    public void selectOne(){
        UserDao userDao=new UserDao();
        User user = userDao.selectOne(new User(1));
        System.out.println(user.toString());
    }

    @Test
    /**
     * 自动SQL条件查询
     */
    public void selectBy(){
        UserDao userDao=new UserDao();
        for (User user : userDao.selectAll(new User("李四", "马武"))) {
            System.out.println(user.toString());
        }
    }
    @Test
    /**
     * 自动SQL修改
     */
    public void update(){
        UserDao userDao=new UserDao();
        int update = userDao.update(new User(6, "qier", "123"));
        System.out.println(update);
    }

    @Test
    /**
     * 自动SQL删除
     */
    public void delete(){
        UserDao userDao=new UserDao();
        int delete = userDao.delete(new User(2));
        System.out.println(delete);
    }
    @Test
    /**
     * 自动SQL添加
     */
    public void insert(){
        UserDao userDao=new UserDao();
        int insert = userDao.insert(new User(7,"qier","123456"));
        System.out.println(insert);
    }
    @Test
    /**
     * 注入SQL查询
     */
    public void executeQuery(){
        UserDao userDao=new UserDao();
        for (User user : userDao.executeQuery("select * from user where id=?", 6)) {
            System.out.println(user.toString());
        }

    }
    @Test
    /**
     * 注入SQL修改
     */
    public void executeUpdate(){
        UserDao userDao=new UserDao();
        int i = userDao.executeUpdate("update user set username=? where id=?", "张三", 4);
        System.out.println(i);
    }

}
