package com.sicobo.sicobo.serviceimpl;

import com.sicobo.sicobo.dao.DaoPayment;
import com.sicobo.sicobo.dao.DaoUser;
import com.sicobo.sicobo.dto.DTOPayment;
import com.sicobo.sicobo.model.BeanPayment;
import com.sicobo.sicobo.model.BeanUser;
import com.sicobo.sicobo.model.BeanWarehouse;
import com.sicobo.sicobo.service.IPaymentService;
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
import static com.sicobo.sicobo.util.Constantes.MessageCodes.*;
import static com.sicobo.sicobo.util.Constantes.MessageHeaders.*;
import static com.sicobo.sicobo.util.Constantes.MessageType.*;


@Service
@Slf4j
public class PaymentServiceImpl  implements IPaymentService {

    @Autowired
    private DaoPayment daoPayment;

    @Autowired
    private DaoUser daoUser;

    @Override
    public ResponseEntity<Object> listar() {
        return null;
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> guardar(DTOPayment dtoPayment) {
        try {
            if (dtoPayment.getPaymentId() == null) {
                return new ResponseEntity<>(new Message(FAILED_EXECUTION, "Valor inválido en el pago", FAILED, FAIL_CODE, null), HttpStatus.OK);
            }
            if (dtoPayment.getPaymentDate() == null) {
                return new ResponseEntity<>(new Message(FAILED_EXECUTION, "Valor inválido en la fecha de pago", FAILED, FAIL_CODE, null), HttpStatus.OK);
            }
            if (dtoPayment.getAmount() == 0) {
                return new ResponseEntity<>(new Message(FAILED_EXECUTION, "Valor inválido en el monto del pago", FAILED, FAIL_CODE, null), HttpStatus.OK);
            }
            if (dtoPayment.getAmountMonths() == 0) {
                return new ResponseEntity<>(new Message(FAILED_EXECUTION, "Valor inválido en la cantidad de meses del pago", FAILED, FAIL_CODE, null), HttpStatus.OK);
            }
            if (dtoPayment.getBeanUser() == 0) {
                return new ResponseEntity<>(new Message(FAILED_EXECUTION, "Valor inválido en el cliente del pago", FAILED, FAIL_CODE, null), HttpStatus.OK);
            }
            if (dtoPayment.getBeanWarehouse() == 0) {
                return new ResponseEntity<>(new Message(FAILED_EXECUTION, "Valor inválido en la bodega del pago", FAILED, FAIL_CODE, null), HttpStatus.OK);
            }
            if (dtoPayment.getDueDate() == null) {
                return new ResponseEntity<>(new Message(FAILED_EXECUTION, "Valor inválido en la fecha de vencimiento del pago", FAILED, FAIL_CODE, null), HttpStatus.OK);
            }

            BeanPayment beanPayment = new BeanPayment();
            beanPayment.setPaymentId(dtoPayment.getPaymentId());
            BeanUser beanUser = new BeanUser();
            beanUser.setId(dtoPayment.getBeanUser());
            beanPayment.setBeanUser(beanUser);
            beanPayment.setAmount((dtoPayment.getAmount() / 100));
            beanPayment.setPaymentDate(dtoPayment.getPaymentDate());
            beanPayment.setAmountMonths(dtoPayment.getAmountMonths());
            BeanWarehouse beanWarehouse = new BeanWarehouse();
            beanWarehouse.setId((long) dtoPayment.getBeanWarehouse());
            beanPayment.setBeanWarehouse(beanWarehouse);
            beanPayment.setDueDate(dtoPayment.getDueDate());
            beanPayment.setStatus(dtoPayment.getStatus());

            BeanPayment paymentSaved = daoPayment.save(beanPayment);
            Optional<BeanPayment> paymentSearched = daoPayment.findById(paymentSaved.getId());
            Optional<BeanUser> userSearched = daoUser.findById(paymentSearched.get().getBeanUser().getId());
            System.out.println(userSearched.get());
            paymentSearched.get().setBeanUser(userSearched.get());
            return new ResponseEntity<>(new Message(SUCCESSFUL_REGISTRATION, INSERT_SUCCESSFUL, SUCCESS, SUCCESS_CODE, paymentSearched.get()), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Override
    public ResponseEntity<Object> editar(DTOPayment dtoPayment) {
        return null;
    }

    @Override
    public ResponseEntity<Object> eliminar(BeanPayment beanPayment) {
        return null;
    }

    @Override
    public ResponseEntity<Object> buscar(Long id) {
        return null;
    }
}
