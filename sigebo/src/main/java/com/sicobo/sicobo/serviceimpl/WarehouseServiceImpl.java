package com.sicobo.sicobo.serviceimpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.sicobo.sicobo.dao.DaoSiteAssigment;
import com.sicobo.sicobo.dao.DaoUser;
import com.sicobo.sicobo.dao.DaoWarehouse;
import com.sicobo.sicobo.dao.DaoWarehouseImage;
import com.sicobo.sicobo.dto.DTOWarehouse;
import com.sicobo.sicobo.model.*;
import com.sicobo.sicobo.service.IWarehouseService;
import com.sicobo.sicobo.util.Message;
import com.sicobo.sicobo.util.WarehouseValidator;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.sicobo.sicobo.util.Constantes.MessageBody.*;
import static com.sicobo.sicobo.util.Constantes.MessageCodes.*;
import static com.sicobo.sicobo.util.Constantes.MessageHeaders.*;
import static com.sicobo.sicobo.util.Constantes.MessageType.FAILED;
import static com.sicobo.sicobo.util.Constantes.MessageType.SUCCESS;

@Service
@Slf4j
public class WarehouseServiceImpl implements IWarehouseService {

    @Autowired
    DaoWarehouse daoWarehouse;

    @Autowired
    DaoUser daoUser;

    @Autowired
    DaoSiteAssigment daoSiteAssigment;

    @Autowired
    DaoWarehouseImage daoWarehouseImage;

    private Cloudinary cloudinary;

