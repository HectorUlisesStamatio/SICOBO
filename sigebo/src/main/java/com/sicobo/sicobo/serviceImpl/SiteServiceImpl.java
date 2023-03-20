package com.sicobo.sicobo.serviceImpl;

import com.sicobo.sicobo.dao.DaoSite;
import com.sicobo.sicobo.dto.DTOSite;
import com.sicobo.sicobo.model.BeanSite;
import com.sicobo.sicobo.model.BeanState;
import com.sicobo.sicobo.service.ISiteService;
import com.sicobo.sicobo.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
@Slf4j
public class SiteServiceImpl implements ISiteService {

    @Autowired
    private DaoSite daoSite;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> listar() {
        return new ResponseEntity(new Message("Consulta exitosa","La consulta de sitios ha sido exitosa", "success",200, daoSite.findAll()), HttpStatus.OK);
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> guardar(DTOSite dtoSite) {
        BeanSite beanSite = new BeanSite(dtoSite.getName(), 0, dtoSite.getAddress(), new BeanState((long)dtoSite.getBeanState()));
        if(daoSite.existsBeanSiteByName(beanSite.getName())){
            return new ResponseEntity(new Message("Ejecución fallida","El nombre que has elegido ya está en uso. Por favor, elige otro nombre y vuelve a intentarlo", "failed",400, null), HttpStatus.BAD_REQUEST);
        }

        try{
            BeanSite beanSiteCreated = daoSite.save(beanSite);
            return new ResponseEntity(new Message("Registro exitoso","Se ha realizado el registro exitosamente", "success",200, beanSiteCreated), HttpStatus.OK);
        }catch(Exception e){
            log.error("Ocurrio un error en SiteServiceImpl - guardar" + e.getMessage());
            return new ResponseEntity(new Message("Ejecución fallida","Ocurrio un error interno", "failed",200, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Object> editar(DTOSite dtoSite) {
        return null;
    }

    @Override
    public ResponseEntity<Object> eliminar(BeanSite beanSite) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> buscar(Long id) {
        if(!daoSite.existsBeanSiteById(id)){
            return new ResponseEntity(new Message("Ejecución fallida","No se encuentra el sitio seleccionado", "failed",400, null), HttpStatus.BAD_REQUEST);
        }
        BeanSite site = daoSite.findBeanSiteById(id);
        return new ResponseEntity(new Message("Consulta exitosa","Se ha realizado la consulta de forma exitosa", "success",200,site ), HttpStatus.OK);
    }


}
