package com.sicobo.sicobo.serviceImpl;

import com.sicobo.sicobo.dao.DaoUser;
import com.sicobo.sicobo.dto.DTOUser;
import com.sicobo.sicobo.model.BeanUser;
import com.sicobo.sicobo.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements IUserService //UserDetailsService
{

    @Autowired
    private DaoUser daoUser;

    @Override
    public ResponseEntity<Object> listar() {
        return null;
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> registrar(DTOUser dtoUser) {
        return null;
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

    /*
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BeanUser usuario = daoUser.findByUsername(username);

        if(usuario == null){
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> grantedAuthorities;
        grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(usuario.getRole().toString()));
        return new User(usuario.getUsername(), usuario.getPassword(), grantedAuthorities);
    }
    */

}