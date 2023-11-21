/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.ccat.air_service.services;

import com.asset.ccat.air_service.cache.AIRRequestsCache;
import com.asset.ccat.air_service.configurations.Properties;
import com.asset.ccat.air_service.defines.AIRDefines;
import com.asset.ccat.air_service.defines.ErrorCodes;
import com.asset.ccat.air_service.exceptions.AIRException;
import com.asset.ccat.air_service.exceptions.AIRServiceException;
import com.asset.ccat.air_service.logger.CCATLogger;
import com.asset.ccat.air_service.mappers.GetBalanceAndDateMapper;
import com.asset.ccat.air_service.models.DedicatedAccount;
import com.asset.ccat.air_service.models.SubscriberAccountModel;
import com.asset.ccat.air_service.models.requests.GetDedicatedAccountsRequest;
import com.asset.ccat.air_service.parser.AIRParser;
import com.asset.ccat.air_service.proxy.AIRProxy;
import com.asset.ccat.air_service.utils.AIRUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author Mahmoud Shehab
 */
@Component
public class GetBalanceAndDateService {

    @Autowired
    AIRRequestsCache aIRRequestsCache;
    @Autowired
    AIRProxy aIRProxy;
    @Autowired
    AIRUtils aIRUtils;
    @Autowired
    AIRParser aIRParser;
    @Autowired
    GetBalanceAndDateMapper getBalanceAndDateMapper;
    @Autowired
    LookupsService lookupsService;
    @Autowired
    GetAccountDetailsService getAccountDetailsService;
    @Autowired
    Properties properties;

    public List<DedicatedAccount> getBalanceAndDate(GetDedicatedAccountsRequest request) throws AIRServiceException, AIRException {
        try {
            String xmlRequest = aIRRequestsCache.getAirRequestsCache().get(AIRDefines.AIR_COMMAND_KEY.GET_BALANCE_AND_DATE);
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.SUBSCRIBER_NUMBER, request.getMsisdn());
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.ORIGIN_OPERATOR_ID, request.getUsername().toLowerCase());
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.ORIGIN_NODE_TYPE, properties.getOriginNodeType());
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.ORIGIN_HOST_NAME, properties.getOriginHostName());
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.ORIGIN_TRANSACTION_ID, "1");
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.ORIGIN_TIME_STAMP, aIRUtils.getCurrentFormattedDate());
            CCATLogger.DEBUG_LOGGER.debug(" AIR getBalanceAndDate request is " + xmlRequest);
            String result = aIRProxy.sendAIRRequest(xmlRequest);
            CCATLogger.DEBUG_LOGGER.debug(" AIR getBalanceAndDate response is " + result);
            HashMap resultMap = aIRParser.parse(result);
            List<DedicatedAccount> list = getBalanceAndDateMapper.map(request.getMsisdn(), resultMap);
            calculateUnits(list, request.getAccountModel());
            return list;
        } catch (AIRServiceException | AIRException ex) {
            throw ex;
        } catch (SAXException | IOException ex) {
            CCATLogger.DEBUG_LOGGER.error("Failed to parse air response | ex: [" + ex.getMessage() + "]");
            CCATLogger.ERROR_LOGGER.error("Failed to parse air response", ex);
            throw new AIRServiceException(ErrorCodes.ERROR.ERROR_PARSING_RESPONSE);
        } catch (Exception ex) {
            CCATLogger.DEBUG_LOGGER.error("Unknown error in getBalanceAndDate() | ex: [" + ex.getMessage() + "]");
            CCATLogger.ERROR_LOGGER.error("Unknown error in getBalanceAndDate()", ex);
            throw new AIRServiceException(ErrorCodes.ERROR.UNKNOWN_ERROR);
        }
    }

    private void calculateUnits(List<DedicatedAccount> dedicatedAccountList, SubscriberAccountModel accountModel) throws AIRServiceException {
        HashMap<Integer, HashMap<Integer, DedicatedAccount>> cachedDedicatedAccountsMap = lookupsService.getDedicatedAccountsMap();
        HashMap<Integer, DedicatedAccount> serviceClassDas = cachedDedicatedAccountsMap.get(accountModel.getServiceClass().getCode());
        if (Objects.isNull(serviceClassDas)) {
            return;
        }
        for (DedicatedAccount da : dedicatedAccountList) {
            DedicatedAccount cachedDa = serviceClassDas.get(Integer.valueOf(da.getId()));
            if (Objects.nonNull(cachedDa)
                    && Objects.nonNull(cachedDa.getRatingFactor())
                    && cachedDa.getRatingFactor() > 0.0F) {
                String units = Float.toString(da.getBalance() / cachedDa.getRatingFactor());
                da.setUnits(units);
                da.setDescription(cachedDa.getDescription());
            }
        }
    }
}
