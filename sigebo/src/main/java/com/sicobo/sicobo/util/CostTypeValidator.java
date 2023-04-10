package com.sicobo.sicobo.util;

public class CostTypeValidator {


    public boolean validAmount(Double amount){
        boolean flag = false;
        if(amount == null){
            flag = true;
        }else if(amount < 1){
            flag = true;
        }

        return flag;
    }

    public boolean validWarehouseType(int warehouseType){
        boolean flag = false;
        if(warehouseType == 0){
            flag = true;
        }
        return flag;
    }

}
