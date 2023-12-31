package com.joaovitor.javabasecrud.api.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joaovitor.javabasecrud.api.dtos.LancamentoDto;
import com.joaovitor.javabasecrud.api.entities.Funcionario;
import com.joaovitor.javabasecrud.api.entities.Lancamento;
import com.joaovitor.javabasecrud.api.enums.TipoEnum;
import com.joaovitor.javabasecrud.api.response.Response;
import com.joaovitor.javabasecrud.api.services.FuncionarioService;
import com.joaovitor.javabasecrud.api.services.LancamentoService;

@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin(origins = "*")
public class LancamentoController {

    private static final Logger log = LoggerFactory.getLogger(LancamentoController.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Value("${paginacao.qtd_por_pagina}")
    private int qtdPorPagina;

    public LancamentoController(){}

    /**
     * Retorna a listagem de lancamentos de um funcionario
     * 
     * @param funcionarioId
     * @return ResponseEntity<Response<LancamentoDto>>
     */
    @GetMapping(value = "/funcionario/{funcionarioId}")
    public ResponseEntity<Response<Page<LancamentoDto>>> listarPorFuncionarioId(
        @PathVariable("funcionarioId") Long funcionarioId,
        @RequestParam(value = "pag", defaultValue = "0") int pag,
        @RequestParam(value = "ord", defaultValue = "id") String ord,
        @RequestParam(value = "dir", defaultValue = "DESC") String dir
    ){
        log.info("Buscando lancamentos por ID do funcionario: {}, página {}", funcionarioId, pag);

        Response<Page<LancamentoDto>> response = new Response<Page<LancamentoDto>>();
        PageRequest pageRequest = new PageRequest(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
        Page<Lancamento> lancamentos = this.lancamentosService.buscarPorFuncionarioId(funcionarioId, pageRequest);
        Page<LancamentoDto> lancamentosDto = lancamentos.map(lancamento -> this.converterLancamentoDto(lancamento));

        response.setData(lancamentosDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Retorna um lancamentro por ID
     * 
     * @param id
     * @return ResponseEntity<Response<LancamentoDto>>
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Reponse<LancamentoDto>> listarPorId(@PathVariable("id") Long id) {

        log.info("Buscando lancamentos por ID: {}", id);

        Response<LancamentoDto> response = new Response<LancamentoDto>();
        Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

        if(!lancamento.isPresent()){
            log.info("Lancamento não encontrado para esse ID: {}", id);
            response.getErrors().add("Lançamento não encontrado para o id " + id);
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.converterLancamentoDto(lancamento.get()));
        return ResponseEntity.ok(response);

    }

    /**
     * Adicionar um novo lancamento
     * 
     * @param lancamento
     * @param result
     * @param ResponseEntity<Response<LancamentoDto>>
     * @throws ParseException 
     */
    @PostMapping ResponseEntity<Response<LancamentoDto>> adicionar(@Valid @RequestBody LancamentoDto lancamentoDto,
        BindingResult result
    ) throws ParseException {

        log.info("Adicionando lançamento: {}", lancamentoDto.toString());

        Response<LancamentoDto> response = new Response<LancamentoDto>();
        validarFuncionario(lancamentoDto, result);

        Lancamento lancamento = this.converterLancamentoDto(lancamentoDto, result);

        if(result.hasErrors()){
            log.error("Erro validando lancamento: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        lancamento = this.lancamentoService.persistir(lancamento);
        responde.setData(this.converterLancamentoDto(lancamento));
        return ResponseEntity.ok(response);
    }

    /**
     * Remove um lancamento por ID
     * 
     * @param id
     * @return ResponseEntity<Response<Lancamento>>
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id){

        log.info("Removendo lançamento: {}", id);

        Response<String> response = new Response<String>();
        Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

        if(!lancamento.isPresent){
            log.info("Erro ao remover devido ao lancamento ID: {} ser inválido", id);
            response.getErrors().addError("Error ao remover lancamento. Registro não encontrado para o id" + id);
            return ResponseEntity.ok(new Reponse<String>());
        }

        this.lancamentoService.remover(id);
        return ResponseEntity.ok(new Response<String>());
    }
    /**
     * Valida um funcionario, verificando se ele é existente no sistema
     * 
     * @param lancamentoDto
     * @param result
     */
    private void validarFuncionario(LancamentoDto lancamentoDto, BindingResult result){

        if(lancamentoDto.getFuncionarioId() === null){
            result.addError(new ObjectError("funcionario", "Funcionário não informado"));
            return;
        }

        log.info("Validando funcionario id: {}", lancamentoDto.getFuncionarioId());
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(lancamentoDto.getFuncionarioId());

        if(!funcionario.isPresent()){
            result.addError(new ObjectError("funcionario", "Funcionario não encontrado. ID inexistente"));
        }
    }

    /**
     * Converte uma entidade lancamento para seu respectivo DTO
     * 
     * @param lancamento
     * @return LancamentoDto
     */
    private LancamentoDto converterLancamentoDto(Lancamento lancamento){
        LancamentoDto lancamentoDto = new LancamentoDto();

        lancamentoDto.setId(Optional.of(lancamento.getId()));
        lancamentoDto.setData(this.dateFormat.format(lancamento.getData()));
		lancamentoDto.setTipo(lancamento.getTipo().toString());
		lancamentoDto.setDescricao(lancamento.getDescricao());
		lancamentoDto.setLocalizacao(lancamento.getLocalizacao());
		lancamentoDto.setFuncionarioId(lancamento.getFuncionario().getId());

        return lancamentoDto;
    }
    /**
     * Converte um LancamentoDto para uma entidade Lancamento
     * 
     * @param lancamentoDto
     * @param result
     * @return Lancamento
     * @throws ParseException
     */
    private Lancamento converterDtoParaLancamento(LancamentoDto lancamentoDto, BindingResult result)
        throws ParseException {

            Lancamento lancamento = new Lancamento();

            if(lancamentoDto.getId().isPresent()){
                Optional<Lancamento> lanc = this.lancamentoService.buscarPorId(lancamentoDto.getId().get());

                if(lanc.isPresent()){
                    lancamento = lanc.get();
                } else {
                    result.addError(new ObjectError("lancamento", "Lancamento nao encontrado"));
                }

            } else {

                lancamento.setFuncionario(new Funcionario());
                lancamento.getFuncionario().setId(lancamentoDto.getFuncionarioId());

            }
        
        lancamento.setDescricao(lancamentoDto.getDescricao());
        lancamento.setLocalizacao(lancamentoDto.getLocalizacao());
        lancamento.setData(this.dateFormat.parse(lancamentoDto.getData()));

        if(EnumUtils.isValidEnum(TipoEnum.class, lancamentoDto.getTipo())){
            lancamento.setTipo(TipoEnum.valueOf(lancamentoDto.getTipo()));
        } else {
            result.addError(new ObjectError("tipo","Tipo invalido"))
        }

        return lancamento;
    }
}