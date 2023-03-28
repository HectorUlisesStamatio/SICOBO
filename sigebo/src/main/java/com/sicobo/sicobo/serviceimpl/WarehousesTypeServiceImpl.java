package com.sicobo.sicobo.serviceimpl;

import com.sicobo.sicobo.dao.DaoCostType;
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

import java.util.List;

import static com.sicobo.sicobo.util.Constantes.MessageBody.*;
import static com.sicobo.sicobo.util.Constantes.MessageCodes.*;
import static com.sicobo.sicobo.util.Constantes.MessageHeaders.*;
import static com.sicobo.sicobo.util.Constantes.MessageType.*;
@Service
@Slf4j
public class WarehousesTypeServiceImpl implements IWarehousesType {

    @Autowired
    private DaoWarehousesType daoWarehousesType;
    @Autowired
    private DaoCostType daoCostType;

    @Override
    public ResponseEntity<Object> listar() {
        List<BeanWarehousesType> beanWarehousesTypes = daoWarehousesType.findAll();
        return new ResponseEntity(new Message(SUCCESSFUL_SEARCH, SEARCH_SUCCESSFUL, SUCCESS, SUCCESS_CODE, beanWarehousesTypes), HttpStatus.OK);
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
