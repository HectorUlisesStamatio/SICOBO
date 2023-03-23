package com.sicobo.sicobo.serviceImpl;

import com.sicobo.sicobo.dao.DaoUser;
import com.sicobo.sicobo.dto.DTOUser;
import com.sicobo.sicobo.model.BeanUser;
import com.sicobo.sicobo.service.IUserService;
import com.sicobo.sicobo.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;


@Service
@Slf4j
public class UserServiceImpl implements IUserService
{

    @Autowired
    private DaoUser daoUser;
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

    @Override
    public ResponseEntity<Object> listar() {
        return null;
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> registrar(DTOUser dtoUser) {
        BeanUser beanUser = new BeanUser(dtoUser.getName(),dtoUser.getLastname(),dtoUser.getSurname(),dtoUser.getEmail(),dtoUser.getRfc(),dtoUser.getPhone_number(), dtoUser.getUsername(),dtoUser.getPassword(),"ROLE_USUARIO",0,dtoUser.getPolicy_acceptance(),1);

        if(daoUser.existsBeanUserByUsername(beanUser.getUsername())){
            return new ResponseEntity(new Message("Registro fallido","El usuario que has elegido ya está en uso. Por favor, elige otro usuario y vuelve a intentarlo", "failed",400, null), HttpStatus.BAD_REQUEST);
        }
        if(daoUser.existsBeanUserByEmail(beanUser.getEmail())){
            return new ResponseEntity(new Message("Registro fallido","El correo que has elegido ya está en uso. Por favor, elige otro correo y vuelve a intentarlo", "failed",400, null), HttpStatus.BAD_REQUEST);
        }
        if(!passwordValidator.isValid(beanUser.getPassword())){
            return new ResponseEntity(new Message("Registro fallido","La contraseña que has elegido es inválida. Por favor, elige otra contraseña y vuelve a intentarlo", "failed",400, null), HttpStatus.BAD_REQUEST);
        }
        if(!rfcValidator.isValid(beanUser.getRfc())){
            return new ResponseEntity(new Message("Registro fallido","El RFC que has ingresado es inválido. Por favor, escribe el RFC correctamente y vuelve a intentarlo", "failed",400, null), HttpStatus.BAD_REQUEST);
        }
        if(!textValidator.isValid(beanUser.getUsername())){
            return new ResponseEntity(new Message("Registro fallido","El usuario que has ingresado es inválido. Por favor, escribe el usuario correctamente y vuelve a intentarlo", "failed",400, null), HttpStatus.BAD_REQUEST);
        }
        if(!textValidator.isValid(beanUser.getName())){
            return new ResponseEntity(new Message("Registro fallido","El nombre que has ingresado es inválido. Por favor, escribe el nombre correctamente y vuelve a intentarlo", "failed",400, null), HttpStatus.BAD_REQUEST);
        }
        if(!textValidator.isValid(beanUser.getLastname())){
            return new ResponseEntity(new Message("Registro fallido","El apellido que has ingresado es inválido. Por favor, escribe el apellido correctamente y vuelve a intentarlo", "failed",400, null), HttpStatus.BAD_REQUEST);
        }
        if(!textValidator.isValid(beanUser.getSurname())){
            return new ResponseEntity(new Message("Registro fallido","El apellido que has ingresado es inválido. Por favor, escribe el apellido correctamente y vuelve a intentarlo", "failed",400, null), HttpStatus.BAD_REQUEST);
        }
        beanUser.setPhone_number(dtoUser.getExt() + beanUser.getPhone_number());
        if(!phoneValidator.isValid(beanUser.getPhone_number())){
            return new ResponseEntity(new Message("Registro fallido","El teléfono que has ingresado es inválido. Por favor, escribe el teléfono correctamente y vuelve a intentarlo", "failed",400, null), HttpStatus.BAD_REQUEST);
        }
        beanUser.setPassword(passwordEncrypter.encriptarPassword(beanUser.getPassword()));
        try{
            BeanUser beanUserCreated = daoUser.save(beanUser);
            return new ResponseEntity(new Message("Registro exitoso","Se ha realizado el registro exitosamente", "success",200, beanUserCreated), HttpStatus.OK);
        }catch(Exception e){
            log.error("Ocurrió un error al registrar" + e.getMessage());
            return new ResponseEntity(new Message("Ejecución fallida","Ocurrio un error interno", "failed",500, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Object> guardar(DTOUser dtoUser) {
        return null;
    }

    @Override
    public ResponseEntity<Object> editar(DTOUser dtoUser) {
        return null;
    }

    @Override
    public ResponseEntity<Object> eliminar(BeanUser beanUser) {
        return null;
    }

    @Override
    public ResponseEntity<Object> buscar(Long id) {
        return null;
    }


}