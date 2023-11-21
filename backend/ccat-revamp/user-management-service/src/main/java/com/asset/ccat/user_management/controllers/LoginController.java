package com.asset.ccat.user_management.controllers;

import com.asset.ccat.user_management.defines.Defines;
import com.asset.ccat.user_management.defines.ErrorCodes;
import com.asset.ccat.user_management.logger.CCATLogger;
import com.asset.ccat.user_management.models.requests.LoginRequest;
import com.asset.ccat.user_management.models.responses.BaseResponse;
import com.asset.ccat.user_management.models.responses.LoginResponse;
import com.asset.ccat.user_management.models.shared.ServiceInfo;
import com.asset.ccat.user_management.services.UserService;
import java.net.InetAddress;
import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mahmoud Shehab
 */
@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    Environment environment;

    @RequestMapping(value = Defines.ContextPaths.LOGIN, method = RequestMethod.POST)
    public BaseResponse<LoginResponse> userLogin(HttpServletRequest req, @RequestBody LoginRequest loginRequest) throws AuthenticationException, Exception {
        ThreadContext.put("requestId", loginRequest.getRequestId());
        CCATLogger.DEBUG_LOGGER.info("Start login for user " + loginRequest.getUsername());
        LoginResponse response = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        CCATLogger.DEBUG_LOGGER.info("IP => " + InetAddress.getLocalHost().getHostAddress() + ":" + environment.getProperty("server.port"));
        CCATLogger.DEBUG_LOGGER.info("Finished login for user " + loginRequest.getUsername());
        return new BaseResponse<>(ErrorCodes.SUCCESS.SUCCESS,
                "success", 0, new ServiceInfo(InetAddress.getLocalHost().getHostName(), environment.getProperty("server.port")),
                response);
    }
}
