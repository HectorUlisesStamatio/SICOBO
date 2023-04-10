package com.sicobo.sicobo.serviceimpl;


import com.sicobo.sicobo.dao.DaoPolicies;
import com.sicobo.sicobo.dto.DTOPolicies;
import com.sicobo.sicobo.model.BeanPolicies;
import com.sicobo.sicobo.service.IPoliciesService;
import com.sicobo.sicobo.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

import static com.sicobo.sicobo.util.Constantes.MessageBody.*;
import static com.sicobo.sicobo.util.Constantes.MessageCodes.SERVER_FAIL_CODE;
import static com.sicobo.sicobo.util.Constantes.MessageCodes.SUCCESS_CODE;
import static com.sicobo.sicobo.util.Constantes.MessageHeaders.*;
import static com.sicobo.sicobo.util.Constantes.MessageType.FAILED;
import static com.sicobo.sicobo.util.Constantes.MessageType.SUCCESS;

@Service
@Slf4j
public class PoliciesServiceImpl implements IPoliciesService {

    @Autowired
    private DaoPolicies daoPolicies;

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> guardar(DTOPolicies dtoPolicies){
        BeanPolicies beanPolicies = new BeanPolicies();
        beanPolicies.setDescription(dtoPolicies.getDescription());
        beanPolicies.setStatus(1);
        try{
            Optional<BeanPolicies> beanPoliciesOptional = daoPolicies.findByStatusIs(1);

            if (beanPoliciesOptional.isPresent()){
                BeanPolicies beanPoliciesToUpdate = beanPoliciesOptional.get();
                beanPoliciesToUpdate.setStatus(0);
                daoPolicies.save(beanPoliciesToUpdate);
            }
            daoPolicies.save(beanPolicies);
            return new ResponseEntity<>(new Message(SUCCESSFUL_REGISTRATION,INSERT_SUCCESSFUL, SUCCESS,SUCCESS_CODE, beanPolicies), HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(Exception e){
            log.error("Ocurrio un error en PoliciesServiceImpl - guardar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    public ResponseEntity<Object> listar() {
        Optional<BeanPolicies> beanPolicies = daoPolicies.findByStatusIs(1);
        if (beanPolicies.isPresent()){
            return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE, beanPolicies.get()), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE, new BeanPolicies()), HttpStatus.OK);
        }


    }
    
    @Override
    public ResponseEntity<Object> editar(DTOPolicies dtoPolicies) {
        return null;
    }

    @Override
    public ResponseEntity<Object> eliminar(BeanPolicies beanPolicies) {
        return null;
    }

    @Override
    public ResponseEntity<Object> buscar(Long id) {
        return null;
    }
}
