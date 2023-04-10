package com.sicobo.sicobo.service;

import com.sicobo.sicobo.dto.DTOWarehousesType;
import com.sicobo.sicobo.model.BeanWarehousesType;
import org.springframework.http.ResponseEntity;

public interface IWarehousesType {

    public ResponseEntity<Object> listarTodas();
    public ResponseEntity<Object> listar();

    public ResponseEntity<Object> guardar(DTOWarehousesType dtoWarehousesType);

    public ResponseEntity<Object> editar(DTOWarehousesType dtoWarehousesType);

    public ResponseEntity<Object> eliminar(BeanWarehousesType beanWarehousesType);

    public ResponseEntity<Object> buscar(Long id);

}
