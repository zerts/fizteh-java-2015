package ru.fizteh.fivt.students.zerts.miniorm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by User on 17.12.2015.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String name() default "";
}
