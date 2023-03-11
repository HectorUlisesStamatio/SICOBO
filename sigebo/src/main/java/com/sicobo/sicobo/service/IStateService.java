package com.sicobo.sicobo.service;

import com.sicobo.sicobo.dto.DTOState;
import com.sicobo.sicobo.model.BeanState;
import org.springframework.http.ResponseEntity;


public interface IStateService {
    public ResponseEntity<Object> listar();

    public ResponseEntity<Object> guardar(DTOState dtoState);

    public ResponseEntity<Object> editar(DTOState dtoState);

    public ResponseEntity<Object> eliminar(BeanState beanState);

    public ResponseEntity<Object> buscar(Long id);
}
