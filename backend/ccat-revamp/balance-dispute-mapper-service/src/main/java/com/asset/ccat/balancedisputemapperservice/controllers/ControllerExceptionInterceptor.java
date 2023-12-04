package com.asset.ccat.balancedisputemapperservice.controllers;

import com.asset.ccat.balancedisputemapperservice.cache.MessagesCache;
import com.asset.ccat.balancedisputemapperservice.defines.Defines;
import com.asset.ccat.balancedisputemapperservice.defines.ErrorCodes;
import com.asset.ccat.balancedisputemapperservice.exceptions.BalanceDisputeException;
import com.asset.ccat.balancedisputemapperservice.loggers.CCATLogger;
import com.asset.ccat.balancedisputemapperservice.models.response.BaseResponse;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Assem.Hassan
 */
@ControllerAdvice
@RestController
public class ControllerExceptionInterceptor extends ResponseEntityExceptionHandler {

  @Autowired
  ApplicationContext applicationContext;

  @Autowired
  MessagesCache messagesCache;

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<BaseResponse> handelAllExceptions(Exception ex, WebRequest req) {
    CCATLogger.DEBUG_LOGGER.error(" An error has occurred exc: " + ex.getMessage());
    CCATLogger.ERROR_LOGGER.error(" An error has occurred and the error code message: ", ex);
    String requestId = ThreadContext.get("requestId");
    BaseResponse<String> response = new BaseResponse<>();
    response.setStatusCode(ErrorCodes.ERROR.UNKNOWN_ERROR);
    response.setStatusMessage(messagesCache.getErrorMsg(ErrorCodes.ERROR.UNKNOWN_ERROR));
    response.setSeverity(Defines.SEVERITY.FATAL);
    response.setRequestId(requestId);
    CCATLogger.DEBUG_LOGGER.debug("Api Response is " + response);
    ThreadContext.remove("transactionId");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }


  @ExceptionHandler(BalanceDisputeException.class)
  public final ResponseEntity<BaseResponse> handleConfigServerException(BalanceDisputeException ex,
      WebRequest req) {
    CCATLogger.DEBUG_LOGGER.error(" An error has occurred exc: " + ex.getMessage());
    CCATLogger.ERROR_LOGGER.error(" An error has occurred and the error code message: ", ex);
    CCATLogger.DEBUG_LOGGER.debug("create Api Response");
    String requestId = ThreadContext.get("requestId");
    BaseResponse<String> response = new BaseResponse<>();
    response.setRequestId(requestId);
    response.setStatusCode(ex.getErrorCode());
    String msg = "";
    if (ex.getMessage() == null && ex.getArgs() != null && ex.getArgs().length > 0) {
      msg = messagesCache.getErrorMsg(ex.getErrorCode());
      msg = messagesCache.replaceArgument(msg, ex.getArgs());
    } else if (ex.getMessage() != null) {
      msg = ex.getMessage();
    } else {
      msg = messagesCache.getErrorMsg(ex.getErrorCode());
    }
    response.setStatusMessage(msg);
    response.setSeverity(Defines.SEVERITY.ERROR);
    ThreadContext.remove("transactionId");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
