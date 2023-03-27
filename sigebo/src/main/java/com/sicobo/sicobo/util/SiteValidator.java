package com.sicobo.sicobo.util;

import java.util.HashMap;
import java.util.Map;


import static com.sicobo.sicobo.util.Constantes.Stuff.*;
public class SiteValidator {

    private static final String REGEX_FIELD_EMPTY = "^[\\s.\\-_]*$";

    private static final String REGEX_NAME_INCORRECT = "^(?!\\s)(?!.*\\s$)[a-zA-Z0-9#%&.,áéíóúÁÉÍÓÚüÜñÑ\\s]*$";

    private static final int SIZE_MAX_NAME = 255;

    private static final int SIZE_MIN_NAME = 3;

    private static final String REGEX_ADDRESS_INCORRECT = "^(?!\\s)(?!.*\\s$)[a-zA-Z0-9#%&.,áéíóúÁÉÍÓÚüÜñÑ\\s]*$";

    private static final int SIZE_MAX_ADDRESS = 255;

    private static final int SIZE_MIN_ADDRESS = 10;



    public boolean validName(String nombre){
        if(nombre == null || nombre.matches(REGEX_FIELD_EMPTY)) return true;
        if(!nombre.matches(REGEX_NAME_INCORRECT))return true;
        if(nombre.length() > SIZE_MAX_NAME) return true;
        if(nombre.length() < SIZE_MIN_NAME ) return true;
        return false;
    }

    public boolean validAddress(String address){
        if(address == null || address.matches(REGEX_FIELD_EMPTY)) return true;
        if(!address.matches(REGEX_ADDRESS_INCORRECT))return true;
        if(address.length() > SIZE_MAX_ADDRESS) return true;
        if(address.length() < SIZE_MIN_ADDRESS ) return true;
        return false;
    }

    public boolean validState(Long state){
        if(state == 0) return true;
        return false;
    }


}
