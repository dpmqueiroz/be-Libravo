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
import com.libravo.repository.PermissaosRepository;

@CrossOrigin
@RestController
@RequestMapping("/permissoes")
public class PermissaoController {
	
	@Autowired
	private PermissaosRepository permissaoRepository;

	@GetMapping
	public ResponseEntity<?> todasPermissoes(){
		List<Permissao> permissoes = permissaoRepository.findAll();
		
		if(permissoes.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		
		return ResponseEntity.ok(permissoes);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> permissaoPorId(@PathVariable Integer id){
		Optional<Permissao> permissao = permissaoRepository.findById(id);
		
		if(permissao.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não temos nenhuma Permissão com o id: " + id + ".");
		}else {
			return ResponseEntity.ok(permissao.get());
		}
	}
	
	@PostMapping()
	public ResponseEntity<?> adicionarPermissao(@RequestBody Permissao permissao){
		
		List<Permissao> permissaoRecebida = permissaoRepository.findByNome(permissao.getNome());
		
		if(permissaoRecebida.isEmpty()) {
			Permissao permissaoSalva = permissaoRepository.save(permissao);
			return ResponseEntity.ok(permissaoSalva);
		}else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("A permissão que você deseja criar já existe");
		}
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarPermissao(@PathVariable Integer id){
		Optional<Permissao> permissao = permissaoRepository.findById(id);
		
		if(permissao.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não encontramos uma permissão com esse ID" + id + " para remover");
		}else {
			permissaoRepository.delete(permissao.get());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}
	
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizarPermissao (@PathVariable Integer id, @RequestBody Permissao permissao){
		Optional<Permissao> permissaoRecebida = permissaoRepository.findById(id);
		
		if(permissaoRecebida.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Permissão não encontrada");
		}
		
		BeanUtils.copyProperties(permissao, permissaoRecebida.get(), "id");
		
		Permissao permissaoSalva = permissaoRepository.save(permissaoRecebida.get());
		
		return ResponseEntity.ok(permissaoSalva);
	}
	
}
