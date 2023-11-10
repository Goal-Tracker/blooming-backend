package com.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GlobalExceptionHandler {

    private static final String LOG_MESSAGE_FORMAT = "%s : %s";
    public static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "javax.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";

    public void errorPage404(HttpServletRequest request){
        log.info("error 404");
        printErrorInfo(request);
    }

    public void errorPage500(HttpServletRequest request){
        log.info("error 500");
        printErrorInfo(request);
    }

    private void printErrorInfo(HttpServletRequest request){
        log.info("ERROR_EXCEPTION = {}", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE = {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE = {}", request.getAttribute(ERROR_MESSAGE));
        log.info("ERROR_REQUEST_URI = {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME = {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE = {}", request.getAttribute(ERROR_STATUS_CODE));
        log.info("dispatcherType = {}", request.getDispatcherType());
    }
}
