package com.sicobo.sicobo.service;

import com.sicobo.sicobo.model.BeanWarehouse;
import org.springframework.http.ResponseEntity;

public interface IStripeService {

    public ResponseEntity<Object> checkout(BeanWarehouse beanWarehouse);

    public ResponseEntity<Object> paymentIntent();
}
