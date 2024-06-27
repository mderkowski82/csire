package pl.npesystem.data.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import pl.npesystem.data.repositories.TestSecondEntityRepository;
import pl.npesystem.services.records.ColumnProp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "TEST_SECOND_ENTITY")
@FuckedProp(
        repository = TestSecondEntityRepository.class,
        clazz = TestSecondEntity.clazzId,
        view = {Role.USER, Role.ADMIN},
        edit = {Role.USER, Role.ADMIN},
        delete = {Role.USER, Role.ADMIN},
        title = "TEST ENTITY"
)
@Getter
@Setter
@RequiredArgsConstructor
public class TestSecondEntity extends AbstractEntity implements TableInterface {
    public static final String clazzId = "667";

    @FieldProp(position = 1, tab = FormTab.General,label="String Text", renderer = RendererType.Text)
    private String stringValue;

    @FieldProp(position = 2, label="Integer Text", renderer = RendererType.Integer)
    private Integer intValue;

    @FieldProp(position = 3, label="Long Text", renderer = RendererType.Long)
    private Long longValue;

    @FieldProp(position = 4, label="BigDecimal Text", renderer = RendererType.BigDecimal)
    private BigDecimal bigDecimalValue;

    @FieldProp(position = 5, label="String Text", renderer = RendererType.Enumerated)
    @Enumerated(EnumType.STRING)
    private Role enumValue;

    @ManyToOne
    @JoinColumn(name = "test_entity_id")
    @JsonBackReference
    private TestEntity testEntity;

    @OneToOne(mappedBy = "testSecondEntity", orphanRemoval = true)
    @JsonBackReference
    private TestEntity testEntity2;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestSecondEntity that)) return false;
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
                new ColumnProp("enumValue", "String Text")
        );
    }

    @Override
    public String toString() {
        return this.getId() + " " + this.getStringValue();
    }
}
