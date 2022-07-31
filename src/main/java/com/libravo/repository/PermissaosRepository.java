package com.libravo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libravo.model.Permissao;

public interface PermissaosRepository extends JpaRepository<Permissao, Integer>{
	List<Permissao> findByNome(String nome);
}
