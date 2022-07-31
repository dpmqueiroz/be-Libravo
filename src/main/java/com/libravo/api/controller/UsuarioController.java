package com.libravo.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.libravo.model.Permissao;
import com.libravo.model.ReqMudarAcesso;
import com.libravo.model.Usuario;
import com.libravo.repository.PermissaosRepository;
import com.libravo.repository.UsuarioRepository;

@CrossOrigin
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PermissaosRepository permissoesRepository;
	
	@GetMapping
	public ResponseEntity<?> retornarTodosUsuarios(){
		
		try {
			List<Usuario> usuarios = usuarioRepository.findAll();			
			return ResponseEntity.status(HttpStatus.OK).body(usuarios);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar todos os usuarios");
		}
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?>  retornarUsuarioPeloId(@PathVariable Integer id){
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		if(usuario.isPresent()) {
			return ResponseEntity.ok(usuario.get());
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
		}
	}
	
	@PostMapping
	public ResponseEntity<?> cadastraUsuario(@RequestBody Usuario usuario){
		List<Usuario> usuarioRecebido = usuarioRepository.findByEmail(usuario.getEmail());
		if(usuarioRecebido.isEmpty()) {
			
				Usuario novoUsuario = new Usuario();
				novoUsuario.setEmail(usuario.getEmail());
				novoUsuario.setFirstName(usuario.getFirstName());
				novoUsuario.setPermissao(null);
				novoUsuario.setImageUrl(usuario.getImageUrl());

				if(usuario.getLastName() != null) {
					novoUsuario.setLastName(usuario.getLastName());
				}
				
			return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(novoUsuario));
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email já cadastrado");
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarUsuario(@PathVariable Integer id){
		Optional<Usuario> usuarioRecebido = usuarioRepository.findById(id);
		if(usuarioRecebido.isPresent()) {
			usuarioRepository.delete(usuarioRecebido.get());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O Usuário com o ID : " + id + " não existe.");
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editarUsuario(@PathVariable Integer id, @RequestBody Usuario usuario){
		
		Optional<Usuario> usuarioRecebido = usuarioRepository.findById(id);
		
		if(usuarioRecebido.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário que você gostaria de alterar não existe. id = " + id);
		}
		
		BeanUtils.copyProperties(usuario, usuarioRecebido.get(), "id", "permissoes");
		
		Usuario usuarioSalvo = usuarioRepository.save(usuarioRecebido.get());
		
		return ResponseEntity.ok(usuarioSalvo);
	}
	
	@PutMapping("/mudar-acesso")
	public ResponseEntity<?> mudarAcesso(@RequestBody ReqMudarAcesso dados){
		
		Optional<Usuario> usuario = usuarioRepository.findById(dados.getId());
		
		if(usuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O Usuário com ID : " + dados.getId() + " não existe");
		}
		
		List<Permissao> permissao = permissoesRepository.findByNome(dados.getNomeAcesso());
		if(permissao.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("A Permissao : " + dados.getNomeAcesso() + ", não existe");
		}
		
		usuario.get().setPermissao(permissao.get(0));
		
		usuarioRepository.save(usuario.get());
		return ResponseEntity.ok().body("Permissão do usuário " + usuario.get().getFirstName() + " alterado para "+ permissao.get(0).getNome());
	}
	
	
	public ResponseEntity<?> buscarPorEmail(String email){
		List<Usuario> usuario = usuarioRepository.findByEmail(email);
		if(usuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário cadastrado com esse Email: " + email);
		}else {
			return ResponseEntity.status(HttpStatus.OK).body(usuario.get(0));
		}
	}
	
}
