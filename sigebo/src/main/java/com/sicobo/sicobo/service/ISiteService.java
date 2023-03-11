package com.sicobo.sicobo.service;

import com.sicobo.sicobo.dto.DTOSite;
import com.sicobo.sicobo.model.BeanSite;
import org.springframework.http.ResponseEntity;


public interface ISiteService {
    public ResponseEntity<Object> listar();

    public ResponseEntity<Object> guardar(DTOSite dtoSite);

    public ResponseEntity<Object> editar(DTOSite dtoSite);

    public ResponseEntity<Object> eliminar(BeanSite beanSite);

    public ResponseEntity<Object> buscar(Long id);
}