    public WarehouseServiceImpl() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dg4kl7fvl",
                "api_key", "568213252853666",
                "api_secret", "jqtMCxrN3MNXN6PVwZ4Sf_E0pdE"));
    }

    @Override
    public ResponseEntity<Object> listar() {
        return null;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> findWarehousesByBeanSite(BeanSite beanSite) {
        if(!daoWarehouse.existsBeanWarehouseByBeanSite(beanSite)){
            return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,"El sitio no cuenta con bodegas", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        try{
            List<BeanWarehouse> beanWarehouses = daoWarehouse.findBeanWarehouseByBeanSite(beanSite);
            assert beanWarehouses != null;
            return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE,beanWarehouses), HttpStatus.OK);
        }catch(Exception e){
            log.error("Ocurrió un error en  WarehouseServiceImpl - buscar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION, INTERNAL_ERROR, FAILED, SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> findWarehousesByBeanSiteId(Long id) {
            return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE, daoWarehouse.findAllByBeanSite_Id(id)), HttpStatus.OK);


    }

    @Override
    public ResponseEntity<Object> guardar(DTOWarehouse dtoWarehouse) {
        try{
            List<MultipartFile> images = dtoWarehouse.getImages();
            boolean flag = dtoWarehouse.getId() != 0;
            if (images != null && !images.isEmpty()) {
                BeanWarehouse beanWarehouse = new BeanWarehouse();
                if (flag){
                    beanWarehouse.setId(dtoWarehouse.getId());
                    beanWarehouse.setFechaCreacion(dtoWarehouse.getFechaCreacion());
                }
                BeanSite beanSite = new BeanSite();
                beanSite.setId((long) dtoWarehouse.getBeanSite());
                beanWarehouse.setBeanSite(beanSite);

                BeanWarehousesType beanWarehousesType = new BeanWarehousesType();
                beanWarehousesType.setId((long) dtoWarehouse.getWarehousesType());
                beanWarehouse.setWarehousesType(beanWarehousesType);

                beanWarehouse.setStatus(dtoWarehouse.getStatus());
                beanWarehouse.setSection(dtoWarehouse.getSection());
                beanWarehouse.setDescription(dtoWarehouse.getDescription());
                beanWarehouse.setFinalCost(dtoWarehouse.getFinalCost());

                BeanWarehouse beanWarehouseCreated = daoWarehouse.save(beanWarehouse);

                for (MultipartFile image : images) {
                        BeanWarehouseImage beanWarehouseImage = new BeanWarehouseImage();
                        beanWarehouseImage.setBeanWarehouse(beanWarehouse);
                        Map<?, ?> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                        String url = (String) uploadResult.get("url");
                        String secureUrl = (String) uploadResult.get("secure_url");
                        String publicId = (String) uploadResult.get("public_id");

                        beanWarehouseImage.setUrl(url);
                        beanWarehouseImage.setSecureUrl(secureUrl);
                        beanWarehouseImage.setPublicId(publicId);
                        daoWarehouseImage.save(beanWarehouseImage);
                }
                if (flag){
                    return new ResponseEntity<>(new Message(SUCCESSFUL_UPDATE, UPDATE_SUCCESSFUL, SUCCESS,SUCCESS_CODE, beanWarehouseCreated), HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(new Message(SUCCESSFUL_REGISTRATION, INSERT_SUCCESSFUL, SUCCESS,SUCCESS_CODE, beanWarehouseCreated), HttpStatus.OK);
                }

            }else{
                return new ResponseEntity<>(new Message(FAILED_REGISTRATION,ERROR_IMAGES, FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
            }
        }catch(Exception e){
            log.error("Ocurrio un error en WarehouseServiceImpl - guardar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Object> editar(DTOWarehouse dtoWarehouse) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> eliminar(BeanWarehouse beanWarehouse) {
        Optional<BeanWarehouse> beanWarehouseOpt = daoWarehouse.findBeanWarehouseById(beanWarehouse.getId());
        String typeChange = beanWarehouse.getStatus() == 1 ? "habilitar" : "deshabilitar";
        String typeMethod = beanWarehouse.getStatus() == 1 ? "habilitacion" : "deshabilitacion";
        String typeTitle= beanWarehouse.getStatus() == 1 ? "Habilitacion" : "Deshabilitacion";
        if(!beanWarehouseOpt.isPresent()){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,"La bodega ha "+ typeChange +" no se encuentra registrada en el sistema", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        BeanWarehouse beanWarehouseSave = beanWarehouseOpt.get();
        beanWarehouseSave.setStatus(beanWarehouse.getStatus());
        try{
            daoWarehouse.save(beanWarehouseSave);
            return new ResponseEntity<>(new Message(typeTitle + " exitosa","Se ha realizado la " + typeMethod+ " exitosamente", SUCCESS,SUCCESS_CODE, beanWarehouseSave), HttpStatus.OK);
        }catch(Exception e){
            log.error("Ocurrio un error en WarehouseServiceImpl - eliminar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> buscar(Long id) {
        BeanWarehouse beanWarehouse;
        if(!daoWarehouse.existsBeanWarehouseById(id)){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,"No se encuentra la bodega seleccionada", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
        if(daoWarehouse.existsBeanWarehouseByIdAndStatusIsRented(id)){
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,"No es posible modificar una bodega en renta", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        Optional<BeanWarehouse> beanWarehouseOptional = daoWarehouse.findBeanWarehouseById(id);

        if(beanWarehouseOptional.isPresent()){
            beanWarehouse = beanWarehouseOptional.get();
            return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE,beanWarehouse ), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,"No fue posible obtener la bodega para modificaci[on", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> eliminarImagenes(Long id) {
        List<BeanWarehouseImage> beanWarehouseImages = daoWarehouseImage.findAllByBeanWarehouse_Id(id);

        for (BeanWarehouseImage beanWarehouseImage : beanWarehouseImages){
            try {
                cloudinary.uploader().destroy(beanWarehouseImage.getPublicId(), ObjectUtils.emptyMap());
                daoWarehouseImage.deleteById(beanWarehouseImage.getId());
            } catch (Exception e) {
                log.error("Ocurrio un error en WarehouseServiceImpl - eliminarImagenes" + e.getMessage());
                return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(new Message(SUCCESSFUL_DELETE,DELETE_SUCCESSFUL, SUCCESS,SUCCESS_CODE,null ), HttpStatus.OK);
    }
}
