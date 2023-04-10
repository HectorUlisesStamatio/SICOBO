package com.sicobo.sicobo.serviceimpl;

import com.sicobo.sicobo.dao.DaoState;
import com.sicobo.sicobo.dto.DTOState;
import com.sicobo.sicobo.model.BeanState;
import com.sicobo.sicobo.service.IStateService;
import com.sicobo.sicobo.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class StateServiceImpl implements IStateService {

    @Autowired
    private DaoState daoState;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> listar() {
        return new ResponseEntity<>(new Message("Consulta exitosa","La consulta de estados ha sido exitosa", "success",200, daoState.findAll()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> guardar(DTOState dtoState) {
        return null;
    }

    @Override
    public ResponseEntity<Object> editar(DTOState dtoState) {
        return null;
    }

    @Override
    public ResponseEntity<Object> eliminar(BeanState beanState) {
        return null;
    }

    @Override
    public ResponseEntity<Object> buscar(Long id) {
        return null;
    }
}
