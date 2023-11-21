package com.asset.ccat.air_service.services;

import com.asset.ccat.air_service.cache.AIRRequestsCache;
import com.asset.ccat.air_service.configurations.Properties;
import com.asset.ccat.air_service.defines.AIRDefines;
import com.asset.ccat.air_service.defines.ErrorCodes;
import com.asset.ccat.air_service.exceptions.AIRException;
import com.asset.ccat.air_service.exceptions.AIRServiceException;
import com.asset.ccat.air_service.logger.CCATLogger;
import com.asset.ccat.air_service.mappers.UpdateServiceClassMapper;
import com.asset.ccat.air_service.models.requests.UpdateServiceClassRequest;
import com.asset.ccat.air_service.parser.AIRParser;
import com.asset.ccat.air_service.proxy.AIRProxy;
import com.asset.ccat.air_service.utils.AIRUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author wael.mohamed
 */
@Service
public class ServiceClassService {

    @Autowired
    Properties properties;
    @Autowired
    AIRRequestsCache aIRRequestsCache;
    @Autowired
    AIRProxy aIRProxy;
    @Autowired
    AIRUtils aIRUtils;
    @Autowired
    AIRParser aIRParser;
    @Autowired
    UpdateServiceClassMapper updateServiceClassMapper;


    public void updateServiceClass(UpdateServiceClassRequest serviceClassRequest) throws AIRServiceException, AIRException {
        try {
            String xmlRequest = aIRRequestsCache.getAirRequestsCache().get(AIRDefines.AIR_COMMAND_KEY.UPDATE_SERVICE_CLASS);
            xmlRequest = xmlRequest.replace(AIRDefines.UPDATE_SERVICE_CLASSES.SERVICE_CLASSE_NEW, serviceClassRequest.getNewServiceClassId().toString());
            xmlRequest = xmlRequest.replace(AIRDefines.UPDATE_SERVICE_CLASSES.SERVICE_CLASSE_CURRENT, serviceClassRequest.getCurrentServiceClassId().toString());
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.SUBSCRIBER_NUMBER, serviceClassRequest.getMsisdn());
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.ORIGIN_TRANSACTION_ID, "1");
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.ORIGIN_TIME_STAMP, aIRUtils.getCurrentFormattedDate());
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.ORIGIN_OPERATOR_ID, serviceClassRequest.getUsername().toLowerCase());
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.ORIGIN_NODE_TYPE, properties.getOriginNodeType());
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.ORIGIN_HOST_NAME, properties.getOriginHostName());
            CCATLogger.DEBUG_LOGGER.debug(" AIR serviceClass language request is " + xmlRequest);
            String result = aIRProxy.sendAIRRequest(xmlRequest);
            CCATLogger.DEBUG_LOGGER.debug(" AIR update serviceClass response is " + result);
            HashMap resultMap = aIRParser.parse(result);
            updateServiceClassMapper.map(serviceClassRequest.getMsisdn(), resultMap);
            CCATLogger.DEBUG_LOGGER.debug("ServiceClassService -> updateServiceClass() Ended successfully.");
        } catch (AIRServiceException | AIRException ex) {
            CCATLogger.DEBUG_LOGGER.debug("ServiceClassService -> updateServiceClass() Ended with AIRException.");
            throw ex;
        } catch (IOException | SAXException ex) {
            CCATLogger.DEBUG_LOGGER.debug("ServiceClassService -> updateServiceClass() Ended with IOException | SAXException.");
            CCATLogger.DEBUG_LOGGER.info(" Error while parsing response " + ex);
            CCATLogger.ERROR_LOGGER.error(" Error while parsing response ", ex);
            throw new AIRServiceException(ErrorCodes.ERROR.ERROR_PARSING_RESPONSE);
        } catch (Exception ex) {
            CCATLogger.DEBUG_LOGGER.debug("ServiceClassService -> updateServiceClass() Ended with Exception.");
            CCATLogger.DEBUG_LOGGER.error("Unknown error in updateServiceClass() | ex: [" + ex.getMessage() + "]");
            CCATLogger.ERROR_LOGGER.error("Unknown error in updateServiceClass()", ex);
            throw new AIRServiceException(ErrorCodes.ERROR.UNKNOWN_ERROR);
        }
    }
}
