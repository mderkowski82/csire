package pl.npesystem.services.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.npesystem.annotations.FieldProp;
import pl.npesystem.annotations.FuckedProp;
import pl.npesystem.data.AbstractEntity;
import pl.npesystem.models.records.FieldPropInfo;
import pl.npesystem.models.records.FuckedPropInfo;
import pl.npesystem.services.records.ColumnProp;
import pl.npesystem.services.records.SimpleEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectionUtils {

    public static final Logger log = LoggerFactory.getLogger(ReflectionUtils.class);


    public static Set<Class<?>> getAllEntitiesClass() {
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
                field.getName(),
                fieldProp.position(),
                fieldProp.label(),
                fieldProp.renderer(),
                fieldProp.editable(),
                fieldProp.tab()
        );
    }

    public static Map<String, Object> mapEntityToRecord(AbstractEntity entity) {

        List<Field> fields = new ArrayList<>();
        Class<?> clazz = entity.getClass();
        while (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        fields = fields.stream()
                .filter(field -> field.isAnnotationPresent(FieldProp.class))
                .collect(Collectors.toList());

        Map<String, Object> record = new HashMap<>();

        for (Field field : fields) {
            System.out.println("Field: " + field.getName() + " Type: " + field.getType());
            boolean isEnum = field.getType().isEnum();
            boolean isCollection = Collection.class.isAssignableFrom(field.getType());
            boolean isEntity = AbstractEntity.class.isAssignableFrom(field.getType());

            try {
                field.setAccessible(true);
                if (isEnum) {
                    record.put(field.getName(), field.get(entity).toString());
                } else if (isCollection) {
                    if(field.get(entity) != null) {
                        Collection<?> collection = (Collection<?>) field.get(entity);
                        record.put(field.getName(), collection.stream().map(o -> {
                            if(AbstractEntity.class.isAssignableFrom(o.getClass())) {
                                AbstractEntity abstractEntity = (AbstractEntity) o;
                                return new SimpleEntity(abstractEntity.getId(), abstractEntity.toString());
                            } else {
                                return o;
                            }
                        }).collect(Collectors.toList()));
                    } else {
                        record.put(field.getName(), null);
                    }
                } else if (isEntity) {
                    Object o = field.get(entity);
                    if (o != null) {
                        AbstractEntity abstractEntity = (AbstractEntity) o;
                        record.put(field.getName(), new SimpleEntity(abstractEntity.getId(), abstractEntity.toString()));
                    } else {
                        record.put(field.getName(), null);
                    }
                } else {
                    record.put(field.getName(), field.get(entity));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Could not access field " + field.getName() + " on " + entity.getClass().getName(), e);
            }
        }
        try {
            System.out.println("Record: " + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(record));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return record;

    }
}
