package com.sicobo.sicobo.service;

import com.sicobo.sicobo.dto.DTOWarehouseImage;
import com.sicobo.sicobo.model.BeanWarehouseImage;
import org.springframework.http.ResponseEntity;

public interface IWarehouseImageService {
    public ResponseEntity<Object> listar();

    public ResponseEntity<Object> guardar(DTOWarehouseImage dtoWarehouseImage);

    public ResponseEntity<Object> editar(DTOWarehouseImage dtoWarehouseImage);

    public ResponseEntity<Object> eliminar(BeanWarehouseImage beanWarehouseImage);

    public ResponseEntity<Object> buscar(Long id);
}
