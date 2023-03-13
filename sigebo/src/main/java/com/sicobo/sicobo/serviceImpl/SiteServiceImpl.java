package com.sicobo.sicobo.serviceImpl;

import com.sicobo.sicobo.dao.DaoSite;
import com.sicobo.sicobo.dto.DTOSite;
import com.sicobo.sicobo.model.BeanSite;
import com.sicobo.sicobo.service.ISiteService;
import com.sicobo.sicobo.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SiteServiceImpl implements ISiteService {

    @Autowired
    private DaoSite daoSite;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> listar() {
        return new ResponseEntity(new Message("Usuario registrado","HOLA", "success",200, daoSite.findAll()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> guardar(DTOSite dtoSite) {
        return null;
    }

    @Override
    public ResponseEntity<Object> editar(DTOSite dtoSite) {
        return null;
    }

    @Override
    public ResponseEntity<Object> eliminar(BeanSite beanSite) {
        return null;
    }

    @Override
    public ResponseEntity<Object> buscar(Long id) {
        return null;
    }


}
