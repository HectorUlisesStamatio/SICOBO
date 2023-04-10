package com.sicobo.sicobo.serviceimpl;

import com.sicobo.sicobo.dao.DaoWarehouse;
import com.sicobo.sicobo.model.BeanWarehouseDetails;
import com.sicobo.sicobo.service.IWarehouseDetails;
import com.sicobo.sicobo.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sicobo.sicobo.util.Constantes.MessageCodes.SUCCESS_CODE;
import static com.sicobo.sicobo.util.Constantes.MessageHeaders.SUCCESSFUL_SEARCH;
import static com.sicobo.sicobo.util.Constantes.MessageType.SUCCESS;

@Service
@Slf4j
public class WarehouseDetailsServiceImpl implements IWarehouseDetails {
    @Autowired
    private DaoWarehouse warehouseRepository;

    @Override
    public ResponseEntity<Object> listar(String parametroUno,String parametroDos) {
        int bandera = 0;
        List<Object[]> bodegas = warehouseRepository.findAllWarehouseDetails();
        List<BeanWarehouseDetails> warehouseDetailsList = new ArrayList<>();
        List<BeanWarehouseDetails> warehouseDetailsListMostrar = new ArrayList<>();
        warehouseDetailsListMostrar.clear();
        for(Object[] warehouse : bodegas){
            String finalCost = "$"+warehouse[0].toString();
            String section = warehouse[1].toString();
            String description = warehouse[2].toString();
            String secure_url = warehouse[3].toString();
            String costo = warehouse[4].toString();
            String addres = warehouse[5].toString();
            String name = warehouse[6].toString();
            String id = warehouse[7].toString();
            BeanWarehouseDetails beanWarehouseDetails = new BeanWarehouseDetails(finalCost,section,description,secure_url,costo,addres,name, id);
            warehouseDetailsList.add(beanWarehouseDetails);
        }

        try{
            float costo = Float.parseFloat(parametroUno);
            bandera =1;
        }catch (Exception e){
            bandera = 0;
        }

        if(parametroUno!=null && parametroDos == null ) {
            if (bandera == 0) {
                for (int i = 0; i < bodegas.size(); i++) {
                    if (warehouseDetailsList.get(i).getName().equals(parametroUno)) {
                        warehouseDetailsListMostrar.add(warehouseDetailsList.get(i));
                    }
                }
                return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH, "La consulta de bodegas ha sido exitosa", SUCCESS, SUCCESS_CODE, warehouseDetailsListMostrar), HttpStatus.OK);
            } else {
                for (int i = 0; i < bodegas.size(); i++) {
                    if (warehouseDetailsList.get(i).getFinal_cost().equals("$" + parametroUno)) {
                        warehouseDetailsListMostrar.add(warehouseDetailsList.get(i));
                    }
                }
                return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH, "La consulta de bodegas ha sido exitosa", SUCCESS, SUCCESS_CODE, warehouseDetailsListMostrar), HttpStatus.OK);
            }
        }else if(parametroUno != null && parametroDos != null){
            for (int i = 0; i < bodegas.size(); i++) {
                if (warehouseDetailsList.get(i).getFinal_cost().equals("$" + parametroDos) && warehouseDetailsList.get(i).getName().equals(parametroUno)) {
                    warehouseDetailsListMostrar.add(warehouseDetailsList.get(i));
                }
            }
            return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH, "La consulta de bodegas ha sido exitosa", SUCCESS, SUCCESS_CODE, warehouseDetailsListMostrar), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new Message(SUCCESSFUL_SEARCH, "La consulta de bodegas ha sido exitosa",SUCCESS, SUCCESS_CODE,warehouseDetailsList), HttpStatus.OK);
        }

    }
}
