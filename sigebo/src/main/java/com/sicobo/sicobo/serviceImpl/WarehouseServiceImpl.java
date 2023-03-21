package com.sicobo.sicobo.serviceImpl;

import com.sicobo.sicobo.dao.DaoWarehouse;
import com.sicobo.sicobo.dto.DTOWarehouse;
import com.sicobo.sicobo.model.BeanWarehouse;
import com.sicobo.sicobo.service.IWarehouseService;
import com.sicobo.sicobo.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class WarehouseServiceImpl implements IWarehouseService {

    @Autowired
    DaoWarehouse daoWarehouse;

    @Override
    public ResponseEntity<Object> listar() {
        return null;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> listWarehousesBySiteId(Long idSite) {
        return new ResponseEntity(new Message("Consulta exitosa","La consulta de bodegas ha sido exitosa", "success",200, daoWarehouse.findAllByBeanSite_Id(idSite)), HttpStatus.OK);
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
