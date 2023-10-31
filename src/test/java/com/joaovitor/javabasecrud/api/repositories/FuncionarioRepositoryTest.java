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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.joaovitor.javabasecrud.api.entities.Empresa;
import com.joaovitor.javabasecrud.api.entities.Funcionario;
import com.joaovitor.javabasecrud.api.enums.PerfilEnum;
import com.joaovitor.javabasecrud.api.utils.PasswordUtils;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")

public class FuncionarioRepositoryTest {

    @Autowired 
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    private static final String EMAIL = "joaovitor@yahoo.com";
    private static final String CPF = "24291173474";

    @Before
    public void setUp() throws Exception {
        Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());
        this.funcionarioRepository.save(obterDadosFuncionario(empresa));
    }

    @After
    public final void teardDown(){
        this.empresaRepository.deleteAll();
    }

    @Test
    public void testBuscarFuncionarioPorEmail(){
        Funcionario funcionario = this.funcionarioRepository.findByEmail(EMAIL);

        assertEquals(EMAIL, funcionario.getEmail());
    }

    @Test
    public void testBuscarFuncionarioPorCpf(){  
        Funcionario funcionario = this.funcionarioRepository.findByCpf(CPF);

        assertEquals(CPF, funcionario.getCpf);
    }

    @Test
    public void testBuscarFuncionarioPorEmailECpf(){
        Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(CPF, EMAIL);

        assertNotNull(funcionario);
    }

    @Test
    public void testBuscarFuncionarioPorEmailOuCpfParaEmailInvalido() {
        Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(CPF, "email@invalido.com");

        assertNotNull(funcionario);
    }

    @Test
    public void testBuscarFuncionarioPorEmailECpfParaCpfInvalido (){
        Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(CPF, "email@invalido.com");

        assertNotNull(funcionario);
    }

    private Funcionario obterDadosFuncionario(Empresa empresa) {
        Funcionario funcionario = new Funcionario();

        funcionario.setNome("Joao");
        funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
        funcionario.setSenha(PasswordUtils.gerarBycrypt('123456'));
        funcionario.setCPf(CPF);
        funcionario.setEmail(EMAIL);
        funcionario.setEmpresa(empresa);

        return funcionario;
    }

    private Empresa obterDadosEmpresa(){
        Empresa empresa = new Empresa();

        empresa.setRazaoSocial('empresa');
        empresa.setCnpj('51463645000100');
        
        return empresa;
    }

}