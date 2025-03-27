package com.sebaslv.dao;

import com.sebaslv.models.UsuarioEntity;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class UsuarioDaoImp implements UsuarioDao{

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<UsuarioEntity> getUsuarios() {
        String query = "FROM Usuario";
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public void eliminar(Long id) {
        UsuarioEntity usuarioEntity = entityManager.find(UsuarioEntity.class, id);
        entityManager.remove(usuarioEntity);
    }

    @Override
    public void registrar(UsuarioEntity usuarioEntity) {
        entityManager.merge(usuarioEntity);
    }

    @Override
    public UsuarioEntity obtenerUsuarioPorCredenciales(UsuarioEntity usuarioEntity) {
        String query = "FROM Usuario WHERE email = :email";
        List<UsuarioEntity> lista = entityManager.createQuery(query)
                .setParameter("email", usuarioEntity.getEmail())
                .getResultList();

        if (lista.isEmpty()) {
            return null;
        }

        String passwordHashed = lista.get(0).getPassword();

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        if (argon2.verify(passwordHashed, usuarioEntity.getPassword())) {
            return lista.get(0);
        };
            return null;
        }
}
