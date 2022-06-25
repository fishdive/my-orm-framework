package cn.jvtc.utils;

import cn.jvtc.annotations.Column;
import cn.jvtc.annotations.Table;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The type Pojo util.
 *
 * @author 雷族
 */
public class PojoUtil {

    /**
     * 类名和表名不一致则需要加上Table注解，有注解表名为注解值，无注解为首字母小写类名
     *
     * @param object 实体类对象
     * @return 表名称 table name
     */
    public static String getTableName(Object object) {
        //判断类注解获取表名
        Table tableAnnotation = object.getClass().getAnnotation(Table.class);
        String className = object.getClass().getName();
        return tableAnnotation == null ? className.substring(className.lastIndexOf(".") + 1).toLowerCase() : tableAnnotation.value();
    }

    /**
     * 类属性名和表字段名不一致则需要加上Column注解，有注解表字段名为注解值，无注解为类属性名
     *
     * @param field 类属性
     * @return 表字段名
     */
    public static String getColumnName(Field field) {
        //获取属性名
        String fieldName = field.getName();
        //判断列注解获取表字段名
        Column columnAnnotation = field.getAnnotation(Column.class);
        return columnAnnotation == null ? fieldName : columnAnnotation.value();
    }

    /**
     * 通过实体类执行方法，通过类属性名确认需要执行的get方法
     *
     * @param object 实体类
     * @param field  类属性名
     * @return 属性get方法值
     * @throws NoSuchMethodException     当找不到特定方法时抛出。
     * @throws InvocationTargetException InvocationTargetException是一个检查异常，它包装了由被调用的方法或构造函数抛出的异常。 在1.4版中，这个异常已经进行了改进，以符合通用的异常链接机制。在构造时提供并通过getTargetException()方法访问的“目标异常”现在被称为原因，可以通过Throwable.getCause()方法以及前面提到的“遗留方法”访问
     * @throws IllegalAccessException    当应用程序试图反射地创建实例(而不是数组)、设置或获取字段或调用方法时，抛出IllegalAccessException，但当前执行的方法没有访问指定类、字段、方法或构造函数的定义。
     */
    public static Object getMethodValue(Object object, Field field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //属性get方法名
        String methodName = "get" + field.getName().substring(0, 1).toUpperCase()
                + field.getName().substring(1);
        //通过get方法名获取get方法
        Method method = object.getClass().getMethod(methodName);
        return method.invoke(object);
    }
}
