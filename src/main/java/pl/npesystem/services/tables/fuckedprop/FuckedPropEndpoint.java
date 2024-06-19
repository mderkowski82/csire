package pl.npesystem.services.tables.fuckedprop;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;
import com.vaadin.hilla.Nonnull;
import pl.npesystem.services.records.FuckedPropInfo;

import java.util.Optional;
import java.util.Set;

@Endpoint
@AnonymousAllowed
public class FuckedPropEndpoint {

    private final FuckedPropServices fuckedPropServices;

    public FuckedPropEndpoint(FuckedPropServices fuckedPropServices) {
        this.fuckedPropServices = fuckedPropServices;
    }

    public Optional<Set<FuckedPropInfo>> getTableEntities() {
        return fuckedPropServices.getTableEntities();
    }

    public Optional<FuckedPropInfo> getFuckedPropInfoByClazzId(@Nonnull String clazzId) {
        return fuckedPropServices.getFuckedPropInfoByClazzId(clazzId);
    }

}
