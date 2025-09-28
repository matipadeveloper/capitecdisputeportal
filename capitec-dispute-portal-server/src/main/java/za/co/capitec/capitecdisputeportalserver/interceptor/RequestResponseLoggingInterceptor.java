package za.co.capitec.capitecdisputeportalserver.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.time.LocalDateTime;
import java.util.Collections;

@Component
public class RequestResponseLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            logger.info("=== INCOMING REQUEST ===");
            logger.info("Timestamp: {}", LocalDateTime.now());
            logger.info("Method: {}", request.getMethod());
            logger.info("URI: {}", request.getRequestURI());
            logger.info("Controller: {}", handlerMethod.getBeanType().getSimpleName());
            logger.info("Handler Method: {}", handlerMethod.getMethod().getName());

            logger.info("Headers: {}", Collections.list(request.getHeaderNames()).stream()
                .filter(header -> !header.toLowerCase().contains("authorization") &&
                               !header.toLowerCase().contains("cookie"))
                .collect(StringBuilder::new,
                        (sb, header) -> sb.append(header).append("=").append(request.getHeader(header)).append(", "),
                        StringBuilder::append)
                .toString());

            if (request.getQueryString() != null) {
                logger.info("Query Parameters: {}", request.getQueryString());
            }

            if (!"GET".equals(request.getMethod()) && request instanceof ContentCachingRequestWrapper) {
                ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
                byte[] content = wrapper.getContentAsByteArray();
                if (content.length > 0) {
                    try {
                        String body = new String(content, request.getCharacterEncoding() != null ?
                                               request.getCharacterEncoding() : "UTF-8");
                        logger.info("Request Body: {}", body);
                    } catch (Exception e) {
                        logger.warn("Could not log request body: {}", e.getMessage());
                    }
                }
            }
            logger.info("=======================");
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            logger.info("=== OUTGOING RESPONSE ===");
            logger.info("Timestamp: {}", LocalDateTime.now());
            logger.info("Method: {}", request.getMethod());
            logger.info("URI: {}", request.getRequestURI());
            logger.info("Controller: {}", handlerMethod.getBeanType().getSimpleName());
            logger.info("Handler Method: {}", handlerMethod.getMethod().getName());
            logger.info("Status Code: {}", response.getStatus());

            if (response instanceof ContentCachingResponseWrapper) {
                ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;
                byte[] content = wrapper.getContentAsByteArray();
                if (content.length > 0) {
                    try {
                        String body = new String(content, response.getCharacterEncoding() != null ?
                                               response.getCharacterEncoding() : "UTF-8");
                        logger.info("Response Body: {}", body);
                    } catch (Exception e) {
                        logger.warn("Could not log response body: {}", e.getMessage());
                    }
                }
            }

            if (ex != null) {
                logger.error("Exception occurred: {}", ex.getMessage(), ex);
            }

            logger.info("========================");
        }
    }
}
