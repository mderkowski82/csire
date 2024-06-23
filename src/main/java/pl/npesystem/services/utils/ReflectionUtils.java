package pl.npesystem.services.utils;

import org.reflections.Reflections;
import pl.npesystem.annotations.FieldProp;
import pl.npesystem.annotations.FuckedProp;
import pl.npesystem.data.AbstractEntity;
import pl.npesystem.models.records.FieldPropInfo;
import pl.npesystem.models.records.FuckedPropInfo;
import pl.npesystem.services.records.ColumnProp;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionUtils {
    public static Set<Class<?>> getAllEntities() {
        Reflections reflections = new Reflections("pl.npesystem.data.entities");
        return reflections.getTypesAnnotatedWith(FuckedProp.class)
                .stream()
                .filter(AbstractEntity.class::isAssignableFrom)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static FuckedPropInfo toFuckedPropInfo(Class<?> aClass) {
        FuckedProp annotation = aClass.getAnnotation(FuckedProp.class);

        if (annotation != null) {
            List<FieldPropInfo> fieldPropInfo = toFieldPropInfo(aClass);
            try {
                // Creating an instance of the Class
                Object instance = aClass.getDeclaredConstructor().newInstance();

                // Fetching the method
                Method getDefaultColumnMethod = aClass.getDeclaredMethod("getDefaultColumn");

                // Invocation of the method
                List<ColumnProp> defaultColumns = (List<ColumnProp>) getDefaultColumnMethod.invoke(instance);

                return new FuckedPropInfo(annotation.clazz(),
                        annotation.view(),
                        annotation.edit(),
                        annotation.delete(),
                        annotation.title(),
                        defaultColumns
                );

            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                throw new RuntimeException("Could not call getDefaultColumn on " + aClass.getName(), e);
            }
        }
        throw new RuntimeException("No FuckedProp annotation is present on " + aClass.getName());
    }

    public static List<FieldPropInfo> toFieldPropInfo(Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(FieldProp.class))
                .map(ReflectionUtils::toFieldPropInfo)
                .collect(Collectors.toList());
    }

    private static FieldPropInfo toFieldPropInfo(Field field) {
        FieldProp fieldProp = field.getAnnotation(FieldProp.class);
        return new FieldPropInfo(
                fieldProp.position(),
                fieldProp.label(),
                fieldProp.renderer(),
                fieldProp.editable(),
                fieldProp.tab()
        );
    }
}
