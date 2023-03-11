package com.sicobo.sicobo.service;

import com.sicobo.sicobo.dto.DTOPayment;
import com.sicobo.sicobo.model.BeanPayment;
import org.springframework.http.ResponseEntity;


public interface IPaymentService {
    public ResponseEntity<Object> listar();

    public ResponseEntity<Object> guardar(DTOPayment dtoPayment);

    public ResponseEntity<Object> editar(DTOPayment dtoPayment);

    public ResponseEntity<Object> eliminar(BeanPayment beanPayment);

    public ResponseEntity<Object> buscar(Long id);
}
