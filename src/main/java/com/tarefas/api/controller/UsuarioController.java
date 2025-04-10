package com.tarefas.api.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tarefas.api.dto.LoginRequestDTO;
import com.tarefas.api.dto.TrocaSenhaDTO;
import com.tarefas.api.dto.UsuarioDTO;
import com.tarefas.api.model.Usuario;
import com.tarefas.api.service.UsuarioService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "https://sgp-js.onrender.com")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvarUsuario(usuario));
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioDTO>> listarUsuarios(
        @PageableDefault(size = 20, page = 0, sort = "nome", direction = Direction.ASC) Pageable paginacao) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.listarUsuarios(paginacao));
    }

    @GetMapping("/busca")
    public ResponseEntity<List<UsuarioDTO>> filtrarUsuariosPeloNome(@RequestParam("nome") String nome) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.filtrarUsuariosPeloNome(nome));
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<UsuarioDTO>> filtrarUsuarioCujoNomeComecamCom(@RequestParam("nome") String nome) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.filtrarUsuariosCujoNomeComecamCom(nome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> consultarUsuario(@PathVariable("id") Long id) {
        UsuarioDTO usuario = usuarioService.buscarUsuarioPeloId(id);

        if (Objects.isNull(usuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<UsuarioDTO> consultarUsuarioPeloCpf(@PathVariable("cpf") String cpf) {
        UsuarioDTO usuario = usuarioService.buscarUsuarioPeloCpf(cpf);

        if (Objects.isNull(usuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable("id") Long id) {
        UsuarioDTO usuario = usuarioService.buscarUsuarioPeloId(id);

        if (Objects.isNull(usuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        usuarioService.deletarUsuario(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable("id") Long id, @Valid @RequestBody Usuario dadosUsuario) {
        UsuarioDTO usuario = usuarioService.buscarUsuarioPeloId(id);

        if (Objects.isNull(usuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.atualizarUsuario(id, dadosUsuario));
    }

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequestDTO login) {
    UsuarioDTO usuario = usuarioService.autenticarUsuario(login.getEmail(), login.getSenha());

    if (usuario == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos");
    }

    return ResponseEntity.ok(usuario);
}

@PutMapping("/{id}/senha")
public ResponseEntity<?> alterarSenha(@PathVariable Long id, @RequestBody TrocaSenhaDTO dto) {
    boolean sucesso = usuarioService.trocarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha());

    if (!sucesso) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Senha atual incorreta.");
    }

    return ResponseEntity.ok().build();
}

@RequestMapping(value = "/usuarios/login", method = RequestMethod.OPTIONS)
public ResponseEntity<?> handleOptionsLogin() {
    return ResponseEntity.ok().build();
}

}
