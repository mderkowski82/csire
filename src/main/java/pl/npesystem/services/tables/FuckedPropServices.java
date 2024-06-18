package pl.npesystem.services.tables;

import org.springframework.stereotype.Service;
import pl.npesystem.services.records.FuckedPropInfo;
import pl.npesystem.services.utils.ReflectionUtils;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FuckedPropServices {
    public Optional<Object> getTableEntities() {
        Set<Class<?>> allEntities = ReflectionUtils.getAllEntities();

        Set<FuckedPropInfo> classInfo = allEntities.stream().map(ReflectionUtils::toFuckedPropInfo).collect(Collectors.toUnmodifiableSet());

        return Optional.of(classInfo);
    }
}
