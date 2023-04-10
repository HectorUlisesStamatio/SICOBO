package com.sicobo.sicobo.serviceimpl;

import com.sicobo.sicobo.dao.DaoCostType;
import com.sicobo.sicobo.dto.DTOCostType;
import com.sicobo.sicobo.model.BeanCostType;
import com.sicobo.sicobo.model.BeanWarehousesType;
import com.sicobo.sicobo.service.ICostTypeService;
import com.sicobo.sicobo.util.CostTypeValidator;
import com.sicobo.sicobo.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import static com.sicobo.sicobo.util.Constantes.MessageBody.*;
import static com.sicobo.sicobo.util.Constantes.MessageCodes.*;
import static com.sicobo.sicobo.util.Constantes.MessageHeaders.*;
import static com.sicobo.sicobo.util.Constantes.MessageType.*;
@Service
@Slf4j
public class CostTypeServiceImpl implements ICostTypeService {

    @Autowired
    private DaoCostType daoCostType;

    private CostTypeValidator costTypeValidator = new CostTypeValidator();

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> listar() {
        List<BeanCostType> costTypeList = daoCostType.findAllByStatusIs(1);
        return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE, costTypeList), HttpStatus.OK);
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> guardar(DTOCostType dtoCostType) {

        BeanCostType costTypeToSave = new BeanCostType();
        BeanWarehousesType warehousesTypeToSave = new BeanWarehousesType();
        costTypeToSave.setStatus(1);
        warehousesTypeToSave.setId((long) dtoCostType.getBeanWarehousesType());
        costTypeToSave.setBeanWarehousesType(warehousesTypeToSave);
        costTypeToSave.setAmount(dtoCostType.getAmount().doubleValue());

        if(costTypeValidator.validAmount(dtoCostType.getAmount())){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,"El monto es un valor inválido", FAILED,FAIL_CODE, null), HttpStatus.OK);
        }

        if(costTypeValidator.validWarehouseType(dtoCostType.getBeanWarehousesType())){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,"El tipo de bodega es un valor inválido", FAILED,FAIL_CODE, null), HttpStatus.OK);
        }

        Optional<BeanCostType> costTypeOptional = daoCostType.findBeanCostTypeByBeanWarehousesTypeIdAndStatusIs((long) dtoCostType.getBeanWarehousesType(), 1);

        if(costTypeOptional.isPresent()){
            BeanCostType costType = costTypeOptional.get();
            costType.setStatus(0);
            try{
                daoCostType.save(costType);
               BeanCostType costTypeResult =  daoCostType.save(costTypeToSave);
                return new ResponseEntity<>(new Message(SUCCESSFUL_REGISTRATION,INSERT_SUCCESSFUL, SUCCESS,SUCCESS_CODE, costTypeResult), HttpStatus.INTERNAL_SERVER_ERROR);

            }catch(Exception e){
                log.error("Ocurrio un error en CostTypeServiceImpl - guardar" + e.getMessage());
                return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            try{
                BeanCostType costTypeResult =  daoCostType.save(costTypeToSave);
                return new ResponseEntity<>(new Message(SUCCESSFUL_REGISTRATION,INSERT_SUCCESSFUL, SUCCESS,SUCCESS_CODE, costTypeResult), HttpStatus.INTERNAL_SERVER_ERROR);

            }catch(Exception e){
                log.error("Ocurrio un error en CostTypeServiceImpl - guardar" + e.getMessage());
                return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


    }

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> editar(DTOCostType dtoCostType) {
        BeanCostType a = new BeanCostType();
        return new ResponseEntity<>(new Message(SUCCESSFUL_REGISTRATION,INSERT_SUCCESSFUL, SUCCESS,SUCCESS_CODE,daoCostType.save(a)), HttpStatus.OK);
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> eliminar(BeanCostType beanCostType) {
        return new ResponseEntity<>(new Message(SUCCESSFUL_REGISTRATION,INSERT_SUCCESSFUL, SUCCESS,SUCCESS_CODE,daoCostType.save(beanCostType)), HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> buscar(Long id) {
        Optional<BeanCostType> optionalBeanCostType = daoCostType.findById(id);
        return new ResponseEntity<>(new Message(SUCCESSFUL_REGISTRATION,INSERT_SUCCESSFUL, SUCCESS,SUCCESS_CODE,optionalBeanCostType), HttpStatus.OK);
    }


}
