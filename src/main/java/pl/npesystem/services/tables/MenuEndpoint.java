package pl.npesystem.services.tables;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;
import com.vaadin.hilla.Nonnull;

import java.util.List;

@Endpoint
@AnonymousAllowed
public class MenuEndpoint {
    private final MenuService menuService;

    public MenuEndpoint(MenuService menuService) {
        this.menuService = menuService;
    }

    public List<@Nonnull ParentMenuItem> getMenu() {
        return menuService.getMenu();
    }
}
