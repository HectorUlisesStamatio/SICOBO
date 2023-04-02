package com.sicobo.sicobo.serviceimpl;

import com.sicobo.sicobo.dao.DaoAuthorities;
import com.sicobo.sicobo.dao.DaoUser;
import com.sicobo.sicobo.dto.DTOUser;
import com.sicobo.sicobo.model.BeanAuthorities;
import com.sicobo.sicobo.model.BeanUser;
import com.sicobo.sicobo.service.IUserService;
import com.sicobo.sicobo.util.*;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.sicobo.sicobo.util.Constantes.MessageBody.*;
import static com.sicobo.sicobo.util.Constantes.MessageCodes.*;
import static com.sicobo.sicobo.util.Constantes.MessageHeaders.*;
import static com.sicobo.sicobo.util.Constantes.MessageType.*;


@Service
@Slf4j
public class UserServiceImpl implements IUserService
{
    private static final Duration TOKEN_EXPIRATION = Duration.ofHours(1);

    private final Map<String, Instant> tokens = new HashMap<>();
    @Autowired
    private DaoUser daoUser;
    @Autowired
    private DaoAuthorities daoAuthorities;
    @Autowired
    private PasswordEncrypter passwordEncrypter;
    @Autowired
    private PasswordValidator passwordValidator;
    @Autowired
    private TextValidator textValidator;
    @Autowired
    private PhoneValidator phoneValidator;
    @Autowired
    private RFCValidator rfcValidator;
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> listar() {
        return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH, "La consulta de gestores ha sido exitosa",SUCCESS, SUCCESS_CODE,daoUser.findAllByRoleEquals("ROLE_GESTOR")), HttpStatus.OK);
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> registrar(DTOUser dtoUser) {
        BeanUser beanUser = new BeanUser(dtoUser.getName(),dtoUser.getLastname(),dtoUser.getSurname(),dtoUser.getEmail(),dtoUser.getRfc(),dtoUser.getPhoneNumber(), dtoUser.getUsername(),dtoUser.getPassword(),"ROLE_USUARIO",0,dtoUser.getPolicyAcceptance(),1);

        if(daoUser.existsBeanUserByUsername(beanUser.getUsername())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,USER_EXISTS, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(daoUser.existsBeanUserByEmail(beanUser.getEmail())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,EMAIL_EXISTS, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!passwordValidator.isValid(beanUser.getPassword())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,PASSWORD_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!rfcValidator.isValid(beanUser.getRfc())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,RFC_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!textValidator.isValid(beanUser.getUsername())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,USER_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!textValidator.isValid(beanUser.getName())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,NAME_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!textValidator.isValid(beanUser.getLastname())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,LASTNAME_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!textValidator.isValid(beanUser.getSurname())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,SURNAME_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!phoneValidator.isValid(beanUser.getPhoneNumber())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,PHONE_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        beanUser.setPassword(passwordEncrypter.encriptarPassword(beanUser.getPassword()));
        try{
            BeanUser beanUserCreated = daoUser.save(beanUser);
            BeanAuthorities beanAuthorities = new BeanAuthorities(beanUser.getUsername(),beanUser.getPassword(),beanUser.getEnabled(),beanUser.getRole());
            daoAuthorities.save(beanAuthorities);
            return new ResponseEntity<>(new Message(SUCCESSFUL_REGISTRATION, INSERT_SUCCESSFUL, SUCCESS,SUCCESS_CODE, beanUserCreated), HttpStatus.OK);
        }catch(Exception e){
            log.error("Ocurrió un error al registrar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION, INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> guardar(DTOUser dtoUser) {
        BeanUser beanUser = new BeanUser(dtoUser.getName(),dtoUser.getLastname(),dtoUser.getSurname(),dtoUser.getEmail(),dtoUser.getRfc(),dtoUser.getPhoneNumber(), dtoUser.getUsername(),dtoUser.getPassword(),"ROLE_GESTOR",0,1,1);

        if(daoUser.existsBeanUserByUsername(beanUser.getUsername())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,USER_EXISTS, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(daoUser.existsBeanUserByEmail(beanUser.getEmail())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,EMAIL_EXISTS, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!passwordValidator.isValid(beanUser.getPassword())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,PASSWORD_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!rfcValidator.isValid(beanUser.getRfc())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,RFC_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!textValidator.isValid(beanUser.getUsername())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,USER_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!textValidator.isValid(beanUser.getName())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,NAME_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!textValidator.isValid(beanUser.getLastname())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,LASTNAME_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!textValidator.isValid(beanUser.getSurname())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,SURNAME_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!phoneValidator.isValid(beanUser.getPhoneNumber())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,PHONE_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        beanUser.setPassword(passwordEncrypter.encriptarPassword(beanUser.getPassword()));
        try{
            BeanUser beanUserCreated = daoUser.save(beanUser);
            BeanAuthorities beanAuthorities = new BeanAuthorities(beanUser.getUsername(),beanUser.getPassword(),beanUser.getEnabled(),beanUser.getRole());
            daoAuthorities.save(beanAuthorities);
            return new ResponseEntity<>(new Message(SUCCESSFUL_REGISTRATION, INSERT_SUCCESSFUL, SUCCESS,SUCCESS_CODE, beanUserCreated), HttpStatus.OK);
        }catch(Exception e){
            log.error("Ocurrió un error al guardar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION, INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Object> editar(DTOUser dtoUser) {
        Optional<BeanUser> userSearched = daoUser.findBeanUserById(dtoUser.getId());

        if (!userSearched.isPresent()){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION, "El gestor ha actualizar no se encuentra registrado en el sistema", FAILED, FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        if(daoUser.existsBeanUserByUsernameAndIdNot(dtoUser.getUsername(),dtoUser.getId())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,USER_EXISTS, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(daoUser.existsBeanUserByEmailAndIdNot(dtoUser.getEmail(),dtoUser.getId())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,EMAIL_EXISTS, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!passwordValidator.isValid(dtoUser.getPassword())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,PASSWORD_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!rfcValidator.isValid(dtoUser.getRfc())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,RFC_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!textValidator.isValid(dtoUser.getUsername())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,USER_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!textValidator.isValid(dtoUser.getName())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,NAME_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!textValidator.isValid(dtoUser.getLastname())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,LASTNAME_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!textValidator.isValid(dtoUser.getSurname())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,SURNAME_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!phoneValidator.isValid(dtoUser.getPhoneNumber())){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,PHONE_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        dtoUser.setPassword(passwordEncrypter.encriptarPassword(dtoUser.getPassword()));

        BeanUser userPrepared = userSearched.get();
        userPrepared.setUsername(dtoUser.getUsername());
        userPrepared.setEmail(dtoUser.getEmail());
        userPrepared.setPassword(dtoUser.getPassword());
        userPrepared.setRfc(dtoUser.getRfc());
        userPrepared.setName(dtoUser.getName());
        userPrepared.setLastname(dtoUser.getLastname());
        userPrepared.setSurname(dtoUser.getSurname());
        userPrepared.setPhoneNumber(dtoUser.getPhoneNumber());

        try{
            BeanUser beanUserCreated = daoUser.save(userPrepared);
            Optional<BeanAuthorities> authSearched = daoAuthorities.findBeanAuthoritiesById(dtoUser.getId());
            if (!authSearched.isPresent()){
                return new ResponseEntity<>(new Message(FAILED_EXECUTION, "El gestor ha actualizar no se encuentra registrado en el sistema", FAILED, FAIL_CODE, null), HttpStatus.BAD_REQUEST);
            }
            BeanAuthorities beanAuthorities = authSearched.get();

            beanAuthorities.setUsername(dtoUser.getUsername());
            beanAuthorities.setPassword(dtoUser.getPassword());
            beanAuthorities.setEnabled(dtoUser.getEnabled());
            beanAuthorities.setAuthority("ROLE_GESTOR");

            daoAuthorities.save(beanAuthorities);
            return new ResponseEntity<>(new Message(SUCCESSFUL_UPDATE, UPDATE_SUCCESSFUL, SUCCESS,SUCCESS_CODE, beanUserCreated), HttpStatus.OK);
        }catch(Exception e){
            log.error("Ocurrió un error al editar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION, INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Object> eliminar(BeanUser beanUser) {
        Optional<BeanUser> userSearched = daoUser.findBeanUserById(beanUser.getId());

        if (!userSearched.isPresent()){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION, "El gestor ha desactivar no se encuentra registrado en el sistema", FAILED, FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        BeanUser userEnabled = userSearched.get();
        if (userEnabled.getEnabled() == 1){
            userEnabled.setEnabled(0);
        }else {
            userEnabled.setEnabled(1);
        }

        try {
            BeanUser beanUserEnabled = daoUser.save(userEnabled);
            Optional<BeanAuthorities> authSearched = daoAuthorities.findBeanAuthoritiesById(beanUser.getId());
            if (!authSearched.isPresent()){
                return new ResponseEntity<>(new Message(FAILED_EXECUTION, "El gestor ha actualizar no se encuentra registrado en el sistema", FAILED, FAIL_CODE, null), HttpStatus.BAD_REQUEST);
            }
            BeanAuthorities beanAuthorities = authSearched.get();
            if (beanAuthorities.getEnabled() == 1){
                beanAuthorities.setEnabled(0);
            }else{
                beanAuthorities.setEnabled(1);
            }
            daoAuthorities.save(beanAuthorities);
            return new ResponseEntity<>(new Message(SUCCESSFUL_UPDATE, UPDATE_SUCCESSFUL, SUCCESS,SUCCESS_CODE, beanUserEnabled), HttpStatus.OK);
        }catch (Exception e){
            log.error("Ocurrió un error al deshabilitar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION, INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Object> buscar(Long id) {
        BeanUser user = new BeanUser();
        if(!daoUser.existsBeanUserById(id)){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,"No se encuentra el gestor seleccionado", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        Optional<BeanUser> userOptional = daoUser.findBeanUserById(id);

        if(userOptional.isPresent()){
            user = userOptional.get();
        }

        return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE,user ), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> sendEmailTemplate(String email) {
        MimeMessage message = javaMailSender.createMimeMessage();

        BeanUser user = daoUser.findByEmail(email);

        if(user != null){
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                Context context = new Context();
                Map<String,Object> model = new HashMap<>();

                String oldToken = UUID.randomUUID().toString();
                oldToken = oldToken.replaceAll("-", "").toUpperCase();
                String token = oldToken.substring(0, 10);
                Instant creationTime = Instant.now();

                model.put("userName", "Hola " +user.getUsername());
                model.put("url","http://localhost:8080/resetPassword");
                model.put("token","Su código de seguridad para recuperar contraseña es: "+token);
                context.setVariables(model);

                user.setTokenPassword(token);
                user.setCreation_time(creationTime);
                daoUser.save(user);

                String htmlText = templateEngine.process("emailTemplate", context);
                helper.setFrom("20203TN058@utez.edu.mx");
                helper.setTo(user.getEmail());
                helper.setSubject("Recuperar Contraseña");
                helper.setText(htmlText, true);
                javaMailSender.send(message);
                return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH, SEND_EMAIL_SUCCESFUL, SUCCESS,SUCCESS_CODE, user), HttpStatus.OK);
            } catch (jakarta.mail.MessagingException e) {
                log.error("Ocurrió un error al buscar" + e.getMessage());
                return new ResponseEntity<>(new Message(FAILED_EXECUTION, INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>(new Message(FAILED_SEARCH,"El correo ingresado no existe", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Object> changePassword(String password, String token) {

        BeanUser user = daoUser.findBeanUserByTokenPassword(token);

        if(user == null){
            return new ResponseEntity<>(new Message(INVALID_TOKEN,"Use el código de seguridad adjuntado en su correo", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        if(!passwordValidator.isValid(password)){
            return new ResponseEntity<>(new Message(FAILED_REGISTRATION,PASSWORD_INVALID, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        if (token != null && Instant.now().minus(TOKEN_EXPIRATION).isBefore(user.getCreation_time())) {
            try {
                user.setPassword(passwordEncrypter.encriptarPassword(password));
                user.setTokenPassword(null);
                user.setCreation_time(null);
                daoUser.save(user);
                return new ResponseEntity<>(new Message(SUCCESSFUL_UPDATE, "Se ha cambiado su contraseña correctamente", SUCCESS,SUCCESS_CODE, user), HttpStatus.OK);
            }catch (Exception e){
                log.error("Ocurrió un error al buscar" + e.getMessage());
                return new ResponseEntity<>(new Message(FAILED_EXECUTION, INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            return new ResponseEntity<>(new Message(INVALID_TOKEN,"El código de seguridad ha expirado", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }


    }

    public ResponseEntity<Object> findBeanUserByUsername(String user) {
        if(!daoUser.existsBeanUserByUsername(user)){
            return new ResponseEntity<>(new Message(FAILED_SEARCH,"El usuario no existe", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        try{
            BeanUser beanUser = daoUser.findByUsername(user);
            assert beanUser != null;
            return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH, SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE, beanUser), HttpStatus.OK);
        }catch(Exception e){
            log.error("Ocurrió un error al buscar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION, INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}