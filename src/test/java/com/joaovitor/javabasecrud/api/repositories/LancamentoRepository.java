package com.joaovitor.javabasecrud.api.respositories;

import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.joaovitor.javabasecrud.api.entities.Empresa;
import com.joaovitor.javabasecrud.api.entities.Funcionario;
import com.joaovitor.javabasecrud.api.entities.Lancamento;
import com.joaovitor.javabasecrud.api.enums.PerfilEnum;
import com.joaovitor.javabasecrud.api.enums.TipoEnum;
import com.joaovitor.javabasecrud.api.utils.PasswordUtils;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")

public class LancamentoRepositoryTest {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired 
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;


    private Long funcionariId;

    @Before
    public void setUp() throws Exception {
        Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());

        Funcionario funcionario = this.funcionarioRepository.save(obterDadosFuncionario(empresa));
        this.funcionariId = funcionario.getId();

        this.lancamentoRepository.save(obterDadosLancamentos(funcionario));
     }

    @After
    public void teardDown() throws Exception {
        this.empresaRepository.deleteAll();
    }

    @Test
    public void testBuscarLancamentosPorFuncionarioId(){
        List<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionariId);

        assertEquals(1, lancamentos.size());
    }

    @Test
    public void testBuscarLancamentosPorFuncionarioIdPaginado () {
        PageRequest page = new PageRequest(0,10);

        Page<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionariId,page);

        assertEquals(1, lancamentos.getTotalElements());
    }

    private Lancamento obterDadosLancamentos(Funcionario funcionario){
        Lancamento lancamento = new Lancamento();
        lancamento.setData(new Date());
        lancamento.setTipo(TipoEnum.INICIO_ALMOCO);
        lancamento.setFuncionario(funcionario);

        return lancamento;
    }

    private Funcionario obterDadosFuncionario(Empresa empresa) throws NoSuchAlgorithmException {
        Funcionario funcionario = new Funcionario();

        funcionario.setNome('João Lopes');
        funcionario.setPerfil(PerfilEnum.TOLE_USUARIO);
        funcionario.setSenha(PasswordUtils.gerarBycrypt("123456"));
        funcionario.setCpf("24291173474");
        funcionario.setEmail('joaovitormunizlopes@yahoo.com');
        funcionario.setEmpresa(empresa);

        return funcionario;
    }


    private Empresa ObterDadosEmpresa(){
        Empresa empresa = new Empresa();

        empresa.setRazaoSocial("Empresa de exemplo");
        empresa.setCnpj("51463645000100");

        return empresa
    }
}