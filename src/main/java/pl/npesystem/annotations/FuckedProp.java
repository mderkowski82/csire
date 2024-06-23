package pl.npesystem.annotations;

import pl.npesystem.data.Role;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FuckedProp {
    String clazz() default "";

    Role[] view() default {};

    Role[] edit() default {};

    Role[] delete() default {};

    String title();
}
