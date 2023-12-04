/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.ccat.gateway.services;

import com.asset.ccat.gateway.defines.ErrorCodes;
import com.asset.ccat.gateway.exceptions.GatewayException;
import com.asset.ccat.gateway.logger.CCATLogger;
import com.asset.ccat.gateway.mappers.IMapper;
import com.asset.ccat.gateway.models.customer_care.ServiceClassModel;
import com.asset.ccat.gateway.models.requests.service_class.ServiceClassConversionRequest;
import com.asset.ccat.gateway.models.requests.service_class.ServiceClassRequest;
import com.asset.ccat.gateway.models.requests.service_class.UpdateServiceClassRequest;
import com.asset.ccat.gateway.proxy.admin.ServiceClassesProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wael.mohamed
 */
@Service
public class ServiceClassesService {

    @Autowired
    ServiceClassesProxy serviceClassesProxy;

    @Autowired
    @Qualifier("UpdateServiceClassMapper")
    IMapper iUpdateServiceRequestMapper;
    @Autowired
    @Qualifier("ServiceClassConversionMapper")
    IMapper iServiceClassConversionMapper;

    public List<ServiceClassModel> getAllServiceClasses() throws GatewayException {
        List<ServiceClassModel> list = serviceClassesProxy.getAllServiceClasses();
        return list;
    }

    public void updateServiceClasses(ServiceClassRequest request) throws GatewayException {
        CCATLogger.DEBUG_LOGGER.debug("Started successfully.");

        if (request.getCurrentServiceClass().getCode().equals(request.getNewServiceClass().getCode())) {
            CCATLogger.DEBUG_LOGGER.debug("Source Class code is same destination.");
            throw new GatewayException(ErrorCodes.ERROR.SOURCE_SAME_DESTINATION);
        }
        if (!request.getCurrentServiceClass().getIsAllowedMigration()) {
            CCATLogger.DEBUG_LOGGER.debug("Current Service Class Does not allow migration.");
            throw new GatewayException(ErrorCodes.ERROR.SC_NOT_MIGRATABLE);
        }

        CCATLogger.DEBUG_LOGGER.debug("canMigrate with serviceClassId [ " + request.getNewServiceClass().getCode() + " ]");
        if (request.getCurrentServiceClass().getIsCiConversion()) {
            //call ci-service
            ServiceClassConversionRequest serviceClassConversionRequest = (ServiceClassConversionRequest) iServiceClassConversionMapper.mapTo(request);
            serviceClassesProxy.serviceClassConversion(serviceClassConversionRequest);
            CCATLogger.DEBUG_LOGGER.debug("Ended successfully.");
        } else {
            //call air-service
            UpdateServiceClassRequest updateServiceClassRequest = (UpdateServiceClassRequest) iUpdateServiceRequestMapper.mapTo(request);
            serviceClassesProxy.updateServiceClass(updateServiceClassRequest);
            CCATLogger.DEBUG_LOGGER.debug("Ended successfully.");
        }

    }
}
