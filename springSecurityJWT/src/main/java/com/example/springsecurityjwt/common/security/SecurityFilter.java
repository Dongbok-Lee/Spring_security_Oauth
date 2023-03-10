package com.example.springsecurityjwt.common.security;

import com.example.springsecurityjwt.common.exception.BadRequestException;
import com.example.springsecurityjwt.enumType.AuthProvider;
import com.example.springsecurityjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain){
        log.debug("**** SECURITY FILTER START ****");

        try{
            String authorizationHeader = request.getHeader("Authorization");
            String token;
            String userId;
            String provider;

            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                token = authorizationHeader.substring(7);

                if(securityUtil.isExpiration(token))
                    throw new BadRequestException("Expired_ACCESS_TOKEN");

                userId = (String) securityUtil.get(token).get("userId");
                provider = (String) securityUtil.get(token).get("provider");

                if(!userRepository.existsByIdAndAuthProvider(userId, AuthProvider.findByCode(provider)))
                    throw new BadRequestException("CANNOT_FOUND_USER");
            }

            filterChain.doFilter(request, response);
        }catch(BadRequestException e){
            if(e.getMessage().equalsIgnoreCase("EXPIRED_ACCESS_TOKEN")){
                writeErrorLogs("EXPIRED_ACCESS_TOKEN", e.getMessage(), e.getStackTrace());
                JSONObject jsonObject = createJsonError(String.valueOf(UNAUTHORIZED.value()), e.getMessage());
                setJsonResponse(response, UNAUTHORIZED, jsonObject.toString());
            }else if(e.getMessage().equalsIgnoreCase("CANNOT_FOUND_USER")){
                writeErrorLogs("CANNOT_FOUND_USER", e.getMessage(), e.getStackTrace());
                JSONObject jsonObject = createJsonError(String.valueOf(UNAUTHORIZED.value()), e.getMessage());
                setJsonResponse(response, UNAUTHORIZED, jsonObject.toString());
            }
        }catch(Exception e){
            writeErrorLogs("Exception", e.getMessage(), e.getStackTrace());

            if(response.getStatus() == HttpStatus.OK.value())
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } finally{
            log.debug("**** SECURITY FILTER FINISH ****");
        }


    }

    private JSONObject createJsonError(String errorCode, String errorMessage){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("errorCode", errorCode);
            jsonObject.put("errorMessage", errorMessage);
        }catch(JSONException ex){
            writeErrorLogs("JSONException", ex.getMessage(), ex.getStackTrace());
        }

        return jsonObject;
    }

    private void setJsonResponse(HttpServletResponse response, HttpStatus httpStatus, String jsonValue){
        response.setStatus(httpStatus.value());
        response.setContentType(APPLICATION_JSON_VALUE);

        try{
            response.getWriter().write(jsonValue);
            response.getWriter().flush();
            response.getWriter().close();
        }catch(IOException ex){
            writeErrorLogs("IOException", ex.getMessage(), ex.getStackTrace());
        }
    }

    private void writeErrorLogs(String exception, String message, StackTraceElement[] stackTraceElements){
        log.error("**** " + exception + " ****");
        log.error("**** error message : " + message);
        log.error("**** stack trace :" + Arrays.toString(stackTraceElements));
    }
}
