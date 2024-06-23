package pl.npesystem.annotations;

import pl.npesystem.data.enums.FormTab;
import pl.npesystem.data.enums.RendererType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldProp {
    int position();

    String label();

    RendererType renderer();

    boolean editable() default true;

    FormTab tab() default FormTab.General;
}
