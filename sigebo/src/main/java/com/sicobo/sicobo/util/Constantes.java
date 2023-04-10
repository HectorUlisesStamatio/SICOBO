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
        public static final String ROLE_USUARIO = "ROLE_USUARIO";
    }

    public static final class MessageHeaders{

        private MessageHeaders(){}

        public static final String FAILED_REGISTRATION = "Registro fallido";
        public static final String FAILED_EXECUTION = "Ejecución fallida";
        public static final String SUCCESSFUL_REGISTRATION = "Registro exitoso";
        public static final String SUCCESSFUL_SEARCH = "Búsqueda exitosa";
        public static final String SUCCESSFUL_UPDATE = "Actualización exitosa";
        public static final String SUCCESSFUL_DELETE = "Eliminación exitosa";
        public static final String FAILED_SEARCH = "Búsqueda fallida";

        public static final String INVALID_TOKEN = "Código de seguridad invalido";
    }

    public static final class MessageCodes{

        private MessageCodes(){}
        public static final int SERVER_FAIL_CODE = 500;
        public static final int FAIL_CODE = 400;
        public static final int SUCCESS_CODE = 200;
    }

    public static final class MessageBody{
        private MessageBody(){}
        public static final String INTERNAL_ERROR ="Ocurrió un error interno. Inténlo de nuevo o consulte con los administradores del sitio";
        public static final String SEND_EMAIL_SUCCESFUL ="Se envío correctamente el correo, no olvides revisar tu bandeja de spam";
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


        public static final String DELETE_SUCCESSFUL="Se ha realizado la eliminación exitosamente";

    }

    public static final class MessageType{

        private MessageType(){}
        public static final String FAILED = "failed";
        public static final String SUCCESS = "success";
    }

    public static final class Redirects{

        private Redirects(){}
        public static final String REGISTER = "register";

        public static final String LOGIN = "login";
        public static final String FORGOT_PASSWORD = "forgotPassword";

        public static final String RESET_PASSWORD = "resetPassword";

        public static final String MIS_BODEGAS = "misBodegas";
        public static final String INDEX = "index";
        public static final String ADMIN_DASHBOARD = "adminViews/dashboard";
        public static final String REDIRECT_ADMIN_DASHBOARD = "redirect:/admin/dashboard";
        public static final String REDIRECT_GESTOR_DASHBOARD = "redirect:/gestor/dashboard";
        public static final String REDIRECT_LOGIN = "redirect:/login";
        public static final String GESTOR_WAREHOUSES = "gestorViews/listWarehouses";
        public static final String REDIRECT_ERROR = "redirect:/error";
        public static final String ADMIN_LISTSITES = "adminViews/listSites";
        public static final String ADMIN_REGISTERSITE = "adminViews/registerSite";
        public static final String REDIRECT_ADMIN_LISTSITES = "redirect:/admin/sitios";
        public static final String ADMIN_UPDATESITE = "adminViews/updateSite";
        public static final String ADMIN_REGISTERCOSTTYPE = "adminViews/registerCostType";
        public static final String REDIRECT_ADMIN_REGISTERCOSTTYPE = "redirect:/admin/costos";
        public static final String ADMIN_LISTGESTORES = "adminViews/listGestores";
        public static final String REDIRECT_ADMIN_LISTGESTORES = "redirect:/admin/listarGestores";
        public static final String ADMIN_REGISTERGESTORES = "adminViews/registerGestores";
        public static final String ADMIN_UPDATEGESTORES = "adminViews/updateGestor";
        public static final String GESTOR_REGISTERWAREHOUSE = "gestorViews/registerWarehouse";
        public static final String REDIRECT_GESTOR_LISTWAREHOUSES = "redirect:/gestor/bodegas";
        public static final String ADMIN_TERMSANDCONDITIONS = "adminViews/listTermsAndConditions";
        public static final String REDIRECT_ADMIN_TERMSANDCONDITIONS = "redirect:/admin/listarTerminosYCondiciones";
        public static final String USER_TERMSANDCONDITIONS = "userViews/termsAndConditions";
        public static final String USER_PROFILE = "editProfile";
        public static final String REDIRECT_HOME = "redirect:/";

        public static final String GESTOR_DASHBOARD = "gestorViews/dashboard";

        public static final String USER_INDEX = "redirect:/usuario/bodegas";
        public static final String WAREHOUSES_USER = "/usuario/bodegas";

        public static final String LISTADO_BODEGAS = "listadoBodegas";

        public static final String PRODUCT_DETAIL = "productDetail";

        public static final String REDIRECT_PREPARED_DETAIL = "redirect:/usuario/detalleProducto/";
        public static final String LISTPAYMENTS = "adminViews/listPayments";

        public static final String PREPARED_DETAIL = "http://localhost:8080/detalleProducto/";
        public static final String PAYMENT_INFORMATION = "paymentInformation";
        public static final String PAYMENT_RENOVATION_INFORMATION = "userViews/paymentRenovationtInformation";
        public static final String MY_WAREHOUSE_DETAIL = "userViews/myWarehouseDetail";
        public static final String MY_WAREHOUSES = "misBodegas";
        public static final String REDIRECT_DETAIL_RENOVATION = "redirect:/renovacionBodega/";


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
        public static final String STATES = "states";
        public static final String SITE = "site";
        public static final String WAREHOUSE_TYPES = "warehouseTypes";
        public static final String COST_TYPES = "costTypes";
        public static final String COSTTYPES = "pagos";
        public static final String HIDE_COST ="hideCost";
        public static final String COST ="cost";
        public static final String GESTORES = "gestores";
        public static final String MAP_ERROR = "error";
        public static final String WAREHOUSE = "warehouse";
        public static final String TERMSANDCONDTIONS = "termsAndConditions";
        public static final String POLICIES = "politicas";
        public static final String WAREHOUSES = "bodegas";
        public static final String PAYMENT = "payment";
        public static final String SELECT_OPTION = "selectOption";
        public static final String WAREHOUSE_OPTION = "Warehouse";
        public static final String MYWAREHOUSE_OPTION = "MyWarehouses";

    }

    public static class ObjectMessages{

        private ObjectMessages(){}
        public static final Message MESSAGE_CATCH_ERROR = new Message(INTERNAL_ERROR, "No se realizó la acción indicada. Inténtalo de nuevo", FAILED, SERVER_FAIL_CODE, null );
        public static final Message MESSAGE_FIELD_ERRORS = new Message(FAILED_EXECUTION, "Ingresa valores válidos", FAILED, FAIL_CODE, null);
    }



}
