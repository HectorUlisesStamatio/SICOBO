package com.sicobo.sicobo.serviceImpl;

import com.sicobo.sicobo.dao.DaoCostType;
import com.sicobo.sicobo.dto.DTOCostType;
import com.sicobo.sicobo.model.BeanCostType;
import com.sicobo.sicobo.service.ICostTypeService;
import com.sicobo.sicobo.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Service
@Slf4j
public class CostTypeServiceImpl implements ICostTypeService {

    @Autowired
    private DaoCostType daoCostType;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> listar() {
        return new ResponseEntity(new Message("Usuario registrado","HOLA", "success",200, daoCostType.findAll()), HttpStatus.OK);
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> guardar(DTOCostType dtoCostType) {
        BeanCostType a = new BeanCostType();
        return new ResponseEntity(new Message("Usuario registrado","HOLA", "success",200,daoCostType.save(a)), HttpStatus.OK);
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> editar(DTOCostType dtoCostType) {
        BeanCostType a = new BeanCostType();
        return new ResponseEntity(new Message("Usuario registrado","HOLA", "success",200,daoCostType.save(a)), HttpStatus.OK);
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> eliminar(BeanCostType beanCostType) {
        return new ResponseEntity(new Message("Usuario registrado","HOLA", "success",200,daoCostType.save(beanCostType)), HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> buscar(Long id) {
        Optional<BeanCostType> optionalBeanCostType = daoCostType.findById(id);
        return new ResponseEntity(new Message("Usuario registrado","HOLA", "success",200,optionalBeanCostType), HttpStatus.OK);
    }


}
