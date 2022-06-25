package cn.jvtc.annotations;

import java.lang.annotation.*;

/**
 * @author 雷族
 */
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    String value() default "";
}
