package com.sicobo.sicobo.serviceimpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sicobo.sicobo.dao.*;
import com.sicobo.sicobo.dto.DTOWarehouse;
import com.sicobo.sicobo.model.*;
import com.sicobo.sicobo.service.IWarehouseService;
import com.sicobo.sicobo.util.Message;
import com.sicobo.sicobo.util.PaymentValidator;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private DaoPayment daoPayment;

    private Cloudinary cloudinary;

    private PaymentValidator paymentValidator = new PaymentValidator();

    public WarehouseServiceImpl() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dg4kl7fvl",
                "api_key", "568213252853666",
                "api_secret", "jqtMCxrN3MNXN6PVwZ4Sf_E0pdE"));
    }

    @Override
    public ResponseEntity<List<Object[]>> listar(Long id, String username) {
        List<Object[]> resultados = daoWarehouse.findAllClienteWarehousesDetails(id, username);
        return new ResponseEntity<>(resultados, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> findWarehousesByBeanSite(BeanSite beanSite) {
        List<BeanWarehouse> beanWarehouses = new ArrayList<>();
        if(!daoWarehouse.existsBeanWarehouseByBeanSite(beanSite)){
            return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,"El sitio no cuenta con bodegas", FAILED,SUCCESS_CODE, beanWarehouses), HttpStatus.BAD_REQUEST);
        }

        try{
            beanWarehouses = daoWarehouse.findBeanWarehouseByBeanSite(beanSite);
            assert beanWarehouses != null;
            return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE,beanWarehouses), HttpStatus.OK);
        }catch(Exception e){
            log.error("Ocurrió un error en  WarehouseServiceImpl - buscar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION, INTERNAL_ERROR, FAILED, SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> findWarehousesByBeanSiteId(Long id) {
            return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH,SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE, daoWarehouse.findAllByBeanSiteId(id)), HttpStatus.OK);
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

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> detalleBodega(Long id) {
        BeanWarehouse beanWarehouse;
        try {
            if (!daoWarehouse.existsBeanWarehouseByIdAndStatusIs(id, 1)) {
                if (daoWarehouse.existsBeanWarehouseByIdAndStatusIsRented(id)) {
                    return new ResponseEntity<>(new Message(FAILED_EXECUTION, "No es posible rentar una bodega en renta", FAILED, FAIL_CODE, null), HttpStatus.BAD_REQUEST);
                } else {
                    return new ResponseEntity<>(new Message(FAILED_EXECUTION, "No se encuentra la bodega seleccionada", FAILED, FAIL_CODE, null), HttpStatus.BAD_REQUEST);
                }
            }


            Optional<BeanWarehouse> beanWarehouseOptional = daoWarehouse.findBeanWarehouseById(id);
            List<BeanWarehouseImage> warehouseImages = daoWarehouseImage.findAllByBeanWarehouseId(id);
            if (beanWarehouseOptional.isPresent()) {
                beanWarehouse = beanWarehouseOptional.get();
                beanWarehouse.setImages(warehouseImages);
                assert beanWarehouse != null;
                return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH, SEARCH_SUCCESSFUL, SUCCESS, SUCCESS_CODE, beanWarehouse), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Message(FAILED_EXECUTION, "No fue posible obtener la bodega para renta", FAILED, FAIL_CODE, null), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Ocurrio un error en WarehouseServiceImpl - detalleBodega" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION, INTERNAL_ERROR, FAILED, SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Object> buscarBodegaPorUsername(String username) {

        List<Object[]> warehouses = daoWarehouse.findAllWarehousesByClient(username);

        if (warehouses == null){
            return new ResponseEntity<>(new Message(FAILED_SEARCH,"El usuario no existe", FAILED,FAIL_CODE, null), HttpStatus.BAD_REQUEST);
        }

        List<BeanWarehouseForClient> warehouseForClients = new ArrayList<>();

        for (Object[] warehouse : warehouses) {
            String siteName = warehouse[0].toString();
            String warehouseDescription = warehouse[1].toString();
            String warehouseTypeDescription = warehouse[2].toString();
            String warehouseStatus = warehouse[3].toString();
            String stateName = warehouse[4].toString();
            String urlImage = warehouse[5].toString();
            Long paymentId = Long.parseLong( warehouse[6].toString());
            Date dueDate = (Date) warehouse[7];
            Date paymentDate = (Date) warehouse[8];
            Long paymentStatus = Long.parseLong( warehouse[9].toString());
            boolean isRenovation = !paymentValidator.validDate(dueDate, new Date()) && !paymentValidator.validDateOut(dueDate, new Date()) && (paymentStatus == 1);

            BeanWarehouseForClient beanWarehouseForClient = new BeanWarehouseForClient(siteName, warehouseDescription, warehouseTypeDescription, warehouseStatus, stateName, urlImage, paymentId,dueDate,paymentDate, paymentStatus, isRenovation);
            warehouseForClients.add(beanWarehouseForClient);
        }

        return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH, SEARCH_SUCCESSFUL, SUCCESS,SUCCESS_CODE, warehouseForClients), HttpStatus.OK);

    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Object> eliminarImagenes(Long id) {
        List<BeanWarehouseImage> beanWarehouseImages = daoWarehouseImage.findAllByBeanWarehouseId(id);

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

    @Override
    public ResponseEntity<Object>   rentar(Long id) {
        Optional<BeanWarehouse> warehouseSearched = daoWarehouse.findBeanWarehouseById(id);
        boolean existWarehouse = daoWarehouse.existsBeanWarehouseByIdAndStatusIs(id, 1);
        boolean warehouseisRented = daoWarehouse.existsBeanWarehouseByIdAndStatusIsRented(id);
        try {
            if (!warehouseSearched.isPresent()) {
                return new ResponseEntity<>(new Message(FAILED_EXECUTION, "No se encuentra la bodega seleccionada", FAILED, FAIL_CODE, null), HttpStatus.BAD_REQUEST);
            }
            if (!existWarehouse) {
                if (warehouseisRented) {
                    return new ResponseEntity<>(new Message(FAILED_EXECUTION, "No es posible rentar una bodega en renta", FAILED, FAIL_CODE, null), HttpStatus.BAD_REQUEST);
                }else{
                    return new ResponseEntity<>(new Message(FAILED_EXECUTION, "No se encuentra la bodega jsjbsngshgs", FAILED, FAIL_CODE, null), HttpStatus.BAD_REQUEST);
                }
            }


            BeanWarehouse warehouse = warehouseSearched.get();
            warehouse.setStatus(2);

            BeanWarehouse warehouseRented = daoWarehouse.save(warehouse);
            return new ResponseEntity<>(new Message(SUCCESSFUL_UPDATE, UPDATE_SUCCESSFUL, SUCCESS,SUCCESS_CODE, warehouseRented), HttpStatus.OK);

        }catch (Exception e){
            log.error("Ocurrio un error en WarehouseServiceImpl - rentar" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }

    @Override
    public ResponseEntity<Object> detalleBodegaRentada(Long id, BeanUser user) {
        BeanWarehouse beanWarehouse;

        boolean existWarehouse = daoWarehouse.existsBeanWarehouseByIdAndStatusIsOrStatusIs(id, 1, 2);
        boolean isMineWarehouse = daoPayment.existsBeanPaymentByBeanWarehouseIdAndBeanUserId(id, user.getId());
        boolean existPaymentActive = daoPayment.existsBeanPaymentByBeanWarehouseIdAndBeanUserIdAndStatusIs(id, user.getId(), 1);
        try{
            if(!existWarehouse){
                return new ResponseEntity<>(new Message(FAILED_EXECUTION, "No se encuentra la bodega seleccionada", FAILED, FAIL_CODE, null), HttpStatus.BAD_REQUEST);
            }
            if(!isMineWarehouse){
                return new ResponseEntity<>(new Message(FAILED_EXECUTION, "No eres propietario de la bodega", FAILED, FAIL_CODE, null), HttpStatus.BAD_REQUEST);
            }
            if(!existPaymentActive){
                return new ResponseEntity<>(new Message(FAILED_EXECUTION, "El pago de tu bodega ha vencido", FAILED, FAIL_CODE, null), HttpStatus.BAD_REQUEST);
            }

            Optional<BeanWarehouse> beanWarehouseOptional = daoWarehouse.findBeanWarehouseById(id);
            List<BeanWarehouseImage> warehouseImages = daoWarehouseImage.findAllByBeanWarehouseId(id);
            if (beanWarehouseOptional.isPresent()) {
                beanWarehouse = beanWarehouseOptional.get();
                beanWarehouse.setImages(warehouseImages);
                assert beanWarehouse != null;
                return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH, SEARCH_SUCCESSFUL, SUCCESS, SUCCESS_CODE, beanWarehouse), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Message(FAILED_EXECUTION, "No fue posible obtener la bodega para renta", FAILED, FAIL_CODE, null), HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            log.error("Ocurrio un error en WarehouseServiceImpl - detalleBodegaRentada" + e.getMessage());
            return new ResponseEntity<>(new Message(FAILED_EXECUTION,INTERNAL_ERROR, FAILED,SERVER_FAIL_CODE, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Scheduled(cron = "0 29 16 ? * *")
    public void desalojarBodega() {

    }


}
