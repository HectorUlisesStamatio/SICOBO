package com.sicobo.sicobo.util;

public class WarehouseValidator {

    public boolean validState(Long state){
        boolean flag = false;
        if(state == 0){
            flag = true;
        }
        return flag;
    }
}
