package com.asset.ccat.air_service.services;

import com.asset.ccat.air_service.cache.AIRRequestsCache;
import com.asset.ccat.air_service.configurations.Properties;
import com.asset.ccat.air_service.defines.AIRDefines;
import com.asset.ccat.air_service.defines.ErrorCodes;
import com.asset.ccat.air_service.exceptions.AIRException;
import com.asset.ccat.air_service.exceptions.AIRServiceException;
import com.asset.ccat.air_service.logger.CCATLogger;
import com.asset.ccat.air_service.mappers.UpdateBalanceAndDateMapper;
import com.asset.ccat.air_service.models.requests.UpdateAccumulatorModel;
import com.asset.ccat.air_service.models.requests.UpdateAccumulatorsRequest;
import com.asset.ccat.air_service.models.requests.UpdateLimitRequest;
import com.asset.ccat.air_service.parser.AIRParser;
import com.asset.ccat.air_service.proxy.AIRProxy;
import com.asset.ccat.air_service.utils.AIRUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author Mahmoud Shehab
 */
@Component
public class UpdateAccumulatorService {

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
    UpdateBalanceAndDateMapper balanceAndDateMapper;
    @Autowired
    UserLimitsService userLimitsService;


    public void updateAccumulators(UpdateAccumulatorsRequest updateAccumulatorsRequest) throws AIRServiceException, AIRException {
        try {
            String normalVal = (String) generateValuesXML(updateAccumulatorsRequest.getList()).get("normal");
            String setVal = (String) generateValuesXML(updateAccumulatorsRequest.getList()).get("set");
            String requestVal = "";
            if (Objects.nonNull(normalVal)) {
                requestVal = normalVal;
            } else if (Objects.nonNull(setVal)) {
                requestVal = setVal;
            } else {
                throw new AIRServiceException(ErrorCodes.ERROR.ERROR_WHILE_PARSING_REQUEST);
            }
            String xmlRequest = aIRRequestsCache.getAirRequestsCache().get(AIRDefines.AIR_COMMAND_KEY.UPDATE_ACCUMULATORS);
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.SUBSCRIBER_NUMBER, updateAccumulatorsRequest.getMsisdn());
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.ORIGIN_OPERATOR_ID, updateAccumulatorsRequest.getUsername().toLowerCase());
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.ORIGIN_TRANSACTION_ID, "1");
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.ORIGIN_TIME_STAMP, aIRUtils.getCurrentFormattedDate());
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.ORIGIN_NODE_TYPE, properties.getOriginNodeType());
            xmlRequest = xmlRequest.replace(AIRDefines.AIR_BASE_PLACEHOLDER.ORIGIN_HOST_NAME, properties.getOriginHostName());
            xmlRequest = xmlRequest.replace(AIRDefines.UPDATE_ACCUMULATORS_PLACEHOLDER.ACCUMULATORS_INFO, requestVal);
            CCATLogger.DEBUG_LOGGER.debug(" AIR update accumulators request is " + xmlRequest);
            String result = aIRProxy.sendAIRRequest(xmlRequest);
            CCATLogger.DEBUG_LOGGER.debug(" AIR update accumulators response is " + result);
            HashMap resultMap = aIRParser.parse(result);
            balanceAndDateMapper.map(updateAccumulatorsRequest.getMsisdn(), resultMap);
            Float totalsAmounts = 0.0f;
            Integer actionType = -1;
            for (UpdateAccumulatorModel account : updateAccumulatorsRequest.getList()) {
                if (Objects.nonNull(account.getAdjustmentAmount()) && Objects.nonNull(account.getAdjustmentMethod())) {
                    totalsAmounts += account.getAdjustmentAmount();
                    actionType = account.getAdjustmentMethod();
                }
            }
            if(actionType!=-1||actionType!=0){
                UpdateLimitRequest updateLimitRequest = new UpdateLimitRequest(updateAccumulatorsRequest.getUserId(),
                        actionType,
                        totalsAmounts,
                        0.0f);
                updateLimitRequest.setToken(updateAccumulatorsRequest.getToken());
                CCATLogger.DEBUG_LOGGER.debug("UpdateAccumulatorService -> updateAccumulators() : Starting Update User Limits ");
                userLimitsService.updateLimits(updateLimitRequest);
                CCATLogger.DEBUG_LOGGER.debug("UpdateAccumulatorService -> updateAccumulators() : Ending Update User Limits ");

            }

            } catch (AIRServiceException | AIRException ex) {
            throw ex;
        } catch (IOException | SAXException ex) {
            CCATLogger.DEBUG_LOGGER.info(" Error while parsing response " + ex);
            CCATLogger.ERROR_LOGGER.error(" Error while parsing response ", ex);
            throw new AIRServiceException(ErrorCodes.ERROR.ERROR_PARSING_RESPONSE);
        } catch (Exception ex) {
            CCATLogger.DEBUG_LOGGER.debug("updateAccumulators() Ended with Exception.");
            CCATLogger.DEBUG_LOGGER.error("Unknown error in updateAccumulators() | ex: [" + ex.getMessage() + "]");
            CCATLogger.ERROR_LOGGER.error("Unknown error in updateAccumulators()", ex);
            throw new AIRServiceException(ErrorCodes.ERROR.UNKNOWN_ERROR);
        }
    }

    private HashMap<String, String> generateValuesXML(List<UpdateAccumulatorModel> accumulators) throws AIRServiceException {
        StringBuilder accumaltorsXML = new StringBuilder();
        StringBuilder accumaltorsSetXML = new StringBuilder();
        HashMap<String, String> map = new HashMap();
        try {
            if (!accumulators.isEmpty()) {
                for (UpdateAccumulatorModel accumulatorModel : accumulators) {
                    if (accumulatorModel.getIsReset()) {
                        accumulatorModel.setAdjustmentAmount(0f);
                        accumulatorModel.setStartDate(new Date());
                        accumulatorModel.setAdjustmentMethod(AIRDefines.UPDATE_BALANCE_SETAMT);
                        accumulatorModel.setIsDateEdited(true);
                        accumaltorsSetXML.append(updateAccumulatorXML(accumulatorModel));
                    } else {
                        accumaltorsXML.append(updateAccumulatorXML(accumulatorModel));
                    }
                }
                if (Objects.nonNull(accumaltorsXML)) {
                    map.put("normal", accumaltorsXML.toString());
                }
                if (Objects.nonNull(accumaltorsSetXML)) {
                    map.put("set", accumaltorsSetXML.toString());
                }
            }
        } catch (Exception e) {
            CCATLogger.DEBUG_LOGGER.debug("Exception in generateValuesXML()");
            CCATLogger.ERROR_LOGGER.error("Exception in generateValuesXML", e);
            throw new AIRServiceException(ErrorCodes.ERROR.ERROR_WHILE_PARSING_REQUEST);
        }
        return map;
    }

    private String updateAccumulatorXML(UpdateAccumulatorModel accumulatorModel) throws AIRServiceException {
        CCATLogger.DEBUG_LOGGER.debug("In updateAccumulatorXML()");
        String accumulatorItem = "";
        try {
            if (Objects.nonNull(accumulatorModel.getIsDateEdited()) && accumulatorModel.getIsDateEdited()
                    && !accumulatorModel.getAdjustmentMethod().equals(0)) {
                accumulatorItem = AIRDefines.AIR_TAGS.TAG_STRUCT_3MEMBERS;
            } else if ((Objects.nonNull(accumulatorModel.getIsDateEdited()) && accumulatorModel.getIsDateEdited())
                    || (!accumulatorModel.getAdjustmentMethod().equals(0))) {
                accumulatorItem = AIRDefines.AIR_TAGS.TAG_STRUCT_2MEMBERS;
            } else {
                CCATLogger.DEBUG_LOGGER.debug("dedicatedAccount " + accumulatorModel.getId() + " skipped");
                return accumulatorItem;
            }
            String accumulatorID = AIRDefines.AIR_TAGS.TAG_MEMBER_INT;
            accumulatorID = accumulatorID.replace(AIRDefines.AIR_TAGS.TAG_MEMBER_KEY, AIRDefines.accumulatorID);
            accumulatorID = accumulatorID.replace(AIRDefines.AIR_TAGS.TAG_MEMBER_VALUE, String.valueOf(accumulatorModel.getId()));
            accumulatorItem = accumulatorItem.replace("$MEMBER_1$", accumulatorID);
            Long value = accumulatorModel.getAdjustmentAmount() == null ? 0 : accumulatorModel.getAdjustmentAmount().longValue();
            if (accumulatorModel.getAdjustmentMethod().equals(AIRDefines.UPDATE_BALANCE_ADD)) {
                String accumulatorValueRelative = AIRDefines.AIR_TAGS.TAG_MEMBER_INT;
                accumulatorValueRelative = accumulatorValueRelative.replace(AIRDefines.AIR_TAGS.TAG_MEMBER_KEY, AIRDefines.accumulatorValueRelative);
                accumulatorValueRelative = accumulatorValueRelative.replace(AIRDefines.AIR_TAGS.TAG_MEMBER_VALUE, value + "");
                accumulatorItem = accumulatorItem.replace("$MEMBER_2$", accumulatorValueRelative);
            } else if (accumulatorModel.getAdjustmentMethod().equals(AIRDefines.UPDATE_BALANCE_SUBTRACT)) {
                String accumulatorValueRelative = AIRDefines.AIR_TAGS.TAG_MEMBER_INT;
                accumulatorValueRelative = accumulatorValueRelative.replace(AIRDefines.AIR_TAGS.TAG_MEMBER_KEY, AIRDefines.accumulatorValueRelative);
                accumulatorValueRelative = accumulatorValueRelative.replace(AIRDefines.AIR_TAGS.TAG_MEMBER_VALUE, "-" + value);
                accumulatorItem = accumulatorItem.replace("$MEMBER_2$", accumulatorValueRelative);
            } else if (accumulatorModel.getAdjustmentMethod().equals(AIRDefines.UPDATE_BALANCE_SETAMT)) {
                String accumulatorValueAbsolute = AIRDefines.AIR_TAGS.TAG_MEMBER_INT;
                accumulatorValueAbsolute = accumulatorValueAbsolute.replace(AIRDefines.AIR_TAGS.TAG_MEMBER_KEY, AIRDefines.accumulatorValueAbsolute);
                accumulatorValueAbsolute = accumulatorValueAbsolute.replace(AIRDefines.AIR_TAGS.TAG_MEMBER_VALUE, value + "");
                accumulatorItem = accumulatorItem.replace("$MEMBER_2$", accumulatorValueAbsolute);
            }
            if (Objects.nonNull(accumulatorModel.getIsDateEdited()) && accumulatorModel.getIsDateEdited()) {
                String accumulatorStartDate = AIRDefines.AIR_TAGS.TAG_MEMBER_DATE;
                accumulatorStartDate = accumulatorStartDate.replace(AIRDefines.AIR_TAGS.TAG_MEMBER_KEY, AIRDefines.accumulatorStartDate);
                accumulatorStartDate = accumulatorStartDate.replace(AIRDefines.AIR_TAGS.TAG_MEMBER_VALUE,
                        aIRUtils.formatNewAIR(accumulatorModel.getStartDate()));
                //Check if date time is zeros, so it will be overrided to set time in order to let AIR success
                if (accumulatorStartDate.substring(9, accumulatorStartDate.length()).equals("00:00:00+0200")) {
                    accumulatorStartDate = accumulatorStartDate.substring(0, 9) + "19:59:16+0200";
                }
                if (!accumulatorModel.getAdjustmentMethod().equals(0)) {
                    accumulatorItem = accumulatorItem.replace("$MEMBER_3$", accumulatorStartDate);
                } else {
                    accumulatorItem = accumulatorItem.replace("$MEMBER_2$", accumulatorStartDate);
                }
            }
            return accumulatorItem;
        } catch (Exception ex) {
            CCATLogger.ERROR_LOGGER.error("Exception in updateAccumulatorXML", ex);
            CCATLogger.DEBUG_LOGGER.debug("Exception in updateAccumulatorXML");
            throw new AIRServiceException(ErrorCodes.ERROR.ERROR_WHILE_PARSING_REQUEST);
        }
    }
}
