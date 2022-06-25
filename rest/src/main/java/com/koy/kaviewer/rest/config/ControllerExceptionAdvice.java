package com.koy.kaviewer.rest.config;

import com.koy.kaviewer.kafka.exception.KaViewerBizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@RestControllerAdvice
public class ControllerExceptionAdvice implements HandlerExceptionResolver {
    private static final String MSG_KEY = "message";
    private static final String STACKTRACE_KEY = "stackTrace";

    @ResponseBody
    @ExceptionHandler
    ResponseEntity<Map<String, Object>> exceptionHandler(KaViewerBizException kaViewerBizException) {
        return new ResponseEntity<>(buildResponse(kaViewerBizException.getMessage(), kaViewerBizException.getCause()), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        final MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
        final ModelAndView modelAndView = new ModelAndView(mappingJackson2JsonView);
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        if (e instanceof KaViewerBizException) {
            KaViewerBizException kaViewerBizException = (KaViewerBizException) e;
            modelAndView.setStatus(HttpStatus.BAD_REQUEST);
            modelAndView.addAllObjects(buildResponse(kaViewerBizException.getErrorMsg().getMsg(), e));
            return modelAndView;
        }
        modelAndView.addAllObjects(buildResponse(e.getMessage(), e));
        return modelAndView;

    }

    private Map<String, Object> buildResponse(String message, Throwable e) {
        final Throwable cause = e.getCause();
        return new TreeMap<>(Map.of(MSG_KEY, StringUtils.isEmpty(message) ? e.getClass().getName() : message,
                STACKTRACE_KEY, (Objects.isNull(cause) ?
                        e.getStackTrace()[0].toString() : cause.getStackTrace()[0].toString())));
    }

}
