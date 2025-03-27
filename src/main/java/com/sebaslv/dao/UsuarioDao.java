package com.sebaslv.dao;

import com.sebaslv.models.UsuarioEntity;

import java.util.List;

public interface UsuarioDao {

    List<UsuarioEntity> getUsuarios();

    void eliminar(Long id);

    void registrar(UsuarioEntity usuarioEntity);

    UsuarioEntity obtenerUsuarioPorCredenciales(UsuarioEntity usuarioEntity);
}
