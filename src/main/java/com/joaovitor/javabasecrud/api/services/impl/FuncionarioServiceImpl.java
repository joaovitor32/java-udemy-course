package com.joaovitor.javabasecrud.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joaovitor.javabasecrud.api.entities.Funcionario;
import com.joaovitor.javabasecrud.api.repositories.FuncionarioRepository;
import com.joaovitor.javabasecrud.api.services.FuncionarioService;

@Service
public class FuncionarioServiceImpl implements FuncionarioService{

    private static final Logger log = LoggerFactory.getLogger(FuncionarioService.class);

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public Funcionario persistir(Funcionario funcionario){
        log.info('Persistindo funcionario: {}', funcionario);
        return this.funcionarioRepository.save(funcionario);
    } 

    public Optional<Funcionario> buscarPorCpf(String cpf){
        log.info('Buscando funcionario pelo cpf {}', cpf);
        return Optional.ofNullable(this.funcionarioRepository.findByCpf(cpf));
    }

    public Optional<Funcionario> buscaPorEmail(String email){
        log.info('Buscando funcionario pelo email {}', email);
        return Optional.ofNullable(this.funcionarioRepository.findByEmail(email));
    }

    public Optional<Funcionario> buscarPorId(Long id){
        log.info('Buscando funcionario pelo IDL {}', id);
        return Optional.ofNullable(this.funcionarioRepository.findOne(id));
    }
}