package com.sicobo.sicobo.serviceimpl;

import com.sicobo.sicobo.dao.DaoSiteAssigment;
import com.sicobo.sicobo.dao.DaoUser;
import com.sicobo.sicobo.dao.DaoWarehouse;
import com.sicobo.sicobo.dto.DTOWarehouse;
import com.sicobo.sicobo.model.BeanSite;
import com.sicobo.sicobo.model.BeanWarehouse;
import com.sicobo.sicobo.service.IWarehouseService;
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
import static com.sicobo.sicobo.util.Constantes.MessageCodes.*;
import static com.sicobo.sicobo.util.Constantes.MessageHeaders.*;
import static com.sicobo.sicobo.util.Constantes.MessageType.FAILED;
import static com.sicobo.sicobo.util.Constantes.MessageType.SUCCESS;

@Service
@Slf4j
public class WarehouseServiceImpl implements IWarehouseService {

    @Autowired
    DaoWarehouse daoWarehouse;

    @Autowired
    DaoUser daoUser;

    @Autowired
    DaoSiteAssigment daoSiteAssigment;

    @Override
    public ResponseEntity<Object> listar() {
        return null;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> findWarehousesByBeanSite(BeanSite beanSite) {
        if(!daoWarehouse.existsBeanWarehouseByBeanSite(beanSite)){
            return new ResponseEntity<>(new Message(FAILED_SEARCH,"El sitio no cuenta con bodegas", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        try{
            List<BeanWarehouse> beanWarehouses = daoWarehouse.findBeanWarehouseByBeanSite(beanSite);
            assert beanWarehouses != null;
            return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE,beanWarehouses), HttpStatus.OK);
        }catch(Exception e){
            log.error("Ocurrió un error al buscar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION, INTERNAL_ERROR, FAILED, SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<Object> guardar(DTOWarehouse dtoWarehouse) {
        return null;
    }

    @Override
    public ResponseEntity<Object> editar(DTOWarehouse dtoWarehouse) {
        return null;
    }

    @Override
    public ResponseEntity<Object> eliminar(BeanWarehouse beanWarehouse) {
        return null;
    }

    @Override
    public ResponseEntity<Object> buscar(Long id) {
        return null;
    }
}
