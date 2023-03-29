package com.sicobo.sicobo.util;

import static com.sicobo.sicobo.util.Constantes.MessageBody.*;
import static com.sicobo.sicobo.util.Constantes.MessageCodes.*;
import static com.sicobo.sicobo.util.Constantes.MessageType.*;
import static com.sicobo.sicobo.util.Constantes.MessageHeaders.*;

public final class Constantes {

    private Constantes(){}
    public static final class Roles {

        private Roles(){}
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        public static final String ROLE_GESTOR = "ROLE_GESTOR";
    }

    public static final class MessageHeaders{

        private MessageHeaders(){}

        public static final String FAILED_REGISTRATION = "Registro fallido";
        public static final String FAILED_EXECUTION = "Ejecución fallida";
        public static final String SUCCESSFUL_REGISTRATION = "Registro exitoso";
        public static final String SUCCESSFUL_SEARCH = "Búsqueda exitosa";

        public static final String SUCCESSFUL_UPDATE = "Actualización exitosa";
        public static final String FAILED_SEARCH = "Búsqueda fallida";
    }

    public static final class MessageCodes{

        private MessageCodes(){}

        public static final int SERVER_FAIL_CODE = 500;
        public static final int FAIL_CODE = 400;
        public static final int SUCCESS_CODE = 200;
    }

    public static final class MessageBody{
        private MessageBody(){}
        public static final String INTERNAL_ERROR ="Ocurrió un error interno";
        public static final String INSERT_SUCCESSFUL="Se ha realizado el registro exitosamente";
        public static final String SEARCH_SUCCESSFUL="Se ha realizado la búsqueda exitosamente";
        public static final String UPDATE_SUCCESSFUL="Se ha realizado la actualización exitosamente";

        public static final String ERROR_NAME = "Ingresa un valor válido en el campo nombre";

        public static final String ERROR_ADDRESS = "Ingresa un valor válido en el campo direccion";

        public static final String ERROR_STATE = "Ingresa un valor válido en el campo estado";
        public static final String ERROR_IMAGES = "Ingresa un valor válido en el campo imágenes";

        public static final String USER_EXISTS = "El usuario que has elegido ya está en uso. Por favor, elige otro usuario y vuelve a intentarlo";

        public static final String EMAIL_EXISTS = "El correo que has elegido ya está en uso. Por favor, elige otro correo y vuelve a intentarlo";

        public static final String PASSWORD_INVALID = "La contraseña que has elegido es inválida. Por favor, elige otra contraseña y vuelve a intentarlo";

        public static final String RFC_INVALID = "El RFC que has ingresado es inválido. Por favor, escribe el RFC correctamente y vuelve a intentarlo";

        public static final String USER_INVALID = "El usuario que has ingresado es inválido. Por favor, escribe el usuario correctamente y vuelve a intentarlo";

        public static final String NAME_INVALID  = "El nombre que has ingresado es inválido. Por favor, escribe el nombre correctamente y vuelve a intentarlo";

        public static final String LASTNAME_INVALID = "El apellido paterno que has ingresado es inválido. Por favor, escribe el apellido correctamente y vuelve a intentarlo";

        public static final String SURNAME_INVALID = "El apellido materno que has ingresado es inválido. Por favor, escribe el apellido correctamente y vuelve a intentarlo";

        public static final String PHONE_INVALID = "El teléfono que has ingresado es inválido. Por favor, escribe el teléfono correctamente y vuelve a intentarlo";



    }

    public static final class MessageType{

        private MessageType(){}
        public static final String FAILED = "failed";
        public static final String SUCCESS = "success";
    }

    public static final class Redirects{

        private Redirects(){}
        public static final String REGISTER = "register";
        public static final String INDEX = "index";
        public static final String ADMIN_DASHBOARD = "adminViews/dashboard";
        public static final String REDIRECT_LOGIN = "redirect:/login";
        public static final String GESTOR_WAREHOUSES = "gestorViews/listWarehouses";
        public static final String REDIRECT_ERROR = "redirect:/error";
        public static final String ADMIN_LISTSITES = "adminViews/listSites";
        public static final String ADMIN_REGISTERSITE = "adminViews/registerSite";
        public static final String REDIRECT_ADMIN_LISTSITES = "redirect:/admin/sitios";
        public static final String ADMIN_UPDATESITE = "adminViews/updateSite";
        public static final String ADMIN_LISTGESTORES = "adminViews/listGestores";
        public static final String REDIRECT_ADMIN_LISTGESTORES = "redirect:/admin/listarGestores";
        public static final String ADMIN_REGISTERGESTORES = "adminViews/registerGestores";
        public static final String ADMIN_UPDATEGESTORES = "adminViews/updateGestor";
        public static final String GESTOR_REGISTERWAREHOUSE = "gestorViews/registerWarehouse";
        public static final String GESTOR_UPDATEWAREHOUSE = "gestorViews/updateWarehouse";
        public static final String REDIRECT_GESTOR_LISTSITES = "redirect:/gestor/bodegas";
    }

    public static final class Stuff{

        private Stuff(){}

        public static final String MESSAGE = "message";
        public static final String USER = "user";
        public static final String ERRORS = "Error: ";
        public static final String STATUS = "status";
        public static final String SITIOID = "sitioId";
        public static final String OPTION = "opcion";
        public static final String RESPONSE = "response";
        public static final String SITES = "sitios";

        public static final String GESTORES = "gestores";
        public static final String STATES = "states";
        public static final String SITE = "site";
        public static final String MAP_ERROR = "error";
        public static final String WAREHOUSES_TYPES = "warehousesTypes";
        public static final String WAREHOUSE = "warehouse";
    }

    public static class ObjectMessages{

        private ObjectMessages(){}
        public static final Message MESSAGE_CATCH_ERROR = new Message(INTERNAL_ERROR, "No se realizó la acción indicada. Inténtalo de nuevo", FAILED, SERVER_FAIL_CODE, null );
        public static final Message MESSAGE_FIELD_ERRORS = new Message(FAILED_EXECUTION, "Ingresa valores válidos", FAILED, FAIL_CODE, null);
    }



}
