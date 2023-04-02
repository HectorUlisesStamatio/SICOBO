
package com.sicobo.sicobo.controller;


import com.sicobo.sicobo.dao.DaoUser;
import com.sicobo.sicobo.dto.DTOUser;
import com.sicobo.sicobo.model.BeanUser;
import com.sicobo.sicobo.serviceimpl.UserServiceImpl;
import com.sicobo.sicobo.util.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;
import java.util.UUID;

import static com.sicobo.sicobo.util.Constantes.MessageType.FAILED;
import static com.sicobo.sicobo.util.Constantes.ObjectMessages.MESSAGE_CATCH_ERROR;
import static com.sicobo.sicobo.util.Constantes.Redirects.*;
import static com.sicobo.sicobo.util.Constantes.Stuff.*;

@Controller
public class HomeController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private DaoUser userRepository;

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String inicio(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String rol = auth.getAuthorities().toString();
        try{
            if(Objects.equals(rol, "[ROLE_ADMIN]")) {
                return ADMIN_DASHBOARD;
            }else if(Objects.equals(rol, "[ROLE_GESTOR]")) {
                return "paginaEnBlanco";
            }else if(Objects.equals(rol, "[ROLE_USUARIO]")) {
                return INDEX;
            }else{
                return INDEX;
            }
        }catch (Exception E){
            log.warn("No se pudo realizar la validación del rol");
        }
        return "index";
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

        ResponseEntity<?> responseEntity = userService.sendEmail(email);
        Message message = (Message) responseEntity.getBody();
        assert message != null;

        if(message.getType().equals(FAILED)) {
            model.addAttribute(MESSAGE,message);
            return FORGOT_PASSWORD;
        }else{
            System.out.println("Se envío correctamente el correo");
            model.addAttribute(MESSAGE,message);
            return FORGOT_PASSWORD;
        }

    }

}