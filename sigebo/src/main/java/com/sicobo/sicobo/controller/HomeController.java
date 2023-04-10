package com.sicobo.sicobo.controller;

import com.sicobo.sicobo.dao.DaoUser;
import com.sicobo.sicobo.dto.DTOUser;
import com.sicobo.sicobo.model.BeanPayment;
import com.sicobo.sicobo.model.BeanUser;
import com.sicobo.sicobo.model.BeanWarehouse;
import com.sicobo.sicobo.serviceimpl.*;
import com.sicobo.sicobo.model.BeanGestorInfo;
import com.sicobo.sicobo.util.Message;
import com.stripe.model.checkout.Session;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.sicobo.sicobo.util.Constantes.MessageType.FAILED;
import static com.sicobo.sicobo.util.Constantes.ObjectMessages.MESSAGE_CATCH_ERROR;
import static com.sicobo.sicobo.util.Constantes.ObjectMessages.MESSAGE_FIELD_ERRORS;
import static com.sicobo.sicobo.util.Constantes.Redirects.*;
import static com.sicobo.sicobo.util.Constantes.Roles.*;
import static com.sicobo.sicobo.util.Constantes.Stuff.*;

@Controller
public class HomeController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private WarehouseServiceImpl warehouseService;
    @Autowired
    private StateServiceImpl stateService;
    @Autowired
    private WarehouseDetailsServiceImpl warehouseDetailsService;
    @Autowired
    private CostTypeServiceImpl costTypeService;

    @Autowired
    private StripeServiceImpl stripeService;

    @Autowired
    private PaymentServiceImpl paymentService;

    private Session session;

    private Session sessionRenovation;

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String inicio(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String rol = auth.getAuthorities().toString();
        try {
            if (Objects.equals(rol, "[ROLE_ADMIN]")) {
                return REDIRECT_ADMIN_DASHBOARD;
            } else if (Objects.equals(rol, "[ROLE_GESTOR]")) {
                return "redirect:/warehousesAll";
            } else if (Objects.equals(rol, "[ROLE_USUARIO]")) {
                return USER_INDEX;
            } else {
                return INDEX;
            }
        } catch (Exception E) {
            log.warn("No se pudo realizar la validación del rol");
        }
        return INDEX;
    }


    @Secured({ROLE_GESTOR})
    @GetMapping("/warehousesAll")
    public String warehousesAll(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Message gestorInfo = (Message) userService.buscarGestor(auth.getName()).getBody();

        if (gestorInfo.getType().equals(FAILED)) {
            return ERRORS;
        }

        long userId = 0;
        String siteName = null;

        List<BeanGestorInfo> gestorInfosList = (List<BeanGestorInfo>) gestorInfo.getResult();
        for (BeanGestorInfo gestor : gestorInfosList) {
            userId = gestor.getUserId();
            siteName = gestor.getSiteName();
        }

        ResponseEntity<List<Object[]>> response = warehouseService.listar(userId, siteName);
        int bodegasDisponibles = 0;
        int bodegasRentadas = 0;

        for (Object[] resultado : response.getBody()) {
            int status = (int) resultado[4];
            if (status == 1) {
                bodegasDisponibles++;
            } else if (status == 2) {
                bodegasRentadas++;
            }
        }
        model.addAttribute("bodegasDisponibles", bodegasDisponibles);
        model.addAttribute("bodegasRentadas", bodegasRentadas);

        model.addAttribute(RESPONSE, response.getBody());
        return GESTOR_DASHBOARD;
    }


    @GetMapping("/register")
    public String register(Model model, DTOUser user) {
        model.addAttribute(USER, user);
        return REGISTER;
    }

    @PostMapping("/registrarUsuario")
    public String registrarUsuario(@Validated @ModelAttribute(USER) DTOUser user, BindingResult result, RedirectAttributes attributes, Model model) {

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                log.error(ERRORS, error.getDefaultMessage());
            }
            return REGISTER;
        }
        Message message = (Message) userService.registrar(user).getBody();
        assert message != null;
        if (message.getType().equals(FAILED)) {
            model.addAttribute(MESSAGE, message);
            return REGISTER;
        }
        return REDIRECT_LOGIN;
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword(Model model) {
        return FORGOT_PASSWORD;
    }

    @GetMapping("/resetPassword")
    public String resetPassword() {
        return RESET_PASSWORD;
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("email") String email, Model model) {

        ResponseEntity<?> responseEntity = userService.sendEmailTemplate(email);
        Message message = (Message) responseEntity.getBody();
        assert message != null;

        if (message.getType().equals(FAILED)) {
            model.addAttribute(MESSAGE, message);
            return LOGIN;
        } else {
            model.addAttribute(MESSAGE, message);
            return LOGIN;
        }

    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("pass") String pass, @RequestParam("password") String password, @RequestParam("token") String token, Model model) {

        if (!pass.equals(password)) {
            model.addAttribute("invalidPass", "Las contraseñas no coinciden");
            return RESET_PASSWORD;
        }

        ResponseEntity<?> responseEntity = userService.changePassword(pass, token);
        Message message = (Message) responseEntity.getBody();
        assert message != null;

        if (message.getType().equals(FAILED)) {
            model.addAttribute(MESSAGE, message);
            return RESET_PASSWORD;
        } else {
            model.addAttribute(MESSAGE, message);
            return LOGIN;
        }

    }

    @Secured({ROLE_ADMIN, ROLE_GESTOR, ROLE_USUARIO})
    @GetMapping("/preferencias/perfil")
    public String profile(Model model, RedirectAttributes attributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Message response = (Message) stateService.listar().getBody();
            assert response != null;
            model.addAttribute(STATES, response.getResult());
            Message message = (Message) userService.findBeanUserByUsername(auth.getName()).getBody();
            assert message != null;
            if (message.getType().equals(FAILED)) {
                attributes.addFlashAttribute(MESSAGE, message);
                return INDEX;
            }
            model.addAttribute(RESPONSE, message);
            model.addAttribute(USER, message.getResult());
        } catch (NullPointerException e) {
            log.error("Valor nulo un error en HomeController - perfil" + e.getMessage());
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        } catch (Exception e) {
            log.error("Ocurrio un error en HomeController - perfil" + e.getMessage());
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }

        return USER_PROFILE;
    }

    @Secured({ROLE_ADMIN, ROLE_GESTOR, ROLE_USUARIO})
    @PostMapping("/preferencias/actualizarPerfil")
    public String updateGestor(@Valid @ModelAttribute(USER) DTOUser user, BindingResult result, RedirectAttributes attributes, Model model) {
        try {
            if (result.hasErrors()) {
                for (ObjectError error : result.getAllErrors()) {
                    log.error("Error: " + error.getDefaultMessage());
                }
                return USER_PROFILE;
            }
            Message response = (Message) userService.editarPerfil(user).getBody();
            assert response != null;
            if (response.getType().equals(FAILED)) {
                model.addAttribute(MESSAGE, response);
                return USER_PROFILE;
            }
            attributes.addFlashAttribute(MESSAGE, response);
        } catch (NullPointerException e) {
            log.error("Valor nulo un error en HomeController - perfil" + e.getMessage());
            attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        } catch (Exception e) {
            log.error("Ocurrio un error en HomeController - perfil" + e.getMessage());
            attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }
        return REDIRECT_HOME;
    }

    @Secured({ROLE_USUARIO})
    @GetMapping(value = {"/bodegas", "/bodegas/{parametroUno}", "/bodegas/{parametroUno}/{parametroDos}"})
    public String listarBodegas(@PathVariable(required = false) Optional<String> parametroUno, @PathVariable(required = false) Optional<String> parametroDos, RedirectAttributes attributes, Model model) {
        try {
            String parametro = null;
            String segundoParametro = null;
            if (parametroUno.isPresent()) {
                parametro = parametroUno.get();
            }
            if (parametroDos.isPresent()) {
                segundoParametro = parametroDos.get();
            }

            Message response = (Message) stateService.listar().getBody();
            Message responseCosto = (Message) costTypeService.listar().getBody();
            Message responseBodegas = (Message) warehouseDetailsService.listar(parametro, segundoParametro).getBody();
            assert response != null;
            assert responseCosto != null;
            assert responseBodegas != null;
            model.addAttribute(STATES, response.getResult());
            model.addAttribute(COST_TYPES, responseCosto.getResult());
            model.addAttribute(WAREHOUSES, responseBodegas.getResult());
        } catch (NullPointerException e) {
            log.error("Valor nulo un error en HomeController - listadoBodegas" + e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error en HomeController - listadoBodegas" + e.getMessage());
        }
        return LISTADO_BODEGAS;
    }


    @Secured({ROLE_USUARIO})
    @GetMapping("/detalleProducto/{idWarehouse}")
    public String preparedDetail(@PathVariable Optional<String> idWarehouse, Model model) {

        try {
            if (!idWarehouse.isPresent()) {
                model.addAttribute(MESSAGE, MESSAGE_FIELD_ERRORS);
                model.addAttribute(WAREHOUSE, null);
                return PRODUCT_DETAIL;
            }

            try {
                Long id = Long.parseLong(idWarehouse.get());
                Message message = (Message) warehouseService.detalleBodega(id).getBody();
                assert message != null;

                if (message.getType().equals(FAILED)) {
                    model.addAttribute(MESSAGE, message);
                    model.addAttribute(WAREHOUSE, null);
                    return PRODUCT_DETAIL;
                }

                model.addAttribute(WAREHOUSE, message);
            } catch (NumberFormatException e) {
                model.addAttribute(MESSAGE, MESSAGE_FIELD_ERRORS);
                model.addAttribute(WAREHOUSE, null);
                return PRODUCT_DETAIL;
            }

        } catch (NullPointerException e) {
            log.error("Valor nulo un error en HomeController - preparedDatail" + e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error en HomeController - preparedDatail" + e.getMessage());
        }

        return PRODUCT_DETAIL;
    }

    @Secured({ROLE_USUARIO})
    @PostMapping("/prepararCompra")
    public String preparedBuy(@RequestParam("idWarehouse") Optional<Long> idWarehouse, @RequestParam("finalCost") Optional<Double> finalCost, @RequestParam("months") Optional<Integer> months, RedirectAttributes attributes, Model model) {
        try {
            if (!idWarehouse.isPresent() || !finalCost.isPresent() || !months.isPresent()) {
                attributes.addFlashAttribute(MESSAGE, MESSAGE_FIELD_ERRORS);
                return REDIRECT_PREPARED_DETAIL + idWarehouse;
            }

            Message message = (Message) warehouseService.detalleBodega(idWarehouse.get()).getBody();
            assert message != null;
            if (message.getType().equals(FAILED)) {
                attributes.addFlashAttribute(MESSAGE, message);
                return REDIRECT_PREPARED_DETAIL + idWarehouse;
            }
            BeanWarehouse warehouse = (BeanWarehouse) message.getResult();
            assert warehouse != null;
            warehouse.setFinalCost(finalCost.get());
            Message response = (Message) stripeService.checkout(warehouse, months.get()).getBody();
            assert response != null;
            if (response.getType().equals(FAILED)) {
                attributes.addFlashAttribute(MESSAGE, response);
                return REDIRECT_PREPARED_DETAIL + warehouse.getId();
            }
            session = (Session) response.getResult();
            assert session != null;

        } catch (NullPointerException e) {
            log.error("Valor nulo un error en HomeController - preparedBuy" + e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error en HomeController - preparedBuy" + e.getMessage());
        }

        return "redirect:" + session.getUrl();
    }

    @Secured({ROLE_USUARIO})
    @GetMapping("/estadoPago")
    public String succesPayment(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = ((User) auth.getPrincipal()).getUsername();
            assert username != null;
            ResponseEntity<?> responseEntity = userService.findBeanUserByUsername(username);
            Message message = (Message) responseEntity.getBody();
            assert message != null;

            if (message.getType().equals(FAILED)) {
                model.addAttribute(MESSAGE, message);
                return WAREHOUSES_USER;
            }
            BeanUser user = (BeanUser) message.getResult();
            assert user != null;
            ResponseEntity<?> responseRented = stripeService.paymentIntent(session, user);
            Message messageResult = (Message) responseRented.getBody();
            assert messageResult != null;

            if (messageResult.getType().equals(FAILED)) {
                model.addAttribute(MESSAGE, messageResult);
                return PAYMENT_INFORMATION;
            }

            Map<String, Object> mapResult = (Map<String, Object>) messageResult.getResult();
            assert mapResult != null;

            BeanWarehouse warehouse = (BeanWarehouse) mapResult.get("warehouse");
            assert warehouse != null;
            BeanPayment payment = (BeanPayment) mapResult.get("payment");
            assert warehouse != null;

            model.addAttribute(MESSAGE, messageResult);
            model.addAttribute(WAREHOUSE, warehouse);
            model.addAttribute(PAYMENT, payment);
        } catch (NullPointerException e) {
            log.error("Ocurrio un error en HomeController - succesPayment" + e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error en HomeController - succesPayment" + e.getMessage());
        }

        return PAYMENT_INFORMATION;
    }

    @Secured({ROLE_USUARIO})
    @GetMapping("/renovacionBodega/{idPayment}")
    public String detailRenovation(@PathVariable("idPayment") Optional<String> idPayment, Model model) {
        try {
            if (!idPayment.isPresent()) {
                model.addAttribute(MESSAGE, MESSAGE_FIELD_ERRORS);
                return MY_WAREHOUSES;
            }
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = ((User) auth.getPrincipal()).getUsername();
            assert username != null;
            ResponseEntity<?> responseEntity = userService.findBeanUserByUsername(username);
            Message message = (Message) responseEntity.getBody();
            assert message != null;

            if (message.getType().equals(FAILED)) {
                model.addAttribute(MESSAGE, message);
                return MY_WAREHOUSES;
            }
            BeanUser user = (BeanUser) message.getResult();
            assert user != null;


            try {
                Long id = Long.parseLong(idPayment.get());

                Message messagePayment = (Message) paymentService.buscarPagoRenovacion(id, user).getBody();
                assert messagePayment != null;

                if (messagePayment.getType().equals(FAILED)) {
                    model.addAttribute(WAREHOUSE, null);
                    model.addAttribute(MESSAGE, messagePayment);
                    return MY_WAREHOUSE_DETAIL;
                }

                BeanPayment payment = (BeanPayment) messagePayment.getResult();
                assert payment != null;

                Message messageResponse = (Message) warehouseService.detalleBodegaRentada(payment.getBeanWarehouse().getId(), user).getBody();

                if (messageResponse.getType().equals(FAILED)) {
                    model.addAttribute(WAREHOUSE, null);
                    model.addAttribute(MESSAGE, messageResponse);
                    return MY_WAREHOUSE_DETAIL;
                }

                model.addAttribute(WAREHOUSE, messageResponse);
                model.addAttribute(PAYMENT, idPayment.get());
            } catch (NumberFormatException e) {
                model.addAttribute(MESSAGE, MESSAGE_FIELD_ERRORS);
                model.addAttribute(WAREHOUSE, null);
                return MY_WAREHOUSE_DETAIL;
            }


        } catch (NullPointerException e) {
            log.error("Ocurrio un error en HomeController - detailRenovation" + e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error en HomeController - detailRenovation" + e.getMessage());
        }
        return MY_WAREHOUSE_DETAIL;
    }

    @Secured({ROLE_USUARIO})
    @PostMapping("/prepararRenovacion")
    public String prepareRenovation(
            @RequestParam("idWarehouse") Optional<Long> idWarehouse,
            @RequestParam("finalCost") Optional<Double> finalCost,
            @RequestParam("months") Optional<Integer> months,
            @RequestParam("idPayment") Optional<Long> idPayment,
            RedirectAttributes attributes, Model model) {
        try {
            if (!idWarehouse.isPresent() || !finalCost.isPresent() || !months.isPresent() || !idPayment.isPresent()) {
                attributes.addFlashAttribute(MESSAGE, MESSAGE_FIELD_ERRORS);
                return REDIRECT_DETAIL_RENOVATION + idPayment.get();
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = ((User) auth.getPrincipal()).getUsername();
            assert username != null;
            ResponseEntity<?> responseEntity = userService.findBeanUserByUsername(username);
            Message message = (Message) responseEntity.getBody();
            assert message != null;

            if (message.getType().equals(FAILED)) {
                attributes.addFlashAttribute(MESSAGE, message);
                return REDIRECT_DETAIL_RENOVATION + idPayment.get();
            }
            BeanUser user = (BeanUser) message.getResult();
            assert user != null;


            Message messageResponse = (Message) warehouseService.detalleBodegaRentada(idWarehouse.get(), user).getBody();
            assert messageResponse != null;
            if (messageResponse.getType().equals(FAILED)) {
                attributes.addFlashAttribute(MESSAGE, messageResponse);
                return REDIRECT_DETAIL_RENOVATION + idPayment.get();
            }


            BeanWarehouse warehouse = (BeanWarehouse) messageResponse.getResult();
            assert warehouse != null;
            warehouse.setFinalCost(finalCost.get());
            Message response = (Message) stripeService.checkoutRenovation(warehouse, months.get(), idPayment.get()).getBody();
            assert response != null;
            if (response.getType().equals(FAILED)) {
                attributes.addFlashAttribute(MESSAGE, response);
                return REDIRECT_DETAIL_RENOVATION + idPayment.get();
            }
            sessionRenovation = (Session) response.getResult();
            assert sessionRenovation != null;

        } catch (NullPointerException e) {
            log.error("Ocurrio un error en HomeController - prepareRenovation" + e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error en HomeController - prepareRenovation" + e.getMessage());
        }

        return "redirect:" + sessionRenovation.getUrl();
    }


    @Secured({ROLE_USUARIO})
    @GetMapping("/estadoRenovacion")
    public String succesPaymentRenovation(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = ((User) auth.getPrincipal()).getUsername();
            assert username != null;
            ResponseEntity<?> responseEntity = userService.findBeanUserByUsername(username);
            Message message = (Message) responseEntity.getBody();
            assert message != null;

            if (message.getType().equals(FAILED)) {
                model.addAttribute(MESSAGE, message);
                return WAREHOUSES_USER;
            }
            BeanUser user = (BeanUser) message.getResult();
            assert user != null;

            ResponseEntity<?> responseRented = stripeService.paymentIntentRenovation(sessionRenovation, user);
            Message messageResult = (Message) responseRented.getBody();
            assert messageResult != null;

            if (messageResult.getType().equals(FAILED)) {
                model.addAttribute(MESSAGE, messageResult);
                return PAYMENT_RENOVATION_INFORMATION;
            }

            Map<String, Object> mapResult = (Map<String, Object>) messageResult.getResult();
            assert mapResult != null;

            BeanWarehouse warehouse = (BeanWarehouse) mapResult.get("warehouse");
            assert warehouse != null;
            BeanPayment payment = (BeanPayment) mapResult.get("payment");
            assert warehouse != null;

            //Envío del correo
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            String correo = user.getEmail();
            String usernameUser = user.getUsername();
            String nombreBodega = warehouse.getSection();
            String dueDate = dateFormat.format(payment.getDueDate());
            String paymentDay = dateFormat.format(payment.getPaymentDate());

            ResponseEntity<?> responseEntityEmail = userService.renovacionBodegaEmail
                    (correo, usernameUser, nombreBodega, dueDate, paymentDay);
            Message messageEmail = (Message) responseEntityEmail.getBody();

            if (messageEmail == null) {
                System.out.println("Hubo un error al enviar el email");
            }

            model.addAttribute(MESSAGE, messageResult);
            model.addAttribute(WAREHOUSE, warehouse);
            model.addAttribute(PAYMENT, payment);
        } catch (NullPointerException e) {
            log.error("Ocurrio un error en HomeController - succesPaymentRenovation" + e.getMessage());
        } catch (Exception e) {
            log.error("Ocurrio un error en HomeController - succesPaymentRenovation" + e.getMessage());
        }

        return PAYMENT_RENOVATION_INFORMATION;
    }

}