package pl.npesystem.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.npesystem.annotations.FieldProp;
import pl.npesystem.annotations.FuckedProp;
import pl.npesystem.data.AbstractEntity;
import pl.npesystem.data.Role;
import pl.npesystem.data.enums.FormTab;
import pl.npesystem.data.enums.RendererType;
import pl.npesystem.data.interfaces.TableInterface;
import pl.npesystem.data.repositories.TestEntityRepository;
import pl.npesystem.services.records.ColumnProp;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TEST_ENTITY")
@FuckedProp(
        repository = TestEntityRepository.class,
        clazz = TestEntity.clazzId,
        view = {Role.USER, Role.ADMIN},
        edit = {Role.USER, Role.ADMIN},
        delete = {Role.USER, Role.ADMIN},
        title = "TEST ENTITY"
)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class TestEntity extends AbstractEntity implements TableInterface {
    public static final String clazzId = "666";

    @FieldProp(position = 1, tab = FormTab.General,label="String Text", renderer = RendererType.Text)
    private String stringValue;

    @FieldProp(position = 2, label="Integer Text", renderer = RendererType.Integer)
    private Integer intValue;

    @FieldProp(position = 3, label="Long Text", renderer = RendererType.Long)
    private Long longValue;

    @FieldProp(position = 4, label="BigDecimal Text", renderer = RendererType.BigDecimal)
    private BigDecimal bigDecimalValue;

    @FieldProp(position = 5, label="Boolean Text", renderer = RendererType.Boolean)
    private Boolean aBooleanValue;

    @FieldProp(position = 7, label="Role", renderer = RendererType.Enumerated)
    @Enumerated(EnumType.STRING)
    private Role enumValue;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @CollectionTable(name = "TEST_ENTITY_role", joinColumns = @JoinColumn(name = "owner_id"))
    @FieldProp(position = 8, label="Set Role", renderer = RendererType.Enumerateds)
    private Set<Role> enumValues = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "one_to_one_id")
    private TestSecondEntity oneToOne;

    @ToString.Exclude
    @OneToMany(mappedBy = "testEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TestSecondEntity> oneToMany = new LinkedHashSet<>();

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "many_to_one_id")
    private TestSecondEntity manyToOne;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "TEST_ENTITY_manyToMany",
            joinColumns = @JoinColumn(name = "testEntity_id"),
            inverseJoinColumns = @JoinColumn(name = "manyToMany_id"))
    private Set<TestSecondEntity> manyToMany = new LinkedHashSet<>();



    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestEntity that)) return false;
        if (!super.equals(o)) return false;

        return Objects.equals(stringValue, that.stringValue) && Objects.equals(intValue, that.intValue) && Objects.equals(longValue, that.longValue) && Objects.equals(bigDecimalValue, that.bigDecimalValue) && enumValue == that.enumValue;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(stringValue);
        result = 31 * result + Objects.hashCode(intValue);
        result = 31 * result + Objects.hashCode(longValue);
        result = 31 * result + Objects.hashCode(bigDecimalValue);
        result = 31 * result + Objects.hashCode(enumValue);
        return result;
    }

    @JsonIgnore
    public List<ColumnProp> getDefaultColumn() {
        return List.of(
                new ColumnProp("stringValue", "String Text"),
                new ColumnProp("intValue", "Integer Text"),
                new ColumnProp("longValue", "Long Text"),
                new ColumnProp("bigDecimalValue", "BigDecimal Text"),
                new ColumnProp("aBooleanValue", "Boolean Text"),
                new ColumnProp("enumValue", "String Text"),
                new ColumnProp("enumValues", "Set Role"),
                new ColumnProp("testSecondEntity", "Entity"),
                new ColumnProp("testSecondEntities", "Entities")
        );
    }
}
