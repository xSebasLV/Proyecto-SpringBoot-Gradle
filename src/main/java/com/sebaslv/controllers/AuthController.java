package com.sebaslv.controllers;

import com.sebaslv.dao.UsuarioDao;
import com.sebaslv.models.UsuarioEntity;
import com.sebaslv.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "api/login", method = RequestMethod.POST)
    public String login(@RequestBody UsuarioEntity usuarioEntity) {

        UsuarioEntity usuarioEntityLogueado = usuarioDao.obtenerUsuarioPorCredenciales(usuarioEntity);
        if (usuarioEntityLogueado != null) {
//            String tokenJwt = jwtUtil.create(String.valueOf(usuarioLogueado.getId()), usuarioLogueado.getEmail());

            return "OK";
        };
        return "FAIL";
    }
}
