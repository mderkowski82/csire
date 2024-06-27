package pl.npesystem.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import pl.npesystem.annotations.FieldProp;
import pl.npesystem.annotations.FuckedProp;
import pl.npesystem.data.AbstractEntity;
import pl.npesystem.data.Role;
import pl.npesystem.data.enums.RendererType;
import pl.npesystem.data.repositories.UserRepository;
import pl.npesystem.services.records.ColumnProp;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "application_user")
@FuckedProp(
        repository = UserRepository.class,
        clazz = User.clazzId,
        view = {Role.USER, Role.ADMIN},
        edit = {Role.USER, Role.ADMIN},
        delete = {Role.USER, Role.ADMIN},
        title = "Użytkownicy")
public class User extends AbstractEntity {
    public static final String clazzId = "1";

    @FieldProp(position = 1, label = "Login", renderer = RendererType.Text)
    private String username;
    private String name;
    @JsonIgnore
    private String hashedPassword;
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;
    @Basic
    @Column(length = 1000000, columnDefinition = "text")
    private byte[] profilePicture;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<ColumnProp> getDefaultColumn() {
        return List.of(

        );
    }
}
