package com.joaovitor.javabasecrud.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    @Transaction(readOnly = true)
    Empresa findByCnpj(String cnpj)

}