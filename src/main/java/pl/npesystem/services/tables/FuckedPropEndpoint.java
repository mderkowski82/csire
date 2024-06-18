package pl.npesystem.services.tables;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;

import java.util.Optional;

@Endpoint
@AnonymousAllowed
public class FuckedPropEndpoint {

    private final FuckedPropServices fuckedPropServices;

    public FuckedPropEndpoint(FuckedPropServices fuckedPropServices) {
        this.fuckedPropServices = fuckedPropServices;
    }

    public Optional<Object> getTableEntities() {
        return fuckedPropServices.getTableEntities();
    }
}
