package com.asset.ccat.air_service.controllers;

import com.asset.ccat.air_service.defines.Defines;
import com.asset.ccat.air_service.defines.ErrorCodes;
import com.asset.ccat.air_service.exceptions.AIRException;
import com.asset.ccat.air_service.exceptions.AIRServiceException;
import com.asset.ccat.air_service.logger.CCATLogger;
import com.asset.ccat.air_service.models.DedicatedAccount;
import com.asset.ccat.air_service.models.requests.GetDedicatedAccountsRequest;
import com.asset.ccat.air_service.models.responses.BaseResponse;
import com.asset.ccat.air_service.models.shared.ServiceInfo;
import com.asset.ccat.air_service.services.GetBalanceAndDateService;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.List;

/**
 * @author Mahmoud Shehab
 */
@RestController
public class GetDedicatedAccountsController {

    @Autowired
    Environment environment;

    @Autowired
    GetBalanceAndDateService getBalanceAndDateService;

    @RequestMapping(value = Defines.ContextPaths.BALANCE_AND_DATE + Defines.WEB_ACTIONS.GET, method = RequestMethod.POST)
    public BaseResponse<List<DedicatedAccount>> getDedicatedAccounts(HttpServletRequest req, @RequestBody GetDedicatedAccountsRequest request) throws AuthenticationException, AIRServiceException, AIRException, Exception {
        ThreadContext.put("sessionId", request.getSessionId());
        ThreadContext.put("requestId", request.getRequestId());
        CCATLogger.DEBUG_LOGGER.info("Received Delete FAFList Request [" + request + "]");
        List<DedicatedAccount> response = getBalanceAndDateService.getBalanceAndDate(request);
        CCATLogger.DEBUG_LOGGER.info("IP => " + InetAddress.getLocalHost().getHostAddress() + environment.getProperty("server.port"));
        CCATLogger.DEBUG_LOGGER.info("Finished Serving Get Dedicated Accounts Request Successfully");

        return new BaseResponse<>(ErrorCodes.SUCCESS.SUCCESS,
                "success", 0,
                new ServiceInfo(InetAddress.getLocalHost().getHostAddress(), environment.getProperty("server.port")),
                response);
    }
}
