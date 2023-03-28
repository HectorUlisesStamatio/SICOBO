package com.sicobo.sicobo.serviceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sicobo.sicobo.dao.DaoSiteAssigment;
import com.sicobo.sicobo.dao.DaoUser;
import com.sicobo.sicobo.dao.DaoWarehouse;
import com.sicobo.sicobo.dao.DaoWarehouseImage;
import com.sicobo.sicobo.dto.DTOWarehouse;
import com.sicobo.sicobo.model.*;
import com.sicobo.sicobo.service.IWarehouseService;
import com.sicobo.sicobo.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            return new ResponseEntity<>(new Message(FAILED_SEARCH,"El sitio no cuenta con bodegas", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
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

    @Override
    public ResponseEntity<Object> guardar(DTOWarehouse dtoWarehouse) {
        try{
            List<MultipartFile> images = dtoWarehouse.getImages();

            if (images != null && images.size() > 0) {
                BeanWarehouse beanWarehouse = new BeanWarehouse();

                BeanSite beanSite = new BeanSite();
                beanSite.setId((long) dtoWarehouse.getBeanSite());
                beanWarehouse.setBeanSite(beanSite);

                BeanWarehousesType beanWarehousesType = new BeanWarehousesType();
                beanWarehousesType.setId((long) dtoWarehouse.getWarehousesType());
                beanWarehouse.setWarehousesType(beanWarehousesType);

                beanWarehouse.setStatus(1);
                beanWarehouse.setSection(dtoWarehouse.getSection());
                beanWarehouse.setDescription(dtoWarehouse.getDescription());
                beanWarehouse.setFinalCost(dtoWarehouse.getFinalCost());

                BeanWarehouse beanWarehouseCreated = daoWarehouse.save(beanWarehouse);

                int i = 0;
                for (MultipartFile image : images) {
                        BeanWarehouseImage beanWarehouseImage = new BeanWarehouseImage();
                        beanWarehouseImage.setBeanWarehouse(beanWarehouse);
                        System.out.println("Imagen "+i);
                        i++;
                        Map<?, ?> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                        String url = (String) uploadResult.get("url");
                        String secureUrl = (String) uploadResult.get("secure_url");
                        String publicId = (String) uploadResult.get("public_id");

                        beanWarehouseImage.setUrl(url);
                        beanWarehouseImage.setSecureUrl(secureUrl);
                        beanWarehouseImage.setPublicId(publicId);
                        System.out.println(beanWarehouseImage);
                        daoWarehouseImage.save(beanWarehouseImage);
                }

                return new ResponseEntity<>(new Message(SUCCESSFUL_REGISTRATION, INSERT_SUCCESSFUL, SUCCESS,SUCCESS_CODE, beanWarehouseCreated), HttpStatus.OK);
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
    public ResponseEntity<Object> eliminar(BeanWarehouse beanWarehouse) {
        return null;
    }

    @Override
    public ResponseEntity<Object> buscar(Long id) {
        return null;
    }
}
