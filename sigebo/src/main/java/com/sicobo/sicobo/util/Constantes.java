package com.sicobo.sicobo.util;

import static com.sicobo.sicobo.util.Constantes.Message_Body.*;
import static com.sicobo.sicobo.util.Constantes.Message_Codes.*;
import static com.sicobo.sicobo.util.Constantes.Message_Type.*;
import static com.sicobo.sicobo.util.Constantes.Message_Headers.*;

public final class Constantes {

    public static final class Roles {
        public final static String ROLE_ADMIN = "ROLE_ADMIN";
        public final static String ROLE_GESTOR = "ROLE_GESTOR";
    }

    public static final class Message_Headers{
        public final static String FAILED_REGISTRATION = "Registro fallido";
        public final static String FAILED_EXECUTION = "Ejecución fallida";
        public final static String SUCCESSFUL_REGISTRATION = "Registro exitoso";
        public final static String SUCCESSFUL_SEARCH = "Búsqueda exitosa";

        public final static String SUCCESSFUL_UPDATE = "Actualización exitosa";
        public final static String FAILED_SEARCH = "Búsqueda fallida";
    }

    public static final class Message_Codes{
        public final static int SERVER_FAIL_CODE = 500;
        public final static int FAIL_CODE = 400;
        public final static int SUCCESS_CODE = 200;
    }

    public static final class Message_Body{
        public static final String INTERNAL_ERROR ="Ocurrió un error interno";
        public static final String INSERT_SUCCESSFUL="Se ha realizado el registro exitosamente";
        public static final String SEARCH_SUCCESSFUL="Se ha realizado la búsqueda exitosamente";
        public static final String UPDATE_SUCCESSFUL="Se ha realizado la actualización exitosamente";

        public static final String ERROR_NAME = "Ingresa un valor válido en el campo nombre";

        public static final String ERROR_ADDRESS = "Ingresa un valor válido en el campo direccion";

        public static final String ERROR_STATE = "Ingresa un valor válido en el campo estado";

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
        public static final String GESTOR_BODEGAS = "gestorViews/listWarehouses";
        public static final String REDIRECT_ERROR = "redirect:/error";
        public static final String ADMIN_LISTSITES = "adminViews/listSites";
        public static final String ADMIN_REGISTERSITE = "adminViews/registerSite";
        public static final String REDIRECT_ADMIN_LISTSITES = "redirect:/admin/sitios";
        public static final String ADMIN_UPDATESITE = "adminViews/updateSite";
    }

    public static final class Stuff{
        public static final String MESSAGE = "message";
        public static final String USER = "user";
        public static final String ERRORS = "Error: ";
        public static final String STATUS = "status";
        public static final String OPTION = "opcion";
        public static final String RESPONSE = "response";
        public static final String SITES = "sitios";

        public static final String STATES = "states";
        public static final String SITE = "site";
        public static final String MAP_ERROR = "error";
    }

    public static class ObjectMessages{
        public static final Message MESSAGE_CATCH_ERROR = new Message(INTERNAL_ERROR, "No se realizó la acción indicada. Inténtalo de nuevo", FAILED, SERVER_FAIL_CODE, null );
        public static final Message MESSAGE_FIELD_ERRORS = new Message(FAILED_EXECUTION, "Ingresa valores válidos", FAILED, FAIL_CODE, null);
    }



}
