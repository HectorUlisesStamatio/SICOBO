package com.sicobo.sicobo.util;

public class SiteValidator {

    private static final String REGEX_FIELD_EMPTY = "^[\\s.\\-_]*$";

    private static final String REGEX_NAME_INCORRECT = "^(?!\\s)(?!.*\\s$)[a-zA-Z0-9#%&.,áéíóúÁÉÍÓÚüÜñÑ\\s]*$";

    private static final int SIZE_MAX_NAME = 255;

    private static final int SIZE_MIN_NAME = 3;

    private static final String REGEX_ADDRESS_INCORRECT = "^(?!\\s)(?!.*\\s$)[a-zA-Z0-9#%&.,áéíóúÁÉÍÓÚüÜñÑ\\s]*$";

    private static final int SIZE_MAX_ADDRESS = 255;

    private static final int SIZE_MIN_ADDRESS = 10;



    public boolean validName(String nombre){
        boolean flag = false;
        if(nombre == null || nombre.matches(REGEX_FIELD_EMPTY)){
            flag = true;
        }
        if(!nombre.matches(REGEX_NAME_INCORRECT)){
            flag = true;
        }
        if(nombre.length() > SIZE_MAX_NAME){
            flag = true;
        }
        if(nombre.length() < SIZE_MIN_NAME ){
            flag = true;
        }

        return flag;
    }

    public boolean validAddress(String address){
        boolean flag = false;
        if(address == null || address.matches(REGEX_FIELD_EMPTY)){
            flag = true;
        }
        if(!address.matches(REGEX_ADDRESS_INCORRECT)){
            flag = true;
        }
        if(address.length() > SIZE_MAX_ADDRESS){
            flag = true;
        }
        if(address.length() < SIZE_MIN_ADDRESS ){
            flag = true;
        }
        return flag;
    }

    public boolean validState(Long state){
        boolean flag = false;
        if(state == 0){
            flag = true;
        }
        return flag;
    }


}
