package com.sicobo.sicobo.service;

import com.sicobo.sicobo.dto.DTOWarehousesType;
import com.sicobo.sicobo.model.BeanWarehousesType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IWarehousesType {
    public ResponseEntity<Object> listar();

    public ResponseEntity<Object> guardar(DTOWarehousesType dtoWarehousesType);

    public ResponseEntity<Object> editar(DTOWarehousesType dtoWarehousesType);

    public ResponseEntity<Object> eliminar(BeanWarehousesType beanWarehousesType);

    public ResponseEntity<Object> buscar(Long id);
}
