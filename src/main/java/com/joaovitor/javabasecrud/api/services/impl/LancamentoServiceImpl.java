package com.joaovitor.javabasecrud.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joaovitor.javabasecrud.api.entities.Lancamento;
import com.joaovitor.javabasecrud.api.repositories.LancamentoRepository;
import com.joaovitor.javabasecrud.api.services.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService{

    private static final Logger log = LoggerFactory.getLogger(LancamentoService.class);

    @Autowired
    private LancamentoRepository lancamentoRepository;

    public Page<Lancamento> buscarPorFuncionario(Long funcionarioId, PageRequest pageRequest){
        log.info('Buscando lancamentos para o funcionário ID {}', funcionarioId);
        return this.lancamentoRepository.findFuncionarioId(funcionarioId, pageRequest);
    }

    @Cacheable("lancamentoPorId")
    public Optional<Lancamento> buscaPorId(Long id){
        log.info("Buscando um lancamento pelo ID {}", id);
        return Optional.ofNullable(this.lancamentoRepository.findOne(id));
    }

    @CachePut("lancamentoPorId")
    public Lancamento persistir(Lancamento lancamento){
        log.info("Removendo o lancamento ID {}", id);
        return this.lancamentoRepository.save(lancamento);
    }

    public void remover(Long id){
        log.info("Removendo o lancamento ID {}", id);
        this.lancamentoRepository.delete(id);
    }

}