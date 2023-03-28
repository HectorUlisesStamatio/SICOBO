package com.sicobo.sicobo.serviceimpl;

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

import java.util.List;

import static com.sicobo.sicobo.util.Constantes.MessageBody.INTERNAL_ERROR;
import static com.sicobo.sicobo.util.Constantes.MessageBody.SEARCH_SUCCESSFUL;
import static com.sicobo.sicobo.util.Constantes.MessageType.FAILED;
import static com.sicobo.sicobo.util.Constantes.MessageType.SUCCESS;
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
        try{
            List<BeanWarehousesType> beanWarehousesTypes = daoWarehousesType.findAllByBeanCostTypeIsNotNull();
            assert beanWarehousesTypes != null;

            if(!beanWarehousesTypes.isEmpty()){
                return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE,beanWarehousesTypes), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new Message(FAILED_SEARCH,"No hay registros de tipos de bodegas", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
            }
        }catch(Exception e){
            log.error("Ocurrió un error en  WarehouseTypeServiceImpl - listar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION, INTERNAL_ERROR, FAILED, SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

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
