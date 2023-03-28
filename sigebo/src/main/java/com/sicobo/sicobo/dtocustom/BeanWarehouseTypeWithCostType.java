package com.sicobo.sicobo.dtocustom;

import com.sicobo.sicobo.model.BeanCostType;
import com.sicobo.sicobo.model.BeanWarehousesType;
import lombok.Data;

public class BeanWarehouseTypeWithCostType {

    private BeanWarehousesType beanWarehousesType;

    private BeanCostType beanCostType;

    public BeanWarehouseTypeWithCostType() {

    }

    public BeanWarehouseTypeWithCostType(BeanWarehousesType beanWarehousesType, BeanCostType beanCostType) {
        this.beanWarehousesType = beanWarehousesType;
        this.beanCostType = beanCostType;
    }

    public BeanWarehousesType getBeanWarehousesType() {
        return beanWarehousesType;
    }

    public void setBeanWarehousesType(BeanWarehousesType beanWarehousesType) {
        this.beanWarehousesType = beanWarehousesType;
    }

    public BeanCostType getBeanCostType() {
        return beanCostType;
    }

    public void setBeanCostType(BeanCostType beanCostType) {
        this.beanCostType = beanCostType;
    }
}
