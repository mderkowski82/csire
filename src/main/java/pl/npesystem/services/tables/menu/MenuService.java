package pl.npesystem.services.tables.menu;

import org.springframework.stereotype.Service;
import pl.npesystem.data.entities.Brand;
import pl.npesystem.data.entities.User;
import pl.npesystem.models.records.ChildMenuItem;
import pl.npesystem.models.records.ParentMenuItem;
import pl.npesystem.services.utils.ReflectionUtils;

import java.util.List;

@Service
public class MenuService {

    public List<ParentMenuItem> getMenu() {
        return List.of(
                new ParentMenuItem(
                        1,
                        "Encje",
                        List.of(
                                new ChildMenuItem(1L, "Użytkownicy", ReflectionUtils.toFuckedPropInfo(User.class)),
                                new ChildMenuItem(2L, "Brand", ReflectionUtils.toFuckedPropInfo(Brand.class)),
                                new ChildMenuItem(3L, "Użytkownicy 3", ReflectionUtils.toFuckedPropInfo(User.class))
                        )
                ),
                new ParentMenuItem(
                        2,
                        "Encje 2",
                        List.of(
                                new ChildMenuItem(1L, "Użytkownicy x", ReflectionUtils.toFuckedPropInfo(User.class)),
                                new ChildMenuItem(2L, "Użytkownicy x 2", ReflectionUtils.toFuckedPropInfo(User.class)),
                                new ChildMenuItem(3L, "Użytkownicy x 3", ReflectionUtils.toFuckedPropInfo(User.class))
                        )
                )
        );
    }
}
