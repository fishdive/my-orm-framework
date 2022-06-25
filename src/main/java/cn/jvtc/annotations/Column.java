package cn.jvtc.annotations;

import java.lang.annotation.*;

/**
 * @author 雷族
 */
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    String value() default "";
}
