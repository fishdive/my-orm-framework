package cn.jvtc.dao;

import cn.jvtc.core.BasePlus;
import cn.jvtc.po.User;

/**
 * @author 雷族
 */
public class UserDao extends BasePlus<User> {
    /**
     * 构造方法里面必须写入实体类class
     */
    public UserDao() {
        super(User.class);
    }

    public UserDao(Class<User> clazz) {
        super(clazz);
    }
}
