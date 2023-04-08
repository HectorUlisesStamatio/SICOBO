package com.sicobo.sicobo.service;

import com.sicobo.sicobo.model.BeanUser;
import com.sicobo.sicobo.model.BeanWarehouse;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.http.ResponseEntity;

public interface IStripeService {

    public ResponseEntity<Object> checkout(BeanWarehouse beanWarehouse, int meses);

    public ResponseEntity<Object> paymentIntent(Session session, BeanUser user) throws StripeException;
}
