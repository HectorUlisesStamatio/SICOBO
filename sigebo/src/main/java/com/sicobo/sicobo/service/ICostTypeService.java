package com.sicobo.sicobo.service;

import com.sicobo.sicobo.dto.DTOCostType;
import com.sicobo.sicobo.model.BeanCostType;
import org.springframework.http.ResponseEntity;


public interface ICostTypeService {

    public ResponseEntity<Object> listar();

    public ResponseEntity<Object> guardar(DTOCostType dtoCostType);

    public ResponseEntity<Object> editar(DTOCostType dtoCostType);

    public ResponseEntity<Object> eliminar(BeanCostType beanCostType);

    public ResponseEntity<Object> buscar(Long id);
}
