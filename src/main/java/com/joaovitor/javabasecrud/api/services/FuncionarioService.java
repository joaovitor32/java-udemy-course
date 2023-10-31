package com.joaovitor.javabasecrud.api.services;

import java.util.Optional;

import com.joaovitor.javabasecrud.api.entities.Funcionario;

public interface FuncionarioService {

 
    /**
     * Persiste um funcionario na base de dados
     * 
     * @param funcionario
     * @return Funcionario
     */
    Funcionario persistir(Funcionario funcionario);

    /**
     * Busca e retorna um funcionario dado um CPF
     * 
     * @param cpf
     * @return Optional<Funcionario>
     */
    Optional<Funcionario> buscaPorCpf(String cpf);

    /**
     * Busca e retorna um funcionário dado um email
     * 
     * @param email
     * @return Optional<Funcionario>
     */
    Optional<Funcionario> buscaPorEmail(String email);

    /**
     * Busca e retorna um funcionário por ID
     * 
     * @param id
     * @return Optional<Funcionario>
     */
    Optional<Funcionario> buscaPorId(Long id);
}