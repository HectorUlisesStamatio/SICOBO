package com.sicobo.sicobo.service;

import org.springframework.http.ResponseEntity;

public interface IWarehouseDetails {
    public ResponseEntity<Object> listar(String parametroUno,String parametroDos);
}
