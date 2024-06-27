package pl.npesystem.data.repositories;

import pl.npesystem.data.entities.Brand;
import pl.npesystem.data.entities.User;

import java.util.Optional;

public interface UserRepository extends GenericRepository<User> {

    Optional<User> findByUsername(String username);
}