package com.sicobo.sicobo.controller;

import com.sicobo.sicobo.dao.DaoUser;
import com.sicobo.sicobo.dto.DTOUser;
import com.sicobo.sicobo.model.BeanGestorInfo;
import com.sicobo.sicobo.serviceimpl.UserServiceImpl;
import com.sicobo.sicobo.serviceimpl.WarehouseServiceImpl;
import com.sicobo.sicobo.serviceimpl.CostTypeServiceImpl;
import com.sicobo.sicobo.serviceimpl.StateServiceImpl;
import com.sicobo.sicobo.serviceimpl.WarehouseDetailsServiceImpl;
import com.sicobo.sicobo.util.Message;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.sicobo.sicobo.util.Constantes.MessageType.FAILED;
import static com.sicobo.sicobo.util.Constantes.ObjectMessages.MESSAGE_CATCH_ERROR;
import static com.sicobo.sicobo.util.Constantes.Redirects.*;
import static com.sicobo.sicobo.util.Constantes.Roles.*;
import static com.sicobo.sicobo.util.Constantes.Stuff.*;

@Controller
public class HomeController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private DaoUser userRepository;

    @Autowired
    private WarehouseServiceImpl warehouseService;
    @Autowired
    private StateServiceImpl stateService;
    @Autowired
    private WarehouseDetailsServiceImpl warehouseDetailsService;
    @Autowired
    private CostTypeServiceImpl costTypeService;

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String inicio(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String rol = auth.getAuthorities().toString();
        try{
            if(Objects.equals(rol, "[ROLE_ADMIN]")) {
                return ADMIN_DASHBOARD;
            }else if(Objects.equals(rol, "[ROLE_GESTOR]")) {
                return "redirect:/warehousesAll";
            }else if(Objects.equals(rol, "[ROLE_USUARIO]")) {
                return USER_INDEX;
            }else{
                return INDEX;
            }
        }catch (Exception E){
            log.warn("No se pudo realizar la validación del rol");
        }
        return INDEX;
    }



    @Secured({ROLE_GESTOR})
    @GetMapping("/warehousesAll")
    public String warehousesAll(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Message gestorInfo = (Message) userService.buscarGestor(auth.getName()).getBody();

        if(gestorInfo.getType().equals(FAILED)){
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
    public String register(Model model, DTOUser user){
        model.addAttribute(USER, user);
        return REGISTER;
    }

    @PostMapping("/registrarUsuario")
    public String registrarUsuario(@Validated @ModelAttribute(USER) DTOUser user, BindingResult result, RedirectAttributes attributes, Model model){

        if(result.hasErrors()){
            for (ObjectError error: result.getAllErrors()){
                log.error(ERRORS, error.getDefaultMessage());
            }
            return REGISTER;
        }
        Message message = (Message) userService.registrar(user).getBody();
        assert message != null;
        if(message.getType().equals(FAILED)){
            model.addAttribute(MESSAGE, message);
            return REGISTER;
        }
        return REDIRECT_LOGIN;
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword(Model model){
        return FORGOT_PASSWORD;
    }

    @GetMapping("/resetPassword")
    public String resetPassword(){
        return RESET_PASSWORD;
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("email") String email, Model model) {

        ResponseEntity<?> responseEntity = userService.sendEmailTemplate(email);
        Message message = (Message) responseEntity.getBody();
        assert message != null;

        if(message.getType().equals(FAILED)) {
            model.addAttribute(MESSAGE,message);
            return LOGIN;
        }else{
            System.out.println("Se envío correctamente el correo");
            model.addAttribute(MESSAGE,message);
            return LOGIN;
        }

    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("pass") String pass,@RequestParam("password") String password,@RequestParam("token") String token ,Model model) {

        if (!pass.equals(password)) {
            model.addAttribute("invalidPass", "Las contraseñas no coinciden");
            return RESET_PASSWORD;
        }

        ResponseEntity<?> responseEntity = userService.changePassword(pass, token);
        Message message = (Message) responseEntity.getBody();
        assert message != null;

        if(message.getType().equals(FAILED)) {
            model.addAttribute(MESSAGE,message);
            return RESET_PASSWORD;
        }else{
            System.out.println("Se cambio correctamente la contraseña");
            model.addAttribute(MESSAGE,message);
            return LOGIN;
        }

    }

    @Secured({ROLE_ADMIN,ROLE_GESTOR,ROLE_USUARIO})
    @GetMapping("/preferencias/perfil")
    public String profile(Model model,RedirectAttributes attributes){
        try{
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
        }catch (NullPointerException e) {
            log.error("Valor nulo un error en HomeController - perfil" + e.getMessage());
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }catch(Exception e){
            log.error("Ocurrio un error en HomeController - perfil" + e.getMessage());
            model.addAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }

        return USER_PROFILE;
    }

    @Secured({ROLE_ADMIN,ROLE_GESTOR,ROLE_USUARIO})
    @PostMapping("/preferencias/actualizarPerfil")
    public String updateGestor(@Valid @ModelAttribute(USER) DTOUser user, BindingResult result, RedirectAttributes attributes, Model model) {
        try{
            if (result.hasErrors()) {
                for (ObjectError error : result.getAllErrors()) {
                    log.error("Error: " + error.getDefaultMessage());
                }
                return USER_PROFILE;
            }
            Message response = (Message) userService.editarPerfil(user).getBody();
            assert response !=null;
            if (response.getType().equals(FAILED)) {
                model.addAttribute(MESSAGE, response);
                return USER_PROFILE;
            }
            attributes.addFlashAttribute(MESSAGE, response);
        }catch (NullPointerException e) {
            log.error("Valor nulo un error en HomeController - perfil" + e.getMessage());
            attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }catch(Exception e){
            log.error("Ocurrio un error en HomeController - perfil" + e.getMessage());
            attributes.addFlashAttribute(MESSAGE, MESSAGE_CATCH_ERROR);
        }
        return REDIRECT_HOME;
    }

    @Secured({ROLE_USUARIO})
    @GetMapping(value = {"/bodegas","/bodegas/{parametroUno}","/bodegas/{parametroUno}/{parametroDos}"})
    public String listarBodegas(@PathVariable(required = false) Optional<String> parametroUno, @PathVariable(required = false) Optional<String> parametroDos, RedirectAttributes attributes, Model model) {
        try {
            String parametro=null;
            String segundoParametro=null;
            if(parametroUno.isPresent()){
                parametro = parametroUno.get();
            }
            if(parametroDos.isPresent()) {
                segundoParametro = parametroDos.get();
            }

            Message response = (Message) stateService.listar().getBody();
            Message responseCosto = (Message) costTypeService.listar().getBody();
            Message responseBodegas = (Message) warehouseDetailsService.listar(parametro,segundoParametro).getBody();
            assert response != null;
            assert responseCosto != null;
            assert responseBodegas != null;
            model.addAttribute(STATES, response.getResult());
            model.addAttribute(COST_TYPES,responseCosto.getResult());
            model.addAttribute(WAREHOUSES,responseBodegas.getResult());
        }catch (NullPointerException e) {
            log.error("Valor nulo un error en HomeController - listadoBodegas" + e.getMessage());
        }  catch (Exception e) {
            log.error("Ocurrio un error en HomeController - listadoBodegas" + e.getMessage());
        }
        return LISTADO_BODEGAS;
    }


}