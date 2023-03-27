package com.sicobo.sicobo.serviceImpl;

import com.sicobo.sicobo.dao.DaoPayment;
import com.sicobo.sicobo.dao.DaoSite;
import com.sicobo.sicobo.dao.DaoSiteAssigment;
import com.sicobo.sicobo.dao.DaoWarehouse;
import com.sicobo.sicobo.dto.DTOSite;
import com.sicobo.sicobo.model.BeanSite;
import com.sicobo.sicobo.model.BeanState;
import com.sicobo.sicobo.model.BeanUser;
import com.sicobo.sicobo.model.BeanWarehouse;
import com.sicobo.sicobo.service.ISiteService;
import com.sicobo.sicobo.util.Message;
import com.sicobo.sicobo.util.SiteValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.sicobo.sicobo.util.Constantes.MessageBody.*;
import static com.sicobo.sicobo.util.Constantes.MessageCodes.*;
import static com.sicobo.sicobo.util.Constantes.MessageHeaders.*;
import static com.sicobo.sicobo.util.Constantes.MessageType.*;
import static com.sicobo.sicobo.util.Constantes.Stuff.*;

@Service
@Slf4j
public class SiteServiceImpl implements ISiteService {

    @Autowired
    private DaoSite daoSite;

    @Autowired
    private DaoSiteAssigment daoSiteAssigment;

    @Autowired
    private DaoWarehouse daoWarehouse;

    @Autowired
    private DaoPayment daoPayment;

    private SiteValidator siteValidator = new SiteValidator();

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> listar() {
        return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,"La consulta de sitios ha sido exitosa", SUCCESS,SUCCESS_CODE, daoSite.findAll()), HttpStatus.OK);
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> guardar(DTOSite dtoSite) {
        BeanSite beanSite = new BeanSite(dtoSite.getName(), 0, dtoSite.getAddress(), new BeanState((long)dtoSite.getBeanState()));
        if(daoSite.existsBeanSiteByName(beanSite.getName())){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,"El nombre que has elegido ya está en uso. Por favor, elige otro nombre y vuelve a intentarlo", FAILED, FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        if( siteValidator.validName(beanSite.getName())){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,ERROR_NAME, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        if( siteValidator.validAddress(beanSite.getAddress())){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,ERROR_ADDRESS, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        if( siteValidator.validState(beanSite.getBeanState().getId())){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,ERROR_STATE, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }


        try{
            BeanSite beanSiteCreated = daoSite.save(beanSite);
            return new ResponseEntity<>(new Message(SUCCESSFUL_REGISTRATION, INSERT_SUCCESSFUL, SUCCESS,SUCCESS_CODE, beanSiteCreated), HttpStatus.OK);
        }catch(Exception e){
            log.error("Ocurrio un error en SiteServiceImpl - guardar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object>   editar(DTOSite dtoSite) {
        Optional<BeanSite> siteSearched = daoSite.findBeanSiteById(dtoSite.getId());
        if(daoSite.existsBeanSiteByNameAndIdNot(dtoSite.getName(), dtoSite.getId())){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,"El nombre que has elegido ya está en uso. Por favor, elige otro nombre y vuelve a intentarlo", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(!siteSearched.isPresent()){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,"El sitio ha actualizar no se encuentra registrado en el sistema", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if( siteValidator.validName(dtoSite.getName())){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,ERROR_NAME, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        if( siteValidator.validAddress(dtoSite.getAddress())){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,ERROR_ADDRESS, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        if( siteValidator.validState((long) dtoSite.getBeanState())){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,ERROR_STATE, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        BeanSite sitePrepared =siteSearched.get();
        sitePrepared.setName(dtoSite.getName());
        sitePrepared.setAddress(dtoSite.getAddress());
        sitePrepared.setBeanState(new BeanState((long) dtoSite.getBeanState()));
        try{
            BeanSite beanSiteUpdated = daoSite.save(sitePrepared);
            return new ResponseEntity<>(new Message(SUCCESSFUL_UPDATE,UPDATE_SUCCESSFUL, SUCCESS,SUCCESS_CODE, beanSiteUpdated), HttpStatus.OK);
        }catch(Exception e){
            log.error("Ocurrio un error en SiteServiceImpl - editar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> eliminar(BeanSite beanSite) {
        Optional<BeanSite> siteSearched = daoSite.findBeanSiteById(beanSite.getId());
        String typeChange = beanSite.getStatus() == 1 ? "habilitar" : "deshabilitar";
        String typeMethod = beanSite.getStatus() == 1 ? "habilitacion" : "deshabilitacion";
        String typeTitle= beanSite.getStatus() == 1 ? "Habilitacion" : "Deshabilitacion";
        if(!siteSearched.isPresent()){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,"El sitio ha "+ typeChange +" no se encuentra registrado en el sistema", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

       /* if(beanSite.getStatus() == 1  && !daoSiteAssigment.existsBeanSiteAssigmentByBeanSite_Id(beanSite.getId())){
            return new ResponseEntity(new Message("Ejecución fallida","El sitio ha "+ typeChange +" no contiene gestores para ser administrado", "failed",400, null), HttpStatus.BAD_REQUEST);
        }

        if(beanSite.getStatus() == 0 && daoWarehouse.existsBeanWarehouseByBeanSite_Id(beanSite.getId())){
            List<BeanWarehouse> warehousesBySite = daoWarehouse.findBeanWarehouseByBeanSite_Id(beanSite.getId());
            for (BeanWarehouse warehouse:
                 warehousesBySite) {
                if(daoPayment.existsBeanPaymentByBeanWarehouse_IdAndStatusEquals(warehouse.getId(), 1)){
                    return new ResponseEntity(new Message("Ejecución fallida","El sitio ha "+ typeChange +" contiene bodegas", "failed",400, null), HttpStatus.BAD_REQUEST);
                }
            }
        }*/

        BeanSite sitePrepared =siteSearched.get();
        sitePrepared.setStatus(beanSite.getStatus());
        try{
            BeanSite beanSiteUpdated = daoSite.save(sitePrepared);
            return new ResponseEntity<>(new Message(typeTitle + " exitosa","Se ha realizado la " + typeMethod+ " exitosamente", SUCCESS,SUCCESS_CODE, beanSiteUpdated), HttpStatus.OK);
        }catch(Exception e){
            log.error("Ocurrio un error en SiteServiceImpl - eliminar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> buscar(Long id) {
        if(!daoSite.existsBeanSiteById(id)){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,"No se encuentra el sitio seleccionado", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if( siteValidator.validState(id)){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,ERROR_STATE, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        BeanSite site = daoSite.findBeanSiteById(id).get();
        return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE,site ), HttpStatus.OK);
    }

    public ResponseEntity<Object> findBeanSiteByBeanUser(BeanUser beanUser) {
        if(!daoSite.existsByBeanUser(beanUser)){
            return new ResponseEntity<>(new Message(FAILED_SEARCH,"El usuario no pertenece a un sitio", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        try{
            List<BeanSite> beanSites = daoSite.findByBeanUser(beanUser);
            assert beanSites != null;
            return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH, SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE, beanSites.get(0)), HttpStatus.OK);
        }catch(Exception e){
            log.error("Ocurrió un error al buscar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION, INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
