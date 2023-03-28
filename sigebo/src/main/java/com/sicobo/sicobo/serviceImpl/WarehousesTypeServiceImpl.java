package com.sicobo.sicobo.serviceImpl;

import com.sicobo.sicobo.dao.DaoWarehousesType;
import com.sicobo.sicobo.dto.DTOWarehousesType;
import com.sicobo.sicobo.model.BeanWarehousesType;
import com.sicobo.sicobo.service.IWarehousesType;
import com.sicobo.sicobo.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sicobo.sicobo.util.Constantes.MessageBody.SEARCH_SUCCESSFUL;
import static com.sicobo.sicobo.util.Constantes.MessageType.FAILED;
import static com.sicobo.sicobo.util.Constantes.MessageType.SUCCESS;
import static com.sicobo.sicobo.util.Constantes.Redirects.*;
import static com.sicobo.sicobo.util.Constantes.MessageCodes.*;
import static com.sicobo.sicobo.util.Constantes.Roles.ROLE_GESTOR;
import static com.sicobo.sicobo.util.Constantes.Stuff.*;
import static com.sicobo.sicobo.util.Constantes.MessageCodes.*;
import static com.sicobo.sicobo.util.Constantes.MessageHeaders.*;

@Service
@Slf4j
public class WarehousesTypeServiceImpl implements IWarehousesType {

    @Autowired
    DaoWarehousesType daoWarehousesType;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> listar() {
        return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE, daoWarehousesType.findAll()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> guardar(DTOWarehousesType dtoWarehousesType) {
        return null;
    }

    @Override
    public ResponseEntity<Object> editar(DTOWarehousesType dtoWarehousesType) {
        return null;
    }

    @Override
    public ResponseEntity<Object> eliminar(BeanWarehousesType beanWarehousesType) {
        return null;
    }

    @Override
    public ResponseEntity<Object> buscar(Long id) {
        return null;
    }
}
