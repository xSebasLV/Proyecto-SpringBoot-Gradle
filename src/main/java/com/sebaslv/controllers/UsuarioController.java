package com.sebaslv.controllers;

import com.sebaslv.controllers.request.CreateUserDTO;
import com.sebaslv.dao.UsuarioDao;
import com.sebaslv.models.ERole;
import com.sebaslv.models.RoleEntity;
import com.sebaslv.models.UsuarioEntity;
import com.sebaslv.repositories.UserRepository;
import com.sebaslv.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.GET)
    public UsuarioEntity getUsuario(@PathVariable Long id) {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(id);
        usuarioEntity.setNombre("Alberto");
        usuarioEntity.setApellido("Douglas");
        usuarioEntity.setEmail("juans.lopez2004@gmail.com");
        usuarioEntity.setTelefono("3197334685");
        return usuarioEntity;
    }

    @RequestMapping(value = "api/usuarios", method = RequestMethod.GET)
    public List<UsuarioEntity> getUsuarios(/*@RequestHeader(value = "Authorization") String token*/) {

//        String usuarioId = jwtUtil.getKey(token);

//            if (usuarioId == null) {
//                return new ArrayList<>();
//            }

        return usuarioDao.getUsuarios();
    }

    @RequestMapping(value = "api/usuarios", method = RequestMethod.POST)
    public void registrarUsuarios(@RequestBody UsuarioEntity usuarioEntity) {

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(1, 1024, 1, usuarioEntity.getPassword());

        usuarioEntity.setPassword(hash);

        usuarioDao.registrar(usuarioEntity);
    }

    @RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.DELETE)
    public void eliminar(@PathVariable Long id) {
        usuarioDao.eliminar(id);
    }

    @GetMapping("/session")
    public ResponseEntity<?> getDetailsSession() {
        String sessionId = "";
        User userObject = null;

        List<Object> sessions = sessionRegistry.getAllPrincipals();

        for (Object session : sessions) {
            if (session instanceof User) {
                userObject = (User) session;
            }

            List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(session, false);

            for (SessionInformation sessionInformation : sessionInformations) {
                sessionId = sessionInformation.getSessionId();
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("response", "Hello World");
        response.put("sessionId", sessionId);
        response.put("sessionUser", userObject);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {

        Set<RoleEntity> roles = createUserDTO.getRoles().stream()
                .map(role -> RoleEntity.builder()
                        .name(ERole.valueOf(role))
                        .build())
                        .collect(Collectors.toSet());

        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .nombre(createUserDTO.getNombre())
                .apellido(createUserDTO.getApellido())
                .email(createUserDTO.getEmail())
                .telefono(createUserDTO.getTelefono())
                .roles(roles)
                .build();

        userRepository.save(usuarioEntity);

        return ResponseEntity.ok(usuarioEntity);
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser (@RequestParam String id) {
        userRepository.deleteById(Long.parseLong(id));
        return "Se ha borrado el usuario con id".concat(id);
    }
}
