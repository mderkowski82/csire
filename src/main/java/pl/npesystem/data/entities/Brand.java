package pl.npesystem.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pl.npesystem.annotations.FuckedProp;
import pl.npesystem.data.AbstractEntity;
import pl.npesystem.data.Role;
import pl.npesystem.data.repositories.BrandRepository;
import pl.npesystem.services.records.ColumnProp;

import java.util.List;

@Entity
@Table(name = "brand")
@FuckedProp(
        repository = BrandRepository.class,
        clazz = Brand.clazzId,
        view = {Role.USER, Role.ADMIN},
        edit = {Role.USER, Role.ADMIN},
        title = "Brand"
)
public class Brand extends AbstractEntity {
    public static final String clazzId = "2";
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ColumnProp> getDefaultColumn() {
        return List.of(

        );
    }
}
