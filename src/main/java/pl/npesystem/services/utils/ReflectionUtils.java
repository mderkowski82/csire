package pl.npesystem.services.utils;

import org.reflections.Reflections;
import pl.npesystem.annotations.FuckedProp;
import pl.npesystem.data.AbstractEntity;
import pl.npesystem.models.records.FuckedPropInfo;

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
            return new FuckedPropInfo(annotation.clazz(),
                    annotation.view(),
                    annotation.edit(),
                    annotation.title()
            );
        }
        throw new RuntimeException("No FuckedProp annotation is present on " + aClass.getName());
    }
}
