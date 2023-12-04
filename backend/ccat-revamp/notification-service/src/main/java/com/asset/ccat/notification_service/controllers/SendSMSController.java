package com.asset.ccat.notification_service.controllers;


import com.asset.ccat.notification_service.defines.Defines;
import com.asset.ccat.notification_service.defines.ErrorCodes;
import com.asset.ccat.notification_service.exceptions.NotificationException;
import com.asset.ccat.notification_service.logger.CCATLogger;
import com.asset.ccat.notification_service.models.requests.SendSMSRequest;
import com.asset.ccat.notification_service.models.responses.BaseResponse;
import com.asset.ccat.notification_service.services.SendSMSService;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = Defines.ContextPaths.SEND_SMS)
public class SendSMSController {

    private final SendSMSService sendSmsService;

    public SendSMSController(SendSMSService sendSmsService) {
        this.sendSmsService = sendSmsService;
    }

    @PostMapping(value = Defines.WEB_ACTIONS.SEND)
    public BaseResponse<Object> sendSMS(@RequestBody SendSMSRequest sendSMSRequest) throws NotificationException {
        CCATLogger.DEBUG_LOGGER.debug("SendSMSController -> sendSMS() : Started");

        ThreadContext.put("requestId", sendSMSRequest.getRequestId());
        ThreadContext.put("sessionId", sendSMSRequest.getSessionId());
        sendSmsService.sendSMS(sendSMSRequest);
        return new BaseResponse<>(ErrorCodes.SUCCESS.SUCCESS,
                "success", Defines.SEVERITY.CLEAR, null);

    }

}
