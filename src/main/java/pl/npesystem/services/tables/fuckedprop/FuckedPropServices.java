package pl.npesystem.services.tables.fuckedprop;

import org.springframework.stereotype.Service;
import pl.npesystem.models.records.FuckedPropInfo;
import pl.npesystem.services.utils.ReflectionUtils;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FuckedPropServices {
    public Optional<Set<FuckedPropInfo>> getTableEntities() {
        Set<Class<?>> allEntities = ReflectionUtils.getAllEntitiesClass();

        Set<FuckedPropInfo> classInfo = allEntities.stream().map(ReflectionUtils::toFuckedPropInfo).collect(Collectors.toUnmodifiableSet());

        return Optional.of(classInfo);
    }

    public Optional<FuckedPropInfo> getFuckedPropInfoByClazzId(String clazzId) {
        Optional<Set<FuckedPropInfo>> tableEntities = getTableEntities();
        if(tableEntities.isPresent()) {
            Set<FuckedPropInfo> collect = tableEntities.get().stream().filter(fuckedPropInfo -> fuckedPropInfo.clazz().equals(clazzId)).collect(Collectors.toSet());
            return switch (collect.size()) {
                case 0 -> throw new IllegalStateException("Brak klasy o classId: %s".formatted(clazzId));
                case 1 -> collect.stream().findFirst();
                default -> throw new IllegalStateException("Więcej niż jedna klasa o classId: %s".formatted(clazzId));
            };
        } else {
            throw new IllegalStateException("Brak zarządzalnych klas w systemie");
        }

    }
}
