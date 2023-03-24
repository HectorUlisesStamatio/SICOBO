package com.sicobo.sicobo.util;

public final class Constantes {

    public static final class Roles {
        public final static String ROLE_ADMIN = "ROLE_ADMIN";
    }

    public static final class Message_Headers{
        public final static String FAILED_REGISTRATION = "Registro fallido";
        public final static String FAILED_EXECUTION = "Ejecución fallida";
        public final static String SUCCESSFUL_REGISTRATION = "Registro exitoso";
    }

    public static final class Message_Codes{
        public final static int SERVER_FAIL_CODE = 500;
        public final static int FAIL_CODE = 400;
        public final static int SUCCESS_CODE = 200;
    }

    public static final class Message_Body{
        public static final String INTERNAL_ERROR ="Ocurrió un error interno";
        public static final String INSERT_SUCCESSFUL="Se ha realizado el registro exitosamente";
    }

    public static final class Message_Type{
        public final static String FAILED = "failed";
        public final static String SUCCESS = "success";
    }

    public static final class Redirects{
        public static final String REGISTER = "register";
        public static final String INDEX = "index";
        public static final String ADMIN_DASHBOARD = "adminViews/dashboard";
        public static final String REDIRECT_LOGIN = "redirect:/login";

    }

    public static final class Stuff{
        public static final String MESSAGE = "message";
        public static final String USER = "user";
        public static final String ERRORS = "Error: ";
    }



}
