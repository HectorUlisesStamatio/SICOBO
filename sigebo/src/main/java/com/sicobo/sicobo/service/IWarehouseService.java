package com.sicobo.sicobo.service;

import com.sicobo.sicobo.dto.DTOWarehouse;
import com.sicobo.sicobo.model.BeanWarehouse;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface IWarehouseService {
    public ResponseEntity<List<Object[]>> listar(Long id, String username);

    public ResponseEntity<Object> guardar(DTOWarehouse dtoWarehouse);

    public ResponseEntity<Object> editar(DTOWarehouse dtoWarehouse);

    public ResponseEntity<Object> eliminar(BeanWarehouse beanWarehouse);

    public ResponseEntity<Object> buscar(Long id);

}
