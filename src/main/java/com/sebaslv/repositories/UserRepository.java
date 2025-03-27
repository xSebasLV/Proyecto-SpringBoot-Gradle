package com.sebaslv.repositories;

import com.sebaslv.models.UsuarioEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UsuarioEntity, Long> {
    @Query("select u.nombre from UsuarioEntity u where u.nombre = ?1")
    Optional<UsuarioEntity> getName(String name);
}
